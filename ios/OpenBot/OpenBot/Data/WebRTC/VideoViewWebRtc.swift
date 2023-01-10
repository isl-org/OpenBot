//
// Created by Nitish Yadav on 09/01/23.
//

import Foundation
import AVFoundation
import Starscream
import WebRTC

class VideoViewWebRtc:  WebRTCClientDelegate, CameraSessionDelegate {

    var webRTCClient: WebRTCClient!
    var cameraSession: CameraSession?

    // You can create video source from CMSampleBuffer :)
    var useCustomCapturer: Bool = false
    var cameraFilter: CameraFilter?
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
        print("message is : ",message);

    }

    func didConnectWebRTC() {
        print("didConnectWebRTC")
    }

    func didDisconnectWebRTC() {
        print("didDisconnectWebRTC")
    }

    func didOutput(_ sampleBuffer: CMSampleBuffer) {
        print("inside didOutput");
        if self.useCustomCapturer {
            if let cvpixelBuffer = CMSampleBufferGetImageBuffer(sampleBuffer) {
                self.webRTCClient.captureCurrentFrame(sampleBuffer: cvpixelBuffer)
//                if let buffer = self.cameraFilter?.apply(cvpixelBuffer) {
//
//                } else {
//                    print("no applied image")
//                }
            } else {
                print("no pixelbuffer")
            }
        }
    }
    init(){
        #if targetEnvironment(simulator)
        // simulator does not have camera
        self.useCustomCapturer = false
        #endif
        webRTCClient = WebRTCClient()
        webRTCClient.delegate = self
        webRTCClient.setup(videoTrack: true, audioTrack: true, dataChannel: true, customFrameCapturer: useCustomCapturer)
        NotificationCenter.default.addObserver(self, selector: #selector(websocketDidReceiveMessage), name: .updateDataFromControllerApp, object: nil)
        if useCustomCapturer {
            print("--- use custom capturer ---")
//            self.cameraSession = CameraSession()
            self.cameraSession?.delegate = self
            self.cameraSession?.setupSession()
           let tempCameraSession = CameraController();
            tempCameraSession.setupLivePreview()
            tempCameraSession.initializeCamera();

//            self.cameraFilter = CameraFilter()
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
            print("signalingMessage",signalingMessage)
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

}
