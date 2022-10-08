//
//  HomePageViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 17/08/22.
//

import UIKit
import CoreBluetooth

let height = max(UIScreen.main.bounds.height, UIScreen.main.bounds.width)
let width = min(UIScreen.main.bounds.height, UIScreen.main.bounds.width)
var currentOrientation: UIInterfaceOrientation = UIInterfaceOrientation.portrait
var isBluetoothConnected = false;
var viewControllerName: String?
var leadingConstraint = NSLayoutConstraint()

class HomePageViewController: UIViewController {
    @IBOutlet weak var bluetooth: UIButton!
    @IBOutlet weak var settings: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet var modesCollectionView: UICollectionView!;

    override func viewDidLoad() {
        super.viewDidLoad()

        bluetoothDataController.shared.startScan()
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        setUpTitle();
        let layout = UICollectionViewFlowLayout();
        layout.collectionView?.layer.shadowColor = Colors.gridShadowColor?.cgColor
        layout.collectionView?.layer.shadowOpacity = 1
        if currentOrientation == .portrait {
            layout.itemSize = resized(size: CGSize(width: width * 0.42, height: width * 0.42), basedOn: dimension)
        } else {
            layout.itemSize = resized(size: CGSize(width: width * 0.42, height: width * 0.42), basedOn: dimension)
        }

        layout.minimumInteritemSpacing = 5
        layout.sectionInset = UIEdgeInsets(top: 0, left: 8, bottom: 0, right: 8)
        layout.minimumLineSpacing = 30
        modesCollectionView.collectionViewLayout = layout;

        modesCollectionView.register(modesCollectionViewCell.nib(), forCellWithReuseIdentifier: modesCollectionViewCell.identifier)
        modesCollectionView.delegate = self
        modesCollectionView.dataSource = self
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        changeNavigationColor()
        DataLogger.shared.deleteFiles(path: Strings.forwardSlash + DataLogger.shared.getBaseDirectoryName())


    }

    func changeNavigationColor() {
        if traitCollection.userInterfaceStyle == .light {
            navigationController?.navigationBar.tintColor = Colors.title
        } else {
            navigationController?.navigationBar.tintColor = UIColor.white

        }
    }


    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        DeviceCurrentOrientation.shared.findDeviceOrientation()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        DeviceCurrentOrientation.shared.findDeviceOrientation()
        viewControllerName = classNameFrom(self)
        if (isBluetoothConnected) {
            bluetooth.setImage(Images.bluetoothConnected, for: .normal)
        } else {
            bluetooth.setImage(Images.bluetoothDisconnected, for: .normal)
        }



    }


    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
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
        cell.layer.cornerRadius = adapted(dimensionSize: 10, to: .height)
        cell.translatesAutoresizingMaskIntoConstraints = true
        leadingConstraint = cell.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 30)
        return cell
    }

    func arrowConstraints() {

    }

    func classNameFrom(_ viewController: UIViewController) -> String {
        let currentViewControllerName = NSStringFromClass(viewController.classForCoder).components(separatedBy: ".").last!
        return currentViewControllerName

    }



}


extension UIBarButtonItem {
    convenience init(image :UIImage, title :String, target: Any?, action: Selector?) {
        let button = UIButton(type: .custom)
        button.setInsets(forContentPadding: UIEdgeInsets(top: 0.0, left: 0.0, bottom: 0.0, right: 0.0), imageTitlePadding: 10)
        button.setImage(image, for: .normal)
        button.imageView?.layer.transform = CATransform3DMakeScale(1.3, 1.3, 1.3)
        button.setTitle(title, for: .normal)
        button.frame = CGRect(x: 0, y: 0, width: image.size.width + 10, height: image.size.height)
        if let target = target, let action = action {
            button.addTarget(target, action: action, for: .touchUpInside)
        }
        self.init(customView: button)
    }
}


extension UIButton {
    func setInsets(
            forContentPadding contentPadding: UIEdgeInsets,
            imageTitlePadding: CGFloat
    ) {
        self.contentEdgeInsets = UIEdgeInsets(
                top: contentPadding.top,
                left: contentPadding.left,
                bottom: contentPadding.bottom,
                right: contentPadding.right + imageTitlePadding
        )
        self.titleEdgeInsets = UIEdgeInsets(
                top: 0,
                left: imageTitlePadding,
                bottom: 0,
                right: -imageTitlePadding
        )
    }
}
