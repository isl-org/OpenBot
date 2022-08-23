//
//  FreeRoamController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 23/08/22.
//

import UIKit

class FreeRoamController: UIViewController {

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
        let sonar = sonarView(frame: CGRect(x: 0, y: 300, width: view.frame.width, height: 200))
        sonar.backgroundColor = .clear
        sonar.draw(CGRect(x: 50, y: 50, width: 100, height: 100))
        view.addSubview(sonar)



    }



    
}
