//
//  TempViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 08/09/22.
//

import UIKit

class TempViewController: UIViewController {
    let testView = UIView()

    override func viewDidLoad() {
        super.viewDidLoad()
        testView.frame = CGRect(x: 0, y: 0, width: Int(view.frame.width) / 2, height: Int(view.frame.height / 2))
        testView.backgroundColor = Colors.sonar
        view.addSubview(testView)
        printConstraints();

    }

    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        let orient = UIApplication.shared.statusBarOrientation
        switch orient {
        case .portrait:
            applyLandScapeConstraint()

            break
        default:
            ApplyPortraitConstraint()
            break
        }

    }

    func ApplyPortraitConstraint() {
        removeConstraints()
        let leading = testView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 0)
        leading.identifier = "SerialMonitor"
        let top = testView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 0)
        top.identifier = "SerialMonitor"
        let width = testView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 1)
        width.identifier = "SerialMonitor"
        let height = testView.heightAnchor.constraint(equalTo: view.heightAnchor, multiplier: 0.5)
        height.identifier = "SerialMonitor"
        NSLayoutConstraint.activate([
            leading, width, height
        ])
    }

    func applyLandScapeConstraint() {
        removeConstraints()
        let leading = testView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 0)
        leading.identifier = "SerialMonitor"
//        let top = testView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 0)
//        top.identifier = "SerialMonitor"
        let width = testView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 1)
        width.identifier = "SerialMonitor"
        let height = testView.heightAnchor.constraint(equalTo: view.heightAnchor, multiplier: 0.5)
        height.identifier = "SerialMonitor"
        NSLayoutConstraint.activate([
            leading,  width, height
        ])
    }
    func removeConstraints(){

        for constraint in view.constraints {
            if constraint.identifier == "" {
                view.removeConstraint(constraint)
            }
        }
    }

    func printConstraints() {
        print("printing constraints")
        for constraint in view.constraints {
            print(constraint)
        }
    }


}
