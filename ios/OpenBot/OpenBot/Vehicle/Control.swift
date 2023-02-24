//
// Created by Sparsh Jain on 03/09/22.
//

import Foundation

/// A simple data container for the robot control
class Control {
    // Left and right controls represents the fraction of maximal duty cycle of a
    // PWM signal sent to the motors of the robot's left and right wheels.
    private var left: Float = 0;
    private var right: Float = 0;

    /// Initialization routine
    init() {
    }

    /// Initialization routine
    init(left: Float, right: Float) {
        self.left = max(-1.0, min(1.0, left));
        self.right = max(-1.0, min(1.0, right));
    }

    /// Left control getter
    public func getLeft() -> Float {
        left;
    }

    /// Right control getter
    public func getRight() -> Float {
        right;
    }
}
