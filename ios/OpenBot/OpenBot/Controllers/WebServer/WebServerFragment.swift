//
// Created by Nitish Yadav on 25/07/23.
//

import Foundation
import UIKit
class WebServerFragment : UIViewController,WebSocketManagerDelegate{
    var webSocketManager: WebSocketManager!
    override func viewDidLoad() {
        super.viewDidLoad();
        webSocketManager = WebSocketManager()
        webSocketManager.delegate = self
        webSocketManager.connect()
    }

    func websocketDidConnect() {
        print("WebSocket connected.")
    }

    func websocketDidDisconnect(error: Error?) {
        print("WebSocket disconnected.")
    }

    func websocketDidReceiveMessage(text: String) {
        print("Received message: \(text)")
    }
    
    @IBAction func sendCommand(_ sender: Any) {
        webSocketManager.sendMessage(text: "Hello from iOS!")
        print(webSocketManager.socket?.connect());
    }
    
    
}
