//
// Created by Nitish Yadav on 15/06/23.
//

import Foundation
class ServerListener {
    let browser = NsdService()

    ///function to start the browser to create the client.
    func start() {
        browser.start { result in
            print("client.handler result: \(result)")
            print(" client is :", client)
        }
    }

    /// function to send the values to the connection.
    func send(message: String) {
        print("message is ",message);
    }


}