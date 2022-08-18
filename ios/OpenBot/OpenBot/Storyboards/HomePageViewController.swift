//
//  HomePageViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 17/08/22.
//

import UIKit
import CoreBluetooth

var isBluetoothConnected = false;

class HomePageViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate {
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        self.centralManager?.scanForPeripherals(withServices: nil, options: nil)
    }

    var centralManager: CBCentralManager?
    var tempPeripheral: CBPeripheral!
    private var allservices: [CBService]?
    var writeCharacteristics: CBCharacteristic?
    @IBOutlet weak var bluetooth: UIButton!
    @IBOutlet weak var settings: UIButton!
    @IBOutlet weak var titleLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        setUpTitle();
        if peri != nil {

            centralManager = CBCentralManager(delegate: self, queue: nil)
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        if (isBluetoothConnected) {
            bluetooth.setImage(UIImage(named: "bluetoothConnected"), for: .normal)
        } else {
            bluetooth.setImage(UIImage(named: "bluetoothDisconnected"), for: .normal)
        }
    }

    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        if peripheral.name == peri?.name {
            centralManager?.stopScan()
            self.tempPeripheral = peripheral
            peripheral.delegate = self
            centralManager?.connect(peripheral)
        }
    }

    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {

        print(peripheral)
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
            print("services are :", services)
            for service in services {
                allservices?.append(service)
//                peripheral.discoverCharacteristics([], for: service)
            }
        }
    }
    func setUpTitle() {
        titleLabel.text = "OpenBot";
        titleLabel.textColor = UIColor(named: "HomePageTitleColor")
    }

    @IBAction func onTapSettings() {
        print("setting button clicked");
        if isBluetoothConnected != false{
        let openDataSerialView = (self.storyboard?.instantiateViewController(withIdentifier: "dataSerialMonitor"))!
        guard (self.navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }
        }
    }

}
