//
// Created by Nitish Yadav on 09/09/22.
//

import Foundation
import UIKit

class DeviceCurrentOrientation {
    static let shared: DeviceCurrentOrientation = DeviceCurrentOrientation()
    
    func findDeviceOrientation() {
        let isPortrait = UIDevice.current.orientation.isPortrait
        let isLandscape = UIDevice.current.orientation.isLandscape
        if (isPortrait == false && isLandscape == false) {
            return // improper orientation (i.e. camera facing the ground or the sky)
        }
        switch UIDevice.current.orientation {
        case .landscapeLeft:
            currentOrientation = .landscapeLeft
            break
        case .landscapeRight:
            currentOrientation = .landscapeRight
            break
        case .portrait:
            currentOrientation = .portrait
            break
        case .portraitUpsideDown:
            currentOrientation = .portraitUpsideDown
            break
        default:
            currentOrientation = .unknown
            break
        }
    }
}
