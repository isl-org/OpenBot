//
// Created by Nitish Yadav on 13/04/23.
//

import Foundation
import UIKit
class projectCollectionViewCell : UICollectionViewCell{

    @IBOutlet weak var projectDate: UILabel!
    @IBOutlet weak var projectName: UILabel!
    static var identifier = "projectCollectionViewCell"
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    public func configure(with gridItem: ProjectItem) {
        projectName.text = gridItem.projectName;
        projectDate.text = gridItem.projectDate;
        projectName.font = HelveticaNeue.bold(size: 15);
    }

    static func nib() -> UINib {
        UINib(nibName: "projectCollectionViewCell", bundle: nil);
    }
}
