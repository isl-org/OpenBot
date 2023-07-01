//
// Created by Nitish Yadav on 15/06/23.
//

import Foundation
import Network

var serverConnection: ServerCommunication?

class ServerCommunication {
    let connection: NWConnection
    static var serverEndPoint: Endpoint?
    // outgoing connection
    /// initializing function; endpoint
    init(endpoint: NWEndpoint) {
        print("PeerConnection outgoing endpoint: \(endpoint) endPoint interface is : \(endpoint.interface)")
        let tcpOptions = NWProtocolTCP.Options()
        tcpOptions.enableKeepalive = true
        tcpOptions.keepaliveIdle = 2
        let parameters = NWParameters(tls: nil, tcp: tcpOptions)
        parameters.includePeerToPeer = true
        connection = NWConnection(to: endpoint, using: parameters)
        start()
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
                self.callApi()
                if let endpoint = self.connection.currentPath?.remoteEndpoint {
                    if case let NWEndpoint.hostPort(host, port) = endpoint {
                        let end = self.connection.endpoint
                        if case NWEndpoint.service(let name, let type, let domain, let interface) = end {
                            let hostComponents = host.debugDescription.components(separatedBy: "%")
                            let ipAddress = hostComponents.first ?? ""
                            ServerCommunication.serverEndPoint = Endpoint(name: name, host: ipAddress, port: port.debugDescription);
                        }
                    }
                }
            case .preparing:
                return
            default:
                break;
            }
        }
        connection.start(queue: .main)
    }


    func callApi() {

        guard let host = ServerCommunication.serverEndPoint?.host else {
            print("Invalid URL host")
            return
        }
        guard let port = ServerCommunication.serverEndPoint?.port else {
            print("Invalid URL port");
            return
        }
        guard let url = URL(string: "http://\(host):\(port)/test") else {
            print("Invalid URL found")
            return
        }
        print("host is =========", host);
        let config = URLSessionConfiguration.default

        let session = URLSession(configuration: config)
        let task = session.dataTask(with: url) { (data, response, error) in
            // Handle the response
            if let error = error {
                print("Request error: \(error.localizedDescription)")
            } else if let httpResponse = response as? HTTPURLResponse {
                if httpResponse.statusCode == 200 {
                    // Request succeeded
                    if let data = data {
                        // Process the received data
                        // Example: Convert data to a string
                        if let responseString = String(data: data, encoding: .utf8) {
                            print("Response: \(responseString)")
                        }
                    }
                } else {
                    // Request failed with a non-200 status code
                    print("Request failed with status code: \(httpResponse.statusCode)")
                }
            }
        }

// Start the task
        task.resume()
    }

}