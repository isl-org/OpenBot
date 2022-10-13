//
// Created by Nitish Yadav on 13/10/22.
//

import Foundation
import UIKit
import DropDown

class expandedAutoPilot: UIView {
    let logData = UISwitch()
    var dropDownView = UIView()
    var ddView = UIView()
    let dropDown = DropDown()
    var dropdownLabel = UILabel()
    var dropdownTopAnchor: NSLayoutConstraint!

    override init(frame: CGRect) {
        super.init(frame: frame)
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeDown.direction = .down
        addGestureRecognizer(swipeDown)
        let swipeUp = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeUp.direction = .up
        addGestureRecognizer(swipeUp)
        addSubview(createLabel(text: "Auto Mode", leadingAnchor: 20, topAnchor: 40));
        createBluetoothIcon()
        createCameraIcon()
        createLogDataButton()
        addSubview(createLabel(text: Strings.server, leadingAnchor: 20, topAnchor: 80))
        addSubview(createLabel(text: "Model", leadingAnchor: 20, topAnchor: 120))
        addSubview(createLabel(text: "Speed", leadingAnchor: 20, topAnchor: 160))
        addSubview(createLabel(text: "Device", leadingAnchor: 20, topAnchor: 200))
        createServerDropDown()
//        createModelDropDown(dataSource: ["CIL-Mobil"], buttonName: "CIL-Mobil", leadingAnchor: 180, topAnchor: 200)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    @objc func respondToSwipeGesture(gesture: UIGestureRecognizer) {
        if let swipeGesture = gesture as? UISwipeGestureRecognizer {
            switch swipeGesture.direction {
            case .down:
                UIView.animate(withDuration: 0.45) {
                    if currentOrientation == .portrait {
                        self.frame.origin.y = height - 70
                    } else {
                        self.frame.origin.y = width - 70
                    }
                }
            case .up:
                UIView.animate(withDuration: 0.45) {
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

    func createBluetoothIcon() {
        if let image = Images.ble {
            createIcons(iconImg: image, topAnchor: 10, trailingAnchor: -100, x: 24.5, y: 21, size: resized(size: image.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(ble(_:)))
        }
    }

    func createCameraIcon() {
        if let image = Images.frontCamera {
            createIcons(iconImg: image, topAnchor: 10, trailingAnchor: -30, x: 16.5, y: 17.5, size: resized(size: image.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(switchCamera(_:)))
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
        let icon = UIView(frame: CGRect(x: 0, y: 0, width: 60, height: 60))
        let iconImage = UIImageView(frame: CGRect(x: x, y: y, width: size.width, height: size.height))
        iconImage.image = iconImg
        icon.addSubview(iconImage)
//        icon.backgroundColor = backgroundColor
        addSubview(icon)
        icon.translatesAutoresizingMaskIntoConstraints = false
        icon.widthAnchor.constraint(equalToConstant: 60).isActive = true
        icon.heightAnchor.constraint(equalToConstant: 60).isActive = true
        icon.topAnchor.constraint(lessThanOrEqualTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true
        icon.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: trailingAnchor).isActive = true
        icon.layer.cornerRadius = 30
        let tapGesture = UITapGestureRecognizer(target: self, action: action)
        icon.addGestureRecognizer(tapGesture)
    }

    func createLogDataButton() {
        logData.isOn = false
        logData.setOn(false, animated: true)
        logData.onTintColor = Colors.title
        logData.addTarget(self, action: #selector(switchLogButton(_:)), for: .valueChanged)
        logData.translatesAutoresizingMaskIntoConstraints = false
        addSubview(logData)
        logData.widthAnchor.constraint(equalToConstant: 20).isActive = true
        logData.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 120).isActive = true
        logData.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor, constant: 50).isActive = true
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

    func createDropdown(dataSource: [String], leadingAnchor: CGFloat, topAnchor: CGFloat, menuLeadingAnchor: CGFloat, buttonName: String) -> UIView {
        dropDown.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
            dropDown.textColor = color
        }
        dropDown.anchorView = dropDownView
        dropDown.dataSource = dataSource
        dropDown.show()
        ddView = createDropdownView(borderColor: "", buttonName: buttonName, leadingAnchor: leadingAnchor, topAnchor: topAnchor, action: #selector(showDropdown(_:)))
        dropDown.selectionAction = { [unowned self] (index: Int, item: String) in
            dropdownLabel.text = item
        }
        dropDown.width = 200
        dropDownView.frame.size = CGSize(width: 200, height: 100);
        dropDownView.translatesAutoresizingMaskIntoConstraints = false

        return dropDownView

    }

    func createServerDropDown() {
        let serverDropDown = createDropdown(dataSource: ["No Server"], leadingAnchor: 240, topAnchor: 85, menuLeadingAnchor: 180, buttonName: "No Server")
        dropDown.hide()
        addSubview(serverDropDown)
        serverDropDown.translatesAutoresizingMaskIntoConstraints = false
        serverDropDown.topAnchor.constraint(equalTo: ddView.safeAreaLayoutGuide.bottomAnchor, constant: 5).isActive = true
        serverDropDown.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 180).isActive = true;
    }

    func createModelDropDown(dataSource : [String] , buttonName : String, leadingAnchor: CGFloat, topAnchor: CGFloat) {
        dropDown.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
            dropDown.textColor = color
        }
        dropDown.anchorView = dropDownView
        dropDown.dataSource = dataSource
        dropDown.show()
        ddView = createAnotherDropDownView(borderColor: "", buttonName: buttonName, leadingAnchor: leadingAnchor, topAnchor: topAnchor, action: #selector(showDropdown(_:)))
        dropDown.selectionAction = { [unowned self] (index: Int, item: String) in
            dropdownLabel.text = item
        }
        dropDown.width = 200
        dropDownView.frame.size = CGSize(width: 200, height: 100);
        dropDownView.translatesAutoresizingMaskIntoConstraints = false
        dropDownView.topAnchor.constraint(equalTo: ddView.safeAreaLayoutGuide.bottomAnchor, constant: 5).isActive = true
        dropDownView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor, constant: 180).isActive = true;

    }

    func createAnotherDropDownView(borderColor: String, buttonName: String, leadingAnchor: CGFloat, topAnchor: CGFloat, action: Selector?)->UIView{
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

    @objc func ble(_ sender: UIView) {
        NotificationCenter.default.post(name: .ble, object: nil)
    }

    @objc func switchCamera(_ sender: UIView) {
        NotificationCenter.default.post(name: .switchCamera, object: nil)
    }

    @objc func switchLogButton(_ sender: UISwitch) {
        NotificationCenter.default.post(name: .logData, object: nil)
        if sender.isOn {

        } else {

        }
    }

    @objc func showDropdown(_ sender: UIButton) {
        dropDown.show()
    }


}