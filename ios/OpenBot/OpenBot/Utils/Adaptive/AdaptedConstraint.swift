//
// Created by Nitish Yadav on 13/09/22.
//

import UIKit

class AdaptedConstraint: NSLayoutConstraint {

    // MARK: - Properties
    var initialConstant: CGFloat?

    override func awakeFromNib() {
        super.awakeFromNib()
        saveConstant()
        adaptConstant()
    }

}

// MARK: - Adapt constant
extension AdaptedConstraint {
    func adaptConstant() {
        if let dimension = getDimension(from: firstAttribute) {
            self.constant = adapted(dimensionSize: self.constant, to: dimension)
        }
    }

    func getDimension(from attribute: NSLayoutConstraint.Attribute) -> Dimension? {
        switch attribute {
        case .left, .right, .leading, .trailing, .width, .centerX, .leftMargin,
             .rightMargin, .leadingMargin, .trailingMargin, .centerXWithinMargins:
            return .width
        case .top, .bottom, .height, .centerY, .lastBaseline, .firstBaseline,
             .topMargin, .bottomMargin, .centerYWithinMargins:
            return .height
        case .notAnAttribute:
            return nil
        @unknown default:
            return nil
        }
    }
}

// MARK: - Reset constant
extension AdaptedConstraint {
    func saveConstant() {
        initialConstant = self.constant
    }

    func resetConstant() {
        if let initialConstant = initialConstant {
            self.constant = initialConstant
        }
    }
}
