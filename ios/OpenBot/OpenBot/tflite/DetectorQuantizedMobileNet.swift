//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation

class DetectorQuantizedMobileNet: Detector {
    override init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        try super.init(model: model, device: device, numThreads: numThreads)
    }
}