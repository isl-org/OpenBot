//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.video.Tracker;
import org.opencv.video.TrackerNano;
import org.opencv.video.TrackerNano_Params;

// C++: class TrackerNano
/**
 * the Nano tracker is a super lightweight dnn-based general object tracking.
 *
 * Nano tracker is much faster and extremely lightweight due to special model structure, the whole model size is about 1.9 MB.
 * Nano tracker needs two models: one for feature extraction (backbone) and the another for localization (neckhead).
 * Model download link: https://github.com/HonglinChu/SiamTrackers/tree/master/NanoTrack/models/nanotrackv2
 * Original repo is here: https://github.com/HonglinChu/NanoTrack
 * Author: HongLinChu, 1628464345@qq.com
 */
public class TrackerNano extends Tracker {

    protected TrackerNano(long addr) { super(addr); }

    // internal usage only
    public static TrackerNano __fromPtr__(long addr) { return new TrackerNano(addr); }

    //
    // C++: static Ptr_TrackerNano cv::TrackerNano::create(TrackerNano_Params parameters = TrackerNano::Params())
    //

    /**
     * Constructor
     *     @param parameters NanoTrack parameters TrackerNano::Params
     * @return automatically generated
     */
    public static TrackerNano create(TrackerNano_Params parameters) {
        return TrackerNano.__fromPtr__(create_0(parameters.nativeObj));
    }

    /**
     * Constructor
     * @return automatically generated
     */
    public static TrackerNano create() {
        return TrackerNano.__fromPtr__(create_1());
    }


    //
    // C++:  float cv::TrackerNano::getTrackingScore()
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



    // C++: static Ptr_TrackerNano cv::TrackerNano::create(TrackerNano_Params parameters = TrackerNano::Params())
    private static native long create_0(long parameters_nativeObj);
    private static native long create_1();

    // C++:  float cv::TrackerNano::getTrackingScore()
    private static native float getTrackingScore_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
