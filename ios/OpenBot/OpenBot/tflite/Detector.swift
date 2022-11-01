//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation

class Detector: Network {

    func create(model: Model, device: RuntimeDevice, numThreads: Int) throws -> AnyObject? {
        switch (model.classType) {
        case .MOBILENETV1_1_0_Q, .MOBILENETV3_S_Q:
            return try DetectorQuantizedMobileNet(model: model, device: device, numThreads: numThreads);
        case .YOLOV4:
            return try DetectorFloatYoloV4(model: model, device: device, numThreads: numThreads);
        default:
            return nil;
        }
    }

    class Recognition {
    }
}