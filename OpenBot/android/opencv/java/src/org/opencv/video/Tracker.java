//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

// C++: class Tracker
/**
 * Base abstract class for the long-term tracker
 */
public class Tracker {

    protected final long nativeObj;
    protected Tracker(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static Tracker __fromPtr__(long addr) { return new Tracker(addr); }

    //
    // C++:  void cv::Tracker::init(Mat image, Rect boundingBox)
    //

    /**
     * Initialize the tracker with a known bounding box that surrounded the target
     *     @param image The initial frame
     *     @param boundingBox The initial bounding box
     */
    public void init(Mat image, Rect boundingBox) {
        init_0(nativeObj, image.nativeObj, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }


    //
    // C++:  bool cv::Tracker::update(Mat image, Rect& boundingBox)
    //

    /**
     * Update the tracker, find the new most likely bounding box for the target
     *     @param image The current frame
     *     @param boundingBox The bounding box that represent the new target location, if true was returned, not
     *     modified otherwise
     *
     *     @return True means that target was located and false means that tracker cannot locate target in
     *     current frame. Note, that latter *does not* imply that tracker has failed, maybe target is indeed
     *     missing from the frame (say, out of sight)
     */
    public boolean update(Mat image, Rect boundingBox) {
        double[] boundingBox_out = new double[4];
        boolean retVal = update_0(nativeObj, image.nativeObj, boundingBox_out);
        if(boundingBox!=null){ boundingBox.x = (int)boundingBox_out[0]; boundingBox.y = (int)boundingBox_out[1]; boundingBox.width = (int)boundingBox_out[2]; boundingBox.height = (int)boundingBox_out[3]; } 
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  void cv::Tracker::init(Mat image, Rect boundingBox)
    private static native void init_0(long nativeObj, long image_nativeObj, int boundingBox_x, int boundingBox_y, int boundingBox_width, int boundingBox_height);

    // C++:  bool cv::Tracker::update(Mat image, Rect& boundingBox)
    private static native boolean update_0(long nativeObj, long image_nativeObj, double[] boundingBox_out);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
