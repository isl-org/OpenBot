//
//  GaugeView.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//

import UIKit

class GaugeView: UIView {
    var circle: UIView!
    var outerBezelColor = UIColor(red: 0, green: 0.5, blue: 1, alpha: 1)
    var outerBezelWidth: CGFloat = 10

    var innerBezelColor = UIColor.white
    var innerBezelWidth: CGFloat = 1
    var insideColor = UIColor.white
    var segmentWidth: CGFloat = 30
    var segmentColors = [UIColor(red: 0.10, green: 0.66, blue: 0.98, alpha: 1.00),UIColor(red: 0.00, green: 0.44, blue: 0.77, alpha: 1.00)]
    var totalAngle: CGFloat = 270
    var rotation: CGFloat = -92
    let valueLabel = UILabel()
    var valueFont = UIFont.systemFont(ofSize: 56)


    var outerCenterDiscColor = UIColor(white: 0.9, alpha: 1)
    var outerCenterDiscWidth: CGFloat = 35
    var innerCenterDiscColor = UIColor(white: 0.7, alpha: 1)
    var innerCenterDiscWidth: CGFloat = 25

    var needleColor = UIColor(white: 0.7, alpha: 1)
    var needleWidth: CGFloat = 4
    let needle = UIView()


    func drawBackground(in rect: CGRect, context ctx: CGContext) {
//        // draw the outer bezel as the largest circle
//        outerBezelColor.set()
//        ctx.fillEllipse(in: rect)
//
//        // move in a little on each edge, then draw the inner bezel
//        let innerBezelRect = rect.insetBy(dx: outerBezelWidth, dy: outerBezelWidth)
//        innerBezelColor.set()
//        ctx.fillEllipse(in: innerBezelRect)
//
//        // finally, move in some more and draw the inside of our gauge
//        let insideRect = innerBezelRect.insetBy(dx: innerBezelWidth, dy: innerBezelWidth)
//        insideColor.set()
//        ctx.fillEllipse(in: insideRect)
    }
    override func draw(_ rect: CGRect) {
        guard let ctx = UIGraphicsGetCurrentContext() else { return }
        drawBackground(in: rect, context: ctx)
        drawSegments(in: rect, context: ctx)
//        drawCenterDisc(in: rect, context: ctx)



    }
    func deg2rad(_ number: CGFloat) -> CGFloat {
        return number * .pi / 180
    }
    func drawSegments(in rect: CGRect, context ctx: CGContext) {
        // 1: Save the current drawing configuration
        ctx.saveGState()

        // 2: Move to the center of our drawing rectangle and rotate so that we're pointing at the start of the first segment
        ctx.translateBy(x: rect.midX, y: rect.midY)
        ctx.rotate(by: deg2rad(rotation) - (.pi / 2))

        // 3: Set up the user's line width
        ctx.setLineWidth(segmentWidth)

        // 4: Calculate the size of each segment in the total gauge
        let segmentAngle = deg2rad(90)

        // 5: Calculate how wide the segment arcs should be
        let segmentRadius = ((rect.width - segmentWidth) / 2)

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




    func drawCenterDisc(in rect: CGRect, context ctx: CGContext) {
        ctx.saveGState()
        ctx.translateBy(x: rect.midX, y: rect.midY)

        let outerCenterRect = CGRect(x: -outerCenterDiscWidth / 2, y: -outerCenterDiscWidth / 2, width: outerCenterDiscWidth, height: outerCenterDiscWidth)
        outerCenterDiscColor.set()
        ctx.fillEllipse(in: outerCenterRect)

        let innerCenterRect = CGRect(x: -innerCenterDiscWidth / 2, y: -innerCenterDiscWidth / 2, width: innerCenterDiscWidth, height: innerCenterDiscWidth)
        innerCenterDiscColor.set()
        ctx.fillEllipse(in: innerCenterRect)
        ctx.restoreGState()
    }
    func setUp() {
        needle.backgroundColor = needleColor
        needle.translatesAutoresizingMaskIntoConstraints = false

        // make the needle a third of our height
        needle.bounds = CGRect(x: 0, y: 0, width: needleWidth, height: bounds.height / 3)

        // align it so that it is positioned and rotated from the bottom center
        needle.layer.anchorPoint = CGPoint(x: 0.5, y: 1)

        // now center the needle over our center point
        needle.center = CGPoint(x: bounds.midX, y: bounds.midY)
        addSubview(needle)

        valueLabel.font = valueFont
//        valueLabel.text = "100"
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

            // figure out where the needle is, between 0 and 1
            let needlePosition = CGFloat(value) / 100

            // create a lerp from the start angle (rotation) through to the end angle (rotation + totalAngle)
            let lerpFrom = rotation
            let lerpTo = rotation + totalAngle

            // lerp from the start to the end position, based on the needle's position
            let needleRotation = lerpFrom + (lerpTo - lerpFrom) * needlePosition
            needle.transform = CGAffineTransform(rotationAngle: deg2rad(needleRotation))
        }
    }
}
