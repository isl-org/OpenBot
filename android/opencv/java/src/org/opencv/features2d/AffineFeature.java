//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.features2d.AffineFeature;
import org.opencv.features2d.Feature2D;
import org.opencv.utils.Converters;

// C++: class AffineFeature
/**
 * Class for implementing the wrapper which makes detectors and extractors to be affine invariant,
 * described as ASIFT in CITE: YM11 .
 */
public class AffineFeature extends Feature2D {

    protected AffineFeature(long addr) { super(addr); }

    // internal usage only
    public static AffineFeature __fromPtr__(long addr) { return new AffineFeature(addr); }

    //
    // C++: static Ptr_AffineFeature cv::AffineFeature::create(Ptr_Feature2D backend, int maxTilt = 5, int minTilt = 0, float tiltStep = 1.4142135623730951f, float rotateStepBase = 72)
    //

    /**
     * @param backend The detector/extractor you want to use as backend.
     *     @param maxTilt The highest power index of tilt factor. 5 is used in the paper as tilt sampling range n.
     *     @param minTilt The lowest power index of tilt factor. 0 is used in the paper.
     *     @param tiltStep Tilt sampling step \(\delta_t\) in Algorithm 1 in the paper.
     *     @param rotateStepBase Rotation sampling step factor b in Algorithm 1 in the paper.
     * @return automatically generated
     */
    public static AffineFeature create(Feature2D backend, int maxTilt, int minTilt, float tiltStep, float rotateStepBase) {
        return AffineFeature.__fromPtr__(create_0(backend.getNativeObjAddr(), maxTilt, minTilt, tiltStep, rotateStepBase));
    }

    /**
     * @param backend The detector/extractor you want to use as backend.
     *     @param maxTilt The highest power index of tilt factor. 5 is used in the paper as tilt sampling range n.
     *     @param minTilt The lowest power index of tilt factor. 0 is used in the paper.
     *     @param tiltStep Tilt sampling step \(\delta_t\) in Algorithm 1 in the paper.
     * @return automatically generated
     */
    public static AffineFeature create(Feature2D backend, int maxTilt, int minTilt, float tiltStep) {
        return AffineFeature.__fromPtr__(create_1(backend.getNativeObjAddr(), maxTilt, minTilt, tiltStep));
    }

    /**
     * @param backend The detector/extractor you want to use as backend.
     *     @param maxTilt The highest power index of tilt factor. 5 is used in the paper as tilt sampling range n.
     *     @param minTilt The lowest power index of tilt factor. 0 is used in the paper.
     * @return automatically generated
     */
    public static AffineFeature create(Feature2D backend, int maxTilt, int minTilt) {
        return AffineFeature.__fromPtr__(create_2(backend.getNativeObjAddr(), maxTilt, minTilt));
    }

    /**
     * @param backend The detector/extractor you want to use as backend.
     *     @param maxTilt The highest power index of tilt factor. 5 is used in the paper as tilt sampling range n.
     * @return automatically generated
     */
    public static AffineFeature create(Feature2D backend, int maxTilt) {
        return AffineFeature.__fromPtr__(create_3(backend.getNativeObjAddr(), maxTilt));
    }

    /**
     * @param backend The detector/extractor you want to use as backend.
     * @return automatically generated
     */
    public static AffineFeature create(Feature2D backend) {
        return AffineFeature.__fromPtr__(create_4(backend.getNativeObjAddr()));
    }


    //
    // C++:  void cv::AffineFeature::setViewParams(vector_float tilts, vector_float rolls)
    //

    public void setViewParams(MatOfFloat tilts, MatOfFloat rolls) {
        Mat tilts_mat = tilts;
        Mat rolls_mat = rolls;
        setViewParams_0(nativeObj, tilts_mat.nativeObj, rolls_mat.nativeObj);
    }


    //
    // C++:  void cv::AffineFeature::getViewParams(vector_float tilts, vector_float rolls)
    //

    public void getViewParams(MatOfFloat tilts, MatOfFloat rolls) {
        Mat tilts_mat = tilts;
        Mat rolls_mat = rolls;
        getViewParams_0(nativeObj, tilts_mat.nativeObj, rolls_mat.nativeObj);
    }


    //
    // C++:  String cv::AffineFeature::getDefaultName()
    //

    public String getDefaultName() {
        return getDefaultName_0(nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_AffineFeature cv::AffineFeature::create(Ptr_Feature2D backend, int maxTilt = 5, int minTilt = 0, float tiltStep = 1.4142135623730951f, float rotateStepBase = 72)
    private static native long create_0(long backend_nativeObj, int maxTilt, int minTilt, float tiltStep, float rotateStepBase);
    private static native long create_1(long backend_nativeObj, int maxTilt, int minTilt, float tiltStep);
    private static native long create_2(long backend_nativeObj, int maxTilt, int minTilt);
    private static native long create_3(long backend_nativeObj, int maxTilt);
    private static native long create_4(long backend_nativeObj);

    // C++:  void cv::AffineFeature::setViewParams(vector_float tilts, vector_float rolls)
    private static native void setViewParams_0(long nativeObj, long tilts_mat_nativeObj, long rolls_mat_nativeObj);

    // C++:  void cv::AffineFeature::getViewParams(vector_float tilts, vector_float rolls)
    private static native void getViewParams_0(long nativeObj, long tilts_mat_nativeObj, long rolls_mat_nativeObj);

    // C++:  String cv::AffineFeature::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
