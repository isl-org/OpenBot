//
// Created by Nitish Yadav on 15/06/23.
//

import Foundation
import Network
var serverConnection : ServerCommunication?
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
                print("ready");
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