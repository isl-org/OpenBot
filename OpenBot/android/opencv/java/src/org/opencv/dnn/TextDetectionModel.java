//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRotatedRect;
import org.opencv.dnn.Model;
import org.opencv.utils.Converters;

// C++: class TextDetectionModel
/**
 * Base class for text detection networks
 */
public class TextDetectionModel extends Model {

    protected TextDetectionModel(long addr) { super(addr); }

    // internal usage only
    public static TextDetectionModel __fromPtr__(long addr) { return new TextDetectionModel(addr); }

    //
    // C++:  void cv::dnn::TextDetectionModel::detect(Mat frame, vector_vector_Point& detections, vector_float& confidences)
    //

    /**
     * Performs detection
     *
     * Given the input {@code frame}, prepare network input, run network inference, post-process network output and return result detections.
     *
     * Each result is quadrangle's 4 points in this order:
     * - bottom-left
     * - top-left
     * - top-right
     * - bottom-right
     *
     * Use cv::getPerspectiveTransform function to retrieve image region without perspective transformations.
     *
     * <b>Note:</b> If DL model doesn't support that kind of output then result may be derived from detectTextRectangles() output.
     *
     * @param frame The input image
     * @param detections array with detections' quadrangles (4 points per result)
     * @param confidences array with detection confidences
     */
    public void detect(Mat frame, List<MatOfPoint> detections, MatOfFloat confidences) {
        Mat detections_mat = new Mat();
        Mat confidences_mat = confidences;
        detect_0(nativeObj, frame.nativeObj, detections_mat.nativeObj, confidences_mat.nativeObj);
        Converters.Mat_to_vector_vector_Point(detections_mat, detections);
        detections_mat.release();
    }


    //
    // C++:  void cv::dnn::TextDetectionModel::detect(Mat frame, vector_vector_Point& detections)
    //

    public void detect(Mat frame, List<MatOfPoint> detections) {
        Mat detections_mat = new Mat();
        detect_1(nativeObj, frame.nativeObj, detections_mat.nativeObj);
        Converters.Mat_to_vector_vector_Point(detections_mat, detections);
        detections_mat.release();
    }


    //
    // C++:  void cv::dnn::TextDetectionModel::detectTextRectangles(Mat frame, vector_RotatedRect& detections, vector_float& confidences)
    //

    /**
     * Performs detection
     *
     * Given the input {@code frame}, prepare network input, run network inference, post-process network output and return result detections.
     *
     * Each result is rotated rectangle.
     *
     * <b>Note:</b> Result may be inaccurate in case of strong perspective transformations.
     *
     * @param frame the input image
     * @param detections array with detections' RotationRect results
     * @param confidences array with detection confidences
     */
    public void detectTextRectangles(Mat frame, MatOfRotatedRect detections, MatOfFloat confidences) {
        Mat detections_mat = detections;
        Mat confidences_mat = confidences;
        detectTextRectangles_0(nativeObj, frame.nativeObj, detections_mat.nativeObj, confidences_mat.nativeObj);
    }


    //
    // C++:  void cv::dnn::TextDetectionModel::detectTextRectangles(Mat frame, vector_RotatedRect& detections)
    //

    public void detectTextRectangles(Mat frame, MatOfRotatedRect detections) {
        Mat detections_mat = detections;
        detectTextRectangles_1(nativeObj, frame.nativeObj, detections_mat.nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  void cv::dnn::TextDetectionModel::detect(Mat frame, vector_vector_Point& detections, vector_float& confidences)
    private static native void detect_0(long nativeObj, long frame_nativeObj, long detections_mat_nativeObj, long confidences_mat_nativeObj);

    // C++:  void cv::dnn::TextDetectionModel::detect(Mat frame, vector_vector_Point& detections)
    private static native void detect_1(long nativeObj, long frame_nativeObj, long detections_mat_nativeObj);

    // C++:  void cv::dnn::TextDetectionModel::detectTextRectangles(Mat frame, vector_RotatedRect& detections, vector_float& confidences)
    private static native void detectTextRectangles_0(long nativeObj, long frame_nativeObj, long detections_mat_nativeObj, long confidences_mat_nativeObj);

    // C++:  void cv::dnn::TextDetectionModel::detectTextRectangles(Mat frame, vector_RotatedRect& detections)
    private static native void detectTextRectangles_1(long nativeObj, long frame_nativeObj, long detections_mat_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
