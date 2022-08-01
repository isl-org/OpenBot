//
//  DataSendViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 30/07/22.
//

import UIKit
import CoreBluetooth

class DataSendViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate {
    var centralManager: CBCentralManager?
    var tempPeripheral: CBPeripheral!
    private var allservices: [CBService]?
    var writeCharacteristics : CBCharacteristic?
    @IBOutlet weak var wifiPassword: UITextField!
    @IBOutlet weak var wifiName: UITextField!
    //
    override func viewDidLoad() {

        super.viewDidLoad()
        centralManager = CBCentralManager(delegate: self, queue: nil)

    }

    func centralManagerDidUpdateState(_ central: CBCentralManager) {

        self.centralManager?.scanForPeripherals(withServices: nil, options: nil)
    }

    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        if peripheral.name == peri?.name {
            centralManager?.stopScan()
            self.tempPeripheral = peripheral
            self.tempPeripheral.delegate = self

            centralManager?.connect(peripheral)
        }
    }

    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        print("i am connected to ", peripheral.name)
        print("peripheral delegate", peripheral.delegate)

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
            for service in services {
                print(service)
                allservices?.append(service)
                peripheral.discoverCharacteristics([], for: service)
            }
        }
    }

    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        if let characteristics = service.characteristics {
            for characteristic in characteristics {
                writeCharacteristics = characteristic
                print(" hello ",characteristic)
//                    let dataToSend: Data = "hello jgbjdsb".data(using: String.Encoding.utf8)!
//                var  temp = (wifiName.text ?? "") + (wifiPassword.text ?? "")
//                let dataToSend: Data = temp.data(using: String.Encoding.utf8)!
//                    peripheral.writeValue(dataToSend, for: characteristic, type: CBCharacteristicWriteType.withResponse);
            }
        }
    }

    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        let x = tempPeripheral.readValue(for: writeCharacteristics!)


    }

    @IBAction func sendData(_ sender: Any) {
        print("hi sender ")
        var  temp = (wifiName.text ?? "") + (wifiPassword.text ?? "")
        let dataToSend: Data = temp.data(using: String.Encoding.utf8)!
       tempPeripheral.writeValue(dataToSend, for: writeCharacteristics!, type: CBCharacteristicWriteType.withResponse)
        tempPeripheral.readValue(for: writeCharacteristics!)

    }
}