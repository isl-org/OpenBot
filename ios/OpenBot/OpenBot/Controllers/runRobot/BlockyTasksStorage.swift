//
//  BlocklyTaskStorage.swift
//  OpenBot
//
//  Created by Hardik Garg on 07/09/23.
//

import Foundation

class taskStorage {

    static var taskArray: [[String: [[String: Any]]]] = []

    static func addAttribute(classType: String, task: String, frames: Int, type: String) {
        if let index = taskArray.firstIndex(where: { $0.keys.contains(classType) }) {
            if var existingDict = taskArray[index][classType] {
                if type != "detect" {
                    existingDict.append(["frames": frames])
                }
                existingDict.append([type: task])
                taskArray[index][classType] = existingDict
            }
        } else {
            var dictArray: [[String: Any]] = []
            if type != "detect" {
                dictArray.append(["frames": frames])
            }
            dictArray.append([type: task])
            taskArray.append([classType: dictArray])
        }
    }

    static func getValueOfAttribute(classType: String, type: String) -> Any? {
        for i in taskArray {
            if let value = i[classType] {
                for j in value {
                    if let innerValue = j[type] {
                        return innerValue;
                    }
                }
            }
        }
        return nil;
    }

    static func returnAttributeArray() -> [[String: [[String: Any]]]] {
        taskArray
    }

}
