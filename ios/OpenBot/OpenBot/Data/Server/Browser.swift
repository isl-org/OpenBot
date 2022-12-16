//
// Created by Nitish Yadav on 15/12/22.
//
import Foundation
import Network

let browser = Browser()
var netService: NetService?
class Browser {

    var browser: NWBrowser

    init() {
        let parameters = NWParameters()
        parameters.includePeerToPeer = true

        browser = NWBrowser(for: .bonjour(type: "_openbot._tcp.", domain: nil), using: parameters)

    }

    func start(handler: @escaping (NWBrowser.Result) -> Void) {
        browser.stateUpdateHandler = { newState in
            print("i am here ")
            switch newState {
            case .failed(let error):
                print(error.localizedDescription)
            case .ready, .cancelled:
                break
            case let .waiting(error):
                print("Local network permission has been denied: \(error)")
                self.browser.cancel()

            default:
                break
            }

        }
        browser.browseResultsChangedHandler = { results, changes in
            print("results", results.count);
            print("changes", changes.count);
            for result in results {
                print(result)
                if case NWEndpoint.service = result.endpoint {
                    handler(result)
                }
            }

            for (index, change) in changes.enumerated(){
                switch change {
                case .added(let browseResult):
                    switch browseResult.endpoint {
                    case .hostPort(let host, let port):
                        print("added hostPort \(host) \(port)")
                    case .service(let name, let type, let domain, let interface):
                        print("added service \(name) \(type) \(domain) \(String(describing: interface))")
                        Connection(endpoint: browseResult.endpoint)
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
                    netService = NetService(domain: "local.", type:"_openbot._tcp.", name: "_openbot._tcp.", port: 1100)
                    netService?.remove(from: .main, forMode: .default);
        }

        browser.start(queue: .main)
    }
        func reset() {
        browser.cancel()
        netService?.stop()
        netService = nil
}


}