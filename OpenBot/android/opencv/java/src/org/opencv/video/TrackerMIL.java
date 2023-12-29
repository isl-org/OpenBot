//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.video.Tracker;
import org.opencv.video.TrackerMIL;
import org.opencv.video.TrackerMIL_Params;

// C++: class TrackerMIL
/**
 * The MIL algorithm trains a classifier in an online manner to separate the object from the
 * background.
 *
 * Multiple Instance Learning avoids the drift problem for a robust tracking. The implementation is
 * based on CITE: MIL .
 *
 * Original code can be found here &lt;http://vision.ucsd.edu/~bbabenko/project_miltrack.shtml&gt;
 */
public class TrackerMIL extends Tracker {

    protected TrackerMIL(long addr) { super(addr); }

    // internal usage only
    public static TrackerMIL __fromPtr__(long addr) { return new TrackerMIL(addr); }

    //
    // C++: static Ptr_TrackerMIL cv::TrackerMIL::create(TrackerMIL_Params parameters = TrackerMIL::Params())
    //

    /**
     * Create MIL tracker instance
     * @param parameters MIL parameters TrackerMIL::Params
     * @return automatically generated
     */
    public static TrackerMIL create(TrackerMIL_Params parameters) {
        return TrackerMIL.__fromPtr__(create_0(parameters.nativeObj));
    }

    /**
     * Create MIL tracker instance
     * @return automatically generated
     */
    public static TrackerMIL create() {
        return TrackerMIL.__fromPtr__(create_1());
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_TrackerMIL cv::TrackerMIL::create(TrackerMIL_Params parameters = TrackerMIL::Params())
    private static native long create_0(long parameters_nativeObj);
    private static native long create_1();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
