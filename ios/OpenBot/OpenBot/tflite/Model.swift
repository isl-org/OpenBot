//
// Created by Sparsh Jain on 29/09/22.
//

import Foundation

class Model {
    var id: Int;
    var classType: CLASS;
    var type: TYPE;
    var name: String;
    var pathType: PATH_TYPE;
    var path: String;
    private var inputSize: String;

    init(id: Int, classType: CLASS, type: TYPE, name: String, pathType: PATH_TYPE, path: String, inputSize: String) {
        self.id = id;
        self.classType = classType;
        self.type = type;
        self.name = name;
        self.pathType = pathType;
        self.path = path;
        self.inputSize = inputSize;
    }

    enum CLASS {
        case AUTOPILOT_F,
             MOBILENETV1_1_0_Q,
             MOBILENETV3_S_Q,
             YOLOV4,
             NAVIGATION
    }

    enum TYPE {
        case AUTOPILOT,
             DETECTOR,
             NAVIGATION
    }

    enum PATH_TYPE {
        case URL,
             ASSET,
             FILE
    }

    func getName() -> String {
        name;
    }

    func getInputSize() -> CGSize {
        CGSize().parseSize(inputSize);
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
    func parseSize(_ input: String) -> CGSize {
        let out = input.split(separator: "X");
        let width = Double(out[0]) ?? 0;
        let height = Double(out[1]) ?? 0;
        return CGSize(width: width, height: height);
    }
}