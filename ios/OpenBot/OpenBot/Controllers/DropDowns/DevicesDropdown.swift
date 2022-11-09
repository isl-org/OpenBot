//
// Created by Nitish Yadav on 17/10/22.
//

import Foundation
import UIKit
import DropDown

class Devices: UIView {
    var deviceDD = DropDown()
    let deviceLabel = UILabel()

    override init(frame: CGRect) {
        super.init(frame: frame)
        NotificationCenter.default.addObserver(self, selector: #selector(showDropDown), name: .showDeviceDD, object: nil)
        setupDeviceDD(dataSource: ["CPU","GPU","XNNPACK"]);
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func setupDeviceDD(dataSource: [String]){
        deviceDD.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
            deviceDD.textColor = color
        }
        deviceDD.anchorView = self
        deviceDD.dataSource = dataSource
        deviceDD.selectionAction = { [self] (index: Int, item: String) in
            deviceLabel.text = item
            NotificationCenter.default.post(name: .updateDevice, object: item)
        }
        deviceDD.width = 90

    }

    @objc func showDropDown() {
        deviceDD.show()
    }

}
