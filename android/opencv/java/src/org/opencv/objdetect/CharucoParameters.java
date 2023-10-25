//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import org.opencv.core.Mat;

// C++: class CharucoParameters

public class CharucoParameters {

    protected final long nativeObj;
    protected CharucoParameters(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static CharucoParameters __fromPtr__(long addr) { return new CharucoParameters(addr); }

    //
    // C++:   cv::aruco::CharucoParameters::CharucoParameters()
    //

    public CharucoParameters() {
        nativeObj = CharucoParameters_0();
    }


    //
    // C++: Mat CharucoParameters::cameraMatrix
    //

    public Mat get_cameraMatrix() {
        return new Mat(get_cameraMatrix_0(nativeObj));
    }


    //
    // C++: void CharucoParameters::cameraMatrix
    //

    public void set_cameraMatrix(Mat cameraMatrix) {
        set_cameraMatrix_0(nativeObj, cameraMatrix.nativeObj);
    }


    //
    // C++: Mat CharucoParameters::distCoeffs
    //

    public Mat get_distCoeffs() {
        return new Mat(get_distCoeffs_0(nativeObj));
    }


    //
    // C++: void CharucoParameters::distCoeffs
    //

    public void set_distCoeffs(Mat distCoeffs) {
        set_distCoeffs_0(nativeObj, distCoeffs.nativeObj);
    }


    //
    // C++: int CharucoParameters::minMarkers
    //

    public int get_minMarkers() {
        return get_minMarkers_0(nativeObj);
    }


    //
    // C++: void CharucoParameters::minMarkers
    //

    public void set_minMarkers(int minMarkers) {
        set_minMarkers_0(nativeObj, minMarkers);
    }


    //
    // C++: bool CharucoParameters::tryRefineMarkers
    //

    public boolean get_tryRefineMarkers() {
        return get_tryRefineMarkers_0(nativeObj);
    }


    //
    // C++: void CharucoParameters::tryRefineMarkers
    //

    public void set_tryRefineMarkers(boolean tryRefineMarkers) {
        set_tryRefineMarkers_0(nativeObj, tryRefineMarkers);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::aruco::CharucoParameters::CharucoParameters()
    private static native long CharucoParameters_0();

    // C++: Mat CharucoParameters::cameraMatrix
    private static native long get_cameraMatrix_0(long nativeObj);

    // C++: void CharucoParameters::cameraMatrix
    private static native void set_cameraMatrix_0(long nativeObj, long cameraMatrix_nativeObj);

    // C++: Mat CharucoParameters::distCoeffs
    private static native long get_distCoeffs_0(long nativeObj);

    // C++: void CharucoParameters::distCoeffs
    private static native void set_distCoeffs_0(long nativeObj, long distCoeffs_nativeObj);

    // C++: int CharucoParameters::minMarkers
    private static native int get_minMarkers_0(long nativeObj);

    // C++: void CharucoParameters::minMarkers
    private static native void set_minMarkers_0(long nativeObj, int minMarkers);

    // C++: bool CharucoParameters::tryRefineMarkers
    private static native boolean get_tryRefineMarkers_0(long nativeObj);

    // C++: void CharucoParameters::tryRefineMarkers
    private static native void set_tryRefineMarkers_0(long nativeObj, boolean tryRefineMarkers);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
