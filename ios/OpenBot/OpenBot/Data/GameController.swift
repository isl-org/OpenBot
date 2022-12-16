//
//  GameController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 20/08/22.
//

import Foundation
import GameController

public var connectedController: GCController?;

class GameController: GCPhysicalInputProfile {
    static let shared: GameController = GameController();
    private let maximumControllerCount: Int = 1
    private(set) var controllers = Set<GCController>()
    private var panRecognizer: UIPanGestureRecognizer!
    var selectedSpeedMode: SpeedMode = SpeedMode.SLOW;
    var selectedControlMode: ControlMode = ControlMode.GAMEPAD;
    var selectedDriveMode: DriveMode = DriveMode.JOYSTICK;
    var vehicleControl = Control();
    let bluetooth = bluetoothDataController.shared;
    let dataLogger = DataLogger.shared
    var controlData: String = ""
    var indicatorData: String = ""
    var indicator = "i0,0\n";

    override init() {
        super.init()
        NotificationCenter.default.addObserver(self,
                selector: #selector(didConnectController),
                name: NSNotification.Name.GCControllerDidConnect,
                object: nil)
        NotificationCenter.default.addObserver(self,
                selector: #selector(didDisconnectController),
                name: NSNotification.Name.GCControllerDidDisconnect,
                object: nil)

        GCController.startWirelessControllerDiscovery {
        }
    }

    deinit {
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.GCControllerDidConnect, object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name.GCControllerDidDisconnect, object: nil)
        GCController.stopWirelessControllerDiscovery()
    }

    @objc func didConnectController(_ notification: Notification) {
        guard controllers.count < maximumControllerCount else {
            return
        }
        let controller = notification.object as! GCController;
        controllers.insert(controller);
        connectedController = controller;
        let nc = NotificationCenter.default;
        nc.post(name: Notification.Name(rawValue: Strings.controllerConnected), object: self)
    }

    @objc func didDisconnectController(_ notification: Notification) {
        let controller = notification.object as! GCController
        controllers.remove(controller)
    }

    /**

     - Parameters:
       - mode:
       - gamepad:
     - Returns:
     */
    public func processJoystickInput(mode: DriveMode, gamepad: GCExtendedGamepad) -> Control {
        switch (mode) {
        case .DUAL:
            return convertDualToControl(leftStick: getCenteredAxis(gamepad.leftThumbstick.yAxis.value), rightStick: getCenteredAxis(gamepad.rightThumbstick.yAxis.value));
        case .GAME:
            var rightTrigger: Float = getCenteredAxis(gamepad.rightShoulder.value);
            if (rightTrigger == 0) {
                rightTrigger = getCenteredAxis(gamepad.rightTrigger.value);
            }

            var leftTrigger: Float = getCenteredAxis(gamepad.leftShoulder.value);
            if (leftTrigger == 0) {
                leftTrigger = getCenteredAxis(gamepad.leftTrigger.value);
            }

            var steeringOffset: Float = getCenteredAxis(gamepad.leftThumbstick.xAxis.value);
            if (steeringOffset == 0) {
                steeringOffset = getCenteredAxis(gamepad.dpad.yAxis.value);
            }
            if (steeringOffset == 0) {
                steeringOffset = getCenteredAxis(gamepad.rightThumbstick.xAxis.value);
            }

            return convertGameToControl(leftTrigger: leftTrigger, rightTrigger: rightTrigger, steeringOffset: steeringOffset);
        case .JOYSTICK:
            var yAxis: Float = getCenteredAxis(gamepad.leftThumbstick.yAxis.value);
            if (yAxis == 0) {
                yAxis = getCenteredAxis(gamepad.dpad.yAxis.value);
            }

            if (yAxis == 0) {
                yAxis = getCenteredAxis(gamepad.rightThumbstick.yAxis.value);
            }

            var xAxis: Float = getCenteredAxis(gamepad.leftThumbstick.xAxis.value);
            if (xAxis == 0) {
                xAxis = getCenteredAxis(gamepad.dpad.xAxis.value);
            }
            if (xAxis == 0) {
                xAxis = getCenteredAxis(gamepad.rightThumbstick.xAxis.value);
            }
            return convertJoystickToControl(xAxis: xAxis, yAxis: yAxis);
        }
    }

    private func getCenteredAxis(_ value: Float) -> Float {
        return value;
    }

    /**
     function to return dual mode control values to openbot device movement values.
     - Parameters:
       - leftStick: left joystick yAxis values.
       - rightStick: right joystick yAxis values.
     - Returns: return control object with left and right values.
     */
    public func convertDualToControl(leftStick: Float, rightStick: Float) -> Control {
        Control(left: leftStick, right: rightStick);
    }

    /**
     function to convert controller input for joystick mode to values to return to openbot device to control the navigation.
     - Parameters:
       - xAxis: xAxis position of the joystick
       - yAxis: yAxis position of the joystick
     - Returns: returns control object with left and right values.
     */
    public func convertJoystickToControl(xAxis: Float, yAxis: Float) -> Control {
        var left = yAxis;
        var right = yAxis;

        if (left >= 0) {
            left += xAxis;
        } else {
            left -= xAxis;
        }
        if (right >= 0) {
            right -= xAxis;
        } else {
            right += xAxis;
        }
        return Control(left: left, right: right);
    }

    /**
     function to convert controller input for game mode to the values to return to openbot device to control the device movement.
     - Parameters:
       - leftTrigger: left control of device.
       - rightTrigger: right control of device.
       - steeringOffset: offset in left and right directions.
     - Returns: Returns Control object with left and right values.
     */
    public func convertGameToControl(leftTrigger: Float, rightTrigger: Float, steeringOffset: Float) -> Control {
        var left = rightTrigger - leftTrigger;
        var right = rightTrigger - leftTrigger;

        if (left >= 0) {
            left += steeringOffset;
        } else {
            left -= steeringOffset;
        }
        if (right >= 0) {
            right -= steeringOffset;
        } else {
            right += steeringOffset;
        }
        return Control(left: left, right: right);
    }

    /**
     function to process the controller keys and create events out of them.
     - Parameters:
       - element: takes controllerElement as input and using localized_names of controller and map it to Keymap[enum].
     - Returns: Events
     */
    public func processControllerKeyData(element: GCControllerElement) -> Any {

        switch (element.localizedName) {
        case Keymap.KEYCODE_BUTTON_X.rawValue:
            if (connectedController?.extendedGamepad?.buttonX.isPressed == false) {
                return IndicatorEvent.LEFT;
            }
        case Keymap.KEYCODE_BUTTON_Y.rawValue:
            if (connectedController?.extendedGamepad?.buttonY.isPressed == false) {
                return ControlEvent.STOP;
            }
        case Keymap.KEYCODE_BUTTON_B.rawValue:
            if (connectedController?.extendedGamepad?.buttonB.isPressed == false) {
                return IndicatorEvent.RIGHT;
            }
        case Keymap.KEYCODE_BUTTON_A.rawValue:
            if (connectedController?.extendedGamepad?.buttonA.isPressed == false) {
                return CMD_Events.TOGGLE_LOGS;
            }
        case Keymap.KEYCODE_BUTTON_START.rawValue:
            if (connectedController?.extendedGamepad?.buttonOptions?.isPressed == false) {
                return CMD_Events.TOGGLE_NOISE;
            }
        case Keymap.KEYCODE_BUTTON_L1.rawValue:
            if (connectedController?.extendedGamepad?.leftShoulder.isPressed == false) {
                return DriveMode.JOYSTICK;
            }
        case Keymap.KEYCODE_BUTTON_R1.rawValue:
            if (connectedController?.extendedGamepad?.rightShoulder.isPressed == false) {
                return CMD_Events.TOGGLE_NETWORK;
            }
        case Keymap.KEYCODE_BUTTON_THUMBL.rawValue:
            if (connectedController?.extendedGamepad?.leftThumbstickButton?.isPressed == false) {
                return CMD_Events.CMD_SPEED_DOWN;
            }
        case Keymap.KEYCODE_BUTTON_THUMBR.rawValue:
            if (connectedController?.extendedGamepad?.rightThumbstickButton?.isPressed == false) {
                return CMD_Events.CMD_SPEED_UP;
            }
        case Keymap.CMD_INDICATOR_STOP.rawValue:
            return IndicatorEvent.STOP
        default:
            return "";
        }
        return "";
    }

    /**
     function to get the values to return to openbot for update in indicator.
     - Parameter event: [IndicatorEvent] enum
     - Returns: [String]
     */
    public func getIndicatorEventValue(event: IndicatorEvent) -> String {
        if (event == IndicatorEvent.STOP) {
            return "i0,0\n";
        } else if (event == IndicatorEvent.LEFT) {
            return "i1,0\n"
        } else if (event == IndicatorEvent.RIGHT) {
            return "i0,1\n";
        }
        return "i1,1\n";
    }

    func updateControllerValues() {
        if (connectedController == nil) {
            return
        }
        print(Strings.controllerConnected)
        let controller = connectedController;
        let batteryLevel = String(format: "%.2f", controller!.battery.unsafelyUnwrapped.batteryLevel * 100);
        print(batteryLevel);
        controller?.extendedGamepad?.valueChangedHandler = { [self] gamepad, element in
            let control = processJoystickInput(mode: selectedDriveMode, gamepad: gamepad);
            sendControl(control: control);
            let keyCommand = processControllerKeyData(element: element);
            sendKeyUpdates(keyCommand: keyCommand);
        }
    }

    func sendControl(control: Control) {
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
            let left = (control.getLeft() * selectedSpeedMode.rawValue).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero);
            let right = (control.getRight() * selectedSpeedMode.rawValue).rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero);
            vehicleControl = control;
            print("c" + String(left) + "," + String(right) + "\n");
            dataLogger.setControlLogs(left: (String(left)), right: String(right))
            controlData = String(left) + " " + String(right)
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n");
            NotificationCenter.default.post(name: .updateSpeedLabel, object: String(Int(left)) + "," + String(Int(right)));
        }
    }

    @objc func sendKeyUpdates(keyCommand: Any) {
        switch (keyCommand) {
        case IndicatorEvent.RIGHT:
            setIndicator(keyCommand: keyCommand as! IndicatorEvent);
            break;
        case IndicatorEvent.LEFT:
            setIndicator(keyCommand: keyCommand as! IndicatorEvent);
            break;
        case IndicatorEvent.STOP:
            setIndicator(keyCommand: keyCommand as! IndicatorEvent);
            break;
        case ControlEvent.STOP:
            sendControl(control: Control());
            break;
        case ControlEvent.FORWARD:
            break;
        case CMD_Events.TOGGLE_LOGS:
            toggleLogging()
            break;
        case CMD_Events.TOGGLE_NETWORK:
            toggleNetwork()
            break;
        case CMD_Events.CMD_SPEED_UP:
            NotificationCenter.default.post(name: .increaseSpeedMode, object: nil);
            break;
        case CMD_Events.CMD_SPEED_DOWN:
            NotificationCenter.default.post(name: .decreaseSpeedMode, object: nil);
            break;
        case DriveMode.JOYSTICK:
            updateDriveMode()
        default:
            break;
        }
    }

    func setIndicator(keyCommand: IndicatorEvent) {
        let indicatorValues: String = getIndicatorEventValue(event: keyCommand);
        if (indicator != indicatorValues) {
            dataLogger.setIndicatorLogs(indicator: String(keyCommand.rawValue))
            indicatorData = String(keyCommand.rawValue)
            bluetooth.sendData(payload: indicatorValues);
            indicator = indicatorValues;
        }
    }

    func toggleLogging() {
        NotificationCenter.default.post(name: .logData, object: nil)
    }

    func toggleNetwork() {
        NotificationCenter.default.post(name: .toggleNetworks, object: nil)
    }

    func updateDriveMode() {
        NotificationCenter.default.post(name: .updateDriveMode, object: nil)
    }


}

