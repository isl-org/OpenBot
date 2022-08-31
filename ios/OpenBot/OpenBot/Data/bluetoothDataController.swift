//
// Created by Nitish Yadav on 29/08/22.
//
import Foundation
import CoreBluetooth
import CoreMotion

class bluetoothDataController: CMDeviceMotion, CBCentralManagerDelegate, CBPeripheralDelegate {
    static let shared: bluetoothDataController = bluetoothDataController()
    var centralManager: CBCentralManager?
    var tempPeripheral: CBPeripheral!
    var LabelString: String!
    private var allServices: [CBService]?
    var writeCharacteristics: CBCharacteristic?

    required init?(coder: NSCoder) {
        super.init()
        fatalError("init(coder:) has not been implemented")
    }

    override init() {
        super.init()
        centralManager = CBCentralManager(delegate: self, queue: nil)
        print("i am in init function")
    }

    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        centralManager?.scanForPeripherals(withServices: nil, options: nil)
    }

    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        print(peripheral, "hello")
        if peripheral.name == peri?.name {
            print(peripheral)
            centralManager?.stopScan()
            tempPeripheral = peripheral
//            print("peripheral is :",tempPeripheral)
            tempPeripheral.delegate = self
            centralManager?.connect(tempPeripheral)
        }
    }

    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
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
                allServices?.append(service)
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
        if error != nil {
            return
        }
    }

    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        print("Data sent to :", peripheral.name as Any);

    }

    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor descriptor: CBDescriptor, error: Error?) {
        print(descriptor)
    }

    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        if characteristic.value != nil {
            let data = characteristic.value!
            let x = String(data: data, encoding: .utf8)
            LabelString = x
            bluetoothData = x ?? "nil"
        }
    }

    func readValue(characteristic: CBCharacteristic) {
        tempPeripheral?.readValue(for: characteristic)
    }

    func sendData(payload: String) {
        let dataToSend: Data = payload.data(using: String.Encoding.utf8)!
        tempPeripheral!.writeValue(dataToSend, for: writeCharacteristics!, type: CBCharacteristicWriteType.withResponse)
    }
    func disconnect(){
        centralManager?.cancelPeripheralConnection(tempPeripheral)
    }
}