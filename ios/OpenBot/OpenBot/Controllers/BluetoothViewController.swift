//
//  BluetoothViewController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 22/07/22.
//
//
import UIKit
import CoreBluetooth

class BluetoothViewController: UIViewController{
    var bluetooth = bluetoothDataController.shared
    var centralManager: CBCentralManager?
    @IBOutlet weak var myTable: UITableView!
    var isconnected: Bool = false
    override func viewDidLoad() {
        super.viewDidLoad()
        print(bluetooth.peripherals)
        myTable.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        myTable.reloadData()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        myTable.reloadData()
    }
}



extension BluetoothViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        print("hello ", bluetooth.peripherals.count)
        return bluetooth.peripherals.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        let cell = myTable.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        print(indexPath.row)
        let peripheral = bluetooth.peripherals[indexPath.row]
        cell.textLabel?.text = peripheral.name
        let button = UIButton();
        button.backgroundColor = .systemBackground
        cell.addSubview(button)
        button.frame = CGRect(x: cell.frame.origin.x + 225, y: cell.frame.origin.y, width: 150, height: cell.frame.size.height)
        button.layer.cornerRadius = 25
        button.tag = indexPath.row
        if isBluetoothConnected && bluetooth.peri?.name != cell.textLabel?.text {
            button.isHidden = true
        } else if isBluetoothConnected && bluetooth.peri?.name == cell.textLabel?.text {
            button.setTitle("Disconnect", for: .normal)
            button.addTarget(self, action: #selector(disConnectToBle), for: .touchUpInside)
        } else {
            button.setTitle("Connect", for: .normal)
            button.addTarget(self, action: #selector(connectToBle), for: .touchUpInside)
        }
        return cell;

    }

    @objc func connectToBle(sender: UIButton) {
        print("selection is :", sender.tag)
        bluetooth.peri = bluetooth.peripherals[sender.tag]
        isBluetoothConnected = true;
        bluetooth.connect()
        let dataSend = (storyboard?.instantiateViewController(withIdentifier: "homescreen"))!
        guard (navigationController?.pushViewController(dataSend, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")

        }
    }
    @objc func disConnectToBle() {
        bluetooth.disconnect()
        isBluetoothConnected = false
        bluetooth.startScan()
        viewDidLoad()

    }
}