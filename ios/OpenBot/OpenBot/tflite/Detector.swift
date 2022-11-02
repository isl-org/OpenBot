//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation
import UIKit
class Detector: Network {
    var labels: [String];

    func create(model: Model, device: RuntimeDevice, numThreads: Int) throws -> AnyObject? {
        switch (model.classType) {
        case .MOBILENETV1_1_0_Q, .MOBILENETV3_S_Q:
            return try DetectorQuantizedMobileNet(model: model, device: device, numThreads: numThreads);
        case .YOLOV4:
            return try DetectorFloatYoloV4(model: model, device: device, numThreads: numThreads);
        default:
            return nil;
        }
    }

    override init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        labels = Common.loadAllObjectsList();
        try super.init(model: model, device: device, numThreads: numThreads);
        parseTFlite();
    }

    func parseTFlite() {
        preconditionFailure("This method must be overridden")
    }

    class Recognition {
        var id: String;
        var title: String;
        var confidence: Float;
        var location: CGRect;
        var classId: Int;

        init(id: String, title: String, confidence: Float, location: CGRect, classId: Int) {
            self.id = id;
            self.title = title;
            self.confidence = confidence;
            self.location = location;
            self.classId = classId;
        }

        public func getId() -> String {
            id;
        }

        public func getTitle() -> String {
            title;
        }

        public func getConfidence() -> Float {
            confidence;
        }

        public func getLocation() -> CGRect {
            location;
        }

        public func setLocation(location: CGRect) {
            self.location = location;
        }

        public func getClassId() -> Int {
            classId;
        }
    }

}