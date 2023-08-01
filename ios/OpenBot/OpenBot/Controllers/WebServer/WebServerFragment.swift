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
//        // Listen for socket connection status changes
//        mSocket?.on(clientEvent: .connect) { (dataArray, ack) in
//            print("Socket connected.")
//            // Emit events or perform other actions that require a connection
//        }
//
//        mSocket?.on(clientEvent: .disconnect) { (dataArray, ack) in
//            print("Socket disconnected.")
//            // Handle disconnection if needed
//        }
//
//        mSocket?.on("driveCmd", callback: { (dataArray, ack) in
//            let dataReceived = dataArray[0] as! String
//            do {
//                print("dataReceived ================= ", dataReceived);
//                let jsonData = Data(dataReceived.utf8)
//
//                let decoder = JSONDecoder()
//                let message = try decoder.decode(serverMessage.self, from: jsonData)
//                print("got message at timestamp =============",Date().millisecondsSince1970)
//                // Now you can access the decoded data
//                print("Left value:", message.driveCmd.l)
//                print("Right value:", message.driveCmd.r)
//                DispatchQueue.main.async {
//                    self.CommandLabel.text = dataReceived;
//                }
//                self.webSocketMessageHandler.driveCommand(control:Control(left: message.driveCmd.l, right: message.driveCmd.r));
//            } catch {
//                print("Error decoding JSON:", error)
//            }
//            print("data received", dataReceived ?? "No data received")
//        })
//
//        mSocket?.on("cmd", callback: { (dataArray, ack) in
//            let dataReceived = dataArray[0] as! String
//            print(dataReceived)
//           DispatchQueue.main.async {
//               self.CommandLabel.text = dataReceived;
//           }
//            switch dataReceived {
//            case  "INDICATOR_LEFT" :
//                self.webSocketMessageHandler.indicatorLeft()
//                break;
//            case "INDICATOR_RIGHT" :
//                self.webSocketMessageHandler.indicatorRight();
//                break
//            case "INDICATOR_STOP" :
//                self.webSocketMessageHandler.cancelIndicator();
//            case "SPEED_DOWN" :
//                self.webSocketMessageHandler.speedDown();
//                break;
//            case "SPEED_UP" :
//                self.webSocketMessageHandler.speedUp();
//                break;
//            case "DRIVE_MODE" :
//                self.webSocketMessageHandler.driveMode();
//                break
//            default:
//                break;
//            }
//        })
//
//        mSocket?.on("stream") { (data, ack) in
//            if let imageData = data[0] as? Data {
//                if let image = UIImage(data: imageData) {
//                    // Now you can use this image
//                    print("hello image");
//                }
//            }
//        }
//
//        WebSocketManager.shared.connectSocket()
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
//        print(webRTCClient.isConnected);
        if webRTCClient.isConnected {
            webRTCClient.captureCurrentFrame(sampleBuffer: sampleBuffer)
        }

    }
}


struct serverMessage: Decodable {
    var driveCmd: serverCommand
}

struct serverCommand: Decodable {
    var l: Float
    var r: Float
}
