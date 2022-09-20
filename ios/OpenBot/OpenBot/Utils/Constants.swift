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
    static let frontCamera = UIImage(named: "frontCamera");
    static let ble = UIImage(named: "ble")
    static let settings = UIImage(named: "settings")
    static let closeIcon = UIImage(named: "closeIcon")


}

struct Colors {
    //Colors
    static let gridShadowColor = UIColor(named: "gridItemShadowColor");
    static let title = UIColor(named: "HomePageTitleColor");
    static let sonar = UIColor(named: "sonar")
    static let voltageDividerColor = UIColor(named: "voltageDivider")
    static let freeRoamButtonsColor = UIColor(named: "gamepad")
    static let borderColor = UIColor(named: "borderColor")

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

enum IndicatorEvent: Int {
    case Left = -1
    case Right = 1
    case Stop = 0
}

enum ControlEvent {
    case STOP, FORWARD, BACKWARD, LEFT, RIGHT
}

enum Keymap: String {
    case KEY_TRIANGLE = "Triangle Button"
    case KEY_SQUARE = "Square Button"
    case KEY_CIRCLE = "Circle Button"
    case KEY_CROSS = "Cross Button"
    case KEY_R1 = "R1 Button"
    case KEY_R2 = "R2 Button"
    case KEY_R3 = "R3 Button"
    case KEY_L1 = "L1 Button"
    case KEY_L2 = "L2 Button"
    case KEY_L3 = "L3 Button"
    case KEY_Options = "OPTIONS Button"
    case KEY_Share = "SHARE Button"
}

enum CMD_Events {
    case TOGGLE_LOGS, TOGGLE_NOISE, TOGGLE_CAMERA
}