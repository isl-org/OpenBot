//
// Created by Nitish Yadav on 29/08/22.
//

import Foundation
import CoreBluetooth
import CoreMotion

/// This class provides a BLE communication interface with the OpenBot vehicle
class bluetoothDataController: CMDeviceMotion, CBCentralManagerDelegate, CBPeripheralDelegate {
    static let shared: bluetoothDataController = bluetoothDataController()
    var centralManager: CBCentralManager?
    var tempCentralManager: CBCentralManager?
    var peri: CBPeripheral?
    var peripherals = Array<CBPeripheral>()
    var tempPeripheral: CBPeripheral!
    var vehicleReady: Bool = false
    var sonarData: String = ""
    var voltageDivider: String = ""
    var speedometer: String = ""
    var bumperData: String = ""
    var writeCharacteristics: CBCharacteristic?
    var robotInfo: String = ""
    
    required init?(coder: NSCoder) {
        super.init()
        fatalError("init(coder:) has not been implemented")
    }
    
    /// Initialization routine
    override init() {
        super.init()
        centralManager = CBCentralManager(delegate: self, queue: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(startNotification), name: Notification.Name("updateLabel"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(startNotification), name: Notification.Name("updateSerialMonitor"), object: nil)
    }
    
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        if (central.state == .poweredOn) {
            let options: [String: Any] = [CBCentralManagerScanOptionAllowDuplicatesKey: true]
            centralManager?.scanForPeripherals(withServices: [CBUUID(string: Constants.openbotService)], options: options);
        } else {
            print("bluetooth is off ")
        }
    }
    
    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        if peripheral.name != nil {
            if !peripherals.contains(peripheral) {
                peripherals.append(peripheral)
            }
        }
    }
    
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        print("connected to", peripheral)
        isBluetoothConnected = true;
        NotificationCenter.default.post(name: .bluetoothConnected, object: nil);
        peripheral.discoverServices(nil)
        
    }
    
    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        print("connection was failed")
    }
    
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        print("disconnected from :", peripheral)
        isBluetoothConnected = false
        NotificationCenter.default.post(name: .bluetoothDisconnected, object: nil)
    }
    
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        if let services = peripheral.services {
            for service in services {
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
                if (characteristic.properties == .write) {
                    writeCharacteristics = characteristic;
                } else if (characteristic.properties == .notify) {
                    subscribeToNotifications(peripheral: tempPeripheral, characteristic: characteristic)
                    readValue(characteristic: characteristic)
                    peripheral.discoverDescriptors(for: characteristic)
                }
            }
        }
    }
    
    func subscribeToNotifications(peripheral: CBPeripheral, characteristic: CBCharacteristic) {
        peripheral.setNotifyValue(true, for: characteristic)
    }
    
    func peripheral(_ peripheral: CBPeripheral, didUpdateNotificationStateFor characteristic: CBCharacteristic, error: Error?) {
        if error != nil {
            return
        }
    }
    
    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        //print("Data sent to :", peripheral.name as Any);
    }
    
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor descriptor: CBDescriptor, error: Error?) {
        _ = descriptor.value
    }
    
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        // Deal with errors (if any)
        if let error = error {
            print("Error discovering characteristics: %s", error.localizedDescription)
            return
        }
        
        guard let characteristicData = characteristic.value, let stringFromData = String(data: characteristicData, encoding: .utf8) else { return }
        let header = stringFromData.prefix(1)
        bluetoothData = stringFromData
        NotificationCenter.default.post(name: .updateSerialMonitor, object: nil)
        NotificationCenter.default.post(name: .updateLabel, object: nil)
        
        switch (header) {
        case "r":
            vehicleReady = true
            break
        case "f":
            robotInfo = stringFromData
            break
        case "v":
            voltageDivider = stringFromData
            break
        case "s":
            sonarData = stringFromData
            break
        case "w":
            speedometer = stringFromData
            break
        case "b":
            bumperData = stringFromData
            break
        default:
            break
        }
    }
    
    func peripheral(_ peripheral: CBPeripheral, didModifyServices invalidatedServices: [CBService]) {
        print(invalidatedServices)
    }
    
    func readValue(characteristic: CBCharacteristic) {
        tempPeripheral?.readValue(for: characteristic)
    }
    
    func sendData(payload: String) {
        let dataToSend: Data? = payload.data(using: String.Encoding.utf8)
        if (dataToSend != nil && tempPeripheral != nil) {
            if let writeCharacteristics {
                tempPeripheral.writeValue(dataToSend!, for: writeCharacteristics, type: CBCharacteristicWriteType.withResponse)
            }
        }
    }
    
    func disconnect() {
        centralManager?.cancelPeripheralConnection(tempPeripheral)
        startScan()
        peri = nil
    }
    
    func connect() {
        tempPeripheral = peri
        tempPeripheral.delegate = self
        centralManager?.connect(tempPeripheral)
        centralManager?.stopScan()
    }
    
    func startScan() {
        let options: [String: Any] = [CBCentralManagerScanOptionAllowDuplicatesKey: true]
        switch centralManager?.state {
        case .poweredOn:
            centralManager?.scanForPeripherals(withServices: [CBUUID(string: Constants.openbotService), CBUUID(string: Constants.openbotService_RX), CBUUID(string: Constants.openbotService_TX)], options: options);
        default:
            break
        }
        
    }
    
    @objc func startNotification() {
        //        tempCentralManager = CBCentralManager(delegate: self, queue: nil)
    }
}


