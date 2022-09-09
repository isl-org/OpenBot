//
// Created by Sparsh Jain on 25/08/22.
//

import UIKit

struct Images {
    //Images
    static let freeRoam = UIImage(named: "freeRoam");
    static let dataCollection = UIImage(named: "dataCollection");
    static let controllerMapping = UIImage(named: "controllerMapping");
    static let bluetoothConnected = UIImage(named: "bluetoothConnected");
    static let bluetoothDisconnected = UIImage(named: "bluetoothDisconnected");
}

struct Colors {
    //Colors
    static let gridShadowColor = UIColor(named: "gridItemShadowColor");
    static let title = UIColor(named: "HomePageTitleColor");
    static  let sonar = UIColor(named: "sonar")
    static let voltageDividerColor = UIColor(named: "voltageDivider")
    static let freeRoamButtonsColor = UIColor(named: "gamepad")

}

struct Constants {
// Game Data
    static let gameModes: [ModeItem] = [
        ModeItem(label: Strings.freeRoam, icon: Images.freeRoam!, identifier: Strings.ScreenFreeRoam),
        ModeItem(label: Strings.dataCollection, icon: Images.dataCollection!, identifier: Strings.ScreenDataCollection),
        ModeItem(label: Strings.controllerMapping, icon: Images.controllerMapping!, identifier: Strings.ScreenControllerMapping)
    ];
}

enum SpeedMode: Float {
    case slow = 128
    case medium = 192
    case fast = 255
}

enum ControlMode {
    case phone, gamepad
}

enum DriveMode {
    case joystick, gameController, dual
}
