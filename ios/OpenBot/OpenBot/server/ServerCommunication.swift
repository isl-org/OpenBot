//
// Created by Nitish Yadav on 15/06/23.
//

import Foundation
import Network
var serverConnection : ServerCommunication?
class ServerCommunication  {
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
                print("ready");
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

    func callApi(){
        guard let url = URL(string: "http://192.168.1.9:8000/test") else {
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

struct ipAddress{
    public static func getIPAddress() -> String? {
        var address : String?

        // Get list of all interfaces on the local machine:
        var ifaddr : UnsafeMutablePointer<ifaddrs>?
        guard getifaddrs(&ifaddr) == 0 else { return nil }
        guard let firstAddr = ifaddr else { return nil }

        // For each interface ...
        for ifptr in sequence(first: firstAddr, next: { $0.pointee.ifa_next }) {
            let interface = ifptr.pointee

            // Check for IPv4 or IPv6 interface:
            let addrFamily = interface.ifa_addr.pointee.sa_family
            if addrFamily == UInt8(AF_INET) || addrFamily == UInt8(AF_INET6) {

                // Check interface name:
                // wifi = ["en0"]
                // wired = ["en2", "en3", "en4"]
                // cellular = ["pdp_ip0","pdp_ip1","pdp_ip2","pdp_ip3"]

                let name = String(cString: interface.ifa_name)
                if  name == "en0" || name == "en2" || name == "en3" || name == "en4" || name == "pdp_ip0" || name == "pdp_ip1" || name == "pdp_ip2" || name == "pdp_ip3" {

                    // Convert interface address to a human readable string:
                    var hostname = [CChar](repeating: 0, count: Int(NI_MAXHOST))
                    getnameinfo(interface.ifa_addr, socklen_t(interface.ifa_addr.pointee.sa_len),
                            &hostname, socklen_t(hostname.count),
                            nil, socklen_t(0), NI_NUMERICHOST)
                    address = String(cString: hostname)
                }
            }
        }
        freeifaddrs(ifaddr)

        return address
    }



}