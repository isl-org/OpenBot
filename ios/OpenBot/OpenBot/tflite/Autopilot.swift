//
// Created by Sparsh Jain on 29/09/22.
//

import Foundation
import TensorFlowLite

class Autopilot: Network {
    var cmdIndex: Int = -1;
    var imgIndex: Int = -1;
    let IMAGE_MEAN: Float = 0.0;
    let IMAGE_STD: Float = 255.0;

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
        case "serving_default_cmd_input:0", "cmd_input":
            cmdIndex = index;
            break
        case "serving_default_img_input:0", "img_input":
            imgIndex = index;
            break
        default:
            break;
        }
    }

    /// Propagate an image through a neural network to recognize objects of a predefined class.
    ///
    /// - Parameters
    ///   - pixelBuffer: The buffer containing the image.
    ///   - indicator: A high-level scalar command (such that: -1: left, 0: forward, +1: right) to be sent as an input to the driving policy
    /// - Returns: A list of the recognized objects for a given label
    ///
    func recognizeImage(pixelBuffer: CVPixelBuffer, indicator: Float) -> Control {

        /// Crops the image to the biggest square in the center and scales it down to model dimensions.
        let scaledSize = CGSize(width: getImageSizeX(), height: getImageSizeY())
        guard let scaledPixelBuffer = pixelBuffer.resized(to: scaledSize) else {
            return Control(left: 0, right: 0)
        }

        do {
            /// Get indicator data
            var indicatorData: Data = Data();
            indicatorData.append(contentsOf: indicator.bytes);

            /// Pre-proccess input image (orientation, scaling, cropping...).
            let inputTensor = try tflite!.input(at: imgIndex);
            guard let rgbData = rgbDataFromBuffer(scaledPixelBuffer, isModelQuantized: inputTensor.dataType == .uInt8) else {
                return Control(left: 0, right: 0)
            }

            /// Copy the input data into TensorFlow.
            try tflite?.copy(indicatorData, toInputAt: cmdIndex);
            try tflite?.copy(rgbData, toInputAt: imgIndex);

            /// Run the inference call.
            try tflite?.invoke();

            /// Post-Processing.
            let outputTensor = try tflite?.output(at: 0);
            let outputSize = outputTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let outputData = UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputSize)
            _ = outputTensor?.data.copyBytes(to: outputData)
            return Control(left: outputData[0], right: outputData[1])

        } catch {

            print("error:\(error)")
            return Control(left: 0, right: 0)

        };
    }

    override func getImageMean() -> Float {
        IMAGE_MEAN;
    }

    override func getImageStd() -> Float {
        IMAGE_STD;
    }

}
