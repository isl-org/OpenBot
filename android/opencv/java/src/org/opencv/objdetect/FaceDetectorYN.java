//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.objdetect.FaceDetectorYN;

// C++: class FaceDetectorYN
/**
 * DNN-based face detector
 *
 * model download link: https://github.com/opencv/opencv_zoo/tree/master/models/face_detection_yunet
 */
public class FaceDetectorYN {

    protected final long nativeObj;
    protected FaceDetectorYN(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static FaceDetectorYN __fromPtr__(long addr) { return new FaceDetectorYN(addr); }

    //
    // C++:  void cv::FaceDetectorYN::setInputSize(Size input_size)
    //

    /**
     * Set the size for the network input, which overwrites the input size of creating model. Call this method when the size of input image does not match the input size when creating model
     *
     * @param input_size the size of the input image
     */
    public void setInputSize(Size input_size) {
        setInputSize_0(nativeObj, input_size.width, input_size.height);
    }


    //
    // C++:  Size cv::FaceDetectorYN::getInputSize()
    //

    public Size getInputSize() {
        return new Size(getInputSize_0(nativeObj));
    }


    //
    // C++:  void cv::FaceDetectorYN::setScoreThreshold(float score_threshold)
    //

    /**
     * Set the score threshold to filter out bounding boxes of score less than the given value
     *
     * @param score_threshold threshold for filtering out bounding boxes
     */
    public void setScoreThreshold(float score_threshold) {
        setScoreThreshold_0(nativeObj, score_threshold);
    }


    //
    // C++:  float cv::FaceDetectorYN::getScoreThreshold()
    //

    public float getScoreThreshold() {
        return getScoreThreshold_0(nativeObj);
    }


    //
    // C++:  void cv::FaceDetectorYN::setNMSThreshold(float nms_threshold)
    //

    /**
     * Set the Non-maximum-suppression threshold to suppress bounding boxes that have IoU greater than the given value
     *
     * @param nms_threshold threshold for NMS operation
     */
    public void setNMSThreshold(float nms_threshold) {
        setNMSThreshold_0(nativeObj, nms_threshold);
    }


    //
    // C++:  float cv::FaceDetectorYN::getNMSThreshold()
    //

    public float getNMSThreshold() {
        return getNMSThreshold_0(nativeObj);
    }


    //
    // C++:  void cv::FaceDetectorYN::setTopK(int top_k)
    //

    /**
     * Set the number of bounding boxes preserved before NMS
     *
     * @param top_k the number of bounding boxes to preserve from top rank based on score
     */
    public void setTopK(int top_k) {
        setTopK_0(nativeObj, top_k);
    }


    //
    // C++:  int cv::FaceDetectorYN::getTopK()
    //

    public int getTopK() {
        return getTopK_0(nativeObj);
    }


    //
    // C++:  int cv::FaceDetectorYN::detect(Mat image, Mat& faces)
    //

    /**
     * Detects faces in the input image. Following is an example output.
     *
     * ![image](pics/lena-face-detection.jpg)
     *
     * @param image an image to detect
     * @param faces detection results stored in a 2D cv::Mat of shape [num_faces, 15]
     * - 0-1: x, y of bbox top left corner
     * - 2-3: width, height of bbox
     * - 4-5: x, y of right eye (blue point in the example image)
     * - 6-7: x, y of left eye (red point in the example image)
     * - 8-9: x, y of nose tip (green point in the example image)
     * - 10-11: x, y of right corner of mouth (pink point in the example image)
     * - 12-13: x, y of left corner of mouth (yellow point in the example image)
     * - 14: face score
     * @return automatically generated
     */
    public int detect(Mat image, Mat faces) {
        return detect_0(nativeObj, image.nativeObj, faces.nativeObj);
    }


    //
    // C++: static Ptr_FaceDetectorYN cv::FaceDetectorYN::create(String model, String config, Size input_size, float score_threshold = 0.9f, float nms_threshold = 0.3f, int top_k = 5000, int backend_id = 0, int target_id = 0)
    //

    /**
     * Creates an instance of this class with given parameters
     *
     * @param model the path to the requested model
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @param input_size the size of the input image
     * @param score_threshold the threshold to filter out bounding boxes of score smaller than the given value
     * @param nms_threshold the threshold to suppress bounding boxes of IoU bigger than the given value
     * @param top_k keep top K bboxes before NMS
     * @param backend_id the id of backend
     * @param target_id the id of target device
     * @return automatically generated
     */
    public static FaceDetectorYN create(String model, String config, Size input_size, float score_threshold, float nms_threshold, int top_k, int backend_id, int target_id) {
        return FaceDetectorYN.__fromPtr__(create_0(model, config, input_size.width, input_size.height, score_threshold, nms_threshold, top_k, backend_id, target_id));
    }

    /**
     * Creates an instance of this class with given parameters
     *
     * @param model the path to the requested model
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @param input_size the size of the input image
     * @param score_threshold the threshold to filter out bounding boxes of score smaller than the given value
     * @param nms_threshold the threshold to suppress bounding boxes of IoU bigger than the given value
     * @param top_k keep top K bboxes before NMS
     * @param backend_id the id of backend
     * @return automatically generated
     */
    public static FaceDetectorYN create(String model, String config, Size input_size, float score_threshold, float nms_threshold, int top_k, int backend_id) {
        return FaceDetectorYN.__fromPtr__(create_1(model, config, input_size.width, input_size.height, score_threshold, nms_threshold, top_k, backend_id));
    }

    /**
     * Creates an instance of this class with given parameters
     *
     * @param model the path to the requested model
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @param input_size the size of the input image
     * @param score_threshold the threshold to filter out bounding boxes of score smaller than the given value
     * @param nms_threshold the threshold to suppress bounding boxes of IoU bigger than the given value
     * @param top_k keep top K bboxes before NMS
     * @return automatically generated
     */
    public static FaceDetectorYN create(String model, String config, Size input_size, float score_threshold, float nms_threshold, int top_k) {
        return FaceDetectorYN.__fromPtr__(create_2(model, config, input_size.width, input_size.height, score_threshold, nms_threshold, top_k));
    }

    /**
     * Creates an instance of this class with given parameters
     *
     * @param model the path to the requested model
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @param input_size the size of the input image
     * @param score_threshold the threshold to filter out bounding boxes of score smaller than the given value
     * @param nms_threshold the threshold to suppress bounding boxes of IoU bigger than the given value
     * @return automatically generated
     */
    public static FaceDetectorYN create(String model, String config, Size input_size, float score_threshold, float nms_threshold) {
        return FaceDetectorYN.__fromPtr__(create_3(model, config, input_size.width, input_size.height, score_threshold, nms_threshold));
    }

    /**
     * Creates an instance of this class with given parameters
     *
     * @param model the path to the requested model
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @param input_size the size of the input image
     * @param score_threshold the threshold to filter out bounding boxes of score smaller than the given value
     * @return automatically generated
     */
    public static FaceDetectorYN create(String model, String config, Size input_size, float score_threshold) {
        return FaceDetectorYN.__fromPtr__(create_4(model, config, input_size.width, input_size.height, score_threshold));
    }

    /**
     * Creates an instance of this class with given parameters
     *
     * @param model the path to the requested model
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @param input_size the size of the input image
     * @return automatically generated
     */
    public static FaceDetectorYN create(String model, String config, Size input_size) {
        return FaceDetectorYN.__fromPtr__(create_5(model, config, input_size.width, input_size.height));
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  void cv::FaceDetectorYN::setInputSize(Size input_size)
    private static native void setInputSize_0(long nativeObj, double input_size_width, double input_size_height);

    // C++:  Size cv::FaceDetectorYN::getInputSize()
    private static native double[] getInputSize_0(long nativeObj);

    // C++:  void cv::FaceDetectorYN::setScoreThreshold(float score_threshold)
    private static native void setScoreThreshold_0(long nativeObj, float score_threshold);

    // C++:  float cv::FaceDetectorYN::getScoreThreshold()
    private static native float getScoreThreshold_0(long nativeObj);

    // C++:  void cv::FaceDetectorYN::setNMSThreshold(float nms_threshold)
    private static native void setNMSThreshold_0(long nativeObj, float nms_threshold);

    // C++:  float cv::FaceDetectorYN::getNMSThreshold()
    private static native float getNMSThreshold_0(long nativeObj);

    // C++:  void cv::FaceDetectorYN::setTopK(int top_k)
    private static native void setTopK_0(long nativeObj, int top_k);

    // C++:  int cv::FaceDetectorYN::getTopK()
    private static native int getTopK_0(long nativeObj);

    // C++:  int cv::FaceDetectorYN::detect(Mat image, Mat& faces)
    private static native int detect_0(long nativeObj, long image_nativeObj, long faces_nativeObj);

    // C++: static Ptr_FaceDetectorYN cv::FaceDetectorYN::create(String model, String config, Size input_size, float score_threshold = 0.9f, float nms_threshold = 0.3f, int top_k = 5000, int backend_id = 0, int target_id = 0)
    private static native long create_0(String model, String config, double input_size_width, double input_size_height, float score_threshold, float nms_threshold, int top_k, int backend_id, int target_id);
    private static native long create_1(String model, String config, double input_size_width, double input_size_height, float score_threshold, float nms_threshold, int top_k, int backend_id);
    private static native long create_2(String model, String config, double input_size_width, double input_size_height, float score_threshold, float nms_threshold, int top_k);
    private static native long create_3(String model, String config, double input_size_width, double input_size_height, float score_threshold, float nms_threshold);
    private static native long create_4(String model, String config, double input_size_width, double input_size_height, float score_threshold);
    private static native long create_5(String model, String config, double input_size_width, double input_size_height);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
