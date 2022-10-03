//
// Created by Sparsh Jain on 12/09/22.
//

import Foundation
import UIKit
import AVFoundation

class DataCollectionController: CameraController {
    let collapseView = collapseSettingView(frame: CGRect(x: 0, y: 0, width: width, height: height))
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

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        createCameraView()
        view.addSubview(collapseView)
        updateControlMode(nil);
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        NotificationCenter.default.addObserver(self, selector: #selector(loadExpandView), name: .clickSetting, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(loadCollapseView), name: .cancelButton, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchLogging), name: .logData, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateControlMode), name: .updateControl, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDriveMode), name: .updateDriveMode, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateSpeedMode), name: .updateSpeed, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updatePreview), name: .updatePreview, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateTraining), name: .updateTraining, object: nil)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        setupCollapseView()
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        expandSettingView.refreshConstraints()
        refreshConstraints()
        setupCollapseView()
    }

    func setupCollapseView() {
        if currentOrientation == .portrait {
            collapseView.frame = CGRect(x: 0, y: 0, width: width, height: height)
            expandSettingView.frame = CGRect(x: 0, y: 0, width: width, height: height)

        } else {
            collapseView.frame = CGRect(x: 0, y: 0, width: height, height: width)
            expandSettingView.frame = CGRect(x: 0, y: 0, width: height, height: width)

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

    @objc func loadExpandView() {
        collapseView.removeFromSuperview()
        view.addSubview(expandSettingView)
    }

    @objc func switchCamera() {
        switchCameraView();

    }

    @objc func openBluetoothSettings() {
        print("openBluetoothSettings");

    }

    @objc func loadCollapseView() {
        expandSettingView.removeFromSuperview()
        view.addSubview(collapseView)
    }

    @objc func switchLogging() {
        loggingEnabled = !loggingEnabled;
        if (loggingEnabled) {
            dataLogger.setupFilesForLogging();
            Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { [self] timer in
                captureImage();
                sensorData.startSensorsUpdates()
                dataLogger.recordLogs();
                if !loggingEnabled {
                    timer.invalidate()
                }
            }
        } else {
            baseDirectory = dataLogger.getBaseDirectoryName()
            saveImages();
            DataLogger.shared.createSensorData(openBotPath: Strings.forwardSlash + baseDirectory);
            dataLogger.deleteFiles(path: Strings.forwardSlash + baseDirectory)
            dataLogger.setupFilesForLogging()
        }
    }

    @objc func updateControllerValues() {
        print("test in data collection");
        if (connectedController == nil) {
            return
        }
        print(Strings.controllerConnected)
        let controller = connectedController;
        let batteryLevel = String(format: "%.2f", controller!.battery.unsafelyUnwrapped.batteryLevel * 100);
        print(batteryLevel);
        controller?.extendedGamepad?.valueChangedHandler = { [self] gamepad, element in
            let control = gameControllerObj?.processJoystickInput(mode: selectedDriveMode, gamepad: gamepad) ?? vehicleControl;
            sendControl(control: control);

            let keyCommand = gameControllerObj?.processControllerKeyData(element: element);
            sendKeyUpdates(keyCommand: keyCommand ?? "");
        }
    }

    func sendControl(control: Control) {
        print("hello nitish")
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
            let left = control.getLeft() * selectedSpeedMode.rawValue;
            let right = control.getRight() * selectedSpeedMode.rawValue;
            print("d" + String(left) + "," + String(right) + "\n");
            dataLogger.ctrlLog = dataLogger.ctrlLog + String(returnCurrentTimestamp()) + " " + String(left) + " " + String(right) + "\n";
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n");
            print("abcdef ", dataLogger.ctrlLog)
        }
    }


    @objc func sendKeyUpdates(keyCommand: Any) {
        switch (keyCommand) {
        case IndicatorEvent.Right:
            setIndicator(keyCommand: keyCommand as! IndicatorEvent);
            break;
        case IndicatorEvent.Left:
            setIndicator(keyCommand: keyCommand as! IndicatorEvent);
            break;
        case IndicatorEvent.Stop:
            setIndicator(keyCommand: keyCommand as! IndicatorEvent);
            break;
        case ControlEvent.STOP:
            sendControl(control: Control());
            break;
        case ControlEvent.FORWARD:
            break;
        case CMD_Events.TOGGLE_LOGS:
            switchLogging();
            break;
        default:
            break;
        }
    }

    func setIndicator(keyCommand: IndicatorEvent) {
        let indicatorValues: String = gameControllerObj?.getIndicatorEventValue(event: keyCommand) ?? "";
        if (indicator != indicatorValues) {
            let index = indicatorValues.index(after: indicatorValues.startIndex)

            let actualValue = indicatorValues[index...]
//            dataLogger.indicator = dataLogger.indicator + String(returnCurrentTimestamp()) + " " + keyCommand.rawValue + "\n";
            bluetooth.sendData(payload: indicatorValues);
            indicator = indicatorValues;
        }
    }

    @objc func updateControlMode(_ notification: Notification?) {
        print("updated")
        if let controlMode = notification?.userInfo?["mode"] as? ControlMode {
            selectedControlMode = controlMode;
        }
        print(selectedControlMode);
        if selectedControlMode == .gamepad {
            print("inside if condition");
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
        }
    }

    @objc func updateSpeedMode(_ notification: Notification) {
        if let speedMode = notification.userInfo?["speed"] as? SpeedMode {
            selectedSpeedMode = speedMode;
        }
    }

    @objc func updatePreview(_ notification: Notification) {
        isPreviewSelected = !isPreviewSelected
    }

    @objc func updateTraining(_ notification: Notification) {
        isTrainingSelected = !isTrainingSelected
    }
}
