//
// Created by Sparsh Jain on 29/09/22.
//

import Foundation
import TensorFlowLite

class Network {

    var tflite: Interpreter?;
    var tfliteOptions: Interpreter.Options = Interpreter.Options();
    var gpuDelegate: MetalDelegate? = nil;


    init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        do {
            var delegates: [Delegate] = [];
            switch (device) {
            case .XNNPACK:
                tfliteOptions.isXNNPackEnabled = true;
                break;
            case .GPU:
                gpuDelegate = MetalDelegate();
                delegates.append(gpuDelegate!);
                break;
            case .CPU:
                break;
            }
            tfliteOptions.threadCount = numThreads;
            let split = model.path.split(separator: "/");
            let index = split.count - 1;
            let fileName = String(split[index]);
            let bundle = Bundle.main
            let path = bundle.path(forResource: fileName, ofType: "")
            if let file = path {
                tflite = try Interpreter(modelPath: file, options: tfliteOptions, delegates: delegates);
            } else {
                print("file not found");
            }
        }
    }

}