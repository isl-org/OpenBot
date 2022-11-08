//
//  AutopilotFragment.swift
//  OpenBot
//
//  Created by Sparsh Jain on 29/09/22.
//

import Foundation
import UIKit

class AutopilotFragment: CameraController {
    var autopilot: Autopilot?;
    var models: [Model] = [];
    var numberOfThreads: Int = 1
    let expandedAutoPilotView = expandedAutoPilot(frame: CGRect(x: 0, y: height - 375, width: width, height: 375))
    var autoPilotMode: Bool = false;
    let gameController = GameController.shared
    let bluetooth = bluetoothDataController.shared;
    var vehicleControl: Control = Control();
    var currentModel: ModelItem!
    var currentDevice : RuntimeDevice = RuntimeDevice.CPU

    override func viewDidLoad() {
        super.viewDidLoad()
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        expandedAutoPilotView.backgroundColor = Colors.freeRoamButtonsColor
        expandedAutoPilotView.layer.cornerRadius = 15
        createCameraView()
        let modelItems = Common.loadAllModels()
        if (modelItems.count > 0) {
            models = Model.fromModelItems(list: modelItems);
            currentModel = modelItems[0]
            autopilot = Autopilot(model: models[0], device: RuntimeDevice.CPU, numThreads: numberOfThreads);
        }
        view.addSubview(expandedAutoPilotView)
        setupNavigationBarItem()
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDevice), name: .updateDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateThread), name: .updateThread, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(toggleAutoMode), name: .autoMode, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateModel), name: .updateModel, object: nil)
        calculateFrame()
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

    func setupNavigationBarItem() {
        if UIImage(named: "back") != nil {
            let backNavigationIcon = (UIImage(named: "back")?.withRenderingMode(.alwaysOriginal))!
            let newBackButton = UIBarButtonItem(image: backNavigationIcon, title: Strings.autoPilot, target: self, action: #selector(AutopilotFragment.back(sender:)))
            navigationItem.leftBarButtonItem = newBackButton
        }
    }

    @objc func back(sender: UIBarButtonItem) {
        _ = navigationController?.popViewController(animated: true)
    }

    @objc func switchCamera() {
        switchCameraView();
    }

    @objc func openBluetoothSettings() {
        let nextViewController = (storyboard?.instantiateViewController(withIdentifier: Strings.bluetoothScreen))
        navigationController?.pushViewController(nextViewController!, animated: true)
    }

    @objc func updateDevice(_ notification: Notification) {
        currentDevice = RuntimeDevice(rawValue: notification.object as! String) ?? RuntimeDevice.CPU
        autopilot = Autopilot(model: Model.fromModelItem(item: currentModel), device: currentDevice, numThreads: numberOfThreads);
        currentDevice.rawValue == "GPU" ? NotificationCenter.default.post(name: .updateThreadLabel, object: "N/A") : NotificationCenter.default.post(name: .updateThreadLabel, object: String(numberOfThreads))
        autopilot?.tfliteOptions.threadCount = numberOfThreads

    }

    @objc func updateModel(_ notification: Notification) {
        let selectedModelName = notification.object as! String
        currentModel = Common.loadSelectedModel(modeName: selectedModelName)
        autopilot = Autopilot(model: Model.fromModelItem(item: currentModel), device: currentDevice, numThreads: numberOfThreads)
    }

    @objc func toggleAutoMode() {
        autoPilotMode = !autoPilotMode;
            Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { [self] timer in
                if !autoPilotMode {
                    timer.invalidate()
                }
                if (timer.isValid) {
                    captureImage();
                    if (images.count > 0) {
                        let controlResult: Control = autopilot?.recogniseImage(image: images[images.count - 1].0.cgImage!, indicator: 0) ?? Control();
                        print(controlResult.getLeft() as Any, controlResult.getRight() as Any);
                        sendControl(control: controlResult);
                    }
                }
            }
    }

    @objc func updateThread(_ notification: Notification) {
        let threadCount = notification.object as! String
        numberOfThreads = Int(threadCount) ?? 1
        autopilot?.tfliteOptions.threadCount = numberOfThreads

    }

    func sendControl(control: Control) {
        if (control.getRight() != vehicleControl.getRight() || control.getLeft() != vehicleControl.getLeft()) {
            let left = control.getLeft() * gameController.selectedSpeedMode.rawValue;
            let right = control.getRight() * gameController.selectedSpeedMode.rawValue;
            NotificationCenter.default.post(name: .updateSpeedLabel, object: String(Int(left)) + "," + String(Int(right)));
            NotificationCenter.default.post(name: .updateRpmLabel, object: String(Int(control.getLeft())) + "," + String(Int(control.getRight())));
            vehicleControl = control;
            print("c" + String(left) + "," + String(right) + "\n");
            bluetooth.sendData(payload: "c" + String(left) + "," + String(right) + "\n");
        }
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        if autoPilotMode {
            autoPilotMode = false
        }
    }
}
