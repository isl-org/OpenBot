//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import org.opencv.dnn.Net;
import org.opencv.dnn.TextDetectionModel;
import org.opencv.dnn.TextDetectionModel_EAST;

// C++: class TextDetectionModel_EAST
/**
 * This class represents high-level API for text detection DL networks compatible with EAST model.
 *
 * Configurable parameters:
 * - (float) confThreshold - used to filter boxes by confidences, default: 0.5f
 * - (float) nmsThreshold - used in non maximum suppression, default: 0.0f
 */
public class TextDetectionModel_EAST extends TextDetectionModel {

    protected TextDetectionModel_EAST(long addr) { super(addr); }

    // internal usage only
    public static TextDetectionModel_EAST __fromPtr__(long addr) { return new TextDetectionModel_EAST(addr); }

    //
    // C++:   cv::dnn::TextDetectionModel_EAST::TextDetectionModel_EAST(Net network)
    //

    /**
     * Create text detection algorithm from deep learning network
     * @param network Net object
     */
    public TextDetectionModel_EAST(Net network) {
        super(TextDetectionModel_EAST_0(network.nativeObj));
    }


    //
    // C++:   cv::dnn::TextDetectionModel_EAST::TextDetectionModel_EAST(string model, string config = "")
    //

    /**
     * Create text detection model from network represented in one of the supported formats.
     * An order of {@code model} and {@code config} arguments does not matter.
     * @param model Binary file contains trained weights.
     * @param config Text file contains network configuration.
     */
    public TextDetectionModel_EAST(String model, String config) {
        super(TextDetectionModel_EAST_1(model, config));
    }

    /**
     * Create text detection model from network represented in one of the supported formats.
     * An order of {@code model} and {@code config} arguments does not matter.
     * @param model Binary file contains trained weights.
     */
    public TextDetectionModel_EAST(String model) {
        super(TextDetectionModel_EAST_2(model));
    }


    //
    // C++:  TextDetectionModel_EAST cv::dnn::TextDetectionModel_EAST::setConfidenceThreshold(float confThreshold)
    //

    /**
     * Set the detection confidence threshold
     * @param confThreshold A threshold used to filter boxes by confidences
     * @return automatically generated
     */
    public TextDetectionModel_EAST setConfidenceThreshold(float confThreshold) {
        return new TextDetectionModel_EAST(setConfidenceThreshold_0(nativeObj, confThreshold));
    }


    //
    // C++:  float cv::dnn::TextDetectionModel_EAST::getConfidenceThreshold()
    //

    /**
     * Get the detection confidence threshold
     * @return automatically generated
     */
    public float getConfidenceThreshold() {
        return getConfidenceThreshold_0(nativeObj);
    }


    //
    // C++:  TextDetectionModel_EAST cv::dnn::TextDetectionModel_EAST::setNMSThreshold(float nmsThreshold)
    //

    /**
     * Set the detection NMS filter threshold
     * @param nmsThreshold A threshold used in non maximum suppression
     * @return automatically generated
     */
    public TextDetectionModel_EAST setNMSThreshold(float nmsThreshold) {
        return new TextDetectionModel_EAST(setNMSThreshold_0(nativeObj, nmsThreshold));
    }


    //
    // C++:  float cv::dnn::TextDetectionModel_EAST::getNMSThreshold()
    //

    /**
     * Get the detection confidence threshold
     * @return automatically generated
     */
    public float getNMSThreshold() {
        return getNMSThreshold_0(nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::dnn::TextDetectionModel_EAST::TextDetectionModel_EAST(Net network)
    private static native long TextDetectionModel_EAST_0(long network_nativeObj);

    // C++:   cv::dnn::TextDetectionModel_EAST::TextDetectionModel_EAST(string model, string config = "")
    private static native long TextDetectionModel_EAST_1(String model, String config);
    private static native long TextDetectionModel_EAST_2(String model);

    // C++:  TextDetectionModel_EAST cv::dnn::TextDetectionModel_EAST::setConfidenceThreshold(float confThreshold)
    private static native long setConfidenceThreshold_0(long nativeObj, float confThreshold);

    // C++:  float cv::dnn::TextDetectionModel_EAST::getConfidenceThreshold()
    private static native float getConfidenceThreshold_0(long nativeObj);

    // C++:  TextDetectionModel_EAST cv::dnn::TextDetectionModel_EAST::setNMSThreshold(float nmsThreshold)
    private static native long setNMSThreshold_0(long nativeObj, float nmsThreshold);

    // C++:  float cv::dnn::TextDetectionModel_EAST::getNMSThreshold()
    private static native float getNMSThreshold_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
