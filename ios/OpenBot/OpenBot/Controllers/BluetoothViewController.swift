//
//  BluetoothViewController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 22/07/22.
//
//
import UIKit
import CoreBluetooth
var peri: CBPeripheral?
var peripherals = Array<CBPeripheral>()
class BluetoothViewController: UIViewController, CBCentralManagerDelegate {
    var centralManager: CBCentralManager?
    @IBOutlet weak var myTable: UITableView!
    var isconnected: Bool = false
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        if (central.state == .poweredOn) {
            self.centralManager?.scanForPeripherals(withServices: nil, options: nil)
        } else {
// do something like alert the user that ble is not on
        }
    }


    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        if peripheral.name != nil {
            peripherals.append(peripheral)
            myTable.reloadData()
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        if !isBluetoothConnected {
            centralManager = CBCentralManager(delegate: self, queue: DispatchQueue.main)
        }
        self.myTable.register(UITableViewCell.self, forCellReuseIdentifier: "cell")

    }
}


extension BluetoothViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return peripherals.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//            centralManager?.stopScan()
        let cell = myTable.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        print(indexPath.row)
        print(peripherals[indexPath.row])
        let peripheral = peripherals[indexPath.row]
        cell.textLabel?.text = peripheral.name
        let button = UIButton();
        button.backgroundColor = .systemBackground
        cell.addSubview(button)
        button.frame = CGRect(x: cell.frame.origin.x + 225, y: cell.frame.origin.y, width: 150, height: cell.frame.size.height)
        button.layer.cornerRadius = 25
        button.tag = indexPath.row
        if isBluetoothConnected && peri?.name != cell.textLabel?.text {
            button.isHidden = true
        } else if isBluetoothConnected && peri?.name == cell.textLabel?.text {
            button.setTitle("Disconnect", for: .normal)
            button.addTarget(self, action: #selector(disConnectToBle), for: .touchUpInside)
        } else {
            button.setTitle("Connect", for: .normal)
            button.addTarget(self, action: #selector(connectToBle), for: .touchUpInside)
        }
        return cell;

    }


    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        peri = peripherals[indexPath.row]
//        centralManager?.stopScan()
////      centralManager?.connect(peri!)
//        isBluetoothConnected = true;
//        let dataSend = (self.storyboard?.instantiateViewController(withIdentifier: "homescreen"))!
//        guard let controller = self.navigationController?.pushViewController(dataSend, animated: true) else {
//            fatalError("guard failure handling has not been implemented")
//
//        }
    }
    @objc func connectToBle(sender: UIButton) {
        print("selection is :", sender.tag)
        peri = peripherals[sender.tag]
        print("connected to ", peri)
        centralManager?.stopScan()
        isBluetoothConnected = true;
        let dataSend = (self.storyboard?.instantiateViewController(withIdentifier: "homescreen"))!
        guard let controller = self.navigationController?.pushViewController(dataSend, animated: true) else {
            fatalError("guard failure handling has not been implemented")

        }
    }
    @objc func disConnectToBle() {
        print("disconnected from ", peri)
        centralManager?.cancelPeripheralConnection(peri!)
        bluetoothDataController.shared.disconnect()
        isBluetoothConnected = false
        peripherals.removeAll();
        peri = nil
        viewDidLoad()
    }
}