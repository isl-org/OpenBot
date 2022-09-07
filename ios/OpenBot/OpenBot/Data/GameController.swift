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

    public func convertDualToControl(leftStick: Float, rightStick: Float) -> Control {
        Control(left: leftStick, right: rightStick);
    }

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
}
