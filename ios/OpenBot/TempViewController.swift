//
//  TempViewController.swift
//  OpenBot
//
//  Created by Nitish Yadav on 13/09/22.
//

import UIKit

class TempViewController: UIViewController {
    var heightConstraint = NSLayoutConstraint()
    var widthConstraint  = NSLayoutConstraint()
    var topConstraint    = NSLayoutConstraint()
    var centerXConstraint = NSLayoutConstraint()
    var centerYConstraint = NSLayoutConstraint()
    let rect = UIView()
    override func viewDidLoad() {
        super.viewDidLoad()
        setupRectangle()

    }

    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        updateConstraints()
    }

    func updateConstraints(){
        updateRectConstraints()
        view.updateAdaptedConstraints()
    }
    func setupRectangle(){
        rect.backgroundColor = Colors.sonar
        rect.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(rect)
        setupRectangleConstraints()
        print(rectSize.height, " ",rectSize.width)
        print(topSpace)
    }
    var rectSize : CGSize {
        resized(size: CGSize(width: 100, height: 100), basedOn: .width)

    }

    var topSpace : CGFloat {
        adapted(dimensionSize: 30, to: .height)
    }

    func setupRectangleConstraints(){
        widthConstraint = rect.widthAnchor.constraint(equalToConstant: rectSize.width)
        heightConstraint = rect.heightAnchor.constraint(equalToConstant: rectSize.height)
//        topConstraint = rect.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: topSpace)
        centerXConstraint = rect.centerXAnchor.constraint(equalTo: view.centerXAnchor)
        centerYConstraint = rect.centerYAnchor.constraint(equalTo: view.centerYAnchor)

        NSLayoutConstraint.activate([
            widthConstraint,
            heightConstraint,
            centerXConstraint,
            centerYConstraint
        ])
    }

    func updateRectConstraints(){
        widthConstraint.constant = rectSize.width
        heightConstraint.constant = rectSize.height

    }
}
