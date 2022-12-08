//
// Created by Nitish Yadav on 16/09/22.
//

import Foundation
import UIKit
import DropDown

class expandSetting: UIView, UITextFieldDelegate, UIScrollViewDelegate {
    let logData = UISwitch()
    let bluetoothIcon = UIImageView()
    let delayTextField = UITextField()
    let cameraIcon = UIImageView()
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
    var gyroscope = UIButton()
    var selectedResolution: Resolutions = Resolutions.medium
    var sensorButtons = [UIButton]()
    var dropDownView = UIView()
    var ddView = UIView()
    let dropDown = DropDown()
    var dropdownLabel = UILabel()
    var dropdownTopAnchor: NSLayoutConstraint!
    var modelsName = [String]()
    var resolution = [String]()
    var models: [Model] = [];
    var leftSpeedLabel = UILabel()

    override init(frame: CGRect) {
        super.init(frame: frame)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        loadModelsNameAndResolution()
        applyBlurEffect()
        createLogDataButton()
        createBluetoothIcon()
        createCameraIcon()
        resolutionTitle()
        createResolutions()
        createModelResolutionTitle()
        createDropdown()
        createLabels(value: Strings.server, leadingAnchor: 10, topAnchor: 200, labelWidth: 240, labelHeight: 40).font = UIFont.boldSystemFont(ofSize: 16.0)
        createServer()
        createSecondView()
        createSecondViewLabel(value: "Images", leadingAnchor: 10, topAnchor: 10, labelWidth: 100, labelHeight: 40).font = UIFont.boldSystemFont(ofSize: 16.0)
        createImagesButton()
        createSecondViewLabel(value: Strings.sensorData, leadingAnchor: 10, topAnchor: 90, labelWidth: 200, labelHeight: 40).font = UIFont.boldSystemFont(ofSize: 16.0)
        createSensorButtons()
        _ = createSecondViewLabel(value: Strings.delay, leadingAnchor: 230, topAnchor: 190, labelWidth: 60, labelHeight: 40)
        createDelayField()
        let vehicleControls = VehicleControl();
        createLeftSpeed()
        addSubview(vehicleControls)
        vehicleControls.translatesAutoresizingMaskIntoConstraints = false
        vehicleControls.topAnchor.constraint(equalTo: magnetic.safeAreaLayoutGuide.bottomAnchor, constant: adapted(dimensionSize: 13, to: .height)).isActive = true;
        vehicleControls.leadingAnchor.constraint(equalTo: secondView.safeAreaLayoutGuide.leadingAnchor, constant: 0).isActive = true
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothConnected, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothDisconnected, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateSpeedLabel), name: .updateSpeedLabel, object: nil)

        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard(_:)))
        addGestureRecognizer(tap)
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }


    func createLogDataButton() {
        _ = createLabels(value: Strings.logData, leadingAnchor: 10, topAnchor: 13, labelWidth: 100, labelHeight: 40)
        logData.isOn = false
        logData.setOn(false, animated: true)
        logData.onTintColor = Colors.title
        logData.addTarget(self, action: #selector(switchLogButton(_:)), for: .valueChanged)
        logData.translatesAutoresizingMaskIntoConstraints = false
        addSubview(logData)
        logData.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 90).isActive = true
        logData.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true
    }

    func createBluetoothIcon() {
        bluetoothIcon.frame.size = CGSize(width: 30, height: 30)
        bluetoothIcon.translatesAutoresizingMaskIntoConstraints = false
        if isBluetoothConnected {
            bluetoothIcon.image = Images.bluetoothConnected
        } else {
            bluetoothIcon.image = Images.bluetoothDisconnected
        }
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(ble(_:)))
        bluetoothIcon.isUserInteractionEnabled = true
        bluetoothIcon.addGestureRecognizer(tapGesture)
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

    func resolutionTitle() {
        previewResolution = createLabels(value: Strings.previewResolutionMedium, leadingAnchor: 10, topAnchor: 50, labelWidth: 240, labelHeight: 30)
        previewResolution.font = UIFont.boldSystemFont(ofSize: 16.0)
    }


    func createResolutions() {
        low = createButton(borderColor: "red", buttonName: Strings.low, leadingAnchor: 10, topAnchor: 80, action: #selector(applyLowResolution(_:)))
        medium = createButton(borderColor: "red", buttonName: Strings.medium, leadingAnchor: 100, topAnchor: 80, action: #selector(applyMediumResolution(_:)))
        high = createButton(borderColor: "red", buttonName: Strings.high, leadingAnchor: 190, topAnchor: 80, action: #selector(applyHighResolution(_:)))
        updateResolution()
    }

    func createModelResolutionTitle() {
        modelResolution = createLabels(value: Strings.modelResolution + "256x96", leadingAnchor: 10, topAnchor: 130, labelWidth: 240, labelHeight: 30)
        modelResolution.font = UIFont.boldSystemFont(ofSize: 16.0);
    }

    func createDropdown() {
        modelsName = Common.loadAllModelsName()
        dropDown.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
            dropDown.textColor = color
        }
        dropDown.anchorView = dropDownView
        dropDown.dataSource = modelsName
        ddView = createDropdownView(borderColor: "", buttonName: "CLI-Mobile", leadingAnchor: 10, topAnchor: 155, action: #selector(showDropdown(_:)))
        ddView.backgroundColor = Colors.title
        let upwardImage = UIImageView(image: Images.upwardArrow);
        ddView.addSubview(upwardImage);
        upwardImage.frame.size = CGSize(width: 20, height: 20);
        upwardImage.translatesAutoresizingMaskIntoConstraints = false;
        upwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true;
        upwardImage.topAnchor.constraint(equalTo: ddView.safeAreaLayoutGuide.topAnchor, constant: 15).isActive = true;
        dropDown.selectionAction = { [unowned self] (index: Int, item: String) in
            dropdownLabel.text = item
            modelResolution.text = Strings.modelResolution + resolution[index]
            NotificationCenter.default.post(name: .updateModelResolution, object: resolution[index])
        }
        dropDown.width = 210
        dropDown.cornerRadius = 5
        dropDownView.frame.size = CGSize(width: 200, height: 100);
        dropDownView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(dropDownView)
        dropdownTopAnchor = dropDownView.topAnchor.constraint(equalTo: ddView.safeAreaLayoutGuide.bottomAnchor, constant: 0)
        dropdownTopAnchor.isActive = true
        dropDownView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 10).isActive = true;
    }

    func createServer() {
        server = createButton(borderColor: "red", buttonName: Strings.server, leadingAnchor: 10, topAnchor: 230, action: #selector(serverHandler(_:)))

    }

    func createSecondView() {
        secondView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(secondView)
        if currentOrientation == .portrait {
            leadingConstraint = secondView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 0)
            topConstraint = secondView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 290);
            widthConstraint = secondView.widthAnchor.constraint(equalToConstant: width)
            heightConstraint = secondView.heightAnchor.constraint(equalToConstant: height / 2)
        } else {
            leadingConstraint = secondView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: height / 2-50)
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
            frame.size.width = width
            leadingConstraint.constant = 0
            topConstraint.constant = 290
            widthConstraint.constant = width
            heightConstraint.constant = height / 2
            dropdownTopAnchor.constant = 0;
        } else {
            frame.size.width = height
            leadingConstraint.constant = height / 2-50
            topConstraint.constant = 30
            widthConstraint.constant = height / 2
            heightConstraint.constant = width;
            dropdownTopAnchor.constant = 150;

        }
    }


    func createImagesButton() {
        preview = createSecondViewButton(buttonName: Strings.preview, leadingAnchor: 10, topAnchor: 40, buttonWidth: 120, action: #selector(applyPreview(_:)), borderColor: Colors.freeRoamButtonsColor!.cgColor)
        preview.tag = 0
        training = createSecondViewButton(buttonName: Strings.training, leadingAnchor: 140, topAnchor: 40, buttonWidth: 120, action: #selector(applyTraining(_:)), borderColor: Colors.title!.cgColor)
        training.tag = 1
        setupImageMode()
    }

    func createSensorButtons() {
        vehicle = createSecondViewButton(buttonName: Strings.vehicle, leadingAnchor: 10, topAnchor: 120, buttonWidth: 80, action: #selector(updateSensor(_:)), borderColor: Colors.title!.cgColor)
        vehicle.tag = 1;
        sensorButtons.append(vehicle)
        gps = createSecondViewButton(buttonName: Strings.gps, leadingAnchor: 100, topAnchor: 120, buttonWidth: 80, action: #selector(updateSensor(_:)), borderColor: Colors.title!.cgColor)
        gps.tag = 2;
        sensorButtons.append(gps)
        acceleration = createSecondViewButton(buttonName: Strings.accelerometer, leadingAnchor: 190, topAnchor: 120, buttonWidth: 140, action: #selector(updateSensor(_:)), borderColor: Colors.title!.cgColor)
        acceleration.tag = 3
        sensorButtons.append(acceleration)
        magnetic = createSecondViewButton(buttonName: Strings.magnetic, leadingAnchor: 10, topAnchor: 170, buttonWidth: 100, action: #selector(updateSensor(_:)), borderColor: Colors.title!.cgColor)
        magnetic.tag = 4
        sensorButtons.append(magnetic)
        gyroscope = createSecondViewButton(buttonName: Strings.gyroscope, leadingAnchor: 120, topAnchor: 170, buttonWidth: 100, action: #selector(updateSensor(_:)), borderColor: Colors.title!.cgColor)
        gyroscope.tag = 5
        sensorButtons.append(gyroscope)
    }

    func createDelayField() {

        let delayTextField = UITextField(frame: CGRect(x: 310, y: 177, width: 50, height: 40))
        delayTextField.placeholder = "200"
        delayTextField.font = UIFont.systemFont(ofSize: 15)
        delayTextField.borderStyle = UITextField.BorderStyle.roundedRect
        delayTextField.autocorrectionType = UITextAutocorrectionType.no
        delayTextField.keyboardType = UIKeyboardType.phonePad
        delayTextField.returnKeyType = UIReturnKeyType.default
        delayTextField.clearButtonMode = UITextField.ViewMode.whileEditing
        delayTextField.contentVerticalAlignment = UIControl.ContentVerticalAlignment.center
        delayTextField.delegate = self
        delayTextField.addTarget(self, action: #selector(delayFieldDidChange), for: .editingChanged)
        let bottomLine = CALayer()
        bottomLine.frame = CGRect(x: 0.0, y: delayTextField.frame.height - 10, width: delayTextField.frame.width, height: 1.0)
        bottomLine.backgroundColor = UIColor.white.cgColor
        delayTextField.borderStyle = UITextField.BorderStyle.none
        delayTextField.layer.addSublayer(bottomLine)
        secondView.addSubview(delayTextField)
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

    func createDropdownView(borderColor: String, buttonName: String, leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector?) -> UIView {
        let dd = UIView()
        dd.layer.cornerRadius = 10
        dd.backgroundColor = Colors.freeRoamButtonsColor
        dropdownLabel.text = buttonName
        dropdownLabel.textColor = Colors.borderColor
        let tap = UITapGestureRecognizer(target: self, action: #selector(showDropdown(_:)))
        dd.addGestureRecognizer(tap)
        addSubview(dd)
        dd.translatesAutoresizingMaskIntoConstraints = false
        dd.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true;
        dd.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: leadingAnchor).isActive = true
        dd.widthAnchor.constraint(equalToConstant: 210).isActive = true
        dd.heightAnchor.constraint(equalToConstant: 40).isActive = true
        dropdownLabel.frame = CGRect(x: 10, y: 0, width: 210, height: 40)
        dd.addSubview(dropdownLabel)
        return dd
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

    func createSecondViewLabel(value: String, leadingAnchor: CGFloat, topAnchor: CGFloat, labelWidth: CGFloat, labelHeight: CGFloat) -> UILabel {
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
        return label
    }

    func createSecondViewButton(buttonName: String, leadingAnchor: CGFloat, topAnchor: CGFloat, buttonWidth: CGFloat, action: Selector?, borderColor: CGColor) -> UIButton {
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
        btn.layer.borderColor = borderColor
        btn.translatesAutoresizingMaskIntoConstraints = false
        btn.topAnchor.constraint(equalTo: secondView.topAnchor, constant: topAnchor).isActive = true;
        btn.leadingAnchor.constraint(equalTo: secondView.leadingAnchor, constant: leadingAnchor).isActive = true
        btn.widthAnchor.constraint(equalToConstant: buttonWidth).isActive = true
        btn.heightAnchor.constraint(equalToConstant: 40).isActive = true
        text.frame = CGRect(x: 10, y: 0, width: buttonWidth, height: 40)
        btn.addSubview(text)
        return btn
    }

    func createLeftSpeed() {
        leftSpeedLabel.frame.size = CGSize(width: 100, height: 40);
        leftSpeedLabel.frame.origin = CGPoint(x: 10, y: 240)
        leftSpeedLabel.text = "xxx,xxx"
        secondView.addSubview(leftSpeedLabel)
        leftSpeedLabel.font = leftSpeedLabel.font.withSize(13.5)
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
        if sender.isOn {
            for sensor in sensorButtons {
                sensor.isEnabled = false
            }
        } else {
            for sensor in sensorButtons {
                sensor.isEnabled = true
            }
        }
    }

    @objc func applyLowResolution(_ sender: UIView) {
        selectedResolution = Resolutions.low
        NotificationCenter.default.post(name: .updateResolution, object: selectedResolution)
        updateResolution()
        previewResolution.text = Strings.previewResolutionLow


    }

    @objc func applyMediumResolution(_ sender: UIView) {
        selectedResolution = Resolutions.medium
        NotificationCenter.default.post(name: .updateResolution, object: selectedResolution)
        updateResolution()
        previewResolution.text = Strings.previewResolutionMedium


    }

    @objc func applyHighResolution(_ sender: UIView) {
        selectedResolution = Resolutions.high
        NotificationCenter.default.post(name: .updateResolution, object: selectedResolution)
        updateResolution()
        previewResolution.text = Strings.previewResolutionHigh


    }

    @objc func serverHandler(_ sender: UIView) {
//        print("hello server")
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
        NotificationCenter.default.post(name: .updatePreview, object: nil)
        let borderColor = (sender.layer.borderColor == Colors.title?.cgColor) ? Colors.freeRoamButtonsColor?.cgColor : Colors.title?.cgColor
        sender.layer.borderColor = borderColor
    }

    @objc func applyTraining(_ sender: UIView) {
        NotificationCenter.default.post(name: .updateTraining, object: nil)
        let borderColor = (training.layer.borderColor == Colors.freeRoamButtonsColor?.cgColor) ? Colors.title?.cgColor : Colors.freeRoamButtonsColor?.cgColor
        sender.layer.borderColor = borderColor

    }

    func setupImageMode() {

    }


    @objc func updateSensor(_ sender: UIButton) {
        NotificationCenter.default.post(name: .updateSensorsForLog, object: sender)
        updateBorderColor(sender: sender);
    }

    func updateBorderColor(sender: UIButton) {
        sender.layer.borderColor = (sender.layer.borderColor == Colors.freeRoamButtonsColor?.cgColor) ? Colors.title?.cgColor : Colors.freeRoamButtonsColor?.cgColor
    }

    @objc func delayFieldDidChange(_ sender: UITextField) {
        print(sender.text as Any)
    }

    @objc func showDropdown(_ sender: UIButton) {
        dropDown.show()
    }

    func loadModelsNameAndResolution() {
        modelsName = Common.loadAllModelsName();
        for model in Common.loadAllModelItems() {
            resolution.append(model.inputSize);
        }
    }


    @objc func ble(_ sender: UIView) {
        NotificationCenter.default.post(name: .ble, object: nil)
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        delayTextField.resignFirstResponder()  //if desired
        return true
    }

    @objc func updateConnect(_ notification: Notification) {
        if (isBluetoothConnected) {
            bluetoothIcon.image = Images.bluetoothConnected
        } else {
            bluetoothIcon.image = Images.bluetoothDisconnected
        }
    }

    @objc func dismissKeyboard(_ sender: UIButton) {
        endEditing(true);
    }

    @objc func keyboardWillShow(_ notification: Notification) {
        if currentOrientation == .portrait{
            topConstraint.constant = 260
        }
       else{
           topConstraint.constant = 10
       }
    }

    @objc func keyboardWillHide(_ notification: Notification) {
        if currentOrientation == .portrait{
            topConstraint.constant = 290
        }
        else{
            topConstraint.constant = 30
        }
    }

    @objc func updateSpeedLabel(_ notification: Notification) {
        leftSpeedLabel.text = notification.object as! String
    }

}



