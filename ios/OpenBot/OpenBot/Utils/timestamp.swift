//
// Created by Nitish Yadav on 27/09/22.
//

import Foundation
func returnCurrentTimestamp() -> Int {
    Int(ProcessInfo.processInfo.systemUptime * 1000000)
}