//
// Created by Nitish Yadav on 09/01/23.
//

import Foundation
import AVFoundation
import Starscream
import WebRTC
var webRTCClient: WebRTCClient!
class VideoViewWebRtc: WebRTCClientDelegate, CameraSessionDelegate {
    var cameraDelegate: CameraSessionDelegate?

    // You can create video source from CMSampleBuffer :)
    var useCustomCapturer: Bool = true
    let webRTCStatusMesasgeBase = "WebRTC: "

    func didGenerateCandidate(iceCandidate: RTCIceCandidate) {
        self.sendCandidate(iceCandidate: iceCandidate)
    }

    func didIceConnectionStateChanged(iceConnectionState: RTCIceConnectionState) {
        var state = ""

        switch iceConnectionState {
        case .checking:
            state = "checking..."
        case .closed:
            state = "closed"
        case .completed:
            state = "completed"
        case .connected:
            state = "connected"
        case .count:
            state = "count..."
        case .disconnected:
            state = "disconnected"
        case .failed:
            state = "failed"
        case .new:
            state = "new..."
        @unknown default:
            state = "new..."
        }
    }

    func didOpenDataChannel() {
        print("did open data channel")
    }

    func didReceiveData(data: Data) {
        print(data);
    }

    func didReceiveMessage(message: String) {
        print("message is : ", message);

    }

    func didConnectWebRTC() {
        print("didConnectWebRTC")
    }

    func didDisconnectWebRTC() {
        print("didDisconnectWebRTC")
    }


    init() {
        #if targetEnvironment(simulator)
        self.useCustomCapturer = true
        #endif
        webRTCClient = WebRTCClient()
        webRTCClient.delegate = self
        cameraDelegate = self
        print("camera delegate is :", cameraDelegate)
        webRTCClient.setup(videoTrack: true, audioTrack: true, dataChannel: true, customFrameCapturer: useCustomCapturer)
        NotificationCenter.default.addObserver(self, selector: #selector(websocketDidReceiveMessage), name: .updateDataFromControllerApp, object: nil)
//        NotificationCenter.default.addObserver(self, selector: #selector(cameraBuffer), name: .cameraBuffer, object: nil)
        if useCustomCapturer {
            print("--- use custom capturer ---")
            let tempCameraSession = CameraController.shared.captureSession;
            CameraController.shared.delegate = self;
        }
        if !webRTCClient.isConnected {
            webRTCClient.connect(onSuccess: { (offerSDP: RTCSessionDescription) -> Void in
                self.sendSDP(sessionDescription: offerSDP)
            })
        }
    }

    func sendCandidate(iceCandidate: RTCIceCandidate) {
        let candidate = Candidate(candidate: iceCandidate.sdp, label: iceCandidate.sdpMLineIndex, id: iceCandidate.sdpMid!, type: "candidate");
        let signalingMessage = JSON.toString(CandidateEvent(status: .init(WEB_RTC_EVENT: candidate)));
        client.send(message: signalingMessage);
    }

    @objc func websocketDidReceiveMessage(_ notification: Notification) {
        print("inside websocketDidReceiveMessage")
        let text: Data = notification.object as! Data;
        do {
            let signalingMessage = try JSONDecoder().decode(AnswerEvent.self, from: text)
            print("signalingMessage", signalingMessage)
            if signalingMessage.webrtc_event.type == "offer" {
                webRTCClient.receiveOffer(offerSDP: RTCSessionDescription(type: .offer, sdp: (signalingMessage.webrtc_event.sdp)), onCreateAnswer: { (answerSDP: RTCSessionDescription) -> Void in
                    self.sendSDP(sessionDescription: answerSDP)
                })
            } else if signalingMessage.webrtc_event.type == "answer" {
                print("it is answer");
                webRTCClient.receiveAnswer(answerSDP: RTCSessionDescription(type: .answer, sdp: (signalingMessage.webrtc_event.sdp)))
            } else if signalingMessage.webrtc_event.type == "candidate" {
//                    let candidate = signalingMessage.candidate!
//                    webRTCClient.receiveCandidate(candidate: RTCIceCandidate(sdp: candidate.candidate, sdpMLineIndex: candidate.label, sdpMid: candidate.id))

            }
        } catch {
            print(error)

        }

    }

    private func sendSDP(sessionDescription: RTCSessionDescription) {
        var type = ""
        if sessionDescription.type == .offer {
            type = "offer"
        } else if sessionDescription.type == .answer {
            type = "answer"
        }
        let signalingMessage = JSON.toString(OfferEvent(status: .init(WEB_RTC_EVENT: .init(type: type, sdp: sessionDescription.sdp))));
        client.send(message: signalingMessage);
    }


    @objc func cameraBuffer(_ notification: Notification) {
        if self.useCustomCapturer {
            if let cvpixelBuffer = CMSampleBufferGetImageBuffer(notification.object as! CMSampleBuffer) {
                webRTCClient.captureCurrentFrame(sampleBuffer: cvpixelBuffer)
            } else {
                print("no pixelbuffer")
            }
            //            self.webRTCClient.captureCurrentFrame(sampleBuffer: buffer)
        }
    }


    func didOutput(_ sampleBuffer: CMSampleBuffer) {
        print("didOutput delegate");
        if self.useCustomCapturer {

            if let cvpixelBuffer = CMSampleBufferGetImageBuffer(sampleBuffer) {
                webRTCClient.captureCurrentFrame(sampleBuffer: cvpixelBuffer)
            } else {
                print("no pixelbuffer")
            }
        }
    }

}
