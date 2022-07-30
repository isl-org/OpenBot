//
//  DataSendViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 30/07/22.
//

import UIKit
import CoreBluetooth
class DataSendViewController: UIViewController {

    override func viewDidLoad() {

        super.viewDidLoad()
        print(peri)
        discoverServices()


    }
    private func discoverServices(){
        peri?.discoverServices(nil)
    }

}
