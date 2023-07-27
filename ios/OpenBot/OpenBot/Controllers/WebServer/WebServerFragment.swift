//
// Created by Nitish Yadav on 25/07/23.
//

import Foundation
import UIKit

class WebServerFragment: UIViewController {
    var mSocket = WebSocketManager.shared.socket
    var gameController = GameController.shared
    let webSocketMessageHandler = WebSocketMessageHandler()
    @IBOutlet weak var CommandLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad();
        self.CommandLabel.textColor = .red;
        CommandLabel.textAlignment = .center;
        // Listen for socket connection status changes
        mSocket?.on(clientEvent: .connect) { (dataArray, ack) in
            print("Socket connected.")

            // Emit events or perform other actions that require a connection
        }

        mSocket?.on(clientEvent: .disconnect) { (dataArray, ack) in
            print("Socket disconnected.")
            // Handle disconnection if needed
        }

        mSocket?.on("driveCmd", callback: { (dataArray, ack) in
            let dataReceived = dataArray[0] as! String
            do {
                print("dataReceived ================= ", dataReceived);
                let jsonData = Data(dataReceived.utf8)

                let decoder = JSONDecoder()
                let message = try decoder.decode(serverMessage.self, from: jsonData)
                print("got message at timestamp =============",Date().millisecondsSince1970)
                // Now you can access the decoded data
                print("Left value:", message.driveCmd.l)
                print("Right value:", message.driveCmd.r)
                DispatchQueue.main.async {
                    self.CommandLabel.text = dataReceived;
                }
                self.webSocketMessageHandler.driveCommand(control:Control(left: message.driveCmd.l, right: message.driveCmd.r));
            } catch {
                print("Error decoding JSON:", error)
            }
            print("data received", dataReceived ?? "No data received")
        })

        mSocket?.on("cmd", callback: { (dataArray, ack) in
            let dataReceived = dataArray[0] as! String
            print(dataReceived)
           DispatchQueue.main.async {
               self.CommandLabel.text = dataReceived;
           }
            switch dataReceived {
            case  "INDICATOR_LEFT" :
                self.webSocketMessageHandler.indicatorLeft()
                break;
            case "INDICATOR_RIGHT" :
                self.webSocketMessageHandler.indicatorRight();
                break
            case "INDICATOR_STOP" :
                self.webSocketMessageHandler.cancelIndicator();
            case "SPEED_DOWN" :
                self.webSocketMessageHandler.speedDown();
                break;
            case "SPEED_UP" :
                self.webSocketMessageHandler.speedUp();
                break;
            case "DRIVE_MODE" :
                self.webSocketMessageHandler.driveMode();
                break
            default:
                break;
            }
        })

        WebSocketManager.shared.connectSocket()
    }
}

struct serverMessage: Decodable {
    var driveCmd: serverCommand
}

struct serverCommand: Decodable {
    var l: Float
    var r: Float
}
