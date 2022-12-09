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
    var selectedSpeedMode: SpeedMode = SpeedMode.medium;
    var selectedControlMode: ControlMode = ControlMode.gamepad;
    var selectedDriveMode: DriveMode = DriveMode.joystick;
    var vehicleControl = Control();
    var indicator = "i0,0\n";
    let bluetooth = bluetoothDataController.shared;
    let dataLogger = DataLogger.shared
    let gameController = GameController.shared
    var isLoggedButtonPressed: Bool = false


    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
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
            let newBackButton = UIBarButtonItem(image: backNavigationIcon, title: Strings.dataCollection, target: self, action: #selector(DataCollectionController.back(sender:)))
            navigationItem.leftBarButtonItem = newBackButton
        }

        DeviceCurrentOrientation.shared.findDeviceOrientation()
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchLogging), name: .logData, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updatePreview), name: .updatePreview, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateTraining), name: .updateTraining, object: nil)
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
    }

//    @objc func loadCollapseView() {
//        expandSettingView.removeFromSuperview()
//        view.addSubview(collapseView)
//    }

    @objc func switchLogging() {
        loggingEnabled = !loggingEnabled;
        isLoggedButtonPressed = true;
        if (loggingEnabled) {
            expandSettingView.logData.isOn = true
            dataLogger.setupFilesForLogging();
            images.removeAll()
            Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { [self] timer in
                if !loggingEnabled {
                    timer.invalidate()
                }
                captureImage();
                sensorData.startSensorsUpdates()
                dataLogger.recordLogs();
            }
        } else {
            expandSettingView.logData.isOn = false
            baseDirectory = dataLogger.getBaseDirectoryName()
            dataLogger.allDirectoriesName.append(baseDirectory)
            DataLogger.shared.createSensorData(openBotPath: Strings.forwardSlash + baseDirectory);
            saveImages();
            setupImages()
            dataLogger.setupFilesForLogging()
        }
    }

    @objc func updateControllerValues() {
        gameController.updateControllerValues()

    }

    @objc func updateControlMode(_ notification: Notification?) {
        if let controlMode = notification?.userInfo?["mode"] as? ControlMode {
            selectedControlMode = controlMode;
        }
//        print(selectedControlMode);
        if selectedControlMode == .gamepad {
            gameControllerObj = GameController();
            NotificationCenter.default.addObserver(self, selector: #selector(updateControllerValues), name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil);
        } else if selectedControlMode == .phone {
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
//            print(selectedSpeedMode)
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
}


