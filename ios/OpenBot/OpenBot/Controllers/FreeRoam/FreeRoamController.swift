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
    let deviceOrientation = DeviceCurrentOrientation.shared
    let gameController = GameController.shared
    var bluetoothIcon = UIImageView()
    private let mainView = UIView()

    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigationBarItem()
        setupSpeedMode()
        applySafeAreaConstraints()
        apply()
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
        if currentOrientation == UIInterfaceOrientation.landscapeRight || currentOrientation == UIInterfaceOrientation.landscapeLeft {
            applyLandScapeConstraint()
        }
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothConnected, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothDisconnected, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(decreaseSpeedMode), name: .decreaseSpeedMode, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(increaseSpeedMode), name: .increaseSpeedMode, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDriveMode), name: .updateDriveMode, object: nil)

    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated);
    }

    func applySafeAreaConstraints() {
        mainView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(mainView)
    }

    func apply() {
        let margins = view.layoutMarginsGuide
        NSLayoutConstraint.activate([
            mainView.leadingAnchor.constraint(equalTo: margins.leadingAnchor),
            mainView.trailingAnchor.constraint(equalTo: margins.trailingAnchor),
            mainView.topAnchor.constraint(equalTo: margins.topAnchor),
            mainView.bottomAnchor.constraint(equalTo: margins.bottomAnchor)
        ])
    }


    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        switch currentOrientation {
        case .portrait:
            ApplyPortraitConstraint()
            break
        case .landscapeRight:
            applyLandScapeConstraint()
            break
        case .landscapeLeft:
            applyLandScapeConstraint()
            break
        default:
            ApplyPortraitConstraint();
        }

    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }

    func ApplyPortraitConstraint() {
        removeConstraints()
        let leading = secondView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 0)
        leading.identifier = Strings.secondView
        let top = secondView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: view.frame.width / 2 - 30)
        top.identifier = Strings.secondView
        let width = secondView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 1)
        width.identifier = Strings.secondView
        let height = secondView.heightAnchor.constraint(equalTo: view.heightAnchor, multiplier: 0.5)
        height.identifier = Strings.secondView
        NSLayoutConstraint.activate([
            leading, top, width, height
        ])
    }

    func applyLandScapeConstraint() {
        removeConstraints()
        let leading = secondView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: height / 2 - 12)
        leading.identifier = Strings.secondView
        let top = secondView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: -30)
        top.identifier = Strings.secondView
        let width = secondView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.5)
        width.identifier = Strings.secondView
        let height = secondView.heightAnchor.constraint(equalTo: view.heightAnchor, multiplier: 1)
        height.identifier = Strings.secondView
        NSLayoutConstraint.activate([
            leading, top, width, height
        ])
    }

    func removeConstraints() {
        for constraint in view.constraints {
            if constraint.identifier == Strings.secondView {
                view.removeConstraint(constraint)
            }
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }

    func createFirstView() {
        firstView.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height / 2)
        mainView.addSubview(firstView)
        firstView.translatesAutoresizingMaskIntoConstraints = false

    }

    func createSecondView() {
//        secondView.frame = CGRect(x: 0, y: height / 2 - 30, width: view.frame.width, height: view.frame.height / 2)
        let h = view.frame.height / 2
        let width = view.frame.width
        let size = CGSize(width: width, height: h)
        let origin = CGPoint(x: 0, y: height / 2 - 30)
        secondView.frame = CGRect(origin: origin, size: resized(size: size, basedOn: dimension))
        mainView.addSubview(secondView)
        secondView.translatesAutoresizingMaskIntoConstraints = false
    }

    func createSpeedometer() {
        let speedometer = GaugeView(frame: CGRect(x: 0, y: -10, width: width, height: 256))
        speedometer.backgroundColor = .clear
        speedometer.value = 0
        speedometer.tag = 100
        firstView.addSubview(speedometer)
    }

    func deg2rad(_ number: CGFloat) -> CGFloat {
        number * .pi / 180
    }

    func drawTicks(count: Int, radius: Int) {
        var rotationInDegrees: CGFloat = 0
        let pointOfBreak1: Int = count / 4 - 1;
        let pointOfBreak2: Int = 3 * count / 4 + 1;
        for i in 0..<count {
            if i <= pointOfBreak1 || i >= pointOfBreak2 {
                let tick = createTick()
                let x = CGFloat(Float(187.5) + Float(radius) * cosf(2 * Float(i) * Float(Double.pi) / Float(count) - Float(Double.pi) / 2))
                let y = CGFloat(Float(240) + Float(radius) * sinf(2 * Float(i) * Float(Double.pi) / Float(count) - Float(Double.pi) / 2))
                tick.center = CGPoint(x: x - 10, y: y - 70)
                tick.transform = CGAffineTransform.identity.rotated(by: rotationInDegrees * .pi / 180.0)
                firstView.addSubview(tick)
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
        createLabel(value: Strings.controller, x: 35, y: 20, width: 100, height: 40)
        createLabel(value: Strings.speed, x: 35, y: 207, width: 100, height: 40)
        createLabel(value: Strings.driveMode, x: 35, y: 110, width: 100, height: 40)
    }

    func createVoltageLabel() {
        voltageLabel.frame = CGRect(x: 43, y: 300, width: 50, height: 40)
        voltageLabel.text = "0V"
        voltageLabel.textColor = .white
        voltageLabel.font = voltageLabel.font.withSize(15)
        firstView.addSubview(voltageLabel)
    }

    func createSonalLabel() {
        sonarLabel.frame = CGRect(x: Int(width) - 96, y: 300, width: 60, height: 40)
        sonarLabel.text = "0CM"
        sonarLabel.textColor = .white
        sonarLabel.font = sonarLabel.font.withSize(15)
        firstView.addSubview(sonarLabel)
    }

    func createDIcon() {
        let dIcon = UIButton()
        dIcon.setTitle("D", for: .normal)
        dIcon.layer.borderWidth = 1
        dIcon.layer.borderColor = UIColor(named: "bdColor")?.cgColor
        dIcon.setTitleColor(UIColor(named: "bdColor"), for: .normal)
        dIcon.frame = CGRect(x: 100, y: 230, width: 40, height: 40)
        dIcon.layer.cornerRadius = 5
        firstView.addSubview(dIcon)
    }

    func createDriveIcon() {
        let driveIconRect = createRectangle(x: 160, y: 230, width: 40, height: 40, borderColor: "bdColor", isBorderRequire: true)
        driveIconRect.layer.borderColor = UIColor(named: "bdColor")?.cgColor
        let driveIcon = UIImageView(frame: CGRect(x: driveIconRect.frame.size.width / 4, y: driveIconRect.frame.size.height / 4, width: 20, height: 20))
        driveIcon.image = UIImage(named: "drive")
        firstView.addSubview(driveIconRect)
        driveIconRect.addSubview(driveIcon)
    }

    func createBluetoothIcon() {
        let blueToothIconRect = createRectangle(x: 220, y: 230, width: 40, height: 40, borderColor: "bdColor", isBorderRequire: true)
        blueToothIconRect.layer.borderColor = UIColor(named: "bdColor")?.cgColor
        bluetoothIcon = UIImageView(frame: CGRect(x: 2 * blueToothIconRect.frame.size.width / 4 - 10, y: blueToothIconRect.frame.size.height / 4, width: 20, height: 20))
        if isBluetoothConnected {
            bluetoothIcon.image = Images.bluetoothConnected
        } else {
            bluetoothIcon.image = Images.bluetoothDisconnected
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(openBluetoothSettings(tapGestureRecognizer:)))

        blueToothIconRect.isUserInteractionEnabled = true
        blueToothIconRect.addGestureRecognizer(tapGesture)

        firstView.addSubview(blueToothIconRect)
        blueToothIconRect.addSubview(bluetoothIcon)
    }

    func createVoltageController(h: Double) {
        let outerVoltage = createRectangle(x: 30, y: 190, width: 50, height: 110, borderColor: "bdColor", isBorderRequire: true)
        outerVoltage.layer.borderColor = UIColor(named: "bdColor")?.cgColor;
        let relativeHeight = Double(h * 9.16)
//        let innerVoltage = UIView(frame: CGRect(x: 0, y: 110 - relativeHeight, width: 49, height: relativeHeight - 1))
        let innerVoltage = createRectangle(x: 0, y: 110 - Int(relativeHeight), width: 49, height: Int(relativeHeight) - 1, borderColor: "noColor", isBorderRequire: false)
        innerVoltage.layer.cornerRadius = 5;
        innerVoltage.backgroundColor = Colors.voltageDividerColor
        outerVoltage.addSubview(innerVoltage)
        firstView.addSubview(outerVoltage)
    }

    func createSonarController(h: Double) {
        if outerSonar != nil {
            outerSonar.subviews[0].removeFromSuperview()
        }
        let relativeHeight = Double(h * 0.3667)
        outerSonar = createRectangle(x: Int(width) - 100, y: 190, width: 50, height: 110, borderColor: "borderColor", isBorderRequire: true);
        firstView.addSubview(outerSonar)
        outerSonar.layer.borderColor = UIColor(named: "bdColor")?.cgColor
//        let innerSonar = UIView(frame: CGRect(x: 0, y: 110 - relativeHeight, width: 49, height: relativeHeight - 1))
        let innerSonar = createRectangle(x: 0, y: 110 - Int(relativeHeight), width: 49, height: Int(relativeHeight) - 1, borderColor: "noColor", isBorderRequire: false)
        innerSonar.layer.cornerRadius = 5;
        innerSonar.backgroundColor = Colors.sonar
        outerSonar.addSubview(innerSonar)
    }

    func createLabel(value: String, x: Int, y: Int, width: Int, height: Int) {
        let label = UILabel()
        label.text = value
        label.font = UIFont(name: "medium", size: adapted(dimensionSize: 30, to: .width))
        label.frame = CGRect(x: x, y: y, width: width, height: height)
        label.textColor = Colors.border
        label.font = label.font.withSize(15)
        secondView.addSubview(label)

    }

    func createRectangle(x: Int, y: Int, width: Int, height: Int, borderColor: String, isBorderRequire: Bool) -> UIView {
        let rectangleView = UIView();
        let origin = CGPoint(x: x, y: y)
        let size = CGSize(width: width, height: height)
        rectangleView.frame = CGRect(origin: origin, size: size)
        rectangleView.layer.cornerRadius = 5;
        if isBorderRequire {
            rectangleView.layer.borderWidth = 1;
            rectangleView.layer.borderColor = UIColor(named: "borderColor")?.cgColor;
            return rectangleView;
        }
        return rectangleView;

    }


    func updateControlMode() {
        let gamePadController = createMode(x: 35, y: 55, width: Int(width / 3), label: Strings.gamepad, icon: "gamepad", action: #selector(gamepadMode(_:)))
        let phoneController = createMode(x: Int(width / 3) + 48, y: 55, width: Int(width / 3), label: Strings.phone, icon: "phone", action: #selector(phoneMode(_:)))
        if selectedControlMode == ControlMode.gamepad {
            NotificationCenter.default.addObserver(self, selector: #selector(updateControllerValues), name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil);
            gameControllerObj = gameController
            gameController.selectedControlMode = ControlMode.gamepad
            gamePadController.backgroundColor = Colors.title
            server = nil
            client = Client()
            sharedConnection = nil
        } else if selectedControlMode == ControlMode.phone {
            gameControllerObj = nil;
            gameController.selectedControlMode = ControlMode.phone
            NotificationCenter.default.removeObserver(self, name: NSNotification.Name(rawValue: Strings.controllerConnected), object: nil);
            phoneController.backgroundColor = Colors.title
            server?.start();
            client.start();
        }
        secondView.addSubview(gamePadController)
        secondView.addSubview(phoneController)
    }

    func updateGameControllerModeType() {
        let joystick = createMode(x: 35, y: 145, width: Int(width / 4), label: Strings.joystick, icon: "joystick", action: #selector(joystick(_:)))
        let game = createMode(x: 140, y: 145, width: Int(width / 4), label: Strings.game, icon: "game", action: #selector(gameMode(_:)))
        let dual = createMode(x: 245, y: 145, width: Int(width / 4), label: Strings.dual, icon: "dual", action: #selector(dualMode(_:)))
        if selectedDriveMode == DriveMode.joystick {
            joystick.backgroundColor = Colors.title
        } else if selectedDriveMode == DriveMode.gameController {
            game.backgroundColor = Colors.title
        } else if selectedDriveMode == DriveMode.dual {
            dual.backgroundColor = Colors.title
        }
        secondView.addSubview(joystick)
        secondView.addSubview(game)
        secondView.addSubview(dual)
    }

    @objc func joystick(_ sender: UIView) {
        selectedDriveMode = DriveMode.joystick
        gameController.selectedDriveMode = DriveMode.joystick
        updateGameControllerModeType()
    }

    @objc func gameMode(_ sender: UIView) {
        selectedDriveMode = DriveMode.gameController
        gameController.selectedDriveMode = DriveMode.gameController

        updateGameControllerModeType()
    }

    @objc func dualMode(_ sender: UIView) {
        selectedDriveMode = DriveMode.dual
        gameController.selectedDriveMode = DriveMode.dual
        updateGameControllerModeType()

    }

    func updateSpeedModes() {
        let slowMode = createMode(x: 35, y: 242, width: Int(width / 4), label: Strings.slow, icon: "slow", action: #selector(slow(_:)))
        let mediumMode = createMode(x: 140, y: 242, width: Int(width / 4), label: Strings.medium, icon: "medium", action: #selector(medium(_:)))
        let fastMode = createMode(x: 245, y: 242, width: Int(width / 4), label: Strings.fast, icon: "fast", action: #selector(fast(_:)))
        if selectedSpeedMode == SpeedMode.slow {
            slowMode.backgroundColor = Colors.title
        } else if selectedSpeedMode == SpeedMode.medium {
            mediumMode.backgroundColor = Colors.title
        } else {
            fastMode.backgroundColor = Colors.title
        }
        gameController.selectedSpeedMode = selectedSpeedMode;
        secondView.addSubview(slowMode)
        secondView.addSubview(mediumMode)
        secondView.addSubview(fastMode)
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
        gameController.selectedSpeedMode = SpeedMode.slow
        updateSpeedModes()
    }

    @objc func medium(_ sender: UIView) {
        selectedSpeedMode = SpeedMode.medium
        gameController.selectedSpeedMode = SpeedMode.medium
        updateSpeedModes()
    }

    @objc func fast(_ sender: UIView) {
        selectedSpeedMode = SpeedMode.fast
        gameController.selectedSpeedMode = SpeedMode.fast
        updateSpeedModes()
    }

    func createMode(x: Int, y: Int, width: Int, label: String, icon: String, action: Selector?) -> UIView {
        let modeRectangle = createRectangle(x: x, y: y, width: width, height: 55, borderColor: "noColor", isBorderRequire: false)
        modeRectangle.backgroundColor = Colors.freeRoamButtonsColor
        let modeRectangleLabel = UILabel(frame: CGRect(x: 10, y: 4, width: 100, height: 50))
        modeRectangleLabel.text = label
        modeRectangleLabel.textColor = .white
        modeRectangleLabel.font = modeRectangleLabel.font.withSize(15)
        modeRectangle.addSubview(modeRectangleLabel)
        let modeIcon = UIImageView(frame: CGRect(x: modeRectangle.frame.width - 12.8 - modeRectangle.frame.width / 10, y: modeRectangle.frame.height / 3, width: 19, height: 19))
        modeIcon.image = UIImage(named: icon)
        let tapGesture = UITapGestureRecognizer(target: self, action: action)
        tapGesture.delegate = self
        modeRectangle.addGestureRecognizer(tapGesture)
        modeRectangle.addSubview(modeIcon)
        modeRectangle.layer.borderColor = .none
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
            sonarLabel.text = String(actualSonarValue) + " CM"
            createSonarController(h: Double(actualSonarValue))
        }
    }

    func updateVoltage() {
        let voltage = bluetooth.voltageDivider
        voltageLabel.frame.origin.x = 35;
        if voltage != "" {
            let index = voltage.index(after: voltage.startIndex)
            voltageLabel.text = String(voltage[index...]) + " V"
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
            firstView.addSubview(a)
        }
    }

    @objc func updateControllerValues() {
        gameController.updateControllerValues()
    }

    var statusBarOrientation: UIInterfaceOrientation? {
        get {
            guard let orientation = UIApplication.shared.windows.first?.windowScene?.interfaceOrientation else {
                #if DEBUG
                fatalError("Could not obtain UIInterfaceOrientation from a valid windowScene")
                #else
                return nil
                #endif
            }
            return orientation
        }
    }

    func setupNavigationBarItem() {
        if UIImage(named: "back") != nil {
            let backNavigationIcon = (UIImage(named: "back")?.withRenderingMode(.alwaysOriginal))!
            let newBackButton = UIBarButtonItem(image: backNavigationIcon, title: Strings.freeRoam, target: self, action: #selector(FreeRoamController.back(sender:)),titleColor: Colors.navigationColor ?? .white)
            navigationItem.leftBarButtonItem = newBackButton
        }
    }

    @objc func back(sender: UIBarButtonItem) {

        _ = navigationController?.popViewController(animated: true)
    }

    @objc func openBluetoothSettings(tapGestureRecognizer: UITapGestureRecognizer) {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: Strings.bluetoothScreen))
        navigationController?.pushViewController(nextViewController!, animated: true)
    }

    @objc func updateConnect(_ notification: Notification) {
        if (isBluetoothConnected) {
            bluetoothIcon.image = Images.bluetoothConnected
        } else {
            bluetoothIcon.image = Images.bluetoothDisconnected
        }
    }

    @objc func decreaseSpeedMode(_ notification: Notification) {
            switch selectedSpeedMode {
            case .slow :
                return;
            case .medium :
                selectedSpeedMode = .slow;
                break;
            case .fast :
                selectedSpeedMode = .medium;
                break;
            }
        updateSpeedModes()
    }

    @objc func increaseSpeedMode(_ notification: Notification) {
        switch selectedSpeedMode {
        case .slow :
            selectedSpeedMode = .medium;
            break;
        case .medium :
            selectedSpeedMode = .fast;
            break;
        case .fast :
            return
        }
        updateSpeedModes()
    }

    @objc func updateDriveMode(_ notification: Notification) {
        switch selectedDriveMode {
        case .joystick :
            selectedDriveMode = .gameController
            break;
        case .dual :
            selectedDriveMode = .joystick;
            break;
        case .gameController :
            selectedDriveMode = .dual
            break;
        }
        updateGameControllerModeType()
    }

    func setupSpeedMode() {
        gameController.selectedSpeedMode = SpeedMode.medium;
        gameController.selectedDriveMode = DriveMode.joystick;
    }
}



