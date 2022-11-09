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
    var connectDisconnect : UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
         self.clearsSelectionOnViewWillAppear = false
        let timer = Timer.scheduledTimer(withTimeInterval: 30.0, repeats: true) { timer in
            self.bluetooth.startScan()
            self.tableView.reloadData()
        }


        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothConnected, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(updateConnect), name: .bluetoothDisconnected, object: nil)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }

    @IBAction func connectDisconnectBtn(_ sender: Any) {
        let buttonPosition = (sender as AnyObject).convert(CGPoint(), to:tableView)
        let indexPath = tableView.indexPathForRow(at:buttonPosition)
        let btn = sender as! UIButton
        connectDisconnect = btn
        isBluetoothConnected ? disconnectToBle() : connectToBle(Btn: btn, index: indexPath?.row ?? 0);
    }

    func connectToBle(Btn : UIButton,index : Int){
        bluetooth.peri = bluetooth.peripherals[index]
        isBluetoothConnected = true
        bluetooth.connect()

    }

    func disconnectToBle(){
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "myCell", for: indexPath)
        let peripheral = bluetooth.peripherals[indexPath.row]
        cell.textLabel?.text = peripheral.name
        return cell
    }

    @objc func updateConnect(_ notification: Notification) {
            isBluetoothConnected ? connectDisconnect.setTitle("Disconnect", for: .normal) : connectDisconnect.setTitle("Connect", for: .normal)

    }

}
