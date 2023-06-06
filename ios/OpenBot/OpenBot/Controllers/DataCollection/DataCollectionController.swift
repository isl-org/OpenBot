///
/// Created by Sparsh Jain on 12/09/22.
///

import Foundation
import UIKit
import AVFoundation

/// The DataCollectionController class implements a set of mechanisms allowing to sample visual observations and sensor data from the phone
/// and to save this data in a collection of zip files, that will be later used for training purposes.
class DataCollectionController: CameraController {
    let expandSettingView = expandSetting(frame: CGRect(x: 0, y: 0, width: width, height: height))
    var loggingEnabled: Bool = false;
    let sensorData = sensorDataRetrieve.shared
    var sensorDataTemp: String = ""
    var gameControllerObj: GameController?;
    var selectedSpeedMode: SpeedMode = SpeedMode.NORMAL;
    var selectedControlMode: ControlMode = ControlMode.GAMEPAD;
    var selectedDriveMode: DriveMode = DriveMode.JOYSTICK;
    var vehicleControl = Control();
    var indicator = "i0,0\n";
    let bluetooth = bluetoothDataController.shared;
    let dataLogger = DataLogger.shared
    let gameController = GameController.shared
    var isLoggedButtonPressed: Bool = false
    var count: Int = 0;
    private let imageCaptureQueue = DispatchQueue(label: "openbot.datacollection.imagecapturequeue")
    private var isImageCaptureQueueBusy = false
    var saveZipFilesName = [URL]()
    var paths: [String] = [""]

    /// Initialization routine
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        dataLogger.reset()
    }

    /**
     Removing all notifications
     */
    deinit {
        NotificationCenter.default.removeObserver(self)
    }

    /// Called after the view controller has loaded.
    override func viewDidLoad() {
        super.viewDidLoad()
        createCameraView()
        view.backgroundColor = UIColor(named: "darkBg");
        view.addSubview(expandSettingView)
        updateControlMode(nil);
        navigationItem.hidesBackButton = true
        if UIImage(named: "back") != nil {
            let backNavigationIcon = (UIImage(named: "back")?.withRenderingMode(.alwaysOriginal))!
            let newBackButton = UIBarButtonItem(image: backNavigationIcon, title: Strings.dataCollection, target: self, action: #selector(DataCollectionController.back(sender:)), titleColor: Colors.navigationColor ?? .white)
            navigationItem.leftBarButtonItem = newBackButton
        }
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(toggleLogging), name: .logData, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updatePreview), name: .updatePreview, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateTraining), name: .updateTraining, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDataFromControllerApp), name: .updateStringFromControllerApp, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateLogData), name: .logData, object: nil)
        gameController.resetControl = false
    }

    /// Notifies the view controller that its view is about to be added to a view hierarchy.
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    /// Called after the view was dismissed, covered or otherwise hidden.
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }

    /// Called when the view controller's view's size is changed by its parent (i.e. for the root view controller when its window rotates or is resized).
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        expandSettingView.refreshConstraints()
        refreshConstraints()
    }

    ///
    func saveFolder() {
        _ = DataLogger.shared.getDirectoryInfo()
        let activityManager = UIActivityViewController(activityItems: DataLogger.shared.allDirectories, applicationActivities: nil)
        present(activityManager, animated: true)
        _ = navigationController?.popViewController(animated: true)
    }

    /// Create a ZIP file from the recorded experiment data
    ///
    /// - Parameters: path of the folder to compress
    func createZip(path: URL) {
        for t in DataLogger.shared.baseDirList {
            let baseDirectoryName = t + ".zip";
            let fm = FileManager.default
            let baseDirectoryUrl = fm.urls(for: .documentDirectory, in: .userDomainMask).first!.appendingPathComponent(Strings.forwardSlash + t)
            var error: NSError?
            let coordinator = NSFileCoordinator()
            coordinator.coordinate(readingItemAt: baseDirectoryUrl, options: [.forUploading], error: &error) { (zipUrl) in
                let tmpUrl = try! fm.url(
                        for: .itemReplacementDirectory,
                        in: .userDomainMask,
                        appropriateFor: zipUrl,
                        create: true
                ).appendingPathComponent(baseDirectoryName)
                try! fm.moveItem(at: zipUrl, to: tmpUrl)
                saveZipFilesName.append(tmpUrl)
            }
        }
        let avc = UIActivityViewController(activityItems: saveZipFilesName, applicationActivities: nil)
        present(avc, animated: true)
        avc.completionWithItemsHandler = { activity, success, items, error in
            DataLogger.shared.deleteFiles(fileNameToDelete: Strings.forwardSlash + DataLogger.shared.getBaseDirectoryName())
        }
    }

    ///
    func refreshConstraints() {
        if UIDevice.current.orientation == .portrait {
            for constraint in cameraView.constraints {
                if constraint.identifier == "width" {
                    constraint.constant = width
                } else if constraint.identifier == "height" {
                    constraint.constant = height
                }
            }
        } else {
            for constraint in cameraView.constraints {
                if constraint.identifier == "width" {
                    constraint.constant = height
                } else if constraint.identifier == "height" {
                    constraint.constant = width
                }
            }
        }

    }

    /// Switch between the front and rear camera
    @objc func switchCamera() {
        switchCameraView();
    }

    @objc func openBluetoothSettings() {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: "bluetoothScreen"))
        navigationController?.pushViewController(nextViewController!, animated: true)
        stopSession()
    }

    /// Camera delegate, called at every new frame
    ///
    /// - Parameters:
    ///     - output:
    ///     - sampleBuffer:
    ///     - connection:
    override func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
        // extract the image buffer from the sample buffer
        let pixelBuffer: CVPixelBuffer? = CMSampleBufferGetImageBuffer(sampleBuffer)
        guard let imagePixelBuffer = pixelBuffer else {
            debugPrint("unable to get image from sample buffer")
            return
        }

        if webRTCClient != nil {
            imageCaptureQueue.async {
                webRTCClient.captureCurrentFrame(sampleBuffer: sampleBuffer);
                self.isImageCaptureQueueBusy = false
            }
        }

        guard !isImageCaptureQueueBusy else {
            return
        }

        imageCaptureQueue.async {
            if self.loggingEnabled && (self.isTrainingSelected || self.isPreviewSelected) {
                self.isImageCaptureQueueBusy = true
                let startTime = Date().millisecondsSince1970

                // Record image index and timestep
                self.dataLogger.recordImageLogs(index: self.count)

                // Record preview image
                if self.isPreviewSelected {
                    let imageName = String(self.count) + Strings.underscore + "preview.jpeg"
                    var scaledSize = CGSize(width: CGFloat(1280.0), height: CGFloat(720.0))
                    switch self.previewResolution {
                    case .LOW:
                        scaledSize = CGSize(width: CGFloat(960.0), height: CGFloat(540.0))
                    case .MEDIUM:
                        scaledSize = CGSize(width: CGFloat(1280.0), height: CGFloat(720.0))
                    case .HIGH:
                        scaledSize = CGSize(width: CGFloat(1920.0), height: CGFloat(1080.0))
                    }
                    guard let scaledPixelBuffer = imagePixelBuffer.resized(to: scaledSize, preserveAspectRatio: false, with: self.preAllocatedMemoryPool!) else {
                        debugPrint("unable to resize/crop sample buffer")
                        return
                    }
                    let ciCroppedImage = CIImage(cvPixelBuffer: scaledPixelBuffer)
                    let croppedImage = UIImage(ciImage: ciCroppedImage, scale: UIScreen.main.scale, orientation: UIImage.Orientation.up)
                    self.dataLogger.saveImages(image: croppedImage, name: imageName);
                }

                // Record training image
                if self.isTrainingSelected {
                    let imageName = String(self.count) + Strings.underscore + Strings.crop
                    let scaledSize = CGSize(width: CGFloat(self.widthOfTrainingImage), height: CGFloat(self.heightOfTrainingImage))
                    guard let scaledPixelBuffer = imagePixelBuffer.resized(to: scaledSize, preserveAspectRatio: false, with: self.preAllocatedMemoryPool!) else {
                        debugPrint("unable to resize/crop sample buffer")
                        return
                    }
                    let ciCroppedImage = CIImage(cvPixelBuffer: scaledPixelBuffer)
                    let croppedImage = UIImage(ciImage: ciCroppedImage, scale: UIScreen.main.scale, orientation: UIImage.Orientation.up)
                    self.dataLogger.saveImages(image: croppedImage, name: imageName);
                }
                self.count += 1
                let endTime = Date().millisecondsSince1970
                print(1000 / (endTime - startTime))
                self.isImageCaptureQueueBusy = false
            } else {
                self.count = 0
            }
        }
    }

    /// Activate/deactivate logging
    @objc func toggleLogging() {

        loggingEnabled = !loggingEnabled;
        isLoggedButtonPressed = true;

        if (loggingEnabled) {
            expandSettingView.logData.isOn = true

            // Create the folders that will contain the data
            dataLogger.createOpenBotFolders()

            // Sample the robot sensors at a desired rate
            Timer.scheduledTimer(withTimeInterval: expandSettingView.samplingPeriod, repeats: true) { [self] timer in
                if !loggingEnabled {
                    timer.invalidate()
                }

                // Sample IMU sensor data
                sensorData.sampleIMU()
                dataLogger.recordLogs();
            }
        } else {
            expandSettingView.logData.isOn = false

            // Save the collected sensor data
            dataLogger.saveSensorData()

            // Reset data logger
            dataLogger.reset()
        }
    }

    /// Main control update function
    @objc func updateControllerValues() {
        gameController.updateControllerValues()
    }

    /// update control mode
    @objc func updateControlMode(_ notification: Notification?) {
        if let controlMode = notification?.userInfo?["mode"] as? ControlMode {
            selectedControlMode = controlMode;
        }
        if selectedControlMode == .GAMEPAD {
            gameControllerObj = GameController();
            NotificationCenter.default.addObserver(self, selector: #selector(updateControllerValues), name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil);
        } else if selectedControlMode == .PHONE {
            gameControllerObj = nil;
            NotificationCenter.default.removeObserver(self, name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil);
        }
    }

    /// update drive mode
    @objc func updateDriveMode(_ notification: Notification) {
        if let driveMode = notification.userInfo?["drive"] as? DriveMode {
            selectedDriveMode = driveMode;
            gameController.selectedDriveMode = selectedDriveMode
        }
    }

    /// update speed mode
    @objc func updateSpeedMode(_ notification: Notification) {
        if let speedMode = notification.userInfo?["speed"] as? SpeedMode {
            selectedSpeedMode = speedMode;
            gameController.selectedSpeedMode = selectedSpeedMode
        }

    }

    /// update is preview selected
    @objc func updatePreview(_ notification: Notification) {
        isPreviewSelected = !isPreviewSelected
    }

    /// update is training selected
    @objc func updateTraining(_ notification: Notification) {
        isTrainingSelected = !isTrainingSelected
    }

    /// on tap back
    @objc func back(sender: UIBarButtonItem) {
        if isLoggedButtonPressed && loggingEnabled {
            toggleLogging()
        }
        if let url = URL(string: dataLogger.openbotPath) {
            if isLoggedButtonPressed {
                createZip(path: url)
            }
        }
        _ = navigationController?.popViewController(animated: true)
    }

    /// function to update the data from device.
    @objc func updateDataFromControllerApp(_ notification: Notification) {
        if gameController.selectedControlMode == ControlMode.GAMEPAD {
            return
        }
        if notification.object != nil {
            let command = notification.object as! String
            let rightSpeed = command.slice(from: "r:", to: ", ");
            let leftSpeed = command.slice(from: "l:", to: "}}")
            gameController.sendControlFromPhoneController(control: Control(left: Float(Double(leftSpeed ?? "0.0") ?? 0.0), right: Float(Double(rightSpeed ?? "0.0") ?? 0.0)))
        }
    }

    /// update the logs data
    @objc func updateLogData(_ notification: Notification) {
        if notification.object != nil {
            let logData = notification.object as! Bool
            loggingEnabled = logData;
        }
    }
}


