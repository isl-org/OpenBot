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
        //change the value of variable value in drawSegment() of drawing updated segment
        //value range 0-180

            segmentValue = min(180, segmentValue)
            segmentValue = Int(Double(segmentValue) * 0.705)
        drawSegments(in: rect, context: ctx, value: abs(segmentValue))
    }

    func deg2rad(_ number: CGFloat) -> CGFloat {
        number * .pi / 180
    }

    func drawSegments(in rect: CGRect, context ctx: CGContext, value: Int) {
        ctx.saveGState()
        ctx.translateBy(x: rect.midX, y: rect.midY + 60)
        ctx.rotate(by: deg2rad(rotation) - (.pi / 2))
        ctx.setLineWidth(segmentWidth)
        var segmentAngle = deg2rad(CGFloat(value))
        let segmentRadius = (rect.width / 2) - 50
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
        valueLabel.translatesAutoresizingMaskIntoConstraints = false
        addSubview(valueLabel)
        NSLayoutConstraint.activate([
            valueLabel.centerXAnchor.constraint(equalTo: centerXAnchor),
            valueLabel.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -60)
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