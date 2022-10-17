//
// Created by Nitish Yadav on 14/10/22.
//

import Foundation
import UIKit
import DropDown
class Models : UIView {
    let model = DropDown();
let modelLabel = UILabel()
    override init(frame: CGRect) {
        super.init(frame: frame)
        NotificationCenter.default.addObserver(self, selector: #selector(showDropDown), name: .showModelsDD, object: nil)
        setupModelDD(dataSource: loadAllAutoPilotModels())
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func setupModelDD(dataSource : [String]){
        model.backgroundColor = Colors.freeRoamButtonsColor
        if let color = Colors.borderColor {
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

    func loadModels() -> [ModelItem] {
        if let url = Bundle.main.url(forResource: "config", withExtension: "json") {
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let jsonData = try decoder.decode([ModelItem].self, from: data)
                return jsonData;
            } catch {
                print("error:\(error)")
            }
        }
        return [];
    }

    func loadAllAutoPilotModels()->[String]{
        var autoPilot :[String] = []
        let allModels = loadModels()
        for model in allModels {
            if model.type == "AUTOPILOT" {
                autoPilot.append(String(model.name.prefix(upTo: model.name.firstIndex(of: ".")!)))
            }
        }
        return autoPilot
    }
}
extension Notification.Name {
    static let updateModel = Notification.Name("updateModel")
}