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
        let speedoMeter = GaugeView(frame: CGRect(x: 40, y: 40, width: 256, height: 256))
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
//        let sonar = sonarView(frame: CGRect(x: 0, y: 300, width: view.frame.width, height: 200))
//        sonar.backgroundColor = .clear
//        sonar.draw(CGRect(x: 50, y: 50, width: 100, height: 100))
//        view.addSubview(sonar)

        let diameter = 200.0
        let ticks = 60
        drawTicks(count: ticks)
    }
    func drawTicks(count: Int) {

        let radius = 90
        var rotationInDegrees: CGFloat = 0
        var pointOfBreak1 :Int = count/4 ;
        var pointOfBreak2 :Int = 3 * count/4 ;

        for i in 0 ..< count {
            if i <= pointOfBreak1 || i >= pointOfBreak2 {


                let tick = createTick()
//
                let x = CGFloat(Float(169) + Float(radius) * cosf(2 * Float(i) * Float(M_PI) / Float(count) - Float(M_PI) / 2))
                let y = CGFloat(Float(169) + Float(radius) * sinf(2 * Float(i) * Float(M_PI) / Float(count) - Float(M_PI) / 2))
                tick.center = CGPoint(x: x, y: y)
                // degress -> radians
                tick.transform = CGAffineTransform.identity.rotated(by: rotationInDegrees * .pi / 180.0)
                view.addSubview(tick)

                rotationInDegrees = rotationInDegrees + (360.0 / CGFloat(count))
            }
        }

    }

    func createTick() -> UIView {
        let tick = UIView(frame: CGRect(x: 0, y: 0, width: 2.0, height: 10.0))
        tick.backgroundColor = UIColor(red: 0.00, green: 0.44, blue: 0.77, alpha: 1.00)

        return tick
    }



    
}
