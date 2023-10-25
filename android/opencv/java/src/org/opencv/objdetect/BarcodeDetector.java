//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

// C++: class BarcodeDetector

public class BarcodeDetector extends GraphicalCodeDetector {

    protected BarcodeDetector(long addr) { super(addr); }

    // internal usage only
    public static BarcodeDetector __fromPtr__(long addr) { return new BarcodeDetector(addr); }

    //
    // C++:   cv::barcode::BarcodeDetector::BarcodeDetector()
    //

    /**
     * Initialize the BarcodeDetector.
     */
    public BarcodeDetector() {
        super(BarcodeDetector_0());
    }


    //
    // C++:   cv::barcode::BarcodeDetector::BarcodeDetector(string prototxt_path, string model_path)
    //

    /**
     * Initialize the BarcodeDetector.
     *
     * Parameters allow to load _optional_ Super Resolution DNN model for better quality.
     * @param prototxt_path prototxt file path for the super resolution model
     * @param model_path model file path for the super resolution model
     */
    public BarcodeDetector(String prototxt_path, String model_path) {
        super(BarcodeDetector_1(prototxt_path, model_path));
    }


    //
    // C++:  bool cv::barcode::BarcodeDetector::decodeWithType(Mat img, Mat points, vector_string& decoded_info, vector_string& decoded_type)
    //

    /**
     * Decodes barcode in image once it's found by the detect() method.
     *
     * @param img grayscale or color (BGR) image containing bar code.
     * @param points vector of rotated rectangle vertices found by detect() method (or some other algorithm).
     * For N detected barcodes, the dimensions of this array should be [N][4].
     * Order of four points in vector&lt;Point2f&gt; is bottomLeft, topLeft, topRight, bottomRight.
     * @param decoded_info UTF8-encoded output vector of string or empty vector of string if the codes cannot be decoded.
     * @param decoded_type vector strings, specifies the type of these barcodes
     * @return true if at least one valid barcode have been found
     */
    public boolean decodeWithType(Mat img, Mat points, List<String> decoded_info, List<String> decoded_type) {
        return decodeWithType_0(nativeObj, img.nativeObj, points.nativeObj, decoded_info, decoded_type);
    }


    //
    // C++:  bool cv::barcode::BarcodeDetector::detectAndDecodeWithType(Mat img, vector_string& decoded_info, vector_string& decoded_type, Mat& points = Mat())
    //

    /**
     * Both detects and decodes barcode
     *
     * @param img grayscale or color (BGR) image containing barcode.
     * @param decoded_info UTF8-encoded output vector of string(s) or empty vector of string if the codes cannot be decoded.
     * @param decoded_type vector of strings, specifies the type of these barcodes
     * @param points optional output vector of vertices of the found  barcode rectangle. Will be empty if not found.
     * @return true if at least one valid barcode have been found
     */
    public boolean detectAndDecodeWithType(Mat img, List<String> decoded_info, List<String> decoded_type, Mat points) {
        return detectAndDecodeWithType_0(nativeObj, img.nativeObj, decoded_info, decoded_type, points.nativeObj);
    }

    /**
     * Both detects and decodes barcode
     *
     * @param img grayscale or color (BGR) image containing barcode.
     * @param decoded_info UTF8-encoded output vector of string(s) or empty vector of string if the codes cannot be decoded.
     * @param decoded_type vector of strings, specifies the type of these barcodes
     * @return true if at least one valid barcode have been found
     */
    public boolean detectAndDecodeWithType(Mat img, List<String> decoded_info, List<String> decoded_type) {
        return detectAndDecodeWithType_1(nativeObj, img.nativeObj, decoded_info, decoded_type);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::barcode::BarcodeDetector::BarcodeDetector()
    private static native long BarcodeDetector_0();

    // C++:   cv::barcode::BarcodeDetector::BarcodeDetector(string prototxt_path, string model_path)
    private static native long BarcodeDetector_1(String prototxt_path, String model_path);

    // C++:  bool cv::barcode::BarcodeDetector::decodeWithType(Mat img, Mat points, vector_string& decoded_info, vector_string& decoded_type)
    private static native boolean decodeWithType_0(long nativeObj, long img_nativeObj, long points_nativeObj, List<String> decoded_info, List<String> decoded_type);

    // C++:  bool cv::barcode::BarcodeDetector::detectAndDecodeWithType(Mat img, vector_string& decoded_info, vector_string& decoded_type, Mat& points = Mat())
    private static native boolean detectAndDecodeWithType_0(long nativeObj, long img_nativeObj, List<String> decoded_info, List<String> decoded_type, long points_nativeObj);
    private static native boolean detectAndDecodeWithType_1(long nativeObj, long img_nativeObj, List<String> decoded_info, List<String> decoded_type);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
