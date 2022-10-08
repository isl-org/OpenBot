//
//  AutopilotFragment.swift
//  OpenBot
//
//  Created by Sparsh Jain on 29/09/22.
//

import Foundation
class AutopilotFragment: CameraController {
    var autopilot: Autopilot?;
    var models: [Model] = [];

    override func viewDidLoad() {
        super.viewDidLoad()
        createCameraView()
        let modelItems = loadModels();
        if (modelItems.count > 0) {
            models = Model.fromModelItems(list: modelItems);
            print(models)
            autopilot = Autopilot(model: models[0], device: RuntimeDevice.XNNPACK, numThreads: 1);
            checkItem();
        }
    }

    func checkItem() {
        do {
            try print(autopilot?.tflite?.input(at: 0) as Any);

            try print(autopilot?.tflite?.input(at: 1) as Any);

//            let doubleArray: [Double] = [1, 1];
            var data: Data = Data();
            data.append([UInt8(0.8)], count: 2);
            data.append([UInt8(1)], count: 2);
//            encode(with: .)
//            let stre: String? = String(bytes: s, encoding: .utf8);
//            var data: Data = Data();
//            if (stre != nil) {
//                data = stre.data(using: .utf8) ?? Data();
//            }
            try autopilot?.tflite?.copy(data, toInputAt: 0)
            try autopilot?.tflite?.invoke();
            let outputTensor = try autopilot?.tflite?.output(at: 0);
            // Copy output to `Data` to process the inference results.
            let outputSize = outputTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let outputData =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputSize)
            outputTensor?.data.copyBytes(to: outputData)
            for i in outputData.indices {
                print(outputData[i]);
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
}
