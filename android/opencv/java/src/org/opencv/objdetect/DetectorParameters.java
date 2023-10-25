//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;



// C++: class DetectorParameters
/**
 * struct DetectorParameters is used by ArucoDetector
 */
public class DetectorParameters {

    protected final long nativeObj;
    protected DetectorParameters(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static DetectorParameters __fromPtr__(long addr) { return new DetectorParameters(addr); }

    //
    // C++:   cv::aruco::DetectorParameters::DetectorParameters()
    //

    public DetectorParameters() {
        nativeObj = DetectorParameters_0();
    }


    //
    // C++:  bool cv::aruco::DetectorParameters::readDetectorParameters(FileNode fn)
    //

    // Unknown type 'FileNode' (I), skipping the function


    //
    // C++:  bool cv::aruco::DetectorParameters::writeDetectorParameters(FileStorage fs, String name = String())
    //

    // Unknown type 'FileStorage' (I), skipping the function


    //
    // C++: int DetectorParameters::adaptiveThreshWinSizeMin
    //

    public int get_adaptiveThreshWinSizeMin() {
        return get_adaptiveThreshWinSizeMin_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::adaptiveThreshWinSizeMin
    //

    public void set_adaptiveThreshWinSizeMin(int adaptiveThreshWinSizeMin) {
        set_adaptiveThreshWinSizeMin_0(nativeObj, adaptiveThreshWinSizeMin);
    }


    //
    // C++: int DetectorParameters::adaptiveThreshWinSizeMax
    //

    public int get_adaptiveThreshWinSizeMax() {
        return get_adaptiveThreshWinSizeMax_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::adaptiveThreshWinSizeMax
    //

    public void set_adaptiveThreshWinSizeMax(int adaptiveThreshWinSizeMax) {
        set_adaptiveThreshWinSizeMax_0(nativeObj, adaptiveThreshWinSizeMax);
    }


    //
    // C++: int DetectorParameters::adaptiveThreshWinSizeStep
    //

    public int get_adaptiveThreshWinSizeStep() {
        return get_adaptiveThreshWinSizeStep_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::adaptiveThreshWinSizeStep
    //

    public void set_adaptiveThreshWinSizeStep(int adaptiveThreshWinSizeStep) {
        set_adaptiveThreshWinSizeStep_0(nativeObj, adaptiveThreshWinSizeStep);
    }


    //
    // C++: double DetectorParameters::adaptiveThreshConstant
    //

    public double get_adaptiveThreshConstant() {
        return get_adaptiveThreshConstant_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::adaptiveThreshConstant
    //

    public void set_adaptiveThreshConstant(double adaptiveThreshConstant) {
        set_adaptiveThreshConstant_0(nativeObj, adaptiveThreshConstant);
    }


    //
    // C++: double DetectorParameters::minMarkerPerimeterRate
    //

    public double get_minMarkerPerimeterRate() {
        return get_minMarkerPerimeterRate_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::minMarkerPerimeterRate
    //

    public void set_minMarkerPerimeterRate(double minMarkerPerimeterRate) {
        set_minMarkerPerimeterRate_0(nativeObj, minMarkerPerimeterRate);
    }


    //
    // C++: double DetectorParameters::maxMarkerPerimeterRate
    //

    public double get_maxMarkerPerimeterRate() {
        return get_maxMarkerPerimeterRate_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::maxMarkerPerimeterRate
    //

    public void set_maxMarkerPerimeterRate(double maxMarkerPerimeterRate) {
        set_maxMarkerPerimeterRate_0(nativeObj, maxMarkerPerimeterRate);
    }


    //
    // C++: double DetectorParameters::polygonalApproxAccuracyRate
    //

    public double get_polygonalApproxAccuracyRate() {
        return get_polygonalApproxAccuracyRate_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::polygonalApproxAccuracyRate
    //

    public void set_polygonalApproxAccuracyRate(double polygonalApproxAccuracyRate) {
        set_polygonalApproxAccuracyRate_0(nativeObj, polygonalApproxAccuracyRate);
    }


    //
    // C++: double DetectorParameters::minCornerDistanceRate
    //

    public double get_minCornerDistanceRate() {
        return get_minCornerDistanceRate_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::minCornerDistanceRate
    //

    public void set_minCornerDistanceRate(double minCornerDistanceRate) {
        set_minCornerDistanceRate_0(nativeObj, minCornerDistanceRate);
    }


    //
    // C++: int DetectorParameters::minDistanceToBorder
    //

    public int get_minDistanceToBorder() {
        return get_minDistanceToBorder_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::minDistanceToBorder
    //

    public void set_minDistanceToBorder(int minDistanceToBorder) {
        set_minDistanceToBorder_0(nativeObj, minDistanceToBorder);
    }


    //
    // C++: double DetectorParameters::minMarkerDistanceRate
    //

    public double get_minMarkerDistanceRate() {
        return get_minMarkerDistanceRate_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::minMarkerDistanceRate
    //

    public void set_minMarkerDistanceRate(double minMarkerDistanceRate) {
        set_minMarkerDistanceRate_0(nativeObj, minMarkerDistanceRate);
    }


    //
    // C++: int DetectorParameters::cornerRefinementMethod
    //

    public int get_cornerRefinementMethod() {
        return get_cornerRefinementMethod_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::cornerRefinementMethod
    //

    public void set_cornerRefinementMethod(int cornerRefinementMethod) {
        set_cornerRefinementMethod_0(nativeObj, cornerRefinementMethod);
    }


    //
    // C++: int DetectorParameters::cornerRefinementWinSize
    //

    public int get_cornerRefinementWinSize() {
        return get_cornerRefinementWinSize_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::cornerRefinementWinSize
    //

    public void set_cornerRefinementWinSize(int cornerRefinementWinSize) {
        set_cornerRefinementWinSize_0(nativeObj, cornerRefinementWinSize);
    }


    //
    // C++: int DetectorParameters::cornerRefinementMaxIterations
    //

    public int get_cornerRefinementMaxIterations() {
        return get_cornerRefinementMaxIterations_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::cornerRefinementMaxIterations
    //

    public void set_cornerRefinementMaxIterations(int cornerRefinementMaxIterations) {
        set_cornerRefinementMaxIterations_0(nativeObj, cornerRefinementMaxIterations);
    }


    //
    // C++: double DetectorParameters::cornerRefinementMinAccuracy
    //

    public double get_cornerRefinementMinAccuracy() {
        return get_cornerRefinementMinAccuracy_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::cornerRefinementMinAccuracy
    //

    public void set_cornerRefinementMinAccuracy(double cornerRefinementMinAccuracy) {
        set_cornerRefinementMinAccuracy_0(nativeObj, cornerRefinementMinAccuracy);
    }


    //
    // C++: int DetectorParameters::markerBorderBits
    //

    public int get_markerBorderBits() {
        return get_markerBorderBits_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::markerBorderBits
    //

    public void set_markerBorderBits(int markerBorderBits) {
        set_markerBorderBits_0(nativeObj, markerBorderBits);
    }


    //
    // C++: int DetectorParameters::perspectiveRemovePixelPerCell
    //

    public int get_perspectiveRemovePixelPerCell() {
        return get_perspectiveRemovePixelPerCell_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::perspectiveRemovePixelPerCell
    //

    public void set_perspectiveRemovePixelPerCell(int perspectiveRemovePixelPerCell) {
        set_perspectiveRemovePixelPerCell_0(nativeObj, perspectiveRemovePixelPerCell);
    }


    //
    // C++: double DetectorParameters::perspectiveRemoveIgnoredMarginPerCell
    //

    public double get_perspectiveRemoveIgnoredMarginPerCell() {
        return get_perspectiveRemoveIgnoredMarginPerCell_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::perspectiveRemoveIgnoredMarginPerCell
    //

    public void set_perspectiveRemoveIgnoredMarginPerCell(double perspectiveRemoveIgnoredMarginPerCell) {
        set_perspectiveRemoveIgnoredMarginPerCell_0(nativeObj, perspectiveRemoveIgnoredMarginPerCell);
    }


    //
    // C++: double DetectorParameters::maxErroneousBitsInBorderRate
    //

    public double get_maxErroneousBitsInBorderRate() {
        return get_maxErroneousBitsInBorderRate_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::maxErroneousBitsInBorderRate
    //

    public void set_maxErroneousBitsInBorderRate(double maxErroneousBitsInBorderRate) {
        set_maxErroneousBitsInBorderRate_0(nativeObj, maxErroneousBitsInBorderRate);
    }


    //
    // C++: double DetectorParameters::minOtsuStdDev
    //

    public double get_minOtsuStdDev() {
        return get_minOtsuStdDev_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::minOtsuStdDev
    //

    public void set_minOtsuStdDev(double minOtsuStdDev) {
        set_minOtsuStdDev_0(nativeObj, minOtsuStdDev);
    }


    //
    // C++: double DetectorParameters::errorCorrectionRate
    //

    public double get_errorCorrectionRate() {
        return get_errorCorrectionRate_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::errorCorrectionRate
    //

    public void set_errorCorrectionRate(double errorCorrectionRate) {
        set_errorCorrectionRate_0(nativeObj, errorCorrectionRate);
    }


    //
    // C++: float DetectorParameters::aprilTagQuadDecimate
    //

    public float get_aprilTagQuadDecimate() {
        return get_aprilTagQuadDecimate_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::aprilTagQuadDecimate
    //

    public void set_aprilTagQuadDecimate(float aprilTagQuadDecimate) {
        set_aprilTagQuadDecimate_0(nativeObj, aprilTagQuadDecimate);
    }


    //
    // C++: float DetectorParameters::aprilTagQuadSigma
    //

    public float get_aprilTagQuadSigma() {
        return get_aprilTagQuadSigma_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::aprilTagQuadSigma
    //

    public void set_aprilTagQuadSigma(float aprilTagQuadSigma) {
        set_aprilTagQuadSigma_0(nativeObj, aprilTagQuadSigma);
    }


    //
    // C++: int DetectorParameters::aprilTagMinClusterPixels
    //

    public int get_aprilTagMinClusterPixels() {
        return get_aprilTagMinClusterPixels_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::aprilTagMinClusterPixels
    //

    public void set_aprilTagMinClusterPixels(int aprilTagMinClusterPixels) {
        set_aprilTagMinClusterPixels_0(nativeObj, aprilTagMinClusterPixels);
    }


    //
    // C++: int DetectorParameters::aprilTagMaxNmaxima
    //

    public int get_aprilTagMaxNmaxima() {
        return get_aprilTagMaxNmaxima_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::aprilTagMaxNmaxima
    //

    public void set_aprilTagMaxNmaxima(int aprilTagMaxNmaxima) {
        set_aprilTagMaxNmaxima_0(nativeObj, aprilTagMaxNmaxima);
    }


    //
    // C++: float DetectorParameters::aprilTagCriticalRad
    //

    public float get_aprilTagCriticalRad() {
        return get_aprilTagCriticalRad_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::aprilTagCriticalRad
    //

    public void set_aprilTagCriticalRad(float aprilTagCriticalRad) {
        set_aprilTagCriticalRad_0(nativeObj, aprilTagCriticalRad);
    }


    //
    // C++: float DetectorParameters::aprilTagMaxLineFitMse
    //

    public float get_aprilTagMaxLineFitMse() {
        return get_aprilTagMaxLineFitMse_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::aprilTagMaxLineFitMse
    //

    public void set_aprilTagMaxLineFitMse(float aprilTagMaxLineFitMse) {
        set_aprilTagMaxLineFitMse_0(nativeObj, aprilTagMaxLineFitMse);
    }


    //
    // C++: int DetectorParameters::aprilTagMinWhiteBlackDiff
    //

    public int get_aprilTagMinWhiteBlackDiff() {
        return get_aprilTagMinWhiteBlackDiff_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::aprilTagMinWhiteBlackDiff
    //

    public void set_aprilTagMinWhiteBlackDiff(int aprilTagMinWhiteBlackDiff) {
        set_aprilTagMinWhiteBlackDiff_0(nativeObj, aprilTagMinWhiteBlackDiff);
    }


    //
    // C++: int DetectorParameters::aprilTagDeglitch
    //

    public int get_aprilTagDeglitch() {
        return get_aprilTagDeglitch_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::aprilTagDeglitch
    //

    public void set_aprilTagDeglitch(int aprilTagDeglitch) {
        set_aprilTagDeglitch_0(nativeObj, aprilTagDeglitch);
    }


    //
    // C++: bool DetectorParameters::detectInvertedMarker
    //

    public boolean get_detectInvertedMarker() {
        return get_detectInvertedMarker_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::detectInvertedMarker
    //

    public void set_detectInvertedMarker(boolean detectInvertedMarker) {
        set_detectInvertedMarker_0(nativeObj, detectInvertedMarker);
    }


    //
    // C++: bool DetectorParameters::useAruco3Detection
    //

    public boolean get_useAruco3Detection() {
        return get_useAruco3Detection_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::useAruco3Detection
    //

    public void set_useAruco3Detection(boolean useAruco3Detection) {
        set_useAruco3Detection_0(nativeObj, useAruco3Detection);
    }


    //
    // C++: int DetectorParameters::minSideLengthCanonicalImg
    //

    public int get_minSideLengthCanonicalImg() {
        return get_minSideLengthCanonicalImg_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::minSideLengthCanonicalImg
    //

    public void set_minSideLengthCanonicalImg(int minSideLengthCanonicalImg) {
        set_minSideLengthCanonicalImg_0(nativeObj, minSideLengthCanonicalImg);
    }


    //
    // C++: float DetectorParameters::minMarkerLengthRatioOriginalImg
    //

    public float get_minMarkerLengthRatioOriginalImg() {
        return get_minMarkerLengthRatioOriginalImg_0(nativeObj);
    }


    //
    // C++: void DetectorParameters::minMarkerLengthRatioOriginalImg
    //

    public void set_minMarkerLengthRatioOriginalImg(float minMarkerLengthRatioOriginalImg) {
        set_minMarkerLengthRatioOriginalImg_0(nativeObj, minMarkerLengthRatioOriginalImg);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::aruco::DetectorParameters::DetectorParameters()
    private static native long DetectorParameters_0();

    // C++: int DetectorParameters::adaptiveThreshWinSizeMin
    private static native int get_adaptiveThreshWinSizeMin_0(long nativeObj);

    // C++: void DetectorParameters::adaptiveThreshWinSizeMin
    private static native void set_adaptiveThreshWinSizeMin_0(long nativeObj, int adaptiveThreshWinSizeMin);

    // C++: int DetectorParameters::adaptiveThreshWinSizeMax
    private static native int get_adaptiveThreshWinSizeMax_0(long nativeObj);

    // C++: void DetectorParameters::adaptiveThreshWinSizeMax
    private static native void set_adaptiveThreshWinSizeMax_0(long nativeObj, int adaptiveThreshWinSizeMax);

    // C++: int DetectorParameters::adaptiveThreshWinSizeStep
    private static native int get_adaptiveThreshWinSizeStep_0(long nativeObj);

    // C++: void DetectorParameters::adaptiveThreshWinSizeStep
    private static native void set_adaptiveThreshWinSizeStep_0(long nativeObj, int adaptiveThreshWinSizeStep);

    // C++: double DetectorParameters::adaptiveThreshConstant
    private static native double get_adaptiveThreshConstant_0(long nativeObj);

    // C++: void DetectorParameters::adaptiveThreshConstant
    private static native void set_adaptiveThreshConstant_0(long nativeObj, double adaptiveThreshConstant);

    // C++: double DetectorParameters::minMarkerPerimeterRate
    private static native double get_minMarkerPerimeterRate_0(long nativeObj);

    // C++: void DetectorParameters::minMarkerPerimeterRate
    private static native void set_minMarkerPerimeterRate_0(long nativeObj, double minMarkerPerimeterRate);

    // C++: double DetectorParameters::maxMarkerPerimeterRate
    private static native double get_maxMarkerPerimeterRate_0(long nativeObj);

    // C++: void DetectorParameters::maxMarkerPerimeterRate
    private static native void set_maxMarkerPerimeterRate_0(long nativeObj, double maxMarkerPerimeterRate);

    // C++: double DetectorParameters::polygonalApproxAccuracyRate
    private static native double get_polygonalApproxAccuracyRate_0(long nativeObj);

    // C++: void DetectorParameters::polygonalApproxAccuracyRate
    private static native void set_polygonalApproxAccuracyRate_0(long nativeObj, double polygonalApproxAccuracyRate);

    // C++: double DetectorParameters::minCornerDistanceRate
    private static native double get_minCornerDistanceRate_0(long nativeObj);

    // C++: void DetectorParameters::minCornerDistanceRate
    private static native void set_minCornerDistanceRate_0(long nativeObj, double minCornerDistanceRate);

    // C++: int DetectorParameters::minDistanceToBorder
    private static native int get_minDistanceToBorder_0(long nativeObj);

    // C++: void DetectorParameters::minDistanceToBorder
    private static native void set_minDistanceToBorder_0(long nativeObj, int minDistanceToBorder);

    // C++: double DetectorParameters::minMarkerDistanceRate
    private static native double get_minMarkerDistanceRate_0(long nativeObj);

    // C++: void DetectorParameters::minMarkerDistanceRate
    private static native void set_minMarkerDistanceRate_0(long nativeObj, double minMarkerDistanceRate);

    // C++: int DetectorParameters::cornerRefinementMethod
    private static native int get_cornerRefinementMethod_0(long nativeObj);

    // C++: void DetectorParameters::cornerRefinementMethod
    private static native void set_cornerRefinementMethod_0(long nativeObj, int cornerRefinementMethod);

    // C++: int DetectorParameters::cornerRefinementWinSize
    private static native int get_cornerRefinementWinSize_0(long nativeObj);

    // C++: void DetectorParameters::cornerRefinementWinSize
    private static native void set_cornerRefinementWinSize_0(long nativeObj, int cornerRefinementWinSize);

    // C++: int DetectorParameters::cornerRefinementMaxIterations
    private static native int get_cornerRefinementMaxIterations_0(long nativeObj);

    // C++: void DetectorParameters::cornerRefinementMaxIterations
    private static native void set_cornerRefinementMaxIterations_0(long nativeObj, int cornerRefinementMaxIterations);

    // C++: double DetectorParameters::cornerRefinementMinAccuracy
    private static native double get_cornerRefinementMinAccuracy_0(long nativeObj);

    // C++: void DetectorParameters::cornerRefinementMinAccuracy
    private static native void set_cornerRefinementMinAccuracy_0(long nativeObj, double cornerRefinementMinAccuracy);

    // C++: int DetectorParameters::markerBorderBits
    private static native int get_markerBorderBits_0(long nativeObj);

    // C++: void DetectorParameters::markerBorderBits
    private static native void set_markerBorderBits_0(long nativeObj, int markerBorderBits);

    // C++: int DetectorParameters::perspectiveRemovePixelPerCell
    private static native int get_perspectiveRemovePixelPerCell_0(long nativeObj);

    // C++: void DetectorParameters::perspectiveRemovePixelPerCell
    private static native void set_perspectiveRemovePixelPerCell_0(long nativeObj, int perspectiveRemovePixelPerCell);

    // C++: double DetectorParameters::perspectiveRemoveIgnoredMarginPerCell
    private static native double get_perspectiveRemoveIgnoredMarginPerCell_0(long nativeObj);

    // C++: void DetectorParameters::perspectiveRemoveIgnoredMarginPerCell
    private static native void set_perspectiveRemoveIgnoredMarginPerCell_0(long nativeObj, double perspectiveRemoveIgnoredMarginPerCell);

    // C++: double DetectorParameters::maxErroneousBitsInBorderRate
    private static native double get_maxErroneousBitsInBorderRate_0(long nativeObj);

    // C++: void DetectorParameters::maxErroneousBitsInBorderRate
    private static native void set_maxErroneousBitsInBorderRate_0(long nativeObj, double maxErroneousBitsInBorderRate);

    // C++: double DetectorParameters::minOtsuStdDev
    private static native double get_minOtsuStdDev_0(long nativeObj);

    // C++: void DetectorParameters::minOtsuStdDev
    private static native void set_minOtsuStdDev_0(long nativeObj, double minOtsuStdDev);

    // C++: double DetectorParameters::errorCorrectionRate
    private static native double get_errorCorrectionRate_0(long nativeObj);

    // C++: void DetectorParameters::errorCorrectionRate
    private static native void set_errorCorrectionRate_0(long nativeObj, double errorCorrectionRate);

    // C++: float DetectorParameters::aprilTagQuadDecimate
    private static native float get_aprilTagQuadDecimate_0(long nativeObj);

    // C++: void DetectorParameters::aprilTagQuadDecimate
    private static native void set_aprilTagQuadDecimate_0(long nativeObj, float aprilTagQuadDecimate);

    // C++: float DetectorParameters::aprilTagQuadSigma
    private static native float get_aprilTagQuadSigma_0(long nativeObj);

    // C++: void DetectorParameters::aprilTagQuadSigma
    private static native void set_aprilTagQuadSigma_0(long nativeObj, float aprilTagQuadSigma);

    // C++: int DetectorParameters::aprilTagMinClusterPixels
    private static native int get_aprilTagMinClusterPixels_0(long nativeObj);

    // C++: void DetectorParameters::aprilTagMinClusterPixels
    private static native void set_aprilTagMinClusterPixels_0(long nativeObj, int aprilTagMinClusterPixels);

    // C++: int DetectorParameters::aprilTagMaxNmaxima
    private static native int get_aprilTagMaxNmaxima_0(long nativeObj);

    // C++: void DetectorParameters::aprilTagMaxNmaxima
    private static native void set_aprilTagMaxNmaxima_0(long nativeObj, int aprilTagMaxNmaxima);

    // C++: float DetectorParameters::aprilTagCriticalRad
    private static native float get_aprilTagCriticalRad_0(long nativeObj);

    // C++: void DetectorParameters::aprilTagCriticalRad
    private static native void set_aprilTagCriticalRad_0(long nativeObj, float aprilTagCriticalRad);

    // C++: float DetectorParameters::aprilTagMaxLineFitMse
    private static native float get_aprilTagMaxLineFitMse_0(long nativeObj);

    // C++: void DetectorParameters::aprilTagMaxLineFitMse
    private static native void set_aprilTagMaxLineFitMse_0(long nativeObj, float aprilTagMaxLineFitMse);

    // C++: int DetectorParameters::aprilTagMinWhiteBlackDiff
    private static native int get_aprilTagMinWhiteBlackDiff_0(long nativeObj);

    // C++: void DetectorParameters::aprilTagMinWhiteBlackDiff
    private static native void set_aprilTagMinWhiteBlackDiff_0(long nativeObj, int aprilTagMinWhiteBlackDiff);

    // C++: int DetectorParameters::aprilTagDeglitch
    private static native int get_aprilTagDeglitch_0(long nativeObj);

    // C++: void DetectorParameters::aprilTagDeglitch
    private static native void set_aprilTagDeglitch_0(long nativeObj, int aprilTagDeglitch);

    // C++: bool DetectorParameters::detectInvertedMarker
    private static native boolean get_detectInvertedMarker_0(long nativeObj);

    // C++: void DetectorParameters::detectInvertedMarker
    private static native void set_detectInvertedMarker_0(long nativeObj, boolean detectInvertedMarker);

    // C++: bool DetectorParameters::useAruco3Detection
    private static native boolean get_useAruco3Detection_0(long nativeObj);

    // C++: void DetectorParameters::useAruco3Detection
    private static native void set_useAruco3Detection_0(long nativeObj, boolean useAruco3Detection);

    // C++: int DetectorParameters::minSideLengthCanonicalImg
    private static native int get_minSideLengthCanonicalImg_0(long nativeObj);

    // C++: void DetectorParameters::minSideLengthCanonicalImg
    private static native void set_minSideLengthCanonicalImg_0(long nativeObj, int minSideLengthCanonicalImg);

    // C++: float DetectorParameters::minMarkerLengthRatioOriginalImg
    private static native float get_minMarkerLengthRatioOriginalImg_0(long nativeObj);

    // C++: void DetectorParameters::minMarkerLengthRatioOriginalImg
    private static native void set_minMarkerLengthRatioOriginalImg_0(long nativeObj, float minMarkerLengthRatioOriginalImg);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
