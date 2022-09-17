//
// Created by Nitish Yadav on 16/09/22.
//

import Foundation
import UIKit

class collapseSettingView: UIView {
    var loadCollapseSetting = true
    var loadExpandedSetting = false

    override init(frame: CGRect) {
        super.init(frame: frame)
        createBluetoothIcon()
        createCameraIcon()
        createSettingIcon()
        NotificationCenter.default.addObserver(self, selector: #selector(updateScreen), name: .clickSetting, object: nil)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    func createBluetoothIcon() {
        if let image = UIImage(named: "frontCamera") {
            createIcons(iconImg: image, topAnchor: 10, backgroundColor: Colors.title ?? .blue, action: #selector(setting(_:)))
        }
    }

    func createCameraIcon() {
        if let image = UIImage(named: "ble") {
            createIcons(iconImg: image, topAnchor: 80, backgroundColor: Colors.title ?? .blue, action: #selector(setting(_:)))
        }

    }

    func createSettingIcon() {
        if let image = UIImage(named: "settings") {
            createIcons(iconImg: image, topAnchor: 155, backgroundColor: Colors.borderColor ?? .white, action: #selector(setting(_:)))
        }
    }

    func createIcons(iconImg: UIImage, topAnchor: CGFloat, backgroundColor: UIColor, action: Selector?) {
        let icon = UIView()
        let size = CGSize(width: 60, height: 60)
        icon.frame.size = size
        let iconImage = UIImageView(frame: (CGRect(x: icon.frame.width / 4, y: icon.frame.height / 4, width: icon.frame.width * 0.5, height: icon.frame.width * 0.5)))
        icon.addSubview(iconImage)
        iconImage.image = iconImg
        icon.backgroundColor = backgroundColor
        icon.addSubview(iconImage)
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
//        addSubview(expandSetting(frame: CGRect(x: 0, y: 0, width: width, height: height)))
//        exchangeSubview(at: 0, withSubviewAt: 1)
//        addSubview(expandSetting(frame: CGRect(x: 0, y: 0, width: width, height: height)))
        NotificationCenter.default.post(name: .clickSetting, object: nil)

    }

    @objc func updateScreen() {

    }


}

extension Notification.Name {
    static let clickSetting = Notification.Name("clickSetting")

}