//
//  FreeRoamController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//

import UIKit

class FreeRoamController: UIViewController {
    var circle: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        let speedoMeter = GaugeView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 256))
        speedoMeter.backgroundColor = .clear
        view.addSubview(speedoMeter)
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            UIView.animate(withDuration: 1) {
                speedoMeter.value = 33
            }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            UIView.animate(withDuration: 1) {
                speedoMeter.value = 66
            }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
            UIView.animate(withDuration: 1) {
                speedoMeter.value = 0
            }
        }
        let sonar = sonarView(frame: CGRect(x: view.frame.width-70, y: 280, width: 50, height: 110))
        sonar.backgroundColor = .clear
        sonar.layer.cornerRadius = 3
        sonar.layer.masksToBounds  = true
        view.addSubview(sonar)

        let voltage = voltageDivider(frame: CGRect(x: 30, y: 280, width: 50, height: 110))
        voltage.layer.cornerRadius = 5
        voltage.layer.masksToBounds  = true
        view.addSubview(voltage)

        let dIcon = UIButton()
        dIcon.setTitle("D",for: .normal)
        dIcon.layer.borderWidth = 1
        dIcon.layer.borderColor = UIColor.white.cgColor
        dIcon.frame = CGRect(x: 120, y: 310, width: 40, height: 40)
        view.addSubview(dIcon)

        let driveIcon = UIButton()
        driveIcon.setTitle("D",for: .normal)
        driveIcon.layer.borderWidth = 1
        driveIcon.layer.borderColor = UIColor.white.cgColor
//        driveIcon.setBackgroundImage(UIImage("drive"), for: UIControl.State.normal)
        driveIcon.frame = CGRect(x: 180, y: 310, width: 40, height: 40)
        view.addSubview(driveIcon)



        let diameter = 200.0
        let ticks = 40
        var radius = 120
        for i  in 0 ... 3 {
            drawTicks(count: ticks, radius: radius)
            radius = radius - 22;
        }
    }
    func drawTicks(count: Int , radius : Int ) {
        var rotationInDegrees: CGFloat = 0
        let pointOfBreak1 :Int = count/4 ;
        let pointOfBreak2 :Int = 3 * count/4 ;
        for i in 0 ..< count {
            if i <= pointOfBreak1 || i >= pointOfBreak2 {
                let tick = createTick()
                let x = CGFloat(Float(187.5) + Float(radius) * cosf(2 * Float(i) * Float(M_PI) / Float(count) - Float(M_PI) / 2))
                let y = CGFloat(Float(240) + Float(radius) * sinf(2 * Float(i) * Float(M_PI) / Float(count) - Float(M_PI) / 2))
                tick.center = CGPoint(x: x, y: y)
                // degress -> radians
                tick.transform = CGAffineTransform.identity.rotated(by: rotationInDegrees * .pi / 180.0)
                view.addSubview(tick)
                rotationInDegrees = rotationInDegrees + (360.0 / CGFloat(count))
            }
        }
    }
    func createTick() -> UIView {
        let tick = UIView(frame: CGRect(x: 0, y: 0, width: 2.0, height: 1.0))
        tick.backgroundColor = UIColor(red: 0.00, green: 0.44, blue: 0.77, alpha: 1.00)

        return tick
    }
}
