//
//  HomePageViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 17/08/22.
//

import UIKit
var isBluetoothConnected = false;

class HomePageViewController: UIViewController {
    @IBOutlet weak var bluetooth: UIButton!
    @IBOutlet weak var titleLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setUpTitle();


    }
    func setUpTitle(){
        titleLabel.text = "OpenBot";
        titleLabel.textColor = UIColor(named: "HomePageTitleColor")
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);
        if (isBluetoothConnected) {
            bluetooth.setImage(UIImage(named: "bluetoothConnected"), for: .normal)
        } else {
            bluetooth.setImage(UIImage(named: "bluetoothDisconnected"), for: .normal)
        }
    }

    @IBAction func tap() {
        isBluetoothConnected = !isBluetoothConnected;
    }
   
    
    @IBAction func settingButton(_ sender: Any) {
        print("setting button clicked")
    }
}
