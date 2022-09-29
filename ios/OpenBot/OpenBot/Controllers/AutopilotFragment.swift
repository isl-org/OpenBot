//
//  AutopilotFragment.swift
//  OpenBot
//
//  Created by Sparsh Jain on 29/09/22.
//

import Foundation

class AutopilotFragment: CameraController {
    var autopilot: Autopilot?;

    override func viewDidLoad() {
        super.viewDidLoad()
        createCameraView()
        autopilot = Autopilot(model: Model(id: 1, classType: .AUTOPILOT_F, type: .AUTOPILOT, name: "CIL-Mobile.tflit", pathType: .ASSET, path: "networks/autopilot_float.tflite", inputSize: "256x96"), device: RuntimeDevice.XNNPACK, numThreads: 1);
    }
}
