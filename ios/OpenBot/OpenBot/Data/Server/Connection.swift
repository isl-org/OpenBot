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
        print("PeerConnection incoming connection: \(connection)")
        self.connection = connection
        start()
    }

    func start() {
        connection.stateUpdateHandler = { newState in
            print("connection.stateUpdateHandler \(newState)")
            switch newState {
            case .ready :
                self.receiveMessage()
            case .preparing :
                return
            default:
                break;
            }

        }
        connection.start(queue: .main)
    }

    func send(_ message: String) {
        connection.send(content: message.data(using: .utf8), contentContext: .defaultMessage, isComplete: true, completion: .contentProcessed({ error in
            print("Connection send error: \(String(describing: error))")

        }))
    }

    func receiveMessage() {
        connection.receive(minimumIncompleteLength: 1, maximumLength: 1000) { data, _, _, error in
            if let error = error {
                return
            }
            if let data = data,
               let message = String(data: data, encoding: .utf8) {
//                print("Connection receiveMessage message: \(message)")
                NotificationCenter.default.post(name: .updateDataFromControllerApp, object: message);

            }
            self.receiveMessage()
        }
    }
}
