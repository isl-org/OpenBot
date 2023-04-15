//
// Created by Nitish Yadav on 13/09/22.
//

import UIKit

/// This extension adds a method to UIView to update any "adapted constraints" on the view.
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

/// This extension adds computed properties to UIView to get/set the top, bottom, right, and left edges of the view's frame.
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

/**
 extension to create google Sign-In button
 */
extension UIView {
    func createSignInButton(frame: CGRect) -> UIView {
        let signInBtn = UIView(frame: frame);
        signInBtn.backgroundColor = UIColor(named: "signInButtonColor");
        signInBtn.layer.cornerRadius = 10;
        signInBtn.addSubview(createGoogleIcon(frame: CGRect(x: signInBtn.frame.width / 2 - 100, y: 16, width: 20, height: 20)));
        signInBtn.addSubview(createSingInText(frame: CGRect(x: signInBtn.frame.width / 2 - 69, y: 5, width: 160, height: 40)))
        return signInBtn;
    }

    private func createGoogleIcon(frame: CGRect) -> UIImageView {
        let googleIcon = UIImageView(frame: frame);
        googleIcon.image = UIImage(named: "googleIcon");
        return googleIcon;
    }

    private func createSingInText(frame: CGRect) -> UILabel {
        let signInText = UILabel(frame: frame);
        signInText.text = "Sign-in with Google";
        signInText.textColor = traitCollection.userInterfaceStyle == .dark ? UIColor.white : UIColor.black;
        signInText.font = signInText.font?.withSize(18);
        return signInText;
    }
}

class BottomSheetView: UIView {
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private func setup(){
        self.backgroundColor = Colors.bdColor;
        layer.cornerRadius = 30;
        layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMinYCorner]
    }
}