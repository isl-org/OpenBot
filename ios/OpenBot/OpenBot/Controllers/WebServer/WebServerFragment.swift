//
// Created by Nitish Yadav on 25/07/23.
//

import Foundation
import UIKit
import AVFoundation

class WebServerFragment: CameraController {
    var gameController = GameController.shared
    @IBOutlet weak var CommandLabel: UILabel!
    let mSocket = NativeWebSocket.shared;

    override func viewDidLoad() {
        super.viewDidLoad();
        self.CommandLabel.textColor = .red;
        CommandLabel.textAlignment = .center;
        NotificationCenter.default.addObserver(self, selector: #selector(flipCamera), name: .switchCamera, object: nil);
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        sendMessage();
        ServerWebrtcDelegate();
    }

    /// function to parse the message for the connection and send it.
    func sendMessage() {
        var msg = JSON.toString(ConnectionActiveEvent(status: .init(CONNECTION_ACTIVE: "true")));
        var data = msg.data(using: .utf8);
        mSocket.send(data: data!);
        msg = JSON.toString(VideoProtocolEvent(status: .init(VIDEO_PROTOCOL: "WEBRTC")));
        data = msg.data(using: .utf8);
        mSocket.send(data: data!);
        msg = JSON.toString(VideoServerUrlEvent(status: .init(VIDEO_SERVER_URL: "")));
        data = msg.data(using: .utf8);
        mSocket.send(data: data!);
        msg = JSON.toString(VideoCommandEvent(status: .init(VIDEO_COMMAND: "START")));
        data = msg.data(using: .utf8);
        mSocket.send(data: data!);
        msg = JSON.toString(VehicleStatusEvent(status: .init(LOGS: false, NOISE: false, NETWORK: false, DRIVE_MODE: "GAME", INDICATOR_LEFT: false, INDICATOR_RIGHT: false, INDICATOR_STOP: true)));
        data = msg.data(using: .utf8);
        mSocket.send(data: data!);
    }

    override func captureOutput(_ output: AVFoundation.AVCaptureOutput, didOutput sampleBuffer: CoreMedia.CMSampleBuffer, from connection: AVFoundation.AVCaptureConnection) {
        if webRTCClient.isConnected {
            webRTCClient.captureCurrentFrame(sampleBuffer: sampleBuffer)
        }

    }

    @objc func flipCamera(_ notification: NSNotification) {
            switchCameraView()
    }
}


struct serverMessage: Decodable {
    var driveCmd: serverCommand
}

struct serverCommand: Decodable {
    var l: Float
    var r: Float
}

struct serverCmd: Decodable {
    var command: String
}

