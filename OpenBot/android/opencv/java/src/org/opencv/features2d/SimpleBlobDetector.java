//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.SimpleBlobDetector;
import org.opencv.features2d.SimpleBlobDetector_Params;
import org.opencv.utils.Converters;

// C++: class SimpleBlobDetector
/**
 * Class for extracting blobs from an image. :
 *
 * The class implements a simple algorithm for extracting blobs from an image:
 *
 * 1.  Convert the source image to binary images by applying thresholding with several thresholds from
 *     minThreshold (inclusive) to maxThreshold (exclusive) with distance thresholdStep between
 *     neighboring thresholds.
 * 2.  Extract connected components from every binary image by findContours and calculate their
 *     centers.
 * 3.  Group centers from several binary images by their coordinates. Close centers form one group that
 *     corresponds to one blob, which is controlled by the minDistBetweenBlobs parameter.
 * 4.  From the groups, estimate final centers of blobs and their radiuses and return as locations and
 *     sizes of keypoints.
 *
 * This class performs several filtrations of returned blobs. You should set filterBy\* to true/false
 * to turn on/off corresponding filtration. Available filtrations:
 *
 * <ul>
 *   <li>
 *    <b>By color</b>. This filter compares the intensity of a binary image at the center of a blob to
 * blobColor. If they differ, the blob is filtered out. Use blobColor = 0 to extract dark blobs
 * and blobColor = 255 to extract light blobs.
 *   </li>
 *   <li>
 *    <b>By area</b>. Extracted blobs have an area between minArea (inclusive) and maxArea (exclusive).
 *   </li>
 *   <li>
 *    <b>By circularity</b>. Extracted blobs have circularity
 * (\(\frac{4*\pi*Area}{perimeter * perimeter}\)) between minCircularity (inclusive) and
 * maxCircularity (exclusive).
 *   </li>
 *   <li>
 *    <b>By ratio of the minimum inertia to maximum inertia</b>. Extracted blobs have this ratio
 * between minInertiaRatio (inclusive) and maxInertiaRatio (exclusive).
 *   </li>
 *   <li>
 *    <b>By convexity</b>. Extracted blobs have convexity (area / area of blob convex hull) between
 * minConvexity (inclusive) and maxConvexity (exclusive).
 *   </li>
 * </ul>
 *
 * Default values of parameters are tuned to extract dark circular blobs.
 */
public class SimpleBlobDetector extends Feature2D {

    protected SimpleBlobDetector(long addr) { super(addr); }

    // internal usage only
    public static SimpleBlobDetector __fromPtr__(long addr) { return new SimpleBlobDetector(addr); }

    //
    // C++: static Ptr_SimpleBlobDetector cv::SimpleBlobDetector::create(SimpleBlobDetector_Params parameters = SimpleBlobDetector::Params())
    //

    public static SimpleBlobDetector create(SimpleBlobDetector_Params parameters) {
        return SimpleBlobDetector.__fromPtr__(create_0(parameters.nativeObj));
    }

    public static SimpleBlobDetector create() {
        return SimpleBlobDetector.__fromPtr__(create_1());
    }


    //
    // C++:  void cv::SimpleBlobDetector::setParams(SimpleBlobDetector_Params params)
    //

    public void setParams(SimpleBlobDetector_Params params) {
        setParams_0(nativeObj, params.nativeObj);
    }


    //
    // C++:  SimpleBlobDetector_Params cv::SimpleBlobDetector::getParams()
    //

    public SimpleBlobDetector_Params getParams() {
        return new SimpleBlobDetector_Params(getParams_0(nativeObj));
    }


    //
    // C++:  String cv::SimpleBlobDetector::getDefaultName()
    //

    public String getDefaultName() {
        return getDefaultName_0(nativeObj);
    }


    //
    // C++:  vector_vector_Point cv::SimpleBlobDetector::getBlobContours()
    //

    public List<MatOfPoint> getBlobContours() {
        List<MatOfPoint> retVal = new ArrayList<MatOfPoint>();
        Mat retValMat = new Mat(getBlobContours_0(nativeObj));
        Converters.Mat_to_vector_vector_Point(retValMat, retVal);
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_SimpleBlobDetector cv::SimpleBlobDetector::create(SimpleBlobDetector_Params parameters = SimpleBlobDetector::Params())
    private static native long create_0(long parameters_nativeObj);
    private static native long create_1();

    // C++:  void cv::SimpleBlobDetector::setParams(SimpleBlobDetector_Params params)
    private static native void setParams_0(long nativeObj, long params_nativeObj);

    // C++:  SimpleBlobDetector_Params cv::SimpleBlobDetector::getParams()
    private static native long getParams_0(long nativeObj);

    // C++:  String cv::SimpleBlobDetector::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // C++:  vector_vector_Point cv::SimpleBlobDetector::getBlobContours()
    private static native long getBlobContours_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
