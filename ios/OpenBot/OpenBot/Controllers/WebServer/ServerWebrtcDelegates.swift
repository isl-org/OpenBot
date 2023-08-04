//
// Created by Nitish Yadav on 25/07/23.
//

import Foundation
import AVFoundation
import Starscream
import WebRTC

/// function to create webRTC Delegate
class ServerWebrtcDelegate: WebRTCClientDelegate {
    var mSocket = NativeWebSocket.shared;
    var useCustomCapturer: Bool = true
    let webSocketMsgHandler = WebSocketMessageHandler();

    /// callback to check the generate candidate and send to the controller
    func didGenerateCandidate(iceCandidate: RTCIceCandidate) {
        sendCandidate(iceCandidate: iceCandidate)
    }

    /// callback to check whether the ice connection is changed.
    func didIceConnectionStateChanged(iceConnectionState: RTCIceConnectionState) {
        print("ice connection state is : ", iceConnectionState);
    }

    /// callback function to open data channel
    func didOpenDataChannel() {
        print("did open data channel")
    }

    /// callback function to check data is available
    func didReceiveData(data: Data) {
        print(Date().millisecondsSince1970)
        print("data received", data);
    }

    /// callback function message string received
    func didReceiveMessage(message: String) {
        print("message is : ", message);

    }

    func didConnectWebRTC() {
        print("didConnectWebRTC")
    }

    func didDisconnectWebRTC() {
        print("didDisconnectWebRTC")
    }

    /// initializing function
    init() {
        #if targetEnvironment(simulator)
        useCustomCapturer = true
        #endif
        webRTCClient = WebRTCClient()
        webRTCClient.delegate = self
        webRTCClient.setup(videoTrack: true, audioTrack: true, dataChannel: true, customFrameCapturer: useCustomCapturer)
        NotificationCenter.default.addObserver(self, selector: #selector(websocketDidReceiveMessage), name: .updateDataFromControllerApp, object: nil)
        if useCustomCapturer {
            _ = CameraController.shared.captureSession;
        }
        if !webRTCClient.isConnected {
            webRTCClient.connect(onSuccess: { (offerSDP: RTCSessionDescription) -> Void in
                self.sendSDP(sessionDescription: offerSDP)
            })
        }
    }

    /// function to send ICE candidate object to the controller
    ///
    /// - Parameter iceCandidate:
    func sendCandidate(iceCandidate: RTCIceCandidate) {
        let candidate = Candidate(candidate: iceCandidate.sdp, label: iceCandidate.sdpMLineIndex, id: iceCandidate.sdpMid!, type: "candidate");
        let signalingMessage = JSON.toString(CandidateEvent(status: .init(WEB_RTC_EVENT: candidate)));
        let data = signalingMessage.data(using: .utf8);

        mSocket.send(data: data!);
    }

    /// function to read the message from the controller connection
    @objc func websocketDidReceiveMessage(_ notification: Notification) {
        let msg = notification.object as! String
        let jsonDecoder = JSONDecoder();
        let text: Data = msg.data(using: .utf8)!;
        print("inside websocketDidReceiveMessage")
        if msg.contains("driveCmd") {
            let cmd = try! jsonDecoder.decode(serverMessage.self, from: text);
            print(cmd.driveCmd.l);
            self.webSocketMsgHandler.driveCommand(control: Control(left: cmd.driveCmd.l, right: cmd.driveCmd.r));
        } else if msg.contains("command") {
            let cmd = try! jsonDecoder.decode(serverCmd.self, from: text)
            print(cmd.command);
            switch cmd.command {
            case "INDICATOR_LEFT":
                self.webSocketMsgHandler.indicatorLeft()
                break;
            case "INDICATOR_RIGHT":
                self.webSocketMsgHandler.indicatorRight();
                break
            case "INDICATOR_STOP":
                self.webSocketMsgHandler.cancelIndicator();
            case "SPEED_DOWN":
                self.webSocketMsgHandler.speedDown();
                break;
            case "SPEED_UP":
                self.webSocketMsgHandler.speedUp();
                break;
            case "DRIVE_MODE":
                self.webSocketMsgHandler.driveMode()
            case "SWITCH_CAMERA":
                NotificationCenter.default.post(name: .switchCamera, object: nil);
                break;
            default:
                break;
            }
        } else {
            do {
                let signalingMessage = try JSONDecoder().decode(AnswerEvent.self, from: text)
                if signalingMessage.webrtc_event.type == "offer" {
                    webRTCClient.receiveOffer(offerSDP: RTCSessionDescription(type: .offer, sdp: (signalingMessage.webrtc_event.sdp)), onCreateAnswer: { (answerSDP: RTCSessionDescription) -> Void in
                        self.sendSDP(sessionDescription: answerSDP)
                    })
                } else if signalingMessage.webrtc_event.type == "answer" {
                    webRTCClient.receiveAnswer(answerSDP: RTCSessionDescription(type: .answer, sdp: (signalingMessage.webrtc_event.sdp)))
                } else if signalingMessage.webrtc_event.type == "candidate" {
                }

            } catch {
                print(error)
            }
        }
    }

    ///function to send the offer/answer SDP to the controller
    private func sendSDP(sessionDescription: RTCSessionDescription) {
        var type = ""
        if sessionDescription.type == .offer {
            type = "offer"
        } else if sessionDescription.type == .answer {
            type = "answer"
        }
        let signalingMessage = JSON.toString(OfferEvent(status: .init(WEB_RTC_EVENT: .init(type: type, sdp: sessionDescription.sdp))));
        let data = signalingMessage.data(using: .utf8);
        mSocket.send(data: data!);
    }
}
