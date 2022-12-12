//
// Created by Nitish Yadav on 06/09/22.
//

import Foundation
import UIKit

var bluetoothData = ""

class DataLogger {
    static let shared: DataLogger = DataLogger()
    var enabled: Bool = false;
    let sensorData = sensorDataRetrieve.shared
    let bluetooth = bluetoothDataController.shared;
    var carSensorsData: String = ""
    var acceleration: String = ""
    var gyroscope: String = ""
    var magnetometer: String = ""
    var locationCoordinates: String = ""
    var gps: String = ""
    var baseDirectory: String = ""
    var allDirectoriesName = [String]()
    var bumper: String = ""
    var ctrlLog: String = ""
    var indicator: String = ""
    var inferenceTime: String = ""
    var light: String = ""
    var sonar: String = ""
    var voltage: String = ""
    var wheels: String = ""
    var motion: String = ""
    var allDirectories = [URL]()
    var isVehicleLogSelected: Bool = true
    var isAccelerationLogSelected: Bool = true
    var isGpsLogSelected: Bool = true
    var isMagneticLogSelected: Bool = true
    var isGyroscopeLogSelected: Bool = true

    init() {
        NotificationCenter.default.addObserver(self, selector: #selector(updateLogger), name: .updateSensorsForLog, object: nil)
    }

    func getDirectoryInfo() -> URL {
        allDirectories.removeAll()
        let fileManager = FileManager.default
        var documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
        documentsURL = documentsURL.appendingPathComponent(Strings.forwardSlash + baseDirectory)
        do {
            let fileURLs = try fileManager.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: nil)
            allDirectories = fileURLs
        } catch {
            print("Error while enumerating files \(documentsURL.path): \(error.localizedDescription)")
        }
        return documentsURL
    }

    func createOpenBotFolder(openBotPath: String) {
        createFolder(path: openBotPath)
    }

    func createImageFolder(openBotPath: String) {
        let imagePath = openBotPath + "/images"
        createFolder(path: imagePath)

    }


    func createSensorData(openBotPath: String) {
        let sensorDataPath = openBotPath + "/sensor_data"

        createFolder(path: sensorDataPath)
        if URL(string: openBotPath) != nil {
            saveSensorFiles(path: sensorDataPath, data: acceleration, fileName: FileName.accelerator)
            saveSensorFiles(path: sensorDataPath, data: magnetometer, fileName: FileName.magnetic)
            saveSensorFiles(path: sensorDataPath, data: gyroscope, fileName: FileName.gyroscopeLog)
            saveSensorFiles(path: sensorDataPath, data: gps, fileName: FileName.gpsLog)
            saveSensorFiles(path: sensorDataPath, data: bumper, fileName: FileName.bumperLog)
            saveSensorFiles(path: sensorDataPath, data: ctrlLog, fileName: FileName.ctrlLog)
            saveSensorFiles(path: sensorDataPath, data: indicator, fileName: FileName.indicatorLog)
            saveSensorFiles(path: sensorDataPath, data: inferenceTime, fileName: FileName.inferenceLog)
            saveSensorFiles(path: sensorDataPath, data: light, fileName: FileName.lightLog)
            saveSensorFiles(path: sensorDataPath, data: motion, fileName: FileName.motionLog)
            saveSensorFiles(path: sensorDataPath, data: sonar, fileName: FileName.sonarLog)
            saveSensorFiles(path: sensorDataPath, data: voltage, fileName: FileName.voltageLog)
            saveSensorFiles(path: sensorDataPath, data: wheels, fileName: FileName.wheelsLog)
        }
    }

    func createFolder(path: String) {
        if !FileManager.default.fileExists(atPath: path) {
            try? FileManager.default.createDirectory(atPath: path, withIntermediateDirectories: false, attributes: nil)
        }

    }

    func saveImages(path: String, image: UIImage, name: String) {

        let imagePath = URL(string: path)
        let imageName = imagePath?.appendingPathComponent(name);
        let ima = imageName?.absoluteString
        let image = image
        let fileManager = FileManager.default
        if let ima = ima {
            fileManager.createFile(atPath: ima, contents: image.pngData())
        }
    }

    func deleteFiles(fileNameToDelete: String) {

        print("fileNameToDelete : ",fileNameToDelete)
        var filePath = ""
// Fine documents directory on device
        let dirs : [String] = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.allDomainsMask, true)
        if dirs.count > 0 {
            let dir = dirs[0] //documents directory
            filePath = dir.appendingFormat("/" + fileNameToDelete)
            print("Local path = \(filePath)")

        } else {
            print("Could not find local directory to store file")
            return
        }
        do {
            let fileManager = FileManager.default
            if fileManager.fileExists(atPath: filePath) {
                // Delete file
                try fileManager.removeItem(atPath: filePath)
            } else {
                print("File does not exist")
            }

        }
        catch let error as NSError {
            print("An error took place: \(error)")
        }
    }

    func deleteAllFilesFromDocument(){
        let fileManager = FileManager.default
        do {
            let documentDirectoryURL = try fileManager.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false)
            let fileURLs = try fileManager.contentsOfDirectory(at: documentDirectoryURL, includingPropertiesForKeys: nil, options: .skipsHiddenFiles)
            for url in fileURLs {
                try fileManager.removeItem(at: url)
            }
        } catch {
            print(error)
        }
    }

    func deleteZipFileFromDocument(){
        let fileManager = FileManager.default
        let documentDirectoryURL = FileManager.getDocumentsDirectory()
        do {
            let fileURLs = try fileManager.contentsOfDirectory(at: documentDirectoryURL, includingPropertiesForKeys: nil, options: .skipsHiddenFiles)
            for url in fileURLs {
                if url.lastPathComponent.first == "2" && !url.lastPathComponent.contains("."){
                    deleteFiles(fileNameToDelete: url.lastPathComponent);
                }
            }
        } catch {
            print("Error in deleting .zip file")
        }
    }

    func saveSensorFiles(path: String, data: String, fileName: String) {
        let fileManager = FileManager.default
        let sensorPath = URL(string: path)
        let sensorFileName = sensorPath?.appendingPathComponent(fileName)
        let sen = sensorFileName?.absoluteString
        let str = data
        fileManager.createFile(atPath: sen ?? "", contents: str.data(using: String.Encoding.utf8))
    }

    func saveAccelerationFile(path: String, data: String) {
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

    func saveFramesFile(path: String, data: String) {
        let fileManager = FileManager.default
        let sensorPath = URL(string: path)
        let frame = sensorPath?.appendingPathComponent("rgbFrames.txt")
        let f = frame?.absoluteString
        let str = data
        if let f = f {
            fileManager.createFile(atPath: f, contents: str.data(using: String.Encoding.utf8))
        }
    }

    func getBaseDirectoryName() -> String {
        knowDateOrTime(format: "yyyy") + knowDateOrTime(format: "MM") + knowDateOrTime(format: "dd") + "_"
                + knowDateOrTime(format: "H") + knowDateOrTime(format: "mm") + knowDateOrTime(format: "ss")
    }

    func getDocumentDirectoryInformation() -> [URL] {
        var fileURLs : [URL] = []
        let fileManager = FileManager.default
        let documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
//        documentsURL = documentsURL.appendingPathComponent(Strings.forwardSlash +  Global.shared.baseDirectory)
        do {
            fileURLs = try fileManager.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: nil)
            return fileURLs
        } catch {
            print("Error while enumerating files \(documentsURL.path): \(error.localizedDescription)")
        }
        return fileURLs
    }

    func recordLogs() {
        let timestamp = returnCurrentTimestamp()
        if isVehicleLogSelected {
            carSensorsData = carSensorsData + bluetoothData + "\n"
        }
        if isGpsLogSelected {
            if (sensorData.location != nil) {
                gps = gps + String(timestamp) + " " + String(sensorData.location.coordinate.latitude) + " " + String(sensorData.location.coordinate.longitude) + " " + String(sensorData.location.altitude) + " " + String(sensorData.location.speed) + "\n"
            }
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

    func recordVehicleLogs() {
        let timestamp = returnCurrentTimestamp()
        if bluetoothData != "" {
            let index = bluetooth.sonarData.index(after: bluetooth.sonarData.startIndex)
            sonar = sonar + String(timestamp) + " " + String(bluetooth.sonarData[index...]) + "\n"
            wheels = wheels + String(timestamp) + " " + String(bluetooth.speedometer[index...]) + "\n"
            voltage = voltage + String(timestamp) + " " + String(bluetooth.voltageDivider[index...]) + "\n"
            if bluetooth.bumperData != "" {
                bumper = bumper + String(timestamp) + " " + String(bluetooth.bumperData[index...]) + "\n";
            }
        }

    }

    func convertToString(XValue: Double, YValue: Double, ZValue: Double) -> String {
        String(XValue) + " " + String(YValue) + " " + String(ZValue);
    }

    func setupFilesForLogging() {
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
        switch tag {
        case 1:
            isVehicleLogSelected = !isVehicleLogSelected
        case 2:
            isGpsLogSelected = !isGpsLogSelected
        case 3:
            isAccelerationLogSelected = !isAccelerationLogSelected
        case 4:
            isMagneticLogSelected = !isMagneticLogSelected
        default:
            isGyroscopeLogSelected = !isGyroscopeLogSelected
        }
    }

    func setControlLogs(left: String, right: String) {
        ctrlLog = ctrlLog + String(returnCurrentTimestamp()) + " " + left + " " + right + "\n";
    }

    func setIndicatorLogs(indicator: String) {
        self.indicator = self.indicator + String(returnCurrentTimestamp()) + " " + indicator + "\n";
    }


}
