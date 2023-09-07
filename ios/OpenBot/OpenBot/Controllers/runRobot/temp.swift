//
// Created by Hardik Garg on 06/09/23.
//

import Foundation

class taskStorage {

    static var array: [[String: String]] = [];

    static func addAttribute(classType: String, task: String) {
        let taskDict = [classType: task]
        array.append(taskDict)
    }

    static func removeAttribute(classTye: String) {
        array.removeAll { item in
            item.keys.contains(classTye)
        }
    }

    static func getValueOfAttribute(classType: String) -> String? {
        for i in array {
            if let value = i[classType] {
                return value;
            }
        }
        return nil;
    }

    static func returnAttributeArray() -> [[String: String]] {
        array
    }

}