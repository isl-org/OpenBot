//
// Created by Nitish Yadav on 22/12/22.
//

import Foundation
import UIKit

class RobotInfoFrame: UIViewController {
    var robotType = UILabel();
    var bluetoothIcon: UIImageView!;
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


    override func viewDidLoad() {
        super.viewDidLoad();
        _ = createLabels(text: "N/A", topAnchor: 10, leadingAnchor: width / 2);
        bluetoothIcon = createIcons(iconName: "", leadingAnchor: width - 40, topAnchor: 10);
        _ = createLogoIcon();
        createSensorHeading()
        createSensorCheckBoxes();
        createWheelOdometerHeading();
        createWheelOdometerCheckBoxes();
        createLedHeading();
        createLedCheckBoxes();
        createLedCheckBoxes();
        createReadingHeading();
        createReadings();
        createSendCommandHeading()
        createSendCommand();
        updateConstraints();
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        updateConstraints();
    }

    func crateRobotTypeHeading() {
        robotType = createHeadings(text: "Robot Type");
        robotType.frame.origin = CGPoint(x: 10, y: 40);
    }

    func createSensorHeading() {
        sensorHeading = createHeadings(text: "Sensors");
    }


    func createLogoIcon() -> UIImageView {
        let icon = UIImageView();
        icon.image = Images.robotInfoIcon;
        icon.frame.size = UIImage(named: "openBotLogo")!.size;
        view.addSubview(icon);
        icon.frame.origin.x = (width - 239) / 2;
        icon.frame.origin.y = 50;
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
        indicatorCheckbox = createCheckbox(leadingAnchor: 20, topAnchor: 470, action:  #selector(updateSonarCheckBox(_:)));
        indicatorLabel = createLabels(text: Strings.indicatorText, topAnchor: 460, leadingAnchor: 55);
        frontLedCheckbox = createCheckbox(leadingAnchor: 135, topAnchor: 470, action:  #selector(updateSonarCheckBox(_:)));
        frontLedLabel = createLabels(text: Strings.front, topAnchor: 460, leadingAnchor: 165);
        backLedCheckbox = createCheckbox(leadingAnchor: 215, topAnchor: 470, action: #selector(updateSonarCheckBox(_:)));
        backLedLabel = createLabels(text: Strings.back, topAnchor: 460, leadingAnchor: 245);
        statusCheckbox = createCheckbox(leadingAnchor: 290, topAnchor: 470, action:  #selector(updateSonarCheckBox(_:)));
        statusLabel = createLabels(text: Strings.status, topAnchor: 460, leadingAnchor: 320);
    }

    func createReadingHeading() {
        readingHeading = createHeadings(text: Strings.readings);
    }

    func createReadings() {
        battery = createLabels(text: Strings.battery, topAnchor: 530, leadingAnchor: 20);
        speed = createLabels(text: Strings.speedText, topAnchor: 530, leadingAnchor: 110);
        sonar = createLabels(text: Strings.sonarLabel, topAnchor: 530, leadingAnchor: 255);
    }

    func createSendCommandHeading() {
        sendCommandsHeading = createHeadings(text: Strings.sendCommand);
    }

    func createSendCommand() {
        motorLabel = createLabels(text: Strings.motors, topAnchor: 610, leadingAnchor: 20);
        forwardButton = createButton(width: 100, height:45, title: Strings.forward,leadingAnchor: 70,topAnchor: 610);
        backwardButton = createButton(width: 100, height: 45, title: Strings.backward,leadingAnchor: 180,topAnchor: 610);
        stopButton = createButton(width: 70, height: 45, title: Strings.stop,leadingAnchor: 290,topAnchor: 610);

    }


    func createIcons(iconName: String, leadingAnchor: CGFloat, topAnchor: CGFloat) -> UIImageView {
        let icon = UIImageView();
        icon.image = Images.bluetoothDisconnected;
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
        label.frame.size.width = CGFloat(text.count * 10);
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
        label.font = label.font.withSize(adapted(dimensionSize: 12, to: .height));
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
        view.addSubview(checkbox);
        checkbox.addTarget(self, action: action!, for: .valueChanged)
        return checkbox
    }


    func createButton(width: Double, height: Double, title: String, leadingAnchor : CGFloat, topAnchor : CGFloat)-> UIButton{
        let button = UIButton();
        button.frame.size = CGSize(width: width, height: height);
        button.setTitle(title, for: .normal);
        button.setTitleColor(Colors.bdColor, for: .normal);
        button.frame.origin = CGPoint(x: leadingAnchor, y: topAnchor);
        button.backgroundColor = Colors.title;
        button.layer.cornerRadius = 5;
        view.addSubview(button)
        return button;
    }

    func updateConstraints() {
        if currentOrientation == .portrait {
            sensorHeading.frame.origin = CGPoint(x: 10, y: 260);
            wheelOdometerHeadingHeading.frame.origin = CGPoint(x: 10, y: 340);
            voltageDividerCheckBox.frame.origin = CGPoint(x: 20, y: 305);
            voltageDividerLabel.frame.origin = CGPoint(x: 55, y: 295);
            sonarLabel.frame.origin = CGPoint(x: 205, y: 295);
            sonarCheckBox.frame.origin = CGPoint(x: 170, y: 305);
            bumperCheckBox.frame.origin = CGPoint(x: 275, y: 305);
            bumperLabel.frame.origin = CGPoint(x: 310, y: 295);
            frontCheckBox.frame.origin = CGPoint(x: 20, y: 385);
            frontCheckBoxLabel.frame.origin = CGPoint(x: 65, y: 375);
            backCheckBox.frame.origin = CGPoint(x: 125, y: 385);
            backLabel.frame.origin = CGPoint(x: 165, y: 375);
            ledHeading.frame.origin = CGPoint(x: 10, y: 420);
            readingHeading.frame.origin = CGPoint(x: 10, y: 500);
            battery.frame.origin = CGPoint(x: 20, y: 530);
            speed.frame.origin = CGPoint(x: 110, y: 530);
            sonar.frame.origin = CGPoint(x: 255, y: 530);
            motorLabel.frame.origin = CGPoint(x: 20, y: 610);
            forwardButton.frame.origin = CGPoint(x: 70, y: 610);
            backwardButton.frame.origin = CGPoint(x: 180, y: 610);
            stopButton.frame.origin = CGPoint(x: 290, y: 610);
            sendCommandsHeading.frame.origin = CGPoint(x: 10, y: 565);


        } else {
            sensorHeading.frame.origin = CGPoint(x: height / 2 - 15, y: 20);
            wheelOdometerHeadingHeading.frame.origin = CGPoint(x: height / 2 - 15, y: 80);
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
            indicatorCheckbox.frame.origin = CGPoint(x: height/2 - 10, y: 195);
            indicatorLabel.frame.origin = CGPoint(x: height/2 + 30, y: 185);
            frontLedCheckbox.frame.origin = CGPoint(x: height/2 + 110, y: 195);
            frontLedLabel.frame.origin = CGPoint(x: height/2 + 145, y: 185);
            backLedCheckbox.frame.origin = CGPoint(x: height/2 + 185, y: 195);
            backLedLabel.frame.origin = CGPoint(x: height/2+215, y: 185);
            statusCheckbox.frame.origin = CGPoint(x: height/2+260, y: 195);
            statusLabel.frame.origin = CGPoint(x:height/2 + 290, y: 185);
            readingHeading.frame.origin = CGPoint(x: height/2-15, y:235);
            battery.frame.origin = CGPoint(x: height/2-10, y: 265);
            speed.frame.origin = CGPoint(x: height/2+90, y: 265);
            sonar.frame.origin = CGPoint(x: height/2+250, y: 265);
            sendCommandsHeading.frame.origin = CGPoint(x: 20, y:readingHeading.frame.origin.y+30);
            motorLabel.frame.origin = CGPoint(x: 25, y: sendCommandsHeading.frame.origin.y+50);
            forwardButton.frame.origin = CGPoint(x: 90, y: sendCommandsHeading.frame.origin.y+50);
            backwardButton.frame.origin = CGPoint(x: 200, y: sendCommandsHeading.frame.origin.y+50);
            stopButton.frame.origin = CGPoint(x: 315, y: sendCommandsHeading.frame.origin.y+50);
        }
    }

    @objc func updateSonarCheckBox(_ sender: UIButton) {
        print("hello checkbox")
    }

    @objc func updateBumperCheckBox(_ sender: UIButton) {
        print("hello bumper checkbox")
    }


}
