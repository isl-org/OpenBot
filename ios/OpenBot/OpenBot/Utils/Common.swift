//
// Created by Sparsh Jain on 31/10/22.
//

import Foundation

class Common {
    
    /**
     - Returns:
     All modelItems
     */
    static func loadAllModelItemsFromBundle() -> [ModelItem] {
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
    
    static func loadAllModelItems() -> [ModelItem] {
        // let models = loadAllModelFromDocumentDirectory()
        // if models.count > 0 {
        //     return models
        // }
        return loadAllModelItemsFromBundle()
    }
    
    static func loadAllModelFromDocumentDirectory() -> [ModelItem] {
        let fileName = "config.json"
        var filePath = ""
        let dirs: [String] = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.allDomainsMask, true)
        if dirs.count > 0 {
            let dir = dirs[0] //documents directory
            filePath = dir.appending("/" + fileName)
        } else {
            print("Could not find local directory to store file")
        }
        do {
            let data = try Data.init(contentsOf: URL(string: "file://" + filePath)!)
            let decoder = JSONDecoder()
            let jsonData = try decoder.decode([ModelItem].self, from: data)
            return jsonData
        } catch let error as NSError {
            print("An error took place: \(error)")
        }
        return []
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
            
            if model.type == mode && model.pathType == "ASSET" {
                if isModelItemAvailableInDocument(modelName: model.name + Strings.tflite) {
                    selectedModels.append(model.name)
                }
            }
            let split = model.path.split(separator: "/");
            let index = split.count - 1;
            if index < 0 {
                continue;
            }
            let fileName = String(split[index]);
            if model.type == mode {
                let bundle = Bundle.main
                let path = bundle.path(forResource: fileName, ofType: "")
                if path != nil {
                    selectedModels.append(String(model.name.prefix(upTo: model.name.firstIndex(of: ".")!)))
                }
                if isModelItemAvailableInDocument(modelName: String(model.name.prefix(upTo: model.name.firstIndex(of: ".")!))) {
                    selectedModels.append(String(model.name.prefix(upTo: model.name.firstIndex(of: ".")!)))
                }
                
            }
        }
        return selectedModels
    }
    
    static func loadAllModelsName() -> [String] {
        var models: [String] = []
        let allModels = loadAllModelItems()
        for model in allModels {
            if model.name != "" {
                let index = model.name.firstIndex(of: ".")
                if index == nil {
                    models.append(model.name);
                } else {
                    models.append(String(model.name.prefix(upTo: index!)))
                }
                
            }
        }
        return models
    }
    
    static func returnModelItem(modelName: String) -> ModelItem {
        let allModels = loadAllModelItems()
        for item in allModels {
            if item.name != nil {
                
                if item.name.contains(modelName) {
                    return item;
                }
                
            }
        }
        let model: ModelItem = ModelItem.init(id: allModels.count + 1, class: allModels[0].class, type: allModels[0].type, name: modelName, pathType: allModels[allModels.count - 1].pathType, path: "", inputSize: allModels[0].inputSize)
        return model;
    }
    
    /**
     
     - Parameter mode:
     - Returns:
     List of all model of type mode which are defined in config.json
     */
    
    static func returnAllModelItemsName(mode: String) -> [String] {
        var selectedModels: [String] = []
        let allModels = loadAllModelItems()
        for model in allModels {
            if model.type == mode, let index = model.name.firstIndex(of: ".") {
                selectedModels.append(String(model.name.prefix(upTo: index)))
            }
        }
        return selectedModels
    }
    
    static func isModelItemAvailableInBundle(modelName: String) -> Bool {
        let allModels = loadAllModelItemsFromBundle()
        for model in allModels {
            let split = model.path.split(separator: "/");
            let index = split.count - 1;
            let fileName = String(split[index]);
            if model.name.prefix(upTo: model.name.index(of: ".")!) == modelName {
                let bundle = Bundle.main
                let path = bundle.path(forResource: fileName, ofType: "")
                if path != nil {
                    return true;
                }
            }
            
            
        }
        return false
    }
    
    static func isModelItemAvailableInDocument(modelName: String) -> Bool {
        
        for url in DataLogger.shared.getDocumentDirectoryInformation() {
            let model = returnModelItem(modelName: modelName)
            if url.lastPathComponent == model.name {
                return true
            }
        }
        
        return false
    }
    
    static func returnNameOfFile(url: URL) -> String {
        let filepath = url.absoluteString
        let index = filepath.lastIndex(of: "/")
        if let index {
            return filepath.substring(from: index)
        }
        return "unnamed"
    }
}
