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
    var previewResolution = UILabel()
    var low = UIButton()
    var medium = UIButton()
    var high = UIButton()
    var modelResolution = UILabel()
    var server = UIButton()
    var secondView = UIView()
    var leadingConstraint: NSLayoutConstraint!
    var topConstraint: NSLayoutConstraint!
    var widthConstraint: NSLayoutConstraint!
    var heightConstraint: NSLayoutConstraint!
    var preview = UIButton()
    var training = UIButton()
    var vehicle = UIButton()
    var gps = UIButton()
    var acceleration = UIButton()
    var magnetic = UIButton()
    var temperature = UIButton()
    var gyroscope = UIButton()
    var proximity = UIButton()
    var light = UIButton()
    var gravity = UIButton()
    var motion = UIButton()
    var selectedImages: ImagesMode = ImagesMode.preview
    var selectedResolution: Resolutions = Resolutions.medium
    var selectedSensor = [UIButton()]

    override init(frame: CGRect) {
        super.init(frame: frame)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        applyBlurEffect()
        createLogDataButton()
        createBluetoothIcon()
        createCameraIcon()
        createCancelButton()
        resolutionTitle()
        createResolutions()
        createModelResolutionTitle()
        _ = createLabels(value: "server", leadingAnchor: 10, topAnchor: 250, labelWidth: 240, labelHeight: 40)
        createServer()
        createSecondView()
        createSecondViewLabel(value: "Images", leadingAnchor: 10, topAnchor: 10, labelWidth: 100, labelHeight: 40)
        createImagesButton()
        createSecondViewLabel(value: Strings.sensorData, leadingAnchor: 10, topAnchor: 90, labelWidth: 200, labelHeight: 40)
        createSensorButtons()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    func createLogDataButton() {
        logData.frame.size = CGSize(width: 100, height: 100)
        _ = createLabels(value: Strings.logData, leadingAnchor: 10, topAnchor: 13, labelWidth: 100, labelHeight: 40)
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

    func resolutionTitle() {
        previewResolution = createLabels(value: Strings.previewResolutionMedium, leadingAnchor: 10, topAnchor: 50, labelWidth: 240, labelHeight: 30)
    }


    func createResolutions() {
        low = createButton(borderColor: "red", buttonName: Strings.low, leadingAnchor: 10, topAnchor: 80, action: #selector(applyLowResolution(_:)))
        medium = createButton(borderColor: "red", buttonName: Strings.medium, leadingAnchor: 100, topAnchor: 80, action: #selector(applyMediumResolution(_:)))
        high = createButton(borderColor: "red", buttonName: Strings.high, leadingAnchor: 190, topAnchor: 80, action: #selector(applyHighResolution(_:)))
        updateResolution()
    }

    func createModelResolutionTitle() {
        modelResolution = createLabels(value: Strings.modelResolution, leadingAnchor: 10, topAnchor: 130, labelWidth: 240, labelHeight: 30)
    }

    func createServer() {
        server = createButton(borderColor: "red", buttonName: Strings.server, leadingAnchor: 10, topAnchor: 280, action: #selector(serverHandler(_:)))
    }


    func createSecondView() {
        secondView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(secondView)
        if currentOrientation == .portrait {
            leadingConstraint = secondView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 0)
            topConstraint = secondView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 320);
            widthConstraint = secondView.widthAnchor.constraint(equalToConstant: width)
            heightConstraint = secondView.heightAnchor.constraint(equalToConstant: height / 2)
        } else {
            leadingConstraint = secondView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: height / 2)
            leadingConstraint.identifier = Strings.expendSetting;
            topConstraint = secondView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 30);
            topConstraint.identifier = Strings.expendSetting
            widthConstraint = secondView.widthAnchor.constraint(equalToConstant: height / 2)
            widthConstraint.identifier = Strings.expendSetting
            heightConstraint = secondView.heightAnchor.constraint(equalToConstant: width)
            heightConstraint.identifier = Strings.expendSetting
        }

        leadingConstraint.identifier = Strings.expendSetting;
        topConstraint.identifier = Strings.expendSetting
        widthConstraint.identifier = Strings.expendSetting
        heightConstraint.identifier = Strings.expendSetting
        NSLayoutConstraint.activate([
            leadingConstraint, topConstraint, widthConstraint, heightConstraint
        ])

    }

    func refreshConstraints() {

        if currentOrientation == .portrait {
            leadingConstraint.constant = 0
            topConstraint.constant = 320
            widthConstraint.constant = width
            heightConstraint.constant = height / 2
        } else {
            leadingConstraint.constant = height / 2
            topConstraint.constant = 30
            widthConstraint.constant = height / 2
            heightConstraint.constant = width
        }
    }


    func createImagesButton() {
        preview = createSecondViewButton( buttonName: Strings.preview, leadingAnchor: 10, topAnchor: 40, action: #selector(applyPreview(_:)))
        training = createSecondViewButton( buttonName: Strings.training, leadingAnchor: 100, topAnchor: 40, action: #selector(applyTraining(_:)))
        updateImageMode()
    }

    func createSensorButtons() {
        vehicle = createSecondViewButton( buttonName: Strings.vehicle, leadingAnchor: 10, topAnchor: 120, action: #selector(updateSensor(_:)))
        gps = createSecondViewButton( buttonName: Strings.gps, leadingAnchor: 100, topAnchor: 120, action: #selector(updateSensor(_:)))
        acceleration = createSecondViewButton( buttonName: Strings.accelerometer, leadingAnchor: 190, topAnchor: 120, action: #selector(updateSensor(_:)))
        magnetic = createSecondViewButton( buttonName: Strings.magnetic, leadingAnchor: 280, topAnchor: 120, action: #selector(updateSensor(_:)))
        temperature = createSecondViewButton( buttonName: Strings.temperature, leadingAnchor: 10, topAnchor: 170, action: #selector(updateSensor(_:)))
        gyroscope = createSecondViewButton( buttonName: Strings.gyroscope, leadingAnchor: 100, topAnchor: 170, action: #selector(updateSensor(_:)))
        proximity = createSecondViewButton(buttonName: "Proximity", leadingAnchor: 190, topAnchor: 170, action: #selector(updateSensor(_:)))
        light = createSecondViewButton( buttonName: "Light", leadingAnchor: 10, topAnchor: 220, action: #selector(updateSensor(_:)))
        gravity = createSecondViewButton( buttonName: "Gravity", leadingAnchor: 100, topAnchor: 220, action: #selector(updateSensor(_:)))
        motion = createSecondViewButton(buttonName: "Motion", leadingAnchor: 190, topAnchor: 220, action: #selector(updateSensor(_:)))


    }

    func createButton(borderColor: String, buttonName: String, leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector?) -> UIButton {
        let btn = UIButton()
        btn.layer.cornerRadius = 10
        btn.backgroundColor = Colors.freeRoamButtonsColor
        let text = UILabel()
        text.text = buttonName
        text.textColor = Colors.borderColor
        if let action = action {
            btn.addTarget(self, action: action, for: .touchUpInside)
        }
        addSubview(btn)
        btn.translatesAutoresizingMaskIntoConstraints = false
        btn.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true;
        btn.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: leadingAnchor).isActive = true
        btn.widthAnchor.constraint(equalToConstant: 80).isActive = true
        btn.heightAnchor.constraint(equalToConstant: 40).isActive = true
        text.frame = CGRect(x: 10, y: 0, width: 70, height: 40)
        btn.addSubview(text)
        return btn
    }

    func createLabels(value: String, leadingAnchor: CGFloat, topAnchor: CGFloat, labelWidth: CGFloat, labelHeight: CGFloat) -> UILabel {
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
        return label
    }

    func createSecondViewLabel(value: String, leadingAnchor: CGFloat, topAnchor: CGFloat, labelWidth: CGFloat, labelHeight: CGFloat) {
        let label = UILabel()
        label.text = value
        label.font = UIFont(name: "medium", size: adapted(dimensionSize: 30, to: .width))
        label.frame.size = CGSize(width: labelWidth, height: labelHeight)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.textColor = .white
        label.font = label.font.withSize(15)
        secondView.addSubview(label)
        label.topAnchor.constraint(equalTo: secondView.topAnchor, constant: topAnchor).isActive = true
        label.leadingAnchor.constraint(equalTo: secondView.leadingAnchor, constant: leadingAnchor).isActive = true
    }

    func createSecondViewButton( buttonName: String, leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector?) -> UIButton {
        let btn = UIButton()
        btn.layer.cornerRadius = 10
        btn.backgroundColor = Colors.freeRoamButtonsColor
        let text = UILabel()
        text.text = buttonName
        text.textColor = Colors.borderColor
        if let action = action {
            btn.addTarget(self, action: action, for: .touchUpInside)
        }
        secondView.addSubview(btn)
        btn.layer.borderWidth = 2
        btn.layer.borderColor = Colors.freeRoamButtonsColor?.cgColor
        btn.translatesAutoresizingMaskIntoConstraints = false
        btn.topAnchor.constraint(equalTo: secondView.topAnchor, constant: topAnchor).isActive = true;
        btn.leadingAnchor.constraint(equalTo: secondView.leadingAnchor, constant: leadingAnchor).isActive = true
        btn.widthAnchor.constraint(equalToConstant: 80).isActive = true
        btn.heightAnchor.constraint(equalToConstant: 40).isActive = true
        text.frame = CGRect(x: 10, y: 0, width: 70, height: 40)
        btn.addSubview(text)
        return btn
    }


    @objc func cancelExpandedView() {
        NotificationCenter.default.post(name: .cancelButton, object: nil)
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
        NotificationCenter.default.post(name: .logData, object: nil)
    }

    @objc func applyLowResolution(_ sender: UIView) {
        selectedResolution = Resolutions.low
        updateResolution()
        previewResolution.text = Strings.previewResolutionLow


    }

    @objc func applyMediumResolution(_ sender: UIView) {
        selectedResolution = Resolutions.medium
        updateResolution()
        previewResolution.text = Strings.previewResolutionMedium


    }

    @objc func applyHighResolution(_ sender: UIView) {
        selectedResolution = Resolutions.high
        updateResolution()
        previewResolution.text = Strings.previewResolutionHigh


    }

    @objc func serverHandler(_ sender: UIView) {
        print("hello server")
    }

    @objc func updateResolution() {

        switch (selectedResolution) {

        case .low:
            low.backgroundColor = Colors.title
            medium.backgroundColor = Colors.freeRoamButtonsColor
            high.backgroundColor = Colors.freeRoamButtonsColor
        case .medium:
            low.backgroundColor = Colors.freeRoamButtonsColor
            medium.backgroundColor = Colors.title
            high.backgroundColor = Colors.freeRoamButtonsColor
        case .high:
            low.backgroundColor = Colors.freeRoamButtonsColor
            medium.backgroundColor = Colors.freeRoamButtonsColor
            high.backgroundColor = Colors.title
        }

    }

    @objc func applyPreview(_ sender: UIView) {
        selectedImages = ImagesMode.preview
        updateImageMode()
    }

    @objc func applyTraining(_ sender: UIView) {
        selectedImages = ImagesMode.training
        updateImageMode()
    }

    func updateImageMode() {
        switch (selectedImages) {
        case .preview:
            preview.layer.borderColor = Colors.title?.cgColor
            training.layer.borderColor = Colors.freeRoamButtonsColor?.cgColor
        case .training:
            preview.layer.borderColor = Colors.freeRoamButtonsColor?.cgColor
            training.layer.borderColor = Colors.title?.cgColor
        }
    }

    @objc func updateSensor(_ sender: UIButton) {

        var index = 0
        var isFound = false
        for sensor in selectedSensor {
            if sensor == sender {
                sender.layer.borderColor = Colors.freeRoamButtonsColor?.cgColor
                selectedSensor.remove(at: index)
                isFound = true
                break;
            }
            index = index + 1
        }
        if !isFound {
            selectedSensor.append(sender)
            sender.layer.borderColor = Colors.title?.cgColor

        }
    }
}

extension Notification.Name {
    static let cancelButton = Notification.Name(Strings.cancelButton)
    static let logData = Notification.Name(Strings.logDataNotify)

}