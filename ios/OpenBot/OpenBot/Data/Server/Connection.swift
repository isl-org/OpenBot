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
        self.connection = tempConnection ?? connection
        start()
    }

    func start() {
        connection.stateUpdateHandler = { newState in
            print("connection.stateUpdateHandler \(newState)")
            switch newState {
            case .ready :
                self.receiveMessage()
                self.send("");
            case .preparing :
                return
            default:
                break;
            }

        }
        connection.start(queue: .main)
    }

    func send(_ message: String) {
        let message1 = "sending message to controller"
        let content: Data = message1.data(using: .utf8)!
        connection.send(content: content, completion: NWConnection.SendCompletion.contentProcessed(({ (NWError) in
            if (NWError == nil) {
                print("Data was sent to TCP destination ")

            } else {
                print("ERROR! Error when data (Type: Data) sending. NWError: \n \(NWError!)")
            }
        })))







//        let jsonObject: Any = [
//            "status": [
//                "CONNECTION_ACTIVE": "false"
//            ]
//        ]
//
//        let data = try! JSONSerialization.data(withJSONObject: jsonObject)
//        let string = NSString(data: data, encoding: NSUTF8StringEncoding)
//        print(string)
//        self.connection.send(content: data, completion: NWConnection.SendCompletion.contentProcessed(({ (NWError) in
//            if (NWError == nil) {
//                print("Data was sent to UDP",String(data: data, encoding: .utf8) ?? "pratap")
//            } else {
//                print("ERROR! Error when data (Type: Data) sending. NWError: \n \(NWError!)")
//            }
//        })))



//        connection.send(content: message.data(using: .utf8), contentContext: .defaultMessage, isComplete: true, completion: .contentProcessed({ error in
//            print("Connection send error: \(String(describing: error))")
//
//        }))
    }

    func receiveMessage() {
        connection.receive(minimumIncompleteLength: 1, maximumLength: 1000) { data, _, _, error in
            if let error = error {
                return
            }
            if let data = data,
               let message = String(data: data, encoding: .utf8) {
                if message.contains("SWITCH_CAMERA"){
                    NotificationCenter.default.post(name: .switchCamera, object: nil);
                }
                print(message);
                NotificationCenter.default.post(name: .updateDataFromControllerApp, object: message);
            }
            self.receiveMessage()
        }
    }
}
