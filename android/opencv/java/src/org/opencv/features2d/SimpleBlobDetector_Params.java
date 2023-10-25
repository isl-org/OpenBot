//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;



// C++: class Params

public class SimpleBlobDetector_Params {

    protected final long nativeObj;
    protected SimpleBlobDetector_Params(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static SimpleBlobDetector_Params __fromPtr__(long addr) { return new SimpleBlobDetector_Params(addr); }

    //
    // C++:   cv::SimpleBlobDetector::Params::Params()
    //

    public SimpleBlobDetector_Params() {
        nativeObj = SimpleBlobDetector_Params_0();
    }


    //
    // C++: float SimpleBlobDetector_Params::thresholdStep
    //

    public float get_thresholdStep() {
        return get_thresholdStep_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::thresholdStep
    //

    public void set_thresholdStep(float thresholdStep) {
        set_thresholdStep_0(nativeObj, thresholdStep);
    }


    //
    // C++: float SimpleBlobDetector_Params::minThreshold
    //

    public float get_minThreshold() {
        return get_minThreshold_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::minThreshold
    //

    public void set_minThreshold(float minThreshold) {
        set_minThreshold_0(nativeObj, minThreshold);
    }


    //
    // C++: float SimpleBlobDetector_Params::maxThreshold
    //

    public float get_maxThreshold() {
        return get_maxThreshold_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::maxThreshold
    //

    public void set_maxThreshold(float maxThreshold) {
        set_maxThreshold_0(nativeObj, maxThreshold);
    }


    //
    // C++: size_t SimpleBlobDetector_Params::minRepeatability
    //

    public long get_minRepeatability() {
        return get_minRepeatability_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::minRepeatability
    //

    public void set_minRepeatability(long minRepeatability) {
        set_minRepeatability_0(nativeObj, minRepeatability);
    }


    //
    // C++: float SimpleBlobDetector_Params::minDistBetweenBlobs
    //

    public float get_minDistBetweenBlobs() {
        return get_minDistBetweenBlobs_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::minDistBetweenBlobs
    //

    public void set_minDistBetweenBlobs(float minDistBetweenBlobs) {
        set_minDistBetweenBlobs_0(nativeObj, minDistBetweenBlobs);
    }


    //
    // C++: bool SimpleBlobDetector_Params::filterByColor
    //

    public boolean get_filterByColor() {
        return get_filterByColor_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::filterByColor
    //

    public void set_filterByColor(boolean filterByColor) {
        set_filterByColor_0(nativeObj, filterByColor);
    }


    //
    // C++: uchar SimpleBlobDetector_Params::blobColor
    //

    // Return type 'uchar' is not supported, skipping the function


    //
    // C++: void SimpleBlobDetector_Params::blobColor
    //

    // Unknown type 'uchar' (I), skipping the function


    //
    // C++: bool SimpleBlobDetector_Params::filterByArea
    //

    public boolean get_filterByArea() {
        return get_filterByArea_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::filterByArea
    //

    public void set_filterByArea(boolean filterByArea) {
        set_filterByArea_0(nativeObj, filterByArea);
    }


    //
    // C++: float SimpleBlobDetector_Params::minArea
    //

    public float get_minArea() {
        return get_minArea_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::minArea
    //

    public void set_minArea(float minArea) {
        set_minArea_0(nativeObj, minArea);
    }


    //
    // C++: float SimpleBlobDetector_Params::maxArea
    //

    public float get_maxArea() {
        return get_maxArea_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::maxArea
    //

    public void set_maxArea(float maxArea) {
        set_maxArea_0(nativeObj, maxArea);
    }


    //
    // C++: bool SimpleBlobDetector_Params::filterByCircularity
    //

    public boolean get_filterByCircularity() {
        return get_filterByCircularity_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::filterByCircularity
    //

    public void set_filterByCircularity(boolean filterByCircularity) {
        set_filterByCircularity_0(nativeObj, filterByCircularity);
    }


    //
    // C++: float SimpleBlobDetector_Params::minCircularity
    //

    public float get_minCircularity() {
        return get_minCircularity_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::minCircularity
    //

    public void set_minCircularity(float minCircularity) {
        set_minCircularity_0(nativeObj, minCircularity);
    }


    //
    // C++: float SimpleBlobDetector_Params::maxCircularity
    //

    public float get_maxCircularity() {
        return get_maxCircularity_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::maxCircularity
    //

    public void set_maxCircularity(float maxCircularity) {
        set_maxCircularity_0(nativeObj, maxCircularity);
    }


    //
    // C++: bool SimpleBlobDetector_Params::filterByInertia
    //

    public boolean get_filterByInertia() {
        return get_filterByInertia_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::filterByInertia
    //

    public void set_filterByInertia(boolean filterByInertia) {
        set_filterByInertia_0(nativeObj, filterByInertia);
    }


    //
    // C++: float SimpleBlobDetector_Params::minInertiaRatio
    //

    public float get_minInertiaRatio() {
        return get_minInertiaRatio_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::minInertiaRatio
    //

    public void set_minInertiaRatio(float minInertiaRatio) {
        set_minInertiaRatio_0(nativeObj, minInertiaRatio);
    }


    //
    // C++: float SimpleBlobDetector_Params::maxInertiaRatio
    //

    public float get_maxInertiaRatio() {
        return get_maxInertiaRatio_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::maxInertiaRatio
    //

    public void set_maxInertiaRatio(float maxInertiaRatio) {
        set_maxInertiaRatio_0(nativeObj, maxInertiaRatio);
    }


    //
    // C++: bool SimpleBlobDetector_Params::filterByConvexity
    //

    public boolean get_filterByConvexity() {
        return get_filterByConvexity_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::filterByConvexity
    //

    public void set_filterByConvexity(boolean filterByConvexity) {
        set_filterByConvexity_0(nativeObj, filterByConvexity);
    }


    //
    // C++: float SimpleBlobDetector_Params::minConvexity
    //

    public float get_minConvexity() {
        return get_minConvexity_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::minConvexity
    //

    public void set_minConvexity(float minConvexity) {
        set_minConvexity_0(nativeObj, minConvexity);
    }


    //
    // C++: float SimpleBlobDetector_Params::maxConvexity
    //

    public float get_maxConvexity() {
        return get_maxConvexity_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::maxConvexity
    //

    public void set_maxConvexity(float maxConvexity) {
        set_maxConvexity_0(nativeObj, maxConvexity);
    }


    //
    // C++: bool SimpleBlobDetector_Params::collectContours
    //

    public boolean get_collectContours() {
        return get_collectContours_0(nativeObj);
    }


    //
    // C++: void SimpleBlobDetector_Params::collectContours
    //

    public void set_collectContours(boolean collectContours) {
        set_collectContours_0(nativeObj, collectContours);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::SimpleBlobDetector::Params::Params()
    private static native long SimpleBlobDetector_Params_0();

    // C++: float SimpleBlobDetector_Params::thresholdStep
    private static native float get_thresholdStep_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::thresholdStep
    private static native void set_thresholdStep_0(long nativeObj, float thresholdStep);

    // C++: float SimpleBlobDetector_Params::minThreshold
    private static native float get_minThreshold_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::minThreshold
    private static native void set_minThreshold_0(long nativeObj, float minThreshold);

    // C++: float SimpleBlobDetector_Params::maxThreshold
    private static native float get_maxThreshold_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::maxThreshold
    private static native void set_maxThreshold_0(long nativeObj, float maxThreshold);

    // C++: size_t SimpleBlobDetector_Params::minRepeatability
    private static native long get_minRepeatability_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::minRepeatability
    private static native void set_minRepeatability_0(long nativeObj, long minRepeatability);

    // C++: float SimpleBlobDetector_Params::minDistBetweenBlobs
    private static native float get_minDistBetweenBlobs_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::minDistBetweenBlobs
    private static native void set_minDistBetweenBlobs_0(long nativeObj, float minDistBetweenBlobs);

    // C++: bool SimpleBlobDetector_Params::filterByColor
    private static native boolean get_filterByColor_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::filterByColor
    private static native void set_filterByColor_0(long nativeObj, boolean filterByColor);

    // C++: bool SimpleBlobDetector_Params::filterByArea
    private static native boolean get_filterByArea_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::filterByArea
    private static native void set_filterByArea_0(long nativeObj, boolean filterByArea);

    // C++: float SimpleBlobDetector_Params::minArea
    private static native float get_minArea_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::minArea
    private static native void set_minArea_0(long nativeObj, float minArea);

    // C++: float SimpleBlobDetector_Params::maxArea
    private static native float get_maxArea_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::maxArea
    private static native void set_maxArea_0(long nativeObj, float maxArea);

    // C++: bool SimpleBlobDetector_Params::filterByCircularity
    private static native boolean get_filterByCircularity_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::filterByCircularity
    private static native void set_filterByCircularity_0(long nativeObj, boolean filterByCircularity);

    // C++: float SimpleBlobDetector_Params::minCircularity
    private static native float get_minCircularity_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::minCircularity
    private static native void set_minCircularity_0(long nativeObj, float minCircularity);

    // C++: float SimpleBlobDetector_Params::maxCircularity
    private static native float get_maxCircularity_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::maxCircularity
    private static native void set_maxCircularity_0(long nativeObj, float maxCircularity);

    // C++: bool SimpleBlobDetector_Params::filterByInertia
    private static native boolean get_filterByInertia_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::filterByInertia
    private static native void set_filterByInertia_0(long nativeObj, boolean filterByInertia);

    // C++: float SimpleBlobDetector_Params::minInertiaRatio
    private static native float get_minInertiaRatio_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::minInertiaRatio
    private static native void set_minInertiaRatio_0(long nativeObj, float minInertiaRatio);

    // C++: float SimpleBlobDetector_Params::maxInertiaRatio
    private static native float get_maxInertiaRatio_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::maxInertiaRatio
    private static native void set_maxInertiaRatio_0(long nativeObj, float maxInertiaRatio);

    // C++: bool SimpleBlobDetector_Params::filterByConvexity
    private static native boolean get_filterByConvexity_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::filterByConvexity
    private static native void set_filterByConvexity_0(long nativeObj, boolean filterByConvexity);

    // C++: float SimpleBlobDetector_Params::minConvexity
    private static native float get_minConvexity_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::minConvexity
    private static native void set_minConvexity_0(long nativeObj, float minConvexity);

    // C++: float SimpleBlobDetector_Params::maxConvexity
    private static native float get_maxConvexity_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::maxConvexity
    private static native void set_maxConvexity_0(long nativeObj, float maxConvexity);

    // C++: bool SimpleBlobDetector_Params::collectContours
    private static native boolean get_collectContours_0(long nativeObj);

    // C++: void SimpleBlobDetector_Params::collectContours
    private static native void set_collectContours_0(long nativeObj, boolean collectContours);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
