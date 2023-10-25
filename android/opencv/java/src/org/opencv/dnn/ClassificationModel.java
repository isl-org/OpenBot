//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import org.opencv.core.Mat;
import org.opencv.dnn.ClassificationModel;
import org.opencv.dnn.Model;
import org.opencv.dnn.Net;

// C++: class ClassificationModel
/**
 * This class represents high-level API for classification models.
 *
 * ClassificationModel allows to set params for preprocessing input image.
 * ClassificationModel creates net from file with trained weights and config,
 * sets preprocessing input, runs forward pass and return top-1 prediction.
 */
public class ClassificationModel extends Model {

    protected ClassificationModel(long addr) { super(addr); }

    // internal usage only
    public static ClassificationModel __fromPtr__(long addr) { return new ClassificationModel(addr); }

    //
    // C++:   cv::dnn::ClassificationModel::ClassificationModel(String model, String config = "")
    //

    /**
     * Create classification model from network represented in one of the supported formats.
     * An order of {@code model} and {@code config} arguments does not matter.
     * @param model Binary file contains trained weights.
     * @param config Text file contains network configuration.
     */
    public ClassificationModel(String model, String config) {
        super(ClassificationModel_0(model, config));
    }

    /**
     * Create classification model from network represented in one of the supported formats.
     * An order of {@code model} and {@code config} arguments does not matter.
     * @param model Binary file contains trained weights.
     */
    public ClassificationModel(String model) {
        super(ClassificationModel_1(model));
    }


    //
    // C++:   cv::dnn::ClassificationModel::ClassificationModel(Net network)
    //

    /**
     * Create model from deep learning network.
     * @param network Net object.
     */
    public ClassificationModel(Net network) {
        super(ClassificationModel_2(network.nativeObj));
    }


    //
    // C++:  ClassificationModel cv::dnn::ClassificationModel::setEnableSoftmaxPostProcessing(bool enable)
    //

    /**
     * Set enable/disable softmax post processing option.
     *
     * If this option is true, softmax is applied after forward inference within the classify() function
     * to convert the confidences range to [0.0-1.0].
     * This function allows you to toggle this behavior.
     * Please turn true when not contain softmax layer in model.
     * @param enable Set enable softmax post processing within the classify() function.
     * @return automatically generated
     */
    public ClassificationModel setEnableSoftmaxPostProcessing(boolean enable) {
        return new ClassificationModel(setEnableSoftmaxPostProcessing_0(nativeObj, enable));
    }


    //
    // C++:  bool cv::dnn::ClassificationModel::getEnableSoftmaxPostProcessing()
    //

    /**
     * Get enable/disable softmax post processing option.
     *
     * This option defaults to false, softmax post processing is not applied within the classify() function.
     * @return automatically generated
     */
    public boolean getEnableSoftmaxPostProcessing() {
        return getEnableSoftmaxPostProcessing_0(nativeObj);
    }


    //
    // C++:  void cv::dnn::ClassificationModel::classify(Mat frame, int& classId, float& conf)
    //

    public void classify(Mat frame, int[] classId, float[] conf) {
        double[] classId_out = new double[1];
        double[] conf_out = new double[1];
        classify_0(nativeObj, frame.nativeObj, classId_out, conf_out);
        if(classId!=null) classId[0] = (int)classId_out[0];
        if(conf!=null) conf[0] = (float)conf_out[0];
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::dnn::ClassificationModel::ClassificationModel(String model, String config = "")
    private static native long ClassificationModel_0(String model, String config);
    private static native long ClassificationModel_1(String model);

    // C++:   cv::dnn::ClassificationModel::ClassificationModel(Net network)
    private static native long ClassificationModel_2(long network_nativeObj);

    // C++:  ClassificationModel cv::dnn::ClassificationModel::setEnableSoftmaxPostProcessing(bool enable)
    private static native long setEnableSoftmaxPostProcessing_0(long nativeObj, boolean enable);

    // C++:  bool cv::dnn::ClassificationModel::getEnableSoftmaxPostProcessing()
    private static native boolean getEnableSoftmaxPostProcessing_0(long nativeObj);

    // C++:  void cv::dnn::ClassificationModel::classify(Mat frame, int& classId, float& conf)
    private static native void classify_0(long nativeObj, long frame_nativeObj, double[] classId_out, double[] conf_out);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
