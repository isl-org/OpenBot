//
// Created by Sparsh Jain on 25/08/22.
//

struct Strings {
    //screens
    static let OpenBot: String = "OpenBot";
    static let freeRoam: String = "Free Roam";
    static let dataCollection: String = "Data Collection";
    static let controllerMapping: String = "Controller Mapping";
    static let autoPilot : String = "Auto Pilot"

    static let controller: String = "Controller"
    static let speed: String = "Speed"
    static let driveMode: String = "Drive Mode"
    static let gamepad: String = "Gamepad"
    static let phone: String = "Phone"
    static let joystick: String = "Joystick"
    static let game: String = "Game"
    static let dual: String = "Dual"
    static let slow: String = "Slow"
    static let medium: String = "Medium"
    static let fast: String = "Fast"
    static let logData: String = "Log Data"
    static let previewResolutionMedium: String = "Preview Resolution (1280 x 720)"
    static let previewResolutionLow: String = "Preview Resolution (960 x 540)"
    static let previewResolutionHigh: String = "Preview Resolution (1920 x 1080)"
    static let low: String = "Low"
    static let high: String = "High"
    static let modelResolution: String = "Model Resolution "
    static let server: String = "Server"
    static let expendSetting: String = "expendSetting"
    static let preview: String = "Preview"
    static let training: String = "Training"
    static let sensorData: String = "Sensor Data"
    static let vehicle: String = "Vehicle"
    static let gps: String = "GPS"
    static let accelerometer: String = "Accelerometer"
    static let magnetic: String = "Magnetic"
    static let gyroscope: String = "Gyroscope"
    static let delay: String = "Delay (ms)"
    static let images: String = "/images"
    static let sensor: String = "/sensor_data"
    static let timestamp: String = "timestamp[ns],frame\n"
    static let crop: String = "crop.jpeg"
    static let underscore: String = "_"
    static let comma: String = ","
    static let newLine: String = "\n"
    static let forwardSlash: String = "/"
    static let Autopilot: String = "Autopilot"
    static let ObjectTracking: String = "Object Tracking"
    static let autoMode : String = "Auto Mode"
    static let model : String = "Model"
    static let input : String = "Input"
    static let device : String = "Device"
    static let threads : String = "Threads"
    static  let object : String = "Object"


// Notifications
    static let controllerConnected: String = "connectedWithControllerSuccessfully";
    static let clickSetting: String = "clickSetting"
    static let cancelButton: String = "cancelButton"
    static let switchCamera: String = "switchCamera"
    static let ble: String = "ble"
    static let logDataNotify: String = "log_data"
    static let updateSpeedMode: String = "update_speed"
    static let updateControlMode: String = "update_control"
    static let updateDriveMode: String = "update_drive_mode"
    static let updateResolution: String = "updateResolution"
    static let updatePreview : String = "updatePreview"
    static let updateTraining : String = "updateTraining"
    static let updateSensorsForLog : String = "updateSensorsForLog"


// Screen Identifiers
    static let ScreenFreeRoam: String = "freeRoam";
    static let ScreenDataCollection: String = "dataCollection";
    static let ScreenControllerMapping: String = "controllerMapping";
    static let AutopilotFragment: String = "AutopilotFragment";
    static let ObjectTrackingFragment: String = "ObjectTrackingFragment";


// UIVIew Identifiers
    static let secondView: String = "secondView"
    static let bluetoothScreen : String = "bluetoothScreen"

// Logging Headers
    static let acceleration: String = "TimeStamp[ns] x[m/s^2], y[m/s^2], z[m/s^2]\n"
    static let locationCoordinates: String = "TimeStamp latitude, longitude\n"
    static let gyroscopeHeader: String = "TimeStamp[ns], x[rad/s], yx[rad/s], zx[rad/s]\n"

    static let magnetometer: String = "TimeStamp[ns] x[uT], y[uT], z[uT]\n"
    static let gpsHeader: String = "TimeStamp latitude, longitude, altitude[m], speed[m/s]\n"
    static let bumper: String = "timestamp[ns], bumper\n"
    static let ctrlLog: String = "timestamp[ns], leftCtrl, rightCtrl\n"
    static let indicator: String = "timestamp[ns], signal\n"
    static let inferenceTime: String = "frame, inferenceTime [ns]\n"
    static let light: String = "timestamp[ns], light[lux]\n"
    static let sonar: String = "timestamp[ns], distance[cm]\n"
    static let voltageHeader: String = "timestamp[ns], batteryVoltage\n"
    static let wheels: String = "timestamp[ns], leftWheel, rightWheel\n"
    static let motion: String = "timestamp[ns]"



}