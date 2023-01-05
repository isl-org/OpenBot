//
// Created by Nitish Yadav on 03/01/23.
//

import Foundation

struct SignalingSDP: Codable {
    let type: String
    let sdp: String
}

struct SignalingCandidate: Codable {
    let type: String
    let candidate: Candidate
}

struct SignalingMessage: Codable {
    let type: String
    let sdp: String
    let candidate: Candidate?
}



struct Candidate: Codable {
    var candidate: String
    var label: Int32
    var id: String
    var type: String
}

