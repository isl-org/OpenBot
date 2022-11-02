//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation
import UIKit
class Detector: Network {
    var labels: [String] = [];
    var NUM_DETECTIONS: Int = 0;

    static func create(model: Model, device: RuntimeDevice, numThreads: Int) throws -> AnyObject? {
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
        try super.init(model: model, device: device, numThreads: numThreads);
        labels = loadLabelList(filePath: getLabelPath());
        parseTFlite();
    }

    func parseTFlite() {
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

    /**
     function to get the list of all the labels
     - Returns: list of labels
     */

    func getLabels() -> [String] {
        var result: [String] = []

        for label in labels {
            if (label != "???" && label != "") {
                result.append(label);
            }
        }
        return result
    }

    func feedData() {

    }

    /**
     this function will be overridden by child classes
     - Returns:
     */
    func getLabelPath() -> String {
        "";
    }


    func loadLabelList(filePath: String) -> [String] {
        var result: [String] = []
        if let filepath = Bundle.main.path(forResource: filePath, ofType: "") {
            do {
                let contents = try String(contentsOfFile: filepath)
                result = contents.components(separatedBy: CharacterSet.newlines)
            } catch {
                print("cannot convert file content to string");
            }
        } else {
            print("labelmap.txt not found");
        }
        return result
    }
}