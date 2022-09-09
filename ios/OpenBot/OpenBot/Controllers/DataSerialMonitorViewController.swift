//
//  DataSerialMonitorViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 18/08/22.

import UIKit
import CoreBluetooth

var bluetoothData = ""

class DataSerialMonitorViewController: UIViewController {
    let datalogger = DataLogger.shared
    var payloadData: String = ""
    var labelString: String = "nil"
    let bluetooth = bluetoothDataController.shared
    let serialMonitor = UITextView()
    @IBOutlet weak var sendDataToBle: UITextField!
    @IBOutlet weak var bleSendData: UITextView!

    override func viewDidLoad() {
        super.viewDidLoad()
        createSerialMonitor()
        NotificationCenter.default.addObserver(self, selector: #selector(updateLabel), name: .updateLabel, object: nil)
    }

    func createSerialMonitor(){
        serialMonitor.frame = CGRect(x: 0 , y: 0, width: view.frame.width, height: view.frame.height/2)
        serialMonitor.backgroundColor = Colors.sonar
//        view.addSubview(serialMonitor)
    }

    @IBAction func sendData(_ sender: Any) {
        guard let bleSendData = bleSendData else {
            return
        }
        let temp = (sendDataToBle.text ?? "") + "\n"
        labelString = (labelString) + (temp) + "\n"
        let range = NSRange(location: bleSendData.text.count - 1, length: 0)
        bleSendData.scrollRangeToVisible(range)
        bleSendData.text = labelString
        payloadData = temp
        print("value is :", temp)
        bluetooth.sendData(payload: payloadData)
        sendDataToBle.text = "";
    }

    @objc func updateLabel() {
        labelString = (labelString) + bluetoothData + "\n"
        let range = NSRange(location: bleSendData.text.count - 1, length: 0)
        bleSendData.scrollRangeToVisible(range)
        bleSendData.text = labelString
    }


    @IBAction func startLogs(_ sender: Any) {

        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let documentsDirectory: String = paths.first ?? ""
        let openBotPath = documentsDirectory + "/openBot"
        DataLogger.shared.createOpenBotFolder(openBotPath: openBotPath)
        DataLogger.shared.createImageFolder(openBotPath: openBotPath)
        DataLogger.shared.createSensorData(openBotPath: openBotPath)

        if let url = URL(string: openBotPath) {
            createZip(path: url)
//            saveFolder(path: url)
        }
    }


    func saveFolder(path: URL) {
        let archiveUrl = DataLogger.shared.getDirectoryInfo()
        let activityManager = UIActivityViewController(activityItems: [archiveUrl], applicationActivities: nil)
        present(activityManager, animated: true)

    }


    func createZip(path: URL) {
        let baseDirectoryName = datalogger.knowDateOrTime(format: "yyyy") + datalogger.knowDateOrTime(format: "MM") + datalogger.knowDateOrTime(format: "dd") + "_"
                + datalogger.knowDateOrTime(format: "H") + datalogger.knowDateOrTime(format: "mm") + datalogger.knowDateOrTime(format: "ss") + ".zip"
        let fm = FileManager.default
        let baseDirectoryUrl = fm.urls(for: .documentDirectory, in: .userDomainMask).first!.appendingPathComponent("/openBot")
        var archiveUrl: URL?
        var error: NSError?
        let coordinator = NSFileCoordinator()
        coordinator.coordinate(readingItemAt: baseDirectoryUrl, options: [.forUploading], error: &error) { (zipUrl) in
            let tmpUrl = try! fm.url(
                    for: .itemReplacementDirectory,
                    in: .userDomainMask,
                    appropriateFor: zipUrl,
                    create: true
            ).appendingPathComponent(baseDirectoryName)
            try! fm.moveItem(at: zipUrl, to: tmpUrl)
            archiveUrl = tmpUrl
        }
        if let archiveUrl = archiveUrl {
            let avc = UIActivityViewController(activityItems: [archiveUrl], applicationActivities: nil)
            present(avc, animated: true)
        } else {
            print(error as Any)
        }
    }
    
    @IBAction func openTemp(_ sender: Any) {
        let openDataSerialView = (storyboard?.instantiateViewController(withIdentifier: "temp"))!
        guard (navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }
    }
    
}
