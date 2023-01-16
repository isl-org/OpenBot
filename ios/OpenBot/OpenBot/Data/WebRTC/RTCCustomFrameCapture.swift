//
// Created by Nitish Yadav on 03/01/23.
//

import Foundation
import WebRTC

class RTCCustomFrameCapturer: RTCVideoCapturer {

    let kNanosecondsPerSecond: Float64 = 1000000000
    var nanoseconds: Float64 = 0

    override init(delegate: RTCVideoCapturerDelegate) {
        super.init(delegate: delegate)
    }

    public func capture(_ sampleBuffer: CMSampleBuffer) {
        let _pixelBuffer = CMSampleBufferGetImageBuffer(sampleBuffer);
        if let pixelBuffer = _pixelBuffer {
            let rtcPixelBuffer = RTCCVPixelBuffer(pixelBuffer: pixelBuffer)
            let timeStampNs = CMTimeGetSeconds(CMSampleBufferGetPresentationTimeStamp(sampleBuffer)) * kNanosecondsPerSecond
            let rtcVideoFrame: RTCVideoFrame!
            switch currentOrientation {
            case .portrait:
                rtcVideoFrame = RTCVideoFrame(buffer: rtcPixelBuffer, rotation: RTCVideoRotation._0, timeStampNs: Int64(timeStampNs))
            case .unknown:
                rtcVideoFrame = RTCVideoFrame(buffer: rtcPixelBuffer, rotation: RTCVideoRotation._0, timeStampNs: Int64(timeStampNs))
            case .portraitUpsideDown:
                rtcVideoFrame = RTCVideoFrame(buffer: rtcPixelBuffer, rotation: RTCVideoRotation._0, timeStampNs: Int64(timeStampNs))

            case .landscapeLeft:
                rtcVideoFrame = RTCVideoFrame(buffer: rtcPixelBuffer, rotation: RTCVideoRotation._270, timeStampNs: Int64(timeStampNs))

            case .landscapeRight:
                rtcVideoFrame = RTCVideoFrame(buffer: rtcPixelBuffer, rotation: RTCVideoRotation._90, timeStampNs: Int64(timeStampNs))

            @unknown default:
                rtcVideoFrame = RTCVideoFrame(buffer: rtcPixelBuffer, rotation: RTCVideoRotation._0, timeStampNs: Int64(timeStampNs))

            }
            self.delegate?.capturer(self, didCapture: rtcVideoFrame)
        }
    }

    public func capture(_ pixelBuffer: CVPixelBuffer) {
        let rtcPixelBuffer = RTCCVPixelBuffer(pixelBuffer: pixelBuffer)
        let timeStampNs = nanoseconds * kNanosecondsPerSecond
        let rtcVideoFrame = RTCVideoFrame(buffer: rtcPixelBuffer, rotation: RTCVideoRotation._0, timeStampNs: Int64(timeStampNs))
        self.delegate?.capturer(self, didCapture: rtcVideoFrame)
        nanoseconds += 1
    }
}

