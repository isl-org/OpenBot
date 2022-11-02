//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation
import TensorFlowLite


class DetectorQuantizedMobileNet: Detector {

    // indices in tflite model
    private var outputLocationsIdx: Int = -1;
    private var outputClassesIdx: Int = -1;
    private var outputScoresIdx: Int = -1;
    private var numDetectionsIdx: Int = -1;

    override init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        try super.init(model: model, device: device, numThreads: numThreads)
    }

    override func parseTFlite() {
        let index0 = try! tflite?.output(at: 0);
        let index1 = try! tflite?.output(at: 1);
        let index2 = try! tflite?.output(at: 2);
        let index3 = try! tflite?.output(at: 3);

        assignIndex(tensor: index0, index: 0);
        assignIndex(tensor: index1, index: 1);
        assignIndex(tensor: index2, index: 2);
        assignIndex(tensor: index3, index: 3);
        NUM_DETECTIONS = try! tflite?.output(at: outputLocationsIdx).shape.dimensions[1] ?? 0;
    }

    func assignIndex(tensor: Tensor?, index: Int) {
        switch (tensor?.name) {
        case "TFLite_Detection_PostProcess":
            outputLocationsIdx = index;
            break
        case "TFLite_Detection_PostProcess:1":
            outputClassesIdx = index;
            break
        case "TFLite_Detection_PostProcess:2":
            outputScoresIdx = index;
            break
        case "TFLite_Detection_PostProcess:3":
            numDetectionsIdx = index;
            break
        default:
            break;
        }
    }

    override func getLabelPath() -> String {
        "labelmap.txt";
    }
}