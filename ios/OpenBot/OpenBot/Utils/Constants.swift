//
// Created by Sparsh Jain on 25/08/22.
//

import UIKit

struct Images {
    //Images
    static let freeRoam = UIImage(named: "freeRoam");
    static let dataCollection = UIImage(named: "dataCollection");
    static let controllerMapping = UIImage(named: "controllerMapping");
    static let autopilotIcon = UIImage(named: "autopilot");
    static let objectTrackingIcon = UIImage(named: "objectTracking");
    static let modelManagementIcon = UIImage(named: "modelManagement")
    static let bluetoothConnected = UIImage(named: "bluetoothConnected");
    static let bluetoothDisconnected = UIImage(named: "bluetoothDisconnected");
    static let frontCamera = UIImage(named: "frontCamera");
    static let ble = UIImage(named: "ble")
    static let settings = UIImage(named: "settings")
    static let closeIcon = UIImage(named: "closeIcon")
    static let phoneIcon = UIImage(named: "phone")
    static let gamepadIcon = UIImage(named: "gamepad")
    static let slowIcon = UIImage(named: "slow")
    static let mediumIcon = UIImage(named: "medium")
    static let fastIcon = UIImage(named: "fast")
    static let joystickIcon = UIImage(named: "joystick")
    static let dualDriveIcon = UIImage(named: "dual")
    static let driveIcon = UIImage(named: "drive")
    static let gameDriveIcon = UIImage(named: "game")
    static let downArrow = UIImage(systemName: "arrowtriangle.down.fill")
    static let minus = UIImage(systemName: "minus");
    static let plus = UIImage(systemName: "plus");
    static let edit = UIImage(named: "edit")
    static let upwardArrow = UIImage(named: "upward-arrow")

}

struct Colors {
    //Colors
    static let gridShadowColor = UIColor(named: "gridItemShadowColor");
    static let title = UIColor(named: "HomePageTitleColor");
    static let sonar = UIColor(named: "sonar")
    static let voltageDividerColor = UIColor(named: "voltageDivider")
    static let freeRoamButtonsColor = UIColor(named: "gamepad")
    static let border = UIColor(named: "borderColor")

}

struct Constants {

    //BLE
    static let openbotService = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";

// Game Data
    static let gameModes: [ModeItem] = [
        ModeItem(label: Strings.freeRoam, icon: Images.freeRoam!, identifier: Strings.ScreenFreeRoam),
        ModeItem(label: Strings.dataCollection, icon: Images.dataCollection!, identifier: Strings.ScreenDataCollection),
        ModeItem(label: Strings.controllerMapping, icon: Images.controllerMapping!, identifier: Strings.ScreenControllerMapping),
        ModeItem(label: Strings.Autopilot, icon: Images.autopilotIcon!, identifier: Strings.AutopilotFragment),
        ModeItem(label: Strings.ObjectTracking, icon: Images.objectTrackingIcon!, identifier: Strings.ObjectTrackingFragment),
        ModeItem(label: Strings.modelManagement, icon: Images.modelManagementIcon!, identifier: Strings.ScreenModelManagement)
    ];
    static let frameColors: [UIColor] = [UIColor.red, UIColor.orange, UIColor.blue, UIColor.green, UIColor.brown];
    static let autopilotMode = "AUTOPILOT";
    static let objectTrackingMode = "DETECTOR";
    static let types: [String] = ["AUTOPILOT", "DETECTOR", "NAVIGATION"];
    static let classes: [String] = ["AUTOPILOT_F", "MOBILENETV1_1_0_Q", "MOBILENETV3_S_Q", "YOLOV4", "NAVIGATION"]
    static let devices : [String] = ["CPU","XNNPACK"]
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
    case TOGGLE_LOGS, TOGGLE_NOISE, TOGGLE_CAMERA, TOGGLE_NETWORK
}

enum Resolutions {
    case low, medium, high
}

enum RuntimeDevice: String {
    case CPU = "CPU",
         GPU = "GPU",
         XNNPACK = "XNNPACK"
}


enum CLASS: String {
    case AUTOPILOT_F = "AUTOPILOT_F",
         MOBILENETV1_1_0_Q = "MOBILENETV1_1_0_Q",
         MOBILENETV3_S_Q = "MOBILENETV3_S_Q",
         YOLOV4 = "YOLOV4",
         NAVIGATION = "NAVIGATION"
}

enum TYPE: String {
    case AUTOPILOT = "AUTOPILOT",
         DETECTOR = "DETECTOR",
         NAVIGATION = "NAVIGATION"
}

enum PATH_TYPE: String {
    case URL = "URL",
         ASSET = "ASSET",
         FILE = "FILE"
}

