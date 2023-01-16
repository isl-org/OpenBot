//
//  Bluetoothtable.swift
//  OpenBot
//
//  Created by Nitish Yadav on 18/10/22.
//

import UIKit
import CoreBluetooth

class BluetoothTable: UITableViewController {
    var bluetooth = bluetoothDataController.shared
    var centralManager: CBCentralManager?
    var isconnected: Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()
        self.clearsSelectionOnViewWillAppear = false
//        let timer = Timer.scheduledTimer(withTimeInterval: 30.0, repeats: true) { timer in
//            self.bluetooth.startScan()
//            self.tableView.reloadData()
//        }
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothConnected, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothDisconnected, object: nil)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        bluetooth.sendData(payload: "f\n");
    }

    func connectToBle(Btn: UIButton, index: Int) {
        bluetooth.peri = bluetooth.peripherals[index]
        isBluetoothConnected = true
        bluetooth.connect()
        bluetooth.centralManager?.stopScan()
    }

    func disconnectToBle() {
        bluetooth.disconnect()
        isBluetoothConnected = false
        tableView.reloadData()
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return bluetooth.peripherals.count
    }

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        super.tableView(tableView, heightForRowAt: indexPath)
        return 44
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "myCell", for: indexPath) as! BluetoothTableViewCell
        let peripheral = bluetooth.peripherals[indexPath.row]
        cell.textLabel?.text = peripheral.name
        cell.connectButton.addTarget(self, action: #selector(connect(sender:)), for: .touchUpInside)
        cell.connectButton.tag = indexPath.row
        switch peripheral.state {
        case .disconnected:
            cell.connectButton.setTitle(Strings.connect, for: .normal)
            cell.textLabel?.textColor = Colors.border
            cell.connectButton.setTitleColor(Colors.border, for: .normal)
        case .connected:
            cell.connectButton.setTitle(Strings.disconnect, for: .normal)
            cell.textLabel?.textColor = Colors.title
            cell.connectButton.setTitleColor(UIColor.red, for: .normal)
        case .connecting:
            cell.connectButton.setTitle(Strings.connecting, for: .normal)
            cell.connectButton.setTitleColor(UIColor.green, for: .normal)
        case .disconnecting:
            cell.connectButton.setTitle(Strings.disconnecting, for: .normal)
        @unknown default:
            cell.connectButton.setTitle(Strings.connect, for: .normal)
        }
        return cell
    }

    @objc func updateConnect(_ notification: Notification) {
//        print(bluetooth.peripherals)
        tableView.reloadData()
    }

    @objc func connect(sender: UIButton) {

        if isBluetoothConnected {
            disconnectToBle();
            if sender.title(for: .normal) == "Connect" {
                connectToBle(Btn: sender, index: sender.tag)
            }
        } else {
            connectToBle(Btn: sender, index: sender.tag)
        }
        tableView.reloadData()
    }
}

class BluetoothTableViewCell: UITableViewCell {
    @IBOutlet weak var connectButton: UIButton!
}
