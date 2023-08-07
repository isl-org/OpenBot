//
// Created by Nitish Yadav on 25/07/23.
//

import Foundation
import UIKit
import AVFoundation
import FirebaseAuth
class WebServerFragment: CameraController {
    var gameController = GameController.shared
    @IBOutlet weak var CommandLabel: UILabel!
    let mSocket = NativeWebSocket.shared;
    let roomId : String =  Auth.auth().currentUser?.email ?? ""
    override func viewDidLoad() {
        super.viewDidLoad();
        self.CommandLabel.textColor = .red;
        CommandLabel.textAlignment = .center;
        NotificationCenter.default.addObserver(self, selector: #selector(websocketDidReceiveMessage), name: .updateDataFromControllerApp, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(flipCamera), name: .switchCamera, object: nil);
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        sendMessage();
        _ = ServerWebrtcDelegate();
    }

    /// function to parse the message for the connection and send it.
    func sendMessage() {
        var msg = JSON.toString(ServerConnectionActiveEvent(status: .init(CONNECTION_ACTIVE: "true"), roomId: roomId));
        print(msg);
        var data = msg.data(using: .utf8);
        mSocket.send(data: data!);
        msg = JSON.toString(ServerVideoProtocolEvent(status: .init(VIDEO_PROTOCOL: "WEBRTC"), roomId: roomId));
        data = msg.data(using: .utf8);
        mSocket.send(data: data!);
        msg = JSON.toString(ServerVideoServerUrlEvent(status: .init(VIDEO_SERVER_URL: ""), roomId: roomId));
        data = msg.data(using: .utf8);
        mSocket.send(data: data!);
        msg = JSON.toString(ServerVideoCommandEvent(status: .init(VIDEO_COMMAND: "START"), roomId: roomId));
        data = msg.data(using: .utf8);
        mSocket.send(data: data!);
        msg = JSON.toString(ServerVehicleStatusEvent(status: .init(LOGS: false, NOISE: false, NETWORK: false, DRIVE_MODE: "GAME", INDICATOR_LEFT: false, INDICATOR_RIGHT: false, INDICATOR_STOP: true), roomId: roomId));
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

    @objc func websocketDidReceiveMessage(_ notification: Notification) {
        let msg = notification.object as! String
        DispatchQueue.main.async {
            self.CommandLabel.text = msg
        }

    }


}

struct serverMessage: Decodable {
    var driveCmd: serverCommand
    var roomId: String
}

struct serverCommand: Decodable {
    var l: Float
    var r: Float
}

struct serverCmd: Decodable {
    var command: String
    var roomId: String
}


