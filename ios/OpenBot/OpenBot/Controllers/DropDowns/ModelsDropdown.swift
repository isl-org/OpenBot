//
// Created by Nitish Yadav on 14/10/22.
//

import Foundation
import UIKit
import DropDown

class Models: UIView {
    let model = DropDown();
    let modelLabel = UILabel()

    public init(frame: CGRect, selectedModels: [String]) {
        super.init(frame: frame)
        NotificationCenter.default.addObserver(self, selector: #selector(showDropDown), name: .showModelsDD, object: nil)
        setupModelDD(dataSource: selectedModels)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }


    func setupModelDD(dataSource: [String]) {
        model.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.border {
            model.textColor = color
        }
        model.anchorView = self
        model.dataSource = dataSource
        model.selectionAction = { [self] (index: Int, item: String) in
            modelLabel.text = item
            NotificationCenter.default.post(name: .updateModel, object: item)
        }
        model.width = 150
    }

    @objc func showDropDown() {
        model.show()
    }
}


