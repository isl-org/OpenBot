//
// Created by Sparsh Jain and Quentin Leboutet on 18/11/22.
//

import Foundation
import Accelerate
import CoreImage

extension CVPixelBuffer {
    
    /// Resizes a pixel buffer to a specific size. Finds the biggest square in the pixel buffer and advances rows based on it.
    ///
    /// - Parameter size: The size to resize the pixel buffer to.
    /// - Returns: A resized pixel buffer or nil if the resize operation failed.
    func resized(to size: CGSize) -> CVPixelBuffer? {
        
        // Get the size and pixel format of the input pixel buffer.
        let imageWidth = CVPixelBufferGetWidth(self)
        let imageHeight = CVPixelBufferGetHeight(self)
        let aspectRatio = CGFloat(imageWidth)/CGFloat(imageHeight)
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
        
        // Get the distance between the crop mask and the input image
        
        let scaleX = size.width / CGFloat(imageWidth)
        let scaleY = size.height / CGFloat(imageHeight)
        
        var scaledVImageBuffer: vImage_Buffer
        if (scaleY<=scaleX) { // scaling operation should bring the vertical borders of the scaled image in contact with the vertical borders of the crop mask
            let scaledImageRowBytes = Int(size.width) * imageChannels
            guard let scaledImageBytes = malloc(Int(size.width/aspectRatio) * scaledImageRowBytes) else {
                return nil
            }
            // Create a vImage buffer for the scaled image.
            scaledVImageBuffer = vImage_Buffer(data: scaledImageBytes, height: UInt(size.width/aspectRatio), width: UInt(size.width), rowBytes: scaledImageRowBytes)
        } else { // scaling operation should bring the horizontal borders of the scaled image in contact with the horizontal borders of the crop mask
            let scaledImageRowBytes = Int(size.height*aspectRatio) * imageChannels
            guard let scaledImageBytes = malloc(Int(size.height) * scaledImageRowBytes) else {
                return nil
            }
            // Create a vImage buffer for the scaled image.
            scaledVImageBuffer = vImage_Buffer(data: scaledImageBytes, height: UInt(size.height), width: UInt(size.height*aspectRatio), rowBytes: scaledImageRowBytes)
        }
        
        // Perform the scale operation on the input vImage buffer and store the result in the scaled vImage buffer.
        let scaleError = vImageScale_ARGB8888(&inputVImageBuffer, &scaledVImageBuffer, nil, vImage_Flags(0))
        
        // Get the cropping rectangle coordinates in the original buffer
        let cropX = Int((CGFloat(scaledVImageBuffer.width) - size.width) / 2)
        let cropY = Int((CGFloat(scaledVImageBuffer.height) - size.height) / 2)
        
        // Make sure the cropping rectangle is entirely within the original buffer
        guard cropX >= 0 && cropY >= 0 && cropX + Int(size.width) <= scaledVImageBuffer.width && cropY + Int(size.height) <= scaledVImageBuffer.height else {
            print("ERROR !!")
            return nil
        }
        
        // Initialize a new vImage_Buffer for the cropped region
        let croppedVImageBuffer = vImage_Buffer(data: scaledVImageBuffer.data.advanced(by: cropY * scaledVImageBuffer.rowBytes + cropX * imageChannels),
                                                height: vImagePixelCount(size.height),
                                                width: vImagePixelCount(size.width),
                                                rowBytes: scaledVImageBuffer.rowBytes)
        
        // Unlock the base address of the input pixel buffer.
        CVPixelBufferUnlockBaseAddress(self, CVPixelBufferLockFlags(rawValue: 0))
        
        // Check that the scale operation succeeded.
        guard scaleError == kvImageNoError else {
            return nil
        }
        
        // Create a CVPixelBuffer from the scaled vImage buffer.
        var croppedPixelBuffer: CVPixelBuffer?
        
        // Converts the scaled vImage buffer to CVPixelBuffer
        let conversionStatus = CVPixelBufferCreateWithBytes(nil, Int(size.width), Int(size.height), pixelBufferType, croppedVImageBuffer.data, croppedVImageBuffer.rowBytes, nil, nil, nil, &croppedPixelBuffer)
        
        // Check that the CVPixelBuffer creation succeeded.
        guard conversionStatus == kCVReturnSuccess else {
            return nil
        }
        return croppedPixelBuffer
    }
}
