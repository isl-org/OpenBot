//
// Created by Nitish Yadav on 16/09/22.
//

import Foundation
import UIKit

class expandSetting: UIView {
    override init(frame: CGRect) {
        super.init(frame: frame)
        createLogDataButton()
        createBluetoothIcon()
        createCameraIcon()
        createLabels(value: "Preview Resolution (1280 x 720)", positionX: 20, positionY: 120, labelWidth: 240, labelHeight: 30)
        NotificationCenter.default.addObserver(self, selector: #selector(updateScreen), name: .clickSetting, object: nil)
        createCancelButton()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    func createBluetoothIcon() {
        let bluetoothIcon = UIImageView(frame: CGRect(x: 200, y: 65, width: 30, height: 30))
        bluetoothIcon.image = UIImage(named: "bluetoothConnected")
        addSubview(bluetoothIcon)
    }

    func createCameraIcon() {
        let cameraView = UIImageView(frame: CGRect(x: 240, y: 65, width: 30, height: 30))
        cameraView.image = UIImage(named: "frontCamera")
        addSubview(cameraView)
    }

    func createLogDataButton() {
        let logData = UISwitch(frame: CGRect(x: 120, y: 65, width: 100, height: 40))
        createLabels(value: "Log Data", positionX: 40, positionY: 60, labelWidth: 100, labelHeight: 40)
        logData.isOn = true
        logData.setOn(true, animated: false)
        logData.onTintColor = Colors.title
        addSubview(logData)
    }

    func createCancelButton() {
        let cancelButton = UIButton()
        cancelButton.frame = CGRect(x: 280, y: 50, width: 60, height: 50)
        let cancelIcon = UIImageView(frame: CGRect(x: 0, y: 0, width: 50, height: 50))
        cancelIcon.image = UIImage(named: "closeIcon")
        cancelButton.addTarget(self, action: #selector(pressed), for: .touchUpInside)
        cancelButton.addSubview(cancelIcon)
        addSubview(cancelButton)

    }

    func createLabels(value: String, positionX: CGFloat, positionY: CGFloat, labelWidth: CGFloat, labelHeight: CGFloat) {
        let label = UILabel()
        label.text = value
        label.font = UIFont(name: "medium", size: adapted(dimensionSize: 30, to: .width))
        label.frame = CGRect(x: positionX, y: positionY, width: labelWidth, height: labelHeight)
        label.textColor = .white
        label.font = label.font.withSize(15)
        addSubview(label)
    }

    func createRectangle(x: Int, y: Int, width: Int, height: Int, borderColor: String) -> UIView {
//        let rectangleView = UIView(frame: CGRect(x: x, y: y, width: width, height: height))
        let rectangleView = UIView();
        let origin = CGPoint(x: x, y: y)
        let size = CGSize(width: width, height: height)
        let resize = resized(size: size, basedOn: dimension)
        rectangleView.frame = CGRect(origin: origin, size: resized(size: size, basedOn: dimension))
        rectangleView.layer.cornerRadius = 5;
        rectangleView.layer.borderWidth = 1;
        rectangleView.layer.borderColor = UIColor(named: borderColor)?.cgColor
        return rectangleView;
    }

    @objc func pressed() {
        NotificationCenter.default.post(name: .cancelButton, object: nil)
    }

    @objc func updateScreen() {

    }
}

extension Notification.Name {
    static let cancelButton = Notification.Name("cancelButton")

}