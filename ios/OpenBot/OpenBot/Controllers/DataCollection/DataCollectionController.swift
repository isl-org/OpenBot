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
    var imagePath: String = ""
    var sensorPath: String = ""
    
    var imagePixelBuffers: [(CVPixelBuffer, Bool, Bool)] = []
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let documentsDirectory: String = paths.first ?? ""
        let openBotPath = documentsDirectory + Strings.forwardSlash + baseDirectory
        dataLogger.createOpenBotFolder(openBotPath: openBotPath)
        dataLogger.createImageFolder(openBotPath: openBotPath)
        dataLogger.createSensorData(openBotPath: openBotPath)
        self.imagePath = openBotPath + Strings.images
        self.sensorPath = openBotPath + Strings.sensor
        
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
        NotificationCenter.default.addObserver(self, selector: #selector(switchLogging), name: .logData, object: nil)
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

        guard !isImageCaptureQueueBusy else {
            return
        }
        
        imageCaptureQueue.async {
            if self.loggingEnabled {
                
                self.isImageCaptureQueueBusy = true
                // Record image index and timestep
                self.rgbFrames = self.rgbFrames + String(returnCurrentTimestamp()) + Strings.comma + String(self.count) + Strings.newLine
                
                // add the pixel buffer to the array
                self.imagePixelBuffers.append((imagePixelBuffer, self.isPreviewSelected, self.isTrainingSelected))
                
                // update the index and timestamp record
                self.dataLogger.setImageLogs(count: String(self.count))
                
                
                // update index
                print(self.imagePixelBuffers.count)
                self.count += 1
                print(self.count)
                self.isImageCaptureQueueBusy = false
                print(self.loggingEnabled)
            } else {
                self.count = 0
            }
        }
        
        print("---")
    }
    
    @objc func switchLogging() {
        
        self.loggingEnabled = !self.loggingEnabled;
        self.isLoggedButtonPressed = true;
        
        if (loggingEnabled) {
            expandSettingView.logData.isOn = true
            dataLogger.setupFilesForLogging();
            Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { [self] timer in
                if !loggingEnabled {
                    timer.invalidate()
                }
                sensorData.sampleSensors()
                dataLogger.recordLogs();
            }
        } else {
            expandSettingView.logData.isOn = false
            baseDirectory = dataLogger.getBaseDirectoryName()
            dataLogger.allDirectoriesName.append(baseDirectory)
            dataLogger.createSensorData(openBotPath: Strings.forwardSlash + baseDirectory);
            processAndSaveImages();
            dataLogger.setupFilesForLogging()
        }
    }
    
    @objc func processAndSaveImages() {
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let documentsDirectory: String = paths.first ?? ""
        let openBotPath = documentsDirectory + Strings.forwardSlash + baseDirectory
        dataLogger.createOpenBotFolder(openBotPath: openBotPath)
        dataLogger.createImageFolder(openBotPath: openBotPath)
        dataLogger.createSensorData(openBotPath: openBotPath)
        let imagePath = openBotPath + Strings.images
        let sensorPath = openBotPath + Strings.sensor
        var count: Int = 0;
        if self.imagePixelBuffers.count > 0 {
            print(self.imagePixelBuffers.count)
            for pixelBuffer in self.imagePixelBuffers {
                if pixelBuffer.1 {
                    let imageName = String(count) + Strings.underscore + "preview.jpeg"
                    let ciImage = CIImage(cvPixelBuffer: pixelBuffer.0)
                    let image = UIImage(ciImage: ciImage)
                    dataLogger.saveImages(path: imagePath, image: image, name: imageName);
                    print(imageName)
                }
                if pixelBuffer.2 {
                    let imageName = String(count) + Strings.underscore + Strings.crop
                    let scaledSize = CGSize(width: CGFloat(self.widthOfTrainingImage), height: CGFloat(self.heightOfTrainingImage))
                    guard let scaledPixelBuffer = pixelBuffer.0.resized(to: scaledSize) else {
                        debugPrint("unable to resize/crop sample buffer")
                        return
                    }
                    let ciCroppedImage = CIImage(cvPixelBuffer: scaledPixelBuffer)
                    let croppedImage = UIImage(ciImage: ciCroppedImage)
                    dataLogger.saveImages(path: imagePath, image: croppedImage, name: imageName);
                    print(imageName)
                }
                count = count + 1
            }
        }
        self.imagePixelBuffers.removeAll()
        dataLogger.saveFramesFile(path: sensorPath, data: rgbFrames);
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
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let documentsDirectory: String = paths.first ?? ""
        let openBotPath = documentsDirectory + Strings.forwardSlash + baseDirectory
        if isLoggedButtonPressed && loggingEnabled {
            switchLogging()
        }
        if let url = URL(string: openBotPath) {
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


