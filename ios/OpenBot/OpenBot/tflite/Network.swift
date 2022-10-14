//
// Created by Sparsh Jain on 29/09/22.
//

import Foundation
import TensorFlowLite

class Network {

    /** Dimensions of inputs. */
    private let DIM_BATCH_SIZE: Int = 1;
    private let DIM_PIXEL_SIZE: Int = 3;

    var tflite: Interpreter?;
    var tfliteOptions: Interpreter.Options = Interpreter.Options();
    var gpuDelegate: MetalDelegate? = nil;
    private var imageSize: CGSize;
    var intValues: [Int];
    var imgData: Data = Data();

    init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        do {
            var delegates: [Delegate] = [];
            switch (device) {
            case .XNNPACK:
                tfliteOptions.isXNNPackEnabled = true;
                break;
            case .GPU:
                gpuDelegate = MetalDelegate();
                delegates.append(gpuDelegate!);
                break;
            case .CPU:
                break;
            }
            tfliteOptions.threadCount = numThreads;
            let split = model.path.split(separator: "/");
            let index = split.count - 1;
            let fileName = String(split[index]);
            let bundle = Bundle.main
            let path = bundle.path(forResource: fileName, ofType: "")
            if let file = path {
                tflite = try Interpreter(modelPath: file, options: tfliteOptions, delegates: delegates);
                try tflite?.allocateTensors()
            } else {
                print("file not found");
            }

            imageSize = model.getInputSize();
            intValues = [Int(imageSize.width) * Int(imageSize.height)];
        }
    }

    func convertImageToData(image: CGImage) {
        let startTime = returnCurrentTimestamp();
        var inputData = Data()
        guard let context = CGContext(
                data: nil,
                width: image.width, height: image.height,
                bitsPerComponent: 8, bytesPerRow: image.width * 4,
                space: CGColorSpaceCreateDeviceRGB(),
                bitmapInfo: CGImageAlphaInfo.noneSkipFirst.rawValue
        )
        else {
            return
        }

        context.draw(image, in: CGRect(x: 0, y: 0, width: image.width, height: image.height))
        guard let imageData = context.data else {
            return
        }
        for row in 0..<getImageSizeX() {
            for col in 0..<getImageSizeY() {
                let offset = 4 * (row * context.width + col)
                // (Ignore offset 0, the unused alpha channel)
                let red = imageData.load(fromByteOffset: offset + 1, as: UInt8.self)
                let green = imageData.load(fromByteOffset: offset + 2, as: UInt8.self)
                let blue = imageData.load(fromByteOffset: offset + 3, as: UInt8.self)

                // Normalize channel values to [0.0, 1.0]. This requirement varies
                // by model. For example, some models might require values to be
                // normalized to the range [-1.0, 1.0] instead, and others might
                // require fixed-point values or the original bytes.
                var normalizedRed = Float32(red) / 255.0
                var normalizedGreen = Float32(green) / 255.0
                var normalizedBlue = Float32(blue) / 255.0

                // Append normalized values to Data object in RGB order.
                let elementSize = MemoryLayout.size(ofValue: normalizedRed)
                var bytes = [UInt8](repeating: 0, count: elementSize)
                memcpy(&bytes, &normalizedRed, elementSize)
                inputData.append(&bytes, count: elementSize)
                memcpy(&bytes, &normalizedGreen, elementSize)
                inputData.append(&bytes, count: elementSize)
                memcpy(&bytes, &normalizedBlue, elementSize)
                inputData.append(&bytes, count: elementSize)
            }
        }
        let endTime = returnCurrentTimestamp();
        imgData = inputData;

        print("Timecost to convert image: ", endTime - startTime);
    }

    func getImageSizeX() -> Int {
        Int(imageSize.width);
    }

    func getImageSizeY() -> Int {
        Int(imageSize.height);
    }
}