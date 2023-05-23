//
// Created by Nitish Yadav on 13/04/23.
//

import Foundation
import Foundation

public struct ProjectItem: Codable {
    var projectName: String
    var projectDate: String
    var projectId: String
}

public struct ProjectData: Codable {
    var projectId: String
    var projectCommand: String
}

public struct QrData: Codable {
    let driveLink: String
    let projectName: String
}


