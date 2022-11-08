//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation
import TensorFlowLite

class DetectorFloatYoloV4: Detector {
    // indices in tflite model
    private var outputLocationsIdx: Int = -1;
    private var outputScoresIdx: Int = -1;

    let IMAGE_MEAN: Float = 0.0;
    let IMAGE_STD: Float = 255.0;

    override init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        try super.init(model: model, device: device, numThreads: numThreads)
    }

    override func parseTFlite() {
        let index0 = try! tflite?.output(at: 0);
        let index1 = try! tflite?.output(at: 1);

        assignIndex(tensor: index0, index: 0);
        assignIndex(tensor: index1, index: 1);
        NUM_DETECTIONS = try! tflite?.output(at: outputLocationsIdx).shape.dimensions[1] ?? 0;
    }

    func assignIndex(tensor: Tensor?, index: Int) {
        switch (tensor?.name) {
        case "Identity", "StatefulPartitionedCall:0":
            outputLocationsIdx = index;
            break
        case "Identity_1", "StatefulPartitionedCall:1":
            outputScoresIdx = index;
            break
        default:
            break;
        }
    }

    override func getLabelPath() -> String {
        "coco.txt";
    }

    override func getNumDetections() -> Int {
        NUM_DETECTIONS;
    }

    /**
     normalized to the range [-1.0, 1.0]
    */
    override func addPixelValue(red: UInt8, blue: UInt8, green: UInt8) {
        var normalizedRed = (Float32(((red >> 16) & 0xFF)) - IMAGE_MEAN) / IMAGE_STD;
        var normalizedGreen = (Float32((green >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;
        var normalizedBlue = (Float32((blue) & 0xFF) - IMAGE_MEAN) / IMAGE_STD;

        let elementSize = MemoryLayout.size(ofValue: normalizedRed)
        var bytes = [UInt8](repeating: 0, count: elementSize)
        memcpy(&bytes, &normalizedRed, elementSize)
        imgData.append(&bytes, count: elementSize)
        memcpy(&bytes, &normalizedGreen, elementSize)
        imgData.append(&bytes, count: elementSize)
        memcpy(&bytes, &normalizedBlue, elementSize)
        imgData.append(&bytes, count: elementSize)
    }


    override func runInference() throws {
        do {

            let outputLocationsTensor = try tflite?.output(at: outputLocationsIdx);
            print(outputLocationsTensor);
            let outputScoresTensor = try tflite?.output(at: outputScoresIdx);
            print(outputScoresTensor);
            let outputSize = outputLocationsTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let outputData =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputSize)
            outputLocationsTensor?.data.copyBytes(to: outputData);
            var i: Int = 0;
            print("outputLocationsTensor");
            for item in outputData {
                print("Index: ", i);
                i += 1;
                print(item);
            }

            let outputScoresTensorSize = outputScoresTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let outputScoresTensorData =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputScoresTensorSize)
            outputScoresTensor?.data.copyBytes(to: outputScoresTensorData);
            i = 0;
            print("outputScoresTensor")
            for item in outputScoresTensorData {
                print("Index: ", i);
                i += 1;
                print(item);
            }
        } catch {
            print("error:\(error)")
        }
    }
}