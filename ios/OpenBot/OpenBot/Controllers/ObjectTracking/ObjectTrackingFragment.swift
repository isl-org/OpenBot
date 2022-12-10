//
// Created by Sparsh Jain on 27/10/22.
//

import Foundation
import UIKit

class ObjectTrackingFragment: CameraController {
    var objectTrackingSettings: ObjectTrackingSettings?;
    var numberOfThreads: Int = 1
    var bottomAnchorConstraint: NSLayoutConstraint!
    var trailingAnchorConstraint: NSLayoutConstraint!
    var detector: Detector?
    var models: [Model] = [];
    var autoMode: Bool = false;
    var vehicleControl: Control = Control();
    let bluetooth = bluetoothDataController.shared;
    var currentModel: ModelItem!
    var currentDevice: RuntimeDevice = RuntimeDevice.CPU
    private var MINIMUM_CONFIDENCE_TF_OD_API: Float = 50.0;
    private var TIMER_OF_MODEL: Float = 0.03

    override func viewDidLoad() {
        let modelItems = Common.loadAllModelItemsFromBundle()
        if (modelItems.count > 0) {
            let model = modelItems.first(where: { $0.type == TYPE.DETECTOR.rawValue })
            currentModel = model
            detector = try! Detector.create(model: Model.fromModelItem(item: model ?? modelItems[0]), device: RuntimeDevice.CPU, numThreads: numberOfThreads) as? Detector;
        }
        if currentOrientation == .portrait {
            objectTrackingSettings = ObjectTrackingSettings(frame: CGRect(x: 0, y: height - 375, width: width, height: 375), detector: detector, model: currentModel);
        } else {
            objectTrackingSettings = ObjectTrackingSettings(frame: CGRect(x: height - 375, y: 30, width: width, height: 375), detector: detector, model: currentModel);
        }
        objectTrackingSettings!.backgroundColor = Colors.freeRoamButtonsColor
        objectTrackingSettings!.layer.cornerRadius = 5
        createCameraView()
        view.addSubview(objectTrackingSettings!)
        bottomAnchorConstraint = objectTrackingSettings!.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -5);
        trailingAnchorConstraint = objectTrackingSettings!.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: 0)
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDevice), name: .updateDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateThread), name: .updateThread, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConfidence), name: .updateConfidence, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(toggleAutoMode), name: .autoModeObjectTracking, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateModel), name: .updateModel, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateSelectedObject), name: .updateObject, object: nil)
        setupNavigationBarItem()
        super.viewDidLoad()
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        calculateFrame()
    }

    func calculateFrame() {
        if currentOrientation == .portrait || currentOrientation == .portraitUpsideDown {
            objectTrackingSettings?.frame.origin.x = 0;
            objectTrackingSettings?.frame.origin.y = height - 375;
        } else {
            objectTrackingSettings?.frame.origin.x = height - 375;
            objectTrackingSettings?.frame.origin.y = 30;

        }
    }

    @objc func openBluetoothSettings() {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: Strings.bluetoothScreen))
        navigationController?.pushViewController(nextViewController!, animated: true)
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        switch objectTrackingSettings?.autoModeButton.isOn {
        case false:
            autoMode = false;
        case true:
            autoMode = false;
            toggleAutoMode()
        case .none:
            autoMode = false;
        case .some(_):
            autoMode = false;
        }
    }

    @objc func switchCamera() {
        switchCameraView();
    }

    @objc func updateDevice(_ notification: Notification) throws {
        currentDevice = RuntimeDevice(rawValue: notification.object as! String) ?? RuntimeDevice.CPU
        detector = try! Detector.create(model: Model.fromModelItem(item: currentModel), device: currentDevice, numThreads: numberOfThreads) as? Detector;
        currentDevice.rawValue == RuntimeDevice.GPU.rawValue ? NotificationCenter.default.post(name: .updateThreadLabel, object: "N/A") : NotificationCenter.default.post(name: .updateThreadLabel, object: String(numberOfThreads))
        detector?.tfliteOptions.threadCount = numberOfThreads
    }

    @objc func updateThread(_ notification: Notification) {
        let threadCount = notification.object as! String
        numberOfThreads = Int(threadCount) ?? 1
        detector?.tfliteOptions.threadCount = numberOfThreads
    }

    @objc func updateConfidence(_ notification: Notification) {
        let confidence = notification.object as! Int
        MINIMUM_CONFIDENCE_TF_OD_API = Float(confidence)
    }

    @objc func toggleAutoMode() {
        var frames: [UIView] = [];
        autoMode = !autoMode;
        if (autoMode) {
            images.removeAll();
            Timer.scheduledTimer(withTimeInterval: TimeInterval(TIMER_OF_MODEL), repeats: true) { [self] timer in
                do {
                    if !autoMode {
                        timer.invalidate()
                        sendControl(control: Control())
                    }
                    if (frames.count > 0) {
                        for frame in frames {
                            frame.removeFromSuperview();
                        }
                    }
                    frames.removeAll();
                    if (timer.isValid) {
                        let startTime = returnCurrentTimestamp();
                        captureImage();
                        if (images.count > 0) {
                            let res = try detector?.recognizeImage(image: images[images.count - 1].0, height: originalHeight, width: originalWidth);
                            var i = 0;
                            if (res!.count > 0) {
                                for item in res! {
                                    if (item.getConfidence() * 100 > MINIMUM_CONFIDENCE_TF_OD_API) {
                                        let frame = addFrame(item: item, color: Constants.frameColors[i % 5]);
                                        frames.append(frame);
                                        cameraView.addSubview(frame);
                                        i += 1;
                                    }
                                }
                                let control: Control = updateTarget(res!.first!.getLocation());
                                sendControl(control: control);
                            }
                        }
                        let endTime = returnCurrentTimestamp();
                        print("timecost to run the image is ", endTime - startTime);
                    }
                } catch {
                    print("error:\(error)")
                }
            }
        } else {
            objectTrackingSettings?.autoModeButton.isOn = false
        }
    }

    func sendControl(control: Control) {
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
            let left = control.getLeft() * gameController.selectedSpeedMode.rawValue;
            let right = control.getRight() * gameController.selectedSpeedMode.rawValue;
            NotificationCenter.default.post(name: .updateSpeedLabel, object: String(Int(left)) + "," + String(Int(right)));
            NotificationCenter.default.post(name: .updateRpmLabel, object: String(Int(control.getLeft())) + "," + String(Int(control.getRight())));
            vehicleControl = control;
            print("c" + String(left) + "," + String(right) + "\n");
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n");
        }
    }

    @objc func updateModel(_ notification: Notification) throws {
        let selectedModelName = notification.object as! String
        currentModel = Common.returnModelItem(modelName: selectedModelName)
        detector = try! Detector.create(model: Model.fromModelItem(item: currentModel), device: currentDevice, numThreads: numberOfThreads) as? Detector;
        NotificationCenter.default.post(name: .updateObjectList, object: detector?.getLabels())
        if selectedModelName == "MobileNetV1-300" {
            TIMER_OF_MODEL = 0.03
        } else {
            TIMER_OF_MODEL = 0.7
        }
    }

    @objc func updateSelectedObject(_ notification: Notification) throws {
        detector?.setSelectedClass(newClass: notification.object as! String)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        if autoMode {
            autoMode = false
        }
    }

    func setupNavigationBarItem() {
        if UIImage(named: "back") != nil {
            let backNavigationIcon = (UIImage(named: "back")?.withRenderingMode(.alwaysOriginal))!
            let newBackButton = UIBarButtonItem(image: backNavigationIcon, title: Strings.objectTracking, target: self, action: #selector(ObjectTrackingFragment.back(sender:)))
            navigationItem.leftBarButtonItem = newBackButton
        }
    }

    @objc func back(sender: UIBarButtonItem) {
        _ = navigationController?.popViewController(animated: true)
    }

    func updateTarget(_ detection: CGRect) -> Control {
        let screenWidth = UIScreen.main.bounds.size.width
        let screenHeight = UIScreen.main.bounds.size.height
        let dx: CGFloat = screenWidth / CGFloat(detector!.getImageSizeX());
        let dy: CGFloat = screenHeight / CGFloat(detector!.getImageSizeY());
        let location = detection.applying(CGAffineTransform(scaleX: dx, y: dy))
        var centerX: Float = Float(location.midX);
        centerX = max(0, min(centerX, Float(screenWidth)));
        let x_pos_norm: Float = 1.0 - 2.0 * centerX / Float(screenWidth);
        var left: Float = 0.0;
        var right: Float = 0.0;
        if (x_pos_norm < 0.0) {
            left = 1;
            right = 1.0 + x_pos_norm;
        } else {
            left = 1 - x_pos_norm;
            right = 1;
        }
        return Control(left: left, right: right)
    }

    func addFrame(item: Detector.Recognition, color: UIColor) -> UIView {
        let frame = UIView()
        let screenWidth = UIScreen.main.bounds.size.width
        let screenHeight = UIScreen.main.bounds.size.height
        let detection = item.getLocation();
        let dx = screenWidth / CGFloat(detector!.getImageSizeX());
        let dy = screenHeight / CGFloat(detector!.getImageSizeY());
        let rect = detection.applying(CGAffineTransform(scaleX: dx, y: dy))

        frame.frame = rect;
        frame.layer.borderColor = color.cgColor;
        frame.layer.borderWidth = 2.0
        let nameString = UITextView();
        nameString.textColor = UIColor.white;
        nameString.font = nameString.font?.withSize(12)
        nameString.backgroundColor = color.withAlphaComponent(0.5);
        nameString.text = item.getTitle() + " " + String(format: "%.2f", item.getConfidence() * 100) + "%";
        nameString.translatesAutoresizingMaskIntoConstraints = true
        nameString.sizeToFit()
        frame.addSubview(nameString);
        return frame;
    }


}
