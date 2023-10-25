//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;



// C++: class Params

public class QRCodeDetectorAruco_Params {

    protected final long nativeObj;
    protected QRCodeDetectorAruco_Params(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static QRCodeDetectorAruco_Params __fromPtr__(long addr) { return new QRCodeDetectorAruco_Params(addr); }

    //
    // C++:   cv::QRCodeDetectorAruco::Params::Params()
    //

    public QRCodeDetectorAruco_Params() {
        nativeObj = QRCodeDetectorAruco_Params_0();
    }


    //
    // C++: float QRCodeDetectorAruco_Params::minModuleSizeInPyramid
    //

    public float get_minModuleSizeInPyramid() {
        return get_minModuleSizeInPyramid_0(nativeObj);
    }


    //
    // C++: void QRCodeDetectorAruco_Params::minModuleSizeInPyramid
    //

    public void set_minModuleSizeInPyramid(float minModuleSizeInPyramid) {
        set_minModuleSizeInPyramid_0(nativeObj, minModuleSizeInPyramid);
    }


    //
    // C++: float QRCodeDetectorAruco_Params::maxRotation
    //

    public float get_maxRotation() {
        return get_maxRotation_0(nativeObj);
    }


    //
    // C++: void QRCodeDetectorAruco_Params::maxRotation
    //

    public void set_maxRotation(float maxRotation) {
        set_maxRotation_0(nativeObj, maxRotation);
    }


    //
    // C++: float QRCodeDetectorAruco_Params::maxModuleSizeMismatch
    //

    public float get_maxModuleSizeMismatch() {
        return get_maxModuleSizeMismatch_0(nativeObj);
    }


    //
    // C++: void QRCodeDetectorAruco_Params::maxModuleSizeMismatch
    //

    public void set_maxModuleSizeMismatch(float maxModuleSizeMismatch) {
        set_maxModuleSizeMismatch_0(nativeObj, maxModuleSizeMismatch);
    }


    //
    // C++: float QRCodeDetectorAruco_Params::maxTimingPatternMismatch
    //

    public float get_maxTimingPatternMismatch() {
        return get_maxTimingPatternMismatch_0(nativeObj);
    }


    //
    // C++: void QRCodeDetectorAruco_Params::maxTimingPatternMismatch
    //

    public void set_maxTimingPatternMismatch(float maxTimingPatternMismatch) {
        set_maxTimingPatternMismatch_0(nativeObj, maxTimingPatternMismatch);
    }


    //
    // C++: float QRCodeDetectorAruco_Params::maxPenalties
    //

    public float get_maxPenalties() {
        return get_maxPenalties_0(nativeObj);
    }


    //
    // C++: void QRCodeDetectorAruco_Params::maxPenalties
    //

    public void set_maxPenalties(float maxPenalties) {
        set_maxPenalties_0(nativeObj, maxPenalties);
    }


    //
    // C++: float QRCodeDetectorAruco_Params::maxColorsMismatch
    //

    public float get_maxColorsMismatch() {
        return get_maxColorsMismatch_0(nativeObj);
    }


    //
    // C++: void QRCodeDetectorAruco_Params::maxColorsMismatch
    //

    public void set_maxColorsMismatch(float maxColorsMismatch) {
        set_maxColorsMismatch_0(nativeObj, maxColorsMismatch);
    }


    //
    // C++: float QRCodeDetectorAruco_Params::scaleTimingPatternScore
    //

    public float get_scaleTimingPatternScore() {
        return get_scaleTimingPatternScore_0(nativeObj);
    }


    //
    // C++: void QRCodeDetectorAruco_Params::scaleTimingPatternScore
    //

    public void set_scaleTimingPatternScore(float scaleTimingPatternScore) {
        set_scaleTimingPatternScore_0(nativeObj, scaleTimingPatternScore);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::QRCodeDetectorAruco::Params::Params()
    private static native long QRCodeDetectorAruco_Params_0();

    // C++: float QRCodeDetectorAruco_Params::minModuleSizeInPyramid
    private static native float get_minModuleSizeInPyramid_0(long nativeObj);

    // C++: void QRCodeDetectorAruco_Params::minModuleSizeInPyramid
    private static native void set_minModuleSizeInPyramid_0(long nativeObj, float minModuleSizeInPyramid);

    // C++: float QRCodeDetectorAruco_Params::maxRotation
    private static native float get_maxRotation_0(long nativeObj);

    // C++: void QRCodeDetectorAruco_Params::maxRotation
    private static native void set_maxRotation_0(long nativeObj, float maxRotation);

    // C++: float QRCodeDetectorAruco_Params::maxModuleSizeMismatch
    private static native float get_maxModuleSizeMismatch_0(long nativeObj);

    // C++: void QRCodeDetectorAruco_Params::maxModuleSizeMismatch
    private static native void set_maxModuleSizeMismatch_0(long nativeObj, float maxModuleSizeMismatch);

    // C++: float QRCodeDetectorAruco_Params::maxTimingPatternMismatch
    private static native float get_maxTimingPatternMismatch_0(long nativeObj);

    // C++: void QRCodeDetectorAruco_Params::maxTimingPatternMismatch
    private static native void set_maxTimingPatternMismatch_0(long nativeObj, float maxTimingPatternMismatch);

    // C++: float QRCodeDetectorAruco_Params::maxPenalties
    private static native float get_maxPenalties_0(long nativeObj);

    // C++: void QRCodeDetectorAruco_Params::maxPenalties
    private static native void set_maxPenalties_0(long nativeObj, float maxPenalties);

    // C++: float QRCodeDetectorAruco_Params::maxColorsMismatch
    private static native float get_maxColorsMismatch_0(long nativeObj);

    // C++: void QRCodeDetectorAruco_Params::maxColorsMismatch
    private static native void set_maxColorsMismatch_0(long nativeObj, float maxColorsMismatch);

    // C++: float QRCodeDetectorAruco_Params::scaleTimingPatternScore
    private static native float get_scaleTimingPatternScore_0(long nativeObj);

    // C++: void QRCodeDetectorAruco_Params::scaleTimingPatternScore
    private static native void set_scaleTimingPatternScore_0(long nativeObj, float scaleTimingPatternScore);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
