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

        // Not quantized, convert to floats
        let bytes = Array<UInt8>(unsafeData: byteData)!
        var floats = [Float32]()
        for i in 0..<bytes.count {
            floats.append((Float32(bytes[i]) - 0) / 255)
        }

        return Data(copyingBufferOf: floats)
    }


}

extension UIImage {
    func pixelData() -> [UInt8]? {
        guard let cgImage = cgImage else {
            return nil
        }
        let w = cgImage.width
        let h = cgImage.height

        let bytesPerPixel = 4
        let bytesPerRow = bytesPerPixel * w
        let bitsPerComponent = 8
        var rawBytes: [UInt8] = [UInt8](repeating: 0, count: w * h * 4)
        rawBytes.withUnsafeMutableBytes { ptr in
            if let cgImage = self.cgImage,
               let context = CGContext(data: ptr.baseAddress,
                       width: w,
                       height: h,
                       bitsPerComponent: bitsPerComponent,
                       bytesPerRow: bytesPerRow,
                       space: CGColorSpaceCreateDeviceRGB(),
                       bitmapInfo: CGImageAlphaInfo.premultipliedLast.rawValue) {
                let rect = CGRect(x: 0, y: 0, width: w, height: h)
                context.draw(cgImage, in: rect)
            }
        }
        return rawBytes
    }

    func resized(to newSize: CGSize, scale: CGFloat = 1) -> UIImage {
        let format = UIGraphicsImageRendererFormat.default()
        format.scale = scale
        let renderer = UIGraphicsImageRenderer(size: newSize, format: format)
        let image = renderer.image { _ in
            draw(in: CGRect(origin: .zero, size: newSize))
        }
        return image
    }

    public func pixelBuffer(width: Int, height: Int) -> CVPixelBuffer? {
        var maybePixelBuffer: CVPixelBuffer?
        let attrs = [kCVPixelBufferCGImageCompatibilityKey: kCFBooleanTrue,
                     kCVPixelBufferCGBitmapContextCompatibilityKey: kCFBooleanTrue]
        let status = CVPixelBufferCreate(kCFAllocatorDefault,
                Int(width),
                Int(height),
                kCVPixelFormatType_32ARGB,
                attrs as CFDictionary,
                &maybePixelBuffer)

        guard status == kCVReturnSuccess, let pixelBuffer = maybePixelBuffer else {
            return nil
        }

        CVPixelBufferLockBaseAddress(pixelBuffer, CVPixelBufferLockFlags(rawValue: 0))
        let pixelData = CVPixelBufferGetBaseAddress(pixelBuffer)

        guard let context = CGContext(data: pixelData,
                width: Int(width),
                height: Int(height),
                bitsPerComponent: 8,
                bytesPerRow: CVPixelBufferGetBytesPerRow(pixelBuffer),
                space: CGColorSpaceCreateDeviceRGB(),
                bitmapInfo: CGImageAlphaInfo.noneSkipFirst.rawValue)
        else {
            return nil
        }

        context.translateBy(x: 0, y: CGFloat(height))
        context.scaleBy(x: 1, y: -1)

        UIGraphicsPushContext(context)
        self.draw(in: CGRect(x: 0, y: 0, width: width, height: height))
        UIGraphicsPopContext()
        CVPixelBufferUnlockBaseAddress(pixelBuffer, CVPixelBufferLockFlags(rawValue: 0))
        return pixelBuffer
    }
}


extension Data {
    /// Creates a new buffer by copying the buffer pointer of the given array.
    ///
    /// - Warning: The given array's element type `T` must be trivial in that it can be copied bit
    ///     for bit with no indirection or reference-counting operations; otherwise, reinterpreting
    ///     data from the resulting buffer has undefined behavior.
    /// - Parameter array: An array with elements of type `T`.
    init<T>(copyingBufferOf array: [T]) {
        self = array.withUnsafeBufferPointer(Data.init)
    }
}

extension Array {
    /// Creates a new array from the bytes of the given unsafe data.
    ///
    /// - Warning: The array's `Element` type must be trivial in that it can be copied bit for bit
    ///     with no indirection or reference-counting operations; otherwise, copying the raw bytes in
    ///     the `unsafeData`'s buffer to a new array returns an unsafe copy.
    /// - Note: Returns `nil` if `unsafeData.count` is not a multiple of
    ///     `MemoryLayout<Element>.stride`.
    /// - Parameter unsafeData: The data containing the bytes to turn into an array.
    init?(unsafeData: Data) {
        guard unsafeData.count % MemoryLayout<Element>.stride == 0 else {
            return nil
        }
        self = unsafeData.withUnsafeBytes {
            .init($0.bindMemory(to: Element.self))
        }
    }
}
