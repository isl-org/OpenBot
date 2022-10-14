//
// Created by Sparsh Jain on 29/09/22.
//

import Foundation
import UIKit

struct ModelItem: Codable {
    var id: Int;
    var `class`: String;
    var type: String;
    var name: String;
    var pathType: String;
    var path: String;
    var inputSize: String;
}

class Model {
    var id: Int;
    var classType: CLASS;
    var type: TYPE;
    var name: String;
    var pathType: PATH_TYPE;
    var path: String;
    var inputSize: String;

    init(id: Int, classType: CLASS, type: TYPE, name: String, pathType: PATH_TYPE, path: String, inputSize: String) {
        self.id = id;
        self.classType = classType;
        self.type = type;
        self.name = name;
        self.pathType = pathType;
        self.path = path;
        self.inputSize = inputSize;
    }

    func getName() -> String {
        name;
    }

    func getInputSize() -> CGSize {
        CGSize.parseSize(inputSize);
    }

    func setPath(path: String) {
        self.path = path;
    }

    func setPathType(pathType: PATH_TYPE) {
        self.pathType = pathType;
    }

    func setInputSize(inputSize: String) {
        self.inputSize = inputSize;
    }

    func setName(name: String) {
        self.name = name;
    }

    func setClassType(classType: CLASS) {
        self.classType = classType;
    }

    func setType(type: TYPE) {
        self.type = type;
    }
}


extension CGSize {
    static func parseSize(_ input: String) -> CGSize {
        let out = input.split(separator: "x");
        let width = Int(out[0]) ?? 0;
        let height = Int(out[1]) ?? 0;
        return CGSize(width: width, height: height);
    }
}

extension Model {
    static func fromModelItems(list: [ModelItem]) -> [Model] {
        var models: [Model] = [];
        for item in list {
            let model: Model = Model(
                    id: item.id,
                    classType: CLASS(rawValue: item.class) ?? CLASS.AUTOPILOT_F,
                    type: TYPE(rawValue: item.type) ?? TYPE.AUTOPILOT,
                    name: item.name,
                    pathType: PATH_TYPE(rawValue: item.pathType) ?? PATH_TYPE.ASSET,
                    path: item.path,
                    inputSize: item.inputSize);
            models.append(model);
        }
        return models;
    }
}