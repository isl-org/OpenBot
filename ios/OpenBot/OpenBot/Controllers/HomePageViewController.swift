//
//  HomePageViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 17/08/22.
//
import UIKit
import CoreBluetooth
var isBluetoothConnected = false;
class HomePageViewController: UIViewController {
    @IBOutlet weak var bluetooth: UIButton!
    @IBOutlet weak var settings: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet var modesCollectionView: UICollectionView!;
    public var gridItems = [ModeItem(label: "Free Roam", icon: "freeRoam", identifier: "freeRoam"), ModeItem(label: "Data Collection", icon: "dataCollection", identifier: "dataSerialMonitor"), ModeItem(label: "Controller Mapping", icon: "controllerMapping", identifier: "controllerMapping")];
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpTitle();
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
        if (isBluetoothConnected) {
            bluetooth.setImage(UIImage(named: "bluetoothConnected"), for: .normal)
        } else {
            bluetooth.setImage(UIImage(named: "bluetoothDisconnected"), for: .normal)
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
            let yourAlert = UIAlertController(title: "Connection Error", message: "Please connect to BlueTooth", preferredStyle: UIAlertController.Style.alert)
            yourAlert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: { (handler) in}))
            self.present(yourAlert, animated: true, completion: nil)
//            let openDataSerialView = (self.storyboard?.instantiateViewController(withIdentifier: "sensorScreen"))!
//            guard (self.navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
//                fatalError("guard failure handling has not been implemented")
//            }

        }
    }
}
extension UIViewController: UICollectionViewDelegate {
    public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
        let viewController = (storyboard?.instantiateViewController(withIdentifier: HomePageViewController().gridItems[indexPath.row].identifier))!
        guard (navigationController?.pushViewController(viewController, animated: true)) != nil else {
            fatalError("guard failure handling has not been implemented")
        }
    }
}
extension UIViewController: UICollectionViewDataSource {
    public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        HomePageViewController().gridItems.count;
    }

    public func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: modesCollectionViewCell.identifier, for: indexPath) as! modesCollectionViewCell;
        cell.configure(with: HomePageViewController().gridItems[indexPath.row]);
        return cell
    }
}
extension UIViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
         CGSize(width: 190, height: 190)
    }
}