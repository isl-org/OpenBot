//
// Created by Nitish Yadav on 27/09/22.
//

import Foundation

/**
 return the amount of time the system has been awake since the last time it was restarted in nanosecond.
 **/
func returnCurrentTimestamp() -> Int {
    Int(ProcessInfo.processInfo.systemUptime * 1000000)
}