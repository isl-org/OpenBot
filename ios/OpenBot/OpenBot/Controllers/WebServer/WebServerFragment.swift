//
// Created by Nitish Yadav on 25/07/23.
//

import Foundation
import UIKit

class WebServerFragment: UIViewController {
    var mSocket = WebSocketManager.shared.socket
    override func viewDidLoad() {
        super.viewDidLoad();
        // Listen for socket connection status changes
        mSocket?.on(clientEvent: .connect) { (dataArray, ack) in
            print("Socket connected.")
            // Emit events or perform other actions that require a connection
        }

        mSocket?.on(clientEvent: .disconnect) { (dataArray, ack) in
            print("Socket disconnected.")
            // Handle disconnection if needed
        }

        mSocket?.on("counter") { (dataArray, ack) in
            let dataReceived = dataArray[0] as? Int
            print("data received", dataReceived ?? "No data received")
            if let dataReceived = dataReceived {

            }
        }
        mSocket?.on("chatMessage", callback: {(dataArray,ack) in
            let dataReceived = dataArray[0]
            print("data received", dataReceived ?? "No data received")
        })

        WebSocketManager.shared.connectSocket()
    }

    @IBAction func sendCommand(_ sender: Any) {
        sendMessage();
        ServerWebrtcDelegate();
    }

    /// function to parse the message for the connection and send it.
    func sendMessage() {
        var msg = JSON.toString(ConnectionActiveEvent(status: .init(CONNECTION_ACTIVE: "true")));
        mSocket?.emit("msg", msg);
        msg = JSON.toString(VideoProtocolEvent(status: .init(VIDEO_PROTOCOL: "WEBRTC")));
        mSocket?.emit("msg", msg);
        msg = JSON.toString(VideoServerUrlEvent(status: .init(VIDEO_SERVER_URL: "")));
        mSocket?.emit("msg", msg);
        msg = JSON.toString(VideoCommandEvent(status: .init(VIDEO_COMMAND: "START")));
        mSocket?.emit("msg", msg);
        msg = JSON.toString(VehicleStatusEvent(status: .init(LOGS: false, NOISE: false, NETWORK: false, DRIVE_MODE: "GAME", INDICATOR_LEFT: false, INDICATOR_RIGHT: false, INDICATOR_STOP: true)));
        mSocket?.emit("msg", msg);
    }

}
