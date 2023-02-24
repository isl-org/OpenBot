//
// Created by Sparsh Jain on 20/08/22.
//

import UIKit
import GameController

class GameViewController: UIViewController {

    var dateFormatter = DateFormatter()
    private let maximumControllerCount: Int = 1
    var gameController = GameController.shared
    weak var delegate: InputManagerDelegate?
    let controllerImage: UIImageView! = nil

    let overlayLeft = DrawRect(frame: CGRect(origin: CGPoint(x: 27, y: 295), size: CGSize(width: 25, height: 20)))
    let overlayRight = DrawRect(frame: CGRect(origin: CGPoint(x: 71, y: 295), size: CGSize(width: 25, height: 20)))
    let overlayUp = DrawRect(frame: CGRect(origin: CGPoint(x: 51, y: 271), size: CGSize(width: 20, height: 25)))
    let overlayDown = DrawRect(frame: CGRect(origin: CGPoint(x: 51, y: 314), size: CGSize(width: 20, height: 25)))

    let overlayA = DrawCircle(frame: CGRect(origin: CGPoint(x: 297, y: 322), size: CGSize(width: 28, height: 28)))
    let overlayB = DrawCircle(frame: CGRect(origin: CGPoint(x: 326, y: 293), size: CGSize(width: 28, height: 28)))
    let overlayX = DrawCircle(frame: CGRect(origin: CGPoint(x: 268, y: 293), size: CGSize(width: 28, height: 28)))
    let overlayY = DrawCircle(frame: CGRect(origin: CGPoint(x: 297, y: 264), size: CGSize(width: 28, height: 28)))

    let overlayOptions = DrawRect(frame: CGRect(origin: CGPoint(x: 141, y: 299), size: CGSize(width: 20, height: 12)))
    let overlayMenu = DrawRect(frame: CGRect(origin: CGPoint(x: 210, y: 299), size: CGSize(width: 20, height: 12)))

    let overlayL1Shoulder = DrawRect(frame: CGRect(origin: CGPoint(x: 34, y: 188), size: CGSize(width: 56, height: 25)))
    let overlayL2Shoulder = DrawRect(frame: CGRect(origin: CGPoint(x: 34, y: 153), size: CGSize(width: 56, height: 25)))
    let overlayR1Shoulder = DrawRect(frame: CGRect(origin: CGPoint(x: 280, y: 188), size: CGSize(width: 56, height: 25)))
    let overlayR2Shoulder = DrawRect(frame: CGRect(origin: CGPoint(x: 280, y: 153), size: CGSize(width: 56, height: 25)))

    let overlayLeftThumb = DrawCircle(frame: CGRect(origin: CGPoint(x: 100, y: 338), size: CGSize(width: 46, height: 46)))
    let overlayRightThumb = DrawCircle(frame: CGRect(origin: CGPoint(x: 225, y: 338), size: CGSize(width: 46, height: 46)))

    var overlayLeftThumb_var = DrawCircle(frame: CGRect(origin: CGPoint(x: 100, y: 338), size: CGSize(width: 46, height: 46)))
    var overlayRightThumb_var = DrawCircle(frame: CGRect(origin: CGPoint(x: 225, y: 338), size: CGSize(width: 46, height: 46)))

    var restrictRotation: UIInterfaceOrientationMask = .portrait

    /// Notifies the view controller that its view is about to be added to a view hierarchy.
    override func viewWillAppear(_ animated: Bool) {
        AppDelegate.AppUtility.lockOrientation(UIInterfaceOrientationMask.portrait, andRotateTo: UIInterfaceOrientation.portrait)
    }

    /// Called after the view controller has loaded.
    override func viewDidLoad() {
        super.viewDidLoad()
        dateFormatter.dateFormat = "HH:mm:ss.SSSS"
        gameController.resetControl = true

        NotificationCenter.default.addObserver(self, selector: #selector(didConnectController), name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil)
    }

    /// Initialization routine
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        didConnectController();
    }

    override func viewWillDisappear(_ animated: Bool) {
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        AppDelegate.AppUtility.lockOrientation(UIInterfaceOrientationMask.all)
    }

    /// Called after the view was dismissed, covered or otherwise hidden.
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    @objc func didConnectController() {
        if (connectedController == nil) {
            return
        }

        let controller = connectedController;
        _ = String(format: "%.2f", controller!.battery.unsafelyUnwrapped.batteryLevel * 100);

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
            self.buttonChangedHandler("SELECT", pressed, self.overlayOptions)
        }
        controller!.extendedGamepad?.buttonMenu.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("OPTIONS", pressed, self.overlayMenu)
        }
        controller!.extendedGamepad?.leftShoulder.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("L1", pressed, self.overlayL1Shoulder)
        }
        controller!.extendedGamepad?.rightShoulder.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("R1", pressed, self.overlayR1Shoulder)
        }
        controller!.extendedGamepad?.leftTrigger.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("L2", pressed, self.overlayL2Shoulder)
        }
        controller!.extendedGamepad?.leftTrigger.valueChangedHandler = { (button, value, pressed) in
            self.triggerChangedHandler("L2", value, pressed)
        }
        controller!.extendedGamepad?.rightTrigger.pressedChangedHandler = { (button, value, pressed) in
            self.buttonChangedHandler("R2", pressed, self.overlayR2Shoulder)
        }
        controller!.extendedGamepad?.rightTrigger.valueChangedHandler = { (button, value, pressed) in
            self.triggerChangedHandler("R2", value, pressed)
        }
        controller!.extendedGamepad?.leftThumbstick.valueChangedHandler = { (button, xvalue, yvalue) in
            self.thumbstickChangedHandlerLeft("THUMB-LEFT", xvalue, yvalue)
        }
        controller!.extendedGamepad?.rightThumbstick.valueChangedHandler = { (button, xvalue, yvalue) in
            self.thumbstickChangedHandlerRight("THUMB-RIGHT", xvalue, yvalue)
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

            view.addSubview(overlay)
        } else {

            overlay.removeFromSuperview()
        }
    }

    func triggerChangedHandler(_ button: String, _ value: Float, _ pressed: Bool) {
        if pressed {
            _ = String(format: "%.2f", value)

        }
    }

    func thumbstickChangedHandlerLeft(_ button: String, _ xvalue: Float, _ yvalue: Float) {
        if (xvalue == 0 && yvalue == 0) {
            overlayLeftThumb_var.removeFromSuperview()
        } else {
            overlayLeftThumb_var.removeFromSuperview()
            overlayLeftThumb_var = DrawCircle(frame: CGRect(origin: CGPoint(x: 100 + Int(xvalue * 12), y: 338 - Int(yvalue * 12)), size: CGSize(width: 46, height: 46)))
            view.addSubview(overlayLeftThumb_var)
        }
    }

    func thumbstickChangedHandlerRight(_ button: String, _ xvalue: Float, _ yvalue: Float) {
        if (xvalue == 0 && yvalue == 0) {
            overlayRightThumb_var.removeFromSuperview()
        } else {
            overlayRightThumb_var.removeFromSuperview()
            overlayRightThumb_var = DrawCircle(frame: CGRect(origin: CGPoint(x: 225 + Int(xvalue * 12), y: 338 - Int(yvalue * 12)), size: CGSize(width: 46, height: 46)))
            view.addSubview(overlayRightThumb_var)
        }
    }
}

protocol InputManagerDelegate: AnyObject {
    func inputManager(_ manager: GameViewController, didConnect controller: GCController)
    func inputManager(_ manager: GameViewController, didDisconnect controller: GCController)
}

class DrawCircle: UIView {
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

class DrawRect: UIView {
    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = UIColor(white: 1, alpha: 0.0)
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func draw(_ rect: CGRect) {
        let bpath = UIBezierPath(roundedRect: rect, cornerRadius: rect.width / 10)
        let color: UIColor = UIColor.red
        color.set()
        bpath.fill()
    }
}

