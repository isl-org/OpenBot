//
//  DetectorFloatYoloV5.swift
//  OpenBot
//
//  Created by Quentin Leboutet on 15.01.23.
//

import Foundation
import TensorFlowLite

class DetectorFloatYoloV5: Detector {

    /// Additional normalization of the used input.
    let IMAGE_MEAN: Float = 0.0;
    let IMAGE_STD: Float = 255.0;

    private var inputSize: Int = -1
    private var output_box: Int = -1
    var numClass: Int = -1
    var isModelQuantized: Bool = false
    private var inputScale: Float = 0
    private var inputZeroPoint: Int = -1
    private var outputScale: Float = 0
    private var outputZeroPoint: Int = -1
    private var outData: UnsafeMutableBufferPointer<UInt8>?;

    /// Initializes a DetectorYoloV5.
    override init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        try super.init(model: model, device: device, numThreads: numThreads)
        inputSize = self.getImageSizeX()
        let tmp_0: Float = pow((Float(inputSize) / 32), 2) * 3
        let tmp_1: Float = pow((Float(inputSize) / 16), 2) * 3
        let tmp_2: Float = pow((Float(inputSize) / 8), 2) * 3
        output_box = Int(tmp_0 + tmp_1 + tmp_2)
    }

    override func getMaintainAspect() -> Bool {
        return false;
    }

    override func getCropRect() -> CGRect {
        return CGRect(x: 0, y: 0, width: 0, height: 0);
    }

    override func getLabelPath() -> String {
        "coco.txt";
    }

    override func getNumBytesPerChannel() -> Int {
        parseTFlite();
        return isModelQuantized ? 1 : 4;
    }

    override func getNumDetections() -> Int {
        NUM_DETECTIONS;
    }

    override func parseTFlite() {
        inputScale = try! tflite?.input(at: 0).quantizationParameters?.scale ?? 0
        inputZeroPoint = try! tflite?.input(at: 0).quantizationParameters?.zeroPoint ?? 0
        outputScale = try! tflite?.output(at: 0).quantizationParameters?.scale ?? 0
        outputZeroPoint = try! tflite?.output(at: 0).quantizationParameters?.zeroPoint ?? 0
        isModelQuantized = (Int(outputScale) + outputZeroPoint) != 0
        let shape = try! tflite?.output(at: 0).shape.dimensions
        numClass = shape![shape!.count - 1] - 5
        let outputSize = try! tflite?.output(at: 0).data.count ?? 0
        outData = UnsafeMutableBufferPointer<UInt8>.allocate(capacity: outputSize)
    }

    override func feedData() throws {

        do {
            _ = try! tflite?.output(at: 0).data.copyBytes(to: outData!);
        } catch {
            print("error:\(error)")
        }
    }

    override func getRecognitions(className: String, width: Int, height: Int) -> [Recognition] {

        var out = Array(repeating: Array<Float32>(repeating: 0, count: numClass + 5), count: output_box);

        var recognitions: [Recognition] = [];
        var classes = Array<Float32>(repeating: 0, count: numClass);
        let output_channels: Int = numClass + 5


        for i in (0..<output_box) {
            for j in (0..<numClass + 5) {
                if (isModelQuantized) {
                    out[i][j] = outputScale * Float(Int((outData![i * output_channels + j] & 0xFF)) - outputZeroPoint)
                } else {
                    out[i][j] = Float(outData![i * output_channels + j])
                }
            }


            /// Denormalize xywh
            for j in (0..<4) {
                out[i][j] *= Float(inputSize)
            }
        }

        for i in (0..<output_box) {
            let confidence: Float = out[i][4]
            var classId = -1;
            var maxClass: Float = 0;

            for c in (0..<labels.count) {
                classes[c] = out[i][5 + c]
            }

            for c in (0..<labels.count) {
                if (classes[c] > maxClass) {
                    classId = c;
                    maxClass = classes[c];
                }
            }

            let score: Float = maxClass * confidence
            if (score > getObjThresh()) {
                if (classId > -1 && className == labels[classId]) {
                    let xPos = out[i][0]
                    let yPos = out[i][1]
                    let w = out[i][2]
                    let h = out[i][3]
                    let detection: CGRect = CGRect(x: CGFloat(max(0, xPos - w / 2)), y: CGFloat(max(0, yPos - h / 2)), width: CGFloat(w), height: CGFloat(h)).applying(CGAffineTransform(scaleX: CGFloat(width) / CGFloat(getImageSizeX()), y: CGFloat(height) / CGFloat(getImageSizeY())));
                    recognitions.append(Recognition(id: String(i), title: labels[classId], confidence: score, location: detection, classId: classId));
                }
            }
        }

        return nms(recognitions: recognitions);
    }

    override func getImageMean() -> Float {
        IMAGE_MEAN;
    }

    override func getImageStd() -> Float {
        IMAGE_STD;
    }
}
