//
// Created by Sparsh Jain on 28/10/22.
//

import Foundation
import UIKit

class ObjectTrackingSettings: UIView {
    let autoModeButton = UISwitch()

    override init(frame: CGRect) {
        super.init(frame: frame);
        createSwitchButton()

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

    @objc func switchButton(_ sender: UISwitch) {
        NotificationCenter.default.post(name: .autoMode, object: nil)
    }
}