//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.MatOfRect2d;
import org.opencv.core.MatOfRotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Image2BlobParams;
import org.opencv.dnn.Net;
import org.opencv.utils.Converters;

// C++: class Dnn

public class Dnn {

    // C++: enum Backend (cv.dnn.Backend)
    public static final int
            DNN_BACKEND_DEFAULT = 0,
            DNN_BACKEND_HALIDE = 0+1,
            DNN_BACKEND_INFERENCE_ENGINE = 0+2,
            DNN_BACKEND_OPENCV = 0+3,
            DNN_BACKEND_VKCOM = 0+4,
            DNN_BACKEND_CUDA = 0+5,
            DNN_BACKEND_WEBNN = 0+6,
            DNN_BACKEND_TIMVX = 0+7,
            DNN_BACKEND_CANN = 0+8;


    // C++: enum DataLayout (cv.dnn.DataLayout)
    public static final int
            DNN_LAYOUT_UNKNOWN = 0,
            DNN_LAYOUT_ND = 1,
            DNN_LAYOUT_NCHW = 2,
            DNN_LAYOUT_NCDHW = 3,
            DNN_LAYOUT_NHWC = 4,
            DNN_LAYOUT_NDHWC = 5,
            DNN_LAYOUT_PLANAR = 6;


    // C++: enum ImagePaddingMode (cv.dnn.ImagePaddingMode)
    public static final int
            DNN_PMODE_NULL = 0,
            DNN_PMODE_CROP_CENTER = 1,
            DNN_PMODE_LETTERBOX = 2;


    // C++: enum SoftNMSMethod (cv.dnn.SoftNMSMethod)
    public static final int
            SoftNMSMethod_SOFTNMS_LINEAR = 1,
            SoftNMSMethod_SOFTNMS_GAUSSIAN = 2;


    // C++: enum Target (cv.dnn.Target)
    public static final int
            DNN_TARGET_CPU = 0,
            DNN_TARGET_OPENCL = 0+1,
            DNN_TARGET_OPENCL_FP16 = 0+2,
            DNN_TARGET_MYRIAD = 0+3,
            DNN_TARGET_VULKAN = 0+4,
            DNN_TARGET_FPGA = 0+5,
            DNN_TARGET_CUDA = 0+6,
            DNN_TARGET_CUDA_FP16 = 0+7,
            DNN_TARGET_HDDL = 0+8,
            DNN_TARGET_NPU = 0+9,
            DNN_TARGET_CPU_FP16 = 0+10;


    //
    // C++:  vector_Target cv::dnn::getAvailableTargets(dnn_Backend be)
    //

    public static List<Integer> getAvailableTargets(int be) {
        return getAvailableTargets_0(be);
    }


    //
    // C++:  Net cv::dnn::readNetFromDarknet(String cfgFile, String darknetModel = String())
    //

    /**
     * Reads a network model stored in &lt;a href="https://pjreddie.com/darknet/"&gt;Darknet&lt;/a&gt; model files.
     * @param cfgFile      path to the .cfg file with text description of the network architecture.
     * @param darknetModel path to the .weights file with learned network.
     * @return Network object that ready to do forward, throw an exception in failure cases.
     */
    public static Net readNetFromDarknet(String cfgFile, String darknetModel) {
        return new Net(readNetFromDarknet_0(cfgFile, darknetModel));
    }

    /**
     * Reads a network model stored in &lt;a href="https://pjreddie.com/darknet/"&gt;Darknet&lt;/a&gt; model files.
     * @param cfgFile      path to the .cfg file with text description of the network architecture.
     * @return Network object that ready to do forward, throw an exception in failure cases.
     */
    public static Net readNetFromDarknet(String cfgFile) {
        return new Net(readNetFromDarknet_1(cfgFile));
    }


    //
    // C++:  Net cv::dnn::readNetFromDarknet(vector_uchar bufferCfg, vector_uchar bufferModel = std::vector<uchar>())
    //

    /**
     * Reads a network model stored in &lt;a href="https://pjreddie.com/darknet/"&gt;Darknet&lt;/a&gt; model files.
     * @param bufferCfg   A buffer contains a content of .cfg file with text description of the network architecture.
     * @param bufferModel A buffer contains a content of .weights file with learned network.
     * @return Net object.
     */
    public static Net readNetFromDarknet(MatOfByte bufferCfg, MatOfByte bufferModel) {
        Mat bufferCfg_mat = bufferCfg;
        Mat bufferModel_mat = bufferModel;
        return new Net(readNetFromDarknet_2(bufferCfg_mat.nativeObj, bufferModel_mat.nativeObj));
    }

    /**
     * Reads a network model stored in &lt;a href="https://pjreddie.com/darknet/"&gt;Darknet&lt;/a&gt; model files.
     * @param bufferCfg   A buffer contains a content of .cfg file with text description of the network architecture.
     * @return Net object.
     */
    public static Net readNetFromDarknet(MatOfByte bufferCfg) {
        Mat bufferCfg_mat = bufferCfg;
        return new Net(readNetFromDarknet_3(bufferCfg_mat.nativeObj));
    }


    //
    // C++:  Net cv::dnn::readNetFromCaffe(String prototxt, String caffeModel = String())
    //

    /**
     * Reads a network model stored in &lt;a href="http://caffe.berkeleyvision.org"&gt;Caffe&lt;/a&gt; framework's format.
     * @param prototxt   path to the .prototxt file with text description of the network architecture.
     * @param caffeModel path to the .caffemodel file with learned network.
     * @return Net object.
     */
    public static Net readNetFromCaffe(String prototxt, String caffeModel) {
        return new Net(readNetFromCaffe_0(prototxt, caffeModel));
    }

    /**
     * Reads a network model stored in &lt;a href="http://caffe.berkeleyvision.org"&gt;Caffe&lt;/a&gt; framework's format.
     * @param prototxt   path to the .prototxt file with text description of the network architecture.
     * @return Net object.
     */
    public static Net readNetFromCaffe(String prototxt) {
        return new Net(readNetFromCaffe_1(prototxt));
    }


    //
    // C++:  Net cv::dnn::readNetFromCaffe(vector_uchar bufferProto, vector_uchar bufferModel = std::vector<uchar>())
    //

    /**
     * Reads a network model stored in Caffe model in memory.
     * @param bufferProto buffer containing the content of the .prototxt file
     * @param bufferModel buffer containing the content of the .caffemodel file
     * @return Net object.
     */
    public static Net readNetFromCaffe(MatOfByte bufferProto, MatOfByte bufferModel) {
        Mat bufferProto_mat = bufferProto;
        Mat bufferModel_mat = bufferModel;
        return new Net(readNetFromCaffe_2(bufferProto_mat.nativeObj, bufferModel_mat.nativeObj));
    }

    /**
     * Reads a network model stored in Caffe model in memory.
     * @param bufferProto buffer containing the content of the .prototxt file
     * @return Net object.
     */
    public static Net readNetFromCaffe(MatOfByte bufferProto) {
        Mat bufferProto_mat = bufferProto;
        return new Net(readNetFromCaffe_3(bufferProto_mat.nativeObj));
    }


    //
    // C++:  Net cv::dnn::readNetFromTensorflow(String model, String config = String())
    //

    /**
     * Reads a network model stored in &lt;a href="https://www.tensorflow.org/"&gt;TensorFlow&lt;/a&gt; framework's format.
     * @param model  path to the .pb file with binary protobuf description of the network architecture
     * @param config path to the .pbtxt file that contains text graph definition in protobuf format.
     * Resulting Net object is built by text graph using weights from a binary one that
     * let us make it more flexible.
     * @return Net object.
     */
    public static Net readNetFromTensorflow(String model, String config) {
        return new Net(readNetFromTensorflow_0(model, config));
    }

    /**
     * Reads a network model stored in &lt;a href="https://www.tensorflow.org/"&gt;TensorFlow&lt;/a&gt; framework's format.
     * @param model  path to the .pb file with binary protobuf description of the network architecture
     * Resulting Net object is built by text graph using weights from a binary one that
     * let us make it more flexible.
     * @return Net object.
     */
    public static Net readNetFromTensorflow(String model) {
        return new Net(readNetFromTensorflow_1(model));
    }


    //
    // C++:  Net cv::dnn::readNetFromTensorflow(vector_uchar bufferModel, vector_uchar bufferConfig = std::vector<uchar>())
    //

    /**
     * Reads a network model stored in &lt;a href="https://www.tensorflow.org/"&gt;TensorFlow&lt;/a&gt; framework's format.
     * @param bufferModel buffer containing the content of the pb file
     * @param bufferConfig buffer containing the content of the pbtxt file
     * @return Net object.
     */
    public static Net readNetFromTensorflow(MatOfByte bufferModel, MatOfByte bufferConfig) {
        Mat bufferModel_mat = bufferModel;
        Mat bufferConfig_mat = bufferConfig;
        return new Net(readNetFromTensorflow_2(bufferModel_mat.nativeObj, bufferConfig_mat.nativeObj));
    }

    /**
     * Reads a network model stored in &lt;a href="https://www.tensorflow.org/"&gt;TensorFlow&lt;/a&gt; framework's format.
     * @param bufferModel buffer containing the content of the pb file
     * @return Net object.
     */
    public static Net readNetFromTensorflow(MatOfByte bufferModel) {
        Mat bufferModel_mat = bufferModel;
        return new Net(readNetFromTensorflow_3(bufferModel_mat.nativeObj));
    }


    //
    // C++:  Net cv::dnn::readNetFromTFLite(String model)
    //

    /**
     * Reads a network model stored in &lt;a href="https://www.tensorflow.org/lite"&gt;TFLite&lt;/a&gt; framework's format.
     * @param model  path to the .tflite file with binary flatbuffers description of the network architecture
     * @return Net object.
     */
    public static Net readNetFromTFLite(String model) {
        return new Net(readNetFromTFLite_0(model));
    }


    //
    // C++:  Net cv::dnn::readNetFromTFLite(vector_uchar bufferModel)
    //

    /**
     * Reads a network model stored in &lt;a href="https://www.tensorflow.org/lite"&gt;TFLite&lt;/a&gt; framework's format.
     * @param bufferModel buffer containing the content of the tflite file
     * @return Net object.
     */
    public static Net readNetFromTFLite(MatOfByte bufferModel) {
        Mat bufferModel_mat = bufferModel;
        return new Net(readNetFromTFLite_1(bufferModel_mat.nativeObj));
    }


    //
    // C++:  Net cv::dnn::readNetFromTorch(String model, bool isBinary = true, bool evaluate = true)
    //

    /**
     * Reads a network model stored in &lt;a href="http://torch.ch"&gt;Torch7&lt;/a&gt; framework's format.
     * @param model    path to the file, dumped from Torch by using torch.save() function.
     * @param isBinary specifies whether the network was serialized in ascii mode or binary.
     * @param evaluate specifies testing phase of network. If true, it's similar to evaluate() method in Torch.
     * @return Net object.
     *
     * <b>Note:</b> Ascii mode of Torch serializer is more preferable, because binary mode extensively use {@code long} type of C language,
     * which has various bit-length on different systems.
     *
     * The loading file must contain serialized &lt;a href="https://github.com/torch/nn/blob/master/doc/module.md"&gt;nn.Module&lt;/a&gt; object
     * with importing network. Try to eliminate a custom objects from serialazing data to avoid importing errors.
     *
     * List of supported layers (i.e. object instances derived from Torch nn.Module class):
     * - nn.Sequential
     * - nn.Parallel
     * - nn.Concat
     * - nn.Linear
     * - nn.SpatialConvolution
     * - nn.SpatialMaxPooling, nn.SpatialAveragePooling
     * - nn.ReLU, nn.TanH, nn.Sigmoid
     * - nn.Reshape
     * - nn.SoftMax, nn.LogSoftMax
     *
     * Also some equivalents of these classes from cunn, cudnn, and fbcunn may be successfully imported.
     */
    public static Net readNetFromTorch(String model, boolean isBinary, boolean evaluate) {
        return new Net(readNetFromTorch_0(model, isBinary, evaluate));
    }

    /**
     * Reads a network model stored in &lt;a href="http://torch.ch"&gt;Torch7&lt;/a&gt; framework's format.
     * @param model    path to the file, dumped from Torch by using torch.save() function.
     * @param isBinary specifies whether the network was serialized in ascii mode or binary.
     * @return Net object.
     *
     * <b>Note:</b> Ascii mode of Torch serializer is more preferable, because binary mode extensively use {@code long} type of C language,
     * which has various bit-length on different systems.
     *
     * The loading file must contain serialized &lt;a href="https://github.com/torch/nn/blob/master/doc/module.md"&gt;nn.Module&lt;/a&gt; object
     * with importing network. Try to eliminate a custom objects from serialazing data to avoid importing errors.
     *
     * List of supported layers (i.e. object instances derived from Torch nn.Module class):
     * - nn.Sequential
     * - nn.Parallel
     * - nn.Concat
     * - nn.Linear
     * - nn.SpatialConvolution
     * - nn.SpatialMaxPooling, nn.SpatialAveragePooling
     * - nn.ReLU, nn.TanH, nn.Sigmoid
     * - nn.Reshape
     * - nn.SoftMax, nn.LogSoftMax
     *
     * Also some equivalents of these classes from cunn, cudnn, and fbcunn may be successfully imported.
     */
    public static Net readNetFromTorch(String model, boolean isBinary) {
        return new Net(readNetFromTorch_1(model, isBinary));
    }

    /**
     * Reads a network model stored in &lt;a href="http://torch.ch"&gt;Torch7&lt;/a&gt; framework's format.
     * @param model    path to the file, dumped from Torch by using torch.save() function.
     * @return Net object.
     *
     * <b>Note:</b> Ascii mode of Torch serializer is more preferable, because binary mode extensively use {@code long} type of C language,
     * which has various bit-length on different systems.
     *
     * The loading file must contain serialized &lt;a href="https://github.com/torch/nn/blob/master/doc/module.md"&gt;nn.Module&lt;/a&gt; object
     * with importing network. Try to eliminate a custom objects from serialazing data to avoid importing errors.
     *
     * List of supported layers (i.e. object instances derived from Torch nn.Module class):
     * - nn.Sequential
     * - nn.Parallel
     * - nn.Concat
     * - nn.Linear
     * - nn.SpatialConvolution
     * - nn.SpatialMaxPooling, nn.SpatialAveragePooling
     * - nn.ReLU, nn.TanH, nn.Sigmoid
     * - nn.Reshape
     * - nn.SoftMax, nn.LogSoftMax
     *
     * Also some equivalents of these classes from cunn, cudnn, and fbcunn may be successfully imported.
     */
    public static Net readNetFromTorch(String model) {
        return new Net(readNetFromTorch_2(model));
    }


    //
    // C++:  Net cv::dnn::readNet(String model, String config = "", String framework = "")
    //

    /**
     * Read deep learning network represented in one of the supported formats.
     * @param model Binary file contains trained weights. The following file
     * extensions are expected for models from different frameworks:
     * * {@code *.caffemodel} (Caffe, http://caffe.berkeleyvision.org/)
     * * {@code *.pb} (TensorFlow, https://www.tensorflow.org/)
     * * {@code *.t7} | {@code *.net} (Torch, http://torch.ch/)
     * * {@code *.weights} (Darknet, https://pjreddie.com/darknet/)
     * * {@code *.bin} (DLDT, https://software.intel.com/openvino-toolkit)
     * * {@code *.onnx} (ONNX, https://onnx.ai/)
     * @param config Text file contains network configuration. It could be a
     * file with the following extensions:
     * * {@code *.prototxt} (Caffe, http://caffe.berkeleyvision.org/)
     * * {@code *.pbtxt} (TensorFlow, https://www.tensorflow.org/)
     * * {@code *.cfg} (Darknet, https://pjreddie.com/darknet/)
     * * {@code *.xml} (DLDT, https://software.intel.com/openvino-toolkit)
     * @param framework Explicit framework name tag to determine a format.
     * @return Net object.
     *
     * This function automatically detects an origin framework of trained model
     * and calls an appropriate function such REF: readNetFromCaffe, REF: readNetFromTensorflow,
     * REF: readNetFromTorch or REF: readNetFromDarknet. An order of {@code model} and {@code config}
     * arguments does not matter.
     */
    public static Net readNet(String model, String config, String framework) {
        return new Net(readNet_0(model, config, framework));
    }

    /**
     * Read deep learning network represented in one of the supported formats.
     * @param model Binary file contains trained weights. The following file
     * extensions are expected for models from different frameworks:
     * * {@code *.caffemodel} (Caffe, http://caffe.berkeleyvision.org/)
     * * {@code *.pb} (TensorFlow, https://www.tensorflow.org/)
     * * {@code *.t7} | {@code *.net} (Torch, http://torch.ch/)
     * * {@code *.weights} (Darknet, https://pjreddie.com/darknet/)
     * * {@code *.bin} (DLDT, https://software.intel.com/openvino-toolkit)
     * * {@code *.onnx} (ONNX, https://onnx.ai/)
     * @param config Text file contains network configuration. It could be a
     * file with the following extensions:
     * * {@code *.prototxt} (Caffe, http://caffe.berkeleyvision.org/)
     * * {@code *.pbtxt} (TensorFlow, https://www.tensorflow.org/)
     * * {@code *.cfg} (Darknet, https://pjreddie.com/darknet/)
     * * {@code *.xml} (DLDT, https://software.intel.com/openvino-toolkit)
     * @return Net object.
     *
     * This function automatically detects an origin framework of trained model
     * and calls an appropriate function such REF: readNetFromCaffe, REF: readNetFromTensorflow,
     * REF: readNetFromTorch or REF: readNetFromDarknet. An order of {@code model} and {@code config}
     * arguments does not matter.
     */
    public static Net readNet(String model, String config) {
        return new Net(readNet_1(model, config));
    }

    /**
     * Read deep learning network represented in one of the supported formats.
     * @param model Binary file contains trained weights. The following file
     * extensions are expected for models from different frameworks:
     * * {@code *.caffemodel} (Caffe, http://caffe.berkeleyvision.org/)
     * * {@code *.pb} (TensorFlow, https://www.tensorflow.org/)
     * * {@code *.t7} | {@code *.net} (Torch, http://torch.ch/)
     * * {@code *.weights} (Darknet, https://pjreddie.com/darknet/)
     * * {@code *.bin} (DLDT, https://software.intel.com/openvino-toolkit)
     * * {@code *.onnx} (ONNX, https://onnx.ai/)
     * file with the following extensions:
     * * {@code *.prototxt} (Caffe, http://caffe.berkeleyvision.org/)
     * * {@code *.pbtxt} (TensorFlow, https://www.tensorflow.org/)
     * * {@code *.cfg} (Darknet, https://pjreddie.com/darknet/)
     * * {@code *.xml} (DLDT, https://software.intel.com/openvino-toolkit)
     * @return Net object.
     *
     * This function automatically detects an origin framework of trained model
     * and calls an appropriate function such REF: readNetFromCaffe, REF: readNetFromTensorflow,
     * REF: readNetFromTorch or REF: readNetFromDarknet. An order of {@code model} and {@code config}
     * arguments does not matter.
     */
    public static Net readNet(String model) {
        return new Net(readNet_2(model));
    }


    //
    // C++:  Net cv::dnn::readNet(String framework, vector_uchar bufferModel, vector_uchar bufferConfig = std::vector<uchar>())
    //

    /**
     * Read deep learning network represented in one of the supported formats.
     * This is an overloaded member function, provided for convenience.
     * It differs from the above function only in what argument(s) it accepts.
     * @param framework    Name of origin framework.
     * @param bufferModel  A buffer with a content of binary file with weights
     * @param bufferConfig A buffer with a content of text file contains network configuration.
     * @return Net object.
     */
    public static Net readNet(String framework, MatOfByte bufferModel, MatOfByte bufferConfig) {
        Mat bufferModel_mat = bufferModel;
        Mat bufferConfig_mat = bufferConfig;
        return new Net(readNet_3(framework, bufferModel_mat.nativeObj, bufferConfig_mat.nativeObj));
    }

    /**
     * Read deep learning network represented in one of the supported formats.
     * This is an overloaded member function, provided for convenience.
     * It differs from the above function only in what argument(s) it accepts.
     * @param framework    Name of origin framework.
     * @param bufferModel  A buffer with a content of binary file with weights
     * @return Net object.
     */
    public static Net readNet(String framework, MatOfByte bufferModel) {
        Mat bufferModel_mat = bufferModel;
        return new Net(readNet_4(framework, bufferModel_mat.nativeObj));
    }


    //
    // C++:  Mat cv::dnn::readTorchBlob(String filename, bool isBinary = true)
    //

    /**
     * Loads blob which was serialized as torch.Tensor object of Torch7 framework.
     * WARNING: This function has the same limitations as readNetFromTorch().
     * @param filename automatically generated
     * @param isBinary automatically generated
     * @return automatically generated
     */
    public static Mat readTorchBlob(String filename, boolean isBinary) {
        return new Mat(readTorchBlob_0(filename, isBinary));
    }

    /**
     * Loads blob which was serialized as torch.Tensor object of Torch7 framework.
     * WARNING: This function has the same limitations as readNetFromTorch().
     * @param filename automatically generated
     * @return automatically generated
     */
    public static Mat readTorchBlob(String filename) {
        return new Mat(readTorchBlob_1(filename));
    }


    //
    // C++:  Net cv::dnn::readNetFromModelOptimizer(String xml, String bin)
    //

    /**
     * Load a network from Intel's Model Optimizer intermediate representation.
     * @param xml XML configuration file with network's topology.
     * @param bin Binary file with trained weights.
     * @return Net object.
     * Networks imported from Intel's Model Optimizer are launched in Intel's Inference Engine
     * backend.
     */
    public static Net readNetFromModelOptimizer(String xml, String bin) {
        return new Net(readNetFromModelOptimizer_0(xml, bin));
    }


    //
    // C++:  Net cv::dnn::readNetFromModelOptimizer(vector_uchar bufferModelConfig, vector_uchar bufferWeights)
    //

    /**
     * Load a network from Intel's Model Optimizer intermediate representation.
     * @param bufferModelConfig Buffer contains XML configuration with network's topology.
     * @param bufferWeights Buffer contains binary data with trained weights.
     * @return Net object.
     * Networks imported from Intel's Model Optimizer are launched in Intel's Inference Engine
     * backend.
     */
    public static Net readNetFromModelOptimizer(MatOfByte bufferModelConfig, MatOfByte bufferWeights) {
        Mat bufferModelConfig_mat = bufferModelConfig;
        Mat bufferWeights_mat = bufferWeights;
        return new Net(readNetFromModelOptimizer_1(bufferModelConfig_mat.nativeObj, bufferWeights_mat.nativeObj));
    }


    //
    // C++:  Net cv::dnn::readNetFromONNX(String onnxFile)
    //

    /**
     * Reads a network model &lt;a href="https://onnx.ai/"&gt;ONNX&lt;/a&gt;.
     * @param onnxFile path to the .onnx file with text description of the network architecture.
     * @return Network object that ready to do forward, throw an exception in failure cases.
     */
    public static Net readNetFromONNX(String onnxFile) {
        return new Net(readNetFromONNX_0(onnxFile));
    }


    //
    // C++:  Net cv::dnn::readNetFromONNX(vector_uchar buffer)
    //

    /**
     * Reads a network model from &lt;a href="https://onnx.ai/"&gt;ONNX&lt;/a&gt;
     * in-memory buffer.
     * @param buffer in-memory buffer that stores the ONNX model bytes.
     * @return Network object that ready to do forward, throw an exception
     * in failure cases.
     */
    public static Net readNetFromONNX(MatOfByte buffer) {
        Mat buffer_mat = buffer;
        return new Net(readNetFromONNX_1(buffer_mat.nativeObj));
    }


    //
    // C++:  Mat cv::dnn::readTensorFromONNX(String path)
    //

    /**
     * Creates blob from .pb file.
     * @param path to the .pb file with input tensor.
     * @return Mat.
     */
    public static Mat readTensorFromONNX(String path) {
        return new Mat(readTensorFromONNX_0(path));
    }


    //
    // C++:  Mat cv::dnn::blobFromImage(Mat image, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, bool crop = false, int ddepth = CV_32F)
    //

    /**
     * Creates 4-dimensional blob from image. Optionally resizes and crops {@code image} from center,
     * subtract {@code mean} values, scales values by {@code scalefactor}, swap Blue and Red channels.
     * @param image input image (with 1-, 3- or 4-channels).
     * @param scalefactor multiplier for {@code images} values.
     * @param size spatial size for output image
     * @param mean scalar with mean values which are subtracted from channels. Values are intended
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param swapRB flag which indicates that swap first and last channels
     * in 3-channel image is necessary.
     * @param crop flag which indicates whether image will be cropped after resize or not
     * @param ddepth Depth of output blob. Choose CV_32F or CV_8U.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop, int ddepth) {
        return new Mat(blobFromImage_0(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop, ddepth));
    }

    /**
     * Creates 4-dimensional blob from image. Optionally resizes and crops {@code image} from center,
     * subtract {@code mean} values, scales values by {@code scalefactor}, swap Blue and Red channels.
     * @param image input image (with 1-, 3- or 4-channels).
     * @param scalefactor multiplier for {@code images} values.
     * @param size spatial size for output image
     * @param mean scalar with mean values which are subtracted from channels. Values are intended
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param swapRB flag which indicates that swap first and last channels
     * in 3-channel image is necessary.
     * @param crop flag which indicates whether image will be cropped after resize or not
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop) {
        return new Mat(blobFromImage_1(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop));
    }

    /**
     * Creates 4-dimensional blob from image. Optionally resizes and crops {@code image} from center,
     * subtract {@code mean} values, scales values by {@code scalefactor}, swap Blue and Red channels.
     * @param image input image (with 1-, 3- or 4-channels).
     * @param scalefactor multiplier for {@code images} values.
     * @param size spatial size for output image
     * @param mean scalar with mean values which are subtracted from channels. Values are intended
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param swapRB flag which indicates that swap first and last channels
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean, boolean swapRB) {
        return new Mat(blobFromImage_2(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB));
    }

    /**
     * Creates 4-dimensional blob from image. Optionally resizes and crops {@code image} from center,
     * subtract {@code mean} values, scales values by {@code scalefactor}, swap Blue and Red channels.
     * @param image input image (with 1-, 3- or 4-channels).
     * @param scalefactor multiplier for {@code images} values.
     * @param size spatial size for output image
     * @param mean scalar with mean values which are subtracted from channels. Values are intended
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean) {
        return new Mat(blobFromImage_3(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3]));
    }

    /**
     * Creates 4-dimensional blob from image. Optionally resizes and crops {@code image} from center,
     * subtract {@code mean} values, scales values by {@code scalefactor}, swap Blue and Red channels.
     * @param image input image (with 1-, 3- or 4-channels).
     * @param scalefactor multiplier for {@code images} values.
     * @param size spatial size for output image
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImage(Mat image, double scalefactor, Size size) {
        return new Mat(blobFromImage_4(image.nativeObj, scalefactor, size.width, size.height));
    }

    /**
     * Creates 4-dimensional blob from image. Optionally resizes and crops {@code image} from center,
     * subtract {@code mean} values, scales values by {@code scalefactor}, swap Blue and Red channels.
     * @param image input image (with 1-, 3- or 4-channels).
     * @param scalefactor multiplier for {@code images} values.
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImage(Mat image, double scalefactor) {
        return new Mat(blobFromImage_5(image.nativeObj, scalefactor));
    }

    /**
     * Creates 4-dimensional blob from image. Optionally resizes and crops {@code image} from center,
     * subtract {@code mean} values, scales values by {@code scalefactor}, swap Blue and Red channels.
     * @param image input image (with 1-, 3- or 4-channels).
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImage(Mat image) {
        return new Mat(blobFromImage_6(image.nativeObj));
    }


    //
    // C++:  Mat cv::dnn::blobFromImages(vector_Mat images, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, bool crop = false, int ddepth = CV_32F)
    //

    /**
     * Creates 4-dimensional blob from series of images. Optionally resizes and
     * crops {@code images} from center, subtract {@code mean} values, scales values by {@code scalefactor},
     * swap Blue and Red channels.
     * @param images input images (all with 1-, 3- or 4-channels).
     * @param size spatial size for output image
     * @param mean scalar with mean values which are subtracted from channels. Values are intended
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param scalefactor multiplier for {@code images} values.
     * @param swapRB flag which indicates that swap first and last channels
     * in 3-channel image is necessary.
     * @param crop flag which indicates whether image will be cropped after resize or not
     * @param ddepth Depth of output blob. Choose CV_32F or CV_8U.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop, int ddepth) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImages_0(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop, ddepth));
    }

    /**
     * Creates 4-dimensional blob from series of images. Optionally resizes and
     * crops {@code images} from center, subtract {@code mean} values, scales values by {@code scalefactor},
     * swap Blue and Red channels.
     * @param images input images (all with 1-, 3- or 4-channels).
     * @param size spatial size for output image
     * @param mean scalar with mean values which are subtracted from channels. Values are intended
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param scalefactor multiplier for {@code images} values.
     * @param swapRB flag which indicates that swap first and last channels
     * in 3-channel image is necessary.
     * @param crop flag which indicates whether image will be cropped after resize or not
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImages_1(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop));
    }

    /**
     * Creates 4-dimensional blob from series of images. Optionally resizes and
     * crops {@code images} from center, subtract {@code mean} values, scales values by {@code scalefactor},
     * swap Blue and Red channels.
     * @param images input images (all with 1-, 3- or 4-channels).
     * @param size spatial size for output image
     * @param mean scalar with mean values which are subtracted from channels. Values are intended
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param scalefactor multiplier for {@code images} values.
     * @param swapRB flag which indicates that swap first and last channels
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean, boolean swapRB) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImages_2(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB));
    }

    /**
     * Creates 4-dimensional blob from series of images. Optionally resizes and
     * crops {@code images} from center, subtract {@code mean} values, scales values by {@code scalefactor},
     * swap Blue and Red channels.
     * @param images input images (all with 1-, 3- or 4-channels).
     * @param size spatial size for output image
     * @param mean scalar with mean values which are subtracted from channels. Values are intended
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param scalefactor multiplier for {@code images} values.
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImages_3(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3]));
    }

    /**
     * Creates 4-dimensional blob from series of images. Optionally resizes and
     * crops {@code images} from center, subtract {@code mean} values, scales values by {@code scalefactor},
     * swap Blue and Red channels.
     * @param images input images (all with 1-, 3- or 4-channels).
     * @param size spatial size for output image
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param scalefactor multiplier for {@code images} values.
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImages_4(images_mat.nativeObj, scalefactor, size.width, size.height));
    }

    /**
     * Creates 4-dimensional blob from series of images. Optionally resizes and
     * crops {@code images} from center, subtract {@code mean} values, scales values by {@code scalefactor},
     * swap Blue and Red channels.
     * @param images input images (all with 1-, 3- or 4-channels).
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * @param scalefactor multiplier for {@code images} values.
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImages(List<Mat> images, double scalefactor) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImages_5(images_mat.nativeObj, scalefactor));
    }

    /**
     * Creates 4-dimensional blob from series of images. Optionally resizes and
     * crops {@code images} from center, subtract {@code mean} values, scales values by {@code scalefactor},
     * swap Blue and Red channels.
     * @param images input images (all with 1-, 3- or 4-channels).
     * to be in (mean-R, mean-G, mean-B) order if {@code image} has BGR ordering and {@code swapRB} is true.
     * in 3-channel image is necessary.
     * if {@code crop} is true, input image is resized so one side after resize is equal to corresponding
     * dimension in {@code size} and another one is equal or larger. Then, crop from the center is performed.
     * If {@code crop} is false, direct resize without cropping and preserving aspect ratio is performed.
     * @return 4-dimensional Mat with NCHW dimensions order.
     *
     * <b>Note:</b>
     * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
     */
    public static Mat blobFromImages(List<Mat> images) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImages_6(images_mat.nativeObj));
    }


    //
    // C++:  Mat cv::dnn::blobFromImageWithParams(Mat image, Image2BlobParams param = Image2BlobParams())
    //

    /**
     * Creates 4-dimensional blob from image with given params.
     *
     * This function is an extension of REF: blobFromImage to meet more image preprocess needs.
     * Given input image and preprocessing parameters, and function outputs the blob.
     *
     * @param image input image (all with 1-, 3- or 4-channels).
     * @param param struct of Image2BlobParams, contains all parameters needed by processing of image to blob.
     * @return 4-dimensional Mat.
     */
    public static Mat blobFromImageWithParams(Mat image, Image2BlobParams param) {
        return new Mat(blobFromImageWithParams_0(image.nativeObj, param.nativeObj));
    }

    /**
     * Creates 4-dimensional blob from image with given params.
     *
     * This function is an extension of REF: blobFromImage to meet more image preprocess needs.
     * Given input image and preprocessing parameters, and function outputs the blob.
     *
     * @param image input image (all with 1-, 3- or 4-channels).
     * @return 4-dimensional Mat.
     */
    public static Mat blobFromImageWithParams(Mat image) {
        return new Mat(blobFromImageWithParams_1(image.nativeObj));
    }


    //
    // C++:  void cv::dnn::blobFromImageWithParams(Mat image, Mat& blob, Image2BlobParams param = Image2BlobParams())
    //

    public static void blobFromImageWithParams(Mat image, Mat blob, Image2BlobParams param) {
        blobFromImageWithParams_2(image.nativeObj, blob.nativeObj, param.nativeObj);
    }

    public static void blobFromImageWithParams(Mat image, Mat blob) {
        blobFromImageWithParams_3(image.nativeObj, blob.nativeObj);
    }


    //
    // C++:  Mat cv::dnn::blobFromImagesWithParams(vector_Mat images, Image2BlobParams param = Image2BlobParams())
    //

    /**
     * Creates 4-dimensional blob from series of images with given params.
     *
     * This function is an extension of REF: blobFromImages to meet more image preprocess needs.
     * Given input image and preprocessing parameters, and function outputs the blob.
     *
     * @param images input image (all with 1-, 3- or 4-channels).
     * @param param struct of Image2BlobParams, contains all parameters needed by processing of image to blob.
     * @return 4-dimensional Mat.
     */
    public static Mat blobFromImagesWithParams(List<Mat> images, Image2BlobParams param) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImagesWithParams_0(images_mat.nativeObj, param.nativeObj));
    }

    /**
     * Creates 4-dimensional blob from series of images with given params.
     *
     * This function is an extension of REF: blobFromImages to meet more image preprocess needs.
     * Given input image and preprocessing parameters, and function outputs the blob.
     *
     * @param images input image (all with 1-, 3- or 4-channels).
     * @return 4-dimensional Mat.
     */
    public static Mat blobFromImagesWithParams(List<Mat> images) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        return new Mat(blobFromImagesWithParams_1(images_mat.nativeObj));
    }


    //
    // C++:  void cv::dnn::blobFromImagesWithParams(vector_Mat images, Mat& blob, Image2BlobParams param = Image2BlobParams())
    //

    public static void blobFromImagesWithParams(List<Mat> images, Mat blob, Image2BlobParams param) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        blobFromImagesWithParams_2(images_mat.nativeObj, blob.nativeObj, param.nativeObj);
    }

    public static void blobFromImagesWithParams(List<Mat> images, Mat blob) {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        blobFromImagesWithParams_3(images_mat.nativeObj, blob.nativeObj);
    }


    //
    // C++:  void cv::dnn::imagesFromBlob(Mat blob_, vector_Mat& images_)
    //

    /**
     * Parse a 4D blob and output the images it contains as 2D arrays through a simpler data structure
     * (std::vector&lt;cv::Mat&gt;).
     * @param blob_ 4 dimensional array (images, channels, height, width) in floating point precision (CV_32F) from
     * which you would like to extract the images.
     * @param images_ array of 2D Mat containing the images extracted from the blob in floating point precision
     * (CV_32F). They are non normalized neither mean added. The number of returned images equals the first dimension
     * of the blob (batch size). Every image has a number of channels equals to the second dimension of the blob (depth).
     */
    public static void imagesFromBlob(Mat blob_, List<Mat> images_) {
        Mat images__mat = new Mat();
        imagesFromBlob_0(blob_.nativeObj, images__mat.nativeObj);
        Converters.Mat_to_vector_Mat(images__mat, images_);
        images__mat.release();
    }


    //
    // C++:  void cv::dnn::shrinkCaffeModel(String src, String dst, vector_String layersTypes = std::vector<String>())
    //

    /**
     * Convert all weights of Caffe network to half precision floating point.
     * @param src Path to origin model from Caffe framework contains single
     * precision floating point weights (usually has {@code .caffemodel} extension).
     * @param dst Path to destination model with updated weights.
     * @param layersTypes Set of layers types which parameters will be converted.
     * By default, converts only Convolutional and Fully-Connected layers'
     * weights.
     *
     * <b>Note:</b> Shrinked model has no origin float32 weights so it can't be used
     * in origin Caffe framework anymore. However the structure of data
     * is taken from NVidia's Caffe fork: https://github.com/NVIDIA/caffe.
     * So the resulting model may be used there.
     */
    public static void shrinkCaffeModel(String src, String dst, List<String> layersTypes) {
        shrinkCaffeModel_0(src, dst, layersTypes);
    }

    /**
     * Convert all weights of Caffe network to half precision floating point.
     * @param src Path to origin model from Caffe framework contains single
     * precision floating point weights (usually has {@code .caffemodel} extension).
     * @param dst Path to destination model with updated weights.
     * By default, converts only Convolutional and Fully-Connected layers'
     * weights.
     *
     * <b>Note:</b> Shrinked model has no origin float32 weights so it can't be used
     * in origin Caffe framework anymore. However the structure of data
     * is taken from NVidia's Caffe fork: https://github.com/NVIDIA/caffe.
     * So the resulting model may be used there.
     */
    public static void shrinkCaffeModel(String src, String dst) {
        shrinkCaffeModel_1(src, dst);
    }


    //
    // C++:  void cv::dnn::writeTextGraph(String model, String output)
    //

    /**
     * Create a text representation for a binary network stored in protocol buffer format.
     * @param model  A path to binary network.
     * @param output A path to output text file to be created.
     *
     * <b>Note:</b> To reduce output file size, trained weights are not included.
     */
    public static void writeTextGraph(String model, String output) {
        writeTextGraph_0(model, output);
    }


    //
    // C++:  void cv::dnn::NMSBoxes(vector_Rect2d bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    //

    /**
     * Performs non maximum suppression given boxes and corresponding scores.
     *
     * @param bboxes a set of bounding boxes to apply NMS.
     * @param scores a set of corresponding confidences.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     * @param eta a coefficient in adaptive threshold formula: \(nms\_threshold_{i+1}=eta\cdot nms\_threshold_i\).
     * @param top_k if {@code &gt;0}, keep at most {@code top_k} picked indices.
     */
    public static void NMSBoxes(MatOfRect2d bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta, int top_k) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_0(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta, top_k);
    }

    /**
     * Performs non maximum suppression given boxes and corresponding scores.
     *
     * @param bboxes a set of bounding boxes to apply NMS.
     * @param scores a set of corresponding confidences.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     * @param eta a coefficient in adaptive threshold formula: \(nms\_threshold_{i+1}=eta\cdot nms\_threshold_i\).
     */
    public static void NMSBoxes(MatOfRect2d bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_1(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta);
    }

    /**
     * Performs non maximum suppression given boxes and corresponding scores.
     *
     * @param bboxes a set of bounding boxes to apply NMS.
     * @param scores a set of corresponding confidences.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     */
    public static void NMSBoxes(MatOfRect2d bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_2(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj);
    }


    //
    // C++:  void cv::dnn::NMSBoxes(vector_RotatedRect bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    //

    public static void NMSBoxesRotated(MatOfRotatedRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta, int top_k) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxesRotated_0(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta, top_k);
    }

    public static void NMSBoxesRotated(MatOfRotatedRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxesRotated_1(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta);
    }

    public static void NMSBoxesRotated(MatOfRotatedRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxesRotated_2(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj);
    }


    //
    // C++:  void cv::dnn::NMSBoxesBatched(vector_Rect2d bboxes, vector_float scores, vector_int class_ids, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    //

    /**
     * Performs batched non maximum suppression on given boxes and corresponding scores across different classes.
     *
     * @param bboxes a set of bounding boxes to apply NMS.
     * @param scores a set of corresponding confidences.
     * @param class_ids a set of corresponding class ids. Ids are integer and usually start from 0.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     * @param eta a coefficient in adaptive threshold formula: \(nms\_threshold_{i+1}=eta\cdot nms\_threshold_i\).
     * @param top_k if {@code &gt;0}, keep at most {@code top_k} picked indices.
     */
    public static void NMSBoxesBatched(MatOfRect2d bboxes, MatOfFloat scores, MatOfInt class_ids, float score_threshold, float nms_threshold, MatOfInt indices, float eta, int top_k) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat class_ids_mat = class_ids;
        Mat indices_mat = indices;
        NMSBoxesBatched_0(bboxes_mat.nativeObj, scores_mat.nativeObj, class_ids_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta, top_k);
    }

    /**
     * Performs batched non maximum suppression on given boxes and corresponding scores across different classes.
     *
     * @param bboxes a set of bounding boxes to apply NMS.
     * @param scores a set of corresponding confidences.
     * @param class_ids a set of corresponding class ids. Ids are integer and usually start from 0.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     * @param eta a coefficient in adaptive threshold formula: \(nms\_threshold_{i+1}=eta\cdot nms\_threshold_i\).
     */
    public static void NMSBoxesBatched(MatOfRect2d bboxes, MatOfFloat scores, MatOfInt class_ids, float score_threshold, float nms_threshold, MatOfInt indices, float eta) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat class_ids_mat = class_ids;
        Mat indices_mat = indices;
        NMSBoxesBatched_1(bboxes_mat.nativeObj, scores_mat.nativeObj, class_ids_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta);
    }

    /**
     * Performs batched non maximum suppression on given boxes and corresponding scores across different classes.
     *
     * @param bboxes a set of bounding boxes to apply NMS.
     * @param scores a set of corresponding confidences.
     * @param class_ids a set of corresponding class ids. Ids are integer and usually start from 0.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     */
    public static void NMSBoxesBatched(MatOfRect2d bboxes, MatOfFloat scores, MatOfInt class_ids, float score_threshold, float nms_threshold, MatOfInt indices) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat class_ids_mat = class_ids;
        Mat indices_mat = indices;
        NMSBoxesBatched_2(bboxes_mat.nativeObj, scores_mat.nativeObj, class_ids_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj);
    }


    //
    // C++:  void cv::dnn::softNMSBoxes(vector_Rect bboxes, vector_float scores, vector_float& updated_scores, float score_threshold, float nms_threshold, vector_int& indices, size_t top_k = 0, float sigma = 0.5, SoftNMSMethod method = SoftNMSMethod::SOFTNMS_GAUSSIAN)
    //

    /**
     * Performs soft non maximum suppression given boxes and corresponding scores.
     * Reference: https://arxiv.org/abs/1704.04503
     * @param bboxes a set of bounding boxes to apply Soft NMS.
     * @param scores a set of corresponding confidences.
     * @param updated_scores a set of corresponding updated confidences.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     * @param top_k keep at most {@code top_k} picked indices.
     * @param sigma parameter of Gaussian weighting.
     * SEE: SoftNMSMethod
     */
    public static void softNMSBoxes(MatOfRect bboxes, MatOfFloat scores, MatOfFloat updated_scores, float score_threshold, float nms_threshold, MatOfInt indices, long top_k, float sigma) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat updated_scores_mat = updated_scores;
        Mat indices_mat = indices;
        softNMSBoxes_0(bboxes_mat.nativeObj, scores_mat.nativeObj, updated_scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, top_k, sigma);
    }

    /**
     * Performs soft non maximum suppression given boxes and corresponding scores.
     * Reference: https://arxiv.org/abs/1704.04503
     * @param bboxes a set of bounding boxes to apply Soft NMS.
     * @param scores a set of corresponding confidences.
     * @param updated_scores a set of corresponding updated confidences.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     * @param top_k keep at most {@code top_k} picked indices.
     * SEE: SoftNMSMethod
     */
    public static void softNMSBoxes(MatOfRect bboxes, MatOfFloat scores, MatOfFloat updated_scores, float score_threshold, float nms_threshold, MatOfInt indices, long top_k) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat updated_scores_mat = updated_scores;
        Mat indices_mat = indices;
        softNMSBoxes_2(bboxes_mat.nativeObj, scores_mat.nativeObj, updated_scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, top_k);
    }

    /**
     * Performs soft non maximum suppression given boxes and corresponding scores.
     * Reference: https://arxiv.org/abs/1704.04503
     * @param bboxes a set of bounding boxes to apply Soft NMS.
     * @param scores a set of corresponding confidences.
     * @param updated_scores a set of corresponding updated confidences.
     * @param score_threshold a threshold used to filter boxes by score.
     * @param nms_threshold a threshold used in non maximum suppression.
     * @param indices the kept indices of bboxes after NMS.
     * SEE: SoftNMSMethod
     */
    public static void softNMSBoxes(MatOfRect bboxes, MatOfFloat scores, MatOfFloat updated_scores, float score_threshold, float nms_threshold, MatOfInt indices) {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat updated_scores_mat = updated_scores;
        Mat indices_mat = indices;
        softNMSBoxes_3(bboxes_mat.nativeObj, scores_mat.nativeObj, updated_scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj);
    }


    //
    // C++:  String cv::dnn::getInferenceEngineBackendType()
    //

    /**
     * Returns Inference Engine internal backend API.
     *
     * See values of {@code CV_DNN_BACKEND_INFERENCE_ENGINE_*} macros.
     *
     * {@code OPENCV_DNN_BACKEND_INFERENCE_ENGINE_TYPE} runtime parameter (environment variable) is ignored since 4.6.0.
     *
     * @deprecated
     * @return automatically generated
     */
    @Deprecated
    public static String getInferenceEngineBackendType() {
        return getInferenceEngineBackendType_0();
    }


    //
    // C++:  String cv::dnn::setInferenceEngineBackendType(String newBackendType)
    //

    /**
     * Specify Inference Engine internal backend API.
     *
     * See values of {@code CV_DNN_BACKEND_INFERENCE_ENGINE_*} macros.
     *
     * @return previous value of internal backend API
     *
     * @deprecated
     * @param newBackendType automatically generated
     */
    @Deprecated
    public static String setInferenceEngineBackendType(String newBackendType) {
        return setInferenceEngineBackendType_0(newBackendType);
    }


    //
    // C++:  void cv::dnn::resetMyriadDevice()
    //

    /**
     * Release a Myriad device (binded by OpenCV).
     *
     * Single Myriad device cannot be shared across multiple processes which uses
     * Inference Engine's Myriad plugin.
     */
    public static void resetMyriadDevice() {
        resetMyriadDevice_0();
    }


    //
    // C++:  String cv::dnn::getInferenceEngineVPUType()
    //

    /**
     * Returns Inference Engine VPU type.
     *
     * See values of {@code CV_DNN_INFERENCE_ENGINE_VPU_TYPE_*} macros.
     * @return automatically generated
     */
    public static String getInferenceEngineVPUType() {
        return getInferenceEngineVPUType_0();
    }


    //
    // C++:  String cv::dnn::getInferenceEngineCPUType()
    //

    /**
     * Returns Inference Engine CPU type.
     *
     * Specify OpenVINO plugin: CPU or ARM.
     * @return automatically generated
     */
    public static String getInferenceEngineCPUType() {
        return getInferenceEngineCPUType_0();
    }


    //
    // C++:  void cv::dnn::releaseHDDLPlugin()
    //

    /**
     * Release a HDDL plugin.
     */
    public static void releaseHDDLPlugin() {
        releaseHDDLPlugin_0();
    }




    // C++:  vector_Target cv::dnn::getAvailableTargets(dnn_Backend be)
    private static native List<Integer> getAvailableTargets_0(int be);

    // C++:  Net cv::dnn::readNetFromDarknet(String cfgFile, String darknetModel = String())
    private static native long readNetFromDarknet_0(String cfgFile, String darknetModel);
    private static native long readNetFromDarknet_1(String cfgFile);

    // C++:  Net cv::dnn::readNetFromDarknet(vector_uchar bufferCfg, vector_uchar bufferModel = std::vector<uchar>())
    private static native long readNetFromDarknet_2(long bufferCfg_mat_nativeObj, long bufferModel_mat_nativeObj);
    private static native long readNetFromDarknet_3(long bufferCfg_mat_nativeObj);

    // C++:  Net cv::dnn::readNetFromCaffe(String prototxt, String caffeModel = String())
    private static native long readNetFromCaffe_0(String prototxt, String caffeModel);
    private static native long readNetFromCaffe_1(String prototxt);

    // C++:  Net cv::dnn::readNetFromCaffe(vector_uchar bufferProto, vector_uchar bufferModel = std::vector<uchar>())
    private static native long readNetFromCaffe_2(long bufferProto_mat_nativeObj, long bufferModel_mat_nativeObj);
    private static native long readNetFromCaffe_3(long bufferProto_mat_nativeObj);

    // C++:  Net cv::dnn::readNetFromTensorflow(String model, String config = String())
    private static native long readNetFromTensorflow_0(String model, String config);
    private static native long readNetFromTensorflow_1(String model);

    // C++:  Net cv::dnn::readNetFromTensorflow(vector_uchar bufferModel, vector_uchar bufferConfig = std::vector<uchar>())
    private static native long readNetFromTensorflow_2(long bufferModel_mat_nativeObj, long bufferConfig_mat_nativeObj);
    private static native long readNetFromTensorflow_3(long bufferModel_mat_nativeObj);

    // C++:  Net cv::dnn::readNetFromTFLite(String model)
    private static native long readNetFromTFLite_0(String model);

    // C++:  Net cv::dnn::readNetFromTFLite(vector_uchar bufferModel)
    private static native long readNetFromTFLite_1(long bufferModel_mat_nativeObj);

    // C++:  Net cv::dnn::readNetFromTorch(String model, bool isBinary = true, bool evaluate = true)
    private static native long readNetFromTorch_0(String model, boolean isBinary, boolean evaluate);
    private static native long readNetFromTorch_1(String model, boolean isBinary);
    private static native long readNetFromTorch_2(String model);

    // C++:  Net cv::dnn::readNet(String model, String config = "", String framework = "")
    private static native long readNet_0(String model, String config, String framework);
    private static native long readNet_1(String model, String config);
    private static native long readNet_2(String model);

    // C++:  Net cv::dnn::readNet(String framework, vector_uchar bufferModel, vector_uchar bufferConfig = std::vector<uchar>())
    private static native long readNet_3(String framework, long bufferModel_mat_nativeObj, long bufferConfig_mat_nativeObj);
    private static native long readNet_4(String framework, long bufferModel_mat_nativeObj);

    // C++:  Mat cv::dnn::readTorchBlob(String filename, bool isBinary = true)
    private static native long readTorchBlob_0(String filename, boolean isBinary);
    private static native long readTorchBlob_1(String filename);

    // C++:  Net cv::dnn::readNetFromModelOptimizer(String xml, String bin)
    private static native long readNetFromModelOptimizer_0(String xml, String bin);

    // C++:  Net cv::dnn::readNetFromModelOptimizer(vector_uchar bufferModelConfig, vector_uchar bufferWeights)
    private static native long readNetFromModelOptimizer_1(long bufferModelConfig_mat_nativeObj, long bufferWeights_mat_nativeObj);

    // C++:  Net cv::dnn::readNetFromONNX(String onnxFile)
    private static native long readNetFromONNX_0(String onnxFile);

    // C++:  Net cv::dnn::readNetFromONNX(vector_uchar buffer)
    private static native long readNetFromONNX_1(long buffer_mat_nativeObj);

    // C++:  Mat cv::dnn::readTensorFromONNX(String path)
    private static native long readTensorFromONNX_0(String path);

    // C++:  Mat cv::dnn::blobFromImage(Mat image, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, bool crop = false, int ddepth = CV_32F)
    private static native long blobFromImage_0(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop, int ddepth);
    private static native long blobFromImage_1(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop);
    private static native long blobFromImage_2(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB);
    private static native long blobFromImage_3(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3);
    private static native long blobFromImage_4(long image_nativeObj, double scalefactor, double size_width, double size_height);
    private static native long blobFromImage_5(long image_nativeObj, double scalefactor);
    private static native long blobFromImage_6(long image_nativeObj);

    // C++:  Mat cv::dnn::blobFromImages(vector_Mat images, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, bool crop = false, int ddepth = CV_32F)
    private static native long blobFromImages_0(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop, int ddepth);
    private static native long blobFromImages_1(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop);
    private static native long blobFromImages_2(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB);
    private static native long blobFromImages_3(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3);
    private static native long blobFromImages_4(long images_mat_nativeObj, double scalefactor, double size_width, double size_height);
    private static native long blobFromImages_5(long images_mat_nativeObj, double scalefactor);
    private static native long blobFromImages_6(long images_mat_nativeObj);

    // C++:  Mat cv::dnn::blobFromImageWithParams(Mat image, Image2BlobParams param = Image2BlobParams())
    private static native long blobFromImageWithParams_0(long image_nativeObj, long param_nativeObj);
    private static native long blobFromImageWithParams_1(long image_nativeObj);

    // C++:  void cv::dnn::blobFromImageWithParams(Mat image, Mat& blob, Image2BlobParams param = Image2BlobParams())
    private static native void blobFromImageWithParams_2(long image_nativeObj, long blob_nativeObj, long param_nativeObj);
    private static native void blobFromImageWithParams_3(long image_nativeObj, long blob_nativeObj);

    // C++:  Mat cv::dnn::blobFromImagesWithParams(vector_Mat images, Image2BlobParams param = Image2BlobParams())
    private static native long blobFromImagesWithParams_0(long images_mat_nativeObj, long param_nativeObj);
    private static native long blobFromImagesWithParams_1(long images_mat_nativeObj);

    // C++:  void cv::dnn::blobFromImagesWithParams(vector_Mat images, Mat& blob, Image2BlobParams param = Image2BlobParams())
    private static native void blobFromImagesWithParams_2(long images_mat_nativeObj, long blob_nativeObj, long param_nativeObj);
    private static native void blobFromImagesWithParams_3(long images_mat_nativeObj, long blob_nativeObj);

    // C++:  void cv::dnn::imagesFromBlob(Mat blob_, vector_Mat& images_)
    private static native void imagesFromBlob_0(long blob__nativeObj, long images__mat_nativeObj);

    // C++:  void cv::dnn::shrinkCaffeModel(String src, String dst, vector_String layersTypes = std::vector<String>())
    private static native void shrinkCaffeModel_0(String src, String dst, List<String> layersTypes);
    private static native void shrinkCaffeModel_1(String src, String dst);

    // C++:  void cv::dnn::writeTextGraph(String model, String output)
    private static native void writeTextGraph_0(String model, String output);

    // C++:  void cv::dnn::NMSBoxes(vector_Rect2d bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    private static native void NMSBoxes_0(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta, int top_k);
    private static native void NMSBoxes_1(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta);
    private static native void NMSBoxes_2(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj);

    // C++:  void cv::dnn::NMSBoxes(vector_RotatedRect bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    private static native void NMSBoxesRotated_0(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta, int top_k);
    private static native void NMSBoxesRotated_1(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta);
    private static native void NMSBoxesRotated_2(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj);

    // C++:  void cv::dnn::NMSBoxesBatched(vector_Rect2d bboxes, vector_float scores, vector_int class_ids, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    private static native void NMSBoxesBatched_0(long bboxes_mat_nativeObj, long scores_mat_nativeObj, long class_ids_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta, int top_k);
    private static native void NMSBoxesBatched_1(long bboxes_mat_nativeObj, long scores_mat_nativeObj, long class_ids_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta);
    private static native void NMSBoxesBatched_2(long bboxes_mat_nativeObj, long scores_mat_nativeObj, long class_ids_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj);

    // C++:  void cv::dnn::softNMSBoxes(vector_Rect bboxes, vector_float scores, vector_float& updated_scores, float score_threshold, float nms_threshold, vector_int& indices, size_t top_k = 0, float sigma = 0.5, SoftNMSMethod method = SoftNMSMethod::SOFTNMS_GAUSSIAN)
    private static native void softNMSBoxes_0(long bboxes_mat_nativeObj, long scores_mat_nativeObj, long updated_scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, long top_k, float sigma);
    private static native void softNMSBoxes_2(long bboxes_mat_nativeObj, long scores_mat_nativeObj, long updated_scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, long top_k);
    private static native void softNMSBoxes_3(long bboxes_mat_nativeObj, long scores_mat_nativeObj, long updated_scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj);

    // C++:  String cv::dnn::getInferenceEngineBackendType()
    private static native String getInferenceEngineBackendType_0();

    // C++:  String cv::dnn::setInferenceEngineBackendType(String newBackendType)
    private static native String setInferenceEngineBackendType_0(String newBackendType);

    // C++:  void cv::dnn::resetMyriadDevice()
    private static native void resetMyriadDevice_0();

    // C++:  String cv::dnn::getInferenceEngineVPUType()
    private static native String getInferenceEngineVPUType_0();

    // C++:  String cv::dnn::getInferenceEngineCPUType()
    private static native String getInferenceEngineCPUType_0();

    // C++:  void cv::dnn::releaseHDDLPlugin()
    private static native void releaseHDDLPlugin_0();

}
