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
    var imageSize: CGSize;
    var intValues: [Int];
    var imgData: Data = Data();
    var outputMap: [Int: Any] = [Int: Any]();

    init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        do {
            var delegates: [Delegate] = [];
            switch (device) {
            case .XNNPACK:
                tfliteOptions.isXNNPackEnabled = true;
                break;
            case .GPU:
                var gpuOptions = MetalDelegate.Options();
                gpuOptions.isQuantizationEnabled = false;
                gpuOptions.isPrecisionLossAllowed = false;
                tfliteOptions.threadCount = 0;
                gpuOptions.waitType = .none;
                gpuDelegate = MetalDelegate(options: gpuOptions);
                delegates.append(gpuDelegate!);
                break;
            case .CPU:
                tfliteOptions.threadCount = numThreads;
                break;
            }
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
        do {
            imgData.removeAll();
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
                    let offset = (row * context.width + col)
                    // (Ignore offset 0, the unused alpha channel)
                    let red = imageData.load(fromByteOffset: offset + 1, as: UInt8.self)
                    let green = imageData.load(fromByteOffset: offset + 2, as: UInt8.self)
                    let blue = imageData.load(fromByteOffset: offset + 3, as: UInt8.self)

                    // normalized to the range
                    addPixelValue(red: red, blue: blue, green: green)
                }
            }
        } catch {
            print("error:\(error)")
        }
    }

    func addPixelValue(red: UInt8, blue: UInt8, green: UInt8) {

    }

    func getImageSizeX() -> Int {
        Int(imageSize.width);
    }

    func getImageSizeY() -> Int {
        Int(imageSize.height);
    }
}
