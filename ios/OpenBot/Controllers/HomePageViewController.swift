//
//  HomePageViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 17/08/22.
//

import UIKit
import CoreBluetooth

var isBluetoothConnected = false;

public struct GridItem {
    var label: String;
    var icon: UIImage?;
}

class HomePageViewController: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate {
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        self.centralManager?.scanForPeripherals(withServices: nil, options: nil)
    }

    var centralManager: CBCentralManager?
    var tempPeripheral: CBPeripheral!
    private var allservices: [CBService]?
    var writeCharacteristics: CBCharacteristic?
    @IBOutlet weak var bluetooth: UIButton!
    @IBOutlet weak var settings: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet var modesCollectionView: UICollectionView!;
    
 

    public var gridItems = [GridItem(label: "Free Roam", icon: UIImage(named: "freeRoam")), GridItem(label: "Data Collection", icon: UIImage(named: "dataCollection")), GridItem(label: "Controller Mapping", icon: UIImage(named: "controllerMapping"))];
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpTitle();
        if peri != nil {
            centralManager = CBCentralManager(delegate: self, queue: nil)
        }
        let layout = UICollectionViewFlowLayout();
        layout.collectionView?.layer.cornerRadius = 10;
        layout.collectionView?.layer.shadowColor = UIColor(named: "gridItemShadowColor")?.cgColor
        layout.collectionView?.layer.shadowOpacity = 1
        layout.itemSize = CGSize(width: 190, height: 190);
        
        modesCollectionView.collectionViewLayout = layout;
        modesCollectionView.register(modesCollectionViewCell.nib(), forCellWithReuseIdentifier: modesCollectionViewCell.identifier)
        modesCollectionView.delegate = self
        modesCollectionView.dataSource = self
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
//        content.layer.cornerRadius = 10
//        content.layer.shadowColor = UIColor(named: "gridItemShadowColor")?.cgColor
//        content.layer.shadowOpacity = 1
//        content.layer.shadowOffset = CGSize(width: 0, height: 4)
//        content.layer.shadowRadius = 14 / 2
        if (isBluetoothConnected) {
            bluetooth.setImage(UIImage(named: "bluetoothConnected"), for: .normal)
        } else {
            bluetooth.setImage(UIImage(named: "bluetoothDisconnected"), for: .normal)
        }
    }

    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        if peripheral.name == peri?.name {
            centralManager?.stopScan()
            self.tempPeripheral = peripheral
            peripheral.delegate = self
            centralManager?.connect(peripheral)
        }
    }

    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {

        print(peripheral)
        peripheral.discoverServices(nil)
    }

    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        print("connection was failed")

    }

    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        print("disconnected from :", peripheral)
    }

    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        if let services = peripheral.services {
            print("services are :", services)
            for service in services {
                allservices?.append(service)
//                peripheral.discoverCharacteristics([], for: service)
            }
        }
    }
    func setUpTitle() {
        titleLabel.text = "OpenBot";
        titleLabel.textColor = UIColor(named: "HomePageTitleColor")
    }

    @IBAction func onTapSettings() {

        if isBluetoothConnected != false{
        let openDataSerialView = (self.storyboard?.instantiateViewController(withIdentifier: "dataSerialMonitor"))!
        guard (self.navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }
        }
        else{
//            let yourAlert = UIAlertController(title: "Connection Error", message: "Please connect to BlueTooth", preferredStyle: UIAlertController.Style.alert)
//            yourAlert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: { (handler) in}))
//            self.present(yourAlert, animated: true, completion: nil)
            let openDataSerialView = (self.storyboard?.instantiateViewController(withIdentifier: "sensorScreen"))!
            guard (self.navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
                fatalError("guard failure handling has not been implemented")
            }

        }
    }


}

extension UIViewController: UICollectionViewDelegate{
    public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
        let openDataSerialView = (self.storyboard?.instantiateViewController(withIdentifier: "dataSerialMonitor"))!
        guard (self.navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }
        print(indexPath);
    }
}
extension UIViewController: UICollectionViewDataSource{
    public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return HomePageViewController().gridItems.count;
    }
    
    public func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: modesCollectionViewCell.identifier, for: indexPath) as! modesCollectionViewCell;
        cell.configure(with: HomePageViewController().gridItems[indexPath.row]);
        return cell
    }
}
extension UIViewController: UICollectionViewDelegateFlowLayout{
    func collectionView(_collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath)->
    CGSize{
        return CGSize(width: 190, height: 190)
    }
}


