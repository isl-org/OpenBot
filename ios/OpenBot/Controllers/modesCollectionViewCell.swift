//
//  modesCollectionViewCell.swift
//  OpenBot
//
//  Created by Sparsh Jain on 18/08/22.
//

import UIKit

class modesCollectionViewCell: UICollectionViewCell {
    @IBOutlet var icon : UIImageView!
    @IBOutlet weak var content: UIView!
    @IBOutlet weak var title: UILabel!
    static var identifier = "modesCollectionViewCell"
//    @IBOutlet weak var content: UIView!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    public func configure(with gridItem: GridItem){
        icon.image = gridItem.icon!;
        title.text = gridItem.label;
    }
    
    static func nib()-> UINib{
        return UINib(nibName: "modesCollectionViewCell", bundle: nil);
    }
    
    override func layoutSubviews() {
        content.layer.cornerRadius = 10
        content.layer.shadowColor = UIColor(named: "gridItemShadowColor")?.cgColor
        content.layer.shadowOpacity = 1
        content.layer.shadowOffset = CGSize(width: 0, height: 4)
        content.layer.shadowRadius = 14 / 2
    }
}
