//
// Created by Nitish Yadav on 03/01/23.
//

import Foundation
import UIKit

extension UIColor {

    func rectImage(width: CGFloat, height: CGFloat) -> UIImage {
        let rect = CGRect(x: 0, y: 0, width: width, height: height)
        UIGraphicsBeginImageContext(rect.size)
        let contextRef = UIGraphicsGetCurrentContext()
        contextRef?.setFillColor(cgColor)
        contextRef?.fill(rect)
        let img = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return img!
    }

    func circleImage(width: CGFloat, height: CGFloat) -> UIImage {
        let rect = CGRect(x: 0, y: 0, width: width, height: height)
        UIGraphicsBeginImageContextWithOptions(rect.size, false, 0)
        let contextRef = UIGraphicsGetCurrentContext()
        contextRef?.setFillColor(cgColor)
        contextRef?.fillEllipse(in: rect)
        let img = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return img!
    }
}

