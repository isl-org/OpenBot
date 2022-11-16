//
// Created by Sparsh Jain on 28/10/22.
//

import Foundation
import UIKit

class ObjectTrackingSettings: UIView {
    let autoModeButton = UISwitch()
    var speedLabel = UILabel()
    var modelDropdownLabel = UILabel()
    var imageInputLabel = UILabel()
    var threadLabel = UILabel()
    var confidenceLabel = UILabel()
    var deviceDropDownLabel = UILabel()
    var objectDropDownLabel = UILabel()
    var detector: Detector?;
    init(frame: CGRect, detector: Detector?) {
        self.detector = detector;
        super.init(frame: frame);
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeDown.direction = .down
        addGestureRecognizer(swipeDown)
        let swipeUp = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeUp.direction = .up
        addGestureRecognizer(swipeUp)
        createBar()
        addSubview(createLabel(text: Strings.autoMode, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 15, to: .height))));
        createBluetoothIcon()
        createCameraIcon()
        createSwitchButton()
        addSubview(createLabel(text: Strings.model, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 60, to: .height))))
        createModelDropDown()
        addSubview(createLabel(text: Strings.speed, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 90, to: .height))))
        setupSpeed()
        addSubview(createLabel(text: Strings.input, leadingAnchor: 180, topAnchor: Int(adapted(dimensionSize: 90, to: .height))))
        setupInput();
        addSubview(createLabel(text: Strings.object, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 120, to: .height))))
        setupObjectDropDown()
        addSubview(createLabel(text: Strings.confidence, leadingAnchor: 180, topAnchor: Int(adapted(dimensionSize: 120, to: .height))))
        setupConfidence()
        addSubview(createLabel(text: Strings.device, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 150, to: Dimension.height))))
        createDeviceDropDown()
        addSubview(createLabel(text: Strings.threads, leadingAnchor: 180, topAnchor: Int(adapted(dimensionSize: 150, to: .height))))
        setupThreads();
        setupVehicleControls();
        NotificationCenter.default.addObserver(self, selector: #selector(updateModel), name: .updateModel, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateObject), name: .updateObject, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDevice), name: .updateDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateThreadLabel), name: .updateThreadLabel, object: nil)

    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    func createSwitchButton() {
        autoModeButton.isOn = false
        autoModeButton.setOn(false, animated: true)
        autoModeButton.onTintColor = Colors.title
        autoModeButton.addTarget(self, action: #selector(switchButton(_:)), for: .valueChanged)
        autoModeButton.translatesAutoresizingMaskIntoConstraints = false
        addSubview(autoModeButton)
        autoModeButton.widthAnchor.constraint(equalToConstant: 40).isActive = true
        autoModeButton.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 120).isActive = true
        autoModeButton.topAnchor.constraint(equalTo: topAnchor, constant: 25).isActive = true
    }

    func createLabel(text: String, leadingAnchor: Int, topAnchor: Int) -> UILabel {
        let label = UILabel()
        label.text = text
        label.textColor = Colors.borderColor
        label.frame.origin = CGPoint(x: leadingAnchor, y: topAnchor)
        label.frame.size = resized(size: CGSize(width: text.count * 10, height: 40), basedOn: .height)
        return label
    }

    @objc func switchButton(_ sender: UISwitch) {
        NotificationCenter.default.post(name: .autoModeObjectTracking, object: nil)
    }

    @objc func respondToSwipeGesture(gesture: UIGestureRecognizer) {
        if let swipeGesture = gesture as? UISwipeGestureRecognizer {
            switch swipeGesture.direction {
            case .down:
                UIView.animate(withDuration: 0.25) {
                    if currentOrientation == .portrait {
                        self.frame.origin.y = height - adapted(dimensionSize: 50, to: .height)
                    } else {
                        self.frame.origin.y = width - adapted(dimensionSize: 50, to: .height)
                    }
                }
            case .up:
                UIView.animate(withDuration: 0.25) {
                    if currentOrientation == .portrait {
                        self.frame.origin.y = height - width;
                    } else {
                        self.frame.origin.y = adapted(dimensionSize: 20, to: .height)
                    }
                }
            default:
                break
            }
        }
    }

    func createBar() {
        let bar = UIView()
        bar.backgroundColor = Colors.title
        addSubview(bar)
        bar.translatesAutoresizingMaskIntoConstraints = false
        bar.widthAnchor.constraint(equalToConstant: adapted(dimensionSize: 50, to: .height)).isActive = true
        bar.heightAnchor.constraint(equalToConstant: 5).isActive = true
        bar.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: width / 2 - 30).isActive = true
        bar.topAnchor.constraint(equalTo: topAnchor, constant: 10).isActive = true
        bar.layer.cornerRadius = 2
    }


    func createBluetoothIcon() {
        if (isBluetoothConnected) {
            createIcons(iconImg: Images.bluetoothConnected!, topAnchor: 10, trailingAnchor: -adapted(dimensionSize: 60, to: .height), x: 24.5, y: 21, size: resized(size: Images.bluetoothConnected!.size, basedOn: Dimension.width), backgroundColor: Colors.title ?? .blue, action: #selector(ble(_:)))
        } else {
            createIcons(iconImg: Images.bluetoothDisconnected!, topAnchor: 10, trailingAnchor: -adapted(dimensionSize: 60, to: .height), x: 24.5, y: 21, size: resized(size: Images.bluetoothDisconnected!.size, basedOn: Dimension.width), backgroundColor: Colors.title ?? .blue, action: #selector(ble(_:)))
        }
    }

    func createCameraIcon() {
        if let image = Images.frontCamera {
            createIcons(iconImg: image, topAnchor: 13, trailingAnchor: -20, x: 16.5, y: 17.5, size: resized(size: image.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(switchCamera(_:)))
        }
    }

    @objc func ble(_ sender: UIView) {
        NotificationCenter.default.post(name: .ble, object: nil)
    }

    @objc func switchCamera(_ sender: UIView) {
        NotificationCenter.default.post(name: .switchCamera, object: nil)
    }


    func createIcons(iconImg: UIImage, topAnchor: CGFloat, trailingAnchor: CGFloat, x: CGFloat, y: CGFloat, size: CGSize, backgroundColor: UIColor, action: Selector?) {
        let icon = UIView()
        let iconImage = UIImageView(frame: CGRect(x: x, y: y, width: size.width, height: size.height))
        iconImage.image = iconImg
        icon.addSubview(iconImage)
        addSubview(icon)
        icon.translatesAutoresizingMaskIntoConstraints = false
        icon.widthAnchor.constraint(equalToConstant: 40).isActive = true
        icon.heightAnchor.constraint(equalToConstant: 40).isActive = true
        icon.topAnchor.constraint(equalTo: self.topAnchor, constant: topAnchor).isActive = true
        icon.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: trailingAnchor).isActive = true
        icon.layer.cornerRadius = 30
        let tapGesture = UITapGestureRecognizer(target: self, action: action)
        icon.addGestureRecognizer(tapGesture)
    }

    func setupInput() {
        imageInputLabel.frame = CGRect(x: width - 80, y: adapted(dimensionSize: 90, to: .height), width: 100, height: 40)
        addSubview(imageInputLabel)
    }

    func setupSpeed() {
        speedLabel = createLabel(text: "*** fps", leadingAnchor: 90, topAnchor: Int(adapted(dimensionSize: 90, to: .height)))
        addSubview(speedLabel)
    }

    func setupObjectDropDown() {
        let object = ObjectClassDropdown(frame: CGRect(x: 91, y: 100, width: 40, height: 205), selectedObject: "Car", detector: detector!);
        addSubview(object)
        let dd = UIView()
        dd.backgroundColor = Colors.freeRoamButtonsColor
        objectDropDownLabel.text = "person"
        objectDropDownLabel.textColor = Colors.borderColor
        let tap = UITapGestureRecognizer(target: self, action: #selector(showObjectDropdown(_:)))
        objectDropDownLabel.addGestureRecognizer(tap)
        dd.addGestureRecognizer(tap)
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        dd.addSubview(upwardImage)
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: dd.trailingAnchor, constant: -20).isActive = true
        upwardImage.topAnchor.constraint(equalTo: dd.topAnchor, constant: 11.5).isActive = true
        addSubview(dd)
        dd.translatesAutoresizingMaskIntoConstraints = false
        dd.topAnchor.constraint(equalTo: topAnchor, constant: adapted(dimensionSize: 120, to: .height)).isActive = true;
        dd.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 80).isActive = true
        dd.widthAnchor.constraint(equalToConstant: 100).isActive = true
        dd.heightAnchor.constraint(equalToConstant: 30).isActive = true
        objectDropDownLabel.frame = CGRect(x: 10, y: 0, width: 80, height: 40)
        dd.addSubview(objectDropDownLabel)
    }

    func setupConfidence() {
        let plusImageView = UIView();
        plusImageView.frame.size = CGSize(width: 30, height: 30);
        plusImageView.frame.origin = CGPoint(x: width - 40, y: adapted(dimensionSize: 120, to: .height));
        addSubview(plusImageView)
        let plusImage = UIImageView();
        plusImage.image = Images.plus
        plusImage.frame.size = CGSize(width: 20, height: 20);
        plusImage.isUserInteractionEnabled = true;
        let tapOnPlus = UITapGestureRecognizer(target: self, action: #selector(increaseConfidence(_:)))
        plusImageView.addGestureRecognizer(tapOnPlus)
        plusImageView.addSubview(plusImage)
        plusImage.translatesAutoresizingMaskIntoConstraints = false
        plusImage.leadingAnchor.constraint(equalTo: plusImageView.leadingAnchor, constant: 5.5).isActive = true
        plusImage.topAnchor.constraint(equalTo: plusImageView.topAnchor, constant: 10).isActive = true
        //setting minus
        let minusImageView = UIView()
        minusImageView.frame.size = CGSize(width: 30, height: 30);
        minusImageView.frame.origin = CGPoint(x: width - 100, y: adapted(dimensionSize: 120, to: .height))
        addSubview(minusImageView)
        let minusImage = UIImageView()
        minusImage.image = Images.minus
        minusImage.frame.size = CGSize(width: 30, height: 30);
        minusImage.isUserInteractionEnabled = true;
        minusImageView.addSubview(minusImage)
        let tapOnMinus = UITapGestureRecognizer(target: self, action: #selector(decreaseConfidence(_:)))
        minusImageView.addGestureRecognizer(tapOnMinus)
        minusImage.translatesAutoresizingMaskIntoConstraints = false
        minusImage.leadingAnchor.constraint(equalTo: minusImageView.leadingAnchor, constant: 5.5).isActive = true
        minusImage.topAnchor.constraint(equalTo: minusImageView.topAnchor, constant: 10).isActive = true
        //thread Label
        confidenceLabel.frame.size = CGSize(width: 10, height: 40);
        addSubview(confidenceLabel);
        confidenceLabel.translatesAutoresizingMaskIntoConstraints = false
        confidenceLabel.text = "50%";
        confidenceLabel.textColor = Colors.borderColor
        confidenceLabel.translatesAutoresizingMaskIntoConstraints = false
        confidenceLabel.leadingAnchor.constraint(equalTo: minusImageView.trailingAnchor, constant: 0).isActive = true
        confidenceLabel.topAnchor.constraint(equalTo: minusImageView.topAnchor, constant: 8).isActive = true
    }

    func setupThreads() {
        //setting plus
        let plusImageView = UIView();
        plusImageView.frame.size = CGSize(width: 30, height: 30);
        plusImageView.frame.origin = CGPoint(x: width - 40, y: adapted(dimensionSize: 150, to: .height));
        addSubview(plusImageView)
        let plusImage = UIImageView();
        plusImage.image = Images.plus
        plusImage.frame.size = CGSize(width: 20, height: 20);
        plusImage.isUserInteractionEnabled = true;
        let tapOnPlus = UITapGestureRecognizer(target: self, action: #selector(increaseThreads(_:)))
        plusImageView.addGestureRecognizer(tapOnPlus)
        plusImageView.addSubview(plusImage)
        plusImage.translatesAutoresizingMaskIntoConstraints = false
        plusImage.leadingAnchor.constraint(equalTo: plusImageView.leadingAnchor, constant: 5.5).isActive = true
        plusImage.topAnchor.constraint(equalTo: plusImageView.topAnchor, constant: 10).isActive = true
        //setting minus
        let minusImageView = UIView()
        minusImageView.frame.size = CGSize(width: 30, height: 30);
        minusImageView.frame.origin = CGPoint(x: width - 100, y: adapted(dimensionSize: 150, to: .height))
        addSubview(minusImageView)
        let minusImage = UIImageView()
        minusImage.image = Images.minus
        minusImage.frame.size = CGSize(width: 30, height: 30);
        minusImage.isUserInteractionEnabled = true;
        minusImageView.addSubview(minusImage)
        let tapOnMinus = UITapGestureRecognizer(target: self, action: #selector(decreaseThreads(_:)))
        minusImageView.addGestureRecognizer(tapOnMinus)
        minusImage.translatesAutoresizingMaskIntoConstraints = false
        minusImage.leadingAnchor.constraint(equalTo: minusImageView.leadingAnchor, constant: 5.5).isActive = true
        minusImage.topAnchor.constraint(equalTo: minusImageView.topAnchor, constant: 10).isActive = true
        //thread Label
        threadLabel.frame.size = CGSize(width: 10, height: 40);
        addSubview(threadLabel);
        threadLabel.translatesAutoresizingMaskIntoConstraints = false
        threadLabel.text = "1";
        threadLabel.textColor = Colors.borderColor
        threadLabel.translatesAutoresizingMaskIntoConstraints = false
        threadLabel.leadingAnchor.constraint(equalTo: minusImageView.trailingAnchor, constant: 8).isActive = true
        threadLabel.topAnchor.constraint(equalTo: minusImageView.topAnchor, constant: 8).isActive = true
    }

    @objc func increaseConfidence(_ sender: UIImage) {
        if confidenceLabel.text == "100%" {
            return
        }
        var value = Int(confidenceLabel.text?.prefix((confidenceLabel.text?.count ?? 1) - 1) ?? "50")
        print("value is : ", value)
        value = (value ?? 5) + 5;
        confidenceLabel.text = String(value!) + "%"
        NotificationCenter.default.post(name: .updateConfidence, object: confidenceLabel.text)
    }

    @objc func decreaseConfidence(_ sender: UIImage) {
        if confidenceLabel.text == "0%" {
            return
        }
        var value = Int(confidenceLabel.text?.prefix((confidenceLabel.text?.count ?? 1) - 1) ?? "50")
        value = (value ?? 5) - 5;
        confidenceLabel.text = String(value!) + "%"
        NotificationCenter.default.post(name: .updateConfidence, object: confidenceLabel.text)
    }

    @objc func increaseThreads(_ sender: UIImage) {
        if threadLabel.text == "9" || threadLabel.text == "N/A" {
            return
        }
        var value = Int(threadLabel.text ?? "1")
        value = (value ?? 1) + 1;
        threadLabel.text = String(value!)
        NotificationCenter.default.post(name: .updateThread, object: threadLabel.text)
    }

    @objc func decreaseThreads(_ sender: UIImage) {
        if threadLabel.text == "1" || threadLabel.text == "N/A" {
            return
        }
        var value = Int(threadLabel.text ?? "1")
        value = (value ?? 1) - 1;
        threadLabel.text = String(value!)
        NotificationCenter.default.post(name: .updateThread, object: threadLabel.text)
    }

    @objc func updateDevice(_ notification: Notification) {
        let selectedDevice = notification.object as! String
        deviceDropDownLabel.text = selectedDevice
    }

    @objc func updateThreadLabel(_ notification: Notification) {
        threadLabel.text = (notification.object as! String)

    }

    func setupVehicleControls() {
        let vehicleControls = VehicleControl();
        addSubview(vehicleControls)
        vehicleControls.translatesAutoresizingMaskIntoConstraints = false
        vehicleControls.topAnchor.constraint(equalTo: threadLabel.safeAreaLayoutGuide.bottomAnchor, constant: adapted(dimensionSize: 20, to: .height)).isActive = true;
        vehicleControls.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 7).isActive = true
    }


    func createDeviceDropDown() {
        let device = Devices(frame: CGRect(x: 80, y: adapted(dimensionSize: 150, to: .height), width: 100, height: 100));
        addSubview(device)
        print(device.deviceDD.frame)
        let dd = UIView()
        dd.layer.cornerRadius = 10
        dd.backgroundColor = Colors.freeRoamButtonsColor
        deviceDropDownLabel.text = RuntimeDevice.CPU.rawValue
        deviceDropDownLabel.textColor = Colors.borderColor
        let tap = UITapGestureRecognizer(target: self, action: #selector(showDeviceDropdown(_:)))
        dd.addGestureRecognizer(tap)
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        dd.addSubview(upwardImage)
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: dd.trailingAnchor, constant: -20).isActive = true
        upwardImage.topAnchor.constraint(equalTo: dd.topAnchor, constant: 11.5).isActive = true
        addSubview(dd)
        dd.translatesAutoresizingMaskIntoConstraints = false
        dd.topAnchor.constraint(equalTo: topAnchor, constant: adapted(dimensionSize: 150, to: .height)).isActive = true;
        dd.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 80).isActive = true
        dd.widthAnchor.constraint(equalToConstant: 100).isActive = true
        dd.heightAnchor.constraint(equalToConstant: 30).isActive = true
        deviceDropDownLabel.frame = CGRect(x: 10, y: 0, width: 210, height: 40)
        dd.addSubview(deviceDropDownLabel)
    }

    @objc func showDeviceDropdown(_ sender: UIButton) {
        NotificationCenter.default.post(name: .showDeviceDD, object: nil)
    }

    @objc func showObjectDropdown(_ sender: UIButton) {
        NotificationCenter.default.post(name: .showObjectDD, object: nil)
    }


    func createModelDropDown() {
        let selectedModels = Common.loadSelectedModels(mode: Constants.objectTrackingMode);
        let model = Models(frame: CGRect(x: 180, y: adapted(dimensionSize: 60, to: .height), width: 100, height: 200), selectedModels: selectedModels);
        addSubview(model)
        let dd = UIView()
        dd.layer.cornerRadius = 10
        dd.backgroundColor = Colors.freeRoamButtonsColor
        modelDropdownLabel.text = selectedModels.first
        modelDropdownLabel.textColor = Colors.borderColor
        let tap = UITapGestureRecognizer(target: self, action: #selector(showModelDropdown(_:)))
        dd.addGestureRecognizer(tap)
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        dd.addSubview(upwardImage)
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: dd.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: dd.topAnchor, constant: 11.5).isActive = true
        addSubview(dd)
        dd.translatesAutoresizingMaskIntoConstraints = false
        dd.topAnchor.constraint(equalTo: topAnchor, constant: adapted(dimensionSize: 60, to: .height)).isActive = true;
        dd.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 180).isActive = true
        dd.widthAnchor.constraint(equalToConstant: 180).isActive = true
        dd.heightAnchor.constraint(equalToConstant: 40).isActive = true
        modelDropdownLabel.frame = CGRect(x: 0, y: 0, width: 210, height: 40)
        dd.addSubview(modelDropdownLabel)
    }

    @objc func showModelDropdown(_ sender: UIButton) {
        NotificationCenter.default.post(name: .showModelsDD, object: nil)
    }

    @objc func updateModel(_ notification: Notification) {
        let selectedModel = notification.object as! String
        modelDropdownLabel.text = selectedModel
        let model = Common.loadSelectedModel(modeName: selectedModel)
        imageInputLabel.text = model.inputSize

    }

    @objc func updateObject(_ notification: Notification) {
        let selectedObject = notification.object as! String
        objectDropDownLabel.text = selectedObject
        detector?.setSelectedClass(selectedClass: selectedObject);
    }
}
