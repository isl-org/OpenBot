//
//  voltageDivider.swift
//  OpenBot
//
//  Created by Nitish Yadav on 24/08/22.
//

import UIKit

class voltageDivider: UIView {
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    required public init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    override func draw(_ rect: CGRect) {
        guard let context = UIGraphicsGetCurrentContext() else {
            print("could not get graphics context")
            return
        }
        let bottomRect = CGRect(origin: CGPoint(x: rect.origin.x, y: rect.height/3 ), size: CGSize(width: rect.size.width, height:rect.height)
        )
        UIColor(red: 0.31, green: 0.71, blue: 0.42, alpha: 1.00).set()
        guard let context = UIGraphicsGetCurrentContext() else { return }
        context.fill(bottomRect)
        context.setStrokeColor(UIColor.white.cgColor)
        context.setLineWidth(2)
        context.stroke(rect.insetBy(dx: 0, dy: 0))


    }

}


