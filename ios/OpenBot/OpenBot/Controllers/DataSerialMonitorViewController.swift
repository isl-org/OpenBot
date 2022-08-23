//
//  DataSerialMonitorViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 18/08/22.
//

import UIKit
import CoreBluetooth
class DataSerialMonitorViewController: UIViewController,CBCentralManagerDelegate, CBPeripheralDelegate {
    var centralManager: CBCentralManager?
    var tempPeripheral: CBPeripheral!
    var LabelString: String!
    private var allservices: [CBService]?
    var writeCharacteristics: CBCharacteristic?
    @IBOutlet weak var sendDataToBle: UITextField!
    @IBOutlet weak var bleSendData: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        bleSendData.isEditable = false
        centralManager = CBCentralManager(delegate: self, queue: nil)
//        print("tempPeripheral is :", tempPeripheral)
    }

    func centralManagerDidUpdateState(_ central: CBCentralManager) {

        self.centralManager?.scanForPeripherals(withServices: nil, options: nil)
    }

    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
//        print(peri)
//        print(peripheral.name)
        if peripheral.name == peri?.name {
            print(10)
            centralManager?.stopScan()
            self.tempPeripheral = peripheral
//            print("peripheral is :",tempPeripheral)
            tempPeripheral.delegate = self
            centralManager?.connect(tempPeripheral)
        }
    }

    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {

//        print("i am connected to ", peripheral.name)
//        print("peripheral delegate", peripheral.delegate)

        peripheral.discoverServices(nil)

    }

    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        print("connection was failed")

    }

    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        print("disconnected from :", peripheral)
    }

    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        if let services = peripheral.services {
//            print("services are :", services)
            for service in services {
                allservices?.append(service)
                peripheral.discoverCharacteristics([], for: service)
            }
        }
    }

    func peripheral(_ peripheral: CBPeripheral, didDiscoverDescriptorsFor characteristic: CBCharacteristic, error: Error?) {
        if let des = characteristic.descriptors {
            for d in des {
                peripheral.readValue(for: d)
            }
        }
    }

    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        if let characteristics = service.characteristics {
            for characteristic in characteristics {
                writeCharacteristics = characteristic
//                print(" hello ", characteristic)
//                    let dataToSend: Data = "hello jgbjdsb".data(using: String.Encoding.utf8)!
//                var  temp = (wifiName.text ?? "") + (wifiPassword.text ?? "")
//                let dataToSend: Data = temp.data(using: String.Encoding.utf8)!
//                    peripheral.writeValue(dataToSend, for: characteristic, type: CBCharacteristicWriteType.withResponse)
                subscribeToNotifications(peripheral: tempPeripheral, characteristic: characteristic)
                readValue(characteristic: characteristic)

                peripheral.discoverDescriptors(for: characteristic)
            }
        }
    }

    func subscribeToNotifications(peripheral: CBPeripheral, characteristic: CBCharacteristic) {
        peripheral.setNotifyValue(true, for: characteristic)
    }

    func peripheral(_ peripheral: CBPeripheral, didUpdateNotificationStateFor characteristic: CBCharacteristic, error: Error?) {
        if let error = error {
            // Handle error
            return
        }
        // Successfully subscribed to or unsubscribed from notifications/indications on a characteristic
    }

    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        print("Data sended to :" , peripheral.name);
    }

    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor descriptor: CBDescriptor, error: Error?) {
        print(descriptor)
    }

    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
//        print("didUpdateValue : ",characteristic.value)
        if characteristic.value != nil {
            let data = characteristic.value!

            let x = String(data: data, encoding: .utf8)

            LabelString = (LabelString ?? "") + (x ?? "") + "\n"
            print(x)
            let range = NSRange(location: bleSendData.text.count - 1, length: 0)
            bleSendData.scrollRangeToVisible(range)
            bleSendData.text = LabelString
        }
//    print("characteristic are : ", writeCharacteristics)
    }

    func readValue(characteristic: CBCharacteristic) {
        self.tempPeripheral?.readValue(for: characteristic)
    }

    @IBAction func sendData(_ sender: Any) {
        var temp = (sendDataToBle.text ?? "") + "\n"
        LabelString = (LabelString ?? "") + (temp) + "\n"
        let range = NSRange(location: bleSendData.text.count - 1, length: 0)
        bleSendData.scrollRangeToVisible(range)
        bleSendData.text = LabelString

        print("value is :", temp)
//        print("temp peripheral is :",peri)
        let dataToSend: Data = temp.data(using: String.Encoding.utf8)!
        tempPeripheral!.writeValue(dataToSend, for: writeCharacteristics!, type: CBCharacteristicWriteType.withResponse)
        sendDataToBle.text = "";
    }

}
