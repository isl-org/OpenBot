//
//  GameViewController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 20/08/22.
//

import UIKit
import GameController

class GameViewController: UIViewController {
    @IBOutlet weak var logTextView: UITextView!
    var dateFormatter = DateFormatter()
    private let maximumControllerCount: Int = 1
    var gameControllerObj: GameController?;
    weak var delegate: InputManagerDelegate?
    let overlayLeft = Draw(frame: CGRect(origin: CGPoint(x: 50, y: 170), size: CGSize(width: 18, height: 18)))
    let overlayRight = Draw(frame: CGRect(origin: CGPoint(x: 92, y: 170), size: CGSize(width: 18, height: 18)))
    let overlayUp = Draw(frame: CGRect(origin: CGPoint(x: 72, y: 148), size: CGSize(width: 18, height: 18)))
    let overlayDown = Draw(frame: CGRect(origin: CGPoint(x: 72, y: 190), size: CGSize(width: 18, height: 18)))

    let overlayA = Draw(frame: CGRect(origin: CGPoint(x: 298, y: 192), size: CGSize(width: 28, height: 28)))
    let overlayB = Draw(frame: CGRect(origin: CGPoint(x: 325, y: 164), size: CGSize(width: 28, height: 28)))
    let overlayX = Draw(frame: CGRect(origin: CGPoint(x: 270, y: 164), size: CGSize(width: 28, height: 28)))
    let overlayY = Draw(frame: CGRect(origin: CGPoint(x: 298, y: 137), size: CGSize(width: 28, height: 28)))

    let overlayOptions = Draw(frame: CGRect(origin: CGPoint(x: 115, y: 140), size: CGSize(width: 12, height: 12)))
    let overlayMenu = Draw(frame: CGRect(origin: CGPoint(x: 265, y: 140), size: CGSize(width: 12, height: 12)))

    let overlayLeftShoulder = Draw(frame: CGRect(origin: CGPoint(x: 78, y: 105), size: CGSize(width: 28, height: 28)))
    let overlayRightShoulder = Draw(frame: CGRect(origin: CGPoint(x: 310, y: 105), size: CGSize(width: 28, height: 28)))

    let overlayLeftThumb = Draw(frame: CGRect(origin: CGPoint(x: 112, y: 207), size: CGSize(width: 46, height: 46)))
    let overlayRightThumb = Draw(frame: CGRect(origin: CGPoint(x: 232, y: 207), size: CGSize(width: 46, height: 46)))

    var restrictRotation: UIInterfaceOrientationMask = .portrait

    override func viewWillAppear(_ animated: Bool) {
        AppDelegate.AppUtility.lockOrientation(UIInterfaceOrientationMask.portrait, andRotateTo: UIInterfaceOrientation.portrait)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        dateFormatter.dateFormat = "HH:mm:ss.SSSS"
        clearLog()
        NotificationCenter.default.addObserver(self, selector: #selector(didConnectController), name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil)

    }


    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        gameControllerObj = GameController();
        didConnectController();
    }



    override func viewWillDisappear(_ animated: Bool) {
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        AppDelegate.AppUtility.lockOrientation(UIInterfaceOrientationMask.all)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    func clearLog() {
        logTextView.text = ""
    }

    func writeToLog(newLine: String) {
        logTextView.text = newLine + "\n" + logTextView.text
    }

    @objc func didConnectController() {
        if (connectedController == nil) {
            return
        }
        writeToLog(newLine: Strings.controllerConnected)
        let controller = connectedController;
        let batteryLevel = String(format: "%.2f", controller!.battery.unsafelyUnwrapped.batteryLevel * 100);
        writeToLog(newLine: "Battery Level:" + batteryLevel);
        delegate?.inputManager(self, didConnect: controller!)

        controller!.extendedGamepad?.dpad.left.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("←", pressed, self.overlayLeft)
        }
        controller!.extendedGamepad?.dpad.right.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("→", pressed, self.overlayRight)
        }
        controller!.extendedGamepad?.dpad.up.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("↑", pressed, self.overlayUp)
        }
        controller!.extendedGamepad?.dpad.down.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("↓", pressed, self.overlayDown)
        }

        controller!.extendedGamepad?.buttonA.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("A", pressed, self.overlayA)
        }
        controller!.extendedGamepad?.buttonB.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("B", pressed, self.overlayB)
        }
        controller!.extendedGamepad?.buttonX.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("X", pressed, self.overlayX)
        }
        controller!.extendedGamepad?.buttonY.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("Y", pressed, self.overlayY)
        }

        controller!.extendedGamepad?.buttonOptions?.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("SHARE", pressed, self.overlayOptions)
        }
        controller!.extendedGamepad?.buttonMenu.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("OPTIONS", pressed, self.overlayMenu)
        }

        controller!.extendedGamepad?.leftShoulder.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("L1", pressed, self.overlayLeftShoulder)
        }
        controller!.extendedGamepad?.rightShoulder.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("R1", pressed, self.overlayRightShoulder)
        }

        controller!.extendedGamepad?.leftTrigger.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("L2", pressed, self.overlayLeftShoulder)
        }
        controller!.extendedGamepad?.leftTrigger.valueChangedHandler = { (button, value, pressed) in
            self.triggerChangedHandler("L2", value, pressed)
        }
        controller!.extendedGamepad?.rightTrigger.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("R2", pressed, self.overlayRightShoulder)
        }
        controller!.extendedGamepad?.rightTrigger.valueChangedHandler = { (button, value, pressed) in
            self.triggerChangedHandler("R2", value, pressed)
        }

        controller!.extendedGamepad?.leftThumbstick.valueChangedHandler = { (button, xvalue, yvalue) in
            self.thumbstickChangedHandler("THUMB-LEFT", xvalue, yvalue)
        }
        controller!.extendedGamepad?.rightThumbstick.valueChangedHandler = { (button, xvalue, yvalue) in
            self.thumbstickChangedHandler("THUMB-RIGHT", xvalue, yvalue)
        }

        controller!.extendedGamepad?.leftThumbstickButton?.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("THUMB-LEFT", pressed, self.overlayLeftThumb)
        }
        controller!.extendedGamepad?.rightThumbstickButton?.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("THUMB-RIGHT", pressed, self.overlayRightThumb)
        }
    }

    func getTimestamp() -> String {
        dateFormatter.string(from: Date())
    }

    func buttonChangedHandler(_ button: String, _ pressed: Bool, _ overlay: UIView) {
        if pressed {
            writeToLog(newLine: getTimestamp() + " - " + button + " " + "down")
            view.addSubview(overlay)
        } else {
            writeToLog(newLine: getTimestamp() + " - " + button + " " + "up")
            overlay.removeFromSuperview()
        }
    }

    func triggerChangedHandler(_ button: String, _ value: Float, _ pressed: Bool) {
        if pressed {
            let analogValue = String(format: "%.2f", value)
            writeToLog(newLine: getTimestamp() + " - " + button + " " + analogValue)
        }
    }

    func thumbstickChangedHandler(_ button: String, _ xvalue: Float, _ yvalue: Float) {
        let analogValueX = String(format: "%.2f", xvalue)
        let analogValueY = String(format: "%.2f", yvalue)
        writeToLog(newLine: getTimestamp() + " - " + button + " " + analogValueX + " / " + analogValueY)
    }

}

protocol InputManagerDelegate: AnyObject {
    func inputManager(_ manager: GameViewController, didConnect controller: GCController)
    func inputManager(_ manager: GameViewController, didDisconnect controller: GCController)
}

class Draw: UIView {
    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = UIColor(white: 1, alpha: 0.0)
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func draw(_ rect: CGRect) {
        let bpath = UIBezierPath(arcCenter: CGPoint(x: rect.height / 2, y: rect.width / 2),
                radius: CGFloat(rect.height / 2),
                startAngle: CGFloat(0),
                endAngle: CGFloat(Double.pi * 2),
                clockwise: true)

        let color: UIColor = UIColor.red
        color.set()
        bpath.fill()
    }

}