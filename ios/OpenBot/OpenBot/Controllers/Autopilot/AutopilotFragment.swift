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
    let expandedAutoPilotView = expandedAutoPilot(frame: CGRect(x: 0, y: height/2, width: width, height: height/2))

    override func viewDidLoad() {
        super.viewDidLoad()
        expandedAutoPilotView.backgroundColor = Colors.freeRoamButtonsColor
        createCameraView()
        let modelItems = loadModels();
        if (modelItems.count > 0) {
            models = Model.fromModelItems(list: modelItems);
            print("models are : ", models)
            autopilot = Autopilot(model: models[0], device: RuntimeDevice.XNNPACK, numThreads: 1);
            checkItem();
        }
        view.addSubview(expandedAutoPilotView)
        setupNavigationBarItem()
    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        if currentOrientation == .portrait{
            expandedAutoPilotView.frame.origin = CGPoint(x: 0, y: height/2)

        }
        else{
            expandedAutoPilotView.frame.origin = CGPoint(x: height/2+20, y: 20)
        }
    }

    func checkItem() {
        do {
            try print(autopilot?.tflite?.input(at: 0) as Any);

            try print(autopilot?.tflite?.input(at: 1) as Any);

            var data: Data = Data();
            data.append([UInt8(0.3)], count: 2);
            data.append([UInt8(1)], count: 2);

            //make image input


            try autopilot?.tflite?.copy(UIImage(named: "bluetooth")!.jpegData(compressionQuality: 1)!, toInputAt: 1)
            try autopilot?.tflite?.invoke()
            let imageOutput = try autopilot?.tflite?.output(at: 0)
            print(imageOutput)
//            encode(with: .)
//            let stre: String? = String(bytes: s, encoding: .utf8);
//            var data: Data = Data();
//            if (stre != nil) {
//                data = stre.data(using: .utf8) ?? Data();
//            }
            try autopilot?.tflite?.copy(data, toInputAt: 0)
            try autopilot?.tflite?.invoke();
            let outputTensor = try autopilot?.tflite?.output(at: 0);
            print("outputtensor", outputTensor)
            // Copy output to `Data` to process the inference results.
            let outputSize = outputTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let outputData =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputSize)
            outputTensor?.data.copyBytes(to: outputData)
            for i in outputData.indices {
                print("outputData ", outputData[i]);
            }
//            print()
//            print(outputData);
//            let str = String(decoding: outputTensor?.data ?? Data(), as: UTF8.self)
//            print(str)
        } catch {
            print("error:\(error)")
        };
    }

    func loadModels() -> [ModelItem] {
        if let url = Bundle.main.url(forResource: "config", withExtension: "json") {
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let jsonData = try decoder.decode([ModelItem].self, from: data)
                return jsonData;
            } catch {
                print("error:\(error)")
            }
        }
        return [];
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
}
