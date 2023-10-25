//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import org.opencv.core.Scalar;
import org.opencv.core.Size;

// C++: class Image2BlobParams
/**
 * Processing params of image to blob.
 *
 * It includes all possible image processing operations and corresponding parameters.
 *
 * SEE: blobFromImageWithParams
 *
 * <b>Note:</b>
 * The order and usage of {@code scalefactor} and {@code mean} are (input - mean) * scalefactor.
 * The order and usage of {@code scalefactor}, {@code size}, {@code mean}, {@code swapRB}, and {@code ddepth} are consistent
 * with the function of REF: blobFromImage.
 */
public class Image2BlobParams {

    protected final long nativeObj;
    protected Image2BlobParams(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static Image2BlobParams __fromPtr__(long addr) { return new Image2BlobParams(addr); }

    //
    // C++:   cv::dnn::Image2BlobParams::Image2BlobParams()
    //

    public Image2BlobParams() {
        nativeObj = Image2BlobParams_0();
    }


    //
    // C++:   cv::dnn::Image2BlobParams::Image2BlobParams(Scalar scalefactor, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, int ddepth = CV_32F, DataLayout datalayout = DNN_LAYOUT_NCHW, ImagePaddingMode mode = DNN_PMODE_NULL)
    //

    public Image2BlobParams(Scalar scalefactor, Size size, Scalar mean, boolean swapRB, int ddepth) {
        nativeObj = Image2BlobParams_1(scalefactor.val[0], scalefactor.val[1], scalefactor.val[2], scalefactor.val[3], size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, ddepth);
    }

    public Image2BlobParams(Scalar scalefactor, Size size, Scalar mean, boolean swapRB) {
        nativeObj = Image2BlobParams_4(scalefactor.val[0], scalefactor.val[1], scalefactor.val[2], scalefactor.val[3], size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB);
    }

    public Image2BlobParams(Scalar scalefactor, Size size, Scalar mean) {
        nativeObj = Image2BlobParams_5(scalefactor.val[0], scalefactor.val[1], scalefactor.val[2], scalefactor.val[3], size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3]);
    }

    public Image2BlobParams(Scalar scalefactor, Size size) {
        nativeObj = Image2BlobParams_6(scalefactor.val[0], scalefactor.val[1], scalefactor.val[2], scalefactor.val[3], size.width, size.height);
    }

    public Image2BlobParams(Scalar scalefactor) {
        nativeObj = Image2BlobParams_7(scalefactor.val[0], scalefactor.val[1], scalefactor.val[2], scalefactor.val[3]);
    }


    //
    // C++: Scalar Image2BlobParams::scalefactor
    //

    public Scalar get_scalefactor() {
        return new Scalar(get_scalefactor_0(nativeObj));
    }


    //
    // C++: void Image2BlobParams::scalefactor
    //

    public void set_scalefactor(Scalar scalefactor) {
        set_scalefactor_0(nativeObj, scalefactor.val[0], scalefactor.val[1], scalefactor.val[2], scalefactor.val[3]);
    }


    //
    // C++: Size Image2BlobParams::size
    //

    public Size get_size() {
        return new Size(get_size_0(nativeObj));
    }


    //
    // C++: void Image2BlobParams::size
    //

    public void set_size(Size size) {
        set_size_0(nativeObj, size.width, size.height);
    }


    //
    // C++: Scalar Image2BlobParams::mean
    //

    public Scalar get_mean() {
        return new Scalar(get_mean_0(nativeObj));
    }


    //
    // C++: void Image2BlobParams::mean
    //

    public void set_mean(Scalar mean) {
        set_mean_0(nativeObj, mean.val[0], mean.val[1], mean.val[2], mean.val[3]);
    }


    //
    // C++: bool Image2BlobParams::swapRB
    //

    public boolean get_swapRB() {
        return get_swapRB_0(nativeObj);
    }


    //
    // C++: void Image2BlobParams::swapRB
    //

    public void set_swapRB(boolean swapRB) {
        set_swapRB_0(nativeObj, swapRB);
    }


    //
    // C++: int Image2BlobParams::ddepth
    //

    public int get_ddepth() {
        return get_ddepth_0(nativeObj);
    }


    //
    // C++: void Image2BlobParams::ddepth
    //

    public void set_ddepth(int ddepth) {
        set_ddepth_0(nativeObj, ddepth);
    }


    //
    // C++: DataLayout Image2BlobParams::datalayout
    //

    // Return type 'DataLayout' is not supported, skipping the function


    //
    // C++: void Image2BlobParams::datalayout
    //

    // Unknown type 'DataLayout' (I), skipping the function


    //
    // C++: ImagePaddingMode Image2BlobParams::paddingmode
    //

    // Return type 'ImagePaddingMode' is not supported, skipping the function


    //
    // C++: void Image2BlobParams::paddingmode
    //

    // Unknown type 'ImagePaddingMode' (I), skipping the function


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::dnn::Image2BlobParams::Image2BlobParams()
    private static native long Image2BlobParams_0();

    // C++:   cv::dnn::Image2BlobParams::Image2BlobParams(Scalar scalefactor, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, int ddepth = CV_32F, DataLayout datalayout = DNN_LAYOUT_NCHW, ImagePaddingMode mode = DNN_PMODE_NULL)
    private static native long Image2BlobParams_1(double scalefactor_val0, double scalefactor_val1, double scalefactor_val2, double scalefactor_val3, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, int ddepth);
    private static native long Image2BlobParams_4(double scalefactor_val0, double scalefactor_val1, double scalefactor_val2, double scalefactor_val3, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB);
    private static native long Image2BlobParams_5(double scalefactor_val0, double scalefactor_val1, double scalefactor_val2, double scalefactor_val3, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3);
    private static native long Image2BlobParams_6(double scalefactor_val0, double scalefactor_val1, double scalefactor_val2, double scalefactor_val3, double size_width, double size_height);
    private static native long Image2BlobParams_7(double scalefactor_val0, double scalefactor_val1, double scalefactor_val2, double scalefactor_val3);

    // C++: Scalar Image2BlobParams::scalefactor
    private static native double[] get_scalefactor_0(long nativeObj);

    // C++: void Image2BlobParams::scalefactor
    private static native void set_scalefactor_0(long nativeObj, double scalefactor_val0, double scalefactor_val1, double scalefactor_val2, double scalefactor_val3);

    // C++: Size Image2BlobParams::size
    private static native double[] get_size_0(long nativeObj);

    // C++: void Image2BlobParams::size
    private static native void set_size_0(long nativeObj, double size_width, double size_height);

    // C++: Scalar Image2BlobParams::mean
    private static native double[] get_mean_0(long nativeObj);

    // C++: void Image2BlobParams::mean
    private static native void set_mean_0(long nativeObj, double mean_val0, double mean_val1, double mean_val2, double mean_val3);

    // C++: bool Image2BlobParams::swapRB
    private static native boolean get_swapRB_0(long nativeObj);

    // C++: void Image2BlobParams::swapRB
    private static native void set_swapRB_0(long nativeObj, boolean swapRB);

    // C++: int Image2BlobParams::ddepth
    private static native int get_ddepth_0(long nativeObj);

    // C++: void Image2BlobParams::ddepth
    private static native void set_ddepth_0(long nativeObj, int ddepth);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
