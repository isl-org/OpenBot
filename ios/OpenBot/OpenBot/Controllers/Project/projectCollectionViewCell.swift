//
// Created by Nitish Yadav on 13/04/23.
//

import Foundation
import UIKit
/**
    Class to create cells for projects
 */
class projectCollectionViewCell : UICollectionViewCell{

    @IBOutlet weak var projectDate: UILabel!
    @IBOutlet weak var projectName: UILabel!
    static var identifier = "projectCollectionViewCell"
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    /**
     Function to configure cells for their name and date
     - Parameter gridItem:
     */
    public func configure(with gridItem: ProjectItem) {
        projectName.text = gridItem.projectName;
        projectName.font = HelveticaNeue.bold(size: 15);
        projectDate.text = dateFormatter(isoDate: gridItem.projectDate);
        projectDate.font = HelveticaNeue.regular(size: 10);
    }
    /**
     calling nib
     - Returns:
     */
    static func nib() -> UINib {
        UINib(nibName: "projectCollectionViewCell", bundle: nil);
    }
    private func dateFormatter(isoDate : String)->String{
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        let date = dateFormatter.date(from: isoDate)

        let dateFormatterOutput = DateFormatter()
        dateFormatterOutput.dateFormat = "dd MMMM yyyy"
        let formattedDate = dateFormatterOutput.string(from: date!)
      return formattedDate
    }
}
