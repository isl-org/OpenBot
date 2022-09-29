//
// Created by Sparsh Jain on 29/09/22.
//

import Foundation
import TensorFlowLite

class Autopilot: Network {
    override init(model: Model, device: RuntimeDevice, numThreads: Int) {
        try! super.init(model: model, device: device, numThreads: numThreads)

    }
}