//
//  FreeRoamController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//

import UIKit

class FreeRoamController: UIViewController, UIGestureRecognizerDelegate {
    var temp: String = ""
    var circle: UIView!
    var isPhoneModeEnable: Bool = false
    var isGameModeEnable: Bool = false
    var isJoystickEnable: Bool = false
    var isGameEnable: Bool = false
    var isDualEnable: Bool = false
    var isSlowEnable: Bool = false
    var isMediumEnable: Bool = false
    var isFastEnable: Bool = false
    var segmentWidth: CGFloat = 40
    var segmentColors = [UIColor(red: 0.10, green: 0.66, blue: 0.98, alpha: 1.00), UIColor(red: 0.00, green: 0.44, blue: 0.77, alpha: 1.00)]
    var rotation: CGFloat = -89
    var sonarLabel = UILabel()
    var voltageLabel = UILabel()
    var segmentValue: Int = 200
    var outerSonar : UIView!
    let bluetooth = bluetoothDataController.shared
    override func viewDidLoad() {
        super.viewDidLoad()
        createSpeedometer()
        createSonalLabel();
        createVoltageLabel()
        NotificationCenter.default.addObserver(self, selector: #selector(updateScreen), name: .updateLabel, object: nil)
        createDIcon()
        createDriveIcon()
        createBluetoothIcon()
        createGamepad()
        createPhone()
        createJoystick()
        createGame()
        createDual()
        createSlowMode()
        createMediumMode()
        createFastMode()
        drawTick()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    func createSpeedometer(){
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
    func drawTick(){
        let ticks = 40
        var radius = 120
        for _ in 0...3 {
            drawTicks(count: ticks, radius: radius)
            radius = radius - 22;
        }
    }
    func createLabels(){
        createLabel(value: "Controller", x: 35, y: 420, width: 100, height: 40)
        createLabel(value: "Speed", x: 35, y: 615, width: 100, height: 40)
        createLabel(value: "Drive Mode", x: 35, y: 515, width: 100, height: 40)



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
    func createDIcon(){
        let dIcon = UIButton()
        dIcon.setTitle("D", for: .normal)
        dIcon.layer.borderWidth = 1
        dIcon.layer.borderColor = UIColor.white.cgColor
        dIcon.frame = CGRect(x: 120, y: 310, width: 40, height: 40)
        dIcon.layer.cornerRadius = 5
        view.addSubview(dIcon)
    }
    func createDriveIcon(){
        let driveIconRect = createRectangle(x: 180, y: 310, width: 40, height: 40, borderColor: "borderColor")
        let driveIcon = UIImageView(frame: CGRect(x: driveIconRect.frame.size.width / 4, y: driveIconRect.frame.size.height / 4, width: 20, height: 20))
        driveIcon.image = UIImage(named: "drive")
        view.addSubview(driveIconRect)
        driveIconRect.addSubview(driveIcon)
}
    func createBluetoothIcon(){
        let blueToothIconRect = createRectangle(x: 240, y: 310, width: 40, height: 40, borderColor: "borderColor")
        let blueToothIcon = UIImageView(frame: CGRect(x: 2 * blueToothIconRect.frame.size.width / 5, y: blueToothIconRect.frame.size.height / 4, width: 10, height: 20))
        blueToothIcon.image = Images.bluetoothConnected
        view.addSubview(blueToothIconRect)
        blueToothIconRect.addSubview(blueToothIcon)
    }

    func createVoltageController(h: Int) {
        let outerVoltage = createRectangle(x: 30, y: 280, width: 50, height: 110, borderColor: "borderColor")
        let innerVoltage = UIView(frame: CGRect(x: 0, y: 110 - h, width: 49, height: h - 1))
        innerVoltage.backgroundColor = UIColor(named: "voltageDivider")
        outerVoltage.addSubview(innerVoltage)
        view.addSubview(outerVoltage)
    }

    func createSonarController(h: Double) {
        if outerSonar != nil {
            outerSonar.subviews[0].removeFromSuperview()
        }
        let relativeHeight: Double
        if h > 300 {
            relativeHeight = 110
        } else {
            relativeHeight = (Double(h * 0.3667))
        }
        outerSonar = createRectangle(x: Int(view.frame.width) - 70, y: 280, width: 50, height: 110, borderColor: "borderColor");
        view.addSubview(outerSonar)
        let innerSonar = UIView(frame: CGRect(x: 0, y: 110 - relativeHeight, width: 49, height: relativeHeight - 1))
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
        view.addSubview(label)

    }

    func createRectangle(x: Int, y: Int, width: Int, height: Int, borderColor: String) -> UIView {
        let rectangleView = UIView(frame: CGRect(x: x, y: y, width: width, height: height))
        rectangleView.layer.cornerRadius = 5;
        rectangleView.layer.borderWidth = 1;
        rectangleView.layer.borderColor = UIColor(named: borderColor)?.cgColor
        return rectangleView;
    }

    func createGamepad() {
        let gamePadController = createMode(x: 35, y: 460, width: 150, label: "Gamepad", icon: "gamepad")
        if isGameModeEnable {
            gamePadController.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            gamePadController.backgroundColor = UIColor(named: "gamepad")
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(gamepadMode(_:)))
        tapGesture.delegate = self
        gamePadController.addGestureRecognizer(tapGesture)
        view.addSubview(gamePadController)
    }


    func createPhone() {
        let phoneController = createMode(x: Int(view.frame.width / 2) + 20, y: 460, width: 120, label: "Phone", icon: "phone")
        if isPhoneModeEnable {
            phoneController.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            phoneController.backgroundColor = UIColor(named: "gamepad")
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(phoneMode(_:)))
        tapGesture.delegate = self
        phoneController.addGestureRecognizer(tapGesture)
        view.addSubview(phoneController)
    }

    func createJoystick() {
        let joystick = createMode(x: 35, y: 550, width: 100, label: "Joystick", icon: "joystick")
        if isJoystickEnable {
            joystick.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            joystick.backgroundColor = UIColor(named: "gamepad")
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(joystick(_:)))
        tapGesture.delegate = self
        joystick.addGestureRecognizer(tapGesture)
        view.addSubview(joystick)


    }

    func createGame() {
        let game = createMode(x: 140, y: 550, width: 100, label: "Game", icon: "game")
        if isGameEnable {
            game.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            game.backgroundColor = UIColor(named: "gamepad")
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(gameMode(_:)))
        tapGesture.delegate = self
        game.addGestureRecognizer(tapGesture)
        view.addSubview(game)

    }

    func createDual() {
        let dual = createMode(x: 250, y: 550, width: 100, label: "Dual", icon: "dual")
        if isDualEnable {
            dual.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            dual.backgroundColor = UIColor(named: "gamepad")
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(dualMode(_:)))
        tapGesture.delegate = self
        dual.addGestureRecognizer(tapGesture)
        view.addSubview(dual)
    }

    @objc func joystick(_ sender: UIView) {
        isJoystickEnable = true;
        isGameEnable = false
        isDualEnable = false
        createJoystick()
        createGame()
        createDual()

    }

    @objc func gameMode(_ sender: UIView) {
        isJoystickEnable = false;
        isGameEnable = true
        isDualEnable = false
        createJoystick()
        createGame()
        createDual()

    }

    @objc func dualMode(_ sender: UIView) {
        isJoystickEnable = false
        isGameEnable = false
        isDualEnable = true
        createJoystick()
        createGame()
        createDual()

    }

    func createSlowMode() {
        let slowMode = createMode(x: 35, y: 650, width: 100, label: "Slow", icon: "slow")
        if isSlowEnable {
            slowMode.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            slowMode.backgroundColor = UIColor(named: "gamepad")
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(slow(_:)))
        tapGesture.delegate = self
        slowMode.addGestureRecognizer(tapGesture)
        view.addSubview(slowMode)
    }

    func createMediumMode() {
        let mediumMode = createMode(x: 140, y: 650, width: 100, label: "Medium", icon: "medium")
        if isMediumEnable {
            mediumMode.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            mediumMode.backgroundColor = UIColor(named: "gamepad")
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(medium(_:)))
        tapGesture.delegate = self
        mediumMode.addGestureRecognizer(tapGesture)
        view.addSubview(mediumMode)
    }

    func createFastMode() {
        let fastMode = createMode(x: 250, y: 650, width: 100, label: "Fast", icon: "fast")
        if isFastEnable {
            fastMode.backgroundColor = UIColor(named: "HomePageTitleColor")
        } else {
            fastMode.backgroundColor = UIColor(named: "gamepad")
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(fast(_:)))
        tapGesture.delegate = self
        fastMode.addGestureRecognizer(tapGesture)
        view.addSubview(fastMode)
    }

    @objc func phoneMode(_ sender: UIView) {
        isPhoneModeEnable = true;
        isGameModeEnable = false
        createPhone()
        createGamepad()
    }

    @objc func gamepadMode(_ sender: UIView) {
        isGameModeEnable = true
        isPhoneModeEnable = false
        createPhone()
        createGamepad()

    }

    @objc func slow(_ sender: UIView) {
        isSlowEnable = true
        isMediumEnable = false
        isFastEnable = false
        createSlowMode()
        createMediumMode()
        createFastMode()
    }

    @objc func medium(_ sender: UIView) {
        isSlowEnable = false
        isMediumEnable = true
        isFastEnable = false
        createSlowMode()
        createMediumMode()
        createFastMode()
    }

    @objc func fast(_ sender: UIView) {
        isSlowEnable = false
        isMediumEnable = false
        isFastEnable = true
        createSlowMode()
        createMediumMode()
        createFastMode()
    }

    func createMode(x: Int, y: Int, width: Int, label: String, icon: String) -> UIView {
        let modeRectangle = createRectangle(x: x, y: y, width: width, height: 60, borderColor: "noColor")
        modeRectangle.backgroundColor = UIColor(named: "gamepad")
        let modeRectangleLabel = UILabel(frame: CGRect(x: 10, y: 5, width: 100, height: 50))
        modeRectangleLabel.text = label
        modeRectangleLabel.textColor = .white
        modeRectangleLabel.font = modeRectangleLabel.font.withSize(12)
        modeRectangle.addSubview(modeRectangleLabel)
        let modeIcon = UIImageView(frame: CGRect(x: modeRectangle.frame.width - 20 - modeRectangle.frame.width / 10, y: modeRectangle.frame.height / 3, width: 20, height: 20))
        modeIcon.image = UIImage(named: icon)
        modeRectangle.addSubview(modeIcon)
        return modeRectangle
    }

    @objc func updateScreen() {
        updateSonar()
        updateVoltage()
        updateSpeedometer()

    }
    func updateSonar(){
        let sonar = bluetooth.sonarData
        if sonar != "" {
            let index = sonar.index(after: sonar.startIndex)
            sonarLabel.text = String(sonar[index...]) + "CM"
            createSonarController(h: Double(String(sonar[index...])) ?? 0)
        }

    }
    func updateVoltage(){
        let voltage = bluetooth.voltageDivider
        if voltage != "" {
            let index = voltage.index(after: voltage.startIndex)
            voltageLabel.text = String(voltage[index...]) + "V"
            createVoltageController(h: Int(String(voltage[index...])) ?? 0)
        }
    }
    func updateSpeedometer(){
        let oldTag = view.viewWithTag(100)
        oldTag?.removeFromSuperview()
        let a = GaugeView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 256))
        a.tag = 100
        var value = Int(sonarLabel.text?.prefix(2) ?? "0") ?? 0
        if value > 180{
            value = 180
        }
        a.value = value
        a.backgroundColor = .clear
        view.addSubview(a)
    }
}
