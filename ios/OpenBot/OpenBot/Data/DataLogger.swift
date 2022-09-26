//
// Created by Nitish Yadav on 06/09/22.
//

import Foundation
import UIKit

class DataLogger {
    static let shared: DataLogger = DataLogger()
    var enabled: Bool = false;

    func getDirectoryInfo() -> URL {
        let fileManager = FileManager.default
        var documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
//        documentsURL = documentsURL.appendingPathComponent("/OpenBot/images")
        do {
            let fileURLs = try fileManager.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: nil)
            print(fileURLs)

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
//        if URL(string: openBotPath) != nil {
//            saveImages(path: imagePath, image: image)
//        }

    }

    func createSensorData(openBotPath: String) {
        let sensorDataPath = openBotPath + "/sensor_data"
        createFolder(path: sensorDataPath)
        if URL(string: openBotPath) != nil {
            saveSensorFiles(path: sensorDataPath, data: Global.shared.acceleration, fileName: "acceleration.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.magnetometer, fileName: "magnetic.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.gyroscope, fileName: "gyroscope.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.vehicle, fileName: "vehicle.txt")
            saveSensorFiles(path: sensorDataPath, data: Global.shared.gps, fileName: "gps.txt")
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
        do {
            try FileManager.default.removeItem(atPath: path)
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
}
