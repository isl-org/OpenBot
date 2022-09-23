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
        documentsURL = documentsURL.appendingPathComponent("/openBot")
        do {
            let fileURLs = try fileManager.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: nil)

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
            saveSensorFile(path: sensorDataPath, data: sensorDataRetrieve.shared.sensorData)
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

    func saveSensorFile(path: String,data : String) {
        let fileManager = FileManager.default
        let sensorPath = URL(string: path)
        let sensor = sensorPath?.appendingPathComponent("sensor1.txt")
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

}
