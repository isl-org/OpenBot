//
// Created by Nitish Yadav on 23/09/22.
//

import Foundation
import UIKit

class VehicleControl: UIView {
    var controller = UIButton()
    var controlMode: ControlMode = ControlMode.phone;
    var speedMode: SpeedMode = SpeedMode.slow;
    var driveMode: DriveMode = DriveMode.dual;

    override init(frame: CGRect) {
        super.init(frame: frame)

        DeviceCurrentOrientation.shared.findDeviceOrientation()
        setupVehicleControl();
        createControllerMode()
        createDriveMode()
        createSpeedMode()

    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func setupVehicleControl() {

            widthAnchor.constraint(equalToConstant: width).isActive = true;
            heightAnchor.constraint(equalToConstant: 60).isActive = true;
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
        btn.layer.cornerRadius = 10
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
        if (controlMode == ControlMode.gamepad) {
            controlMode = ControlMode.phone;
            createAndUpdateButton(iconName: Images.phoneIcon!, leadingAnchor: 50, topAnchor: 0, action: #selector(updateControlMode(_:)));
        } else {
            controlMode = ControlMode.gamepad;
            createAndUpdateButton(iconName: Images.gamepadIcon!, leadingAnchor: 50, topAnchor: 0, action: #selector(updateControlMode(_:)));
        }
        let userInfo = ["mode": controlMode];
        NotificationCenter.default.post(name: .updateControl, object: nil, userInfo: userInfo)
    }

    @objc func updateDriveMode(_ sender: UIView) {
        if (controlMode == .gamepad) {
            switch (driveMode) {
            case .joystick:
                driveMode = .gameController;
                createAndUpdateButton(iconName: Images.gameDriveIcon!, leadingAnchor: 140, topAnchor: 0, action: #selector(updateDriveMode(_:)));
                break;
            case .gameController:
                driveMode = .dual;
                createAndUpdateButton(iconName: Images.dualDriveIcon!, leadingAnchor: 140, topAnchor: 0, action: #selector(updateDriveMode(_:)));
                break;
            case .dual:
                driveMode = .joystick;
                createAndUpdateButton(iconName: Images.joystickIcon!, leadingAnchor: 140, topAnchor: 0, action: #selector(updateDriveMode(_:)));
                break;
            }
            let userInfo = ["drive": driveMode];
            NotificationCenter.default.post(name: .updateDriveMode, object: nil, userInfo: userInfo)
        }
    }

    @objc func updateSpeedMode(_ sender: UIView) {

        switch (speedMode) {
        case .slow:
            speedMode = .medium;
            createAndUpdateButton(iconName: Images.mediumIcon!, leadingAnchor: 240, topAnchor: 0, action: #selector(updateSpeedMode(_:)));
            break;
        case .medium:
            speedMode = .fast;
            createAndUpdateButton(iconName: Images.fastIcon!, leadingAnchor: 240, topAnchor: 0, action: #selector(updateSpeedMode(_:)));
            break;
        case .fast:
            speedMode = .slow;
            createAndUpdateButton(iconName: Images.slowIcon!, leadingAnchor: 240, topAnchor: 0, action: #selector(updateSpeedMode(_:)));
            break;
        }
        let userInfo = ["speed": speedMode];
        NotificationCenter.default.post(name: .updateSpeed, object: nil, userInfo: userInfo)
    }


}