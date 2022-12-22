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

    override func viewDidLoad() {
        super.viewDidLoad();
        _ = createLabels(text: "N/A", topAnchor: 10, leadingAnchor: width / 2);
        bluetoothIcon = createIcons(iconName: "", leadingAnchor: width - 40, topAnchor: 10);
        _=createLogoIcon();
        createSensorHeading()
        createSensorCheckBoxes();
        createWheelOdometerHeading();
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
        if currentOrientation == .portrait {
            sensorHeading.frame.origin = CGPoint(x: 10, y: 262);
        } else {
            sensorHeading.frame.origin = CGPoint(x: height / 2, y: 20);
        }
    }



    func createBoxes() {
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
        if currentOrientation == .portrait {
            wheelOdometerHeadingHeading.frame.origin = CGPoint(x: 10, y: 320);
        } else {
            wheelOdometerHeadingHeading.frame.origin = CGPoint(x: height / 2, y: 20);
        }
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


    func createButton(width : Double, height : Double,title : String) {
        let button = UIButton();
        button.frame.size = CGSize(width: width, height: height);
        button.setTitle(title, for: .normal);
        button.setTitleColor(Colors.bdColor, for: .normal);

    }

    func updateConstraints() {
        if currentOrientation == .portrait {
            sensorHeading.frame.origin = CGPoint(x: 10, y: 262);
            wheelOdometerHeadingHeading.frame.origin = CGPoint(x: 10, y: 320);
            voltageDividerCheckBox.frame.origin = CGPoint(x: 20, y: 300);
            voltageDividerLabel.frame.origin = CGPoint(x: 55, y: 292);
            sonarLabel.frame.origin = CGPoint(x: 205, y: 292);
            sonarCheckBox.frame.origin = CGPoint(x: 170, y: 300);
            bumperCheckBox.frame.origin = CGPoint(x: 275, y: 300);
            bumperLabel.frame.origin = CGPoint(x: 310, y: 292);

        } else {
            sensorHeading.frame.origin = CGPoint(x: height / 2-10, y: 20);
            wheelOdometerHeadingHeading.frame.origin = CGPoint(x: height/2-10, y:80);
            voltageDividerCheckBox.frame.origin = CGPoint(x: height/2-10, y: 50);
            voltageDividerLabel.frame.origin = CGPoint(x: height/2 + 35, y: 40);
            sonarLabel.frame.origin = CGPoint(x: height/2+180, y: 40);
            sonarCheckBox.frame.origin = CGPoint(x: height/2+140, y: 50);
            bumperCheckBox.frame.origin = CGPoint(x: height/2+240, y: 50);
            bumperLabel.frame.origin = CGPoint(x: height/2+270, y: 40);
        }
    }

    @objc func updateSonarCheckBox(_ sender: UIButton) {
        print("hello checkbox")
    }

    @objc func updateBumperCheckBox(_ sender: UIButton) {
        print("hello bumper checkbox")
    }


}
