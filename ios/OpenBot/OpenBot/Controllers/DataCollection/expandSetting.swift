//
// Created by Nitish Yadav on 16/09/22.
//

import Foundation
import UIKit

class expandSetting: UIView {
    let logData = UISwitch()
    let bluetoothIcon = UIImageView()
    let cameraIcon = UIImageView()
    let cancelButton = UIButton()
    var low = UIView()

    override init(frame: CGRect) {
        super.init(frame: frame)
        applyBlurEffect()
        createLogDataButton()
        createBluetoothIcon()
        createCameraIcon()
        createLabels(value: Strings.previewResulation, leadingAnchor: 10, topAnchor: 50, labelWidth: 240, labelHeight: 30)
        createCancelButton()
        createLow()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    func createLogDataButton() {

        logData.frame.size = CGSize(width: 100, height: 100)
        createLabels(value: Strings.logData, leadingAnchor: 10, topAnchor: 13, labelWidth: 100, labelHeight: 40)
        logData.isOn = true
        logData.setOn(true, animated: false)
        logData.onTintColor = Colors.title
        logData.transform = CGAffineTransform(scaleX: 1.2, y: 0.7)
        logData.addTarget(self, action: #selector(switchLogButton(_:)), for: .valueChanged)
        logData.translatesAutoresizingMaskIntoConstraints = false
        addSubview(logData)
        logData.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 90).isActive = true
        logData.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true
    }

    func createBluetoothIcon() {
        bluetoothIcon.frame.size = CGSize(width: 30, height: 30)
        bluetoothIcon.translatesAutoresizingMaskIntoConstraints = false
        bluetoothIcon.image = Images.bluetoothConnected
        addSubview(bluetoothIcon)
        bluetoothIcon.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true
        bluetoothIcon.leadingAnchor.constraint(equalTo: logData.trailingAnchor, constant: 20).isActive = true

    }

    func createCameraIcon() {
        cameraIcon.frame.size = CGSize(width: 30, height: 30)
        cameraIcon.translatesAutoresizingMaskIntoConstraints = false
        cameraIcon.image = Images.frontCamera
        cameraIcon.isUserInteractionEnabled = true
        let tap = UITapGestureRecognizer(target: self, action: #selector(reverseCamera(_:)))
        cameraIcon.addGestureRecognizer(tap)
        addSubview(cameraIcon)
        cameraIcon.leadingAnchor.constraint(equalTo: bluetoothIcon.trailingAnchor, constant: 20).isActive = true
        cameraIcon.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true
    }



    func createCancelButton() {

        cancelButton.frame.size = CGSize(width: 60, height: 60)
        let cancelIcon = UIImageView(frame: CGRect(x: 0, y: 0, width: 60, height: 60))
        cancelIcon.image = Images.closeIcon
        cancelButton.addTarget(self, action: #selector(cancelExpandedView), for: .touchUpInside)
        cancelButton.addSubview(cancelIcon)
        cancelButton.translatesAutoresizingMaskIntoConstraints = false
        addSubview(cancelButton)
        cancelButton.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 2).isActive = true
        cancelButton.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: -20).isActive = true


    }

    func createLow(){
       low =   createRectangle(width: 100, height: 100, borderColor: "sonar")
        addSubview(low)
        low.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 200).isActive = true
        low.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 200).isActive = true
        print(low.frame)
    }

    func createLabels(value: String, leadingAnchor: CGFloat, topAnchor: CGFloat, labelWidth: CGFloat, labelHeight: CGFloat) {
        let label = UILabel()
        label.text = value
        label.font = UIFont(name: "medium", size: adapted(dimensionSize: 30, to: .width))
        label.frame.size = CGSize(width: labelWidth, height: labelHeight)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.textColor = .white
        label.font = label.font.withSize(15)
        addSubview(label)
        label.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true
        label.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: leadingAnchor).isActive = true
    }

    func createRectangle( width: Int, height: Int, borderColor: String) -> UIView {
        let rectangle = UIView()
        rectangle.frame.size = CGSize( width: 100, height: 100)
        rectangle.backgroundColor = .red
        rectangle.translatesAutoresizingMaskIntoConstraints = false
        return rectangle
    }

    @objc func cancelExpandedView() {
        NotificationCenter.default.post(name: .cancelButton, object: nil)
    }

    @objc func updateScreen() {

    }

    @objc func reverseCamera(_ sender: UITapGestureRecognizer? = nil) {
        NotificationCenter.default.post(name: .switchCamera, object: nil)
    }

    func applyBlurEffect() {
        let blurEffectView = UIView(frame: bounds);
        blurEffectView.backgroundColor = UIColor(named: "darkBg");
        blurEffectView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        addSubview(blurEffectView)
    }

    @objc func switchLogButton(_ sender: UISwitch) {
        print("hello switch button")
    }
}

extension Notification.Name {
    static let cancelButton = Notification.Name(Strings.cancelButton)

}