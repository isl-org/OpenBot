//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;



// C++: class Params

public class TrackerMIL_Params {

    protected final long nativeObj;
    protected TrackerMIL_Params(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static TrackerMIL_Params __fromPtr__(long addr) { return new TrackerMIL_Params(addr); }

    //
    // C++:   cv::TrackerMIL::Params::Params()
    //

    public TrackerMIL_Params() {
        nativeObj = TrackerMIL_Params_0();
    }


    //
    // C++: float TrackerMIL_Params::samplerInitInRadius
    //

    public float get_samplerInitInRadius() {
        return get_samplerInitInRadius_0(nativeObj);
    }


    //
    // C++: void TrackerMIL_Params::samplerInitInRadius
    //

    public void set_samplerInitInRadius(float samplerInitInRadius) {
        set_samplerInitInRadius_0(nativeObj, samplerInitInRadius);
    }


    //
    // C++: int TrackerMIL_Params::samplerInitMaxNegNum
    //

    public int get_samplerInitMaxNegNum() {
        return get_samplerInitMaxNegNum_0(nativeObj);
    }


    //
    // C++: void TrackerMIL_Params::samplerInitMaxNegNum
    //

    public void set_samplerInitMaxNegNum(int samplerInitMaxNegNum) {
        set_samplerInitMaxNegNum_0(nativeObj, samplerInitMaxNegNum);
    }


    //
    // C++: float TrackerMIL_Params::samplerSearchWinSize
    //

    public float get_samplerSearchWinSize() {
        return get_samplerSearchWinSize_0(nativeObj);
    }


    //
    // C++: void TrackerMIL_Params::samplerSearchWinSize
    //

    public void set_samplerSearchWinSize(float samplerSearchWinSize) {
        set_samplerSearchWinSize_0(nativeObj, samplerSearchWinSize);
    }


    //
    // C++: float TrackerMIL_Params::samplerTrackInRadius
    //

    public float get_samplerTrackInRadius() {
        return get_samplerTrackInRadius_0(nativeObj);
    }


    //
    // C++: void TrackerMIL_Params::samplerTrackInRadius
    //

    public void set_samplerTrackInRadius(float samplerTrackInRadius) {
        set_samplerTrackInRadius_0(nativeObj, samplerTrackInRadius);
    }


    //
    // C++: int TrackerMIL_Params::samplerTrackMaxPosNum
    //

    public int get_samplerTrackMaxPosNum() {
        return get_samplerTrackMaxPosNum_0(nativeObj);
    }


    //
    // C++: void TrackerMIL_Params::samplerTrackMaxPosNum
    //

    public void set_samplerTrackMaxPosNum(int samplerTrackMaxPosNum) {
        set_samplerTrackMaxPosNum_0(nativeObj, samplerTrackMaxPosNum);
    }


    //
    // C++: int TrackerMIL_Params::samplerTrackMaxNegNum
    //

    public int get_samplerTrackMaxNegNum() {
        return get_samplerTrackMaxNegNum_0(nativeObj);
    }


    //
    // C++: void TrackerMIL_Params::samplerTrackMaxNegNum
    //

    public void set_samplerTrackMaxNegNum(int samplerTrackMaxNegNum) {
        set_samplerTrackMaxNegNum_0(nativeObj, samplerTrackMaxNegNum);
    }


    //
    // C++: int TrackerMIL_Params::featureSetNumFeatures
    //

    public int get_featureSetNumFeatures() {
        return get_featureSetNumFeatures_0(nativeObj);
    }


    //
    // C++: void TrackerMIL_Params::featureSetNumFeatures
    //

    public void set_featureSetNumFeatures(int featureSetNumFeatures) {
        set_featureSetNumFeatures_0(nativeObj, featureSetNumFeatures);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::TrackerMIL::Params::Params()
    private static native long TrackerMIL_Params_0();

    // C++: float TrackerMIL_Params::samplerInitInRadius
    private static native float get_samplerInitInRadius_0(long nativeObj);

    // C++: void TrackerMIL_Params::samplerInitInRadius
    private static native void set_samplerInitInRadius_0(long nativeObj, float samplerInitInRadius);

    // C++: int TrackerMIL_Params::samplerInitMaxNegNum
    private static native int get_samplerInitMaxNegNum_0(long nativeObj);

    // C++: void TrackerMIL_Params::samplerInitMaxNegNum
    private static native void set_samplerInitMaxNegNum_0(long nativeObj, int samplerInitMaxNegNum);

    // C++: float TrackerMIL_Params::samplerSearchWinSize
    private static native float get_samplerSearchWinSize_0(long nativeObj);

    // C++: void TrackerMIL_Params::samplerSearchWinSize
    private static native void set_samplerSearchWinSize_0(long nativeObj, float samplerSearchWinSize);

    // C++: float TrackerMIL_Params::samplerTrackInRadius
    private static native float get_samplerTrackInRadius_0(long nativeObj);

    // C++: void TrackerMIL_Params::samplerTrackInRadius
    private static native void set_samplerTrackInRadius_0(long nativeObj, float samplerTrackInRadius);

    // C++: int TrackerMIL_Params::samplerTrackMaxPosNum
    private static native int get_samplerTrackMaxPosNum_0(long nativeObj);

    // C++: void TrackerMIL_Params::samplerTrackMaxPosNum
    private static native void set_samplerTrackMaxPosNum_0(long nativeObj, int samplerTrackMaxPosNum);

    // C++: int TrackerMIL_Params::samplerTrackMaxNegNum
    private static native int get_samplerTrackMaxNegNum_0(long nativeObj);

    // C++: void TrackerMIL_Params::samplerTrackMaxNegNum
    private static native void set_samplerTrackMaxNegNum_0(long nativeObj, int samplerTrackMaxNegNum);

    // C++: int TrackerMIL_Params::featureSetNumFeatures
    private static native int get_featureSetNumFeatures_0(long nativeObj);

    // C++: void TrackerMIL_Params::featureSetNumFeatures
    private static native void set_featureSetNumFeatures_0(long nativeObj, int featureSetNumFeatures);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
