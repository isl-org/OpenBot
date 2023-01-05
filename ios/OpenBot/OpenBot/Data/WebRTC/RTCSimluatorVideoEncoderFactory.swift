//
// Created by Nitish Yadav on 03/01/23.
//

import Foundation
import WebRTC

class RTCSimluatorVideoEncoderFactory: RTCDefaultVideoEncoderFactory {

    override init() {
        super.init()
    }

    override class func supportedCodecs() -> [RTCVideoCodecInfo] {
        var codecs = super.supportedCodecs()
        codecs = codecs.filter {
            $0.name != "H264"
        }
        return codecs
    }
}
