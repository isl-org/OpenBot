//
// Created by Sparsh Jain on 01/08/22.
//

import Foundation
import CoreBluetooth

// TODO: Modernize completion blocks to use Swift.Result

open class BlePeripheral: NSObject {
    // Config
    private static var kProfileCharacteristicUpdates = true

    // Constants
    static var kUndefinedRssiValue = 127

    // Notifications
    public enum NotificationUserInfoKey: String {
        case uuid = "uuid"
        case name = "name"
        case invalidatedServices = "invalidatedServices"
    }

    enum PeripheralError: Error {
        case timeout
    }

    // Data
    var peripheral: CBPeripheral

    public static var rssiRunningAverageFactor: Double = 1        /// Global Parameter that affects all rssi measurements. 1 means don't use a running average. The closer to 0 the more resistant the value it is to change
    private var runningRssi: Int?
    public var rssi: Int? {
        /// rssi only is updated when a non undefined value is received from CoreBluetooth. Note: this is slighty different to the CoreBluetooth implementation, because it will not be updated with undefined values.  If runningRssiFactorFactor == 1, the newer value replaces the old value and not average is calculated
        get {
            return runningRssi
        }
        set {
            guard newValue != BlePeripheral.kUndefinedRssiValue else {
                return
            }     // Don't accept undefined values

            // based on https://en.wikipedia.org/wiki/Exponential_smoothing
            if newValue == nil || runningRssi == nil || runningRssi == BlePeripheral.kUndefinedRssiValue {
                runningRssi = newValue
            } else {
                runningRssi = Int(BlePeripheral.rssiRunningAverageFactor * Double(newValue!) + (1 - BlePeripheral.rssiRunningAverageFactor) * Double(runningRssi!))
            }
        }
    }
    public var lastSeenTime: CFAbsoluteTime

    open var identifier: UUID {
        return peripheral.identifier
    }

    open var name: String? {
        return peripheral.name
    }

    public var debugName: String {
        return peripheral.name ?? peripheral.identifier.uuidString
    }

    open var state: CBPeripheralState {
        return peripheral.state
    }

    func maximumWriteValueLength(for: CBCharacteristicWriteType) -> Int {
        return peripheral.maximumWriteValueLength(for: .withoutResponse)
    }

    public struct Advertisement {
        var advertisementData: [String: Any]

        init(advertisementData: [String: Any]?) {
            self.advertisementData = advertisementData ?? [String: Any]()
        }

        // Advertisement data formatted
        public var localName: String? {
            return advertisementData[CBAdvertisementDataLocalNameKey] as? String
        }

        public var manufacturerData: Data? {
            return advertisementData[CBAdvertisementDataManufacturerDataKey] as? Data
        }

        public var manufacturerHexDescription: String? {
            guard let manufacturerData = manufacturerData else {
                return nil
            }
            return HexUtils.hexDescription(data: manufacturerData)
//            return String(data: manufacturerData, encoding: .utf8)
        }

        public var manufacturerIdentifier: Data? {
            guard let manufacturerData = manufacturerData, manufacturerData.count >= 2 else {
                return nil
            }
            let manufacturerIdentifierData = manufacturerData[0..<2]
            return manufacturerIdentifierData
        }

        public var services: [CBUUID]? {
            return advertisementData[CBAdvertisementDataServiceUUIDsKey] as? [CBUUID]
        }

        public var servicesOverflow: [CBUUID]? {
            return advertisementData[CBAdvertisementDataOverflowServiceUUIDsKey] as? [CBUUID]
        }

        public var servicesSolicited: [CBUUID]? {
            return advertisementData[CBAdvertisementDataSolicitedServiceUUIDsKey] as? [CBUUID]
        }

        public var serviceData: [CBUUID: Data]? {
            return advertisementData[CBAdvertisementDataServiceDataKey] as? [CBUUID: Data]
        }

        public var txPower: Int? {
            let number = advertisementData[CBAdvertisementDataTxPowerLevelKey] as? NSNumber
            return number?.intValue
        }

        public var isConnectable: Bool? {
            let connectableNumber = advertisementData[CBAdvertisementDataIsConnectable] as? NSNumber
            return connectableNumber?.boolValue
        }
    }

    public var advertisement: Advertisement

    typealias CapturedReadCompletionHandler = (_ value: Any?, _ error: Error?) -> Void

    private class CaptureReadHandler {

        var identifier: String
        var result: CapturedReadCompletionHandler
        var timeoutTimer: Foundation.Timer?
        var timeoutAction: ((String) -> Void)?
        var isNotifyOmitted: Bool

        init(identifier: String, result: @escaping CapturedReadCompletionHandler, timeout: Double?, timeoutAction: ((String) -> Void)?, isNotifyOmitted: Bool = false) {
            self.identifier = identifier
            self.result = result
            self.isNotifyOmitted = isNotifyOmitted

            if let timeout = timeout {
                self.timeoutAction = timeoutAction
                DispatchQueue.global(qos: .background).async {
                    self.timeoutTimer = Timer.scheduledTimer(timeInterval: timeout, target: self, selector: #selector(self.timerFired), userInfo: nil, repeats: false)
                }
            }
        }

        @objc private func timerFired() {
            timeoutTimer?.invalidate()
            timeoutTimer = nil
            result(nil, PeripheralError.timeout)
            timeoutAction?(identifier)
        }
    }

    private func timeOutRemoveCaptureHandler(identifier: String) {   // Default behaviour for a capture handler timeout
        guard captureReadHandlers.count > 0, let index = captureReadHandlers.firstIndex(where: { $0.identifier == identifier }) else {
            return
        }
        // print("captureReadHandlers index: \(index) / \(captureReadHandlers.count)")

        // Remove capture handler
        captureReadHandlers.remove(at: index)
        finishedExecutingCommand(error: PeripheralError.timeout)
    }

    // Internal data
    private var notifyHandlers = [String: ((Error?) -> Void)]()                 // Nofify handlers for each service-characteristic
    private var captureReadHandlers = [CaptureReadHandler]()
    private var commandQueue = CommandQueue<BleCommand>()

    // Profiling
    //private var profileStartTime: CFTimeInterval = 0

    // MARK: - Init
    public init(peripheral: CBPeripheral, advertisementData: [String: Any]?, rssi: Int?) {
        self.peripheral = peripheral
        self.advertisement = Advertisement(advertisementData: advertisementData)
        self.lastSeenTime = CFAbsoluteTimeGetCurrent()

        super.init()
        self.rssi = rssi
        self.peripheral.delegate = self
        // print("create peripheral: \(peripheral.name ?? peripheral.identifier.uuidString)")
        commandQueue.executeHandler = executeCommand
    }

    deinit {
        //print("peripheral deinit")
    }

    func reset() {
        rssi = nil
        notifyHandlers.removeAll()
        captureReadHandlers.removeAll()
        commandQueue.first()?.isCancelled = true        // Stop current command if is processing
        commandQueue.removeAll()
    }

    // MARK: - Discover
    func discover(serviceUuids: [CBUUID]?, completion: ((Error?) -> Void)?) {
        let command = BleCommand(type: .discoverService, parameters: serviceUuids, completion: completion)
        commandQueue.append(command)
    }

    func discover(characteristicUuids: [CBUUID]?, service: CBService, completion: ((Error?) -> Void)?) {
        let command = BleCommand(type: .discoverCharacteristic, parameters: [characteristicUuids as Any, service], completion: completion)
        commandQueue.append(command)
    }

    func discover(characteristicUuids: [CBUUID]?, serviceUuid: CBUUID, completion: ((Error?) -> Void)?) {
        // Discover service
        discover(serviceUuids: [serviceUuid]) { [unowned self] error in
            guard error == nil else {
                completion?(error)
                return
            }

            guard let service = self.peripheral.services?.first(where: { $0.uuid == serviceUuid }) else {
                completion?(BleCommand.CommandError.invalidService)
                return
            }

            // Discover characteristic
            self.discover(characteristicUuids: characteristicUuids, service: service, completion: completion)
        }
    }

    func discoverDescriptors(characteristic: CBCharacteristic, completion: ((Error?) -> Void)?) {
        print("log test 10001");
        let command = BleCommand(type: .discoverDescriptor, parameters: [characteristic], completion: completion)
        print("log 10002");
        commandQueue.append(command)
    }

    // MARK: - Connection
    func disconnect(centralManager: CBCentralManager) {
        let command = BleCommand(type: .disconnect, parameters: [centralManager], completion: nil)
        commandQueue.append(command)
    }

    // MARK: - Service
    func discoveredService(uuid: CBUUID) -> CBService? {
        let service = peripheral.services?.first(where: { $0.uuid == uuid })
        return service
    }

    func service(uuid: CBUUID, completion: ((CBService?, Error?) -> Void)?) {

        if let discoveredService = discoveredService(uuid: uuid) {                      // Service was already discovered
            completion?(discoveredService, nil)
        } else {
            discover(serviceUuids: [uuid], completion: { [unowned self] (error) in
                // Discover service
                var discoveredService: CBService?
                if error == nil {
                    discoveredService = self.discoveredService(uuid: uuid)
                }
                completion?(discoveredService, error)
            })
        }
    }

    // MARK: - Characteristic
    func discoveredCharacteristic(uuid: CBUUID, service: CBService) -> CBCharacteristic? {
        let characteristic = service.characteristics?.first(where: { $0.uuid == uuid })
        return characteristic
    }

    func characteristic(uuid: CBUUID, service: CBService, completion: ((CBCharacteristic?, Error?) -> Void)?) {

        if let discoveredCharacteristic = discoveredCharacteristic(uuid: uuid, service: service) {              // Characteristic was already discovered
            completion?(discoveredCharacteristic, nil)
        } else {
            discover(characteristicUuids: [uuid], service: service, completion: { [unowned self] (error) in
                // Discover characteristic
                var discoveredCharacteristic: CBCharacteristic?
                if error == nil {
                    discoveredCharacteristic = self.discoveredCharacteristic(uuid: uuid, service: service)
                }
                completion?(discoveredCharacteristic, error)
            })
        }
    }

    func characteristic(uuid: CBUUID, serviceUuid: CBUUID, completion: ((CBCharacteristic?, Error?) -> Void)?) {
        if let discoveredService = discoveredService(uuid: serviceUuid) {                                              // Service was already discovered
            characteristic(uuid: uuid, service: discoveredService, completion: completion)
        } else {                                                                                                // Discover service
            service(uuid: serviceUuid) { (service, error) in
                if let service = service, error == nil {                                                        // Discover characteristic
                    print("log test 1000");
                    self.characteristic(uuid: uuid, service: service, completion: completion)
                    print("log test 1001");
                } else {
                    print("log test 1002");
                    completion?(nil, error != nil ? error : BleCommand.CommandError.invalidService)
                    print("log test 1003");
                }
            }
        }
    }

    func enableNotify(for characteristic: CBCharacteristic, handler: ((Error?) -> Void)?, completion: ((Error?) -> Void)? = nil) {
        let command = BleCommand(type: .setNotify, parameters: [characteristic, true, handler as Any], completion: completion)
        commandQueue.append(command)
    }

    func disableNotify(for characteristic: CBCharacteristic, completion: ((Error?) -> Void)? = nil) {
        let command = BleCommand(type: .setNotify, parameters: [characteristic, false], completion: completion)
        commandQueue.append(command)
    }

    func updateNotifyHandler(for characteristic: CBCharacteristic, handler: ((Error?) -> Void)? = nil) {
        let identifier = handlerIdentifier(from: characteristic)
        if notifyHandlers[identifier] == nil {
            print("Warning: trying to update inexistent notifyHandler")
        }
        notifyHandlers[identifier] = handler
    }

    func readCharacteristic(_ characteristic: CBCharacteristic, completion readCompletion: @escaping CapturedReadCompletionHandler) {
        print("log test 1");
        let command = BleCommand(type: .readCharacteristic, parameters: [characteristic, readCompletion as Any], completion: nil)
        print("log test 2");
        commandQueue.append(command)
        print("log test 10");
    }

    func write(data: Data, for characteristic: CBCharacteristic, type: CBCharacteristicWriteType, completion: ((Error?) -> Void)? = nil) {
        let command = BleCommand(type: .writeCharacteristic, parameters: [characteristic, type, data], completion: completion)
        commandQueue.append(command)
    }

    func writeAndCaptureNotify(data: Data, for characteristic: CBCharacteristic, writeCompletion: ((Error?) -> Void)? = nil, readCharacteristic: CBCharacteristic, readTimeout: Double? = nil, readCompletion: CapturedReadCompletionHandler? = nil) {
        let type: CBCharacteristicWriteType = .withResponse     // Force write with response
        let command = BleCommand(type: .writeCharacteristicAndWaitNofity, parameters: [characteristic, type, data, readCharacteristic, readCompletion as Any, readTimeout as Any], timeout: readTimeout, completion: writeCompletion)
        commandQueue.append(command)
    }

    // MARK: - Descriptors
    func readDescriptor(_ descriptor: CBDescriptor, completion readCompletion: @escaping CapturedReadCompletionHandler) {
        let command = BleCommand(type: .readDescriptor, parameters: [descriptor, readCompletion as Any], completion: nil)
        commandQueue.append(command)
    }

    // MARK: - Rssi
    func readRssi() {
        peripheral.readRSSI()
    }

    // MARK: - Command Queue
    internal class BleCommand: Equatable {
        enum CommandType {
            case discoverService
            case discoverCharacteristic
            case discoverDescriptor
            case setNotify
            case readCharacteristic
            case writeCharacteristic
            case writeCharacteristicAndWaitNofity
            case readDescriptor
            case disconnect
        }

        enum CommandError: Error {
            case invalidService
        }

        var type: CommandType
        var parameters: [Any]?
        var completion: ((Error?) -> Void)?
        var isCancelled = false

        init(type: CommandType, parameters: [Any]?, timeout: Double? = nil, completion: ((Error?) -> Void)?) {
            self.type = type
            self.parameters = parameters
            self.completion = completion
        }

        func completion(withError error: Error?) {
            completion?(error)
        }

        static func ==(left: BleCommand, right: BleCommand) -> Bool {
            return left.type == right.type
        }
    }

    private func executeCommand(command: BleCommand) {

        switch command.type {
        case .discoverService:
            discoverService(with: command)
        case .discoverCharacteristic:
            discoverCharacteristic(with: command)
        case .discoverDescriptor:
            discoverDescriptor(with: command)
        case .setNotify:
            setNotify(with: command)
        case .readCharacteristic:
            readCharacteristic(with: command)
        case .writeCharacteristic, .writeCharacteristicAndWaitNofity:
            write(with: command)
        case .readDescriptor:
            readDescriptor(with: command)
        case .disconnect:
            disconnect(with: command)
        }
    }

    private func handlerIdentifier(from characteristic: CBCharacteristic) -> String {
        guard let service = characteristic.service else {
            print("Error: handleIdentifier with nil characteritic service"); return ""
        }
        return "\(service.uuid.uuidString)-\(characteristic.uuid.uuidString)"
    }

    private func handlerIdentifier(from descriptor: CBDescriptor) -> String {
        guard let characteristic = descriptor.characteristic, let service = characteristic.service else {
            print("Error: handleIdentifier with nil descriptor service"); return ""
        }
        return "\(service.uuid.uuidString)-\(characteristic.uuid.uuidString)-\(descriptor.uuid.uuidString)"
    }

    internal func finishedExecutingCommand(error: Error?) {
        //print("finishedExecutingCommand")

        // Result Callback
        if let command = commandQueue.first(), !command.isCancelled {
            command.completion(withError: error)
        }
        commandQueue.executeNext()
    }

    // MARK: - Commands
    private func discoverService(with command: BleCommand) {
        var serviceUuids = command.parameters as? [CBUUID]
        let discoverAll = serviceUuids == nil

        // Remove services already discovered from the query
        if let services = peripheral.services, let serviceUuidsToDiscover = serviceUuids {
            for (i, serviceUuid) in serviceUuidsToDiscover.enumerated().reversed() {
                if services.contains(where: { $0.uuid == serviceUuid }) {
                    serviceUuids!.remove(at: i)
                }
            }
        }

        // Discover remaining uuids
        if discoverAll || (serviceUuids != nil && serviceUuids!.count > 0) {
            peripheral.discoverServices(serviceUuids)
        } else {
            // Everything was already discovered
            finishedExecutingCommand(error: nil)
        }
    }

    private func discoverCharacteristic(with command: BleCommand) {
        var characteristicUuids = command.parameters![0] as? [CBUUID]
        let discoverAll = characteristicUuids == nil
        let service = command.parameters![1] as! CBService

        // Remove services already discovered from the query
        if let characteristics = service.characteristics, let characteristicUuidsToDiscover = characteristicUuids {
            for (i, characteristicUuid) in characteristicUuidsToDiscover.enumerated().reversed() {
                if characteristics.contains(where: { $0.uuid == characteristicUuid }) {
                    characteristicUuids!.remove(at: i)
                }
            }
        }

        // Discover remaining uuids
        if discoverAll || (characteristicUuids != nil && characteristicUuids!.count > 0) {
            //print("discover \(characteristicUuids == nil ? "all": String(characteristicUuids!.count)) characteristics for \(service.uuid.uuidString)")
            peripheral.discoverCharacteristics(characteristicUuids, for: service)
        } else {
            // Everthing was already discovered
            finishedExecutingCommand(error: nil)
        }
    }

    private func discoverDescriptor(with command: BleCommand) {
        print("test 1");
        let characteristic = command.parameters![0] as! CBCharacteristic
        print("test 2", characteristic);
        peripheral.discoverDescriptors(for: characteristic)
        print("test 3");
    }

    private func setNotify(with command: BleCommand) {
        let characteristic = command.parameters![0] as! CBCharacteristic
        let enabled = command.parameters![1] as! Bool
        let identifier = handlerIdentifier(from: characteristic)
        if enabled {
            let handler = command.parameters![2] as? (Error?) -> Void
            notifyHandlers[identifier] = handler
        } else {
            notifyHandlers.removeValue(forKey: identifier)
        }
        peripheral.setNotifyValue(enabled, for: characteristic)
    }

    private func readCharacteristic(with command: BleCommand) {
        print("log test 2");
        let characteristic = command.parameters!.first as! CBCharacteristic
        let completion = command.parameters![1] as! CapturedReadCompletionHandler

        let identifier = handlerIdentifier(from: characteristic)
        let captureReadHandler = CaptureReadHandler(identifier: identifier, result: completion, timeout: nil, timeoutAction: timeOutRemoveCaptureHandler)
        captureReadHandlers.append(captureReadHandler)
        print("log test 3");
        peripheral.readValue(for: characteristic)
        print("log test 4");
    }

    private func write(with command: BleCommand) {
        let characteristic = command.parameters![0] as! CBCharacteristic
        let writeType = command.parameters![1] as! CBCharacteristicWriteType
        let data = command.parameters![2] as! Data


        if writeType == .withoutResponse {
            let mtu = maximumWriteValueLength(for: .withoutResponse)
            var offset = 0
            while offset < data.count {
                let chunkData = data.subdata(in: offset..<min(offset + mtu, data.count))
                //print("blewrite offset: \(offset) / \(data.count), size: \(chunkData.count)")
                peripheral.writeValue(chunkData, for: characteristic, type: .withoutResponse)
                offset += chunkData.count
            }

            if !command.isCancelled, command.type == .writeCharacteristicAndWaitNofity {
                let readCharacteristic = command.parameters![3] as! CBCharacteristic
                let readCompletion = command.parameters![4] as! CapturedReadCompletionHandler
                let timeout = command.parameters![5] as? Double

                let identifier = handlerIdentifier(from: readCharacteristic)
                let captureReadHandler = CaptureReadHandler(identifier: identifier, result: readCompletion, timeout: timeout, timeoutAction: timeOutRemoveCaptureHandler)
                captureReadHandlers.append(captureReadHandler)
            }

            finishedExecutingCommand(error: nil)
        } else {
            peripheral.writeValue(data, for: characteristic, type: writeType)
        }
    }

    private func readDescriptor(with command: BleCommand) {
        let descriptor = command.parameters!.first as! CBDescriptor
        let completion = command.parameters![1] as! CapturedReadCompletionHandler

        let identifier = handlerIdentifier(from: descriptor)
        let captureReadHandler = CaptureReadHandler(identifier: identifier, result: completion, timeout: nil, timeoutAction: timeOutRemoveCaptureHandler)
        captureReadHandlers.append(captureReadHandler)

        peripheral.readValue(for: descriptor)
    }

    internal func disconnect(with command: BleCommand) {
        let centralManager = command.parameters!.first as! CBCentralManager
        centralManager.cancelPeripheralConnection(self.peripheral)
        finishedExecutingCommand(error: nil)
    }
}

// MARK: - CBPeripheralDelegate
extension BlePeripheral: CBPeripheralDelegate {
    public func peripheralDidUpdateName(_ peripheral: CBPeripheral) {
        print("peripheralDidUpdateName: \(name ?? "{ No Name }")")
        NotificationCenter.default.post(name: .peripheralDidUpdateName, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheral.identifier, NotificationUserInfoKey.name.rawValue: name as Any])
    }

    public func peripheral(_ peripheral: CBPeripheral, didModifyServices invalidatedServices: [CBService]) {
        print("didModifyServices")
        NotificationCenter.default.post(name: .peripheralDidModifyServices, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheral.identifier, NotificationUserInfoKey.invalidatedServices.rawValue: invalidatedServices])
    }

    public func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {


        print("didDiscoverServices for: \(peripheral.name ?? peripheral.identifier.uuidString)")
//        print("i am here")

        finishedExecutingCommand(error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        print("hello nitissh")
//        print("didDiscoverCharacteristicsFor: \(service.uuid.uuidString)")
        finishedExecutingCommand(error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didUpdateNotificationStateFor characteristic: CBCharacteristic, error: Error?) {
        finishedExecutingCommand(error: error)

    }

    public func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {

        let identifier = handlerIdentifier(from: characteristic)
//        print("now i am here officily")
//        print(identifier)
//        let dataToSend: Data = "hello nwvfwdvhwqkdjqwbv".data(using: String.Encoding.utf8)!
//        let temp =  peripheral
//       temp.writeValue(dataToSend, for: characteristic, type: CBCharacteristicWriteType.withResponse)

        /*
        if (BlePeripheral.kProfileCharacteristicUpdates) {
            let currentTime = CACurrentMediaTime()
            let elapsedTime = currentTime - profileStartTime
            print("elapsed: \(String(format: "%.1f", elapsedTime * 1000))")
            profileStartTime = currentTime
        }
         */
        //print("didUpdateValueFor \(characteristic.uuid.uuidString): \(String(data: characteristic.value ?? Data(), encoding: .utf8) ?? "<invalid>")")

        // Check if waiting to capture this read
        var isNotifyOmitted = false
        var hasCaptureHandler = false
        if captureReadHandlers.count > 0, let index = captureReadHandlers.firstIndex(where: { $0.identifier == identifier }) {
            hasCaptureHandler = true
            // print("captureReadHandlers index: \(index) / \(captureReadHandlers.count)")

            // Remove capture handler
            let captureReadHandler = captureReadHandlers.remove(at: index)

            //  print("captureReadHandlers postRemove count: \(captureReadHandlers.count)")

            // Cancel timeout timer
            captureReadHandler.timeoutTimer?.invalidate()
            captureReadHandler.timeoutTimer = nil

            // Send result
            let value = characteristic.value
            //  print("updated value: \(String(data: value!, encoding: .utf8)!)")
            captureReadHandler.result(value, error)

            isNotifyOmitted = captureReadHandler.isNotifyOmitted
        }

        // Notify
        if !isNotifyOmitted {
            if let notifyHandler = notifyHandlers[identifier] {

                //let currentTime = CACurrentMediaTime()
                notifyHandler(error)
                //print("elapsed: \(String(format: "%.1f", (CACurrentMediaTime() - currentTime) * 1000))")
            }
        }

        if hasCaptureHandler {
            finishedExecutingCommand(error: error)
        }
    }

    public func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        if let command = commandQueue.first(), !command.isCancelled, command.type == .writeCharacteristicAndWaitNofity {
            let characteristic = command.parameters![3] as! CBCharacteristic
            let readCompletion = command.parameters![4] as! CapturedReadCompletionHandler
            let timeout = command.parameters![5] as? Double
            let identifier = handlerIdentifier(from: characteristic)

            //print("read timeout started")
            let captureReadHandler = CaptureReadHandler(identifier: identifier, result: readCompletion, timeout: timeout, timeoutAction: timeOutRemoveCaptureHandler)
            captureReadHandlers.append(captureReadHandler)
        } else {
            finishedExecutingCommand(error: error)
        }
    }

    public func peripheral(_ peripheral: CBPeripheral, didDiscoverDescriptorsFor characteristic: CBCharacteristic, error: Error?) {
        finishedExecutingCommand(error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor descriptor: CBDescriptor, error: Error?) {
        let identifier = handlerIdentifier(from: descriptor)

        if captureReadHandlers.count > 0, let index = captureReadHandlers.firstIndex(where: { $0.identifier == identifier }) {
            // Remove capture handler
            let captureReadHandler = captureReadHandlers.remove(at: index)

            // Send result
            let value = descriptor.value
            captureReadHandler.result(value, error)

            finishedExecutingCommand(error: error)
        }
    }

    public func peripheral(_ peripheral: CBPeripheral, didReadRSSI RSSI: NSNumber, error: Error?) {
        guard error == nil else {
            print("didReadRSSI error: \(error!.localizedDescription)"); return
        }

        let rssi = RSSI.intValue
        if rssi != BlePeripheral.kUndefinedRssiValue {  // only update rssi value if is defined ( 127 means undefined )
            self.rssi = rssi
        }

        NotificationCenter.default.post(name: .peripheralDidUpdateRssi, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheral.identifier])
    }
}

// MARK: - Custom Notifications
extension Notification.Name {
    private static let kPrefix = Bundle.main.bundleIdentifier!
    public static let peripheralDidUpdateName = Notification.Name(kPrefix + ".peripheralDidUpdateName")
    public static let peripheralDidModifyServices = Notification.Name(kPrefix + ".peripheralDidModifyServices")
    public static let peripheralDidUpdateRssi = Notification.Name(kPrefix + ".peripheralDidUpdateRssi")
}
