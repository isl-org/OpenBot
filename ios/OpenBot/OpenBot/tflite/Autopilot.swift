//
// Created by Sparsh Jain on 29/09/22.
//

import Foundation
import TensorFlowLite

class Autopilot: Network {
    var cmdIndex: Int = -1;
    var imgIndex: Int = -1;

    override init(model: Model, device: RuntimeDevice, numThreads: Int) {
        try! super.init(model: model, device: device, numThreads: numThreads)
        do {
            let index0 = try tflite?.input(at: 0);
            let index1 = try tflite?.input(at: 0);
            if (index0?.name == "serving_default_cmd_input:0" || index0?.name == "cmd_input") {
                cmdIndex = 0;
                imgIndex = 1;
            } else {
                imgIndex = 0;
                cmdIndex = 1;
            }

        } catch {
            print("error:\(error)")
        }
    }

    func recogniseImage(image: CGImage, indicator: Float) -> Control {
        do {

            print("imgIndex", imgIndex);
            print("cmdIndex", cmdIndex);
            var indicatorData: Data = Data();
            indicatorData.append(contentsOf: indicator.bytes);
            try tflite?.copy(indicatorData, toInputAt: cmdIndex);

            //make image input
            convertImageToData(image: image);
            try tflite?.copy(imgData, toInputAt: imgIndex);

            try tflite?.invoke();

            let outputTensor = try tflite?.output(at: 0);
            let outputSize = outputTensor?.shape.dimensions.reduce(1, { x, y in x * y }) ?? 0
            let outputData =
                    UnsafeMutableBufferPointer<Float32>.allocate(capacity: outputSize)
            let res = outputTensor?.data.copyBytes(to: outputData)
            return Control(left: outputData[0], right: outputData[1])
        } catch {
            print("error:\(error)")
            return Control(left: 0, right: 0)
        };
    }
}