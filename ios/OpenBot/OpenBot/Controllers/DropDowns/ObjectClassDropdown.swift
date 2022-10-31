//
// Created by Sparsh Jain on 28/10/22.
//

import Foundation
import UIKit
import DropDown

class ObjectClassDropdown: UIView {
    let object = DropDown();
    let objectLabel = UILabel()
    public init(frame: CGRect, selectedObject: String) {
        super.init(frame: frame)
        NotificationCenter.default.addObserver(self, selector: #selector(showDropDown), name: .showObjectDD, object: nil)
        setupObjectDD(dataSource: Common.loadAllObjectsList())
    }

    func setupObjectDD(dataSource: [String]) {
        object.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
            object.textColor = color
        }
        object.anchorView = self
        object.dataSource = dataSource
        object.selectionAction = { [self] (index: Int, item: String) in
            objectLabel.text = item
            NotificationCenter.default.post(name: .updateObject, object: item)
        }
        object.width = 150;
    }

    @objc func showDropDown() {
        object.show()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

}