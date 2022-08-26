//
//  GameViewController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 20/08/22.
//

import Foundation
import GameController

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

    func writeToLog(newLine: String) {
        print(newLine);
    }

    func changeHandler(gamePad: GCExtendedGamepad, element: GCControllerElement) {
        print(element);
    }

    @objc func didConnectController(_ notification: Notification) {
        writeToLog(newLine: "didConnectController")
        guard controllers.count < maximumControllerCount else {
            return
        }
        let controller = notification.object as! GCController
        let batteryLevel = String(format: "%.2f", controller.battery.unsafelyUnwrapped.batteryLevel * 100);
        writeToLog(newLine: "Battery Level:" + batteryLevel);

        controllers.insert(controller)

        controller.extendedGamepad?.valueChangedHandler = changeHandler;
//        controller.extendedGamepad?.dpad.left.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("←", pressed)
//        }
//        controller.extendedGamepad?.dpad.right.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("→", pressed)
//        }
//        controller.extendedGamepad?.dpad.up.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("↑", pressed)
//        }
//        controller.extendedGamepad?.dpad.down.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("↓", pressed)
//        }
//
//        // buttonA is labeled "X" (blue) on PS4 controller
//        controller.extendedGamepad?.buttonA.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("A", pressed)
//        }
//        // buttonB is labeled "circle" (red) on PS4 controller
//        controller.extendedGamepad?.buttonB.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("B", pressed)
//        }
//        // buttonX is labeled "square" (pink) on PS4 controller
//        controller.extendedGamepad?.buttonX.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("X", pressed)
//        }
//        // buttonY is labeled "triangle" (green) on PS4 controller
//        controller.extendedGamepad?.buttonY.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("Y", pressed)
//        }
//
//        // buttonOptions is labeled "SHARE" on PS4 controller
//        controller.extendedGamepad?.buttonOptions?.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("SHARE", pressed)
//        }
//        // buttonMenu is labeled "OPTIONS" on PS4 controller
//        controller.extendedGamepad?.buttonMenu.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("OPTIONS", pressed)
//        }
//
//        controller.extendedGamepad?.leftShoulder.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("L1", pressed)
//        }
//        controller.extendedGamepad?.rightShoulder.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("R1", pressed)
//        }
//
//        controller.extendedGamepad?.leftTrigger.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("L2", pressed)
//        }
//        controller.extendedGamepad?.leftTrigger.valueChangedHandler = { (button, value, pressed) in
//            self.triggerChangedHandler("L2", value, pressed)
//        }
//        controller.extendedGamepad?.rightTrigger.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("R2", pressed)
//        }
//        controller.extendedGamepad?.rightTrigger.valueChangedHandler = { (button, value, pressed) in
//            self.triggerChangedHandler("R2", value, pressed)
//        }
//
//        controller.extendedGamepad?.leftThumbstick.valueChangedHandler = { (button, xvalue, yvalue) in
//            self.thumbstickChangedHandler("THUMB-LEFT", xvalue, yvalue)
//        }
//        controller.extendedGamepad?.rightThumbstick.valueChangedHandler = { (button, xvalue, yvalue) in
//            self.thumbstickChangedHandler("THUMB-RIGHT", xvalue, yvalue)
//        }
//
//        controller.extendedGamepad?.leftThumbstickButton?.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("THUMB-LEFT", pressed)
//        }
//        controller.extendedGamepad?.rightThumbstickButton?.pressedChangedHandler = { (button, value, pressed) in
//            self.buttonChangedHandler("THUMB-RIGHT", pressed)
//        }
    }

    @objc func didDisconnectController(_ notification: Notification) {
        writeToLog(newLine: "didDisconnectController")
        let controller = notification.object as! GCController
        controllers.remove(controller)
    }

    func buttonChangedHandler(_ button: String, _ pressed: Bool) {
        if pressed {
            writeToLog(newLine: " - " + button + " " + "down")
        } else {
            writeToLog(newLine: " - " + button + " " + "up")
        }
    }

    func triggerChangedHandler(_ button: String, _ value: Float, _ pressed: Bool) {
        if pressed {
            let analogValue = String(format: "%.2f", value)
            writeToLog(newLine: " - " + button + " " + analogValue)
        }
    }

    func thumbstickChangedHandler(_ button: String, _ xvalue: Float, _ yvalue: Float) {
        let analogValueX = String(format: "%.2f", xvalue)
        let analogValueY = String(format: "%.2f", yvalue)
        writeToLog(newLine: " - " + button + " " + analogValueX + " / " + analogValueY)
    }

}
