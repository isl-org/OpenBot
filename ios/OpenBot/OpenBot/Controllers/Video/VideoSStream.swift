//
// Created by Nitish Yadav on 03/01/23.
//

import Foundation
import AVFoundation
import Starscream
import WebRTC

class VideoFragment: UIViewController, WebRTCClientDelegate, CameraSessionDelegate {

    var webRTCClient: WebRTCClient!
    var cameraSession: CameraSession?

    // You can create video source from CMSampleBuffer :)
    var useCustomCapturer: Bool = false
    var cameraFilter: CameraFilter?
    let webRTCStatusMesasgeBase = "WebRTC: "
    // UI
    var wsStatusLabel: UILabel!
    var webRTCStatusLabel: UILabel!
    var webRTCMessageLabel: UILabel!

    //MARK: - ViewController Override Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        #if targetEnvironment(simulator)
        // simulator does not have camera
        self.useCustomCapturer = false
        #endif
        webRTCClient = WebRTCClient()
        webRTCClient.delegate = self
        webRTCClient.setup(videoTrack: true, audioTrack: true, dataChannel: true, customFrameCapturer: useCustomCapturer)

        if useCustomCapturer {
            print("--- use custom capturer ---")
            self.cameraSession = CameraSession()
            self.cameraSession?.delegate = self
            self.cameraSession?.setupSession()
            self.cameraFilter = CameraFilter()
        }
        if !webRTCClient.isConnected {
            webRTCClient.connect(onSuccess: { (offerSDP: RTCSessionDescription) -> Void in
                self.sendSDP(sessionDescription: offerSDP)
            })
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        self.setupUI()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    // MARK: - UI
    private func setupUI(){
        let remoteVideoViewContainter = UIView(frame: CGRect(x: 0, y: 0, width: ScreenSizeUtil.width(), height: ScreenSizeUtil.height()*0.7))
        remoteVideoViewContainter.backgroundColor = .gray
        self.view.addSubview(remoteVideoViewContainter)

        let remoteVideoView = webRTCClient.remoteVideoView()
        webRTCClient.setupRemoteViewFrame(frame: CGRect(x: 0, y: 0, width: ScreenSizeUtil.width()*0.7, height: ScreenSizeUtil.height()*0.7))
        remoteVideoView.center = remoteVideoViewContainter.center
        remoteVideoViewContainter.addSubview(remoteVideoView)

        let localVideoView = webRTCClient.localVideoView()
        webRTCClient.setupLocalViewFrame(frame: CGRect(x: 0, y: 0, width: ScreenSizeUtil.width()/3, height: ScreenSizeUtil.height()/3))
        localVideoView.center.y = self.view.center.y
        localVideoView.subviews.last?.isUserInteractionEnabled = true
        self.view.addSubview(localVideoView)

        let localVideoViewButton = UIButton(frame: CGRect(x: 0, y: 0, width: localVideoView.frame.width, height: localVideoView.frame.height))
        localVideoViewButton.backgroundColor = UIColor.clear
        localVideoViewButton.addTarget(self, action: #selector(self.localVideoViewTapped(_:)), for: .touchUpInside)
        localVideoView.addSubview(localVideoViewButton)

        wsStatusLabel = UILabel(frame: CGRect(x: 0, y: remoteVideoViewContainter.bottom, width: ScreenSizeUtil.width(), height: 30))
        wsStatusLabel.textAlignment = .center
        self.view.addSubview(wsStatusLabel)
        webRTCStatusLabel = UILabel(frame: CGRect(x: 0, y: wsStatusLabel.bottom, width: ScreenSizeUtil.width(), height: 30))
        webRTCStatusLabel.textAlignment = .center
        webRTCStatusLabel.text = webRTCStatusMesasgeBase + "initialized"
        self.view.addSubview(webRTCStatusLabel)
        webRTCMessageLabel = UILabel(frame: CGRect(x: 0, y: webRTCStatusLabel.bottom, width: ScreenSizeUtil.width(), height: 30))
        webRTCMessageLabel.textAlignment = .center
        webRTCMessageLabel.textColor = .black
        self.view.addSubview(webRTCMessageLabel)

    }

    // MARK: - UI Events
    @objc func callButtonTapped(_ sender: UIButton){
        if !webRTCClient.isConnected {
            webRTCClient.connect(onSuccess: { (offerSDP: RTCSessionDescription) -> Void in
                self.sendSDP(sessionDescription: offerSDP)
            })
        }
    }


    @objc func localVideoViewTapped(_ sender: UITapGestureRecognizer) {
        webRTCClient.switchCameraPosition()
    }

    // MARK: - WebRTC Signaling
    private func sendSDP(sessionDescription: RTCSessionDescription){
        var type = ""
        if sessionDescription.type == .offer {
            type = "offer"
        }else if sessionDescription.type == .answer {
            type = "answer"
        }

        let sdp = SDP.init(sdp: sessionDescription.sdp)
        let signalingMessage = SignalingMessage.init(type: type, sessionDescription: sdp, candidate: nil)
        do {
            let data = try JSONEncoder().encode(signalingMessage)
            let message = String(data: data, encoding: String.Encoding.utf8)!
            let messageToSend =    "{\"status\":{\"WEB_RTC_EVENT\"" + message;
            client.send(message: messageToSend);
        }catch{
            print(error)
        }
    }

    private func sendCandidate(iceCandidate: RTCIceCandidate){
        let candidate = Candidate.init(sdp: iceCandidate.sdp, sdpMLineIndex: iceCandidate.sdpMLineIndex, sdpMid: iceCandidate.sdpMid!)
        let signalingMessage = SignalingMessage.init(type: "candidate", sessionDescription: nil, candidate: candidate)
        do {
            let data = try JSONEncoder().encode(signalingMessage)
            let message = String(data: data, encoding: String.Encoding.utf8)!
            client.send(message: message);
        }catch{
            print(error)
        }
    }

}

// MARK: - WebSocket Delegate
extension VideoFragment {

    func websocketDidReceiveMessage(socket: WebSocketClient, text: String) {

        do{
            let signalingMessage = try JSONDecoder().decode(SignalingMessage.self, from: text.data(using: .utf8)!)

            if signalingMessage.type == "offer" {
                webRTCClient.receiveOffer(offerSDP: RTCSessionDescription(type: .offer, sdp: (signalingMessage.sessionDescription?.sdp)!), onCreateAnswer: {(answerSDP: RTCSessionDescription) -> Void in
                    self.sendSDP(sessionDescription: answerSDP)
                })
            }else if signalingMessage.type == "answer" {
                webRTCClient.receiveAnswer(answerSDP: RTCSessionDescription(type: .answer, sdp: (signalingMessage.sessionDescription?.sdp)!))
            }else if signalingMessage.type == "candidate" {
                let candidate = signalingMessage.candidate!
                webRTCClient.receiveCandidate(candidate: RTCIceCandidate(sdp: candidate.sdp, sdpMLineIndex: candidate.sdpMLineIndex, sdpMid: candidate.sdpMid))
            }
        }catch{
            print(error)
        }

    }

    func websocketDidReceiveData(socket: WebSocketClient, data: Data) { }
}

// MARK: - WebRTCClient Delegate
extension VideoFragment {
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
        }
        self.webRTCStatusLabel.text = self.webRTCStatusMesasgeBase + state
    }

    func didConnectWebRTC() {
        self.webRTCStatusLabel.textColor = .green
    }

    func didDisconnectWebRTC() {
        self.webRTCStatusLabel.textColor = .red
    }

    func didOpenDataChannel() {
        print("did open data channel")
    }

    func didReceiveData(data: Data) {
        print(data);
    }

    func didReceiveMessage(message: String) {
        self.webRTCMessageLabel.text = message
    }
}

extension VideoFragment {
    func didOutput(_ sampleBuffer: CMSampleBuffer) {
        if self.useCustomCapturer {
            if let cvpixelBuffer = CMSampleBufferGetImageBuffer(sampleBuffer){
                if let buffer = self.cameraFilter?.apply(cvpixelBuffer){
                    self.webRTCClient.captureCurrentFrame(sampleBuffer: buffer)
                }else{
                    print("no applied image")
                }
            }else{
                print("no pixelbuffer")
            }
        }
    }
}
