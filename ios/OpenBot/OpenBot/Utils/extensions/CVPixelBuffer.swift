//
// Created by Sparsh Jain on 18/11/22.
//

import Foundation
import Accelerate

extension CVPixelBuffer {
    
    /// Resizes a pixel buffer to a specific size. Finds the biggest square in the pixel buffer and advances rows based on it.
    /// 
    /// - Parameter size: The size to resize the pixel buffer to.
    /// - Returns: A resized pixel buffer or nil if the resize operation failed.
    func resized(to size: CGSize) -> CVPixelBuffer? {
        
        // Get the size and pixel format of the input pixel buffer.
        let imageWidth = CVPixelBufferGetWidth(self)
        let imageHeight = CVPixelBufferGetHeight(self)
        let pixelBufferType = CVPixelBufferGetPixelFormatType(self)
        
        // Check that the pixel format is supported.
        assert(pixelBufferType == kCVPixelFormatType_32BGRA || pixelBufferType == kCVPixelFormatType_32ARGB)
        
        // Get the number of bytes per row and channels of the input pixel buffer.
        let inputImageRowBytes = CVPixelBufferGetBytesPerRow(self)
        let imageChannels = 4
        
        // Lock the base address of the input pixel buffer.
        CVPixelBufferLockBaseAddress(self, CVPixelBufferLockFlags(rawValue: 0))
        
        // Get the base address of the input pixel buffer.
        guard let inputBaseAddress = CVPixelBufferGetBaseAddress(self) else {
            return nil
        }
        
        // Create a vImage buffer from the input pixel buffer.
        var inputVImageBuffer = vImage_Buffer(data: inputBaseAddress, height: UInt(imageHeight), width: UInt(imageWidth), rowBytes: inputImageRowBytes)
        
        // Calculate the number of bytes per row and total bytes of the scaled image.
        let scaledImageRowBytes = Int(size.width) * imageChannels
        guard let scaledImageBytes = malloc(Int(size.height) * scaledImageRowBytes) else {
            return nil
        }
        
        // Create a vImage buffer for the scaled image.
        var scaledVImageBuffer = vImage_Buffer(data: scaledImageBytes, height: UInt(size.height), width: UInt(size.width), rowBytes: scaledImageRowBytes)
        
        // Perform the scale operation on the input vImage buffer and store the result in the scaled vImage buffer.
        let scaleError = vImageScale_ARGB8888(&inputVImageBuffer, &scaledVImageBuffer, nil, vImage_Flags(0))
        
        // Unlock the base address of the input pixel buffer.
        CVPixelBufferUnlockBaseAddress(self, CVPixelBufferLockFlags(rawValue: 0))
        
        // Check that the scale operation succeeded.
        guard scaleError == kvImageNoError else {
            return nil
        }
        
        // Create a CVPixelBuffer from the scaled vImage buffer.
        let releaseCallBack: CVPixelBufferReleaseBytesCallback = { mutablePointer, pointer in
            
            if let pointer = pointer {
                free(UnsafeMutableRawPointer(mutating: pointer))
            }
        }
        var scaledPixelBuffer: CVPixelBuffer?
        
        // Converts the scaled vImage buffer to CVPixelBuffer
        let conversionStatus = CVPixelBufferCreateWithBytes(nil, Int(size.width), Int(size.height), pixelBufferType, scaledImageBytes, scaledImageRowBytes, releaseCallBack, nil, nil, &scaledPixelBuffer)
        
        // Check that the CVPixelBuffer creation succeeded.
        guard conversionStatus == kCVReturnSuccess else {
            free(scaledImageBytes)
            return nil
        }
        return scaledPixelBuffer
    }
}
