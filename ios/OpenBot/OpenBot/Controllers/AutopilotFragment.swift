//
//  AutopilotFragment.swift
//  OpenBot
//
//  Created by Sparsh Jain on 29/09/22.
//

import Foundation
class AutopilotFragment: CameraController {
    var autopilot: Autopilot?;
    var models: [Model] = [];

    override func viewDidLoad() {
        super.viewDidLoad()
        createCameraView()
        let modelItems = loadModels();
        if (modelItems.count > 0) {
            models = Model.fromModelItems(list: modelItems);
            print(models)
            autopilot = Autopilot(model: models[0], device: RuntimeDevice.XNNPACK, numThreads: 1);
        }
    }

    func loadModels() -> [ModelItem] {
        if let url = Bundle.main.url(forResource: "config", withExtension: "json") {
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let jsonData = try decoder.decode([ModelItem].self, from: data)
                return jsonData;
            } catch {
                print("error:\(error)")
            }
        }
        return [];
    }
}
