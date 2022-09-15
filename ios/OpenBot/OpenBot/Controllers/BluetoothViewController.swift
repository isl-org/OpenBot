//
//  BluetoothViewController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 22/07/22.
//
//

import UIKit
import CoreBluetooth

class BluetoothViewController: UIViewController {
    var bluetooth = bluetoothDataController.shared
    var centralManager: CBCentralManager?

    let myTable = UITableView()
    var isconnected: Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()
        setupTableView()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        bluetooth.startScan()


    }

    func setupTableView() {

        myTable.register(UITableViewCell.self, forCellReuseIdentifier: "cellId")
//        myTable.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
        myTable.translatesAutoresizingMaskIntoConstraints = false
        myTable.delegate = self
        myTable.dataSource = self
        view.addSubview(myTable)
        addConstraints()

    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        refreshConstraints()
        myTable.reloadData()
    }

    func addConstraints() {
        let topAnchor = myTable.topAnchor.constraint(equalTo: view.topAnchor, constant: 10)
        if currentOrientation == .portrait {
            let widthOfTable = myTable.widthAnchor.constraint(equalToConstant: width)
            widthOfTable.identifier = "width"
            let heightOfTable = myTable.heightAnchor.constraint(equalToConstant: height)
            heightOfTable.identifier = "height"
            NSLayoutConstraint.activate([
                widthOfTable, heightOfTable, topAnchor
            ])
        } else {
            let widthOfTable = myTable.widthAnchor.constraint(equalToConstant: height)
            widthOfTable.identifier = "width"
            let heightOfTable = myTable.heightAnchor.constraint(equalToConstant: width)
            heightOfTable.identifier = "height"
            NSLayoutConstraint.activate([
                widthOfTable, heightOfTable, topAnchor
            ])
        }


    }

    func refreshConstraints() {
        if UIDevice.current.orientation == .portrait {
            for constraint in myTable.constraints {
                if constraint.identifier == "width" {
                    constraint.constant = width
                } else if constraint.identifier == "height" {
                    constraint.constant = height
                }
            }
        } else {
            for constraint in myTable.constraints {
                if constraint.identifier == "width" {
                    constraint.constant = height
                } else if constraint.identifier == "height" {
                    constraint.constant = width
                }
            }
        }

    }

}


extension BluetoothViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        bluetooth.peripherals.count

    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        44
    }


    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = myTable.dequeueReusableCell(withIdentifier: "cellId", for: indexPath)
        let peripheral = bluetooth.peripherals[indexPath.row]
        let button = UIButton();
        cell.textLabel?.text = peripheral.name
        button.backgroundColor = .systemBackground
        cell.addSubview(button)
        button.frame.size = CGSize(width: 150, height: cell.frame.size.height)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.trailingAnchor.constraint(equalTo: myTable.safeAreaLayoutGuide.trailingAnchor, constant: -10).isActive = true
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
        bluetooth.peri = bluetooth.peripherals[sender.tag]
        isBluetoothConnected = true
        bluetooth.connect()
        let dataSend = (storyboard?.instantiateViewController(withIdentifier: "homescreen"))!
        guard (navigationController?.pushViewController(dataSend, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }
    }

    @objc func disConnectToBle() {
        bluetooth.disconnect()
        bluetooth.startScan()
        isBluetoothConnected = false
        myTable.reloadData()
    }


}


//

//
//extension BluetoothViewController: UITableViewDataSource, UITableViewDelegate {
//    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        bluetooth.peripherals.count
//    }
//
//    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let cell = myTable.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
//        let peripheral = bluetooth.peripherals[indexPath.row]
//        cell.textLabel?.text = peripheral.name
//        button.backgroundColor = .systemBackground
//        cell.addSubview(button)
//        button.frame = CGRect(x: cell.frame.width - 150, y: cell.frame.origin.y, width: 150, height: cell.frame.size.height)
//        button.layer.cornerRadius = 25
//        button.tag = indexPath.row
//        if isBluetoothConnected && bluetooth.peri?.name != cell.textLabel?.text {
//            button.isHidden = true
//        } else if isBluetoothConnected && bluetooth.peri?.name == cell.textLabel?.text {
//            button.setTitle("Disconnect", for: .normal)
//            button.addTarget(self, action: #selector(disConnectToBle), for: .touchUpInside)
//        } else {
//            button.setTitle("Connect", for: .normal)
//            button.addTarget(self, action: #selector(connectToBle), for: .touchUpInside)
//        }
//        return cell;
//    }
//
//
//
//    @objc func connectToBle(sender: UIButton) {
//        bluetooth.peri = bluetooth.peripherals[sender.tag]
//        isBluetoothConnected = true
//        bluetooth.connect()
//        let dataSend = (storyboard?.instantiateViewController(withIdentifier: "homescreen"))!
//        guard (navigationController?.pushViewController(dataSend, animated: true)) != nil else {
//            fatalError("guard failure handling has not been implemented")
//        }
//    }
//
//    @objc func disConnectToBle() {
//        bluetooth.disconnect()
//        bluetooth.startScan()
//        isBluetoothConnected = false
//        viewDidLoad()
//    }
//}
