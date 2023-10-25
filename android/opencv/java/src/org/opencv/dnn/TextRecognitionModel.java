//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.dnn.Model;
import org.opencv.dnn.Net;
import org.opencv.dnn.TextRecognitionModel;
import org.opencv.utils.Converters;

// C++: class TextRecognitionModel
/**
 * This class represents high-level API for text recognition networks.
 *
 * TextRecognitionModel allows to set params for preprocessing input image.
 * TextRecognitionModel creates net from file with trained weights and config,
 * sets preprocessing input, runs forward pass and return recognition result.
 * For TextRecognitionModel, CRNN-CTC is supported.
 */
public class TextRecognitionModel extends Model {

    protected TextRecognitionModel(long addr) { super(addr); }

    // internal usage only
    public static TextRecognitionModel __fromPtr__(long addr) { return new TextRecognitionModel(addr); }

    //
    // C++:   cv::dnn::TextRecognitionModel::TextRecognitionModel(Net network)
    //

    /**
     * Create Text Recognition model from deep learning network
     * Call setDecodeType() and setVocabulary() after constructor to initialize the decoding method
     * @param network Net object
     */
    public TextRecognitionModel(Net network) {
        super(TextRecognitionModel_0(network.nativeObj));
    }


    //
    // C++:   cv::dnn::TextRecognitionModel::TextRecognitionModel(string model, string config = "")
    //

    /**
     * Create text recognition model from network represented in one of the supported formats
     * Call setDecodeType() and setVocabulary() after constructor to initialize the decoding method
     * @param model Binary file contains trained weights
     * @param config Text file contains network configuration
     */
    public TextRecognitionModel(String model, String config) {
        super(TextRecognitionModel_1(model, config));
    }

    /**
     * Create text recognition model from network represented in one of the supported formats
     * Call setDecodeType() and setVocabulary() after constructor to initialize the decoding method
     * @param model Binary file contains trained weights
     */
    public TextRecognitionModel(String model) {
        super(TextRecognitionModel_2(model));
    }


    //
    // C++:  TextRecognitionModel cv::dnn::TextRecognitionModel::setDecodeType(string decodeType)
    //

    /**
     * Set the decoding method of translating the network output into string
     * @param decodeType The decoding method of translating the network output into string, currently supported type:
     * - {@code "CTC-greedy"} greedy decoding for the output of CTC-based methods
     * - {@code "CTC-prefix-beam-search"} Prefix beam search decoding for the output of CTC-based methods
     * @return automatically generated
     */
    public TextRecognitionModel setDecodeType(String decodeType) {
        return new TextRecognitionModel(setDecodeType_0(nativeObj, decodeType));
    }


    //
    // C++:  string cv::dnn::TextRecognitionModel::getDecodeType()
    //

    /**
     * Get the decoding method
     * @return the decoding method
     */
    public String getDecodeType() {
        return getDecodeType_0(nativeObj);
    }


    //
    // C++:  TextRecognitionModel cv::dnn::TextRecognitionModel::setDecodeOptsCTCPrefixBeamSearch(int beamSize, int vocPruneSize = 0)
    //

    /**
     * Set the decoding method options for {@code "CTC-prefix-beam-search"} decode usage
     * @param beamSize Beam size for search
     * @param vocPruneSize Parameter to optimize big vocabulary search,
     * only take top {@code vocPruneSize} tokens in each search step, {@code vocPruneSize} &lt;= 0 stands for disable this prune.
     * @return automatically generated
     */
    public TextRecognitionModel setDecodeOptsCTCPrefixBeamSearch(int beamSize, int vocPruneSize) {
        return new TextRecognitionModel(setDecodeOptsCTCPrefixBeamSearch_0(nativeObj, beamSize, vocPruneSize));
    }

    /**
     * Set the decoding method options for {@code "CTC-prefix-beam-search"} decode usage
     * @param beamSize Beam size for search
     * only take top {@code vocPruneSize} tokens in each search step, {@code vocPruneSize} &lt;= 0 stands for disable this prune.
     * @return automatically generated
     */
    public TextRecognitionModel setDecodeOptsCTCPrefixBeamSearch(int beamSize) {
        return new TextRecognitionModel(setDecodeOptsCTCPrefixBeamSearch_1(nativeObj, beamSize));
    }


    //
    // C++:  TextRecognitionModel cv::dnn::TextRecognitionModel::setVocabulary(vector_string vocabulary)
    //

    /**
     * Set the vocabulary for recognition.
     * @param vocabulary the associated vocabulary of the network.
     * @return automatically generated
     */
    public TextRecognitionModel setVocabulary(List<String> vocabulary) {
        return new TextRecognitionModel(setVocabulary_0(nativeObj, vocabulary));
    }


    //
    // C++:  vector_string cv::dnn::TextRecognitionModel::getVocabulary()
    //

    /**
     * Get the vocabulary for recognition.
     * @return vocabulary the associated vocabulary
     */
    public List<String> getVocabulary() {
        return getVocabulary_0(nativeObj);
    }


    //
    // C++:  string cv::dnn::TextRecognitionModel::recognize(Mat frame)
    //

    /**
     * Given the {@code input} frame, create input blob, run net and return recognition result
     * @param frame The input image
     * @return The text recognition result
     */
    public String recognize(Mat frame) {
        return recognize_0(nativeObj, frame.nativeObj);
    }


    //
    // C++:  void cv::dnn::TextRecognitionModel::recognize(Mat frame, vector_Mat roiRects, vector_string& results)
    //

    /**
     * Given the {@code input} frame, create input blob, run net and return recognition result
     * @param frame The input image
     * @param roiRects List of text detection regions of interest (cv::Rect, CV_32SC4). ROIs is be cropped as the network inputs
     * @param results A set of text recognition results.
     */
    public void recognize(Mat frame, List<Mat> roiRects, List<String> results) {
        Mat roiRects_mat = Converters.vector_Mat_to_Mat(roiRects);
        recognize_1(nativeObj, frame.nativeObj, roiRects_mat.nativeObj, results);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::dnn::TextRecognitionModel::TextRecognitionModel(Net network)
    private static native long TextRecognitionModel_0(long network_nativeObj);

    // C++:   cv::dnn::TextRecognitionModel::TextRecognitionModel(string model, string config = "")
    private static native long TextRecognitionModel_1(String model, String config);
    private static native long TextRecognitionModel_2(String model);

    // C++:  TextRecognitionModel cv::dnn::TextRecognitionModel::setDecodeType(string decodeType)
    private static native long setDecodeType_0(long nativeObj, String decodeType);

    // C++:  string cv::dnn::TextRecognitionModel::getDecodeType()
    private static native String getDecodeType_0(long nativeObj);

    // C++:  TextRecognitionModel cv::dnn::TextRecognitionModel::setDecodeOptsCTCPrefixBeamSearch(int beamSize, int vocPruneSize = 0)
    private static native long setDecodeOptsCTCPrefixBeamSearch_0(long nativeObj, int beamSize, int vocPruneSize);
    private static native long setDecodeOptsCTCPrefixBeamSearch_1(long nativeObj, int beamSize);

    // C++:  TextRecognitionModel cv::dnn::TextRecognitionModel::setVocabulary(vector_string vocabulary)
    private static native long setVocabulary_0(long nativeObj, List<String> vocabulary);

    // C++:  vector_string cv::dnn::TextRecognitionModel::getVocabulary()
    private static native List<String> getVocabulary_0(long nativeObj);

    // C++:  string cv::dnn::TextRecognitionModel::recognize(Mat frame)
    private static native String recognize_0(long nativeObj, long frame_nativeObj);

    // C++:  void cv::dnn::TextRecognitionModel::recognize(Mat frame, vector_Mat roiRects, vector_string& results)
    private static native void recognize_1(long nativeObj, long frame_nativeObj, long roiRects_mat_nativeObj, List<String> results);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
