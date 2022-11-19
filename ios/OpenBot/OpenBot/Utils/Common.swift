//
// Created by Sparsh Jain on 31/10/22.
//

import Foundation

class Common {

    /**

     - Returns:
     All modelItems
     */
    static func loadAllModelItems() -> [ModelItem] {
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

    /**
     Takes modes as argument
     - Returns:
     array of all models of that type which are downloaded
     */
    static func loadSelectedModels(mode: String) -> [String] {
        var selectedModels: [String] = []
        let allModels = loadAllModelItems()
        for model in allModels {
            let split = model.path.split(separator: "/");
            let index = split.count - 1;
            let fileName = String(split[index]);
            if model.type == mode {
                let bundle = Bundle.main
                let path = bundle.path(forResource: fileName, ofType: "")
                if path != nil {
                    selectedModels.append(String(model.name.prefix(upTo: model.name.firstIndex(of: ".")!)))
                }
            }
        }
        return selectedModels
    }

    static func loadAllModels() -> [String] {
        var models: [String] = []
        let allModels = loadAllModelItems()
        for model in allModels {
            models.append(String(model.name.prefix(upTo: model.name.firstIndex(of: ".")!)))
        }
        return models
    }

    static func loadSelectedModel(modeName: String) -> ModelItem {
        var model: ModelItem!
        let allModels = loadAllModelItems()
        for item in allModels {
            model = item
            if model.name != nil {
                if model.name.prefix(upTo: model.name.firstIndex(of: ".")!) == modeName {
                    return model
                }
            }
        }
        return model
    }

    /**

     - Parameter mode:
     - Returns:
     List of all model of type mode which are defined in config.json
     */

    static func loadAllSelectedModelItems(mode: String) -> [String] {
        var selectedModels: [String] = []
        let allModels = loadAllModelItems()
        for model in allModels {
            if model.type == mode {
                    selectedModels.append(String(model.name.prefix(upTo: model.name.firstIndex(of: ".")!)))
            }
        }
        return selectedModels
    }
}