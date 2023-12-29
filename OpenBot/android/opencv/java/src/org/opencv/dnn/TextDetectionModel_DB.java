//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import org.opencv.dnn.Net;
import org.opencv.dnn.TextDetectionModel;
import org.opencv.dnn.TextDetectionModel_DB;

// C++: class TextDetectionModel_DB
/**
 * This class represents high-level API for text detection DL networks compatible with DB model.
 *
 * Related publications: CITE: liao2020real
 * Paper: https://arxiv.org/abs/1911.08947
 * For more information about the hyper-parameters setting, please refer to https://github.com/MhLiao/DB
 *
 * Configurable parameters:
 * - (float) binaryThreshold - The threshold of the binary map. It is usually set to 0.3.
 * - (float) polygonThreshold - The threshold of text polygons. It is usually set to 0.5, 0.6, and 0.7. Default is 0.5f
 * - (double) unclipRatio - The unclip ratio of the detected text region, which determines the output size. It is usually set to 2.0.
 * - (int) maxCandidates - The max number of the output results.
 */
public class TextDetectionModel_DB extends TextDetectionModel {

    protected TextDetectionModel_DB(long addr) { super(addr); }

    // internal usage only
    public static TextDetectionModel_DB __fromPtr__(long addr) { return new TextDetectionModel_DB(addr); }

    //
    // C++:   cv::dnn::TextDetectionModel_DB::TextDetectionModel_DB(Net network)
    //

    /**
     * Create text detection algorithm from deep learning network.
     * @param network Net object.
     */
    public TextDetectionModel_DB(Net network) {
        super(TextDetectionModel_DB_0(network.nativeObj));
    }


    //
    // C++:   cv::dnn::TextDetectionModel_DB::TextDetectionModel_DB(string model, string config = "")
    //

    /**
     * Create text detection model from network represented in one of the supported formats.
     * An order of {@code model} and {@code config} arguments does not matter.
     * @param model Binary file contains trained weights.
     * @param config Text file contains network configuration.
     */
    public TextDetectionModel_DB(String model, String config) {
        super(TextDetectionModel_DB_1(model, config));
    }

    /**
     * Create text detection model from network represented in one of the supported formats.
     * An order of {@code model} and {@code config} arguments does not matter.
     * @param model Binary file contains trained weights.
     */
    public TextDetectionModel_DB(String model) {
        super(TextDetectionModel_DB_2(model));
    }


    //
    // C++:  TextDetectionModel_DB cv::dnn::TextDetectionModel_DB::setBinaryThreshold(float binaryThreshold)
    //

    public TextDetectionModel_DB setBinaryThreshold(float binaryThreshold) {
        return new TextDetectionModel_DB(setBinaryThreshold_0(nativeObj, binaryThreshold));
    }


    //
    // C++:  float cv::dnn::TextDetectionModel_DB::getBinaryThreshold()
    //

    public float getBinaryThreshold() {
        return getBinaryThreshold_0(nativeObj);
    }


    //
    // C++:  TextDetectionModel_DB cv::dnn::TextDetectionModel_DB::setPolygonThreshold(float polygonThreshold)
    //

    public TextDetectionModel_DB setPolygonThreshold(float polygonThreshold) {
        return new TextDetectionModel_DB(setPolygonThreshold_0(nativeObj, polygonThreshold));
    }


    //
    // C++:  float cv::dnn::TextDetectionModel_DB::getPolygonThreshold()
    //

    public float getPolygonThreshold() {
        return getPolygonThreshold_0(nativeObj);
    }


    //
    // C++:  TextDetectionModel_DB cv::dnn::TextDetectionModel_DB::setUnclipRatio(double unclipRatio)
    //

    public TextDetectionModel_DB setUnclipRatio(double unclipRatio) {
        return new TextDetectionModel_DB(setUnclipRatio_0(nativeObj, unclipRatio));
    }


    //
    // C++:  double cv::dnn::TextDetectionModel_DB::getUnclipRatio()
    //

    public double getUnclipRatio() {
        return getUnclipRatio_0(nativeObj);
    }


    //
    // C++:  TextDetectionModel_DB cv::dnn::TextDetectionModel_DB::setMaxCandidates(int maxCandidates)
    //

    public TextDetectionModel_DB setMaxCandidates(int maxCandidates) {
        return new TextDetectionModel_DB(setMaxCandidates_0(nativeObj, maxCandidates));
    }


    //
    // C++:  int cv::dnn::TextDetectionModel_DB::getMaxCandidates()
    //

    public int getMaxCandidates() {
        return getMaxCandidates_0(nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::dnn::TextDetectionModel_DB::TextDetectionModel_DB(Net network)
    private static native long TextDetectionModel_DB_0(long network_nativeObj);

    // C++:   cv::dnn::TextDetectionModel_DB::TextDetectionModel_DB(string model, string config = "")
    private static native long TextDetectionModel_DB_1(String model, String config);
    private static native long TextDetectionModel_DB_2(String model);

    // C++:  TextDetectionModel_DB cv::dnn::TextDetectionModel_DB::setBinaryThreshold(float binaryThreshold)
    private static native long setBinaryThreshold_0(long nativeObj, float binaryThreshold);

    // C++:  float cv::dnn::TextDetectionModel_DB::getBinaryThreshold()
    private static native float getBinaryThreshold_0(long nativeObj);

    // C++:  TextDetectionModel_DB cv::dnn::TextDetectionModel_DB::setPolygonThreshold(float polygonThreshold)
    private static native long setPolygonThreshold_0(long nativeObj, float polygonThreshold);

    // C++:  float cv::dnn::TextDetectionModel_DB::getPolygonThreshold()
    private static native float getPolygonThreshold_0(long nativeObj);

    // C++:  TextDetectionModel_DB cv::dnn::TextDetectionModel_DB::setUnclipRatio(double unclipRatio)
    private static native long setUnclipRatio_0(long nativeObj, double unclipRatio);

    // C++:  double cv::dnn::TextDetectionModel_DB::getUnclipRatio()
    private static native double getUnclipRatio_0(long nativeObj);

    // C++:  TextDetectionModel_DB cv::dnn::TextDetectionModel_DB::setMaxCandidates(int maxCandidates)
    private static native long setMaxCandidates_0(long nativeObj, int maxCandidates);

    // C++:  int cv::dnn::TextDetectionModel_DB::getMaxCandidates()
    private static native int getMaxCandidates_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
