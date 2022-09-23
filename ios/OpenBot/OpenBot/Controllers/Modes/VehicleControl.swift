//
// Created by Nitish Yadav on 23/09/22.
//

import Foundation
import UIKit

class Vehicle : UIView {
    var controller = UIButton()
    var DriveMode = UIButton()
    var speed = UIButton()
    override init(frame: CGRect) {
        super.init(frame: frame)
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        createControllerMode()
        createDriveMode()
        createSpeedMode()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func createControllerMode(){
        controller = createButton(borderColor: "red", buttonName: "Test", leadingAnchor: 10, topAnchor: 40, action:  #selector(applyLowResolution(_:)))
        addSubview(controller)


    }

    func createDriveMode(){
        DriveMode = createButton(borderColor: "red", buttonName: "Test", leadingAnchor: 100, topAnchor: 40, action:  #selector(applyLowResolution(_:)))
        addSubview(DriveMode)

    }

    func createSpeedMode(){
        speed = createButton(borderColor: "red", buttonName: "Test", leadingAnchor: 190, topAnchor: 40, action:  #selector(applyLowResolution(_:)))
        addSubview(speed)
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

        @objc func applyLowResolution(_ sender: UIButton){
                print("hello nitish")
        }



}