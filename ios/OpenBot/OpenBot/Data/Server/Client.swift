//
// Created by Nitish Yadav on 15/12/22.
//

import Foundation

var client = Client()

class Client {
    let browser = Browser()

    func start() {
        print("new client")
        browser.start { result in
            print("client.handler result: \(result)")
//            sharedConnection = Connection(endpoint: result.endpoint)
            print(" client is :", client)
        }
    }

    func send(message: String) {
        sharedConnection?.send(message + "\n");
    }
}