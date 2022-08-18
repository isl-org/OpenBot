//
//  AllModesCollectionViewCell.swift
//  OpenBot
//
//  Created by Sparsh Jain on 17/08/22.
//

import UIKit

class AllModesCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var title: UILabel!

    func setLabel(label: String) {
        title.text = label
    }
}
