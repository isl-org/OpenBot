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
        titleLabel.text = "OpenBot";
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

    /*
    // MARK: - Navigation
     
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
