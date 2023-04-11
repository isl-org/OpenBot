//
// Created by Nitish Yadav on 11/04/23.
//

import Foundation
import UIKit
extension UIButton {
    func setInsets(
            forContentPadding contentPadding: UIEdgeInsets,
            imageTitlePadding: CGFloat
    ) {
        var config = UIButton.Configuration.filled()
        config.contentInsets = NSDirectionalEdgeInsets(top: contentPadding.top, leading: contentPadding.left, bottom: contentPadding.bottom, trailing: contentPadding.right + imageTitlePadding)
        config.titlePadding = imageTitlePadding

        let button = UIButton(frame: CGRect(x: 0, y: 0, width: 100, height: 50))
        button.configuration = config
        button.setTitle("Button Title", for: .normal)
    }
}
class CustomButton : UIButton{

}