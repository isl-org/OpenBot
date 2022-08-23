//
//  sonarView.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//

import UIKit

class sonarView: UIView {

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

    var rectangleWidth = 20
    var rectangeHeight = 40
    var voltageDeviderColor = UIColor(named: "voltageDivider")

    override func draw(_ rect: CGRect) {
        super.draw(rect)
        let h = frame.height
        let w = frame.width
        let color:UIColor = .yellow

        let drect = CGRect(x: (w * 0.25), y: (h * 0.25), width: CGFloat(rectangleWidth), height: CGFloat(rectangeHeight))
        let bpath:UIBezierPath = UIBezierPath(rect: drect)

        color.set()
        bpath.stroke()

    }
    func drawVoltageDivider(in rect: CGRect, context ctx: CGContext){
        ctx.saveGState()
        let k = draw(CGRect(origin: CGPoint(x: 50, y: 50), size: CGSize(width: 100, height: 100)))


    }
}
