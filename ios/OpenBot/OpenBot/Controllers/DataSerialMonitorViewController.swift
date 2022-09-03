//
//  DataSerialMonitorViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 18/08/22.

import UIKit
import CoreBluetooth
var bluetoothData = ""
class DataSerialMonitorViewController: UIViewController {
    var payloadData: String = ""
    var labelString: String = "nil"
    let bluetooth = bluetoothDataController.shared
    @IBOutlet weak var sendDataToBle: UITextField!
    @IBOutlet weak var bleSendData: UITextView!

    override func viewDidLoad() {
        super.viewDidLoad()
        bleSendData.isEditable = false
        NotificationCenter.default.addObserver(self, selector: #selector(updateLabel), name: .updateLabel, object: nil)
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

   @objc func updateLabel(){
       labelString = (labelString) + bluetoothData  + "\n"
        let range = NSRange(location: bleSendData.text.count - 1, length: 0)
        bleSendData.scrollRangeToVisible(range)
        bleSendData.text = labelString
    }
   

    
}
