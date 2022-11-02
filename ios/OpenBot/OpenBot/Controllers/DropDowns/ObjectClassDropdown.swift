//
// Created by Sparsh Jain on 28/10/22.
//

import Foundation
import UIKit
import DropDown

class ObjectClassDropdown: UIView {
    let object = DropDown();
    let objectLabel = UILabel()
    let detector: Detector?;

    public init(frame: CGRect, selectedObject: String, detector: Detector) {
        self.detector = detector;
        super.init(frame: frame);
        NotificationCenter.default.addObserver(self, selector: #selector(showDropDown), name: .showObjectDD, object: nil)
        setupObjectDD(dataSource: detector.getLabels())
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