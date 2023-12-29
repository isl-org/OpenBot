//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;



// C++: class RefineParameters
/**
 * struct RefineParameters is used by ArucoDetector
 */
public class RefineParameters {

    protected final long nativeObj;
    protected RefineParameters(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static RefineParameters __fromPtr__(long addr) { return new RefineParameters(addr); }

    //
    // C++:   cv::aruco::RefineParameters::RefineParameters(float minRepDistance = 10.f, float errorCorrectionRate = 3.f, bool checkAllOrders = true)
    //

    public RefineParameters(float minRepDistance, float errorCorrectionRate, boolean checkAllOrders) {
        nativeObj = RefineParameters_0(minRepDistance, errorCorrectionRate, checkAllOrders);
    }

    public RefineParameters(float minRepDistance, float errorCorrectionRate) {
        nativeObj = RefineParameters_1(minRepDistance, errorCorrectionRate);
    }

    public RefineParameters(float minRepDistance) {
        nativeObj = RefineParameters_2(minRepDistance);
    }

    public RefineParameters() {
        nativeObj = RefineParameters_3();
    }


    //
    // C++:  bool cv::aruco::RefineParameters::readRefineParameters(FileNode fn)
    //

    // Unknown type 'FileNode' (I), skipping the function


    //
    // C++:  bool cv::aruco::RefineParameters::writeRefineParameters(FileStorage fs, String name = String())
    //

    // Unknown type 'FileStorage' (I), skipping the function


    //
    // C++: float RefineParameters::minRepDistance
    //

    public float get_minRepDistance() {
        return get_minRepDistance_0(nativeObj);
    }


    //
    // C++: void RefineParameters::minRepDistance
    //

    public void set_minRepDistance(float minRepDistance) {
        set_minRepDistance_0(nativeObj, minRepDistance);
    }


    //
    // C++: float RefineParameters::errorCorrectionRate
    //

    public float get_errorCorrectionRate() {
        return get_errorCorrectionRate_0(nativeObj);
    }


    //
    // C++: void RefineParameters::errorCorrectionRate
    //

    public void set_errorCorrectionRate(float errorCorrectionRate) {
        set_errorCorrectionRate_0(nativeObj, errorCorrectionRate);
    }


    //
    // C++: bool RefineParameters::checkAllOrders
    //

    public boolean get_checkAllOrders() {
        return get_checkAllOrders_0(nativeObj);
    }


    //
    // C++: void RefineParameters::checkAllOrders
    //

    public void set_checkAllOrders(boolean checkAllOrders) {
        set_checkAllOrders_0(nativeObj, checkAllOrders);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::aruco::RefineParameters::RefineParameters(float minRepDistance = 10.f, float errorCorrectionRate = 3.f, bool checkAllOrders = true)
    private static native long RefineParameters_0(float minRepDistance, float errorCorrectionRate, boolean checkAllOrders);
    private static native long RefineParameters_1(float minRepDistance, float errorCorrectionRate);
    private static native long RefineParameters_2(float minRepDistance);
    private static native long RefineParameters_3();

    // C++: float RefineParameters::minRepDistance
    private static native float get_minRepDistance_0(long nativeObj);

    // C++: void RefineParameters::minRepDistance
    private static native void set_minRepDistance_0(long nativeObj, float minRepDistance);

    // C++: float RefineParameters::errorCorrectionRate
    private static native float get_errorCorrectionRate_0(long nativeObj);

    // C++: void RefineParameters::errorCorrectionRate
    private static native void set_errorCorrectionRate_0(long nativeObj, float errorCorrectionRate);

    // C++: bool RefineParameters::checkAllOrders
    private static native boolean get_checkAllOrders_0(long nativeObj);

    // C++: void RefineParameters::checkAllOrders
    private static native void set_checkAllOrders_0(long nativeObj, boolean checkAllOrders);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
