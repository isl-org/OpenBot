//
// Created by Nitish Yadav on 25/07/23.
//

import Foundation
import Starscream

protocol WebSocketManagerDelegate: AnyObject {
    func websocketDidConnect()
    func websocketDidDisconnect(error: Error?)
    func websocketDidReceiveMessage(text: String)
}

class WebSocketManager {
    weak var delegate: WebSocketManagerDelegate?
    var socket: WebSocket?
    var isConnected : Bool = false
    func connect() {
        let urlString = "wss://echo.websocket.org" // Replace this with your server URL
        if let url = URL(string: urlString) {
            let request = URLRequest(url: url)
            socket = WebSocket(request: request)
            print("socket is =============", socket?.request);
            socket?.delegate = self
            socket?.connect()
            
        }
    }

    func disconnect() {
        socket?.disconnect()
    }

    func sendMessage(text: String) {
        socket?.write(string: text)
    }
}

extension WebSocketManager: WebSocketDelegate {

    func didReceive(event: WebSocketEvent, client: WebSocket) {
        switch event {
        case .connected(let headers):
            isConnected = true
            print("websocket is connected: \(headers)")
        case .disconnected(let reason, let code):
            isConnected = false
            print("websocket is disconnected: \(reason) with code: \(code)")
        case .text(let string):
            print("Received text: \(string)")
        case .binary(let data):
            print("Received data: \(data.count)")
        case .ping(_):
            break
        case .pong(_):
            break
        case .viabilityChanged(_):
            break
        case .reconnectSuggested(_):
            break
        case .cancelled:
            isConnected = false
        case .error(let error):
            isConnected = false

        }
    }

    func websocketDidConnect(socket: WebSocketClient) {
        print("helllo bro")
        delegate?.websocketDidConnect()
    }

    func websocketDidDisconnect(socket: WebSocketClient, error: Error?) {
        delegate?.websocketDidDisconnect(error: error)
    }

    func websocketDidReceiveMessage(socket: WebSocketClient, text: String) {
        delegate?.websocketDidReceiveMessage(text: text)
    }

    func websocketDidReceiveData(socket: WebSocketClient, data: Data) {
        print("data is ", data)
    }
}
