//
// Created by Nitish Yadav on 23/09/22.
//

import Foundation
import UIKit

class VehicleControl: UIView {
    var controlMode: ControlMode = ControlMode.PHONE;
    var speedMode: SpeedMode = SpeedMode.SLOW;
    var driveMode: DriveMode = DriveMode.DUAL;
    var speedLabel = UILabel()
    var speedInRpm = UILabel()
    var isButtonEnable: Bool = true
    let gameController = GameController.shared
    var downSwipe: Bool = false
    let audioPlayer = AudioPlayer.shared;

    /// initializing function
    override init(frame: CGRect) {
        super.init(frame: frame)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        setupVehicleControl();
        updateControlMode(self)
        updateDriveMode(self)
        updateSpeedMode(self)
        createRpm()
        createLabel(text: Strings.controller, bottomAnchor: 0, leadingAnchor: width / 2 - 100, isBoldNeeded: true)
        createLabel(text: Strings.driveMode, bottomAnchor: 0, leadingAnchor: width / 2 - 30, isBoldNeeded: true)
        createLabel(text: Strings.speed, bottomAnchor: 0, leadingAnchor: width / 2 + 50, isBoldNeeded: true)
        NotificationCenter.default.addObserver(self, selector: #selector(toggleAutoMode), name: .autoMode, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(toggleAutoMode), name: .autoModeObjectTracking, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateRpmLabel), name: .updateRpmLabel, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(decreaseSpeedMode), name: .decreaseSpeedMode, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(increaseSpeedMode), name: .increaseSpeedMode, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDrive), name: .updateDriveMode, object: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    /// UI function to set the constraint
    func setupVehicleControl() {
        widthAnchor.constraint(equalToConstant: width).isActive = true;
        heightAnchor.constraint(equalToConstant: 80).isActive = true;
    }

    /// UI function to create a button with following
    ///
    /// - Parameters:
    ///   - iconName: name of the image icon
    ///   - leadingAnchor: space from the left.
    ///   - topAnchor: space from top
    ///   - action: on click action
    ///   - activated: is button active
    func createAndUpdateButton(iconName: UIImage, leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector?, activated: Bool) {
        let btn = UIButton()
        btn.layer.cornerRadius = 3
        if (activated == true) {
            btn.backgroundColor = Colors.title
        } else {
            btn.backgroundColor = Colors.titleDeactivated
        }
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

    /// function to update the control mode of the game
    @objc func updateControlMode(_ sender: UIView) {
        if (isButtonEnable) {
            if (controlMode == ControlMode.GAMEPAD) {
                controlMode = ControlMode.PHONE;
                driveMode = DriveMode.DUAL;
                createAndUpdateButton(iconName: Images.phoneIcon!, leadingAnchor: width / 2 - 100, topAnchor: 0, action: #selector(updateControlMode(_:)), activated: true);
                createAndUpdateButton(iconName: Images.dualDriveIcon!, leadingAnchor: width / 2 - 30, topAnchor: 0, action: #selector(updateDriveMode(_:)), activated: false);

                client.start();
                let msg = JSON.toString(ConnectionActiveEvent(status: .init(CONNECTION_ACTIVE: "true")));
                client.send(message: msg);

            } else {
                controlMode = ControlMode.GAMEPAD;
                driveMode = DriveMode.GAME;
                createAndUpdateButton(iconName: Images.gamepadIcon!, leadingAnchor: width / 2 - 100, topAnchor: 0, action: #selector(updateControlMode(_:)), activated: true);
                createAndUpdateButton(iconName: Images.gameDriveIcon!, leadingAnchor: width / 2 - 30, topAnchor: 0, action: #selector(updateDriveMode(_:)), activated: true);
                let msg = JSON.toString(ConnectionActiveEvent(status: .init(CONNECTION_ACTIVE: "false")));
                client.send(message: msg);


            }
            gameController.selectedControlMode = controlMode
        }
    }

    /// function to update the drive mode
    @objc func updateDriveMode(_ sender: UIView) {
        if (controlMode == .GAMEPAD && isButtonEnable) {
            switch (driveMode) {
            case .JOYSTICK:
                driveMode = .GAME;
                createAndUpdateButton(iconName: Images.gameDriveIcon!, leadingAnchor: width / 2 - 30, topAnchor: 0, action: #selector(updateDriveMode(_:)), activated: true);
                break;
            case .GAME:
                driveMode = .DUAL;
                createAndUpdateButton(iconName: Images.dualDriveIcon!, leadingAnchor: width / 2 - 30, topAnchor: 0, action: #selector(updateDriveMode(_:)), activated: true);
                break;
            case .DUAL:
                driveMode = .JOYSTICK;
                createAndUpdateButton(iconName: Images.joystickIcon!, leadingAnchor: width / 2 - 30, topAnchor: 0, action: #selector(updateDriveMode(_:)), activated: true);
                break;
            }
            gameController.selectedDriveMode = driveMode
        }
    }

    /// function to update the speed mode
    @objc func updateSpeedMode(_ sender: UIView) {
        if isButtonEnable {
            switch (speedMode) {
            case .SLOW:
                speedMode = .NORMAL;
                createAndUpdateButton(iconName: Images.mediumIcon!, leadingAnchor: width / 2 + 40, topAnchor: 0, action: #selector(updateSpeedMode(_:)), activated: true);
                break;
            case .NORMAL:
                speedMode = .FAST;
                createAndUpdateButton(iconName: Images.fastIcon!, leadingAnchor: width / 2 + 40, topAnchor: 0, action: #selector(updateSpeedMode(_:)), activated: true);
                break;
            case .FAST:
                speedMode = .SLOW;
                createAndUpdateButton(iconName: Images.slowIcon!, leadingAnchor: width / 2 + 40, topAnchor: 0, action: #selector(updateSpeedMode(_:)), activated: true);
                break;
            }
            gameController.selectedSpeedMode = speedMode

        }
    }

    /// function to create the label for display with following
    ///
    /// - Parameters:
    ///   - text: Message to display
    ///   - bottomAnchor: space from bottom
    ///   - leadingAnchor: space from left.
    ///   - isBoldNeeded: is bold?
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

    /// function to create the RPM message to display.
    func createRpm() {
        speedInRpm.frame.size = CGSize(width: 100, height: 40);
        speedInRpm.text = "---,--- rpm"
        addSubview(speedInRpm)
        speedInRpm.font = speedLabel.font.withSize(13.5)
        speedInRpm.translatesAutoresizingMaskIntoConstraints = false
        speedInRpm.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -10).isActive = true
        speedInRpm.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -40).isActive = true
    }

    /// function to toggle the auto mode.
    @objc func toggleAutoMode(_ notification: Notification) {
        isButtonEnable = !isButtonEnable
    }

    /// function to update the RPM speed
    @objc func updateRpmLabel(_ notification: Notification) {
        speedInRpm.text = (notification.object as! String)
    }

    ///function to update the setting to display or not
    @objc func swipeDown(_ notification: Notification) {
        switch notification.object as! Bool {
        case true:
            downSwipe = true
        case false:
            downSwipe = false
        }
    }

    /// function to decrease the speed modes
    @objc func decreaseSpeedMode(_ notification: Notification) {
        switch speedMode {
        case .SLOW:
            return;
        case .NORMAL:
            speedMode = .FAST
            break;
        case .FAST:
            speedMode = .SLOW;
            break;
        }
        updateSpeedMode(self);
        audioPlayer.playSpeedMode(speedMode: speedMode);
    }

    ///function to increase the speed modes.
    @objc func increaseSpeedMode(_ notification: Notification) {
        switch speedMode {
        case .SLOW:
            updateSpeedMode(self);
        case .NORMAL:
            updateSpeedMode(self);
            break;
        case .FAST:
            return;
        }
        audioPlayer.playSpeedMode(speedMode: speedMode);
    }

    /// function to update the drive modes.
    @objc func updateDrive(_ notification: Notification) {
        updateDriveMode(self)
        audioPlayer.playDriveMode(driveMode: driveMode);
    }
}
