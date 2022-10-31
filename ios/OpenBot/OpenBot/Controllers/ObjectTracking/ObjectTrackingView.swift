//
// Created by Sparsh Jain on 27/10/22.
//

import Foundation
import UIKit

class ObjectTrackingFragment: CameraController {
    let expandedAutoPilotView = ObjectTrackingSettings(frame: CGRect(x: 0, y: height - 375, width: width, height: 375))

    override func viewDidLoad() {
        super.viewDidLoad()
        expandedAutoPilotView.backgroundColor = Colors.freeRoamButtonsColor
        expandedAutoPilotView.layer.cornerRadius = 15
        createCameraView()
        view.addSubview(expandedAutoPilotView)
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)

    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        calculateFrame()
    }

    func calculateFrame() {
        if currentOrientation == .portrait || currentOrientation == .portraitUpsideDown {
            expandedAutoPilotView.frame.origin = CGPoint(x: 0, y: height - 375)
        } else {
            expandedAutoPilotView.frame.origin = CGPoint(x: height - 375, y: 0)
        }
    }

    @objc func openBluetoothSettings() {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: Strings.bluetoothScreen))
        navigationController?.pushViewController(nextViewController!, animated: true)
    }

    @objc func switchCamera() {
        switchCameraView();
    }
}