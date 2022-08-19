//
//  modesCollectionViewCell.swift
//  OpenBot
//
//  Created by Sparsh Jain on 18/08/22.
//

import UIKit

class modesCollectionViewCell: UICollectionViewCell {
    @IBOutlet var icon: UIImageView!
    @IBOutlet weak var title: UILabel!
    static var identifier = "modesCollectionViewCell"

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    public func configure(with gridItem: ModeItem) {
        icon.image = UIImage(named: gridItem.icon)!;
        title.text = gridItem.label;
    }

    static func nib() -> UINib {
        return UINib(nibName: "modesCollectionViewCell", bundle: nil);
    }

}
