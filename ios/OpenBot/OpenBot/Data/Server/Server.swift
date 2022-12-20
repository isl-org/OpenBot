//
// Created by Nitish Yadav on 15/12/22.
//

import Foundation
import Network
import UIKit

var server = try? Server()
var tempConnection : NWConnection?
class Server {

    let listener: NWListener

    var connections: [Connection] = []
    var shareConnection : Connection? = nil;

    init() throws {
        let tcpOptions = NWProtocolTCP.Options()
        tcpOptions.enableKeepalive = true
        tcpOptions.keepaliveIdle = 2

        let parameters = NWParameters(tls: nil, tcp: tcpOptions)
        parameters.includePeerToPeer = true
        listener = try NWListener(using: parameters)

        listener.service = NWListener.Service(name: "server", type: "_openbot._tcp.")
    }

    func start() {
        listener.stateUpdateHandler = { newState in
            print("listener.stateUpdateHandler \(newState)")
        }
        listener.newConnectionHandler = { [weak self] newConnection in
            print("listener.newConnectionHandler \(newConnection)")
            let connection = Connection(connection: newConnection)
            tempConnection = newConnection;
            sharedConnection = connection;
            print("share connection is :",sharedConnection)
            self?.connections += [connection]
        }
        listener.start(queue: .main)
    }

    func send(jsonObject : String) {
        sharedConnection?.send("sending message to connection");
        print(connections.count)
//        for connection in connections {
//            connection.send(jsonObject);
//        }
    }

}