//
// Created by Nitish Yadav on 31/07/23.
//

import Foundation
@available(iOS 13.0, *)
class NativeWebSocket: NSObject, WebSocketProvider {
//    let url =  URL(string: "ws://192.168.1.11:8080/ws")!
    let url = URL(string: "ws://inconclusive-warm-shamrock.glitch.me")!;
    static let shared : NativeWebSocket = NativeWebSocket();
    var delegate: WebSocketProviderDelegate?
    private var socket: URLSessionWebSocketTask?
    private lazy var urlSession: URLSession = URLSession(configuration: .default, delegate: self, delegateQueue: nil)

   override init() {
        super.init()
        connect();
    }

    func connect() {
        let socket = urlSession.webSocketTask(with: url)
        socket.resume()
        self.socket = socket
        self.readMessage()
    }

    func send(data: Data) {
        self.socket?.send(.data(data)) { _ in }
    }


    private func readMessage() {
        print("inside read message");
        self.socket?.receive { [weak self] message in
            guard let self = self else { return }
            switch message {
            case .success(.data(let data)):
                print("data de bhae");
                self.delegate?.webSocket(self, didReceiveData: data)
                self.readMessage()

            case .success(.string(let text)):
                print("Received text message",text , Date().millisecondsSince1970)
                // Process the text message if needed
                self.delegate?.webSocket(self, didReceiveData: text);
                NotificationCenter.default.post(name: .updateDataFromControllerApp, object: text);
                self.readMessage()

            case .success:
                debugPrint("Warning: Expected to receive data format but received a string. Check the websocket server config.")
                self.readMessage()

            case .failure:
                self.disconnect()
            }
        }
    }

    private func disconnect() {
        self.socket?.cancel()
        self.socket = nil
        self.delegate?.webSocketDidDisconnect(self)
    }
}

@available(iOS 13.0, *)
extension NativeWebSocket: URLSessionWebSocketDelegate, URLSessionDelegate  {
    func urlSession(_ session: URLSession, webSocketTask: URLSessionWebSocketTask, didOpenWithProtocol protocol: String?) {
        self.delegate?.webSocketDidConnect(self)
    }

    func urlSession(_ session: URLSession, webSocketTask: URLSessionWebSocketTask, didCloseWith closeCode: URLSessionWebSocketTask.CloseCode, reason: Data?) {
        self.disconnect()
    }
}
