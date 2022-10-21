//
//  GameController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 20/08/22.
//

import Foundation
import GameController

public var connectedController: GCController?;

class GameController: GCController {
    static let shared: GameController = GameController();
    private let maximumControllerCount: Int = 1
    private(set) var controllers = Set<GCController>()
    private var panRecognizer: UIPanGestureRecognizer!
    var selectedSpeedMode: SpeedMode = SpeedMode.slow;
    var selectedControlMode: ControlMode = ControlMode.gamepad;
    var selectedDriveMode: DriveMode = DriveMode.joystick;
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
        case .dual:
            return convertDualToControl(leftStick: gamepad.leftThumbstick.yAxis.value.rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero), rightStick: gamepad.rightThumbstick.yAxis.value.rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero));
        case .gameController:
            var rightTrigger: Float = gamepad.rightShoulder.value;
            if (rightTrigger == 0) {
                rightTrigger = gamepad.rightTrigger.value;
            }

            var leftTrigger: Float = gamepad.leftShoulder.value;
            if (leftTrigger == 0) {
                leftTrigger = gamepad.leftTrigger.value;
            }

            var steeringOffset: Float = gamepad.leftThumbstick.xAxis.value.rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero);
            if (steeringOffset == 0) {
                steeringOffset = gamepad.dpad.yAxis.value;
            }
            if (steeringOffset == 0) {
                steeringOffset = gamepad.rightThumbstick.xAxis.value.rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero);
            }

            return convertGameToControl(leftTrigger: leftTrigger, rightTrigger: rightTrigger, steeringOffset: steeringOffset);
        case .joystick:
            var yAxis: Float = gamepad.leftThumbstick.yAxis.value.rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero);
            if (yAxis == 0) {
                yAxis = gamepad.dpad.yAxis.value;
            }

            if (yAxis == 0) {
                yAxis = gamepad.rightThumbstick.yAxis.value.rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero);
            }

            var xAxis: Float = gamepad.leftThumbstick.xAxis.value.rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero);
            if (xAxis == 0) {
                xAxis = gamepad.dpad.xAxis.value;
            }
            if (xAxis == 0) {
                xAxis = gamepad.rightThumbstick.xAxis.value.rounded(FloatingPointRoundingRule.toNearestOrAwayFromZero);
            }
            return convertJoystickToControl(xAxis: xAxis, yAxis: yAxis);
        }
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
        case Keymap.KEY_CIRCLE.rawValue:
            return IndicatorEvent.Right;
        case Keymap.KEY_SQUARE.rawValue:
            return IndicatorEvent.Left;
        case Keymap.KEY_TRIANGLE.rawValue:
            return ControlEvent.STOP;
        case Keymap.KEY_CROSS.rawValue:
            return CMD_Events.TOGGLE_LOGS;
        case Keymap.KEY_Options.rawValue:
            return CMD_Events.TOGGLE_NOISE;
        case Keymap.KEY_R1.rawValue:
            return CMD_Events.TOGGLE_NOISE;
        default:
            return "";
        }
    }

    /**
     function to get the values to return to openbot for update in indicator.
     - Parameter event: [IndicatorEvent] enum
     - Returns: [String]
     */
    public func getIndicatorEventValue(event: IndicatorEvent) -> String {
        if (event == IndicatorEvent.Stop) {
            return "i0,0\n";
        } else if (event == IndicatorEvent.Left) {
            return "i1,0\n"
        } else if (event == IndicatorEvent.Right) {
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
            let left = control.getLeft() * selectedSpeedMode.rawValue;
            let right = control.getRight() * selectedSpeedMode.rawValue;
            NotificationCenter.default.post(name: .updateSpeedLabel, object: String(left) + "," + String(right));
            vehicleControl = control;
            print("c" + String(left) + "," + String(right) + "\n");
            dataLogger.setControlLogs(left: (String(left)), right: String(right))
            controlData = String(left) + " " + String(right)
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n");
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
            break;
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


}

extension Notification.Name {
    static let updateSpeedLabel = Notification.Name("updateSpeedLabel")
}