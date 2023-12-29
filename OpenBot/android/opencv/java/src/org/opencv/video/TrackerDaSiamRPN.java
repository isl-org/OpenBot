//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.video.Tracker;
import org.opencv.video.TrackerDaSiamRPN;
import org.opencv.video.TrackerDaSiamRPN_Params;

// C++: class TrackerDaSiamRPN

public class TrackerDaSiamRPN extends Tracker {

    protected TrackerDaSiamRPN(long addr) { super(addr); }

    // internal usage only
    public static TrackerDaSiamRPN __fromPtr__(long addr) { return new TrackerDaSiamRPN(addr); }

    //
    // C++: static Ptr_TrackerDaSiamRPN cv::TrackerDaSiamRPN::create(TrackerDaSiamRPN_Params parameters = TrackerDaSiamRPN::Params())
    //

    /**
     * Constructor
     *     @param parameters DaSiamRPN parameters TrackerDaSiamRPN::Params
     * @return automatically generated
     */
    public static TrackerDaSiamRPN create(TrackerDaSiamRPN_Params parameters) {
        return TrackerDaSiamRPN.__fromPtr__(create_0(parameters.nativeObj));
    }

    /**
     * Constructor
     * @return automatically generated
     */
    public static TrackerDaSiamRPN create() {
        return TrackerDaSiamRPN.__fromPtr__(create_1());
    }


    //
    // C++:  float cv::TrackerDaSiamRPN::getTrackingScore()
    //

    /**
     * Return tracking score
     * @return automatically generated
     */
    public float getTrackingScore() {
        return getTrackingScore_0(nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_TrackerDaSiamRPN cv::TrackerDaSiamRPN::create(TrackerDaSiamRPN_Params parameters = TrackerDaSiamRPN::Params())
    private static native long create_0(long parameters_nativeObj);
    private static native long create_1();

    // C++:  float cv::TrackerDaSiamRPN::getTrackingScore()
    private static native float getTrackingScore_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
