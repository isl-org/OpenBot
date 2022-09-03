//
// Created by Sparsh Jain on 03/09/22.
//

import Foundation

class Control {
    private var left: Float = 0;
    private var right: Float = 0;

    init(left: Float, right: Float) {
        self.left = max(-1.0, min(1.0, left));
        self.right = max(-1.0, min(1.0, right));
    }

    public func getLeft() -> Float {
        left;
    }

    public func getRight() -> Float {
        right;
    }
}