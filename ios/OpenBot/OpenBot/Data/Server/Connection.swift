//
// Created by Nitish Yadav on 15/12/22.
//

import Foundation
import Network
var sharedConnection: Connection?
class Connection {
    let connection: NWConnection
    // outgoing connection
    init(endpoint: NWEndpoint) {
        print("PeerConnection outgoing endpoint: \(endpoint)")
        let tcpOptions = NWProtocolTCP.Options()
        tcpOptions.enableKeepalive = true
        tcpOptions.keepaliveIdle = 2
        let parameters = NWParameters(tls: nil, tcp: tcpOptions)
        parameters.includePeerToPeer = true
        connection = NWConnection(to: endpoint, using: parameters)
        start()
    }

    // incoming connection
    init(connection: NWConnection) {
        print("inside init");
        print("PeerConnection incoming connection: \(connection)")
        self.connection = connection
        start()
    }

    func start() {
        connection.stateUpdateHandler = { newState in
            print("connection.stateUpdateHandler \(newState)")
            switch newState {
            case .ready :
                self.receiveMessage();
            case .preparing :
                return
            default:
                break;
            }
        }
        connection.start(queue: .main)
    }

    func send(_ message: String) {
        print("message is :",message)
        connection.send(content: message.data(using: .utf8), contentContext: .defaultMessage, isComplete: true, completion: .contentProcessed({ error in
            print("Connection send error: \(String(describing: error))")
        }))
    }

    func receiveMessage() {
        connection.receive(minimumIncompleteLength: 1, maximumLength: 100000) { data, _, _, error in
            if error != nil {
                return
            }
            if let data = data,
               let message = String(data: data, encoding: .utf8) {
                let commands = message.split(separator: "\n");
                for command in commands {
                    if command.contains("SWITCH_CAMERA"){
                        NotificationCenter.default.post(name: .switchCamera, object: nil);
                    }
                    if command.contains("answer"){
                        NotificationCenter.default.post(name: .updateDataFromControllerApp, object: command.data(using: .utf8));
                    }
                    print("received message ->",command);
                }

            }
            self.receiveMessage()
        }
//        connection.receive(minimumIncompleteLength: 1, maximumLength: 10000) { data, _, _, error in
//            if let error = error {
//                return
//            }
//            if let data = data,
//               let message = String(data: data, encoding: .utf8) {
//                if message.contains("SWITCH_CAMERA"){
//                    NotificationCenter.default.post(name: .switchCamera, object: nil);
//                }
//                if message.contains("answer"){
//                    NotificationCenter.default.post(name: .updateDataFromControllerApp, object: data);
//                }
//                print("received message ->",message);
//            }
//            self.receiveMessage()
//        }
    }
}
