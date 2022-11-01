//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation

class DetectorQuantizedMobileNet: Detector {
    override init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        try super.init(model: model, device: device, numThreads: numThreads)
    }

    override func parseTFlite() {
        print("overridden parse function");
        let index0 = try! tflite?.output(at: 0);
        let index1 = try! tflite?.output(at: 1);
        let index2 = try! tflite?.output(at: 2);
        let index3 = try! tflite?.output(at: 3);
        print(index0, index1, index2, index3);

    }
}