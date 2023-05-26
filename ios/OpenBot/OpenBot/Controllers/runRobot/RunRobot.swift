//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import UIKit
class runRobot : UIViewController {
    @IBOutlet weak var commandMessage: UILabel!
    let bluetooth = bluetoothDataController.shared
    override func viewDidLoad() {
        super.viewDidLoad()
        NotificationCenter.default.addObserver(self, selector: #selector(updateCommandMsg), name: .commandName, object: nil);
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        NotificationCenter.default.removeObserver(self);
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NotificationCenter.default.post(name: .cancelThread, object: nil)
        bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
    }

    var messageArray = [String]() // Array to store received messages

    @objc func updateCommandMsg(_ notification: Notification) {
        DispatchQueue.main.async {
            let message = notification.object as! String
            self.messageArray.append(message) // Add message to the array

            // Update UI periodically to show all the received messages
            self.updateUI()
        }
    }

    func updateUI() {
        guard !messageArray.isEmpty else {
            return // No more messages to display
        }

        let message = messageArray.joined(separator: "\n") // Combine all messages with a line break
        self.commandMessage.text = message
        print("message ", message);

        messageArray.removeAll() // Clear the array

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) { [weak self] in
            self?.updateUI() // Update UI again after a delay
        }
    }

}
