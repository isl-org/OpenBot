//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation
import UIKit

class Detector: Network {
    var labels: [String] = [];
    var NUM_DETECTIONS: Int = 0;
    var selectedClass: String?;
    let ciContext = CIContext()

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
        selectedClass = labels.first {
            $0.capitalized == "person".capitalized
        };
        parseTFlite();
    }

    func parseTFlite() {
    }

    func getNumDetections() -> Int {
        NUM_DETECTIONS;
    }

    public func setSelectedClass(newClass: String) {
        selectedClass = newClass;
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

        static func >(lhs: Recognition, rhs: Recognition) -> Bool {
            lhs.confidence > rhs.confidence
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
            if (label != "???" && label != "" && label != " ") {
                result.append(label);
            }
        }
        return result
    }

    /**
     this function will be overridden by child classes
     - Returns:
     */
    func getLabelPath() -> String {
        ""
    }


    func loadLabelList(filePath: String) -> [String] {
        var result: [String] = []
        if let filepath = Bundle.main.path(forResource: filePath, ofType: "") {
            do {
                let contents = try String(contentsOfFile: filepath)
                let output = contents.components(separatedBy: "\n")
                for label in output {
                    if (label != " " && label != "") {
                        result.append(label);
                    }
                }
            } catch {
                print("cannot convert file content to string");
            }
        } else {
            print("labelmap.txt or coco.txt not found");
        }
        return result
    }

    func recognizeImage(image: UIImage, height: Double, width: Double) throws -> [Recognition] {
        let inputTensor = try tflite!.input(at: 0);
        let imageData = image.pixelBuffer(width: Int(width), height: Int(height));
        let resizedImage = imageData?.resized(to: CGSize(width: getImageSizeX(), height: getImageSizeY()))
        let rgbData = rgbDataFromBuffer(resizedImage!,
                isModelQuantized: inputTensor.dataType == .uInt8);
        try tflite?.copy(rgbData!, toInputAt: 0);
        try tflite?.invoke();
        try runInference();
        return getRecognitions(className: selectedClass!);
    }

    func runInference() throws {
    }

    func getRecognitions(className: String) -> [Recognition] {
        [];
    }

    var mNmsThresh: Float = 0.25;

    //non maximum suppression

    func nms(recognitions: [Recognition]) -> [Recognition] {
        var nmsList: [Recognition] = [];

        var pq = recognitions;
        pq.sort(by: >);
        while (pq.count > 0) {
            var detections = pq;
            detections.sort(by: >);
            let max = detections[0];
            nmsList.append(max);
            pq.removeAll();
            if (detections.count > 0) {
                let size = detections.count - 1;
                for i in stride(from: 1, to: size, by: 1) {
                    let detection: Recognition = detections[i];
                    let b = detection.getLocation();
                    if (box_iou(a: max.getLocation(), b: b) < mNmsThresh) {
                        pq.append(detection);
                    }
                }
            }
        }
        return nmsList;
    }

    func box_iou(a: CGRect, b: CGRect) -> Float {
        box_intersection(a: a, b: b) / box_union(a: a, b: b);
    }

    func box_intersection(a: CGRect, b: CGRect) -> Float {
        let w = overlap(x1: Float(a.midX), w1: Float(a.width), x2: Float(b.midX), w2: Float(b.width));
        let h = overlap(x1: Float(a.midY), w1: Float(a.height), x2: Float(b.midY), w2: Float(b.height));
        if (w < 0 || h < 0) {
            return 0
        };
        return w * h;
    }

    func box_union(a: CGRect, b: CGRect) -> Float {
        let i = box_intersection(a: a, b: b);
        let u = Float(a.width * a.height) + Float(b.width * b.height) - i;
        return u;
    }

    func overlap(x1: Float, w1: Float, x2: Float, w2: Float) -> Float {
        let l1: Float = x1 - w1 / 2;
        let l2: Float = x2 - w2 / 2;
        let left: Float = l1 > l2 ? l1 : l2;
        let r1 = x1 + w1 / 2;
        let r2 = x2 + w2 / 2;
        let right = r1 < r2 ? r1 : r2;
        return right - left;
    }
}