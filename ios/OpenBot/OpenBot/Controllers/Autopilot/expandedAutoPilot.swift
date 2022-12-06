//
// Created by Nitish Yadav on 13/10/22.
//

import Foundation
import UIKit
import DropDown

class expandedAutoPilot: UIView {
    let autoModeButton = UISwitch()
    var serverLabel = UILabel()
    var speedLabel = UILabel()
    var leftSpeedLabel = UILabel()
    var deviceDropDown = DropDown()
    var deviceDropDownLabel = UILabel()
    var modelDropdownLabel = UILabel()
    var imageInputLabel = UILabel()
    var threadLabel = UILabel()
    var deviceDropDownView = UIView()
    var modelDropDownView = UIView()
    var ddView = UIView()
    var modelDropDown = DropDown()
    var serverDropDown = DropDown()
    var dropDownWidth : NSLayoutConstraint!
    var serverDropDownLabel = UILabel()
    var serverDropDownView = UIView()
    var bluetoothIcon = UIImageView()

    override init(frame: CGRect) {
        super.init(frame: frame)
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
        addSubview(createLabel(text: Strings.server, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 50, to: .height))))
        addSubview(createLabel(text: Strings.model, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 90, to: .height))))
        addSubview(createLabel(text: Strings.speed, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 120, to: .height))))
        setupSpeed()
        addSubview(createLabel(text: Strings.device, leadingAnchor: Int(adapted(dimensionSize: 20, to: .height)), topAnchor: Int(adapted(dimensionSize: 160, to: .height))))
        createDeviceDropDown()
        deviceDropDown.hide()
        createServerDropDown()
        serverDropDown.hide()
        createModelDropDown()
        modelDropDown.hide()
        addSubview(createLabel(text: Strings.input, leadingAnchor: 180, topAnchor: Int(adapted(dimensionSize: 120, to: .height))))
        setupInput();
        addSubview(createLabel(text: Strings.threads, leadingAnchor: 180, topAnchor: Int(adapted(dimensionSize: 160, to: .height))))
        setupThreads();
        setupVehicleControls()
        createLeftSpeed()
        NotificationCenter.default.addObserver(self, selector: #selector(updateModel), name: .updateModel, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDevice), name: .updateDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateThreadLabel), name: .updateThreadLabel, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothConnected, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothDisconnected, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateSpeedLabel), name: .updateSpeedLabel, object: nil)
    }


    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
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
                        self.frame.origin.y = height - 375;
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
           bluetoothIcon =  createIcons(iconImg: Images.bluetoothConnected!, topAnchor: adapted(dimensionSize: 20, to: .height), trailingAnchor: -adapted(dimensionSize: 60, to: .height), x: 24.5, y: 21, size: resized(size: Images.bluetoothConnected!.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(ble(_:)))
        } else {
           bluetoothIcon =  createIcons(iconImg: Images.bluetoothDisconnected!, topAnchor: adapted(dimensionSize: 20, to: .height), trailingAnchor: -adapted(dimensionSize: 60, to: .height), x: 24.5, y: 21, size: resized(size: Images.bluetoothDisconnected!.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(ble(_:)))
        }
    }

    func createCameraIcon() {
        if let image = Images.frontCamera {
            createIcons(iconImg: image, topAnchor: adapted(dimensionSize: 20, to: .height), trailingAnchor: -20, x: 16.5, y: 17.5, size: resized(size: image.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(switchCamera(_:)))
        }

    }


    func createLabel(text: String, leadingAnchor: Int, topAnchor: Int) -> UILabel {
        let label = UILabel()
        label.text = text
        label.textColor = Colors.borderColor
        label.frame.origin = CGPoint(x: leadingAnchor, y: topAnchor)
        label.frame.size = resized(size: CGSize(width: text.count * 10, height: 40), basedOn: .height)
        return label
    }

    func createIcons(iconImg: UIImage, topAnchor: CGFloat, trailingAnchor: CGFloat, x: CGFloat, y: CGFloat, size: CGSize, backgroundColor: UIColor, action: Selector?) ->UIImageView {
        let iconImage = UIImageView(frame: CGRect(x: x, y: y, width: size.width, height: size.height))
        iconImage.image = iconImg
        addSubview(iconImage)
        iconImage.translatesAutoresizingMaskIntoConstraints = false
        iconImage.topAnchor.constraint(equalTo: self.topAnchor, constant: topAnchor).isActive = true
        iconImage.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: trailingAnchor).isActive = true
        iconImage.layer.cornerRadius = 30
        let tapGesture = UITapGestureRecognizer(target: self, action: action)
        iconImage.isUserInteractionEnabled = true
        iconImage.addGestureRecognizer(tapGesture)
        return iconImage
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

    func createServerDropDown() {
        serverDropDown.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
            serverDropDown.textColor = color
        }
        serverDropDown.anchorView = serverDropDownView
        serverDropDown.dataSource = ["No Server"]
        serverDropDown.show()
        ddView = createDropdownView(borderColor: "", buttonName: "No Server", leadingAnchor: 180, topAnchor: adapted(dimensionSize: 50, to: .height), action: #selector(showServerDropdown(_:)))
        serverDropDown.selectionAction = { [unowned self] (index: Int, item: String) in
            serverDropDownLabel.text = item
        }
        serverDropDown.width = 150
        serverDropDownView.frame.size = CGSize(width: 200, height: 100);
        serverDropDownView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(serverDropDownView)
        serverDropDownView.topAnchor.constraint(equalTo: topAnchor, constant: adapted(dimensionSize: 40, to: .height)).isActive = true
        serverDropDownView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 180).isActive = true;
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        ddView.addSubview(upwardImage)
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 11.5).isActive = true
        serverDropDownLabel.text = "No Server"
        serverDropDownLabel.textColor = Colors.borderColor
        serverDropDownLabel.frame = CGRect(x: 10, y: 0, width: 210, height: 40)
        ddView.addSubview(serverDropDownLabel)
    }

    func createModelDropDown() {
        let selectedModels = Common.loadSelectedModels(mode: Constants.autopilotMode);
        modelDropDown.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
            modelDropDown.textColor = color
        }
        modelDropDown.anchorView = modelDropDownView
        modelDropDown.dataSource = selectedModels
        modelDropDown.show()
        ddView = createDropdownView(borderColor: "", buttonName: "CLI-Mobile", leadingAnchor: 180, topAnchor: adapted(dimensionSize: 90, to: .height), action: #selector(showModelDropdown(_:)))
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = Images.downArrow
        ddView.addSubview(upwardImage)
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 11.5).isActive = true
        modelDropdownLabel.text = "CLI-Mobile"
        modelDropdownLabel.textColor = Colors.borderColor
        modelDropdownLabel.frame = CGRect(x: 10, y: 0, width: 210, height: 40)
        ddView.addSubview(modelDropdownLabel)
        modelDropDown.selectionAction = { [self] (index: Int, item: String) in
            modelDropdownLabel.text = item
            NotificationCenter.default.post(name: .updateModel, object: item)

        }
        modelDropDown.width = 150
        modelDropDownView.frame.size = CGSize(width: 200, height: 100);
        modelDropDownView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(modelDropDownView)
        modelDropDownView.topAnchor.constraint(equalTo: upwardImage.topAnchor, constant: 0).isActive = true
        modelDropDownView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 180).isActive = true;
    }

    func setupInput() {
        imageInputLabel.frame = CGRect(x: width - 80, y: adapted(dimensionSize: 120, to: .height), width: 100, height: 40)
        imageInputLabel.text = "256x96";
        addSubview(imageInputLabel)
    }

    func setupSpeed() {
        speedLabel = createLabel(text: "*** fps", leadingAnchor: 90, topAnchor: Int(adapted(dimensionSize: 120, to: .height)))
        addSubview(speedLabel)
    }

    func createDeviceDropDown() {
        deviceDropDown.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
            deviceDropDown.textColor = color
        }
        deviceDropDown.anchorView = deviceDropDownView
        deviceDropDown.dataSource = Constants.devices
        deviceDropDown.show()
        deviceDropDown.width = 90
        ddView = createDropdownView(borderColor: "", buttonName: "CPU", leadingAnchor: 80, topAnchor: adapted(dimensionSize: 160, to: .height), action: #selector(showDeviceDropdown(_:)))
        let downwardImage = UIImageView()
        downwardImage.frame.size = CGSize(width: 5, height: 5)
        downwardImage.image = Images.downArrow
        ddView.addSubview(downwardImage)
        downwardImage.translatesAutoresizingMaskIntoConstraints = false
        downwardImage.trailingAnchor.constraint(equalTo: ddView.trailingAnchor, constant: -10).isActive = true
        downwardImage.topAnchor.constraint(equalTo: ddView.topAnchor, constant: 11.5).isActive = true
        deviceDropDownLabel.text = "CPU"
        deviceDropDownLabel.textColor = Colors.borderColor
        deviceDropDownLabel.frame = CGRect(x: 10, y: 0, width: 210, height: 40)
        dropDownWidth.constant = 100
        ddView.addSubview(deviceDropDownLabel)
        deviceDropDown.selectionAction = { [unowned self] (index: Int, item: String) in
            deviceDropDownLabel.text = item
            NotificationCenter.default.post(name: .updateDevice, object: item)
        }
        deviceDropDownView.frame.size = CGSize(width: 200, height: 100);
        deviceDropDownView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(deviceDropDownView)
        deviceDropDownView.topAnchor.constraint(equalTo: topAnchor, constant: 190).isActive = true
        deviceDropDownView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 91).isActive = true
    }

    func createDropdownView(borderColor: String, buttonName: String, leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector?) -> UIView {
        let dd = UIView()
        dd.layer.cornerRadius = 10
        dd.backgroundColor = Colors.freeRoamButtonsColor
        let tap = UITapGestureRecognizer(target: self, action: action)
        dd.addGestureRecognizer(tap)
        addSubview(dd)
        dd.translatesAutoresizingMaskIntoConstraints = false
        dd.topAnchor.constraint(equalTo: self.topAnchor, constant: topAnchor).isActive = true;
        dd.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: leadingAnchor).isActive = true
        dropDownWidth = dd.widthAnchor.constraint(equalToConstant: 180)
        dropDownWidth.isActive = true
        dd.heightAnchor.constraint(equalToConstant: 40).isActive = true
        return dd
    }

    func setupThreads() {
        //setting plus
        let plusImageView = UIView();
        plusImageView.frame.size = CGSize(width: 30, height: 30);
        plusImageView.frame.origin = CGPoint(x: width - 40, y: adapted(dimensionSize: 160, to: .height));
        addSubview(plusImageView)
        let plusImage = UIImageView();
        plusImage.image = UIImage(systemName: "plus");
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
        minusImageView.frame.origin = CGPoint(x: width - 90, y: adapted(dimensionSize: 160, to: .height))
        addSubview(minusImageView)
        let minusImage = UIImageView()
        minusImage.image = UIImage(systemName: "minus");
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
        threadLabel.leadingAnchor.constraint(equalTo: minusImageView.trailingAnchor, constant: 4).isActive = true
        threadLabel.topAnchor.constraint(equalTo: minusImageView.topAnchor, constant: 8).isActive = true
    }

    func setupVehicleControls() {
        let vehicleControls = VehicleControl();
        addSubview(vehicleControls)
        vehicleControls.translatesAutoresizingMaskIntoConstraints = false
        vehicleControls.topAnchor.constraint(equalTo: threadLabel.safeAreaLayoutGuide.bottomAnchor, constant: adapted(dimensionSize: 10, to: .height)).isActive = true;
        vehicleControls.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 7).isActive = true
    }

    func createLeftSpeed() {
        leftSpeedLabel.frame.size = CGSize(width: 100, height: 40);
        leftSpeedLabel.frame.origin = CGPoint(x: 4, y: adapted(dimensionSize: 200, to: .height))
        leftSpeedLabel.text = "xxx,xxx"
        addSubview(leftSpeedLabel)
        leftSpeedLabel.font = leftSpeedLabel.font.withSize(13.5)
    }


    @objc func ble(_ sender: UIView) {
        NotificationCenter.default.post(name: .ble, object: nil)
    }

    @objc func switchCamera(_ sender: UIView) {
        NotificationCenter.default.post(name: .switchCamera, object: nil)
    }

    @objc func switchButton(_ sender: UISwitch) {
        NotificationCenter.default.post(name: .autoMode, object: nil)
    }

    @objc func showServerDropdown(_ sender: UIButton) {
        serverDropDown.show()
    }

    @objc func showModelDropdown(_ sender: UIButton) {
        modelDropDown.show()
    }

    @objc func showDeviceDropdown(_ sender: UIButton) {
        deviceDropDown.show()
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

    func loadModels() -> [ModelItem] {
        if let url = Bundle.main.url(forResource: "config", withExtension: "json") {
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let jsonData = try decoder.decode([ModelItem].self, from: data)
                return jsonData;
            } catch {
                print("error:\(error)")
            }
        }
        return [];
    }

    func loadAllAutoPilotModels() -> [ModelItem] {
        var autoPilot: [ModelItem] = []
        let allModels = loadModels()
        for model in allModels {
            if model.type == Constants.autopilotMode {
                autoPilot.append(model)
            }
        }
        return autoPilot
    }

    @objc func updateModel(_ notification: Notification) {
        let selectedModel = notification.object as! String
        modelDropdownLabel.text = selectedModel
        let models = loadAllAutoPilotModels()
        for model in models {
            guard let index = model.name.firstIndex(of: ".") else {
                return
            }
            if model.name.prefix(upTo: index) == selectedModel {
                imageInputLabel.text = model.inputSize
                break
            }
        }
    }

    @objc func updateDevice(_ notification: Notification) {
        let selectedDevice = notification.object as! String
        deviceDropDownLabel.text = selectedDevice
    }

    @objc func updateThreadLabel(_ notification: Notification) {
        threadLabel.text = (notification.object as! String)

    }

    @objc func updateConnect(_ notification: Notification) {
        if (isBluetoothConnected) {
            bluetoothIcon.image = Images.bluetoothConnected
        } else {
            bluetoothIcon.image = Images.bluetoothDisconnected
        }
    }

    @objc func updateSpeedLabel(_ notification: Notification) {
        leftSpeedLabel.text = notification.object as! String
    }
}

