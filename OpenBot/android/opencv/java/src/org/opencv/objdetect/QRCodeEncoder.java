//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeEncoder;
import org.opencv.objdetect.QRCodeEncoder_Params;
import org.opencv.utils.Converters;

// C++: class QRCodeEncoder
/**
 * Groups the object candidate rectangles.
 *     rectList  Input/output vector of rectangles. Output vector includes retained and grouped rectangles. (The Python list is not modified in place.)
 *     weights Input/output vector of weights of rectangles. Output vector includes weights of retained and grouped rectangles. (The Python list is not modified in place.)
 *     groupThreshold Minimum possible number of rectangles minus 1. The threshold is used in a group of rectangles to retain it.
 *     eps Relative difference between sides of the rectangles to merge them into a group.
 */
public class QRCodeEncoder {

    protected final long nativeObj;
    protected QRCodeEncoder(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static QRCodeEncoder __fromPtr__(long addr) { return new QRCodeEncoder(addr); }

    // C++: enum CorrectionLevel (cv.QRCodeEncoder.CorrectionLevel)
    public static final int
            CORRECT_LEVEL_L = 0,
            CORRECT_LEVEL_M = 1,
            CORRECT_LEVEL_Q = 2,
            CORRECT_LEVEL_H = 3;


    // C++: enum ECIEncodings (cv.QRCodeEncoder.ECIEncodings)
    public static final int
            ECI_UTF8 = 26;


    // C++: enum EncodeMode (cv.QRCodeEncoder.EncodeMode)
    public static final int
            MODE_AUTO = -1,
            MODE_NUMERIC = 1,
            MODE_ALPHANUMERIC = 2,
            MODE_BYTE = 4,
            MODE_ECI = 7,
            MODE_KANJI = 8,
            MODE_STRUCTURED_APPEND = 3;


    //
    // C++: static Ptr_QRCodeEncoder cv::QRCodeEncoder::create(QRCodeEncoder_Params parameters = QRCodeEncoder::Params())
    //

    /**
     * Constructor
     *     @param parameters QR code encoder parameters QRCodeEncoder::Params
     * @return automatically generated
     */
    public static QRCodeEncoder create(QRCodeEncoder_Params parameters) {
        return QRCodeEncoder.__fromPtr__(create_0(parameters.nativeObj));
    }

    /**
     * Constructor
     * @return automatically generated
     */
    public static QRCodeEncoder create() {
        return QRCodeEncoder.__fromPtr__(create_1());
    }


    //
    // C++:  void cv::QRCodeEncoder::encode(String encoded_info, Mat& qrcode)
    //

    /**
     * Generates QR code from input string.
     *      @param encoded_info Input string to encode.
     *      @param qrcode Generated QR code.
     */
    public void encode(String encoded_info, Mat qrcode) {
        encode_0(nativeObj, encoded_info, qrcode.nativeObj);
    }


    //
    // C++:  void cv::QRCodeEncoder::encodeStructuredAppend(String encoded_info, vector_Mat& qrcodes)
    //

    /**
     * Generates QR code from input string in Structured Append mode. The encoded message is splitting over a number of QR codes.
     *      @param encoded_info Input string to encode.
     *      @param qrcodes Vector of generated QR codes.
     */
    public void encodeStructuredAppend(String encoded_info, List<Mat> qrcodes) {
        Mat qrcodes_mat = new Mat();
        encodeStructuredAppend_0(nativeObj, encoded_info, qrcodes_mat.nativeObj);
        Converters.Mat_to_vector_Mat(qrcodes_mat, qrcodes);
        qrcodes_mat.release();
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_QRCodeEncoder cv::QRCodeEncoder::create(QRCodeEncoder_Params parameters = QRCodeEncoder::Params())
    private static native long create_0(long parameters_nativeObj);
    private static native long create_1();

    // C++:  void cv::QRCodeEncoder::encode(String encoded_info, Mat& qrcode)
    private static native void encode_0(long nativeObj, String encoded_info, long qrcode_nativeObj);

    // C++:  void cv::QRCodeEncoder::encodeStructuredAppend(String encoded_info, vector_Mat& qrcodes)
    private static native void encodeStructuredAppend_0(long nativeObj, String encoded_info, long qrcodes_mat_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
