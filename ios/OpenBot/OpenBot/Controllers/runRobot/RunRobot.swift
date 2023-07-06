//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import UIKit
import AVFoundation

class runRobot: CameraController {
    @IBOutlet weak var stopRobot: UIButton!
    @IBOutlet weak var commandMessage: UILabel!
    let bluetooth = bluetoothDataController.shared
    private var bufferHeight = 0
    private var bufferWidth = 0
    private var result: Control?
    var detector: Detector?
    var autopilot: Autopilot?;
    private let inferenceQueue = DispatchQueue(label: "openbot.runRobot.inferencequeue")
    private var isInferenceQueueBusy = false
    var vehicleControl: Control = Control()
    public var MINIMUM_CONFIDENCE_TF_OD_API: Float = 0.5
    private var useDynamicSpeed: Bool = false
    static var isObjectTracking: Bool = false
    static var isAutopilot: Bool = false;

    /**
      override function calls when view of controller loaded
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        stopRobot.backgroundColor = Colors.title;
        stopRobot.setTitle("Stop Car", for: .normal);
        stopRobot.addTarget(self, action: #selector(cancel), for: .touchUpInside);
        stopRobot.layer.cornerRadius = 10;
        NotificationCenter.default.addObserver(self, selector: #selector(updateCommandMsg), name: .commandName, object: nil);
        setupNavigationBarItem()
        updateConstraints();
        let modelItems = Common.loadAllModelItemsFromBundle()
        if (modelItems.count > 0) {
            let detectorModel = modelItems.first(where: { $0.type == TYPE.DETECTOR.rawValue })
            detector = try! Detector.create(model: Model.fromModelItem(item: detectorModel ?? modelItems[0]), device: RuntimeDevice.CPU, numThreads: 1) as? Detector
            let autopilotModel = modelItems.first(where: { $0.type == TYPE.AUTOPILOT.rawValue });
            autopilot = Autopilot(model: Model.fromModelItem(item: autopilotModel ?? modelItems[0]), device: RuntimeDevice.CPU, numThreads: 1);
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }

    /**
     override function calls after current controller view disappear
     Here we stopping the car, removing all notifications

     - Parameter animated:
     */
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        NotificationCenter.default.post(name: .cancelThread, object: nil)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        stopCar();
        NotificationCenter.default.removeObserver(self);
    }

    /**
     updating current commands
     - Parameter notification:
     */
    var count: Int = 0;

    @objc func updateCommandMsg(_ notification: Notification) {
        DispatchQueue.main.async {
            self.detector?.setSelectedClass(newClass: notification.object as! String)
            let message = notification.object as! String
            // Update UI periodically to show all the received messages
            if self.count > 1 {
                return;
            }
            self.count = self.count + 1;
            DispatchQueue.main.async {
                self.commandMessage.text = message;
            }
        }
    }


    @IBOutlet weak var runRobotConstraints: NSLayoutConstraint!
    let factor = 0.8;

    /**
     override function calls when the current orientation changed
     - Parameters:
       - size:
       - coordinator:
     */
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        updateConstraints();
    }

    /**
     Function calls on cancel button of screen tapped. This stop the execution of blockly code
     */
    @objc func cancel() {
        NotificationCenter.default.post(name: .cancelThread, object: nil);
        NotificationCenter.default.post(name: .commandName, object: "\(Strings.cancel)ed");
        stopCar()
    }

    /**
     Function to setup the navigation bar
     */
    func setupNavigationBarItem() {
        if UIImage(named: "back") != nil {
            let backNavigationIcon = (UIImage(named: "back")?.withRenderingMode(.alwaysOriginal))!
            let newBackButton = UIBarButtonItem(image: backNavigationIcon, title: "Playground", target: self, action: #selector(back(sender:)), titleColor: Colors.navigationColor ?? .white)
            navigationItem.leftBarButtonItem = newBackButton
        }
    }

    /**
     Function to remove current viewController from navigation stack
     - Parameter sender:
     */
    @objc func back(sender: UIBarButtonItem) {
        _ = navigationController?.popViewController(animated: true)
        stopCar();
    }


    /**
     Function to update the constraints of image
     */
    fileprivate func updateConstraints() {
        if currentOrientation == .portrait {
            runRobotConstraints.constant = 0;
        } else {
            runRobotConstraints.constant = -60;
        }
    }

    func stopCar() {
        runRobot.isObjectTracking = false
        runRobot.isAutopilot = false
        bluetooth.sendDataFromJs(payloadData: "c" + String(0) + "," + String(0) + "\n");
        bluetooth.sendDataFromJs(payloadData: "l" + String(0) + "," + String(0) + "\n");
        let indicatorValues = "i0,0\n";
        bluetooth.sendDataFromJs(payloadData: indicatorValues)
    }

    func sendControl(control: Control) {
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
            let left = control.getLeft() * gameController.selectedSpeedMode.rawValue
            let right = control.getRight() * gameController.selectedSpeedMode.rawValue
            vehicleControl = control
            print("send data to bluetooth", control.getLeft(), " ", control.getRight());
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n")
            NotificationCenter.default.post(name: .updateSpeedLabel, object: String(Int(left)) + "," + String(Int(right)))
            NotificationCenter.default.post(name: .updateRpmLabel, object: String(Int(control.getLeft())) + "," + String(Int(control.getRight())))
        }
    }

    func updateTarget(_ detection: CGRect) -> Control {

        // Left and right wheels control values
        var left: Float = 0.0
        var right: Float = 0.0

        let frameWidth = UIScreen.main.bounds.size.width
        let frameHeight = UIScreen.main.bounds.size.height

        let dx: CGFloat = CGFloat(detector!.getImageSizeX()) / frameWidth
        let dy: CGFloat = CGFloat(detector!.getImageSizeY()) / frameHeight
        let trackedPos = detection.applying(CGAffineTransform(scaleX: dx, y: dy))

        // Calculate track box area for distance estimate
        let boxArea: Float = Float(trackedPos.height * trackedPos.width)

        // Make sure object center is in frame
        var centerX: Float = Float(trackedPos.midX)
        centerX = max(0, min(centerX, Float(frameWidth)))

        // Scale relative position along x-axis between -1 and 1
        let x_pos_norm: Float = 1.0 - 2.0 * centerX / Float(frameWidth)

        // Generate vehicle controls
        if (x_pos_norm < 0.0) {
            left = 1.0
            right = 1.0 + x_pos_norm
        } else {
            left = 1 - x_pos_norm
            right = 1.0
        }

        // Adjust speed depending on size of detected object bounding box
        if (useDynamicSpeed) {
            var scaleFactor: Float = 1.0 - boxArea / Float(frameWidth * frameHeight)
            scaleFactor = scaleFactor > 0.75 ? 1.0 : scaleFactor // tracked object far, full speed
            // apply scale factor if tracked object is not too near, otherwise stop
            if (scaleFactor > 0.25) {
                left *= scaleFactor
                right *= scaleFactor
            } else {
                left = 0.0
                right = 0.0
            }
        }

        return Control(left: left, right: right)
    }

    override func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
        print("outside of capture",runRobot.isAutopilot , runRobot.isObjectTracking);
        if (runRobot.isObjectTracking || runRobot.isAutopilot) {
            let pixelBuffer: CVPixelBuffer? = CMSampleBufferGetImageBuffer(sampleBuffer)
            print("inside capture ")
            bufferWidth = CVPixelBufferGetWidth(pixelBuffer!)
            bufferHeight = CVPixelBufferGetHeight(pixelBuffer!)
            guard let imagePixelBuffer = pixelBuffer else {
                debugPrint("unable to get image from sample buffer")
                return
            }
            guard !isInferenceQueueBusy else {
                return
            }

            inferenceQueue.async {
                if runRobot.isObjectTracking {
                    self.runObjectTracking(imagePixelBuffer: imagePixelBuffer);
                } else {
                    self.runAutopilot(imagePixelBuffer: imagePixelBuffer);
                }
            }

        }
    }

    static func enableObjectTracking() {
        print("inside test");
        DispatchQueue.main.async {
            runRobot.isObjectTracking = true;
        }
    }

    static func enableAutopilot() {
        print("inside enableAutopilot")
        DispatchQueue.main.async {
            runRobot.isAutopilot = true;
        }
    }


    func runObjectTracking(imagePixelBuffer: CVPixelBuffer) {
        self.isInferenceQueueBusy = true
        let res = self.detector?.recognizeImage(pixelBuffer: imagePixelBuffer);
        if res != nil {
            if (res!.count > 0) {
                self.result = self.updateTarget(res!.first!.getLocation())
            } else {
                self.result = Control(left: 0, right: 0)
            }
            guard let controlResult = self.result else {
                return
            }

            DispatchQueue.main.async {
                if (res!.count > 0) {
                    for item in res! {
                        if (item.getConfidence() > self.MINIMUM_CONFIDENCE_TF_OD_API) {
                            print("confidence is :=============", item.getConfidence());
                            self.sendControl(control: controlResult)
                        } else {
                            self.sendControl(control: Control())
                        }
                    }
                }
            }
        }
        self.isInferenceQueueBusy = false
    }

    func runAutopilot(imagePixelBuffer: CVPixelBuffer) {
        self.isInferenceQueueBusy = true
        let startTime = Date().millisecondsSince1970
        self.result = self.autopilot?.recognizeImage(pixelBuffer: imagePixelBuffer, indicator: 0) ?? Control();
        guard let controlResult = self.result else {
            return
        }
        DispatchQueue.main.async {
            self.sendControl(control: controlResult)
        }
        sendControl(control: controlResult)
        isInferenceQueueBusy = false
    }
}

