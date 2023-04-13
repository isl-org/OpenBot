//
// Created by Sparsh Jain on 18/08/22.
//

import UIKit

class modesCollectionViewCell: UICollectionViewCell {
    @IBOutlet var icon: UIImageView!
    @IBOutlet weak var title: UILabel!
    static var identifier = "modesCollectionViewCell"

    override func awakeFromNib() {
        super.awakeFromNib()
    }

    public func configure(with gridItem: ModeItem) {
        icon.image = gridItem.icon;
        title.text = gridItem.label;
    }

    static func nib() -> UINib {
        UINib(nibName: "modesCollectionViewCell", bundle: nil);
    }
}
