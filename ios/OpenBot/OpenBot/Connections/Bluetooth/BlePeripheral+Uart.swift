//
//  BlePeripheral+Uart.swift
//  Calibration
//
//  Created by Antonio García on 19/10/2016.
//  Copyright © 2016 Adafruit. All rights reserved.
//

import Foundation
import CoreBluetooth

extension BlePeripheral {
    // Config
    private static let kDebugLog = false

    // Costants
    static let kUartServiceUUID =           CBUUID(string: "6e400001-b5a3-f393-e0a9-e50e24dcca9e")
    static let kUartTxCharacteristicUUID =  CBUUID(string: "6e400002-b5a3-f393-e0a9-e50e24dcca9e")
    static let kUartRxCharacteristicUUID =  CBUUID(string: "6e400003-b5a3-f393-e0a9-e50e24dcca9e")
    //private static let kUartTxMaxBytes = 20
    static let kUartReplyDefaultTimeout = 2.0       // seconds

    // MARK: - Custom properties
    private struct CustomPropertiesKeys {
        static var uartRxCharacteristic: CBCharacteristic?
        static var uartTxCharacteristic: CBCharacteristic?
        static var uartTxCharacteristicWriteType: CBCharacteristicWriteType?
        static var sendSequentiallyCancelled: Bool = false
    }

    private var uartRxCharacteristic: CBCharacteristic? {
        get {
            return objc_getAssociatedObject(self, &CustomPropertiesKeys.uartRxCharacteristic) as! CBCharacteristic?
        }
        set {
            objc_setAssociatedObject(self, &CustomPropertiesKeys.uartRxCharacteristic, newValue, .OBJC_ASSOCIATION_RETAIN)
        }
    }

    private var uartTxCharacteristic: CBCharacteristic? {
        get {
            return objc_getAssociatedObject(self, &CustomPropertiesKeys.uartTxCharacteristic) as! CBCharacteristic?
        }
        set {
            objc_setAssociatedObject(self, &CustomPropertiesKeys.uartTxCharacteristic, newValue, .OBJC_ASSOCIATION_RETAIN)
        }
    }

    private var uartTxCharacteristicWriteType: CBCharacteristicWriteType? {
        get {
            return objc_getAssociatedObject(self, &CustomPropertiesKeys.uartTxCharacteristicWriteType) as! CBCharacteristicWriteType?
        }
        set {
            objc_setAssociatedObject(self, &CustomPropertiesKeys.uartTxCharacteristicWriteType, newValue, .OBJC_ASSOCIATION_RETAIN)
        }
    }

    private var isSendSequentiallyCancelled: Bool {
        get {
            return objc_getAssociatedObject(self, &CustomPropertiesKeys.sendSequentiallyCancelled) as! Bool
        }
        set {
            objc_setAssociatedObject(self, &CustomPropertiesKeys.sendSequentiallyCancelled, newValue, .OBJC_ASSOCIATION_RETAIN)
        }
    }

    
    // MARK: - Errors
    enum PeripheralUartError: Error {
        case invalidCharacteristic
        case enableNotifyFailed
    }

    // MARK: - Initialization
    func uartEnable(uartRxHandler: ((Data?, UUID, Error?) -> Void)?, completion: ((Error?) -> Void)?) {

        // Get uart communications characteristic
        characteristic(uuid: BlePeripheral.kUartTxCharacteristicUUID, serviceUuid: BlePeripheral.kUartServiceUUID) { [unowned self] (characteristic, error) in
            guard let characteristic = characteristic, error == nil else {
                completion?(error != nil ? error : PeripheralUartError.invalidCharacteristic)
                return
            }

            self.uartTxCharacteristic = characteristic
            self.uartTxCharacteristicWriteType = characteristic.properties.contains(.writeWithoutResponse) ? .withoutResponse:.withResponse
            //self.uartTxCharacteristicWriteType = .withResponse        // Debug: force withResponse

            self.characteristic(uuid: BlePeripheral.kUartRxCharacteristicUUID, serviceUuid: BlePeripheral.kUartServiceUUID) { [unowned self] (characteristic, error) in
                guard let characteristic = characteristic, error == nil else {
                    completion?(error != nil ? error : PeripheralUartError.invalidCharacteristic)
                    return
                }

                // Get characteristic info
                self.uartRxCharacteristic = characteristic

                // Prepare notification handler
                let notifyHandler: ((Error?) -> Void)? = { [unowned self] error in
                    let value = characteristic.value
                    if let value = value, BlePeripheral.kDebugLog == true, error == nil {
                        UartLogManager.log(data: value, type: .uartRx)
                    }

                    uartRxHandler?(value, self.identifier, error)
                }

                // Enable notifications
                if !characteristic.isNotifying {
                    self.enableNotify(for: characteristic, handler: notifyHandler, completion: { error in
                        completion?(error != nil ? error : (characteristic.isNotifying ? nil : PeripheralUartError.enableNotifyFailed))
                    })
                } else {
                    self.updateNotifyHandler(for: characteristic, handler: notifyHandler)
                    completion?(nil)
                }
            }
        }
    }

    func isUartEnabled() -> Bool {
        return uartRxCharacteristic != nil && uartTxCharacteristic != nil && uartTxCharacteristicWriteType != nil && uartRxCharacteristic!.isNotifying
    }

    func uartDisable() {
        // Disable notify
        guard let characteristic = uartRxCharacteristic, characteristic.isNotifying else { return }
        
        disableNotify(for: characteristic) { [weak self] error in
            guard let self = self else { return }

            // Clear all Uart specific data
            self.uartRxCharacteristic = nil
            self.uartTxCharacteristic = nil
            self.uartTxCharacteristicWriteType = nil
        }
    }

    // MARK: - Send
    func uartSend(data: Data?, progress: ((Float)->Void)? = nil, completion: ((Error?) -> Void)? = nil) {
        guard let data = data else { completion?(nil); return }
        
        guard let uartTxCharacteristic = uartTxCharacteristic, let uartTxCharacteristicWriteType = uartTxCharacteristicWriteType else {
            DLog("Command Error: characteristic no longer valid")
            completion?(PeripheralUartError.invalidCharacteristic)
            return
        }
        
        // Split data in kUartTxMaxBytes bytes packets
        var offset = 0
        var writtenSize = 0
        
        //let maxPacketSize = peripheral.maximumWriteValueLength(for: uartTxCharacteristicWriteType)
        let maxPacketSize = peripheral.maximumWriteValueLength(for: .withoutResponse)      // Use .withoutResponse event if sending .withResponse or didWriteValueFor is not called when using a larger packet
        
        repeat {
            
            let packetSize = min(data.count-offset, maxPacketSize)
            let packet = data.subdata(in: offset..<offset+packetSize)
            let writeStartingOffset = offset
            self.write(data: packet, for: uartTxCharacteristic, type: uartTxCharacteristicWriteType) { error in
                if let error = error {
                    DLog("write packet at offset: \(writeStartingOffset) error: \(error)")
                } else {
                    //DLog("tx offset:\(offset): \(hexDescription(data: packet))")
                    //DLog("uart tx write (hex): \(hexDescription(data: packet))")
                    // DLog("uart tx write (dec): \(decimalDescription(data: packet))")
                    // DLog("uart tx write (utf8): \(String(data: packet, encoding: .utf8) ?? "<invalid>")")
                    
                    writtenSize += packetSize
                    if BlePeripheral.kDebugLog {
                        UartLogManager.log(data: packet, type: .uartTx)
                    }
                }
                
                if writtenSize >= data.count {
                    progress?(1)
                    completion?(error)
                }
                else {
                    progress?(Float(writtenSize) / Float(data.count))
                }
            }
            offset += packetSize
        } while offset < data.count
        
    }
    
    func uartSendEachPacketSequentially(data: Data?, withResponseEveryPacketCount: Int,  progress: ((Float)->Void)? = nil, completion: ((Error?) -> Void)? = nil) {
        guard let data = data else { completion?(nil); return }
        
        guard let uartTxCharacteristic = uartTxCharacteristic else {//}, let uartTxCharacteristicWriteType = uartTxCharacteristicWriteType else {
            DLog("Command Error: characteristic no longer valid")
            completion?(PeripheralUartError.invalidCharacteristic)
            return
        }
        
        isSendSequentiallyCancelled = false
        uartSentPacket(data: data, offset: 0, uartTxCharacteristic: uartTxCharacteristic, withResponseEveryPacketCount: withResponseEveryPacketCount, numPacketsRemainingForDelay: withResponseEveryPacketCount, progress: progress, completion: completion)
    }
    
    func uartCancelOngoingSendPacketSequentiallyInMainThread() {
        isSendSequentiallyCancelled = true
    }
    
    private func uartSentPacket(data: Data, offset: Int, uartTxCharacteristic: CBCharacteristic, withResponseEveryPacketCount: Int, numPacketsRemainingForDelay: Int, progress: ((Float)->Void)? = nil, completion: ((Error?) -> Void)? = nil) {
        
        //let maxPacketSize = peripheral.maximumWriteValueLength(for: uartTxCharacteristicWriteType)
        let maxPacketSize = peripheral.maximumWriteValueLength(for: .withoutResponse)      // Use .withoutResponse event if sending .withResponse or didWriteValueFor is not called when using a larger packet
        let packetSize = min(data.count-offset, maxPacketSize)
        let packet = data.subdata(in: offset..<offset+packetSize)
        let writeStartingOffset = offset
        let uartTxCharacteristicWriteType: CBCharacteristicWriteType = numPacketsRemainingForDelay <= 0 ? .withResponse : .withoutResponse          // Send a packet .withResponse to force wait until receive response and avoid dropping packets if the peripheral is not processing them fast enough
        
        self.write(data: packet, for: uartTxCharacteristic, type: uartTxCharacteristicWriteType) { error in
            
            var writtenSize = writeStartingOffset
            if let error = error {
                DLog("write packet at offset: \(writeStartingOffset) error: \(error)")
            } else {
                DLog("uart tx \(uartTxCharacteristicWriteType == .withResponse ? "withResponse":"withoutResponse") offset: \(writeStartingOffset): \(HexUtils.hexDescription(data: packet))")
                
                writtenSize += packet.count
                if BlePeripheral.kDebugLog {
                    UartLogManager.log(data: packet, type: .uartTx)
                }
                
                if !self.isSendSequentiallyCancelled && writtenSize < data.count {
                    DispatchQueue.main.async {      // Send in main thread to avoid commandqueue function nesting limit if there is a lot of data to send
                        self.uartSentPacket(data: data, offset: writtenSize, uartTxCharacteristic: uartTxCharacteristic, withResponseEveryPacketCount: withResponseEveryPacketCount,  numPacketsRemainingForDelay: numPacketsRemainingForDelay <= 0 ? withResponseEveryPacketCount : numPacketsRemainingForDelay-1, progress: progress, completion: completion)
                    }
                }
            }
            
            // Call completion handlers in main thread
            DispatchQueue.main.async { [weak self] in
                guard let self = self else { return }
                
                if self.isSendSequentiallyCancelled {
                    completion?(nil)
                }
                else if writtenSize >= data.count {
                    progress?(1)
                    completion?(error)
                }
                else {
                    progress?(Float(writtenSize) / Float(data.count))
                }
            }
        }
    }

    func uartSendAndWaitReply(data: Data?, writeProgress: ((Float)->Void)? = nil, writeCompletion: ((Error?) -> Void)? = nil, readTimeout: Double? = BlePeripheral.kUartReplyDefaultTimeout, readCompletion: @escaping CapturedReadCompletionHandler) {
        
        guard let data = data else {
            if let writeCompletion = writeCompletion {
                writeCompletion(nil)
            } else {
                // If no writeCompletion defined, move the error result to the readCompletion
                readCompletion(nil, nil)
            }
        
            return
        }

        guard let uartTxCharacteristic = uartTxCharacteristic, /*let uartTxCharacteristicWriteType = uartTxCharacteristicWriteType, */let uartRxCharacteristic = uartRxCharacteristic else {
            DLog("Command Error: characteristic no longer valid")
            if let writeCompletion = writeCompletion {
                writeCompletion(PeripheralUartError.invalidCharacteristic)
            } else {
                // If no writeCompletion defined, move the error result to the readCompletion
                readCompletion(nil, PeripheralUartError.invalidCharacteristic)
            }
            return
        }

        // Split data in kUartTxMaxBytes bytes packets
        var offset = 0
        var writtenSize = 0
        //let maxPacketSize = peripheral.maximumWriteValueLength(for: .withResponse)
        let maxPacketSize = peripheral.maximumWriteValueLength(for: .withoutResponse)      // Use .withoutResponse event if sending .withResponse or didWriteValueFor is not called when using a larger packet
        repeat {
            let packetSize = min(data.count-offset, maxPacketSize)
            let packet = data.subdata(in: offset..<offset+packetSize)
            offset += packetSize

            writeAndCaptureNotify(data: packet, for: uartTxCharacteristic, writeCompletion: { error in
                if let error = error {
                    DLog("write packet at offset: \(offset) error: \(error)")
                } else {
                    DLog("uart tx writeAndWait (hex): \(HexUtils.hexDescription(data: packet))")
//                    DLog("uart tx writeAndWait (dec): \(decimalDescription(data: packet))")
//                    DLog("uart tx writeAndWait (utf8): \(String(data: packet, encoding: .utf8) ?? "<invalid>")")
                    
                    writtenSize += packetSize
                }

                if writtenSize >= data.count {
                    writeProgress?(1)
                    writeCompletion?(error)
                }
                else {
                    writeProgress?(Float(writtenSize) / Float(data.count))
                }
            }, readCharacteristic: uartRxCharacteristic, readTimeout: readTimeout, readCompletion: readCompletion)

        } while offset < data.count
    }

    // MARK: - Utils
    func isUartAdvertised() -> Bool {
        return advertisement.services?.contains(BlePeripheral.kUartServiceUUID) ?? false
    }

    func hasUart() -> Bool {
        return peripheral.services?.first(where: {$0.uuid == BlePeripheral.kUartServiceUUID}) != nil
    }
}

// MARK: - Data + CRC
extension Data {
    mutating func appendCrc() {
        var dataBytes = [UInt8](repeating: 0, count: count)
        copyBytes(to: &dataBytes, count: count)

        var crc: UInt8 = 0
        for i in dataBytes {    //add all bytes
            crc = crc &+ i
        }
        crc = ~crc  //invert

        append(&crc, count: 1)
    }
}
