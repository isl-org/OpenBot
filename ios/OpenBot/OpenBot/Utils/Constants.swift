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
    static let robotInfoIcon = UIImage(named: "robotInfo")
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
    static let openBotLogo = UIImage(named: "openBotLogo");
    
}

struct Colors {
    //Colors
    static let gridShadowColor = UIColor(named: "gridItemShadowColor");
    static let title = UIColor(named: "HomePageTitleColor");
    static let titleDeactivated = UIColor(named: "HomePageTitleColorDeactivated");
    static let sonar = UIColor(named: "sonar")
    static let voltageDividerColor = UIColor(named: "voltageDivider")
    static let freeRoamButtonsColor = UIColor(named: "gamepad")
    static let border = UIColor(named: "borderColor")
    static let bdColor = UIColor(named: "bdColor")
    static let navigationColor = UIColor(named: "navigationColor")
    static let modelDetail = UIColor(named: "modelDetailPopup")
    
}

struct Constants {
    
    //BLE
    static let openbotService = "61653dc3-4021-4d1e-ba83-8b4eec61d613";
    static let openbotService_RX = "06386c14-86ea-4d71-811c-48f97c58f8c9";
    static let openbotService_TX = "9bf1103b-834c-47cf-b149-c9e4bcf778a7";
    
    // Game Data
    static let gameModes: [ModeItem] = [
        ModeItem(label: Strings.freeRoam, icon: Images.freeRoam!, identifier: Strings.ScreenFreeRoam),
        ModeItem(label: Strings.dataCollection, icon: Images.dataCollection!, identifier: Strings.ScreenDataCollection),
        ModeItem(label: Strings.controllerMapping, icon: Images.controllerMapping!, identifier: Strings.ScreenControllerMapping),
        ModeItem(label: Strings.Autopilot, icon: Images.autopilotIcon!, identifier: Strings.AutopilotFragment),
        ModeItem(label: Strings.ObjectTracking, icon: Images.objectTrackingIcon!, identifier: Strings.ObjectTrackingFragment),
        ModeItem(label: Strings.modelManagement, icon: Images.modelManagementIcon!, identifier: Strings.ScreenModelManagement),
        ModeItem(label: Strings.robotInfo, icon: Images.robotInfoIcon!, identifier: Strings.ScreenRobotInfo),
    ];
    static let frameColors: [UIColor] = [UIColor.red, UIColor.orange, UIColor.blue, UIColor.green, UIColor.brown];
    static let autopilotMode = "AUTOPILOT";
    static let objectTrackingMode = "DETECTOR";
    static let types: [String] = ["AUTOPILOT", "DETECTOR", "NAVIGATION"];
    static let classes: [String] = ["AUTOPILOT_F", "MOBILENET", "YOLOV4", "YOLOV5", "EFFICIENTDET", "NAVIGATION"]
    static let devices: [String] = ["CPU", "XNNPACK"]
}

enum SpeedMode: Float {
    case SLOW = 128
    case NORMAL = 192
    case FAST = 255
}

enum ControlMode {
    case PHONE, GAMEPAD
}

enum DriveMode {
    case JOYSTICK, GAME, DUAL
}

enum IndicatorEvent: Int {
    case LEFT = -1
    case RIGHT = 1
    case STOP = 0
}

enum ControlEvent {
    case STOP, FORWARD, BACKWARD, LEFT, RIGHT
}

enum Keymap: String {
    case KEYCODE_BUTTON_Y = "Triangle Button"
    case KEYCODE_BUTTON_X = "Square Button"
    case KEYCODE_BUTTON_B = "Circle Button"
    case KEYCODE_BUTTON_A = "Cross Button"
    case KEYCODE_BUTTON_R1 = "R1 Button"
    case KEYCODE_BUTTON_R2 = "R2 Button"
    case KEYCODE_BUTTON_L1 = "L1 Button"
    case KEYCODE_BUTTON_L2 = "L2 Button"
    case KEYCODE_BUTTON_THUMBL = "L3 Button"
    case KEYCODE_BUTTON_THUMBR = "R3 Button"
    case KEYCODE_BUTTON_START = "OPTIONS Button"
    case KEYCODE_BUTTON_SELECT = "SELECT Button"
    case KEY_Share = "SHARE Button"
    case CMD_INDICATOR_STOP = "INDICATOR_STOP";
}

enum CMD_Events {
    case TOGGLE_LOGS, TOGGLE_NOISE, TOGGLE_CAMERA, TOGGLE_NETWORK, CMD_SPEED_UP, CMD_SPEED_DOWN, CMD_DRIVE_MODE
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
         MOBILENET = "MOBILENET",
         YOLOV4 = "YOLOV4",
         YOLOV5 = "YOLOV5",
         EFFICIENTDET = "EFFICIENTDET",
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

struct FileName {
    static let accelerator: String = "accelerometerLog.txt";
    static let magnetic: String = "magneticLog.txt";
    static let gyroscopeLog: String = "gyroscopeLog.txt";
    static let gpsLog: String = "gpsLog.txt";
    static let bumperLog: String = "bumperLog.txt";
    static let ctrlLog: String = "ctrlLog.txt";
    static let indicatorLog: String = "indicatorLog.txt";
    static let inferenceLog: String = "inferenceLog.txt"
    static let lightLog: String = "lightLog.txt";
    static let motionLog: String = "motionLog.txt";
    static let sonarLog: String = "sonarLog.txt";
    static let voltageLog: String = "voltageLog.txt";
    static let wheelsLog: String = "wheelsLog.txt"
    
}
