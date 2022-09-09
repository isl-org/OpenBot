//
// Created by Nitish Yadav on 09/09/22.
//

import Foundation
import UIKit
class DeviceCurrentOrientation {
    static let shared : DeviceCurrentOrientation = DeviceCurrentOrientation()
    enum  Orientation{
        case landScape, portrait
    }

    func findDeviceOrientation(){
        let isPortrait = UIDevice.current.orientation.isPortrait
        switch (isPortrait) {
        case true:
            currentOrientation = Orientation.portrait
            break;
        case false:
            currentOrientation = Orientation.landScape
            break
        }
    }




}
