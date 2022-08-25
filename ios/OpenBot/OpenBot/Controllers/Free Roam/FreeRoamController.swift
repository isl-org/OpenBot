//
//  FreeRoamController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//

import UIKit

class FreeRoamController: UIViewController, UIGestureRecognizerDelegate {
    var circle: UIView!
    var isPhonemodeEnable: Bool = false
    var isGamemodeEnable: Bool = false
    var isJoystickEnable : Bool = false
    var isGameEnable : Bool = false
    var isDualEnable : Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()
        let speedoMeter = GaugeView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 256))
        speedoMeter.backgroundColor = .clear
        view.addSubview(speedoMeter)
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            UIView.animate(withDuration: 1) {
                speedoMeter.value = 33
            }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            UIView.animate(withDuration: 1) {
                speedoMeter.value = 66
            }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
            UIView.animate(withDuration: 1) {
                speedoMeter.value = 0
            }
        }
//

        let dIcon = UIButton()
        dIcon.setTitle("D", for: .normal)
        dIcon.layer.borderWidth = 1
        dIcon.layer.borderColor = UIColor.white.cgColor
        dIcon.frame = CGRect(x: 120, y: 310, width: 40, height: 40)
        dIcon.layer.cornerRadius = 5
        view.addSubview(dIcon)

        let driveIconRect = createRectange(x: 180, y: 310, width: 40, height: 40, borderColor: "borderColor")
        let driveIcon = UIImageView(frame: CGRect(x: driveIconRect.frame.size.width / 4, y: driveIconRect.frame.size.height / 8, width: 20, height: 30))
        driveIcon.image = UIImage(named: "drive")
        view.addSubview(driveIconRect)
        driveIconRect.addSubview(driveIcon)


        let blueToothIconRect = createRectange(x: 240, y: 310, width: 40, height: 40, borderColor: "borderColor")
        let blueToothIcon = UIImageView(frame: CGRect(x: blueToothIconRect.frame.size.width / 4, y: blueToothIconRect.frame.size.height / 8, width: 20, height: 30))
        blueToothIcon.image = UIImage(named: "bluetooth")
        view.addSubview(blueToothIconRect)
        blueToothIconRect.addSubview(blueToothIcon)
        createVoltageController(h: 50)
        createSonarController(h: 40)
        createLabel(value: "12 V", x: 35, y: 380, width: 50, height: 40)
        createLabel(value: "32CM", x: Int(view.frame.width) - 70, y: 380, width: 50, height: 40)
        createLabel(value: "Controller", x: 35, y: 420, width: 100, height: 40)
        createGamepad()
        createPhone()
        createLabel(value: "Drive Mode", x: 35, y: 520, width: 100, height: 40)
        let joystick = createMode(x: 35, y: 550,width: 100, label: "Joytick", icon: "joystick")
        let game = createMode(x: 140, y: 550,width: 100, label: "Game", icon: "game")
        let dual = createMode(x: 250, y: 550,width: 100, label: "Dual", icon: "dual")
        view.addSubview(joystick)
        view.addSubview(game)
        view.addSubview(dual)
        let diameter = 200.0
        let ticks = 40
        var radius = 120
        for i in 0...3 {
            drawTicks(count: ticks, radius: radius)
            radius = radius - 22;
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }

    func drawTicks(count: Int, radius: Int) {
        var rotationInDegrees: CGFloat = 0
        let pointOfBreak1: Int = count / 4;
        let pointOfBreak2: Int = 3 * count / 4;
        for i in 0..<count {
            if i <= pointOfBreak1 || i >= pointOfBreak2 {
                let tick = createTick()
                let x = CGFloat(Float(187.5) + Float(radius) * cosf(2 * Float(i) * Float(M_PI) / Float(count) - Float(M_PI) / 2))
                let y = CGFloat(Float(240) + Float(radius) * sinf(2 * Float(i) * Float(M_PI) / Float(count) - Float(M_PI) / 2))
                tick.center = CGPoint(x: x, y: y)
                // degress -> radians
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

    func createVoltageController(h: Int) {
        let outerVoltage = createRectange(x: 30, y: 280, width: 50, height: 110, borderColor: "borderColor")
        let innerVoltage = UIView(frame: CGRect(x: 0, y: 110 - h - 1, width: 49, height: h - 1))
        innerVoltage.backgroundColor = UIColor(named: "voltageDivider")
        outerVoltage.addSubview(innerVoltage)
        view.addSubview(outerVoltage)
    }

    func createSonarController(h: Int) {
        let outerSonar = createRectange(x: Int(view.frame.width) - 70, y: 280, width: 50, height: 110, borderColor: "borderColor");
        let innerSonar = UIView(frame: CGRect(x: 0, y: 110 - h - 1, width: 49, height: h - 1))
        innerSonar.backgroundColor = UIColor(named: "sonar")
        outerSonar.addSubview(innerSonar)
        view.addSubview(outerSonar)
    }

    func createLabel(value: String, x: Int, y: Int, width: Int, height: Int) {
        let label = UILabel()
        label.text = value
        label.frame = CGRect(x: x, y: y, width: width, height: height)
        label.textColor = .white
        view.addSubview(label)
    }

    func createRectange(x: Int, y: Int, width: Int, height: Int, borderColor: String) -> UIView {
        let rectangeView = UIView(frame: CGRect(x: x, y: y, width: width, height: height))
        rectangeView.layer.cornerRadius = 5;
        rectangeView.layer.borderWidth = 1;
        rectangeView.layer.borderColor = UIColor(named: borderColor)?.cgColor
        return rectangeView;
    }

    func createGamepad() {
        let gamePadController = createRectange(x: 35, y: 460, width: 150, height: 60, borderColor: "nocolor")
        if isGamemodeEnable{
            gamePadController.backgroundColor = UIColor(named: "HomePageTitleColor")
        }
        else{
            gamePadController.backgroundColor = UIColor(named: "gamepad")
        }


        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(gamepadMode(_:)))
        tapGesture.delegate = self
        gamePadController.addGestureRecognizer(tapGesture)
        let gamePadLabel = UILabel(frame: CGRect(x: 0, y: 0, width: 100, height: 50))
        gamePadLabel.text = "Gamepad"
        gamePadLabel.textColor = .white
        gamePadController.addSubview(gamePadLabel)
        let controllerIcon: UIImageView
        controllerIcon = UIImageView(frame: CGRect(x: gamePadController.frame.width - 70, y: gamePadController.frame.height / 16, width: 50, height: 50))
        controllerIcon.image = UIImage(named: "gamepad")
        gamePadController.addSubview(controllerIcon)
        view.addSubview(gamePadController)
    }


    func createPhone() {
        let phoneController = createRectange(x: Int(view.frame.width) / 2, y: 460, width: 150, height: 60, borderColor: "nocolor")
        if isPhonemodeEnable {
            phoneController.backgroundColor = UIColor(named: "HomePageTitleColor")
        }
        else {
            phoneController.backgroundColor = UIColor(named: "gamepad")
        }


        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(phoneMode(_:)))
        tapGesture.delegate = self
        phoneController.addGestureRecognizer(tapGesture)
        let phoneLabel = UILabel(frame: CGRect(x: 0, y: 0, width: 100, height: 50))
        phoneLabel.text = "Phone"
        phoneLabel.textColor = .white
        phoneController.addSubview(phoneLabel)
        let controllerIcon: UIImageView
        controllerIcon = UIImageView(frame: CGRect(x: phoneController.frame.width - 70, y: phoneController.frame.height / 16, width: 50, height: 50))
        controllerIcon.image = UIImage(named: "phone")
        phoneController.addSubview(controllerIcon)
        view.addSubview(phoneController)
    }

    @objc func phoneMode(_ sender: UIView) {
        isPhonemodeEnable = true;
        isGamemodeEnable = false
       createPhone()
        createGamepad()
    }
    @objc func gamepadMode(_ sender: UIView) {
        isGamemodeEnable = true
        isPhonemodeEnable = false
        createPhone()
        createGamepad()

    }
    func createMode(x:Int,y:Int,width:Int,label : String, icon : String)->UIView{
        let modeRectangle = createRectange(x: x, y: y, width: width, height: 60, borderColor: "nocolor")
        modeRectangle.backgroundColor = UIColor(named: "gamepad")
        let modeRectangleLabel = UILabel(frame: CGRect(x: 0, y: 0, width: 100, height: 50))
        modeRectangleLabel.text = label
        modeRectangleLabel.textColor = .white
        modeRectangle.addSubview(modeRectangleLabel)
        let modeIcon = UIImageView(frame: CGRect(x: modeRectangle.frame.width - 50, y: modeRectangle.frame.height / 16, width: 50, height: 50))
        modeIcon.image = UIImage(named: icon)
        modeRectangle.addSubview(modeIcon)
       return modeRectangle
    }
}
