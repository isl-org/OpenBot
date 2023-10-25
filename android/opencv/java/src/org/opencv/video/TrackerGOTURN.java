//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.video.Tracker;
import org.opencv.video.TrackerGOTURN;
import org.opencv.video.TrackerGOTURN_Params;

// C++: class TrackerGOTURN
/**
 * the GOTURN (Generic Object Tracking Using Regression Networks) tracker
 *
 * GOTURN (CITE: GOTURN) is kind of trackers based on Convolutional Neural Networks (CNN). While taking all advantages of CNN trackers,
 * GOTURN is much faster due to offline training without online fine-tuning nature.
 * GOTURN tracker addresses the problem of single target tracking: given a bounding box label of an object in the first frame of the video,
 * we track that object through the rest of the video. NOTE: Current method of GOTURN does not handle occlusions; however, it is fairly
 * robust to viewpoint changes, lighting changes, and deformations.
 * Inputs of GOTURN are two RGB patches representing Target and Search patches resized to 227x227.
 * Outputs of GOTURN are predicted bounding box coordinates, relative to Search patch coordinate system, in format X1,Y1,X2,Y2.
 * Original paper is here: &lt;http://davheld.github.io/GOTURN/GOTURN.pdf&gt;
 * As long as original authors implementation: &lt;https://github.com/davheld/GOTURN#train-the-tracker&gt;
 * Implementation of training algorithm is placed in separately here due to 3d-party dependencies:
 * &lt;https://github.com/Auron-X/GOTURN_Training_Toolkit&gt;
 * GOTURN architecture goturn.prototxt and trained model goturn.caffemodel are accessible on opencv_extra GitHub repository.
 */
public class TrackerGOTURN extends Tracker {

    protected TrackerGOTURN(long addr) { super(addr); }

    // internal usage only
    public static TrackerGOTURN __fromPtr__(long addr) { return new TrackerGOTURN(addr); }

    //
    // C++: static Ptr_TrackerGOTURN cv::TrackerGOTURN::create(TrackerGOTURN_Params parameters = TrackerGOTURN::Params())
    //

    /**
     * Constructor
     *     @param parameters GOTURN parameters TrackerGOTURN::Params
     * @return automatically generated
     */
    public static TrackerGOTURN create(TrackerGOTURN_Params parameters) {
        return TrackerGOTURN.__fromPtr__(create_0(parameters.nativeObj));
    }

    /**
     * Constructor
     * @return automatically generated
     */
    public static TrackerGOTURN create() {
        return TrackerGOTURN.__fromPtr__(create_1());
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_TrackerGOTURN cv::TrackerGOTURN::create(TrackerGOTURN_Params parameters = TrackerGOTURN::Params())
    private static native long create_0(long parameters_nativeObj);
    private static native long create_1();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
