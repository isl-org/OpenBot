//
// Created by Nitish Yadav on 03/01/23.
//

import Foundation
import AVFoundation
import CoreImage

enum FilterType: Int {
    case None
    case Sepia
    case Monochrome
    case ColorControls

    mutating func next() -> FilterType {
        return FilterType(rawValue: rawValue + 1) ?? FilterType(rawValue: 0)!
    }
}

class CameraFilter {

    private var filter: CIFilter
    private let context: CIContext
    var filterType: FilterType = FilterType.None

    init() {
        self.filter = CIFilter()
        self.context = CIContext()
    }

    func apply(_ sampleBuffer: CVPixelBuffer) -> CVPixelBuffer? {
        if self.filterType == .None {
            return sampleBuffer
        }

        let ciimage = CIImage(cvPixelBuffer: sampleBuffer)
        let size: CGSize = ciimage.extent.size
        self.filter.setValue(ciimage, forKey: kCIInputImageKey)

        let filtered = self.filter.outputImage
        var pixelBuffer: CVPixelBuffer? = nil

        let options = [
            kCVPixelBufferCGImageCompatibilityKey as String: kCFBooleanTrue as Any,
            kCVPixelBufferCGBitmapContextCompatibilityKey as String: kCFBooleanTrue as Any
        ] as [String: Any]

        let status: CVReturn = CVPixelBufferCreate(kCFAllocatorDefault,
                Int(size.width),
                Int(size.height),
                kCVPixelFormatType_420YpCbCr8BiPlanarFullRange,
                options as CFDictionary?,
                &pixelBuffer)

        if (status == kCVReturnSuccess && pixelBuffer != nil) {
            self.context.render(filtered!, to: pixelBuffer!)
        }
        return pixelBuffer
    }

    func changeFilter(_ filterType: FilterType) {
        switch filterType {
        case .Sepia:
            self.filter = CIFilter(name: "CISepiaTone")!
            self.filter.setValue(0.8, forKey: "inputIntensity")
        case .Monochrome:
            self.filter = CIFilter(name: "CIColorMonochrome")!
            self.filter.setValue(CIColor(red: 0.75, green: 0.75, blue: 0.75), forKey: "inputColor")
            self.filter.setValue(1.0, forKey: "inputIntensity")
        case .ColorControls:
            self.filter = CIFilter(name: "CIColorControls")!
            self.filter.setValue(1.0, forKey: "inputSaturation")
            self.filter.setValue(0.5, forKey: "inputBrightness")
            self.filter.setValue(3.0, forKey: "inputContrast")
        case .None:
            break
        }
        self.filterType = filterType
    }
}
