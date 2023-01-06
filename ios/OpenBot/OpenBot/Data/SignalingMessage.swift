//
// Created by Nitish Yadav on 03/01/23.
//

import Foundation

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

struct StatusEvent: Codable {
    var status: String
}

struct ConnectionActiveEvent: Codable {
    var CONNECTION_ACTIVE: String
}

struct VideoProtocolEvent: Codable {
    var VIDEO_PROTOCOL: String
}

struct VideoServerUrlEvent: Codable {
    var VIDEO_SERVER_URL: String
}

struct VideoCommandEvent: Codable {
    var VIDEO_COMMAND: String
}

struct VehicleStatusEvent: Codable {
    var LOGS: Bool
    var NOISE: Bool
    var NETWORK: Bool
    var DRIVE_MODE: String
    var INDICATOR_LEFT: Bool
    var INDICATOR_RIGHT: Bool
    var INDICATOR_STOP: Bool
}

struct OfferEvent: Codable {
    var type: String
    var sdp: String
}

struct WebRTCEvent: Codable {
    var WEB_RTC_EVENT: String
}
