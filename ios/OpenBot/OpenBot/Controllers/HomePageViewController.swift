//
//  HomePageViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 17/08/22.
//

import UIKit
import CoreBluetooth
let height = max(UIScreen.main.bounds.height,UIScreen.main.bounds.width)
let width = min(UIScreen.main.bounds.height,UIScreen.main.bounds.width)
var currentOrientation = DeviceCurrentOrientation.Orientation.portrait
var isBluetoothConnected = false;
var viewControllerName: String?

class HomePageViewController: UIViewController {
    @IBOutlet weak var bluetooth: UIButton!
    @IBOutlet weak var settings: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet var modesCollectionView: UICollectionView!;

    override func viewDidLoad() {
        super.viewDidLoad()
        setUpTitle();
        let layout = UICollectionViewFlowLayout();
        layout.collectionView?.layer.cornerRadius = 10;
        layout.collectionView?.layer.shadowColor = Colors.gridShadowColor?.cgColor
        layout.collectionView?.layer.shadowOpacity = 1
        layout.itemSize = CGSize(width: 190, height: 190);
        modesCollectionView.collectionViewLayout = layout;
        modesCollectionView.register(modesCollectionViewCell.nib(), forCellWithReuseIdentifier: modesCollectionViewCell.identifier)
        modesCollectionView.delegate = self
        modesCollectionView.dataSource = self
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);

        viewControllerName = classNameFrom(self)
        if (isBluetoothConnected) {
            bluetooth.setImage(Images.bluetoothConnected, for: .normal)
        } else {
            bluetooth.setImage(Images.bluetoothDisconnected, for: .normal)
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
        titleLabel.text = Strings.OpenBot;
        titleLabel.textColor = Colors.title;
    }

    @IBAction func onTapSettings() {
        if isBluetoothConnected != false {
            let openDataSerialView = (storyboard?.instantiateViewController(withIdentifier: Strings.ScreenDataCollection))!
            guard (navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
                fatalError("guard failure handling has not been implemented")
            }
        } else {
//            let yourAlert = UIAlertController(title: "Connection Error", message: "Please connect to BlueTooth", preferredStyle: UIAlertController.Style.alert)
//            yourAlert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: { (handler) in}))
//            self.present(yourAlert, animated: true, completion: nil)
            let openDataSerialView = (storyboard?.instantiateViewController(withIdentifier: "sensorScreen"))!
            guard (navigationController?.pushViewController(openDataSerialView, animated: true)) != nil else {
                fatalError("guard failure handling has not been implemented")
            }
        }
    }
}

extension UIViewController: UICollectionViewDelegate {
    public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
//        if isBluetoothConnected {
            collectionView.deselectItem(at: indexPath, animated: true)
            let viewController = (storyboard?.instantiateViewController(withIdentifier: Constants.gameModes[indexPath.row].identifier))!
            guard (navigationController?.pushViewController(viewController, animated: true)) != nil else {
                fatalError("guard failure handling has not been implemented")
            }
//        } else {
//            let yourAlert = UIAlertController(title: "Connection Error", message: "Please connect to BlueTooth", preferredStyle: UIAlertController.Style.alert)
//            yourAlert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: { (handler) in }))
//            present(yourAlert, animated: true, completion: nil)
//        }

    }
}

extension UIViewController: UICollectionViewDataSource {
    public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        Constants.gameModes.count;
    }

    public func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: modesCollectionViewCell.identifier, for: indexPath) as! modesCollectionViewCell;
        cell.configure(with: Constants.gameModes[indexPath.row]);
        return cell
    }
}

func classNameFrom(_ viewController: UIViewController) -> String {
    let currentViewControllerName = NSStringFromClass(viewController.classForCoder).components(separatedBy: ".").last!
    return currentViewControllerName

}

extension UIViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        CGSize(width: 190, height: 190)
    }
}