//
// Created by Nitish Yadav on 25/07/23.
//

import Foundation
import SocketIO
class WebSocketManager : NSObject {

    static let shared = WebSocketManager()
    var socket: SocketIOClient!
    let manager = SocketManager(socketURL: URL(string: "http://192.168.1.11:8000")!, config: [.log(true), .compress])

    override init() {
        super.init()
        socket = manager.defaultSocket
    }

    func connectSocket() {
        socket.connect()
    }

    func receiveMsg() {
        socket.on("new message here") { (dataArray, ack) in
            print(dataArray.count)
        }
    }
}

