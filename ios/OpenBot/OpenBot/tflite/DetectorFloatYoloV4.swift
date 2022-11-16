//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation
import TensorFlowLite

class DetectorFloatYoloV4: Detector {
    // indices in tflite model
    private var outputLocationsIdx: Int = -1;
    private var outputScoresIdx: Int = -1;

    // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
    // contains the location of detected boxes
    private var outputLocations: UnsafeMutableBufferPointer<Float32>?;
    // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the scores of detected boxes
    private var outputScores: UnsafeMutableBufferPointer<Float32>?;

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
            let outputScoresTensor = try tflite?.output(at: outputScoresIdx);
            let outputSize = outputLocationsTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            outputLocations =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputSize)
            outputLocationsTensor?.data.copyBytes(to: outputLocations!);

            let outputScoresTensorSize = outputScoresTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            outputScores =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputScoresTensorSize)
            outputScoresTensor?.data.copyBytes(to: outputScores!);
        } catch {
            print("error:\(error)")
        }
    }

    override func getRecognitions(className: String) -> [Recognition] {
        var recognitions: [Recognition] = [];
        var outputScores2D = Array(repeating: Array<Float32>(repeating: 0, count: labels.count), count: NUM_DETECTIONS);
        for a in stride(from: 0, to: NUM_DETECTIONS, by: 1) {
            for b in stride(from: 0, to: labels.count, by: 1) {
                outputScores2D[a][b] = outputScores![b * labels.count + a];
            }
        }
        if (NUM_DETECTIONS > 0) {
            for i in 0..<NUM_DETECTIONS {
                var maxClassScore: Float = 0;
                var classId = -1;
                let classes = outputScores2D[i];
                for c in stride(from: 0, to: classes.count, by: 1) {
                    if (classes[c] > maxClassScore) {
                        classId = c;
                        maxClassScore = classes[c];
                    }
                }

                if (classId > -1 && className == labels[classId]) {
                    let detection: CGRect = CGRect(x: CGFloat(outputLocations![4 * i]), y: CGFloat(outputLocations![(4 * i) + 1]), width: CGFloat(outputLocations![(4 * i) + 2]), height: CGFloat(outputLocations![(4 * i) + 3]))
                    recognitions.append(Recognition(id: String(i), title: labels[classId], confidence: maxClassScore, location: detection, classId: classId));
                }
            }
        }
        return nms(recognitions: recognitions);
    }
}