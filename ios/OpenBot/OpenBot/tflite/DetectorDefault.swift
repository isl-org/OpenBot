//
// Created by Sparsh Jain on 01/11/22.
//

import Foundation
import TensorFlowLite

/// This TensorFlow Lite detector works with the quantized MobileNet and EfficientDet model.
class DetectorDefault: Detector {
    
    /// outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
    /// contains the location of detected boxes
    private var outputLocations: UnsafeMutableBufferPointer<Float32>?;
    /// outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
    /// contains the classes of detected boxes
    private var outputClasses: UnsafeMutableBufferPointer<Float32>?;
    /// outputScores: array of shape [Batchsize, NUM_DETECTIONS]
    /// contains the scores of detected boxes
    private var outputScores: UnsafeMutableBufferPointer<Float32>?;
    /// outputScores: array of shape [Batchsize, NUM_DETECTIONS]
    /// contains the scores of detected boxes
    private var numDetections: UnsafeMutableBufferPointer<Float32>?;
    
    /// indices in tflite model
    private var outputLocationsIdx: Int = -1;
    private var outputClassesIdx: Int = -1;
    private var outputScoresIdx: Int = -1;
    private var numDetectionsIdx: Int = -1;
    
    /// Initializes a DetectorDefault.
    override init(model: Model, device: RuntimeDevice, numThreads: Int) throws {
        try super.init(model: model, device: device, numThreads: numThreads)
    }
    
    override func getMaintainAspect() -> Bool {
        return false;
    }
    
    override func getCropRect() -> CGRect {
        return CGRect(x: 0, y: 0, width: 0, height: 0);
    }
    
    override func getLabelPath() -> String {
        return "labelmap.txt";
    }
    
    override func getNumBytesPerChannel() -> Int {
        /// the quantized model uses a single byte only
        return 1;
    }
    
    override func getNumDetections() -> Int {
        NUM_DETECTIONS;
    }
    
    override func parseTFlite() {
        let index0 = try! tflite?.output(at: 0);
        let index1 = try! tflite?.output(at: 1);
        let index2 = try! tflite?.output(at: 2);
        let index3 = try! tflite?.output(at: 3);
        
        assignIndex(tensor: index0, index: 0);
        assignIndex(tensor: index1, index: 1);
        assignIndex(tensor: index2, index: 2);
        assignIndex(tensor: index3, index: 3);
        NUM_DETECTIONS = try! tflite?.output(at: outputLocationsIdx).shape.dimensions[1] ?? 0;
    }
    
    func assignIndex(tensor: Tensor?, index: Int) {
        switch (tensor?.name) {
        case "TFLite_Detection_PostProcess":
            outputLocationsIdx = index;
            break
        case "TFLite_Detection_PostProcess:1":
            outputClassesIdx = index;
            break
        case "TFLite_Detection_PostProcess:2":
            outputScoresIdx = index;
            break
        case "TFLite_Detection_PostProcess:3":
            numDetectionsIdx = index;
            break
        case "StatefulPartitionedCall:3":
            outputLocationsIdx = index;
            break
        case "StatefulPartitionedCall:2":
            outputClassesIdx = index;
            break
        case "StatefulPartitionedCall:1":
            outputScoresIdx = index;
            break
        case "StatefulPartitionedCall:0":
            numDetectionsIdx = index;
            break
        default:
            break;
        }
    }
    
    override func feedData() throws {
        do {
            
            let outputLocationsTensor = try tflite?.output(at: outputLocationsIdx);
            let outputClassesTensor = try tflite?.output(at: outputClassesIdx);
            let outputScoresTensor = try tflite?.output(at: outputScoresIdx);
            let numDetectionsTensor = try tflite?.output(at: numDetectionsIdx);
            
            let outputSize = outputLocationsTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            outputLocations =
            UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputSize)
            outputLocationsTensor?.data.copyBytes(to: outputLocations!);
            
            let outputClassesTensorSize = outputClassesTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            outputClasses =
            UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputClassesTensorSize)
            outputClassesTensor?.data.copyBytes(to: outputClasses!);
            
            let outputScoresTensorSize = outputScoresTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            outputScores =
            UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputScoresTensorSize)
            outputScoresTensor?.data.copyBytes(to: outputScores!);
            
            let numDetectionsTensorSize = numDetectionsTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            numDetections =
            UnsafeMutableBufferPointer<Float32>.allocate(capacity: numDetectionsTensorSize)
            numDetectionsTensor?.data.copyBytes(to: numDetections!);
            
        } catch {
            print("error:\(error)")
        }
    }
    
    override func getRecognitions(className: String, width: Int, height: Int) -> [Recognition] {
        /// Show the best detections.
        /// after scaling them back to the input size.
        var recognitions: [Recognition] = [];
        for i in 0..<getNumDetections() {
            let xPos = CGFloat(outputLocations![(4 * i) + 1]) * CGFloat(getImageSizeX());
            let yPos = CGFloat(outputLocations![(4 * i)]) * CGFloat(getImageSizeY());
            let w = CGFloat(outputLocations![(4 * i) + 3]) * CGFloat(getImageSizeX()) - xPos;
            let h = CGFloat(outputLocations![(4 * i) + 2]) * CGFloat(getImageSizeY()) - yPos;
            let detection = CGRect(x: xPos, y: yPos, width: w, height: h).applying(CGAffineTransform(scaleX: CGFloat(width)/CGFloat(getImageSizeX()), y: CGFloat(height)/CGFloat(getImageSizeY())));
            /// SSD Mobilenet V1 Model assumes class 0 is background class
            /// in label file and class labels start from 1 to number_of_classes+1,
            /// while outputClasses correspond to class index from 0 to number_of_classes
            let classId: Int = Int(outputClasses![i]);
            let labelId: Int = classId + 1;
            if (className == labels[labelId]) {
                recognitions.append(Recognition(id: String(i), title: labels[labelId], confidence: outputScores![i], location: detection, classId: classId));
            }
        }
        return nms(recognitions: recognitions);
    }
}
