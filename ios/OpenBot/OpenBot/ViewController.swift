//
//  ViewController.swift
//  OpenBot
//
//  Created by Sparsh Jain on 22/07/22.
//
//
import UIKit
import CoreBluetooth
var peri : CBPeripheral?

class ViewController: UIViewController, CBCentralManagerDelegate {
    var centralManager: CBCentralManager?
    var peripherals = Array<CBPeripheral>()
    @IBOutlet weak var myTable: UITableView!
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        if (central.state == .poweredOn){
            self.centralManager?.scanForPeripherals(withServices: nil, options: nil)
        }
        else {
// do something like alert the user that ble is not on
        print("please on bluetoooth")
        }
    }

    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
        peripherals.append(peripheral)
        myTable.reloadData()
    }
    override func viewDidLoad() {
    super.viewDidLoad()
    // Do any additional setup after loading the view.
//        self.myTable.delegate = self

        self.myTable.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        centralManager = CBCentralManager(delegate: self, queue: DispatchQueue.main)
    }
}
extension ViewController : UITableViewDataSource ,UITableViewDelegate{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return peripherals.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell =  myTable.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        let peripheral = peripherals[indexPath.row]
        if peripheral.name == nil {
            cell.textLabel?.text = "Unknown"
        }
        else {
            cell.textLabel?.text = peripheral.name
        }
        return cell;
    }
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        peri = peripherals[indexPath.row]
        print(indexPath.row)
        centralManager?.stopScan()
        isBluetoothConnected = true;
        let dataSend = (self.storyboard?.instantiateViewController(withIdentifier: "sendScreen"))!
        guard let controller = self.navigationController?.pushViewController(dataSend, animated: true) else {
            fatalError("guard failure handling has not been implemented")
        }
    }
    private func startScan(){
        peripherals.removeAll();
        centralManager = CBCentralManager(delegate: self, queue: DispatchQueue.main)
    }



}
