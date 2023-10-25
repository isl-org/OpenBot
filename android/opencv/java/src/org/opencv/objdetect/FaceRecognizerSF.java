//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import org.opencv.core.Mat;
import org.opencv.objdetect.FaceRecognizerSF;

// C++: class FaceRecognizerSF
/**
 * DNN-based face recognizer
 *
 * model download link: https://github.com/opencv/opencv_zoo/tree/master/models/face_recognition_sface
 */
public class FaceRecognizerSF {

    protected final long nativeObj;
    protected FaceRecognizerSF(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static FaceRecognizerSF __fromPtr__(long addr) { return new FaceRecognizerSF(addr); }

    // C++: enum DisType (cv.FaceRecognizerSF.DisType)
    public static final int
            FR_COSINE = 0,
            FR_NORM_L2 = 1;


    //
    // C++:  void cv::FaceRecognizerSF::alignCrop(Mat src_img, Mat face_box, Mat& aligned_img)
    //

    /**
     * Aligning image to put face on the standard position
     * @param src_img input image
     * @param face_box the detection result used for indicate face in input image
     * @param aligned_img output aligned image
     */
    public void alignCrop(Mat src_img, Mat face_box, Mat aligned_img) {
        alignCrop_0(nativeObj, src_img.nativeObj, face_box.nativeObj, aligned_img.nativeObj);
    }


    //
    // C++:  void cv::FaceRecognizerSF::feature(Mat aligned_img, Mat& face_feature)
    //

    /**
     * Extracting face feature from aligned image
     * @param aligned_img input aligned image
     * @param face_feature output face feature
     */
    public void feature(Mat aligned_img, Mat face_feature) {
        feature_0(nativeObj, aligned_img.nativeObj, face_feature.nativeObj);
    }


    //
    // C++:  double cv::FaceRecognizerSF::match(Mat face_feature1, Mat face_feature2, int dis_type = FaceRecognizerSF::FR_COSINE)
    //

    /**
     * Calculating the distance between two face features
     * @param face_feature1 the first input feature
     * @param face_feature2 the second input feature of the same size and the same type as face_feature1
     * @param dis_type defining the similarity with optional values "FR_OSINE" or "FR_NORM_L2"
     * @return automatically generated
     */
    public double match(Mat face_feature1, Mat face_feature2, int dis_type) {
        return match_0(nativeObj, face_feature1.nativeObj, face_feature2.nativeObj, dis_type);
    }

    /**
     * Calculating the distance between two face features
     * @param face_feature1 the first input feature
     * @param face_feature2 the second input feature of the same size and the same type as face_feature1
     * @return automatically generated
     */
    public double match(Mat face_feature1, Mat face_feature2) {
        return match_1(nativeObj, face_feature1.nativeObj, face_feature2.nativeObj);
    }


    //
    // C++: static Ptr_FaceRecognizerSF cv::FaceRecognizerSF::create(String model, String config, int backend_id = 0, int target_id = 0)
    //

    /**
     * Creates an instance of this class with given parameters
     * @param model the path of the onnx model used for face recognition
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @param backend_id the id of backend
     * @param target_id the id of target device
     * @return automatically generated
     */
    public static FaceRecognizerSF create(String model, String config, int backend_id, int target_id) {
        return FaceRecognizerSF.__fromPtr__(create_0(model, config, backend_id, target_id));
    }

    /**
     * Creates an instance of this class with given parameters
     * @param model the path of the onnx model used for face recognition
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @param backend_id the id of backend
     * @return automatically generated
     */
    public static FaceRecognizerSF create(String model, String config, int backend_id) {
        return FaceRecognizerSF.__fromPtr__(create_1(model, config, backend_id));
    }

    /**
     * Creates an instance of this class with given parameters
     * @param model the path of the onnx model used for face recognition
     * @param config the path to the config file for compability, which is not requested for ONNX models
     * @return automatically generated
     */
    public static FaceRecognizerSF create(String model, String config) {
        return FaceRecognizerSF.__fromPtr__(create_2(model, config));
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  void cv::FaceRecognizerSF::alignCrop(Mat src_img, Mat face_box, Mat& aligned_img)
    private static native void alignCrop_0(long nativeObj, long src_img_nativeObj, long face_box_nativeObj, long aligned_img_nativeObj);

    // C++:  void cv::FaceRecognizerSF::feature(Mat aligned_img, Mat& face_feature)
    private static native void feature_0(long nativeObj, long aligned_img_nativeObj, long face_feature_nativeObj);

    // C++:  double cv::FaceRecognizerSF::match(Mat face_feature1, Mat face_feature2, int dis_type = FaceRecognizerSF::FR_COSINE)
    private static native double match_0(long nativeObj, long face_feature1_nativeObj, long face_feature2_nativeObj, int dis_type);
    private static native double match_1(long nativeObj, long face_feature1_nativeObj, long face_feature2_nativeObj);

    // C++: static Ptr_FaceRecognizerSF cv::FaceRecognizerSF::create(String model, String config, int backend_id = 0, int target_id = 0)
    private static native long create_0(String model, String config, int backend_id, int target_id);
    private static native long create_1(String model, String config, int backend_id);
    private static native long create_2(String model, String config);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
