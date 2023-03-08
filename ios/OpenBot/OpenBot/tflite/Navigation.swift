//
// Created by Nitish Yadav on 21/02/23.
//

import Foundation
import TensorFlowLite

var temp: CVPixelBuffer!

class Navigation: Network {
    let IMAGE_MEAN: Float = 0.0;
    let IMAGE_STD: Float = 255.0;
    private var goalIndex: Int = 0;
    private var imgIndex: Int = 0;
    var goalData: Data?
    override init(model: Model, device: RuntimeDevice, numThreads: Int) {
        try! super.init(model: model, device: device, numThreads: numThreads)
        do {
            let index0 = try tflite?.input(at: 0);
            let index1 = try tflite?.input(at: 1);
            assignIndex(tensor: index0, index: 0);
            assignIndex(tensor: index1, index: 1);
        } catch {
            print("error:\(error)")
        }
    }

    func assignIndex(tensor: Tensor?, index: Int) {
        switch (tensor?.name) {
        case "serving_default_goal_input:0", "cmd_input":
            goalIndex = index;
            break
        case "serving_default_img_input:0", "img_input":
            imgIndex = index;
            break
        default:
            break;
        }
    }

    var i = 1;

    func recognizeImage(pixelBuffer: CVPixelBuffer, goalDistance: Float, goalSin: Float, goalCos: Float) -> Control {
        do {
            convertGoalToData(goalDistance: goalDistance, goalSin: goalSin, goalCos: goalCos);
            let inputTensor = try tflite!.input(at: imgIndex);
            let croppedPixelBuffer = pixelBuffer.resizePixelBuffer(pixelBuffer, width: 160, height: 90);
            temp = croppedPixelBuffer;
            guard let rgbData = rgbDataFromBuffer(croppedPixelBuffer!, isModelQuantized: inputTensor.dataType == .uInt8) else {
                return Control(left: 0, right: 0)
            }
            try tflite?.copy(goalData!, toInputAt: goalIndex);
            try tflite?.copy(rgbData, toInputAt: imgIndex);
            try tflite?.invoke();
            let outputTensor = try tflite?.output(at: 0);
            let outputSize = outputTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let outputData = UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputSize)
            _ = outputTensor?.data.copyBytes(to: outputData);
            return Control(left: outputData[0], right: outputData[1])
        } catch {
            print("error:\(error)")
            return Control(left: 0, right: 0)

        };
    }


    func returnCroppedImage(croppedBuffer: CVPixelBuffer) -> CVPixelBuffer {
        croppedBuffer
    }

    func convertGoalToData(goalDistance: Float, goalSin: Float, goalCos: Float) {
        var goalBytes: [UInt8] = []
        let distanceBytes = withUnsafeBytes(of: goalDistance) {
            Array($0)
        }
        let sinBytes = withUnsafeBytes(of: goalSin) {
            Array($0)
        }
        let cosBytes = withUnsafeBytes(of: goalCos) {
            Array($0)
        }
        goalBytes.append(contentsOf: distanceBytes)
        goalBytes.append(contentsOf: sinBytes)
        goalBytes.append(contentsOf: cosBytes)
        goalData = Data(bytes: &goalBytes, count: goalBytes.count)
    }


}