//
// Created by Sparsh Jain on 27/10/22.
//

import Foundation
import UIKit

class ObjectTrackingFragment: CameraController {
    let expandedAutoPilotView = ObjectTrackingSettings()
    var numberOfThreads: Int = 1
    var bottomAnchorConstraint : NSLayoutConstraint!
    var trailingAnchorConstraint : NSLayoutConstraint!
    override func viewDidLoad() {
        super.viewDidLoad()
        expandedAutoPilotView.backgroundColor = Colors.freeRoamButtonsColor
        expandedAutoPilotView.layer.cornerRadius = 15
        createCameraView()
        view.addSubview(expandedAutoPilotView)
        expandedAutoPilotView.translatesAutoresizingMaskIntoConstraints = false
        expandedAutoPilotView.widthAnchor.constraint(equalToConstant: width).isActive = true
        expandedAutoPilotView.heightAnchor.constraint(equalToConstant: width * 0.95).isActive = true
        bottomAnchorConstraint = expandedAutoPilotView.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -5);
        trailingAnchorConstraint = expandedAutoPilotView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant:0 )
        trailingAnchorConstraint.isActive = true
        bottomAnchorConstraint.isActive = true
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDevice), name: .updateDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateThread), name: .updateThread, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConfidence), name: .updateConfidence, object: nil)
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        calculateFrame()
    }

    func calculateFrame() {
        if currentOrientation == .portrait || currentOrientation == .portraitUpsideDown {
            trailingAnchorConstraint.constant = 0;

        } else {
            trailingAnchorConstraint.constant = 10;
        }
    }

    @objc func openBluetoothSettings() {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: Strings.bluetoothScreen))
        navigationController?.pushViewController(nextViewController!, animated: true)
    }

    @objc func switchCamera() {
        switchCameraView();
    }

    @objc func updateDevice(_ notification: Notification) {
        let selectedDevice = notification.object as! String
//        autopilot = Autopilot(model: models[0], device: RuntimeDevice(rawValue: selectedDevice) ?? RuntimeDevice.CPU, numThreads: numberOfThreads);
        selectedDevice == "GPU" ? NotificationCenter.default.post(name: .updateThreadLabel, object: "N/A") : NotificationCenter.default.post(name: .updateThreadLabel, object: String( 1))
        print(selectedDevice)
    }

    @objc func updateThread(_ notification: Notification) {
        let threadCount = notification.object as! String
        numberOfThreads = Int(threadCount) ?? 1
        print(numberOfThreads)
    }

    @objc func updateConfidence(_ notification: Notification) {
        let confidence = notification.object as! String
        print("confidence is : ",confidence)
    }

}