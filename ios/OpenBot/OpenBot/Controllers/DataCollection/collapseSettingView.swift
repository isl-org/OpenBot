//
// Created by Nitish Yadav on 16/09/22.
//

import Foundation
import UIKit

class collapseSettingView: UIView {

    override init(frame: CGRect) {
        super.init(frame: frame)
        createBluetoothIcon()
        createCameraIcon()
        createSettingIcon()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    func createBluetoothIcon() {
        if let image = Images.ble {
            createIcons(iconImg: image, topAnchor: 10, x: 24.5, y: 21, size: resized(size: image.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(ble(_:)))
        }
    }

    func createCameraIcon() {
        if let image = Images.frontCamera {
            createIcons(iconImg: image, topAnchor: 80, x: 16.5, y: 17.5, size: resized(size: image.size, basedOn: Dimension.height), backgroundColor: Colors.title ?? .blue, action: #selector(switchCamera(_:)))
        }

    }

    func createSettingIcon() {
        if let image = Images.settings {
            createIcons(iconImg: image, topAnchor: 150, x: 6.0, y: 9.5, size: resized(size: image.size, basedOn: Dimension.height), backgroundColor: Colors.borderColor ?? .white, action: #selector(setting(_:)))
        }
    }

    func createIcons(iconImg: UIImage, topAnchor: CGFloat, x: CGFloat, y: CGFloat, size: CGSize, backgroundColor: UIColor, action: Selector?) {
        let icon = UIView(frame: CGRect(x: 0, y: 0, width: 60, height: 60))
        let iconImage = UIImageView(frame: CGRect(x: x, y: y, width: size.width, height: size.height))
        iconImage.image = iconImg
        icon.addSubview(iconImage)
        icon.backgroundColor = backgroundColor
        addSubview(icon)
        icon.translatesAutoresizingMaskIntoConstraints = false
        icon.widthAnchor.constraint(equalToConstant: 60).isActive = true
        icon.heightAnchor.constraint(equalToConstant: 60).isActive = true
        icon.topAnchor.constraint(lessThanOrEqualTo: safeAreaLayoutGuide.topAnchor, constant: topAnchor).isActive = true
        icon.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor, constant: -30).isActive = true
        icon.layer.cornerRadius = 30
        let tapGesture = UITapGestureRecognizer(target: self, action: action)
        icon.addGestureRecognizer(tapGesture)
    }

    @objc func setting(_ sender: UIView) {
        NotificationCenter.default.post(name: .clickSetting, object: nil)
    }

    @objc func ble(_ sender: UIView) {
        NotificationCenter.default.post(name: .ble, object: nil)
    }

    @objc func switchCamera(_ sender: UIView) {
        NotificationCenter.default.post(name: .switchCamera, object: nil)
    }

    @objc func updateScreen() {

    }



}

extension Notification.Name {
    static let clickSetting = Notification.Name(Strings.clickSetting)
    static let switchCamera = Notification.Name(Strings.switchCamera)
    static let ble = Notification.Name(Strings.ble)

}