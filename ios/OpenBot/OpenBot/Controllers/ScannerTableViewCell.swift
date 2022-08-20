//
//  ScannerTableViewCell.swift
//  OpenBot
//
//  Created by Nitish Yadav on 30/07/22.
//

import UIKit
import CoreBluetooth
class ScannerTableViewCell: UITableViewCell {

    @IBOutlet weak var peripheralName: UILabel!
    @IBOutlet weak var connectButton: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        print("hello Nitish")
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        print("i am here")
        // Configure the view for the selected state
    }

}
