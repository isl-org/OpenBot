//
// Created by Sparsh Jain on 12/09/22.
//

import Foundation
import UIKit
import AVFoundation

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


    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        dataLogger.reset()
    }

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

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        expandSettingView.refreshConstraints()
        refreshConstraints()
    }

    func saveFolder() {
        _ = DataLogger.shared.getDirectoryInfo()
        let activityManager = UIActivityViewController(activityItems: DataLogger.shared.allDirectories, applicationActivities: nil)
        present(activityManager, animated: true)
        _ = navigationController?.popViewController(animated: true)
    }

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

    @objc func switchCamera() {
        switchCameraView();
    }

    @objc func openBluetoothSettings() {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: "bluetoothScreen"))
        navigationController?.pushViewController(nextViewController!, animated: true)
        stopSession()
    }

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
                    let ciImage = CIImage(cvPixelBuffer: imagePixelBuffer)
                    let image = UIImage(ciImage: ciImage)
                    self.dataLogger.saveImages(image: image, name: imageName);
                }

                // Record cropped image
                if self.isTrainingSelected {
                    let imageName = String(self.count) + Strings.underscore + Strings.crop
                    let scaledSize = CGSize(width: CGFloat(self.widthOfTrainingImage), height: CGFloat(self.heightOfTrainingImage))
                    guard let scaledPixelBuffer = imagePixelBuffer.resized(to: scaledSize) else {
                        debugPrint("unable to resize/crop sample buffer")
                        return
                    }
                    let ciCroppedImage = CIImage(cvPixelBuffer: scaledPixelBuffer)
                    let croppedImage = UIImage(ciImage: ciCroppedImage)
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

    @objc func toggleLogging() {

        loggingEnabled = !loggingEnabled;
        isLoggedButtonPressed = true;

        if (loggingEnabled) {
            expandSettingView.logData.isOn = true

            // Create the folders that will contain the data
            dataLogger.createOpenBotFolders()

            // Sample the robot sensors at a desired rate
            Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { [self] timer in
                if !loggingEnabled {
                    timer.invalidate()
                }
                sensorData.sampleSensors()
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

    @objc func updateControllerValues() {
        gameController.updateControllerValues()
    }

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

    @objc func updateDriveMode(_ notification: Notification) {
        if let driveMode = notification.userInfo?["drive"] as? DriveMode {
            selectedDriveMode = driveMode;
            gameController.selectedDriveMode = selectedDriveMode
        }
    }

    @objc func updateSpeedMode(_ notification: Notification) {
        if let speedMode = notification.userInfo?["speed"] as? SpeedMode {
            selectedSpeedMode = speedMode;
            gameController.selectedSpeedMode = selectedSpeedMode
        }

    }

    @objc func updatePreview(_ notification: Notification) {
        isPreviewSelected = !isPreviewSelected
    }

    @objc func updateTraining(_ notification: Notification) {
        isTrainingSelected = !isTrainingSelected
    }

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

    @objc func updateLogData(_ notification: Notification) {
        if notification.object != nil {
            let logData = notification.object as! Bool
            loggingEnabled = logData;
        }
    }
}


