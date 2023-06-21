//
// Created by Nitish Yadav on 15/06/23.
//

import Foundation
import Network
class ServerListener  {


    let browser = NsdService();

    ///function to start the browser to create the client.
    func start() {
        browser.start { result in
            print("client.handler result: \(result)")
        }

    }

    /// function to send the values to the connection.
    func send(message: String) {
        let msg = JSON.toString(OpenBotSignal(openbot: .init(1)));
        serverConnection?.send(msg);
    }

    func nsdServiceDidChangeServers(_ nsdService: NsdService) {
        print("Something is added to the server");
    }
}