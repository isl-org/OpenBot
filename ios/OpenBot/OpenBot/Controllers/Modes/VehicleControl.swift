//
// Created by Nitish Yadav on 23/09/22.
//

import Foundation
import UIKit

class VehicleControl: UIView {
    var controlMode: ControlMode = ControlMode.phone;
    var speedMode: SpeedMode = SpeedMode.slow;
    var driveMode: DriveMode = DriveMode.dual;
    var speedLabel = UILabel()
    var speedInRpm = UILabel()
    var isButtonEnable: Bool = true
    let gameController = GameController.shared
    var downSwipe: Bool = false

    override init(frame: CGRect) {
        super.init(frame: frame)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        setupVehicleControl();
        createControllerMode()
        createDriveMode()
        createSpeedMode()
//        createLeftSpeed()
        createRpm()
        createLabel(text: "Controller", bottomAnchor: 0, leadingAnchor: width / 2 - 100, isBoldNeeded: true)
        createLabel(text: "Drive Mode", bottomAnchor: 0, leadingAnchor: width / 2 - 30, isBoldNeeded: true)
        createLabel(text: "Speed", bottomAnchor: 0, leadingAnchor: width / 2 + 50, isBoldNeeded: true)
        NotificationCenter.default.addObserver(self, selector: #selector(toggleAutoMode), name: .autoMode, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(toggleAutoMode), name: .autoModeObjectTracking, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateRpmLabel), name: .updateRpmLabel, object: nil)

    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func setupVehicleControl() {
        widthAnchor.constraint(equalToConstant: width).isActive = true;
        heightAnchor.constraint(equalToConstant: 80).isActive = true;
    }

    func createControllerMode() {
        updateControlMode(self);
    }

    func createDriveMode() {
        updateDriveMode(self)
    }

    func createSpeedMode() {
        updateSpeedMode(self)
    }

    func createAndUpdateButton(iconName: UIImage, leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector?) {
        let btn = UIButton()
        btn.layer.cornerRadius = 3
        btn.backgroundColor = Colors.title
        let modeIcon = UIImageView(frame: CGRect(x: 15, y: 15, width: 30, height: 30))
        modeIcon.image = iconName;
        if let action = action {
            btn.addTarget(self, action: action, for: .touchUpInside)
        }
        addSubview(btn)
        btn.translatesAutoresizingMaskIntoConstraints = false
        btn.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true;
        btn.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: leadingAnchor).isActive = true
        btn.widthAnchor.constraint(equalToConstant: 60).isActive = true
        btn.heightAnchor.constraint(equalToConstant: 60).isActive = true
        btn.addSubview(modeIcon)
    }


    @objc func updateControlMode(_ sender: UIView) {
        if (isButtonEnable) {
            if (controlMode == ControlMode.gamepad) {
                controlMode = ControlMode.phone;
                createAndUpdateButton(iconName: Images.phoneIcon!, leadingAnchor: width / 2 - 100, topAnchor: 0, action: #selector(updateControlMode(_:)));

            } else {
                controlMode = ControlMode.gamepad;
                createAndUpdateButton(iconName: Images.gamepadIcon!, leadingAnchor: width / 2 - 100, topAnchor: 0, action: #selector(updateControlMode(_:)));

            }
            gameController.selectedControlMode = controlMode
        }
    }

    @objc func updateDriveMode(_ sender: UIView) {
        if (controlMode == .gamepad && isButtonEnable) {
            switch (driveMode) {
            case .joystick:
                driveMode = .gameController;
                createAndUpdateButton(iconName: Images.gameDriveIcon!, leadingAnchor: width / 2 - 30, topAnchor: 0, action: #selector(updateDriveMode(_:)));
                break;
            case .gameController:
                driveMode = .dual;
                createAndUpdateButton(iconName: Images.dualDriveIcon!, leadingAnchor: width / 2 - 30, topAnchor: 0, action: #selector(updateDriveMode(_:)));
                break;
            case .dual:
                driveMode = .joystick;
                createAndUpdateButton(iconName: Images.joystickIcon!, leadingAnchor: width / 2 - 30, topAnchor: 0, action: #selector(updateDriveMode(_:)));
                break;
            }
            gameController.selectedDriveMode = driveMode
        }
    }

    @objc func updateSpeedMode(_ sender: UIView) {
        if isButtonEnable {
            switch (speedMode) {
            case .slow:
                speedMode = .medium;
                createAndUpdateButton(iconName: Images.mediumIcon!, leadingAnchor: width / 2 + 40, topAnchor: 0, action: #selector(updateSpeedMode(_:)));
                break;
            case .medium:
                speedMode = .fast;
                createAndUpdateButton(iconName: Images.fastIcon!, leadingAnchor: width / 2 + 40, topAnchor: 0, action: #selector(updateSpeedMode(_:)));
                break;
            case .fast:
                speedMode = .slow;
                createAndUpdateButton(iconName: Images.slowIcon!, leadingAnchor: width / 2 + 40, topAnchor: 0, action: #selector(updateSpeedMode(_:)));
                break;
            }
            gameController.selectedSpeedMode = speedMode

        }
    }

    func createLabel(text: String, bottomAnchor: CGFloat, leadingAnchor: CGFloat, isBoldNeeded: Bool) {
        let label = UILabel()
        label.frame.size = CGSize(width: 100, height: 40);
        label.text = text
        addSubview(label)
        label.font = label.font.withSize(13.5)
        if (isBoldNeeded) {
            label.font = UIFont.boldSystemFont(ofSize: 13.5)
        }
        label.translatesAutoresizingMaskIntoConstraints = false
        label.leadingAnchor.constraint(equalTo: self.leadingAnchor, constant: leadingAnchor).isActive = true
        label.bottomAnchor.constraint(equalTo: self.bottomAnchor, constant: bottomAnchor).isActive = true
    }

    func createRpm() {
        speedInRpm.frame.size = CGSize(width: 100, height: 40);
        speedInRpm.text = "---,--- rpm"
        addSubview(speedInRpm)
        speedInRpm.font = speedLabel.font.withSize(13.5)
        speedInRpm.translatesAutoresizingMaskIntoConstraints = false
        speedInRpm.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -10).isActive = true
        speedInRpm.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -40).isActive = true
    }

    @objc func toggleAutoMode(_ notification: Notification) {
        isButtonEnable = !isButtonEnable
    }

    @objc func updateRpmLabel(_ notification: Notification) {
        speedInRpm.text = (notification.object as! String)
    }

    @objc func swipeDown(_ notification: Notification) {
        switch notification.object as! Bool {
        case true:
            downSwipe = true
        case false:
            downSwipe = false
        }
    }
}