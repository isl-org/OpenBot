//
// Created by Nitish Yadav on 14/10/22.
//

import Foundation
import UIKit
import DropDown

class Server: UIView {
    let serverDD = DropDown()
    let serverLabel = UILabel()
    override init(frame: CGRect) {
        super.init(frame: frame)
        NotificationCenter.default.addObserver(self, selector: #selector(showDropDown), name: .showServerDD, object: nil)
        setupServerDD(dataSource: ["No Server"])

    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func setupServerDD(dataSource: [String]) {
        serverDD.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.border {
            serverDD.textColor = color
        }
        serverDD.anchorView = self
        serverDD.dataSource = dataSource
        serverDD.selectionAction = { [self] (index: Int, item: String) in
            serverLabel.text = item
        }
        serverDD.width = 150
    }

    @objc func showDropDown() {
        serverDD.show()
    }
}