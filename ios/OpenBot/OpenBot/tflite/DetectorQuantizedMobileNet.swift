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


    // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
    // contains the location of detected boxes
    private var outputLocations: Any?;
    // outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the classes of detected boxes
    private var outputClasses: Any?;
    // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the scores of detected boxes
    private var outputScores: Any?;
    // numDetections: array of shape [Batchsize]

    // contains the number of detected boxes

    override init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        try super.init(model: model, device: device, numThreads: numThreads)
    }

    override func parseTFlite() {
        let input0 = try! tflite?.input(at: 0);
        print(input0);
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

    override func getNumDetections() -> Int {
        NUM_DETECTIONS;
    }

    override func feedData() {
        outputLocations = [getNumDetections()][4];
        outputClasses = [getNumDetections()];
        outputScores = [getNumDetections()];
        let numDetections: Float32 = 0;
        outputMap[outputLocationsIdx] = outputLocations;
        outputMap[outputClassesIdx] = outputClasses;
        outputMap[outputScoresIdx] = outputScores;
        outputMap[numDetectionsIdx] = numDetections;
    }

    override func runInference() throws {
        do {

            let outputLocationsTensor = try tflite?.output(at: outputLocationsIdx);
            print(outputLocationsTensor);
            let outputClassesTensor = try tflite?.output(at: outputClassesIdx);
            print(outputClassesTensor);
            let outputScoresTensor = try tflite?.output(at: outputScoresIdx);
            print(outputScoresTensor);
            let numDetectionsTensor = try tflite?.output(at: numDetectionsIdx);
            print(numDetectionsTensor);
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

//            print(outputData[0]);
//            print(outputData[1]);
//            print(outputData[2]);
            let outputClassesTensorSize = outputClassesTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let outputClassesTensorData =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputClassesTensorSize)
            outputClassesTensor?.data.copyBytes(to: outputClassesTensorData);
            i = 0;
            print("outputClassesTensor");

            for item in outputClassesTensorData {
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

            let numDetectionsTensorSize = numDetectionsTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let numDetectionsTensorData =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: numDetectionsTensorSize)
            numDetectionsTensor?.data.copyBytes(to: numDetectionsTensorData);
            i = 0;
            print("numDetectionsTensor")
            for item in numDetectionsTensorData {
                print("Index: ", i);
                i += 1;
                print(item);
            }


        } catch {
            print("error:\(error)")
        }
    }


    /**
     normalized to the range
    */
    override func addPixelValue(red: UInt8, blue: UInt8, green: UInt8) {
        var normalizedRed = Float32(((red >> 16) & 0xFF))
        var normalizedGreen = Float32(((green >> 8) & 0xFF))
        var normalizedBlue = Float32(((blue) & 0xFF))

        let elementSize = MemoryLayout.size(ofValue: normalizedRed)
        var bytes = [UInt8](repeating: 0, count: elementSize)
        memcpy(&bytes, &normalizedRed, elementSize)
        imgData.append(&bytes, count: elementSize)
        memcpy(&bytes, &normalizedGreen, elementSize)
        imgData.append(&bytes, count: elementSize)
        memcpy(&bytes, &normalizedBlue, elementSize)
        imgData.append(&bytes, count: elementSize)
    }

    override func getImageSizeX() -> Int {
        Int(imageSize.width) / 2;
    }

    override func getImageSizeY() -> Int {
        Int(imageSize.height) / 2;
    }
}