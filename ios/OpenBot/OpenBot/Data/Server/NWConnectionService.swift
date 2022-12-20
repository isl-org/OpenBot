//
// Created by Sparsh Jain on 12/12/22.
//

import Foundation
import Network

public class NetworkServiceConnection: NSObject {


    private var browser: NWBrowser?
    private var netService: NetService?
//    private var completion: ((Bool) -> Void)?
    let SERVICE_TYPE: String = "_openbot._tcp.";

    public func discoverServices() {

        // Create parameters, and allow browsing over peer-to-peer link.
        let parameters = NWParameters()
        parameters.includePeerToPeer = true

        // Browse for a custom service type.
        let browser = NWBrowser(for: .bonjour(type: SERVICE_TYPE, domain: nil), using: parameters)
        self.browser = browser
        browser.stateUpdateHandler = { newState in
            switch newState {
            case .failed(let error):
                print(error.localizedDescription)
            case .ready, .cancelled:
                break
            case let .waiting(error):
                print("Local network permission has been denied: \(error)")
                self.reset()
            default:
                break
            }
        }

        browser.browseResultsChangedHandler = { (results, changes) in
            print("results", results.count);
            print("changes", changes.count);
            for (index, element) in results.enumerated() {
                print("Service found at index \(index):")
                print("Service name: \(element.endpoint)")
//                print("Service name: \(element.nw)")
                let connection = NWConnection(to: element.endpoint, using: .tcp)

                connection.stateUpdateHandler = { state in
                    switch state {
                    case .ready:
                        if let innerEndpoint = connection.currentPath?.remoteEndpoint,
                           case .hostPort(let host, let port) = innerEndpoint {
                            print("Connected to", "\(host):\(port)")
                            do {
                                let listener = try NWListener(using: .tcp)
//                                listener.port = port
                                print(listener)
                                print(connection)
                                listener.newConnectionHandler = { (connection) in
                                    // Listen for data on the new connection
                                    connection.receive(minimumIncompleteLength: 1, maximumLength: 1024) { (data, _, _, error) in
                                        if let error = error {
                                            // Handle the error
                                            // ...
                                        }

                                        if let data = data {
                                            // Handle the received data
                                            // ...
                                            print(data)
                                        }
                                    }
                                }

                                listener.start(queue: .main)
                            } catch {
                            }
//                            self.listener.service = NWListener.Service(name: name, type: type, domain: domain, txtRecord: txtRecord)

                        }
                    default:
                        break
                    }
                }
                connection.start(queue: .global())
            }
            for (index, change) in changes.enumerated() {
                switch change {
                case .added(let browseResult):
                    switch browseResult.endpoint {
                    case .hostPort(let host, let port):
                        print("added hostPort \(host) \(port)")
                    case .service(let name, let type, let domain, let interface):
                        print("added service \(name) \(type) \(domain) \(String(describing: interface))")
                    default:
                        print("fail")
                    }
                case .removed(let browseResult):
                    print("removed \(browseResult.endpoint)")
                case .changed(_, let browseResult, let flags):
                    if flags.contains(.interfaceAdded) {
                        print("\(browseResult.endpoint) added interfaces")
                    }
                    if flags.contains(.interfaceRemoved) {
                        print("\(browseResult.endpoint) removed interfaces")
                    }
                default:
                    print("no change")
                }
            }
        }
//        netService = NetService(domain: "local.", type:"_lnp._tcp.", name: "LocalNetworkPrivacy", port: 1100)
//        netService?.delegate = self

        self.browser?.start(queue: .main)
        netService?.publish()
    }

    private func reset() {
        browser?.cancel()
        browser = nil
//        netService?.stop()
//        netService = nil
    }
}

//@available(iOS 14.0, *)
//extension NetworkServiceConnection: NetServiceDelegate, NetServiceBrowserDelegate {
//    public func netServiceDidPublish(_ sender: NetService) {
//        reset()
//        print("Local network permission has been granted")
//    }
//
//    public func netServiceBrowser(_ browser: NetServiceBrowser, didFind service: NetService, moreComing: Bool) {
//        print("did find", service);
//    }
//
//    public func netServiceBrowser(_ browser: NetServiceBrowser, didNotSearch errorDict: [String: NSNumber]) {
//        print("errorDict", errorDict)
//    }
//
//    public func netServiceBrowser(_ browser: NetServiceBrowser, didRemove service: NetService, moreComing: Bool) {
//    }
//
//    public func netService(_ sender: NetService, didAcceptConnectionWith inputStream: InputStream, outputStream: OutputStream) {
//
//    }
//}
