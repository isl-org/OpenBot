//
//  DataSerialMonitorViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 18/08/22.


import UIKit
import CoreBluetooth

class DataSerialMonitorViewController: UIViewController {
    var payloadData: String = ""
    var labelString: String = ""
    private var allServices: [CBService]?
    var writeCharacteristics: CBCharacteristic?
    let bluetooth = bluetoothDataController.shared
    @IBOutlet weak var sendDataToBle: UITextField!
    @IBOutlet weak var bleSendData: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        bleSendData.isEditable = false
        Timer.scheduledTimer(withTimeInterval: 0.25, repeats: true) { _ in
            self.labelString = (self.labelString) + (self.bluetooth.LabelString ?? "nil")  + "\n"
            let range = NSRange(location: self.bleSendData.text.count - 1, length: 0)
            self.bleSendData.scrollRangeToVisible(range)
            self.bleSendData.text = self.labelString


        }
    }

    @IBAction func sendData(_ sender: Any) {
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



}
