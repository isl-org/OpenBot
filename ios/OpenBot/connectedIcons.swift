//
//  connectedIcons.swift
//  OpenBot
//
//  Created by Nitish Yadav on 24/08/22.
//

import UIKit

class connectedIcons: UIView {
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    required public init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    override func draw(_ rect: CGRect) {
        guard let context = UIGraphicsGetCurrentContext() else {
            print("could not get graphics context")
            return
        }
    }


}
