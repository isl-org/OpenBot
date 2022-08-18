//
//  AllModesCollectionViewCell.swift
//  OpenBot
//
//  Created by Sparsh Jain on 17/08/22.
//

import UIKit

class AllModesCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var icon: UIImageView!
    @IBOutlet weak var content: UIView!

    func setContent(label: String, image: UIImage) {
        title.text = label
        icon.image = image;
    }

    override func layoutSubviews() {
        content.layer.cornerRadius = 10
        content.layer.shadowColor = UIColor(named: "gridItemShadowColor")?.cgColor
        content.layer.shadowOpacity = 1
        content.layer.shadowOffset = CGSize(width: 0, height: 4)
        content.layer.shadowRadius = 14 / 2
    }
}
