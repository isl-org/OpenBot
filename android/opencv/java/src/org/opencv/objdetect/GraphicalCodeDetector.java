//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

// C++: class GraphicalCodeDetector

public class GraphicalCodeDetector {

    protected final long nativeObj;
    protected GraphicalCodeDetector(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static GraphicalCodeDetector __fromPtr__(long addr) { return new GraphicalCodeDetector(addr); }

    //
    // C++:  bool cv::GraphicalCodeDetector::detect(Mat img, Mat& points)
    //

    /**
     * Detects graphical code in image and returns the quadrangle containing the code.
     *      @param img grayscale or color (BGR) image containing (or not) graphical code.
     *      @param points Output vector of vertices of the minimum-area quadrangle containing the code.
     * @return automatically generated
     */
    public boolean detect(Mat img, Mat points) {
        return detect_0(nativeObj, img.nativeObj, points.nativeObj);
    }


    //
    // C++:  string cv::GraphicalCodeDetector::decode(Mat img, Mat points, Mat& straight_code = Mat())
    //

    /**
     * Decodes graphical code in image once it's found by the detect() method.
     *
     *      Returns UTF8-encoded output string or empty string if the code cannot be decoded.
     *      @param img grayscale or color (BGR) image containing graphical code.
     *      @param points Quadrangle vertices found by detect() method (or some other algorithm).
     *      @param straight_code The optional output image containing binarized code, will be empty if not found.
     * @return automatically generated
     */
    public String decode(Mat img, Mat points, Mat straight_code) {
        return decode_0(nativeObj, img.nativeObj, points.nativeObj, straight_code.nativeObj);
    }

    /**
     * Decodes graphical code in image once it's found by the detect() method.
     *
     *      Returns UTF8-encoded output string or empty string if the code cannot be decoded.
     *      @param img grayscale or color (BGR) image containing graphical code.
     *      @param points Quadrangle vertices found by detect() method (or some other algorithm).
     * @return automatically generated
     */
    public String decode(Mat img, Mat points) {
        return decode_1(nativeObj, img.nativeObj, points.nativeObj);
    }


    //
    // C++:  string cv::GraphicalCodeDetector::detectAndDecode(Mat img, Mat& points = Mat(), Mat& straight_code = Mat())
    //

    /**
     * Both detects and decodes graphical code
     *
     *      @param img grayscale or color (BGR) image containing graphical code.
     *      @param points optional output array of vertices of the found graphical code quadrangle, will be empty if not found.
     *      @param straight_code The optional output image containing binarized code
     * @return automatically generated
     */
    public String detectAndDecode(Mat img, Mat points, Mat straight_code) {
        return detectAndDecode_0(nativeObj, img.nativeObj, points.nativeObj, straight_code.nativeObj);
    }

    /**
     * Both detects and decodes graphical code
     *
     *      @param img grayscale or color (BGR) image containing graphical code.
     *      @param points optional output array of vertices of the found graphical code quadrangle, will be empty if not found.
     * @return automatically generated
     */
    public String detectAndDecode(Mat img, Mat points) {
        return detectAndDecode_1(nativeObj, img.nativeObj, points.nativeObj);
    }

    /**
     * Both detects and decodes graphical code
     *
     *      @param img grayscale or color (BGR) image containing graphical code.
     * @return automatically generated
     */
    public String detectAndDecode(Mat img) {
        return detectAndDecode_2(nativeObj, img.nativeObj);
    }


    //
    // C++:  bool cv::GraphicalCodeDetector::detectMulti(Mat img, Mat& points)
    //

    /**
     * Detects graphical codes in image and returns the vector of the quadrangles containing the codes.
     *      @param img grayscale or color (BGR) image containing (or not) graphical codes.
     *      @param points Output vector of vector of vertices of the minimum-area quadrangle containing the codes.
     * @return automatically generated
     */
    public boolean detectMulti(Mat img, Mat points) {
        return detectMulti_0(nativeObj, img.nativeObj, points.nativeObj);
    }


    //
    // C++:  bool cv::GraphicalCodeDetector::decodeMulti(Mat img, Mat points, vector_string& decoded_info, vector_Mat& straight_code = vector_Mat())
    //

    /**
     * Decodes graphical codes in image once it's found by the detect() method.
     *      @param img grayscale or color (BGR) image containing graphical codes.
     *      @param decoded_info UTF8-encoded output vector of string or empty vector of string if the codes cannot be decoded.
     *      @param points vector of Quadrangle vertices found by detect() method (or some other algorithm).
     *      @param straight_code The optional output vector of images containing binarized codes
     * @return automatically generated
     */
    public boolean decodeMulti(Mat img, Mat points, List<String> decoded_info, List<Mat> straight_code) {
        Mat straight_code_mat = new Mat();
        boolean retVal = decodeMulti_0(nativeObj, img.nativeObj, points.nativeObj, decoded_info, straight_code_mat.nativeObj);
        Converters.Mat_to_vector_Mat(straight_code_mat, straight_code);
        straight_code_mat.release();
        return retVal;
    }

    /**
     * Decodes graphical codes in image once it's found by the detect() method.
     *      @param img grayscale or color (BGR) image containing graphical codes.
     *      @param decoded_info UTF8-encoded output vector of string or empty vector of string if the codes cannot be decoded.
     *      @param points vector of Quadrangle vertices found by detect() method (or some other algorithm).
     * @return automatically generated
     */
    public boolean decodeMulti(Mat img, Mat points, List<String> decoded_info) {
        return decodeMulti_1(nativeObj, img.nativeObj, points.nativeObj, decoded_info);
    }


    //
    // C++:  bool cv::GraphicalCodeDetector::detectAndDecodeMulti(Mat img, vector_string& decoded_info, Mat& points = Mat(), vector_Mat& straight_code = vector_Mat())
    //

    /**
     * Both detects and decodes graphical codes
     *     @param img grayscale or color (BGR) image containing graphical codes.
     *     @param decoded_info UTF8-encoded output vector of string or empty vector of string if the codes cannot be decoded.
     *     @param points optional output vector of vertices of the found graphical code quadrangles. Will be empty if not found.
     *     @param straight_code The optional vector of images containing binarized codes
     * @return automatically generated
     */
    public boolean detectAndDecodeMulti(Mat img, List<String> decoded_info, Mat points, List<Mat> straight_code) {
        Mat straight_code_mat = new Mat();
        boolean retVal = detectAndDecodeMulti_0(nativeObj, img.nativeObj, decoded_info, points.nativeObj, straight_code_mat.nativeObj);
        Converters.Mat_to_vector_Mat(straight_code_mat, straight_code);
        straight_code_mat.release();
        return retVal;
    }

    /**
     * Both detects and decodes graphical codes
     *     @param img grayscale or color (BGR) image containing graphical codes.
     *     @param decoded_info UTF8-encoded output vector of string or empty vector of string if the codes cannot be decoded.
     *     @param points optional output vector of vertices of the found graphical code quadrangles. Will be empty if not found.
     * @return automatically generated
     */
    public boolean detectAndDecodeMulti(Mat img, List<String> decoded_info, Mat points) {
        return detectAndDecodeMulti_1(nativeObj, img.nativeObj, decoded_info, points.nativeObj);
    }

    /**
     * Both detects and decodes graphical codes
     *     @param img grayscale or color (BGR) image containing graphical codes.
     *     @param decoded_info UTF8-encoded output vector of string or empty vector of string if the codes cannot be decoded.
     * @return automatically generated
     */
    public boolean detectAndDecodeMulti(Mat img, List<String> decoded_info) {
        return detectAndDecodeMulti_2(nativeObj, img.nativeObj, decoded_info);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  bool cv::GraphicalCodeDetector::detect(Mat img, Mat& points)
    private static native boolean detect_0(long nativeObj, long img_nativeObj, long points_nativeObj);

    // C++:  string cv::GraphicalCodeDetector::decode(Mat img, Mat points, Mat& straight_code = Mat())
    private static native String decode_0(long nativeObj, long img_nativeObj, long points_nativeObj, long straight_code_nativeObj);
    private static native String decode_1(long nativeObj, long img_nativeObj, long points_nativeObj);

    // C++:  string cv::GraphicalCodeDetector::detectAndDecode(Mat img, Mat& points = Mat(), Mat& straight_code = Mat())
    private static native String detectAndDecode_0(long nativeObj, long img_nativeObj, long points_nativeObj, long straight_code_nativeObj);
    private static native String detectAndDecode_1(long nativeObj, long img_nativeObj, long points_nativeObj);
    private static native String detectAndDecode_2(long nativeObj, long img_nativeObj);

    // C++:  bool cv::GraphicalCodeDetector::detectMulti(Mat img, Mat& points)
    private static native boolean detectMulti_0(long nativeObj, long img_nativeObj, long points_nativeObj);

    // C++:  bool cv::GraphicalCodeDetector::decodeMulti(Mat img, Mat points, vector_string& decoded_info, vector_Mat& straight_code = vector_Mat())
    private static native boolean decodeMulti_0(long nativeObj, long img_nativeObj, long points_nativeObj, List<String> decoded_info, long straight_code_mat_nativeObj);
    private static native boolean decodeMulti_1(long nativeObj, long img_nativeObj, long points_nativeObj, List<String> decoded_info);

    // C++:  bool cv::GraphicalCodeDetector::detectAndDecodeMulti(Mat img, vector_string& decoded_info, Mat& points = Mat(), vector_Mat& straight_code = vector_Mat())
    private static native boolean detectAndDecodeMulti_0(long nativeObj, long img_nativeObj, List<String> decoded_info, long points_nativeObj, long straight_code_mat_nativeObj);
    private static native boolean detectAndDecodeMulti_1(long nativeObj, long img_nativeObj, List<String> decoded_info, long points_nativeObj);
    private static native boolean detectAndDecodeMulti_2(long nativeObj, long img_nativeObj, List<String> decoded_info);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
