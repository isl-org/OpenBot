//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;

// C++: class QRCodeDetector

public class QRCodeDetector extends GraphicalCodeDetector {

    protected QRCodeDetector(long addr) { super(addr); }

    // internal usage only
    public static QRCodeDetector __fromPtr__(long addr) { return new QRCodeDetector(addr); }

    //
    // C++:   cv::QRCodeDetector::QRCodeDetector()
    //

    public QRCodeDetector() {
        super(QRCodeDetector_0());
    }


    //
    // C++:  QRCodeDetector cv::QRCodeDetector::setEpsX(double epsX)
    //

    /**
     * sets the epsilon used during the horizontal scan of QR code stop marker detection.
     *      @param epsX Epsilon neighborhood, which allows you to determine the horizontal pattern
     *      of the scheme 1:1:3:1:1 according to QR code standard.
     * @return automatically generated
     */
    public QRCodeDetector setEpsX(double epsX) {
        return new QRCodeDetector(setEpsX_0(nativeObj, epsX));
    }


    //
    // C++:  QRCodeDetector cv::QRCodeDetector::setEpsY(double epsY)
    //

    /**
     * sets the epsilon used during the vertical scan of QR code stop marker detection.
     *      @param epsY Epsilon neighborhood, which allows you to determine the vertical pattern
     *      of the scheme 1:1:3:1:1 according to QR code standard.
     * @return automatically generated
     */
    public QRCodeDetector setEpsY(double epsY) {
        return new QRCodeDetector(setEpsY_0(nativeObj, epsY));
    }


    //
    // C++:  QRCodeDetector cv::QRCodeDetector::setUseAlignmentMarkers(bool useAlignmentMarkers)
    //

    /**
     * use markers to improve the position of the corners of the QR code
     *
     * alignmentMarkers using by default
     * @param useAlignmentMarkers automatically generated
     * @return automatically generated
     */
    public QRCodeDetector setUseAlignmentMarkers(boolean useAlignmentMarkers) {
        return new QRCodeDetector(setUseAlignmentMarkers_0(nativeObj, useAlignmentMarkers));
    }


    //
    // C++:  String cv::QRCodeDetector::decodeCurved(Mat img, Mat points, Mat& straight_qrcode = Mat())
    //

    /**
     * Decodes QR code on a curved surface in image once it's found by the detect() method.
     *
     *      Returns UTF8-encoded output string or empty string if the code cannot be decoded.
     *      @param img grayscale or color (BGR) image containing QR code.
     *      @param points Quadrangle vertices found by detect() method (or some other algorithm).
     *      @param straight_qrcode The optional output image containing rectified and binarized QR code
     * @return automatically generated
     */
    public String decodeCurved(Mat img, Mat points, Mat straight_qrcode) {
        return decodeCurved_0(nativeObj, img.nativeObj, points.nativeObj, straight_qrcode.nativeObj);
    }

    /**
     * Decodes QR code on a curved surface in image once it's found by the detect() method.
     *
     *      Returns UTF8-encoded output string or empty string if the code cannot be decoded.
     *      @param img grayscale or color (BGR) image containing QR code.
     *      @param points Quadrangle vertices found by detect() method (or some other algorithm).
     * @return automatically generated
     */
    public String decodeCurved(Mat img, Mat points) {
        return decodeCurved_1(nativeObj, img.nativeObj, points.nativeObj);
    }


    //
    // C++:  string cv::QRCodeDetector::detectAndDecodeCurved(Mat img, Mat& points = Mat(), Mat& straight_qrcode = Mat())
    //

    /**
     * Both detects and decodes QR code on a curved surface
     *
     *      @param img grayscale or color (BGR) image containing QR code.
     *      @param points optional output array of vertices of the found QR code quadrangle. Will be empty if not found.
     *      @param straight_qrcode The optional output image containing rectified and binarized QR code
     * @return automatically generated
     */
    public String detectAndDecodeCurved(Mat img, Mat points, Mat straight_qrcode) {
        return detectAndDecodeCurved_0(nativeObj, img.nativeObj, points.nativeObj, straight_qrcode.nativeObj);
    }

    /**
     * Both detects and decodes QR code on a curved surface
     *
     *      @param img grayscale or color (BGR) image containing QR code.
     *      @param points optional output array of vertices of the found QR code quadrangle. Will be empty if not found.
     * @return automatically generated
     */
    public String detectAndDecodeCurved(Mat img, Mat points) {
        return detectAndDecodeCurved_1(nativeObj, img.nativeObj, points.nativeObj);
    }

    /**
     * Both detects and decodes QR code on a curved surface
     *
     *      @param img grayscale or color (BGR) image containing QR code.
     * @return automatically generated
     */
    public String detectAndDecodeCurved(Mat img) {
        return detectAndDecodeCurved_2(nativeObj, img.nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::QRCodeDetector::QRCodeDetector()
    private static native long QRCodeDetector_0();

    // C++:  QRCodeDetector cv::QRCodeDetector::setEpsX(double epsX)
    private static native long setEpsX_0(long nativeObj, double epsX);

    // C++:  QRCodeDetector cv::QRCodeDetector::setEpsY(double epsY)
    private static native long setEpsY_0(long nativeObj, double epsY);

    // C++:  QRCodeDetector cv::QRCodeDetector::setUseAlignmentMarkers(bool useAlignmentMarkers)
    private static native long setUseAlignmentMarkers_0(long nativeObj, boolean useAlignmentMarkers);

    // C++:  String cv::QRCodeDetector::decodeCurved(Mat img, Mat points, Mat& straight_qrcode = Mat())
    private static native String decodeCurved_0(long nativeObj, long img_nativeObj, long points_nativeObj, long straight_qrcode_nativeObj);
    private static native String decodeCurved_1(long nativeObj, long img_nativeObj, long points_nativeObj);

    // C++:  string cv::QRCodeDetector::detectAndDecodeCurved(Mat img, Mat& points = Mat(), Mat& straight_qrcode = Mat())
    private static native String detectAndDecodeCurved_0(long nativeObj, long img_nativeObj, long points_nativeObj, long straight_qrcode_nativeObj);
    private static native String detectAndDecodeCurved_1(long nativeObj, long img_nativeObj, long points_nativeObj);
    private static native String detectAndDecodeCurved_2(long nativeObj, long img_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
