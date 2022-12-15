//
// Created by Nitish Yadav on 15/12/22.
//

import Foundation

import Foundation

let client = Client()

class Client {

    let browser = Browser()

    var connection: Connection?

    func start() {
        browser.start { [weak self] result in
            guard let self = self,
                  self.connection == nil else { return }
            print("client.handler result: \(result)")
            self.connection = Connection(endpoint: result.endpoint)
        }
    }

}