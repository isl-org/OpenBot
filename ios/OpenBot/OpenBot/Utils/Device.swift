//
// Created by Nitish Yadav on 13/09/22.
//

import UIKit

enum Device {
    case iPhoneSE
    case iPhone8
    case iPhone8Plus
    case iPhone11Pro
    case iPhone11ProMax
    case iPhone11ProLandscape
    case iphone8Landscape
    static let baseScreenSize: Device = .iPhoneSE
}

extension Device: RawRepresentable {
    typealias RawValue = CGSize

    init?(rawValue: CGSize) {
        switch rawValue {
        case CGSize(width: 320, height: 568):
            self = .iPhoneSE
        case CGSize(width: 375, height: 667):
            self = .iPhone8
        case CGSize(width: 667, height: 375):
            self = .iphone8Landscape
        case CGSize(width: 414, height: 736):
            self = .iPhone8Plus
        case CGSize(width: 375, height: 812):
            self = .iPhone11Pro
        case CGSize(width: 812, height: 375):
            self = .iPhone11ProLandscape
        case CGSize(width: 414, height: 896):
            self = .iPhone11ProMax
        default:
            return nil
        }
    }

    var rawValue: CGSize {
        switch self {
        case .iPhoneSE:
            return CGSize(width: 320, height: 568)
        case .iPhone8:
            return CGSize(width: 375, height: 667)
        case .iPhone8Plus:
            return CGSize(width: 414, height: 736)
        case .iPhone11Pro:
            return CGSize(width: 375, height: 812)
        case .iPhone11ProMax:
            return CGSize(width: 414, height: 896)
        case .iPhone11ProLandscape:
            return CGSize(width: 812, height: 414)
        case .iphone8Landscape:
           return CGSize(width: 667, height: 375)
        }
    }
}