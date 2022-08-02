//
// Created by Sparsh Jain on 01/08/22.
//

import Foundation
import CoreBluetooth
import QuartzCore

public class BleManager: NSObject {
    // Configuration
    private static let kStopScanningWhenConnectingToPeripheral = false
    private static let kAlwaysAllowDuplicateKeys = true

    // Singleton
    public static let shared = BleManager()

    // Ble
    var centralManager: CBCentralManager?
    private var centralManagerPoweredOnSemaphore = DispatchSemaphore(value: 1)

    // Scanning
    public var isScanning: Bool {
        return scanningStartTime != nil
    }
    public var scanningElapsedTime: TimeInterval? {
        guard let scanningStartTime = scanningStartTime else { return nil }
        return CACurrentMediaTime() - scanningStartTime
    }
    private var isScanningWaitingToStart = false
    internal var scanningStartTime: TimeInterval?        // Time when the scanning started. nil if stopped
    private var scanningServicesFilter: [CBUUID]?
    internal var peripheralsFound = [UUID: BlePeripheral]()
    private var peripheralsFoundFirstTime = [UUID: Date]()       // Date that the perihperal was discovered for the first time. Useful for sorting
    internal var peripheralsFoundLock = NSLock()

    // Connecting
    private var connectionTimeoutTimers = [UUID: Foundation.Timer]()

    // Notifications
    public enum NotificationUserInfoKey: String {
        case uuid = "uuid"
        case error = "error"
    }

    override init() {
        super.init()

        centralManagerPoweredOnSemaphore.wait()
        centralManager = CBCentralManager(delegate: self, queue: DispatchQueue.global(qos: .background), options: [:])
//        centralManager = CBCentralManager(delegate: self, queue: DispatchQueue.main, options: [:])
    }

    deinit {
        scanningServicesFilter?.removeAll()
        peripheralsFound.removeAll()
        peripheralsFoundFirstTime.removeAll()
    }

    public var state: CBManagerState {
        return centralManager?.state ?? .unknown
    }

    func restoreCentralManager() {
        print("Restoring central manager")

        // Restore peripherals status
        peripheralsFoundLock.lock()

        for (_, blePeripheral) in peripheralsFound {
            blePeripheral.peripheral.delegate = nil
        }

        let knownIdentifiers = Array(peripheralsFound.keys)
        let knownPeripherals = centralManager?.retrievePeripherals(withIdentifiers: knownIdentifiers)

        peripheralsFound.removeAll()

        if let knownPeripherals = knownPeripherals {
            for peripheral in knownPeripherals {
                print("Adding prediscovered peripheral: \(peripheral.name ?? peripheral.identifier.uuidString)")
                discovered(peripheral: peripheral)
            }
        }

        peripheralsFoundLock.unlock()

        // Restore central manager delegate if was changed
        centralManager?.delegate = self

        if isScanning {
            startScan()
        }
    }

    // MARK: - Scan
    public func startScan(withServices services: [CBUUID]? = nil) {
        centralManagerPoweredOnSemaphore.wait()
        centralManagerPoweredOnSemaphore.signal()

        isScanningWaitingToStart = true
        guard let centralManager = centralManager, centralManager.state != .poweredOff && centralManager.state != .unauthorized && centralManager.state != .unsupported else {
            print("startScan failed because central manager is not ready")
            return
        }

        scanningServicesFilter = services

        guard centralManager.state == .poweredOn else {
            print("startScan failed because central manager is not powered on")
            return
        }

        // print("start scan")
        scanningStartTime = CACurrentMediaTime()
        NotificationCenter.default.post(name: .didStartScanning, object: nil)

        let options = BleManager.kAlwaysAllowDuplicateKeys ? [CBCentralManagerScanOptionAllowDuplicatesKey: true] : nil
        centralManager.scanForPeripherals(withServices: services, options: options)
        isScanningWaitingToStart = false
    }

    public func stopScan() {
        // print("stop scan")
        centralManager?.stopScan()
        scanningStartTime = nil
        isScanningWaitingToStart = false
        NotificationCenter.default.post(name: .didStopScanning, object: nil)
    }

    public func numPeripherals() -> Int {
        return peripheralsFound.count
    }

    public func peripherals() -> [BlePeripheral] {
        peripheralsFoundLock.lock(); defer { peripheralsFoundLock.unlock() }
        return Array(peripheralsFound.values)
    }

    public func peripheralsSortedByFirstDiscovery() -> [BlePeripheral] {
        let now = Date()
        var peripheralsList = peripherals()
        peripheralsList.sort { (p0, p1) -> Bool in
            peripheralsFoundFirstTime[p0.identifier] ?? now < peripheralsFoundFirstTime[p1.identifier] ?? now
        }

        return peripheralsList
    }

    public func peripheralsSortedByRSSI() -> [BlePeripheral] {
        var peripheralsList = peripherals()
        peripheralsList.sort { (p0, p1) -> Bool in
            return (p0.rssi ?? -127) > (p1.rssi ?? -127)
        }

        return peripheralsList
    }

    public func connectedPeripherals() -> [BlePeripheral] {
        return peripherals().filter {$0.state == .connected}
    }

    public func connectingPeripherals() -> [BlePeripheral] {
        return peripherals().filter {$0.state == .connecting}
    }

    public func connectedOrConnectingPeripherals() -> [BlePeripheral] {
        return peripherals().filter {$0.state == .connected || $0.state == .connecting}
    }

    public func refreshPeripherals() {
        stopScan()

        peripheralsFoundLock.lock()
        // Don't remove connnected or connecting peripherals
        for (identifier, peripheral) in peripheralsFound {
            if peripheral.state != .connected && peripheral.state != .connecting {
                peripheralsFound.removeValue(forKey: identifier)
                peripheralsFoundFirstTime.removeValue(forKey: identifier)
            }
        }
        peripheralsFoundLock.unlock()

        //
        NotificationCenter.default.post(name: .didUnDiscoverPeripheral, object: nil)
        startScan(withServices: scanningServicesFilter)
    }

    // MARK: - Connection Management
    public func connect(to peripheral: BlePeripheral, timeout: TimeInterval? = nil, shouldNotifyOnConnection: Bool = false, shouldNotifyOnDisconnection: Bool = false, shouldNotifyOnNotification: Bool = false) {

        centralManagerPoweredOnSemaphore.wait()
        centralManagerPoweredOnSemaphore.signal()

        // Stop scanning when connecting to a peripheral
        if BleManager.kStopScanningWhenConnectingToPeripheral {
            stopScan()
        }

        // Connect
        NotificationCenter.default.post(name: .willConnectToPeripheral, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheral.identifier])

        //print("connect")
        var options: [String: Bool]?

        #if os(OSX)
        #else
        if shouldNotifyOnConnection || shouldNotifyOnDisconnection || shouldNotifyOnNotification {
            options = [CBConnectPeripheralOptionNotifyOnConnectionKey: shouldNotifyOnConnection, CBConnectPeripheralOptionNotifyOnDisconnectionKey: shouldNotifyOnDisconnection, CBConnectPeripheralOptionNotifyOnNotificationKey: shouldNotifyOnNotification]
        }
        #endif

        if let timeout = timeout {
            self.connectionTimeoutTimers[peripheral.identifier] = Timer.scheduledTimer(timeInterval: timeout, target: self, selector: #selector(self.connectionTimeoutFired), userInfo: peripheral.identifier, repeats: false)
        }
        centralManager?.connect(peripheral.peripheral, options: options)
    }

    @objc private func connectionTimeoutFired(timer: Foundation.Timer) {
        let peripheralIdentifier = timer.userInfo as! UUID
        print("connection timeout for: \(peripheralIdentifier)")
        connectionTimeoutTimers[peripheralIdentifier] = nil

        NotificationCenter.default.post(name: .willDisconnectFromPeripheral, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheralIdentifier])

        if let blePeripheral = peripheralsFound[peripheralIdentifier] {
            centralManager?.cancelPeripheralConnection(blePeripheral.peripheral)
        } else {
            //print("simulate disconnection")
            // The blePeripheral is available on peripheralsFound, so simulate the disconnection
            NotificationCenter.default.post(name: .didDisconnectFromPeripheral, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheralIdentifier])
        }
    }

    public func disconnect(from peripheral: BlePeripheral, waitForQueuedCommands: Bool = false) {
        guard let centralManager = centralManager else { return}

        print("disconnect: \(peripheral.identifier)")
        NotificationCenter.default.post(name: .willDisconnectFromPeripheral, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheral.identifier])

        if waitForQueuedCommands {
            // Send the disconnection to the command queue, so all the previous command are executed before disconnecting
            peripheral.disconnect(centralManager: centralManager)
        } else {
            centralManager.cancelPeripheralConnection(peripheral.peripheral)
        }
    }

    func reconnecToPeripherals(peripheralUUIDs identifiers: [UUID], withServices services: [CBUUID], timeout: Double? = nil) -> Bool {
        guard let centralManager = centralManager else { return false }
        var reconnecting = false

        // Reconnect to a known identifier
        if !identifiers.isEmpty {
            let peripheralsWithIdentifiers = centralManager.retrievePeripherals(withIdentifiers: identifiers)
            for peripheral in peripheralsWithIdentifiers {
                print("Try to connect to known peripheral: \(peripheral.identifier)")
                discovered(peripheral: peripheral, advertisementData: nil)
                if let blePeripheral = peripheralsFound[peripheral.identifier] {
                    connect(to: blePeripheral, timeout: timeout)
                    reconnecting = true
                }
            }
        }

        // Reconnect even if no identifier was saved if we are already connected to a device with the expected services
        let peripheralsWithServices = centralManager.retrieveConnectedPeripherals(withServices: services)
        if !peripheralsWithServices.isEmpty {
            let alreadyConnectingOrConnectedPeripheralsIds = BleManager.shared.connectedOrConnectingPeripherals().map{$0.identifier}
            for peripheral in peripheralsWithServices {
                if !alreadyConnectingOrConnectedPeripheralsIds.contains(peripheral.identifier) {
                    print("Connect to peripheral with known service: \(peripheral.identifier)")
                    discovered(peripheral: peripheral, advertisementData: nil )
                    if let blePeripheral = peripheralsFound[peripheral.identifier] {
                        connect(to: blePeripheral, timeout: timeout)
                        reconnecting = true
                    }
                }
            }
        }

        return reconnecting
    }

    private func discovered(peripheral: CBPeripheral, advertisementData: [String: Any]? = nil, rssi: Int? = nil) {
        peripheralsFoundLock.lock(); defer { peripheralsFoundLock.unlock() }

        if let existingPeripheral = peripheralsFound[peripheral.identifier] {
            existingPeripheral.lastSeenTime = CFAbsoluteTimeGetCurrent()

            if let rssi = rssi, rssi != BlePeripheral.kUndefinedRssiValue {     // only update rssi value if is defined ( 127 means undefined )
                existingPeripheral.rssi = rssi
            }

            if let advertisementData = advertisementData {
                for (key, value) in advertisementData {
                    existingPeripheral.advertisement.advertisementData.updateValue(value, forKey: key)
                }
            }
            peripheralsFound[peripheral.identifier] = existingPeripheral
        } else {      // New peripheral found
            let blePeripheral = BlePeripheral(peripheral: peripheral, advertisementData: advertisementData, rssi: rssi)
            peripheralsFound[peripheral.identifier] = blePeripheral
            peripheralsFoundFirstTime[peripheral.identifier] = Date()
        }
    }


    // MARK: - Notifications
    public func peripheralUUID(from notification: Notification) -> UUID? {
        return notification.userInfo?[NotificationUserInfoKey.uuid.rawValue] as? UUID
    }

    public func peripheral(from notification: Notification) -> BlePeripheral? {
        guard let uuid = peripheralUUID(from: notification) else { return nil }

        return peripheral(with: uuid)
    }

    public func error(from notification: Notification) -> Error? {
        return notification.userInfo?[NotificationUserInfoKey.error.rawValue] as? Error
    }

    public func peripheral(with uuid: UUID) -> BlePeripheral? {
        return peripheralsFound[uuid]
    }
}

// MARK: - CBCentralManagerDelegate
extension BleManager: CBCentralManagerDelegate {
    public func centralManagerDidUpdateState(_ central: CBCentralManager) {

        print("centralManagerDidUpdateState: \(central.state.rawValue)")
        // Unlock state lock if we have a known state
        if central.state == .poweredOn || central.state == .poweredOff || central.state == .unsupported || central.state == .unauthorized {
            centralManagerPoweredOnSemaphore.signal()
        }

        // Scanning
        if central.state == .poweredOn {
            if isScanningWaitingToStart {
                startScan(withServices: scanningServicesFilter)        // Continue scanning now that bluetooth is back
            }
        } else {
            if isScanning {
                isScanningWaitingToStart = true
            }
            scanningStartTime = nil

            // Remove all peripherals found (Important because the BlePeripheral queues could contain old commands that were processing when the bluetooth state changed)
            peripheralsFound.removeAll()
        }

        DispatchQueue.main.async {
            NotificationCenter.default.post(name: .didUpdateBleState, object: nil)
        }
    }

    /*
     func centralManager(_ central: CBCentralManager, willRestoreState dict: [String : Any]) {

     }*/

    public func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        // print("didDiscover: \(peripheral.name ?? peripheral.identifier.uuidString)")
        let rssi = RSSI.intValue
        DispatchQueue.main.async {      // This Fixes iOS12 race condition on cached filtered peripherals. TODO: investigate
            self.discovered(peripheral: peripheral, advertisementData: advertisementData, rssi: rssi)
            NotificationCenter.default.post(name: .didDiscoverPeripheral, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheral.identifier])
        }
    }

    public func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        print("didConnect: \(peripheral.name ?? peripheral.identifier.uuidString)")

        // Remove connection timeout if exists
        if let timer = connectionTimeoutTimers[peripheral.identifier] {
            timer.invalidate()
            connectionTimeoutTimers[peripheral.identifier] = nil
        }

        // Send notification
        DispatchQueue.main.async {
            NotificationCenter.default.post(name: .didConnectToPeripheral, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheral.identifier])
        }
    }

    public func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        print("didFailToConnect: \(peripheral.name ?? peripheral.identifier.uuidString). \(String(describing: error))")

        // Clean
        peripheralsFound[peripheral.identifier]?.reset()

        // Notify
        DispatchQueue.main.async {
            NotificationCenter.default.post(name: .didDisconnectFromPeripheral, object: nil, userInfo: [
                NotificationUserInfoKey.uuid.rawValue: peripheral.identifier,
                NotificationUserInfoKey.error.rawValue: error as Any
            ])
        }
    }

    public func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        print("didDisconnectPeripheral: \(peripheral.name ?? peripheral.identifier.uuidString)")

        // Clean
        peripheralsFound[peripheral.identifier]?.reset()

        // Notify
        DispatchQueue.main.async {
            NotificationCenter.default.post(name: .didDisconnectFromPeripheral, object: nil, userInfo: [NotificationUserInfoKey.uuid.rawValue: peripheral.identifier])

            // Remove from peripheral list (after sending notification so the receiving objects can query about the peripheral before being removed)
            self.peripheralsFoundLock.lock()
            self.peripheralsFound.removeValue(forKey: peripheral.identifier)
            self.peripheralsFoundLock.unlock()
        }
    }
}

// MARK: - Custom Notifications
extension Notification.Name {
    private static let kPrefix = Bundle.main.bundleIdentifier!
    public static let didUpdateBleState = Notification.Name(kPrefix+".didUpdateBleState")
    public static let didStartScanning = Notification.Name(kPrefix+".didStartScanning")
    public static let didStopScanning = Notification.Name(kPrefix+".didStopScanning")
    public static let didDiscoverPeripheral = Notification.Name(kPrefix+".didDiscoverPeripheral")
    public static let didUnDiscoverPeripheral = Notification.Name(kPrefix+".didUnDiscoverPeripheral")
    public static let willConnectToPeripheral = Notification.Name(kPrefix+".willConnectToPeripheral")
    public static let didConnectToPeripheral = Notification.Name(kPrefix+".didConnectToPeripheral")
    public static let willDisconnectFromPeripheral = Notification.Name(kPrefix+".willDisconnectFromPeripheral")
    public static let didDisconnectFromPeripheral = Notification.Name(kPrefix+".didDisconnectFromPeripheral")
}
