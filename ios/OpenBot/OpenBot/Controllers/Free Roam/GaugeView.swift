//
//  GaugeView.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//

import UIKit
class GaugeView: UIView {
    var segmentWidth: CGFloat = 40
    var segmentColors = [UIColor(red: 0.10, green: 0.66, blue: 0.98, alpha: 1.00), UIColor(red: 0.00, green: 0.44, blue: 0.77, alpha: 1.00)]
    var rotation: CGFloat = -89
    //speed label
    let valueLabel = UILabel()
    var valueFont = UIFont(name:"medium", size: 100.0)
    var segmentValue : Int = 50
    override func draw(_ rect: CGRect) {
        guard let ctx = UIGraphicsGetCurrentContext() else {
            print("no ctx found")
            return
        }
            segmentValue = min(180, segmentValue)
            segmentValue = Int(Double(segmentValue) * 0.705)
            drawSegments(in: rect, context: ctx, value: abs(segmentValue))
    }

    func deg2rad(_ number: CGFloat) -> CGFloat {
        number * .pi / 180
    }

    func drawSegments(in rect: CGRect, context ctx: CGContext, value: Int) {
        ctx.saveGState()
        ctx.translateBy(x: 180.5, y: rect.midY + 40)
//        if currentOrientation == .portrait{
////            ctx.translateBy(x: rect.midX-10, y: rect.midY + 40)
//            ctx.translateBy(x: 180.5, y: rect.midY + 40)
//        }
//        else{
//            ctx.translateBy(x: 180.5, y: rect.midY + 40)
//        }
        ctx.rotate(by: deg2rad(rotation) - (.pi / 2))
        ctx.setLineWidth(segmentWidth)
        var segmentAngle = deg2rad(CGFloat(value))
        let segmentRadius = (width / 2) - 60
        for (index, segment) in segmentColors.enumerated() {
            let start = CGFloat(index) * segmentAngle
            segment.set()
            if index == 0 {
                ctx.addArc(center: .zero, radius: segmentRadius, startAngle: start, endAngle: start + segmentAngle, clockwise: false)
            } else {
                segmentAngle = deg2rad(CGFloat(180 - value))
                ctx.addArc(center: .zero, radius: segmentRadius, startAngle: start, endAngle: start + segmentAngle, clockwise: false)
            }
            ctx.drawPath(using: .stroke)
        }
        ctx.restoreGState()
    }

    func setUp() {
        valueLabel.font = valueFont
        valueLabel.textColor = .white
//        valueLabel.translatesAutoresizingMaskIntoConstraints = false
        valueLabel.frame = CGRect(x: 175.5, y: 100, width: 100, height: 100)
        addSubview(valueLabel)
        NSLayoutConstraint.activate([
//            valueLabel.centerXAnchor.constraint(equalTo: centerXAnchor, constant: -10),
//            valueLabel.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -80)
        ])
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
        setUp()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setUp()
    }

    var value: Int = 0 {
        didSet {
            valueLabel.text = String(abs(value))
            segmentValue = value
        }
    }

}