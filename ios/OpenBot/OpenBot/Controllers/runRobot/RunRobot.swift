//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import UIKit
class runRobot : UIViewController {
    let bluetooth = bluetoothDataController.shared
    override func viewDidLoad() {
        super.viewDidLoad()

    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NotificationCenter.default.post(name: .cancelThread, object: nil)
        bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
    }
}
