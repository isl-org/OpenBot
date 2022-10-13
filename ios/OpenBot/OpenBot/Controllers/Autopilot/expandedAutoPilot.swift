//
// Created by Nitish Yadav on 13/10/22.
//

import Foundation
import UIKit
class expandedAutoPilot : UIView {

    override init(frame: CGRect) {
        super.init(frame: frame)
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeDown.direction = .down
        addGestureRecognizer(swipeDown)
        let swipeUp = UISwipeGestureRecognizer(target: self, action: #selector(respondToSwipeGesture))
        swipeUp.direction = .up
        addGestureRecognizer(swipeUp)


    }
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    @objc func respondToSwipeGesture(gesture: UIGestureRecognizer) {
        if let swipeGesture = gesture as? UISwipeGestureRecognizer {
            switch swipeGesture.direction {
            case .down:
                print("Swiped down")
                UIView.animate(withDuration: 0.45) {
                    if currentOrientation == .portrait{
                        self.frame.origin.y = height-70
                    }
                    else {
                        self.frame.origin.y = width-70
                    }

                }
            case .up:
                print("Swiped up")
                UIView.animate(withDuration: 0.45) {
                    if currentOrientation == .portrait{
                        self.frame.origin.y = height/2
                    }
                    else{
                        self.frame.origin.y = 20
                    }

                }
            default:
                break
            }
        }
    }

}