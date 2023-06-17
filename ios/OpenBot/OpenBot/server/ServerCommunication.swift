//
// Created by Nitish Yadav on 15/06/23.
//

import Foundation
import Network

class ServerCommunication : sendInitialMessageDelegate {
    let connection: NWConnection
    // outgoing connection
    weak var msgDelegate: sendInitialMessageDelegate?

    /// initializing function; endpoint
    init(endpoint: NWEndpoint) {
        print("PeerConnection outgoing endpoint: \(endpoint)")
        let tcpOptions = NWProtocolTCP.Options()
        tcpOptions.enableKeepalive = true
        tcpOptions.keepaliveIdle = 2
        let parameters = NWParameters(tls: nil, tcp: tcpOptions)
        parameters.includePeerToPeer = true
        connection = NWConnection(to: endpoint, using: parameters)
        start()
        msgDelegate = self
    }

    /// initializing function; incoming connection
    init(connection: NWConnection) {
        self.connection = connection
        start()
    }

    /// function to start the connection
    func start() {
        connection.stateUpdateHandler = { newState in
            print("connection.stateUpdateHandler \(newState)")
            switch newState {
            case .ready:
               self.callAPI();
            case .preparing:
                return
            default:
                break;
            }
        }
        connection.start(queue: .main)
    }

    /// function to send data in utf8 format.
    func send(_ message: String) {
        connection.send(content: message.data(using: .utf8), contentContext: .defaultMessage, isComplete: true, completion: .contentProcessed({ error in
            print("Connection send error: \(String(describing: error))")
        }))
    }

    /// function to hold the connection to receive incoming messages; parse them based on the commands and generate notifications resp;
    func receiveMessage() {
        connection.receive(minimumIncompleteLength: 1, maximumLength: 100000) { data, _, _, error in
            if error != nil {
                return
            }
            if let data = data,
               let message = String(data: data, encoding: .utf8) {
                let commands = message.split(separator: "\n");
                for command in commands {
                    if command.contains("SWITCH_CAMERA") {
                        NotificationCenter.default.post(name: .switchCamera, object: nil);
                    }
                    if command.contains("answer") {
                        NotificationCenter.default.post(name: .updateDataFromControllerApp, object: command.data(using: .utf8));
                    }
                    if command.contains("driveCmd") {
                        NotificationCenter.default.post(name: .updateStringFromControllerApp, object: message);
                    }
                    print(command);
                }
            }
            self.receiveMessage()
        }
    }

    /// function to parse the message for the connection and send it.
    func sendMessage() {
        var msg = JSON.toString(ConnectionActiveEvent(status: .init(CONNECTION_ACTIVE: "true")));
        client.send(message: msg);
        msg = JSON.toString(VideoProtocolEvent(status: .init(VIDEO_PROTOCOL: "WEBRTC")));
        client.send(message: msg);
        msg = JSON.toString(VideoServerUrlEvent(status: .init(VIDEO_SERVER_URL: "")));
        client.send(message: msg);
        msg = JSON.toString(VideoCommandEvent(status: .init(VIDEO_COMMAND: "START")));
        client.send(message: msg);
        msg = JSON.toString(VehicleStatusEvent(status: .init(LOGS: false, NOISE: false, NETWORK: false, DRIVE_MODE: "GAME", INDICATOR_LEFT: false, INDICATOR_RIGHT: false, INDICATOR_STOP: true)));
        client.send(message: msg)
    }


    func callAPI() {
        guard let url = URL(string: "/models") else {
            print("Invalid URL")
            return
        }

        let task = URLSession.shared.dataTask(with: url) { (data, response, error) in
            if let error = error {
                print("Error: \(error)")
                return
            }

            guard let httpResponse = response as? HTTPURLResponse else {
                print("Invalid response")
                return
            }

            if httpResponse.statusCode == 200 {
                // API call was successful
                if let data = data {
                    // Process the response data here
                    // You can convert the data to JSON, decode it, etc.
                    print("API response: \(String(data: data, encoding: .utf8) ?? "")")
                }
            } else {
                // API call returned an error
                print("API Error: \(httpResponse.statusCode)")
            }
        }

        task.resume()
    }

}