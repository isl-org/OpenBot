//
// Created by Nitish Yadav on 13/10/22.
//

import Foundation
import UIKit
import DropDown

class expandedAutoPilot: UIView {
    let switchBtn  = UISwitch()
    var dropDownView = UIView()
    var ddView = UIView()
    let modelDropDown = DropDown()
    var serverLabel = UILabel()
    var speedLabel = UILabel()
    var deviceDropDownLabel = UILabel()
    var modelDropdownLabel = UILabel()
    var inputLabel = UILabel()
    var threadLabel = UILabel()


    override init(frame: CGRect) {
        super.init(frame: frame)
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeDown.direction = .down
        addGestureRecognizer(swipeDown)
        let swipeUp = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeUp.direction = .up
        addGestureRecognizer(swipeUp)
        createBar()
        addSubview(createLabel(text: "Auto Mode", leadingAnchor: 20, topAnchor: 15));
        createBluetoothIcon()
        createCameraIcon()
        createSwitchButton()
        addSubview(createLabel(text: Strings.server, leadingAnchor: 20, topAnchor: 80))
        addSubview(createLabel(text: "Model", leadingAnchor: 20, topAnchor: 120))
        addSubview(createLabel(text: "Speed", leadingAnchor: 20, topAnchor: 160))
        setupSpeed()
        addSubview(createLabel(text: "Device", leadingAnchor: 20, topAnchor: 200))
        createDeviceDropDown()
        createServerDropDown()
        createModelDropDown()
        addSubview(createLabel(text: "Input", leadingAnchor: 180, topAnchor: 160))
        setupInput();
        addSubview(createLabel(text: "Threads", leadingAnchor: 180, topAnchor: 200))
        setupThreads();
        setupVehicleControls()





        NotificationCenter.default.addObserver(self, selector: #selector(updateModel), name: .updateModel, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDevice), name: .updateDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateThreadLabel), name: .updateThreadLabel, object: nil)


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
                        self.frame.origin.y = height - 70
                    } else {
                        self.frame.origin.y = width - 70
                    }
                }
            case .up:
                UIView.animate(withDuration: 0.25) {
                    if currentOrientation == .portrait {
                        self.frame.origin.y = height / 2
                    } else {
                        self.frame.origin.y = 20
                    }
                }
            default:
                break
            }
        }
    }

    func createBar(){
        let bar = UIView()
        bar.backgroundColor = Colors.title
        addSubview(bar)
        bar.translatesAutoresizingMaskIntoConstraints = false
        bar.widthAnchor.constraint(equalToConstant: 60).isActive = true
        bar.heightAnchor.constraint(equalToConstant:5).isActive = true
        bar.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: width/2-30).isActive = true
        bar.topAnchor.constraint(equalTo: topAnchor, constant: 10).isActive = true
        bar.layer.cornerRadius = 2

    }

    func createBluetoothIcon() {

        if (isBluetoothConnected) {
            createIcons(iconImg: Images.bluetoothConnected!, topAnchor: 10, trailingAnchor: -75, x: 24.5, y: 21, size: resized(size: Images.bluetoothConnected!.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(ble(_:)))
        } else {
            createIcons(iconImg: Images.bluetoothDisconnected!, topAnchor: 10, trailingAnchor: -75, x: 24.5, y: 21, size: resized(size: Images.bluetoothDisconnected!.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(ble(_:)))
        }
    }

    func createCameraIcon() {
        if let image = Images.frontCamera {
            createIcons(iconImg: image, topAnchor: 13, trailingAnchor: -20, x: 16.5, y: 17.5, size: resized(size: image.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(switchCamera(_:)))
        }

    }


    func createLabel(text: String, leadingAnchor: Int, topAnchor: Int) -> UILabel {
        let label = UILabel()
        label.text = text
        label.textColor = Colors.borderColor
        label.frame.origin = CGPoint(x: leadingAnchor, y: topAnchor)
        label.frame.size = CGSize(width: 100, height: 50)
        return label
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
        icon.topAnchor.constraint(lessThanOrEqualTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true
        icon.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: trailingAnchor).isActive = true
        icon.layer.cornerRadius = 30
        let tapGesture = UITapGestureRecognizer(target: self, action: action)
        icon.addGestureRecognizer(tapGesture)
    }

    func createSwitchButton() {
        switchBtn.isOn = false
        switchBtn.setOn(false, animated: true)
        switchBtn.onTintColor = Colors.title
        switchBtn.addTarget(self, action: #selector(switchButton(_:)), for: .valueChanged)
        switchBtn.translatesAutoresizingMaskIntoConstraints = false
        addSubview(switchBtn)
        switchBtn.widthAnchor.constraint(equalToConstant: 40).isActive = true
        switchBtn.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 120).isActive = true
        switchBtn.topAnchor.constraint(equalTo: topAnchor, constant: 25).isActive = true
    }


    func createServerDropDown() {
        let server = Server(frame: CGRect(x: 180, y: 80, width: 100, height: 200));
        addSubview(server)
        let dd = UIView()
        dd.layer.cornerRadius = 10
        serverLabel.text = "No Server"
        serverLabel.textColor = Colors.borderColor
        let tap = UITapGestureRecognizer(target: self, action: #selector(showServerDropdown(_:)))
        dd.addGestureRecognizer(tap)
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = UIImage(systemName: "arrowtriangle.down.fill")
        dd.addSubview(upwardImage)
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: dd.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: dd.topAnchor, constant: 11.5).isActive = true
        addSubview(dd)
        dd.translatesAutoresizingMaskIntoConstraints = false
        dd.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 80).isActive = true;
        dd.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 180).isActive = true
        dd.widthAnchor.constraint(equalToConstant: 180).isActive = true
        dd.heightAnchor.constraint(equalToConstant: 40).isActive = true
        serverLabel.frame = CGRect(x: 10, y: 0, width: 210, height: 40)
        dd.addSubview(serverLabel)
    }

    func createModelDropDown() {
        let model = Models(frame: CGRect(x: 180, y: 120, width: 100, height: 200));
        addSubview(model)
        let dd = UIView()
        dd.layer.cornerRadius = 10
        dd.backgroundColor = Colors.freeRoamButtonsColor
        modelDropdownLabel.text = "CIL-Mobile"
        modelDropdownLabel.textColor = Colors.borderColor
        let tap = UITapGestureRecognizer(target: self, action: #selector(showModelDropdown(_:)))
        dd.addGestureRecognizer(tap)
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = UIImage(systemName: "arrowtriangle.down.fill")
        dd.addSubview(upwardImage)
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: dd.trailingAnchor, constant: -10).isActive = true
        upwardImage.topAnchor.constraint(equalTo: dd.topAnchor, constant: 11.5).isActive = true
        addSubview(dd)
        dd.translatesAutoresizingMaskIntoConstraints = false
        dd.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 120).isActive = true;
        dd.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 180).isActive = true
        dd.widthAnchor.constraint(equalToConstant: 180).isActive = true
        dd.heightAnchor.constraint(equalToConstant: 40).isActive = true
        modelDropdownLabel.frame = CGRect(x: 10, y: 0, width: 210, height: 40)
        dd.addSubview(modelDropdownLabel)
    }

    func setupInput() {
        inputLabel.frame = CGRect(x: 290, y: 164.5, width: 100, height: 40)
        inputLabel.text = "256x96";
        addSubview(inputLabel)
    }

    func setupSpeed(){
        speedLabel = createLabel(text: "*** fps", leadingAnchor: 90, topAnchor: 160)
        addSubview(speedLabel)
    }

    func createDeviceDropDown(){
        let device = Devices(frame: CGRect(x: 91, y: 190, width: 40, height: 200));
        addSubview(device)
        let dd = UIView()
        dd.layer.cornerRadius = 10
        dd.backgroundColor = Colors.freeRoamButtonsColor
        deviceDropDownLabel.text = "CPU"
        deviceDropDownLabel.textColor = Colors.borderColor
        let tap = UITapGestureRecognizer(target: self, action: #selector(showDeviceDropdown(_:)))
        dd.addGestureRecognizer(tap)
        let upwardImage = UIImageView()
        upwardImage.frame.size = CGSize(width: 5, height: 5)
        upwardImage.image = UIImage(systemName: "arrowtriangle.down.fill")
        dd.addSubview(upwardImage)
        upwardImage.translatesAutoresizingMaskIntoConstraints = false
        upwardImage.trailingAnchor.constraint(equalTo: dd.trailingAnchor, constant: -20).isActive = true
        upwardImage.topAnchor.constraint(equalTo: dd.topAnchor, constant: 11.5).isActive = true
        addSubview(dd)
        dd.translatesAutoresizingMaskIntoConstraints = false
        dd.topAnchor.constraint(equalTo: topAnchor, constant: 207).isActive = true;
        dd.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 80).isActive = true
        dd.widthAnchor.constraint(equalToConstant: 100).isActive = true
        dd.heightAnchor.constraint(equalToConstant: 30).isActive = true
        deviceDropDownLabel.frame = CGRect(x: 10, y: 0, width: 210, height: 40)
        dd.addSubview(deviceDropDownLabel)
    }

    func setupThreads() {
        let threadView = UIView();
        threadView.frame.size = CGSize(width: 100, height: 30);
        threadView.frame.origin = CGPoint(x: 260, y: 210)
        addSubview(threadView);
        let plusImage = UIImageView();
        plusImage.image = UIImage(systemName: "plus");
        plusImage.frame.size = CGSize(width: 30, height: 30);
        plusImage.isUserInteractionEnabled = true;
        let tapOnPlus = UITapGestureRecognizer(target: self, action: #selector(increaseThreads(_:)))
        plusImage.addGestureRecognizer(tapOnPlus)
        threadView.addSubview(plusImage)
        plusImage.translatesAutoresizingMaskIntoConstraints = false
        plusImage.trailingAnchor.constraint(equalTo: threadView.trailingAnchor, constant: -2).isActive = true
        plusImage.topAnchor.constraint(equalTo: threadView.topAnchor, constant: 5).isActive = true
        //minus image
        let minusImage = UIImageView()
        minusImage.image = UIImage(systemName: "minus");
        minusImage.frame.size = CGSize(width: 30, height: 30);
        minusImage.isUserInteractionEnabled = true;
        threadView.addSubview(minusImage)
        let tapOnMinus = UITapGestureRecognizer(target: self, action: #selector(decreaseThreads(_:)))
        minusImage.addGestureRecognizer(tapOnMinus)
        minusImage.translatesAutoresizingMaskIntoConstraints = false
        minusImage.leadingAnchor.constraint(equalTo: threadView.leadingAnchor, constant: 2).isActive = true
        minusImage.topAnchor.constraint(equalTo: threadView.topAnchor, constant: 5).isActive = true
        //thread Label
        threadLabel.frame.size = CGSize(width: 10, height: 40);
        threadView.addSubview(threadLabel);
        threadLabel.text = "1";
        threadLabel.textColor = Colors.borderColor
        threadLabel.translatesAutoresizingMaskIntoConstraints = false
        threadLabel.leadingAnchor.constraint(equalTo: threadView.leadingAnchor, constant: 43).isActive = true
        threadLabel.topAnchor.constraint(equalTo: threadView.topAnchor,constant: 5).isActive = true
    }

    func setupVehicleControls(){
        let vehicleControls = VehicleControl();
        addSubview(vehicleControls)
        vehicleControls.translatesAutoresizingMaskIntoConstraints = false
        vehicleControls.topAnchor.constraint(equalTo: threadLabel.safeAreaLayoutGuide.bottomAnchor, constant: adapted(dimensionSize: 20, to: .height)).isActive = true;
        vehicleControls.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 7).isActive = true
    }


    @objc func ble(_ sender: UIView) {

        NotificationCenter.default.post(name: .ble, object: nil)

    }

    @objc func switchCamera(_ sender: UIView) {
        NotificationCenter.default.post(name: .switchCamera, object: nil)
    }

    @objc func switchButton(_ sender: UISwitch) {
        NotificationCenter.default.post(name: .autoMode, object: nil)
        if sender.isOn {

        } else {

        }
    }

    @objc func showServerDropdown(_ sender: UIButton) {
        NotificationCenter.default.post(name: .showServerDD, object: nil)
    }

    @objc func showModelDropdown(_ sender: UIButton) {
        NotificationCenter.default.post(name: .showModelsDD, object: nil)
    }

    @objc func showDeviceDropdown(_ sender: UIButton) {
        NotificationCenter.default.post(name: .showDeviceDD, object: nil)
    }

    @objc func increaseThreads(_ sender: UIImage) {
       if threadLabel.text == "9" || threadLabel.text == "N/A"{
           return
       }
       var value = Int(threadLabel.text ?? "1")
        value = (value ?? 1) + 1;
        threadLabel.text = String(value!)
        NotificationCenter.default.post(name: .updateThread, object: threadLabel.text)
    }

    @objc func decreaseThreads(_ sender: UIImage) {
        if threadLabel.text == "1"  || threadLabel.text == "N/A"{
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

    func loadAllAutoPilotModels()->[ModelItem]{
        var autoPilot :[ModelItem] = []
        let allModels = loadModels()
        for model in allModels {
            if model.type == "AUTOPILOT" {
                autoPilot.append(model)
            }
        }
        return autoPilot
    }


    @objc func updateModel(_ notification: Notification) {
        let selectedModel = notification.object as! String
        modelDropdownLabel.text = selectedModel
        let models  = loadAllAutoPilotModels()
        for model in models {
            guard let index = model.name.firstIndex(of: ".") else {
              return
            }
            if model.name.prefix(upTo: index) == selectedModel{
                inputLabel.text = model.inputSize

                break
            }
        }
    }

    @objc func updateDevice(_ notification: Notification) {
        let selectedDevice = notification.object as! String
        deviceDropDownLabel.text = selectedDevice
    }

    @objc func updateThreadLabel(_ notification: Notification) {
       threadLabel.text = notification.object as! String

    }
}

extension Notification.Name {
    static let showModelsDD = Notification.Name("showModelsDD")
    static let showServerDD = Notification.Name("showServerDD")
    static let showDeviceDD = Notification.Name("showDeviceDD")
    static let autoMode  = Notification.Name("autoMode")
    static let updateThread = Notification.Name("updateThread");
    static let updateThreadLabel = Notification.Name("updateThreadLabel")
}