//
// Created by Nitish Yadav on 15/06/23.
//

import Foundation
import Network

var serverConnection: ServerCommunication?

class ServerCommunication {
    let connection: NWConnection
    // outgoing connection
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
                if let endpoint = self.connection.currentPath?.localEndpoint {
                    if case let NWEndpoint.hostPort(host, _) = endpoint {
                        print("Server IP Address: \(host)")
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
        guard let url = URL(string: "http://192.168.1.6:8000/test") else {
            print("Invalid URL")
            return
        }

// Create a URLSession configuration
        let config = URLSessionConfiguration.default

// Create a URLSession instance
        let session = URLSession(configuration: config)

// Create a data task for the GET request
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