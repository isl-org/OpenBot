//
// Created by Nitish Yadav on 13/09/22.
//

import UIKit

extension UIView {
    func updateAdaptedConstraints() {
        let adaptedConstraints = constraints.filter { (constraint) -> Bool in
            constraint is AdaptedConstraint
        } as! [AdaptedConstraint]

        for constraint in adaptedConstraints {
            constraint.resetConstant()
            constraint.awakeFromNib()
        }
    }
}

extension UIView {

    var top: CGFloat {
        get {
            self.frame.origin.y
        }
        set {
            var frame = self.frame
            frame.origin.y = newValue
            self.frame = frame
        }
    }

    var bottom: CGFloat {
        get {
            frame.origin.y + frame.size.height
        }
        set {
            var frame = self.frame
            frame.origin.y = newValue - self.frame.size.height
            self.frame = frame
        }
    }

    var right: CGFloat {
        get {
            self.frame.origin.x + self.frame.size.width
        }
        set {
            var frame = self.frame
            frame.origin.x = newValue - self.frame.size.width
            self.frame = frame
        }
    }

    var left: CGFloat {
        get {
            self.frame.origin.x
        }
        set {
            var frame = self.frame
            frame.origin.x = newValue
            self.frame = frame
        }
    }
}
