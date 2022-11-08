//
// Created by Sparsh Jain on 27/10/22.
//

import Foundation
import UIKit

class ObjectTrackingFragment: CameraController {
    var objectTrackingSettings: ObjectTrackingSettings?;
    var numberOfThreads: Int = 1
    var bottomAnchorConstraint: NSLayoutConstraint!
    var trailingAnchorConstraint: NSLayoutConstraint!
    var detector: Detector?
    var models: [Model] = [];
    var autoMode: Bool = false;
    var vehicleControl: Control = Control();
    let bluetooth = bluetoothDataController.shared;
    var selectedModel: ModelItem!

    override func viewDidLoad() {
        let modelItems = Common.loadAllModels()
        if (modelItems.count > 0) {
            let model = modelItems.first(where: { $0.type == TYPE.DETECTOR.rawValue })
            selectedModel = model
            detector = try! Detector.create(model: Model.fromModelItem(item: model ?? modelItems[0]), device: RuntimeDevice.CPU, numThreads: numberOfThreads) as? Detector;
        }
        objectTrackingSettings = ObjectTrackingSettings(frame: CGRect(x: 0, y: height - 375, width: width, height: 375), detector: detector);
        objectTrackingSettings!.backgroundColor = Colors.freeRoamButtonsColor
        objectTrackingSettings!.layer.cornerRadius = 15
        createCameraView()
        view.addSubview(objectTrackingSettings!)
        objectTrackingSettings!.translatesAutoresizingMaskIntoConstraints = false
        objectTrackingSettings!.widthAnchor.constraint(equalToConstant: width).isActive = true
        objectTrackingSettings!.heightAnchor.constraint(equalToConstant: width * 0.95).isActive = true
        bottomAnchorConstraint = objectTrackingSettings!.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -5);
        trailingAnchorConstraint = objectTrackingSettings!.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: 0)
        trailingAnchorConstraint.isActive = true
        bottomAnchorConstraint.isActive = true
        NotificationCenter.default.addObserver(self, selector: #selector(openBluetoothSettings), name: .ble, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(switchCamera), name: .switchCamera, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateDevice), name: .updateDevice, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateThread), name: .updateThread, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConfidence), name: .updateConfidence, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(toggleAutoMode), name: .autoMode, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateModel), name: .updateModel, object: nil)

        super.viewDidLoad()
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

    @objc func updateDevice(_ notification: Notification) throws {
        let selectedDevice = notification.object as! String
        switch selectedDevice {
        case RuntimeDevice.CPU.rawValue:
            detector = try Detector(model: Model.fromModelItem(item: selectedModel), device: RuntimeDevice.CPU, numThreads: numberOfThreads)
        case RuntimeDevice.GPU.rawValue:
            detector = try Detector(model: Model.fromModelItem(item: selectedModel), device: RuntimeDevice.GPU, numThreads: 1)
            NotificationCenter.default.post(name: .updateThreadLabel, object: "N/A")
        case RuntimeDevice.XNNPACK.rawValue:
            detector = try Detector(model: Model.fromModelItem(item: selectedModel), device: RuntimeDevice.GPU, numThreads: numberOfThreads)
        default:
            detector = try Detector(model: Model.fromModelItem(item: selectedModel), device: RuntimeDevice.CPU, numThreads: numberOfThreads)
        }
    }

    @objc func updateThread(_ notification: Notification) {
        let threadCount = notification.object as! String
        numberOfThreads = Int(threadCount) ?? 1
        print(numberOfThreads)
    }

    @objc func updateConfidence(_ notification: Notification) {
        let confidence = notification.object as! String
        print("confidence is : ", confidence)
    }

    @objc func toggleAutoMode() {
        autoMode = !autoMode;
        if (autoMode) {
            Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { [self] timer in
                do {
                    if !autoMode {
                        timer.invalidate()
                    }
                    if (timer.isValid) {
                        captureImage();
                        if (images.count > 0) {

                            let image = cropImage(imageToCrop: images[images.count - 1].0, toRect: CGRect(origin: CGPoint(x: 0, y: 0), size: CGSize.parseSize(selectedModel.inputSize)))
                            try detector?.recognizeImage(image: image.cgImage!);
//                        print(controlResult.getLeft() as Any, controlResult.getRight() as Any);
//                        sendControl(control: controlResult);
                        }
                    }
                } catch {
                    print("error:\(error)")
                }
            }
        }
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

    @objc func updateModel(_ notification: Notification) throws {
        let selectedModelName = notification.object as! String
        selectedModel = Common.loadSelectedModel(modeName: selectedModelName)
        detector = try! Detector.create(model: Model.fromModelItem(item: selectedModel), device: RuntimeDevice.CPU, numThreads: numberOfThreads) as? Detector;
        NotificationCenter.default.post(name: .updateObjectList, object: detector?.labels)
    }
}