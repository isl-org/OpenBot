//
//  GaugeView.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//

import UIKit
class GaugeView: UIView {
    var segmentWidth: CGFloat = 40
    var segmentColors = [UIColor(red: 0.10, green: 0.66, blue: 0.98, alpha: 1.00),UIColor(red: 0.00, green: 0.44, blue: 0.77, alpha: 1.00)]
    //rotate the segment
    var rotation: CGFloat = -89
    //speed label
    let valueLabel = UILabel()
    var valueFont = UIFont.systemFont(ofSize: 56)
    override func draw(_ rect: CGRect) {
        guard let ctx = UIGraphicsGetCurrentContext() else { return }
        drawSegments(in: rect, context: ctx)
    }
    func deg2rad(_ number: CGFloat) -> CGFloat {
        return number * .pi / 180
    }
    func drawSegments(in rect: CGRect, context ctx: CGContext) {

        // 1: Save the current drawing configuration
        ctx.saveGState()

        // 2: Move to the center of our drawing rectangle and rotate so that we're pointing at the start of the first segment
        ctx.translateBy(x: rect.midX, y: rect.midY+100)
        ctx.rotate(by: deg2rad(rotation) - (.pi / 2))

        // 3: Set up the user's line width
        ctx.setLineWidth(segmentWidth)

        // 4: Calculate the size of each segment in the total gauge
        let segmentAngle = deg2rad(90)

        // 5: Calculate how wide the segment arcs should be
        let segmentRadius = (rect.width/2)-50

        // 6: Draw each segment
        for (index, segment) in segmentColors.enumerated() {
            // figure out where the segment starts in our arc
            let start = CGFloat(index) * segmentAngle
            // activate its color
            segment.set()
            // add a path for the segment
            ctx.addArc(center: .zero, radius: segmentRadius, startAngle: start, endAngle: start + segmentAngle, clockwise: false)
            // and stroke it using the activated color
            ctx.drawPath(using: .stroke)
        }


        // 7: Reset the graphics state
        ctx.restoreGState()
    }
    func setUp() {
        valueLabel.font = valueFont
        valueLabel.textColor = .white
        valueLabel.translatesAutoresizingMaskIntoConstraints = false
        addSubview(valueLabel)
        NSLayoutConstraint.activate([
            valueLabel.centerXAnchor.constraint(equalTo: centerXAnchor),
            valueLabel.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -20)
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
            // update the value label to show the exact number
            valueLabel.text = String(value)
        }
    }
}
