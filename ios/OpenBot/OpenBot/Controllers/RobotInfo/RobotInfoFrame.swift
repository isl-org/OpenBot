//
// Created by Nitish Yadav on 22/12/22.
//

import Foundation
import UIKit

class RobotInfoFrame: UIViewController {
    var topPadding: CGFloat = 0;
    var robotType = UILabel();
    var robotName = UILabel();
    var blueToothIconButton: UIButton!
    var refreshIcon: UIImageView!
    var openBotIcon: UIImageView!
    var sensorHeading = UILabel();
    var voltageDividerCheckBox = Checkbox();
    var sonarCheckBox = Checkbox();
    var bumperCheckBox = Checkbox()
    var voltageDividerLabel = UILabel();
    var sonarLabel = UILabel();
    var bumperLabel = UILabel();
    var wheelOdometerHeadingHeading = UILabel()
    var frontCheckBox = Checkbox();
    var frontCheckBoxLabel = UILabel();
    var backCheckBox = Checkbox();
    var backLabel = UILabel();
    var ledHeading = UILabel();
    var indicatorCheckbox = Checkbox();
    var indicatorLabel = UILabel();
    var frontLedCheckbox = Checkbox();
    var frontLedLabel = UILabel();
    var backLedCheckbox = Checkbox();
    var backLedLabel = UILabel();
    var statusCheckbox = Checkbox();
    var statusLabel = UILabel();
    var readingHeading = UILabel();
    var battery = UILabel();
    var speed = UILabel();
    var sonar = UILabel();
    var sendCommandsHeading = UILabel();
    var motorLabel = UILabel();
    var forwardButton = UIButton();
    var backwardButton = UIButton();
    var stopButton = UIButton();
    var lightLabel = UILabel();
    let bluetooth = bluetoothDataController.shared;
    var robotInfo: String = "";
    var lightSlider: UISlider!


    override func viewDidLoad() {
        super.viewDidLoad();
        let window = UIApplication.shared.keyWindow
        topPadding = window?.safeAreaInsets.top ?? 20
        createUI();
        updateConstraints();
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothConnected, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothDisconnected, object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(updateScreen), name: .updateLabel, object: nil);
        robotInfo = bluetooth.robotInfo;
        updateRobotInfo();
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        updateConstraints();
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        getRobotInfo();
        updateRobotInfo();
        if !isBluetoothConnected {
            updateScreenOnBluetoothDisconnection();
        }

    }

    func createUI() {
        crateRobotTypeHeading()
        robotName = createLabels(text: "N/A", topAnchor: topPadding + adapted(dimensionSize: 30, to: .height), leadingAnchor: width / 2);
        blueToothIconButton = createBluetoothIcon();
        createRefreshIcon()
        openBotIcon = createLogoIcon();
        createSensorHeading()
        createSensorCheckBoxes();
        createWheelOdometerHeading();
        createWheelOdometerCheckBoxes();
        createLedHeading();
        createLedCheckBoxes();
        createReadingHeading();
        createReadings();
        createSendCommandHeading()
        createSendCommand();
        createLight()
        showLight(isHidden: true);
    }

    func crateRobotTypeHeading() {
        robotType = createHeadings(text: "Robot Type");
        robotType.frame.origin = CGPoint(x: 10, y: topPadding + adapted(dimensionSize: 30, to: .height));
    }

    func createRefreshIcon() {
        if let image = UIImage(named: "refresh") {
            refreshIcon = createIcons(iconImage: image, leadingAnchor: width - 100, topAnchor: topPadding + adapted(dimensionSize: 30, to: .height));
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(refresh(tapGestureRecognizer:)));
        refreshIcon.isUserInteractionEnabled = true;
        refreshIcon.addGestureRecognizer(tapGesture);
    }

    func createBluetoothIcon() -> UIButton {
        let bleButton = UIButton();
        if isBluetoothConnected {
            bleButton.setImage(Images.bluetoothConnected!, for: .normal);
        } else {
            bleButton.setImage(Images.bluetoothDisconnected!, for: .normal);
        }
        bleButton.frame = CGRect(x: width - 60, y: topPadding + adapted(dimensionSize: 40, to: .height), width: 40, height: 40)
        view.addSubview(bleButton);
        bleButton.addTarget(self, action: #selector(openBluetoothSettings(_:)), for: .touchDown);
        return bleButton;
    }

    func createSensorHeading() {
        sensorHeading = createHeadings(text: "Sensors");
    }


    func createLogoIcon() -> UIImageView {
        let icon = UIImageView();
        icon.image = UIImage(named: "openBotLogo");
        let imageSize = UIImage(named: "openBotLogo")!.size;
        let logoWidth = imageSize.width * 0.85;
        let logoHeight = imageSize.height * 0.70;
        icon.frame.size = CGSize(width: logoWidth, height: logoHeight);
        view.addSubview(icon);
        icon.frame.origin.x = (width - icon.frame.width) / 2;
        icon.frame.origin.y = robotType.frame.origin.y + adapted(dimensionSize: 25, to: .height);
        return icon;
    }

    func createSensorCheckBoxes() {
        voltageDividerCheckBox = createCheckbox(leadingAnchor: 20, topAnchor: 300, action: #selector(updateSonarCheckBox(_:)));
        voltageDividerLabel = createLabels(text: Strings.voltageDivider, topAnchor: 292, leadingAnchor: 55);
        sonarCheckBox = createCheckbox(leadingAnchor: 170, topAnchor: 300, action: #selector(updateSonarCheckBox(_:)));
        sonarLabel = createLabels(text: Strings.sonarText, topAnchor: 292, leadingAnchor: 205);
        bumperCheckBox = createCheckbox(leadingAnchor: 275, topAnchor: 300, action: #selector(updateBumperCheckBox(_:)));
        bumperLabel = createLabels(text: Strings.bumperText, topAnchor: 292, leadingAnchor: 310);
    }

    func createWheelOdometerHeading() {
        wheelOdometerHeadingHeading = createHeadings(text: Strings.wheelOdometer);
    }

    func createWheelOdometerCheckBoxes() {
        frontCheckBox = createCheckbox(leadingAnchor: 20, topAnchor: 375, action: #selector(updateSonarCheckBox(_:)));
        frontCheckBoxLabel = createLabels(text: Strings.front, topAnchor: 365, leadingAnchor: 65);
        backCheckBox = createCheckbox(leadingAnchor: 125, topAnchor: 375, action: #selector(updateSonarCheckBox(_:)));
        backLabel = createLabels(text: Strings.back, topAnchor: 365, leadingAnchor: 165);
    }

    func createLedHeading() {
        ledHeading = createHeadings(text: Strings.led);
    }

    func createLedCheckBoxes() {
        indicatorCheckbox = createCheckbox(leadingAnchor: 20, topAnchor: 470, action: #selector(updateSonarCheckBox(_:)));
        indicatorLabel = createLabels(text: Strings.indicatorText, topAnchor: 460, leadingAnchor: 55);
        frontLedCheckbox = createCheckbox(leadingAnchor: 135, topAnchor: 470, action: #selector(updateSonarCheckBox(_:)));
        frontLedLabel = createLabels(text: Strings.front, topAnchor: 460, leadingAnchor: 165);
        backLedCheckbox = createCheckbox(leadingAnchor: 215, topAnchor: 470, action: #selector(updateSonarCheckBox(_:)));
        backLedLabel = createLabels(text: Strings.back, topAnchor: 460, leadingAnchor: 245);
        statusCheckbox = createCheckbox(leadingAnchor: 290, topAnchor: 470, action: #selector(updateSonarCheckBox(_:)));
        statusLabel = createLabels(text: Strings.status, topAnchor: 460, leadingAnchor: 320);
    }

    func createReadingHeading() {
        readingHeading = createHeadings(text: Strings.readings);
    }

    func createReadings() {
        battery = createLabels(text: Strings.battery, topAnchor: 530, leadingAnchor: 20);
        speed = createLabels(text: Strings.speedText, topAnchor: 530, leadingAnchor: 110);
        speed.font = speed.font.withSize(14);
        sonar = createLabels(text: Strings.sonarLabel, topAnchor: 530, leadingAnchor: 255);
    }

    func createSendCommandHeading() {
        sendCommandsHeading = createHeadings(text: Strings.sendCommand);
    }

    func createSendCommand() {
        motorLabel = createLabels(text: Strings.motors, topAnchor: 610, leadingAnchor: 20);
        forwardButton = createButton(width: 100, height: 45, title: Strings.forward, leadingAnchor: 70, topAnchor: 610, action: #selector(forwardButton(_:)));
        backwardButton = createButton(width: 100, height: 45, title: Strings.backward, leadingAnchor: 180, topAnchor: 610, action: #selector(backwardButton(_:)));
        stopButton = createButton(width: 70, height: 45, title: Strings.stop, leadingAnchor: 290, topAnchor: 610, action: #selector(stopButton(_:)));
    }

    func createLight() {
        lightLabel = createLabels(text: Strings.lightLabel, topAnchor: 610, leadingAnchor: 10);
        lightSlider = createSlider();
    }

    func createIcons(iconImage: UIImage, leadingAnchor: CGFloat, topAnchor: CGFloat) -> UIImageView {
        let icon = UIImageView();
        icon.image = iconImage;
        icon.frame.size = CGSize(width: 20, height: 20);
        view.addSubview(icon);
        icon.frame.origin.x = leadingAnchor;
        icon.frame.origin.y = topAnchor;
        return icon;
    }

    func createHeadings(text: String) -> UILabel {
        let label = UILabel();
        label.text = text;
        label.textColor = Colors.bdColor;
        label.frame.size.width = CGFloat(text.count * 15);
        label.frame.size.height = 40;
        view.addSubview(label);
        label.font = HelveticaNeue.bold(size: 12);
        return label;
    }

    func createLabels(text: String, topAnchor: CGFloat, leadingAnchor: CGFloat) -> UILabel {
        let label = UILabel();
        label.text = text;
        label.textColor = Colors.bdColor;
        label.frame.size.width = CGFloat(text.count * 10);
        label.font = label.font.withSize(15);
//        label.font = label.font.withSize(adapted(dimensionSize: 12, to: .height));
        label.frame.size.height = 40;
        view.addSubview(label);
        label.frame.origin.x = leadingAnchor;
        label.frame.origin.y = topAnchor;
        return label;
    }


    func createCheckbox(leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector?) -> Checkbox {
        let checkbox = Checkbox(frame: CGRect(x: leadingAnchor, y: topAnchor, width: 25, height: 25));
        checkbox.checkedBorderColor = Colors.bdColor;
        checkbox.uncheckedBorderColor = Colors.bdColor;
        checkbox.checkmarkStyle = .tick;
        checkbox.checkmarkColor = Colors.bdColor;
        checkbox.isEnabled = false;
        view.addSubview(checkbox);
        checkbox.addTarget(self, action: action!, for: .valueChanged)
        return checkbox
    }


    func createButton(width: Double, height: Double, title: String, leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector) -> UIButton {
        let button = UIButton();
        button.frame.size = CGSize(width: width, height: height);
        button.setTitle(title, for: .normal);
        button.setTitleColor(Colors.bdColor, for: .normal);
        button.frame.origin = CGPoint(x: leadingAnchor, y: topAnchor);
        button.backgroundColor = Colors.title;
        button.layer.cornerRadius = 5;
        button.addTarget(nil, action: action, for: .touchDown);
        view.addSubview(button)
        return button;
    }

    func createSlider() -> UISlider {
        let slider = UISlider();
        slider.frame = CGRect(x: 100, y: 610, width: 200, height: 50);
        view.addSubview(slider);
        slider.thumbTintColor = Colors.title;
        slider.minimumTrackTintColor = Colors.title;
        slider.addTarget(self, action: #selector(lightValueChange(_:)), for: .valueChanged)
        return slider;
    }

    func updateConstraints() {
        if currentOrientation == .portrait {
            robotType.frame.origin.y = topPadding + adapted(dimensionSize: 20, to: .height);
            robotName.frame.origin.y = topPadding + adapted(dimensionSize: 20, to: .height);
            robotName.frame.origin.x = robotType.frame.origin.x + CGFloat((robotType.text?.count ?? 0) * 10) + adapted(dimensionSize: 35, to: .height);
            sensorHeading.frame.origin = CGPoint(x: 10, y: 210);
            blueToothIconButton.frame.origin = CGPoint(x: width - 40, y: topPadding + adapted(dimensionSize: 25, to: .height));
            refreshIcon.frame.origin.y = topPadding + adapted(dimensionSize: 30, to: .height);
            refreshIcon.frame.origin.x = blueToothIconButton.frame.origin.x - 50;
            voltageDividerCheckBox.frame.origin = CGPoint(x: 20, y: sensorHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            voltageDividerLabel.frame.origin = CGPoint(x: 55, y: sensorHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            sonarLabel.frame.origin = CGPoint(x: 205, y: sensorHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            sonarCheckBox.frame.origin = CGPoint(x: 170, y: sensorHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            bumperCheckBox.frame.origin = CGPoint(x: 265, y: sensorHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            bumperLabel.frame.origin = CGPoint(x: 300, y: sensorHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            wheelOdometerHeadingHeading.frame.origin = CGPoint(x: 10, y: voltageDividerLabel.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            frontCheckBox.frame.origin = CGPoint(x: 20, y: wheelOdometerHeadingHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            frontCheckBoxLabel.frame.origin = CGPoint(x: 65, y: wheelOdometerHeadingHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            backCheckBox.frame.origin = CGPoint(x: 125, y: wheelOdometerHeadingHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            backLabel.frame.origin = CGPoint(x: 165, y: wheelOdometerHeadingHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            ledHeading.frame.origin = CGPoint(x: 10, y: wheelOdometerHeadingHeading.frame.origin.y + adapted(dimensionSize: 65, to: .height));
            indicatorCheckbox.frame.origin = CGPoint(x: 20, y: ledHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            indicatorLabel.frame.origin = CGPoint(x: 55, y: ledHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            frontLedCheckbox.frame.origin = CGPoint(x: 135, y: ledHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            frontLedLabel.frame.origin = CGPoint(x: 165, y: ledHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            backLedCheckbox.frame.origin = CGPoint(x: 215, y: ledHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            backLedLabel.frame.origin = CGPoint(x: 245, y: ledHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            statusCheckbox.frame.origin = CGPoint(x: 290, y: ledHeading.frame.origin.y + adapted(dimensionSize: 40, to: .height));
            statusLabel.frame.origin = CGPoint(x: 320, y: ledHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            readingHeading.frame.origin = CGPoint(x: 10, y: ledHeading.frame.origin.y + adapted(dimensionSize: 65, to: .height));
            battery.frame.origin = CGPoint(x: 20, y: readingHeading.frame.origin.y + adapted(dimensionSize: 30, to: .height));
            speed.frame.origin = CGPoint(x: 120, y: readingHeading.frame.origin.y + adapted(dimensionSize: 30, to: .height));
            sonar.frame.origin = CGPoint(x: 275, y: readingHeading.frame.origin.y + adapted(dimensionSize: 30, to: .height));
            sendCommandsHeading.frame.origin = CGPoint(x: 10, y: readingHeading.frame.origin.y + adapted(dimensionSize: 65, to: .height));
            motorLabel.frame.origin = CGPoint(x: 20, y: sendCommandsHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            motorLabel.isHidden = false
            forwardButton.frame.origin = CGPoint(x: 70, y: sendCommandsHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            backwardButton.frame.origin = CGPoint(x: 180, y: sendCommandsHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            stopButton.frame.origin = CGPoint(x: 290, y: sendCommandsHeading.frame.origin.y + adapted(dimensionSize: 35, to: .height));
            lightLabel.frame.origin = CGPoint(x: 10, y: motorLabel.frame.origin.y + 50)
            lightSlider.frame = CGRect(x: 70, y: (motorLabel.frame.origin.y + adapted(dimensionSize: 45, to: .height)), width: width - 90, height: 40);

        } else {
            blueToothIconButton.frame.origin = CGPoint(x: width - 100, y: 20);
            robotName.frame.origin.y = 20;
            robotType.frame.origin = CGPoint(x: topPadding, y: 20)
            robotName.frame.origin.x = robotType.frame.origin.x + CGFloat((robotType.text?.count ?? 0) * 10) + adapted(dimensionSize: 20, to: .height);
            refreshIcon.frame.origin = CGPoint(x: width - 140, y: 30);
            refreshIcon.frame.origin.x = blueToothIconButton.frame.origin.x - 20;
            sensorHeading.frame.origin = CGPoint(x: height / 2 - 15, y: 20);
            wheelOdometerHeadingHeading.frame.origin = CGPoint(x: height / 2 - 15, y: 80);
            voltageDividerCheckBox.frame.origin = CGPoint(x: height / 2 - 10, y: 50);
            voltageDividerCheckBox.frame.origin = CGPoint(x: height / 2 - 10, y: 50);
            voltageDividerLabel.frame.origin = CGPoint(x: height / 2 + 35, y: 40);
            sonarLabel.frame.origin = CGPoint(x: height / 2 + 180, y: 40);
            sonarCheckBox.frame.origin = CGPoint(x: height / 2 + 140, y: 50);
            bumperCheckBox.frame.origin = CGPoint(x: height / 2 + 240, y: 50);
            bumperLabel.frame.origin = CGPoint(x: height / 2 + 270, y: 40);
            frontCheckBox.frame.origin = CGPoint(x: height / 2 - 10, y: 120);
            frontCheckBoxLabel.frame.origin = CGPoint(x: height / 2 + 35, y: 110);
            backCheckBox.frame.origin = CGPoint(x: height / 2 + 95, y: 120);
            backLabel.frame.origin = CGPoint(x: height / 2 + 135, y: 110);
            ledHeading.frame.origin = CGPoint(x: height / 2 - 15, y: 155);
            indicatorCheckbox.frame.origin = CGPoint(x: height / 2 - 10, y: 195);
            indicatorLabel.frame.origin = CGPoint(x: height / 2 + 30, y: 185);
            frontLedCheckbox.frame.origin = CGPoint(x: height / 2 + 110, y: 195);
            frontLedLabel.frame.origin = CGPoint(x: height / 2 + 145, y: 185);
            backLedCheckbox.frame.origin = CGPoint(x: height / 2 + 185, y: 195);
            backLedLabel.frame.origin = CGPoint(x: height / 2 + 215, y: 185);
            statusCheckbox.frame.origin = CGPoint(x: height / 2 + 260, y: 195);
            statusLabel.frame.origin = CGPoint(x: height / 2 + 290, y: 185);
            readingHeading.frame.origin = CGPoint(x: height / 2 - 15, y: 235);
            battery.frame.origin = CGPoint(x: height / 2 - 10, y: 265);
            speed.frame.origin = CGPoint(x: height / 2 + 90, y: 265);
            sonar.frame.origin = CGPoint(x: height / 2 + 250, y: 265);
            sendCommandsHeading.frame.origin = CGPoint(x: topPadding, y: readingHeading.frame.origin.y + 30);
            motorLabel.isHidden = true
            forwardButton.frame.origin = CGPoint(x: 10, y: sendCommandsHeading.frame.origin.y + 50);
            backwardButton.frame.origin = CGPoint(x: 120, y: sendCommandsHeading.frame.origin.y + 50);
            stopButton.frame.origin = CGPoint(x: 230, y: sendCommandsHeading.frame.origin.y + 50);
            lightLabel.frame.origin = CGPoint(x: height / 2 - 15, y: stopButton.frame.origin.y);
            lightSlider.frame = CGRect(x: height / 2 + 55, y: sendCommandsHeading.frame.origin.y + 50, width: height - 390, height: 40);
        }
    }

    func getRobotInfo() {
        if isBluetoothConnected {
//            bluetoothIcon.image = UIImage(named: "bluetoothConnected");
            bluetooth.sendData(payload:"f\n");
            robotInfo = bluetooth.robotInfo;

        } else {
//            bluetoothIcon.image = UIImage(named: "bluetoothDisconnected");
        }
    }

    func updateRobotInfo() {
        robotInfo = bluetooth.robotInfo;
        if robotInfo != "" {
            setRobotName();
            setVoltageCheck();
            setSonarCheck();
            setBumperCheck();
            setFrontWheelOdometryCheck();
            setBackWheelOdometryCheck();
            setFrontLedCheck();
            setBackLedCheck();
            setIndicatorCheck();
            setFrontLedCheck();
            setBackLedCheck();
            setStatusLedCheck();
            showLight(isHidden: false)
            updateLogo(robotName: getRobotName());
        }
    }

    func updateLogo(robotName: String) {
        var asset: NSDataAsset? = nil
//        let data = asset?.data
//        icon.image = UIImage.gifImageWithData(data!)

        switch robotName {
        case "N/A":
            openBotIcon.image = UIImage(named: "openBotLogo");
            break;
        case "DIY":
            asset = NSDataAsset(name: "diy");
            break
        case "MTV":
            asset = NSDataAsset(name: "mtv");
            break;
        case "DIY_ITINKER":
            asset = NSDataAsset(name: "diy");
        case "RC_CAR":
            asset = NSDataAsset(name: "rc_car");
        case "RTR_520":
            asset = NSDataAsset(name: "rtr_520");
        case "rtr_tt":
            asset = NSDataAsset(name: "rtr_tt");
        default:
            openBotIcon.image = UIImage(named: "openBotLogo");
            return;
        }
        let data = asset?.data
        if let data {
            openBotIcon.image = UIImage.gifImageWithData(data)
        }
    }


    func getRobotName() -> String {
        let firstIndex = robotInfo.index(after: robotInfo.firstIndex(of: "f")!);
        let lastIndex = robotInfo.firstIndex(of: ":")!;
        if (firstIndex != nil) && (lastIndex != nil) {
            return robotInfo.substring(with: firstIndex..<lastIndex);
        }
        return "N/A"
    }

    func setRobotName() {
        robotName.text = getRobotName();
        robotName.frame.size.width = CGFloat(getRobotName().count * 10);
    }

    func setVoltageCheck() {
        if robotInfo.contains(":v") {
            voltageDividerCheckBox.isChecked = true;
        }
    }

    func setSonarCheck() {
        if robotInfo.contains(":s") {
            sonarCheckBox.isChecked = true;
        }
    }

    func setBumperCheck() {
        if robotInfo.contains(":b") {
            bumperCheckBox.isChecked = true;
        }
    }

    func setFrontWheelOdometryCheck() {
        if robotInfo.contains(":wf") {
            frontCheckBox.isChecked = true;
        }
    }

    func setBackWheelOdometryCheck() {
        if robotInfo.contains(":wb") {
            backCheckBox.isChecked = true;
        }
    }

    func setIndicatorCheck() {
        if robotInfo.contains("i:") {
            indicatorCheckbox.isChecked = true;
        }
    }

    func setFrontLedCheck() {
        if robotInfo.contains("lf:") {
            frontLedCheckbox.isChecked = true;
        }
    }

    func setBackLedCheck() {
        if robotInfo.contains("lb:") {
            backLedCheckbox.isChecked = true;
        }
    }

    func setStatusLedCheck() {
        if robotInfo.contains("ls:") {
            statusCheckbox.isChecked = true;
        }
    }

    func showLight(isHidden: Bool) {
        lightLabel.isHidden = isHidden;
        lightSlider.isHidden = isHidden;
    }

    @objc func updateSonarCheckBox(_ sender: UIButton) {

    }

    @objc func updateBumperCheckBox(_ sender: UIButton) {
    }

    @objc func updateConnect(_ notification: Notification) {
        if (isBluetoothConnected) {
            blueToothIconButton.setImage(Images.bluetoothConnected, for: .normal);
            updateRobotInfo()
        } else {
            blueToothIconButton.setImage(Images.bluetoothDisconnected, for: .normal);
            updateScreenOnBluetoothDisconnection();
        }
    }

    @objc func forwardButton(_ sender: UIButton) {
        bluetooth.sendData(payload: "c" + String(192) + "," + String(192) + "\n");
    }

    @objc func backwardButton(_ sender: UIButton) {
        bluetooth.sendData(payload: "c" + String(-192) + "," + String(-192) + "\n");
    }

    @objc func stopButton(_ sender: UIButton) {
        bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n");
    }

    @objc func updateScreen() {
        updateSonar()
        updateVoltage()
        updateSpeedometer()
    }

    func updateSonar() {
        let sonarData = bluetooth.sonarData
        if sonarData != "" {
            let index = sonarData.index(after: sonarData.startIndex)
            let actualSonarValue = min(Int(String(sonarData[index...])) ?? 0, 300)
            sonar.text = "Sonar " + String(actualSonarValue) + " CM"
        }
    }

    func updateVoltage() {
        let voltageData = bluetooth.voltageDivider
        if voltageData != "" {
            let index = voltageData.index(after: voltageData.startIndex)
            battery.text = "Battery " + String(voltageData[index...]) + " V"
        }
    }

    func updateSpeedometer() {
        let speedometer = bluetooth.speedometer
        if speedometer != "" {
            let index_1 = speedometer.index(after: speedometer.startIndex)
            let indexOfComma = speedometer.firstIndex(of: ",") ?? index_1
            let index_2 = speedometer.index(before: indexOfComma)
            let leftFront = Float(speedometer[index_1...index_2])
            let rightFont = Float(speedometer[speedometer.index(after: indexOfComma)...])
            speed.text = Strings.speed + " (l,r) " + String(leftFront!) + " " + String(rightFont!) + " rpm";
            print(speedometer);
        }
    }

    func updateScreenOnBluetoothDisconnection() {
        robotName.text = "N/A";
        sonarCheckBox.isChecked = false;
        bumperCheckBox.isChecked = false;
        frontCheckBox.isChecked = false;
        backCheckBox.isChecked = false;
        indicatorCheckbox.isChecked = false;
        frontLedCheckbox.isChecked = false;
        backLedCheckbox.isChecked = false;
        statusCheckbox.isChecked = false;
        voltageDividerCheckBox.isChecked = false;
        showLight(isHidden: true)
        updateLogo(robotName: "N/A")
    }

    @objc func openBluetoothSettings(_ sender: UIButton) {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: Strings.bluetoothScreen))
        navigationController?.pushViewController(nextViewController!, animated: true)
    }

    @objc func refresh(tapGestureRecognizer: UITapGestureRecognizer) {
        if isBluetoothConnected {
            bluetooth.sendData(payload:"f\n");
            updateRobotInfo();
        } else {
            updateScreenOnBluetoothDisconnection()
        }
    }


    @objc func lightValueChange(_ sender: UISlider) {
        print(sender.value)
        var front = round((sender.value * 255) * 100) / 100;
        var back = round((sender.value * 255) * 100) / 100;
        bluetooth.sendData(payload: "l" + String(front) + "," + String(back) + "\n");
    }
}

extension String {
    func index(from: Int) -> Index {
        self.index(startIndex, offsetBy: from)
    }

    func substring(from: Int) -> String {
        let fromIndex = index(from: from)
        return String(self[fromIndex...])
    }

    func substring(to: Int) -> String {
        let toIndex = index(from: to)
        return String(self[..<toIndex])
    }

    func substring(with r: Range<Int>) -> String {
        let startIndex = index(from: r.lowerBound)
        let endIndex = index(from: r.upperBound)
        return String(self[startIndex..<endIndex])
    }
}
