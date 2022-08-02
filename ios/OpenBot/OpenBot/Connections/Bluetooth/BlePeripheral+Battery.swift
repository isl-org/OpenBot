//
//  BlePeripheral+Battery.swift
//  Bluefruit
//
//  Created by Antonio García on 22/06/2017.
//  Copyright © 2017 Adafruit. All rights reserved.
//

import Foundation

import CoreBluetooth

extension BlePeripheral {
    // Costants
    static let kBatteryServiceUUID =        CBUUID(string: "180F")
    static let kBatteryCharacteristicUUID = CBUUID(string: "2A19")

    // MARK: - Actions
    func readBatteryLevel(handler: @escaping ((Int, Error?) -> Void)) {
        self.characteristic(uuid: BlePeripheral.kBatteryCharacteristicUUID, serviceUuid: BlePeripheral.kBatteryServiceUUID) { (characteristic, error) in
            guard error == nil, let characteristic = characteristic else { DLog("Error reading battery characteristic: \(error?.localizedDescription ?? "")"); return }

            self.readCharacteristic(characteristic) { (result, error) in
                guard error == nil, let data = result as? Data, data.count >= 1 else {
                    DLog("Error reading battery level: \(error?.localizedDescription ?? "")")
                    handler(-1, error)
                    return
                }

                let level = Int(data[0])        // from 0 to 100
                handler(level, nil)
            }
        }
    }

    func startReadingBatteryLevel(handler: @escaping ((Int) -> Void)) {

        self.characteristic(uuid: BlePeripheral.kBatteryCharacteristicUUID, serviceUuid: BlePeripheral.kBatteryServiceUUID) { (characteristic, error) in
            guard error == nil, let characteristic = characteristic else { DLog("Error starting read for battery characteristic: \(error?.localizedDescription ?? "")"); return }

            // Read current value
            self.readCharacteristic(characteristic) { (result, error) in
                guard error == nil, let data = result as? Data, data.count >= 1 else {  DLog("Error reading battery level: \(error?.localizedDescription ?? "")"); return }

                let level = Int(data[0])        // from 0 to 100
                handler(level)
            }

            // Enable notifications to receive value changes
            self.enableNotify(for: characteristic, handler: { error in
                guard error == nil else { DLog("Error receiving notify for battery level"); return }
                guard let data = characteristic.value, data.count >= 1 else { DLog("Invalid data receiving notify for battery level"); return }

                let level = Int(data[0])        // from 0 to 100
                handler(level)
            })
        }
    }

    func stopReadingBatteryLevel() {
        self.characteristic(uuid: BlePeripheral.kBatteryCharacteristicUUID, serviceUuid: BlePeripheral.kBatteryServiceUUID) { (characteristic, error) in
            guard error == nil, let characteristic = characteristic else { DLog("Error stopping read for battery characteristic: \(error?.localizedDescription ?? "")"); return }

            self.disableNotify(for: characteristic)
        }
    }

    // MARK: - Utils
    func isBatteryAdvertised() -> Bool {
        return advertisement.services?.contains(BlePeripheral.kBatteryServiceUUID) ?? false
    }

    func hasBattery() -> Bool {
        return peripheral.services?.first(where: {$0.uuid == BlePeripheral.kBatteryServiceUUID}) != nil
    }
}
