//
// Created by Nitish Yadav on 07/04/23.
//

import Foundation
import UIKit
class runRobot : UIViewController {
    let bluetooth = bluetoothDataController.shared
    override func viewDidLoad() {
        print("loaded runRobot")
        super.viewDidLoad()
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        print("viewWillDisappear")
        bluetooth.sendData(payload: "c" + String(0) + "," + String(0) + "\n")
        NotificationCenter.default.post(name: .cancelThread, object: nil)
    }
}
