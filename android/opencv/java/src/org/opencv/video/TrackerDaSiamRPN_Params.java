//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;



// C++: class Params

public class TrackerDaSiamRPN_Params {

    protected final long nativeObj;
    protected TrackerDaSiamRPN_Params(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static TrackerDaSiamRPN_Params __fromPtr__(long addr) { return new TrackerDaSiamRPN_Params(addr); }

    //
    // C++:   cv::TrackerDaSiamRPN::Params::Params()
    //

    public TrackerDaSiamRPN_Params() {
        nativeObj = TrackerDaSiamRPN_Params_0();
    }


    //
    // C++: string TrackerDaSiamRPN_Params::model
    //

    public String get_model() {
        return get_model_0(nativeObj);
    }


    //
    // C++: void TrackerDaSiamRPN_Params::model
    //

    public void set_model(String model) {
        set_model_0(nativeObj, model);
    }


    //
    // C++: string TrackerDaSiamRPN_Params::kernel_cls1
    //

    public String get_kernel_cls1() {
        return get_kernel_cls1_0(nativeObj);
    }


    //
    // C++: void TrackerDaSiamRPN_Params::kernel_cls1
    //

    public void set_kernel_cls1(String kernel_cls1) {
        set_kernel_cls1_0(nativeObj, kernel_cls1);
    }


    //
    // C++: string TrackerDaSiamRPN_Params::kernel_r1
    //

    public String get_kernel_r1() {
        return get_kernel_r1_0(nativeObj);
    }


    //
    // C++: void TrackerDaSiamRPN_Params::kernel_r1
    //

    public void set_kernel_r1(String kernel_r1) {
        set_kernel_r1_0(nativeObj, kernel_r1);
    }


    //
    // C++: int TrackerDaSiamRPN_Params::backend
    //

    public int get_backend() {
        return get_backend_0(nativeObj);
    }


    //
    // C++: void TrackerDaSiamRPN_Params::backend
    //

    public void set_backend(int backend) {
        set_backend_0(nativeObj, backend);
    }


    //
    // C++: int TrackerDaSiamRPN_Params::target
    //

    public int get_target() {
        return get_target_0(nativeObj);
    }


    //
    // C++: void TrackerDaSiamRPN_Params::target
    //

    public void set_target(int target) {
        set_target_0(nativeObj, target);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::TrackerDaSiamRPN::Params::Params()
    private static native long TrackerDaSiamRPN_Params_0();

    // C++: string TrackerDaSiamRPN_Params::model
    private static native String get_model_0(long nativeObj);

    // C++: void TrackerDaSiamRPN_Params::model
    private static native void set_model_0(long nativeObj, String model);

    // C++: string TrackerDaSiamRPN_Params::kernel_cls1
    private static native String get_kernel_cls1_0(long nativeObj);

    // C++: void TrackerDaSiamRPN_Params::kernel_cls1
    private static native void set_kernel_cls1_0(long nativeObj, String kernel_cls1);

    // C++: string TrackerDaSiamRPN_Params::kernel_r1
    private static native String get_kernel_r1_0(long nativeObj);

    // C++: void TrackerDaSiamRPN_Params::kernel_r1
    private static native void set_kernel_r1_0(long nativeObj, String kernel_r1);

    // C++: int TrackerDaSiamRPN_Params::backend
    private static native int get_backend_0(long nativeObj);

    // C++: void TrackerDaSiamRPN_Params::backend
    private static native void set_backend_0(long nativeObj, int backend);

    // C++: int TrackerDaSiamRPN_Params::target
    private static native int get_target_0(long nativeObj);

    // C++: void TrackerDaSiamRPN_Params::target
    private static native void set_target_0(long nativeObj, int target);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
