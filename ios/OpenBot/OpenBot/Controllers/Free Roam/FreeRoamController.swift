//
//  FreeRoamController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//
import UIKit

class FreeRoamController: UIViewController, UIGestureRecognizerDelegate {
    var sonarLabel = UILabel()
    var voltageLabel = UILabel()
    var outerSonar: UIView!
    var selectedSpeedMode: SpeedMode = SpeedMode.medium;
    var selectedControlMode: ControlMode = ControlMode.gamepad;
    var selectedDriveMode: DriveMode = DriveMode.joystick;
    let bluetooth = bluetoothDataController.shared;
    var gameControllerObj: GameController?;
    var vehicleControl = Control();
    let firstView = UIView()
    let secondView = UIView()

    override func viewDidLoad() {
        super.viewDidLoad()
        createFirstView()
        createSecondView()
        createSpeedometer()
        createSonalLabel();
        createVoltageLabel()
        createLabels()
        createVoltageController(h: 0)
        createSonarController(h: 0)
        NotificationCenter.default.addObserver(self, selector: #selector(updateScreen), name: .updateLabel, object: nil)
        createDIcon()
        createDriveIcon()
        createBluetoothIcon()
        updateControlMode()
        updateGameControllerModeType()
        updateSpeedModes()
        drawTick()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }

    func createFirstView(){
        firstView.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height/2)
        view.addSubview(firstView)
    }
    func createSecondView(){
        secondView.frame = CGRect(x: 0, y: view.frame.height/2, width: view.frame.width, height: view.frame.height/2)
        view.addSubview(secondView)
        secondView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            secondView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            secondView.centerYAnchor.constraint(equalTo: view.centerYAnchor)
        ])
    }

    func createSpeedometer() {
        let speedometer = GaugeView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 256))
        speedometer.backgroundColor = .clear
        speedometer.value = 0
        speedometer.tag = 100
        view.addSubview(speedometer)
    }

    func deg2rad(_ number: CGFloat) -> CGFloat {
        number * .pi / 180
    }

    func drawTicks(count: Int, radius: Int) {
        var rotationInDegrees: CGFloat = 0
        let pointOfBreak1: Int = count / 4;
        let pointOfBreak2: Int = 3 * count / 4;
        for i in 0..<count {
            if i <= pointOfBreak1 || i >= pointOfBreak2 {
                let tick = createTick()
                let x = CGFloat(Float(187.5) + Float(radius) * cosf(2 * Float(i) * Float(Double.pi) / Float(count) - Float(Double.pi) / 2))
                let y = CGFloat(Float(240) + Float(radius) * sinf(2 * Float(i) * Float(Double.pi) / Float(count) - Float(Double.pi) / 2))
                tick.center = CGPoint(x: x, y: y)
                tick.transform = CGAffineTransform.identity.rotated(by: rotationInDegrees * .pi / 180.0)
                view.addSubview(tick)
                rotationInDegrees = rotationInDegrees + (360.0 / CGFloat(count))
            }
        }
    }

    func createTick() -> UIView {
        let tick = UIView(frame: CGRect(x: 0, y: 0, width: 2.0, height: 1.0))
        tick.backgroundColor = UIColor(red: 0.00, green: 0.44, blue: 0.77, alpha: 1.00)
        return tick
    }

    func drawTick() {
        let ticks = 40
        var radius = 120
        for _ in 0...3 {
            drawTicks(count: ticks, radius: radius)
            radius = radius - 22;
        }
    }

    func createLabels() {
        createLabel(value: "Controller", x: 35, y: 20, width: 100, height: 40)
        createLabel(value: "Speed", x: 35, y: 210, width: 100, height: 40)
        createLabel(value: "Drive Mode", x: 35, y: 115, width: 100, height: 40)
    }

    func createVoltageLabel() {
        voltageLabel.frame = CGRect(x: 35, y: 380, width: 50, height: 40)
        voltageLabel.text = "0V"
        voltageLabel.textColor = .white
        voltageLabel.font = voltageLabel.font.withSize(12)
        view.addSubview(voltageLabel)
    }

    func createSonalLabel() {
        sonarLabel.frame = CGRect(x: Int(view.frame.width) - 70, y: 380, width: 50, height: 40)
        sonarLabel.text = "0CM"
        sonarLabel.textColor = .white
        sonarLabel.font = sonarLabel.font.withSize(12)
        view.addSubview(sonarLabel)

    }

    func createDIcon() {
        let dIcon = UIButton()
        dIcon.setTitle("D", for: .normal)
        dIcon.layer.borderWidth = 1
        dIcon.layer.borderColor = UIColor.white.cgColor
        dIcon.frame = CGRect(x: 120, y: 310, width: 40, height: 40)
        dIcon.layer.cornerRadius = 5
        view.addSubview(dIcon)
    }

    func createDriveIcon() {
        let driveIconRect = createRectangle(x: 180, y: 310, width: 40, height: 40, borderColor: "borderColor")
        let driveIcon = UIImageView(frame: CGRect(x: driveIconRect.frame.size.width / 4, y: driveIconRect.frame.size.height / 4, width: 20, height: 20))
        driveIcon.image = UIImage(named: "drive")
        view.addSubview(driveIconRect)
        driveIconRect.addSubview(driveIcon)
    }

    func createBluetoothIcon() {
        let blueToothIconRect = createRectangle(x: 240, y: 310, width: 40, height: 40, borderColor: "borderColor")
        let blueToothIcon = UIImageView(frame: CGRect(x: 2 * blueToothIconRect.frame.size.width / 5, y: blueToothIconRect.frame.size.height / 4, width: 10, height: 20))
        blueToothIcon.image = Images.bluetoothConnected
        view.addSubview(blueToothIconRect)
        blueToothIconRect.addSubview(blueToothIcon)
    }

    func createVoltageController(h: Double) {
        let outerVoltage = createRectangle(x: 30, y: 280, width: 50, height: 110, borderColor: "borderColor")
        let relativeHeight = Double(h * 9.16)
        let innerVoltage = UIView(frame: CGRect(x: 0, y: 110 - relativeHeight, width: 49, height: relativeHeight - 1))
        innerVoltage.layer.cornerRadius = 5;

        innerVoltage.backgroundColor = UIColor(named: "voltageDivider")
        outerVoltage.addSubview(innerVoltage)
        view.addSubview(outerVoltage)
    }

    func createSonarController(h: Double) {
        if outerSonar != nil {
            outerSonar.subviews[0].removeFromSuperview()
        }
        let relativeHeight = Double(h * 0.3667)
        outerSonar = createRectangle(x: Int(view.frame.width) - 70, y: 280, width: 50, height: 110, borderColor: "borderColor");
        view.addSubview(outerSonar)
        let innerSonar = UIView(frame: CGRect(x: 0, y: 110 - relativeHeight, width: 49, height: relativeHeight - 1))
        innerSonar.layer.cornerRadius = 5;
        innerSonar.backgroundColor = UIColor(named: "sonar")
        outerSonar.addSubview(innerSonar)
    }

    func createLabel(value: String, x: Int, y: Int, width: Int, height: Int) {
        let label = UILabel()
        label.text = value
        label.font = UIFont(name: "medium", size: 30.0)
        label.frame = CGRect(x: x, y: y, width: width, height: height)
        label.textColor = .white
        label.font = label.font.withSize(12)
        secondView.addSubview(label)

    }

    func createRectangle(x: Int, y: Int, width: Int, height: Int, borderColor: String) -> UIView {
        let rectangleView = UIView(frame: CGRect(x: x, y: y, width: width, height: height))
        rectangleView.layer.cornerRadius = 5;
        rectangleView.layer.borderWidth = 1;
        rectangleView.layer.borderColor = UIColor(named: borderColor)?.cgColor
        return rectangleView;
    }

    func updateControlMode() {
        let gamePadController = createMode(x: 35, y: 460, width: 150, label: "Gamepad", icon: "gamepad", action: #selector(gamepadMode(_:)))
        let phoneController = createMode(x: Int(view.frame.width / 2) + 20, y: 460, width: 120, label: "Phone", icon: "phone", action: #selector(phoneMode(_:)))
        phoneController.backgroundColor = UIColor(named: "gamepad")
        gamePadController.backgroundColor = UIColor(named: "gamepad")

        if selectedControlMode == ControlMode.gamepad {
            NotificationCenter.default.addObserver(self, selector: #selector(updateControllerValues), name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil);
            gameControllerObj = GameController();
            gamePadController.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else if selectedControlMode == ControlMode.phone {
            gameControllerObj = nil;
            NotificationCenter.default.removeObserver(self, name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil);
            phoneController.backgroundColor = UIColor(named: "HomePageTitleColor")
        }
        view.addSubview(gamePadController)
        view.addSubview(phoneController)
    }

    func updateGameControllerModeType() {
        let joystick = createMode(x: 35, y: 550, width: 100, label: "Joystick", icon: "joystick", action: #selector(joystick(_:)))
        let game = createMode(x: 140, y: 550, width: 100, label: "Game", icon: "game", action: #selector(gameMode(_:)))
        let dual = createMode(x: 250, y: 550, width: 100, label: "Dual", icon: "dual", action: #selector(dualMode(_:)))
        joystick.backgroundColor = UIColor(named: "gamepad")
        game.backgroundColor = UIColor(named: "gamepad")
        dual.backgroundColor = UIColor(named: "gamepad")

        if selectedDriveMode == DriveMode.joystick {
            joystick.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else if selectedDriveMode == DriveMode.gameController {
            game.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else if selectedDriveMode == DriveMode.dual {
            dual.backgroundColor = UIColor(named: "HomePageTitleColor")
        }
        view.addSubview(joystick)
        view.addSubview(game)
        view.addSubview(dual)

    }

    @objc func joystick(_ sender: UIView) {
        selectedDriveMode = DriveMode.joystick
        updateGameControllerModeType()
    }

    @objc func gameMode(_ sender: UIView) {
        selectedDriveMode = DriveMode.gameController
        updateGameControllerModeType()

    }

    @objc func dualMode(_ sender: UIView) {
        selectedDriveMode = DriveMode.dual
        updateGameControllerModeType()

    }

    func updateSpeedModes() {
        let slowMode = createMode(x: 35, y: 650, width: 100, label: "Slow", icon: "slow", action: #selector(slow(_:)))
        let mediumMode = createMode(x: 140, y: 650, width: 100, label: "Medium", icon: "medium", action: #selector(medium(_:)))
        let fastMode = createMode(x: 250, y: 650, width: 100, label: "Fast", icon: "fast", action: #selector(fast(_:)))
        slowMode.backgroundColor = UIColor(named: "gamepad")
        mediumMode.backgroundColor = UIColor(named: "gamepad")
        fastMode.backgroundColor = UIColor(named: "gamepad")

        if selectedSpeedMode == SpeedMode.slow {
            slowMode.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else if selectedSpeedMode == SpeedMode.medium {
            mediumMode.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            fastMode.backgroundColor = UIColor(named: "HomePageTitleColor")
        }
        view.addSubview(slowMode)
        view.addSubview(mediumMode)
        view.addSubview(fastMode)
    }

    @objc func phoneMode(_ sender: UIView) {
        selectedControlMode = ControlMode.phone
        updateControlMode()
    }

    @objc func gamepadMode(_ sender: UIView) {
        selectedControlMode = ControlMode.gamepad;
        updateControlMode()
    }

    @objc func slow(_ sender: UIView) {
        selectedSpeedMode = SpeedMode.slow
        updateSpeedModes()
    }

    @objc func medium(_ sender: UIView) {
        selectedSpeedMode = SpeedMode.medium
        updateSpeedModes()
    }

    @objc func fast(_ sender: UIView) {
        selectedSpeedMode = SpeedMode.fast
        updateSpeedModes()
    }

    func createMode(x: Int, y: Int, width: Int, label: String, icon: String, action: Selector?) -> UIView {
        let modeRectangle = createRectangle(x: x, y: y, width: width, height: 60, borderColor: "noColor")
        modeRectangle.backgroundColor = UIColor(named: "gamepad")
        let modeRectangleLabel = UILabel(frame: CGRect(x: 10, y: 5, width: 100, height: 50))
        modeRectangleLabel.text = label
        modeRectangleLabel.textColor = .white
        modeRectangleLabel.font = modeRectangleLabel.font.withSize(12)
        modeRectangle.addSubview(modeRectangleLabel)
        let modeIcon = UIImageView(frame: CGRect(x: modeRectangle.frame.width - 20 - modeRectangle.frame.width / 10, y: modeRectangle.frame.height / 3, width: 20, height: 20))
        modeIcon.image = UIImage(named: icon)
        let tapGesture = UITapGestureRecognizer(target: self, action: action)
        tapGesture.delegate = self
        modeRectangle.addGestureRecognizer(tapGesture)
        modeRectangle.addSubview(modeIcon)
        return modeRectangle
    }

    @objc func updateScreen() {
        updateSonar()
        updateVoltage()
        updateSpeedometer()
    }

    func updateSonar() {
        let sonar = bluetooth.sonarData
        if sonar != "" {
            let index = sonar.index(after: sonar.startIndex)
            let actualSonarValue = min(Int(String(sonar[index...])) ?? 0, 300)
            sonarLabel.text = String(actualSonarValue) + "CM"
            createSonarController(h: Double(actualSonarValue))
        }
    }

    func updateVoltage() {
        let voltage = bluetooth.voltageDivider
        if voltage != "" {
            let index = voltage.index(after: voltage.startIndex)
            voltageLabel.text = String(voltage[index...]) + "V"
            createVoltageController(h: Double(String(voltage[index...])) ?? 0)
        }
    }

    func updateSpeedometer() {
        let oldTag = view.viewWithTag(100)
        oldTag?.removeFromSuperview()
        let a = GaugeView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 256))
        a.tag = 100
        let speedometer = bluetooth.speedometer
        if speedometer != "" {
            let index_1 = speedometer.index(after: speedometer.startIndex)
            let indexOfComma = speedometer.firstIndex(of: ",") ?? index_1
            let index_2 = speedometer.index(before: indexOfComma)
            let leftFront = Float(speedometer[index_1...index_2])
            let rightFont = Float(speedometer[speedometer.index(after: indexOfComma)...])
            let value = Int(((leftFront ?? 0) + (rightFont ?? 0)) / 2)
            a.value = value
            a.backgroundColor = .clear
            view.addSubview(a)
        }
    }

    @objc func updateControllerValues() {
        print("printing with controller");
        if (connectedController == nil) {
            return
        }
        print(Strings.controllerConnected)
        let controller = connectedController;
        let batteryLevel = String(format: "%.2f", controller!.battery.unsafelyUnwrapped.batteryLevel * 100);
        print(batteryLevel);
        controller?.extendedGamepad?.valueChangedHandler = { [self] gamepad, element in
            let control = gameControllerObj?.processJoystickInput(mode: selectedDriveMode, gamepad: gamepad) ?? Control();
            sendControl(control: control);
        }
    }

    func sendControl(control: Control) {
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getRight()) {
            let left = control.getLeft() * selectedSpeedMode.rawValue;
            let right = control.getRight() * selectedSpeedMode.rawValue;
            vehicleControl = control;
            print("c" + String(left) + "," + String(right) + "\n");
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n");
        }
    }
}