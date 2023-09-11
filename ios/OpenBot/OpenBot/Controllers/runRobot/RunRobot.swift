//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import UIKit
import AVFoundation
import SceneKit
import ARKit
import simd

class runRobot: CameraController, ARSCNViewDelegate, UITextFieldDelegate {
    @IBOutlet weak var stopRobot: UIButton!
    @IBOutlet weak var commandMessage: UILabel!
    let bluetooth = bluetoothDataController.shared
    private var bufferHeight = 0
    private var bufferWidth = 0
    private var result: Control?
    static var detector: Detector?
    static var autopilot: Autopilot?;
    private let inferenceQueue = DispatchQueue(label: "openbot.runRobot.inferencequeue");
    private var isInferenceQueueBusy = false
    var vehicleControl: Control = Control()
    public var MINIMUM_CONFIDENCE_TF_OD_API: Float = 0.5
    private var useDynamicSpeed: Bool = false
    static var isObjectTracking: Bool = false
    static var isAutopilot: Bool = false;
    static var isMultipleObjectTracking: Bool = false
    static var isMultipleAi: Bool = false
    static var isPointGoalNavigation: Bool = false
    static var isDetection: Bool = false
    var tempPixelBuffer: CVPixelBuffer!
    var sceneView: ARSCNView!
    let blueDress = try! YCbCrImageBufferConverter()
    private var isNavQueueBusy = false
    private let navQueue = DispatchQueue(label: "openbot.navigation.navqueue")
    private var distance: Float = 0
    static var navigation: Navigation?
    private var isReached: Bool = false
    var marker = SCNNode()
    private var startingPoint = SCNNode()
    private var endingPoint: SCNNode!
    static var forward: Double = 0
    static var left: Double = 0
    var configuration = ARWorldTrackingConfiguration()
    var task: String = ""
    private var framesArray: [Bool] = [];
    private var totalFrames: Int = 0;
    var counter: Int = 0;
    var object: String = "";

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
        NotificationCenter.default.addObserver(self, selector: #selector(updateCommandObject), name: .commandObject, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(createCamera), name: .createCameraView, object: nil);
        setupNavigationBarItem();
        updateConstraints();
        let modelItems = Common.loadAllModelItemsFromBundle()
        if (modelItems.count > 0) {
            let detectorModel = modelItems.first(where: { $0.type == TYPE.DETECTOR.rawValue })
            runRobot.detector = try! Detector.create(model: Model.fromModelItem(item: detectorModel ?? modelItems[0]), device: RuntimeDevice.CPU, numThreads: 1) as? Detector
            let autopilotModel = modelItems.first(where: { $0.type == TYPE.AUTOPILOT.rawValue });
            runRobot.autopilot = Autopilot(model: Model.fromModelItem(item: autopilotModel ?? modelItems[0]), device: RuntimeDevice.CPU, numThreads: 1);
        }
        runRobot.navigation = Navigation(model: Model.fromModelItem(item: Common.returnNavigationModel()), device: RuntimeDevice.CPU, numThreads: 1)
    }

    var temp = 0;


    /**
     override function to create camera view behind stopRobot
     */
    override func createCameraView() {
        if temp > 0 {
            return;
        }
        DispatchQueue.main.async {
            super.createCameraView();
            self.view.sendSubviewToBack(super.cameraView);
        }
        temp = temp + 1;
    }

    /**
     override function calls after controller view is presented
     - Parameter animated:
     */
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if (runRobot.isPointGoalNavigation) {
            pointGoalCameraView {
                let camera = self.sceneView.pointOfView!
                let cameraTransform = camera.transform
                _ = SCNVector3(-cameraTransform.m31, -cameraTransform.m32, -cameraTransform.m33)
                let markerInCameraFrame = SCNVector3(Float(-runRobot.left), 0.0, Float(-runRobot.forward))
                let markerInWorldFrame = markerInCameraFrame.transformed(by: cameraTransform)
                self.marker = SCNNode(geometry: SCNPlane(width: 0.1, height: 0.1))
                self.marker.position = markerInWorldFrame
                let imageMaterial = SCNMaterial()
                imageMaterial.diffuse.contents = Images.gmapMarker
                self.marker.geometry?.firstMaterial = imageMaterial
                self.sceneView.scene.rootNode.addChildNode(self.marker)
                self.startingPoint.position = self.sceneView.pointOfView?.position ?? camera.position
                self.endingPoint = self.marker
                self.calculateRoute()
            }
        }
    }

    /**
     function to restart point goal navigation session when detected error
     - Parameters:
       - session:
       - error:
     */
    func session(_ session: ARSession, didFailWithError error: Error) {
        // Present an error message to the user
        print("Session failed. Changing worldAlignment property.")
        print(error.localizedDescription)
        if let arError = error as? ARError {
            switch arError.errorCode {
            case 102:
                configuration.worldAlignment = .gravity
                restartSessionWithoutDelete()
            default:
                restartSessionWithoutDelete()
            }
        }
    }

    // Restart session with a different worldAlignment - prevents bug from crashing app
    func restartSessionWithoutDelete() {
        sceneView.session.pause()
        sceneView.session.run(configuration, options: [.resetTracking, .removeExistingAnchors])
    }

    /**
     override function calls after current controller view disappear
     Here we stopping the car, removing all notifications

     - Parameter animated:
     */
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        NotificationCenter.default.post(name: .cancelThread, object: nil)
        sceneView?.session.pause()
    }

    /**
     override function when view of controller disappears
     - Parameter animated:
     */
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NotificationCenter.default.removeObserver(self);
    }

    /**
     updating current commands
     - Parameter notification:
     */
    var count: Int = 0;

    /**
     function to update commands on screen
     - Parameter notification:
     */
    @objc func updateCommandMsg(_ notification: Notification) {
        let message = notification.object as! String;
        // Update UI periodically to show all the received messages
        if self.count > 1 {
            return;
        }
        self.count = self.count + 1;
        DispatchQueue.main.async {
            self.commandMessage.text = message;
        }
    }

    /**
     function to update the input objects for classes
     - Parameter notification:
     */
    @objc func updateCommandObject(_ notification: Notification) {
        DispatchQueue.main.async {
            runRobot.detector?.setMultipleSelectedClass(newClasses: notification.object as! [String])
        }
    }

    @objc func createCamera(_ notification: Notification) {
        if notification.object != nil {
            task = notification.object as! String
        }
        createCameraView();
    }

    /**
     function to create camera view for point goal navigation
     - Parameter completion:
     */
    func pointGoalCameraView(completion: @escaping () -> Void) {
        sceneView = ARSCNView(frame: view.bounds)
        sceneView.debugOptions = []
        view.insertSubview(sceneView, belowSubview: stopRobot)
        let scene = SCNScene()
        sceneView.scene = scene
        sceneView.delegate = self
        sceneView.showsStatistics = true
        sceneView.debugOptions = [ARSCNDebugOptions.showFeaturePoints, ARSCNDebugOptions.showWorldOrigin]
        let configuration = ARWorldTrackingConfiguration()
        sceneView.session.run(configuration)
        completion()
    }

    /**
     function to calculate the route to the point
     */
    func calculateRoute() {
        _ = simd_distance(startingPoint.simdPosition, endingPoint.simdPosition)
        _ = simd_normalize(endingPoint.simdPosition - startingPoint.simdPosition)
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
        if (runRobot.isPointGoalNavigation) {
            if currentOrientation == .portrait {
                sceneView.frame.size = CGSize(width: width, height: height);
            } else {
                sceneView.frame.size = CGSize(width: height, height: width);
            }
        }
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

    /**
     function to stop ongoing robot commands
     */
    func stopCar() {
        runRobot.isDetection = false
        runRobot.isObjectTracking = false
        runRobot.isAutopilot = false
        runRobot.isMultipleObjectTracking = false
        runRobot.isPointGoalNavigation = false
        runRobot.isMultipleAi = false
        taskStorage.taskArray = [];
        bluetooth.sendDataFromJs(payloadData: "c" + String(0) + "," + String(0) + "\n");
        bluetooth.sendDataFromJs(payloadData: "l" + String(0) + "," + String(0) + "\n");
        let indicatorValues = "i0,0\n";
        bluetooth.sendDataFromJs(payloadData: indicatorValues)
    }

    /**
     function to send output controls to openBot
     - Parameter control:
     */
    func sendControl(control: Control) {
        if runRobot.isAutopilot || runRobot.isObjectTracking || runRobot.isMultipleObjectTracking || runRobot.isDetection {
            createCameraView();
        }
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

    /**
      function to update target class when object moves
     - Parameter detection:
     - Returns:
     */
    func updateTarget(_ detection: CGRect) -> Control {

        // Left and right wheels control values
        var left: Float = 0.0
        var right: Float = 0.0

        let frameWidth = UIScreen.main.bounds.size.width
        let frameHeight = UIScreen.main.bounds.size.height

        let dx: CGFloat = CGFloat(runRobot.detector!.getImageSizeX()) / frameWidth
        let dy: CGFloat = CGFloat(runRobot.detector!.getImageSizeY()) / frameHeight
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

    /**
     called after camera capture each frame
     - Parameters:
       - output:
       - sampleBuffer:
       - connection:
     */
    override func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
        if (runRobot.isObjectTracking || runRobot.isAutopilot || runRobot.isMultipleObjectTracking || runRobot.isMultipleAi || runRobot.isDetection) {
            let pixelBuffer: CVPixelBuffer? = CMSampleBufferGetImageBuffer(sampleBuffer)
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
                } else if runRobot.isMultipleObjectTracking {
                    self.runMultipleObjectTracking(imagePixelBuffer: imagePixelBuffer);
                } else if runRobot.isAutopilot {
                    self.runAutopilot(imagePixelBuffer: imagePixelBuffer);
                } else if runRobot.isMultipleAi {
                    self.runAutopilot(imagePixelBuffer: imagePixelBuffer);
                    self.runObjectTrackingForMultipleAI(imagePixelBuffer: imagePixelBuffer);
                } else {
                    self.runDetection(imagePixelBuffer: imagePixelBuffer);
                }
            }

        }
    }

    /**
     function to enable object tracking
     - Parameters:
       - object:
       - model:
     */
    static func enableObjectTracking(object: String, model: String) {
        DispatchQueue.main.async {
            runRobot.isObjectTracking = true;
        }
        let currentModel = Common.returnModelItem(modelName: model)
        detector = try! Detector.create(model: Model.fromModelItem(item: currentModel), device: .CPU, numThreads: 1) as? Detector
    }

    /**
     function to enable autopilot
     - Parameter model:
     */
    static func enableAutopilot(model: String) {
        DispatchQueue.main.async {
            runRobot.isAutopilot = true;
        }
        let currentModel = Common.returnModelItem(modelName: model)
        autopilot = Autopilot(model: Model.fromModelItem(item: currentModel), device: RuntimeDevice.CPU, numThreads: 1);
    }

    /**
     function to enable multiple object tracking
     - Parameters:
       - object1:
       - model:
       - object2:
     */
    static func enableMultipleObjectTracking(object1: String, model: String, object2: String) {
        DispatchQueue.main.async {
            runRobot.isMultipleObjectTracking = true;
        }
        let currentModel = Common.returnModelItem(modelName: model)
        detector = try! Detector.create(model: Model.fromModelItem(item: currentModel), device: .CPU, numThreads: 1) as? Detector
    }

    /**
     function to enable point goal navigation
     - Parameters:
       - forward:
       - left:
     */
    static func enablePointGoalNavigation(forward: Double, left: Double) {
        DispatchQueue.main.async {
            runRobot.isPointGoalNavigation = true;
            runRobot.forward = forward
            runRobot.left = left
        }
        navigation = Navigation(model: Model.fromModelItem(item: Common.returnNavigationModel()), device: RuntimeDevice.CPU, numThreads: 1)
    }

    /**
     function to enable detection
     - Parameters:
       - forward:
       - left:
     */
    static func onDetection(object: String, model: String, task: String) {
        let currentModel = Common.returnModelItem(modelName: model)
        detector = try! Detector.create(model: Model.fromModelItem(item: currentModel), device: .CPU, numThreads: 1) as? Detector
        DispatchQueue.main.async {
            runRobot.isDetection = true;
        }
    }

    /**
     delegate revoke when SceneKit node corresponding to a new AR anchor has been added to the scene
     - Parameters:
       - renderer:
       - scene:
       - time:
     */
    func renderer(_ renderer: SCNSceneRenderer, didRenderScene scene: SCNScene, atTime time: TimeInterval) {
        if (runRobot.isPointGoalNavigation) {
            guard let currentFrame = sceneView.session.currentFrame else {
                return
            }
            let pixelBuffer = currentFrame.capturedImage
            if sceneView.pointOfView?.position != nil {
                processPixelBuffer(pixelBuffer, sceneView.pointOfView!)
                tempPixelBuffer = pixelBuffer
            }
        }
    }

    /**
     function to process the buffer and send buffer to navigation.tflite to give result
     - Parameters:
       - pixelBuffer:
       - currentPosition:
     */
    func processPixelBuffer(_ pixelBuffer: CVPixelBuffer, _ currentPosition: SCNNode) {
        guard !isNavQueueBusy else {
            return
        }
        if endingPoint == nil {
            return
        }
        navQueue.async { [self] in
            isNavQueueBusy = true
            let startPose = Pose(tx: currentPosition.position.x, ty: currentPosition.position.y, tz: currentPosition.position.z, qx: currentPosition.orientation.x, qy: currentPosition.orientation.y, qz: currentPosition.orientation.z, qw: currentPosition.orientation.w)
            let endPose = Pose(tx: endingPoint.position.x, ty: endingPoint.position.y, tz: endingPoint.position.z, qx: endingPoint.orientation.x, qy: endingPoint.orientation.y, qz: endingPoint.orientation.z, qw: endingPoint.orientation.w)
            let yaw = computeDeltaYaw(pose: startPose, goalPose: endPose)
            let converted = try! blueDress.convertToBGRA(imageBuffer: pixelBuffer)
            let refCon = NSMutableData()
            var timingInfo: CMSampleTimingInfo = .invalid
            var formatDescription: CMVideoFormatDescription? = nil
            CMVideoFormatDescriptionCreateForImageBuffer(allocator: kCFAllocatorDefault, imageBuffer: converted, formatDescriptionOut: &formatDescription)
            var output: CMSampleBuffer? = nil
            _ = CMSampleBufferCreateForImageBuffer(allocator: kCFAllocatorDefault, imageBuffer: converted, dataReady: true, makeDataReadyCallback: nil, refcon: refCon.mutableBytes, formatDescription: formatDescription!, sampleTiming: &timingInfo, sampleBufferOut: &output)
            // extract the image buffer from the sample buffer
            let pixelBufferBGRA: CVPixelBuffer? = CMSampleBufferGetImageBuffer(output!)
            result = runRobot.navigation?.recognizeImage(pixelBuffer: pixelBufferBGRA!, goalDistance: distance, goalSin: sin(yaw), goalCos: cos(yaw))
            guard let controlResult = result else {
                return
            }
            if isReached {
                sendControl(control: Control())
            } else {
                sendControl(control: controlResult)
            }

            isNavQueueBusy = false
        }
    }

    /**
     delegate revoke when SceneKit node corresponding to a new AR anchor has been added to the scene
     - Parameters:
       - renderer:
       - time:
     */
    func renderer(_ renderer: SCNSceneRenderer, updateAtTime time: TimeInterval) {
        if (runRobot.isPointGoalNavigation) {
            guard let camera = sceneView.pointOfView else {
                return
            }
            checkCameraPosition(position: camera.presentation.simdPosition)
        }
    }

    // Define a distance threshold for triggering the event
    let distanceThreshold: Float = 0.15 // adjust this value as needed

    /**
     function to check openBot is reached at point or not
     - Parameter position:
     */
    func checkCameraPosition(position: simd_float3) {
        if endingPoint != nil && isReached != true {
            distance = simd_distance(position, endingPoint.simdPosition)
            if distance <= distanceThreshold {
                DispatchQueue.main.async {
                    self.sceneView.session.pause()
                    self.isReached = true
                    self.marker.removeFromParentNode()
                }
            }
        }
    }

    /**
     function to send control to robot according to object tracking
     - Parameter imagePixelBuffer:
     */
    func runObjectTracking(imagePixelBuffer: CVPixelBuffer) {
        self.isInferenceQueueBusy = true
        let res = runRobot.detector?.recognizeImage(pixelBuffer: imagePixelBuffer, detectionType: "single");
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

    /**
     function to send control to robot according to multiple object tracking
     - Parameter imagePixelBuffer:
     */
    func runMultipleObjectTracking(imagePixelBuffer: CVPixelBuffer) {
        self.isInferenceQueueBusy = true
        let res = runRobot.detector?.recognizeImage(pixelBuffer: imagePixelBuffer, detectionType: "multiple")
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
                    if let objects = runRobot.detector?.returnMultipleSelectedClass() {
                        let filteredObjects = res!.filter { i in
                            if i.getConfidence() > self.MINIMUM_CONFIDENCE_TF_OD_API {
                                return i.getTitle() == objects[1] || i.getTitle() == objects[0]
                            } else {
                                return false
                            }
                        }
                        let stopObject = filteredObjects.filter {
                            $0.getTitle() == objects[1]
                        }
                        let runObject = filteredObjects.filter {
                            $0.getTitle() == objects[0]
                        }
                        if (stopObject.count == 0 && runObject.count > 0) {
                            self.sendControl(control: controlResult)
                        } else if (stopObject.count > 0 && runObject.count > 0) {
                            for i in stopObject {
                                for j in runObject {
                                    let getStopObjectPos = sqrt(pow(i.getLocation().midX - i.getLocation().minY, 2))
                                    let getStartObjectPos = sqrt(pow(j.getLocation().midX - j.getLocation().minY, 2))
                                    if (getStopObjectPos > getStartObjectPos) {
                                        self.sendControl(control: controlResult)
                                    } else {
                                        self.stopCar();
                                        _ = jsEvaluator(jsCode: self.task);
                                    }
                                }
                            }
                        } else if (stopObject.count > 0 && runObject.count == 0) {
                            self.stopCar();
                            _ = jsEvaluator(jsCode: self.task);
                        } else {
                            self.sendControl(control: Control())
                        }
                    }
                }
            }
        }
        self.isInferenceQueueBusy = false
    }

    /**
     function to send control to robot according to autopilot
     - Parameter imagePixelBuffer:
     */
    func runAutopilot(imagePixelBuffer: CVPixelBuffer) {
        let startTime = returnCurrentTimeStampSince1970()
        self.isInferenceQueueBusy = true
        self.result = runRobot.autopilot?.recognizeImage(pixelBuffer: imagePixelBuffer, indicator: 0) ?? Control();
        guard let controlResult = self.result else {
            return
        }
        DispatchQueue.main.async {
            self.sendControl(control: controlResult)
        }
        sendControl(control: controlResult)
        isInferenceQueueBusy = false
        print("Time for autopilot is -----> \(1000 / (returnCurrentTimeStampSince1970() - startTime))")
    }

    /// function to calculate the delta yaw for device position and goal position
    ///
    /// - Parameters:
    ///   - pose: device position
    ///   - goalPose: destination position
    /// - Returns:
    func computeDeltaYaw(pose: Pose, goalPose: Pose) -> Float {
        // compute robot forward axis (global coordinate system)
        let forward: [Float] = [0.0, 0.0, -1.0]
        let forwardRotated = pose.rotateVector(vector: forward)

        // distance vector to goal (global coordinate system)
        let dx = goalPose.tx - pose.tx
        let dz = goalPose.tz - pose.tz

        let yaw = atan2(forwardRotated[2], forwardRotated[0]) - atan2(dz, dx)

        // fit to range (-pi, pi]
        var resultYaw = yaw
        if (resultYaw > Float.pi) {
            resultYaw -= 2 * Float.pi
        } else if (resultYaw <= -Float.pi) {
            resultYaw += 2 * Float.pi
        }
        return resultYaw
    }

    /**
     A static method used to initialize the object tracking and autopilot
     - Parameters:
       - autoPilotModel:
       - task:
       - object:
       - detectorModel:
     */
    static func enableMultipleAI(autoPilotModel: String, task: String, object: String, detectorModel: String) {
        DispatchQueue.main.async {
            runRobot.isMultipleAi = true;
        }
        let objectTrackingModel = Common.returnModelItem(modelName: detectorModel);
        let auto = Common.returnModelItem(modelName: autoPilotModel);
        detector = try! Detector.create(model: Model.fromModelItem(item: objectTrackingModel), device: .CPU, numThreads: 1) as? Detector
        autopilot = Autopilot(model: Model.fromModelItem(item: auto), device: RuntimeDevice.CPU, numThreads: 1);
    }


    /**
     Function to run autopilot and Object tracking simultaneously
     - Parameter imagePixelBuffer:
     */
    func runObjectTrackingForMultipleAI(imagePixelBuffer: CVPixelBuffer) {
        let startTime = returnCurrentTimeStampSince1970();
        self.isInferenceQueueBusy = true
        let res = runRobot.detector?.recognizeImage(pixelBuffer: imagePixelBuffer, detectionType: "single");
        if res != nil {
            DispatchQueue.main.async {
                if (res!.count > 0) {
                    for item in res! {
                        if (item.getConfidence() > self.MINIMUM_CONFIDENCE_TF_OD_API) {
                            self.stopCar();
                            _ = jsEvaluator(jsCode: self.task);
                        } else {
                            return;
                        }
                    }
                }
            }
        }
        self.isInferenceQueueBusy = false
        print("Time for objectTracking is -----> \(1000 / (returnCurrentTimeStampSince1970() - startTime))")
    }

    /**
     function to send control to robot according to object tracking
     - Parameter imagePixelBuffer:
     */
    func runDetection(imagePixelBuffer: CVPixelBuffer) {
        isInferenceQueueBusy = true
        let res = runRobot.detector?.recognizeImage(pixelBuffer: imagePixelBuffer, detectionType: "all");
        if res != nil {
            if (res!.count > 0) {
                result = updateTarget(res!.first!.getLocation())
            } else {
                result = Control(left: 0, right: 0)
            }
            DispatchQueue.main.async {
                if (res!.count > 0) {
                    for item in res! {
                        if (item.getConfidence() > self.MINIMUM_CONFIDENCE_TF_OD_API) {
                            self.counter = 0
                            if (taskStorage.returnAttributeArray().contains { i in
                                i.keys.contains(item.getTitle())
                            }) {
                                self.object = item.getTitle();
                                _ = jsEvaluator(jsCode: taskStorage.getValueOfAttribute(classType: item.getTitle(), type: "detect") as! String);
                                self.totalFrames = taskStorage.getValueOfAttribute(classType: item.getTitle(), type: "frames") as! Int
                            }
                        } else {
                            self.counter = self.counter + 1;
                            if (self.counter >= self.totalFrames && self.object != "") {
                                _ = jsEvaluator(jsCode: taskStorage.getValueOfAttribute(classType: self.object, type: "unDetect") as! String);
                            }
                        }
                    }
                }
            }
        }
        self.isInferenceQueueBusy = false
    }

    /**
     Function to stop the AI Blocks
     */
    static func disableAI() {
        isObjectTracking = false
        isAutopilot = false;
        isMultipleObjectTracking = false
        isMultipleAi = false
        isDetection = false
    }
}

