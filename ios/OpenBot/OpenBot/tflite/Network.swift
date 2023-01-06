//
// Created by Sparsh Jain on 29/09/22.
//

import Foundation
import TensorFlowLite
import Accelerate
import CoreImage

class Network {

    /** Dimensions of inputs. */
    private let DIM_BATCH_SIZE: Int = 1;
    private let DIM_PIXEL_SIZE: Int = 3;

    var tflite: Interpreter?;
    var tfliteOptions: Interpreter.Options = Interpreter.Options();
    var gpuDelegate: MetalDelegate? = nil;
    var imageSize: CGSize;
    var intValues: [Int];
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
            if index < 0 {
                let modelsInDocumentDirectory = DataLogger.shared.getDocumentDirectoryInformation();
                for modelUrl in modelsInDocumentDirectory {
                    if modelUrl.lastPathComponent.contains(model.name){
                        tflite = try Interpreter(modelPath: modelUrl.absoluteString.replacingOccurrences(of: "file:///", with: ""), options: tfliteOptions, delegates: delegates);
                        try tflite?.allocateTensors()
                    }
                }
            }
            var fileName = "";
            if index >= 0 {
                fileName = String(split[index]);
            }
            let bundle = Bundle.main
            let path = bundle.path(forResource: fileName, ofType: "")
            if path != nil {
                tflite = try Interpreter(modelPath: path!, options: tfliteOptions, delegates: delegates);
                try tflite?.allocateTensors()
            } else {
                let modelsInDocumentDirectory = DataLogger.shared.getDocumentDirectoryInformation();
                for modelUrl in modelsInDocumentDirectory {
                   if modelUrl.lastPathComponent.contains(model.name){
                       tflite = try Interpreter(modelPath: modelUrl.absoluteString.replacingOccurrences(of: "file:///", with: ""), options: tfliteOptions, delegates: delegates);
                       try tflite?.allocateTensors()
                   }
                }
            }
            imageSize = model.getInputSize();
            intValues = [Int(imageSize.width) * Int(imageSize.height)];
        }
    }

    func getImageSizeX() -> Int {
        Int(imageSize.width);
    }

    func getImageSizeY() -> Int {
        Int(imageSize.height);
    }


    /// Returns the RGB data representation of the given image buffer with the specified `byteCount`.
    ///
    /// - Parameters
    ///   - buffer: The BGRA pixel buffer to convert to RGB data.
    ///   - isModelQuantized: Whether the model is quantized (i.e. fixed point values rather than
    ///       floating point values).
    /// - Returns: The RGB data representation of the image buffer or `nil` if the buffer could not be
    ///     converted.
    func rgbDataFromBuffer(
            _ buffer: CVPixelBuffer,
            isModelQuantized: Bool
    ) -> Data? {
        CVPixelBufferLockBaseAddress(buffer, .readOnly)
        defer {
            CVPixelBufferUnlockBaseAddress(buffer, .readOnly)
        }
        guard let sourceData = CVPixelBufferGetBaseAddress(buffer) else {
            return nil
        }

        let width = CVPixelBufferGetWidth(buffer)
        let height = CVPixelBufferGetHeight(buffer)
        let sourceBytesPerRow = CVPixelBufferGetBytesPerRow(buffer)
        let destinationChannelCount = 3
        let destinationBytesPerRow = destinationChannelCount * width

        var sourceBuffer = vImage_Buffer(data: sourceData,
                height: vImagePixelCount(height),
                width: vImagePixelCount(width),
                rowBytes: sourceBytesPerRow)

        guard let destinationData = malloc(height * destinationBytesPerRow) else {
            print("Error: out of memory")
            return nil
        }

        defer {
            free(destinationData)
        }

        var destinationBuffer = vImage_Buffer(data: destinationData,
                height: vImagePixelCount(height),
                width: vImagePixelCount(width),
                rowBytes: destinationBytesPerRow)

        if (CVPixelBufferGetPixelFormatType(buffer) == kCVPixelFormatType_32BGRA) {
            vImageConvert_BGRA8888toRGB888(&sourceBuffer, &destinationBuffer, UInt32(kvImageNoFlags))
        } else if (CVPixelBufferGetPixelFormatType(buffer) == kCVPixelFormatType_32ARGB) {
            vImageConvert_ARGB8888toRGB888(&sourceBuffer, &destinationBuffer, UInt32(kvImageNoFlags))
        }
        
        
        let byteData = Data(bytes: destinationBuffer.data, count: destinationBuffer.rowBytes * height)
        if isModelQuantized {
            return byteData
        }
        else {
            // Set up the destination buffer for the float values
            var floatBuffer = [Float](repeating: 0.0, count: byteData.count)

            // Convert the image buffer to float values
            vDSP_vfltu8(destinationBuffer.data, 1, &floatBuffer, 1, vDSP_Length(byteData.count))
            
            // Apply normalization factor
            var normalizationFactor: Float = 1.0 / getImageStd()
            vDSP_vsmul(floatBuffer, 1, &normalizationFactor, &floatBuffer, 1, vDSP_Length(floatBuffer.count))

            // Create a Data object from the float buffer
            return Data(bytes: floatBuffer, count: byteData.count * MemoryLayout<Float>.size)
        }

    }

    func getImageMean() -> Float32 {
        0.0;
    }

    func getImageStd() -> Float32 {
        255.0;
    }
}


