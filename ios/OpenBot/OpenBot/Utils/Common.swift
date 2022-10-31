//
// Created by Sparsh Jain on 31/10/22.
//

import Foundation

class Common {
    static func loadAllModels() -> [ModelItem] {
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

    static func loadSelectedModels(mode: String) -> [String] {
        var selectedModels: [String] = []
        let allModels = loadAllModels()
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

   static func loadSelectedModel(modeName : String) -> ModelItem {
       var model: ModelItem!
       let allModels = loadAllModels()
       for model in allModels {
           if model.name != nil {
               if model.name.prefix(upTo: model.name.index(of: ".")!) == modeName {
                   return model
               }
           }
       }
       return model

   }
}