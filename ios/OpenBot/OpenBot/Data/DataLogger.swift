//
// Created by Nitish Yadav on 06/09/22.
//

import Foundation
import UIKit
class DataLogger {
    static let shared : DataLogger = DataLogger()
    var enabled: Bool = false;
    let sensorData = sensorDataRetrieve.shared
    let bluetooth = bluetoothDataController.shared;
    var carSensorsData : String = ""
    var acceleration : String = ""
    var gyroscope : String = ""
    var magnetometer : String = ""
    var locationCoordinates : String = ""
    var gps : String = ""
    var baseDirectory :  String = ""
    var bumper : String = ""
    var ctrlLog : String = ""
    var indicator : String = ""
    var inferenceTime : String = ""
    var light : String = ""
    var sonar : String = ""
    var voltage : String = ""
    var wheels : String = ""
    var motion : String = ""
    var isVehicleLogSelected : Bool = true
    var isAccelerationLogSelected : Bool = true
    var isGpsLogSelected : Bool = true
    var isMagneticLogSelected : Bool = true
    var isGyroscopeLogSelected : Bool = true

    init(){
        NotificationCenter.default.addObserver(self, selector: #selector(updateLogger), name: .updateSensorsForLog, object: nil)
//        NotificationCenter.default.addObserver(self, selector: #selector(updateLogger), name: .gpsLog, object: nil)
//        NotificationCenter.default.addObserver(self, selector: #selector(updateLogger), name: .acceleratorLog, object: nil)
//        NotificationCenter.default.addObserver(self, selector: #selector(updateLogger), name: .magneticLog, object: nil)
//        NotificationCenter.default.addObserver(self, selector: #selector(updateLogger), name: .gyroscopeLog, object: nil)

    }
    func getDirectoryInfo() -> URL {
        let fileManager = FileManager.default
        var documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
        documentsURL = documentsURL.appendingPathComponent(Strings.forwardSlash +  baseDirectory)
        do {
            let fileURLs = try fileManager.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: nil)
            print(fileURLs)

        } catch {
            print("Error while enumerating files \(documentsURL.path): \(error.localizedDescription)")
        }
        return documentsURL
    }

    func createOpenBotFolder(openBotPath: String) {
        print(openBotPath)
        createFolder(path: openBotPath)
    }

    func createImageFolder(openBotPath: String) {
        let imagePath = openBotPath + "/images"
        createFolder(path: imagePath)
//        if URL(string: openBotPath) != nil {
//            saveImages(path: imagePath, image: image)
//        }

    }

    func createSensorData(openBotPath: String) {
        let sensorDataPath = openBotPath + "/sensor_data"

        createFolder(path: sensorDataPath)
        if URL(string: openBotPath) != nil {
            saveSensorFiles(path: sensorDataPath, data: acceleration, fileName: "accelerometerLog.txt")
            saveSensorFiles(path: sensorDataPath, data: magnetometer, fileName: "magneticLog.txt")
            saveSensorFiles(path: sensorDataPath, data: gyroscope, fileName: "gyroscopeLog.txt")
            saveSensorFiles(path: sensorDataPath, data: gps, fileName: "gpsLog.txt")
            saveSensorFiles(path: sensorDataPath, data: bumper, fileName: "bumperLog.txt")
            saveSensorFiles(path: sensorDataPath, data: ctrlLog, fileName: "ctrlLog.txt")
            saveSensorFiles(path: sensorDataPath, data: indicator, fileName: "indicatorLog.txt")
            saveSensorFiles(path: sensorDataPath, data: inferenceTime, fileName: "inferenceLog.txt")
            saveSensorFiles(path: sensorDataPath, data: light, fileName: "lightLog.txt")
            saveSensorFiles(path: sensorDataPath, data: motion, fileName: "motionLog.txt")
            saveSensorFiles(path: sensorDataPath, data: sonar, fileName: "sonarLog.txt")
            saveSensorFiles(path: sensorDataPath, data: voltage, fileName: "voltageLog.txt")
            saveSensorFiles(path: sensorDataPath, data: wheels, fileName: "wheelsLog.txt")
        }
    }

    func createFolder(path: String) {
        if !FileManager.default.fileExists(atPath: path) {
            try? FileManager.default.createDirectory(atPath: path, withIntermediateDirectories: false, attributes: nil)
        }

    }

    func saveImages(path: String , image : UIImage,name : String) {
        let imagePath = URL(string: path)
        let imageName = imagePath?.appendingPathComponent(name);
        let ima = imageName?.absoluteString
        let image = image
        let fileManager = FileManager.default
        if let ima = ima {
            fileManager.createFile(atPath: ima, contents: image.pngData())
        }
    }

    func deleteFiles(path: String) {
        let fileManager = FileManager.default
        let documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
        do {
            try FileManager.default.removeItem(atPath: documentsURL.path + path)
        } catch {
            print(error)
        }
    }

    func saveSensorFiles(path: String,data : String,fileName : String) {
        let fileManager = FileManager.default
        let sensorPath = URL(string: path)
        let sensorFileName = sensorPath?.appendingPathComponent(fileName)
        let sen = sensorFileName?.absoluteString
        let str = data
        fileManager.createFile(atPath: sen ?? "", contents: str.data(using: String.Encoding.utf8))
    }

    func saveAccelerationFile(path : String , data : String){
        let fileManager = FileManager.default
        let sensorPath = URL(string: path)
        let sensor = sensorPath?.appendingPathComponent("acceleration.txt")
        let sen = sensor?.absoluteString
        let str = data
        fileManager.createFile(atPath: sen ?? "", contents: str.data(using: String.Encoding.utf8))
    }

    func knowDateOrTime(format: String) -> String {
        let date = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        let dateOrTime = dateFormatter.string(from: date)
        return dateOrTime
    }

    func saveFramesFile(path : String , data : String){
        let fileManager = FileManager.default
        let sensorPath = URL(string: path)
        let frame =  sensorPath?.appendingPathComponent("rgbFrames.txt")
        let f = frame?.absoluteString
        let str = data
        if let f = f {
            fileManager.createFile(atPath: f, contents: str.data(using: String.Encoding.utf8))
        }
    }
    func getBaseDirectoryName()-> String{
        knowDateOrTime(format: "yyyy") + knowDateOrTime(format: "MM") + knowDateOrTime(format: "dd") + "_"
                + knowDateOrTime(format: "H") + knowDateOrTime(format: "mm") + knowDateOrTime(format: "ss")
    }
    func getDocumentDirectoryInformation(){

        let fileManager = FileManager.default
        let documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
//        documentsURL = documentsURL.appendingPathComponent(Strings.forwardSlash +  Global.shared.baseDirectory)
        do {
            let fileURLs = try fileManager.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: nil)
            print("hello ",fileURLs)
        } catch {
            print("Error while enumerating files \(documentsURL.path): \(error.localizedDescription)")
        }
    }

    func recordLogs() {
        let timestamp = returnCurrentTimestamp()
        if isVehicleLogSelected {
            carSensorsData = carSensorsData + bluetoothData + "\n"
        }
        if isGpsLogSelected {
            gps = gps + String(timestamp) + " " + String(sensorData.location.coordinate.latitude) + " " + String(sensorData.location.coordinate.longitude) + " " + String(sensorData.location.altitude) + " " + String(sensorData.location.speed) + "\n"
        }
        if isAccelerationLogSelected {
            acceleration = acceleration + String(timestamp) + " " + convertToString(XValue: sensorData.accelerationX, YValue: sensorData.accelerationY, ZValue: sensorData.accelerationZ) + "\n"
        }
        if isMagneticLogSelected {
            magnetometer = magnetometer + String(timestamp) + " " + convertToString(XValue: sensorData.magneticFieldX, YValue: sensorData.magneticFieldY, ZValue: sensorData.magneticFieldZ) + "\n"
        }
        if isGyroscopeLogSelected {
            gyroscope = gyroscope + String(timestamp) + " " + convertToString(XValue: sensorData.gyroX, YValue: sensorData.gyroY, ZValue: sensorData.gyroZ) + "\n"
        }
        if isVehicleLogSelected {
            recordVehicleLogs()
        }
    }

    func recordVehicleLogs(){
        if bluetoothData != "" {
            let timestamp = returnCurrentTimestamp()
            let index = bluetooth.sonarData.index(after: bluetooth.sonarData.startIndex)
            sonar = sonar + String(timestamp) + " " + String(bluetooth.sonarData[index...])  + "\n"
            wheels = wheels + String(timestamp) + " " + String(bluetooth.speedometer[index...])  + "\n"
            voltage = voltage + String(timestamp) + " " + String(bluetooth.voltageDivider[index...]) + "\n"
            if bluetooth.bumperData != ""{
                bumper = bumper  + String(timestamp) + " " + String(bluetooth.bumperData[index...]) + "\n";
            }

        }
    }
    func convertToString(XValue: Double, YValue: Double, ZValue: Double) -> String {
        String(XValue) + " " + String(YValue) + " " + String(ZValue);
    }
    func setupFilesForLogging(){
//        images.removeAll()
        carSensorsData = "";
        acceleration = Strings.acceleration
        locationCoordinates = Strings.locationCoordinates
        gyroscope = Strings.gyroscopeHeader
        magnetometer = Strings.magnetometer
        gps = Strings.gpsHeader
        baseDirectory = ""
        bumper = Strings.bumper
        ctrlLog = Strings.ctrlLog
        indicator = Strings.indicator
        inferenceTime = Strings.inferenceTime
        light = Strings.light
        sonar = Strings.sonar
        voltage = Strings.voltageHeader
        wheels = Strings.wheels
        motion = Strings.motion
        ctrlLog = Strings.ctrlLog
    }
    @objc func updateLogger(_ notification: Notification?) {
       let selectedButton = notification?.object as! UIButton
        let tag = selectedButton.tag
        switch tag{
        case 1 :
            isVehicleLogSelected = !isVehicleLogSelected
        case 2 :
            isGpsLogSelected = !isGpsLogSelected
        case 3 :
            isAccelerationLogSelected = !isAccelerationLogSelected
        case 4 :
            isMagneticLogSelected = !isMagneticLogSelected
        default :
            isGyroscopeLogSelected = !isGyroscopeLogSelected
        }
        print("hello breaker")
        print(isVehicleLogSelected)
        print(isGpsLogSelected)
        print(isAccelerationLogSelected)
        print(isMagneticLogSelected)
        print(isGyroscopeLogSelected)

    }


}
