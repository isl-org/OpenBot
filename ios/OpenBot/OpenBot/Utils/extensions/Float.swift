//
// Created by Nitish Yadav on 13/09/22.
//


import UIKit
extension CGFloat {
    var adaptedFontSize: CGFloat {
        adapted(dimensionSize: self, to: dimension)
    }
}

extension Float {
    var bytes: [UInt8] {
        withUnsafeBytes(of: self, Array.init)
    }
}