//
// Created by Nitish Yadav on 06/09/22.
//

import Foundation
import UIKit
class DataLogger {
    static let shared: DataLogger = DataLogger()
    var enabled: Bool = false;
    let sensorData = sensorDataRetrieve.shared
    func getDirectoryInfo() -> URL {
        let fileManager = FileManager.default
        var documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
        documentsURL = documentsURL.appendingPathComponent(Strings.forwardSlash +  Global.shared.baseDirectory)
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
        let tempData = ""
        createFolder(path: sensorDataPath)
        if URL(string: openBotPath) != nil {
            saveSensorFiles(path: sensorDataPath, data: Global.shared.acceleration, fileName: "accelerometerLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.magnetometer, fileName: "magneticLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.gyroscope, fileName: "gyroscopeLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.vehicle, fileName: "vehicle.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.gps, fileName: "gpsLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.bumper, fileName: "bumperLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.ctrlLog, fileName: "ctrlLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.indicator, fileName: "indicatorLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.inferenceTime, fileName: "inferenceLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.light, fileName: "lightLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.motion, fileName: "motionLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.sonar, fileName: "sonarLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.voltage, fileName: "voltageLog.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.wheels, fileName: "wheelsLog.txt")
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
        var documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
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
        var documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
//        documentsURL = documentsURL.appendingPathComponent(Strings.forwardSlash +  Global.shared.baseDirectory)
        do {
            let fileURLs = try fileManager.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: nil)
            print("hello ",fileURLs)
        } catch {
            print("Error while enumerating files \(documentsURL.path): \(error.localizedDescription)")
        }
    }

    func recordSensorData() {
        let timestamp = returnCurrentTimestamp()
        if Global.shared.isVehicleLogSelected {
            Global.shared.carSensorsData = Global.shared.carSensorsData + bluetoothData + "\n"
        }
        if Global.shared.isGpsLogSelected {
            Global.shared.gps = Global.shared.gps + String(timestamp) + " " + String(sensorData.location.coordinate.latitude) + " " + String(sensorData.location.coordinate.longitude) + " " + String(sensorData.location.altitude) + " " + String(sensorData.location.speed) + "\n"
        }
        if Global.shared.isAccelerationLogSelected {
            Global.shared.acceleration = Global.shared.acceleration + String(timestamp) + " " + convertToString(XValue: sensorData.accelerationX, YValue: sensorData.accelerationY, ZValue: sensorData.accelerationZ) + "\n"
        }
        if Global.shared.isMagneticLogSelected {
            Global.shared.magnetometer = Global.shared.magnetometer + String(timestamp) + " " + convertToString(XValue: sensorData.magneticFieldX, YValue: sensorData.magneticFieldY, ZValue: sensorData.magneticFieldZ) + "\n"
        }
        if Global.shared.isGyroscopeLogSelected {
            Global.shared.gyroscope = Global.shared.gyroscope + String(timestamp) + " " + convertToString(XValue: sensorData.gyroX, YValue: sensorData.gyroY, ZValue: sensorData.gyroZ) + "\n"
        }
    }
    func convertToString(XValue: Double, YValue: Double, ZValue: Double) -> String {
        String(XValue) + " " + String(YValue) + " " + String(ZValue);
    }
    func setupFilesForLogging(){
        Global.shared.images.removeAll()
        Global.shared.carSensorsData = "";
        Global.shared.acceleration = Strings.acceleration
        Global.shared.locationCoordinates = Strings.locationCoordinates
        Global.shared.gyroscope = Strings.gyroscopeHeader
        Global.shared.magnetometer = Strings.magnetometer
        Global.shared.vehicle = ""
        Global.shared.gps = Strings.gpsHeader
        Global.shared.baseDirectory = ""
        Global.shared.bumper = Strings.bumper
        Global.shared.ctrlLog = Strings.ctrlLog
        Global.shared.indicator = Strings.indicator
        Global.shared.inferenceTime = Strings.inferenceTime
        Global.shared.light = Strings.light
        Global.shared.sonar = Strings.sonar
        Global.shared.voltage = Strings.voltageHeader
        Global.shared.wheels = Strings.wheels
        Global.shared.motion = Strings.motion
    }

}
