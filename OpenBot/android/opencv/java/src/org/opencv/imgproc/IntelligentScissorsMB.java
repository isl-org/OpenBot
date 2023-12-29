//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.imgproc;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.IntelligentScissorsMB;

// C++: class IntelligentScissorsMB
/**
 * Intelligent Scissors image segmentation
 *
 * This class is used to find the path (contour) between two points
 * which can be used for image segmentation.
 *
 * Usage example:
 * SNIPPET: snippets/imgproc_segmentation.cpp usage_example_intelligent_scissors
 *
 * Reference: &lt;a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.138.3811&amp;rep=rep1&amp;type=pdf"&gt;"Intelligent Scissors for Image Composition"&lt;/a&gt;
 * algorithm designed by Eric N. Mortensen and William A. Barrett, Brigham Young University
 * CITE: Mortensen95intelligentscissors
 */
public class IntelligentScissorsMB {

    protected final long nativeObj;
    protected IntelligentScissorsMB(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static IntelligentScissorsMB __fromPtr__(long addr) { return new IntelligentScissorsMB(addr); }

    //
    // C++:   cv::segmentation::IntelligentScissorsMB::IntelligentScissorsMB()
    //

    public IntelligentScissorsMB() {
        nativeObj = IntelligentScissorsMB_0();
    }


    //
    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::setWeights(float weight_non_edge, float weight_gradient_direction, float weight_gradient_magnitude)
    //

    /**
     * Specify weights of feature functions
     *
     * Consider keeping weights normalized (sum of weights equals to 1.0)
     * Discrete dynamic programming (DP) goal is minimization of costs between pixels.
     *
     * @param weight_non_edge Specify cost of non-edge pixels (default: 0.43f)
     * @param weight_gradient_direction Specify cost of gradient direction function (default: 0.43f)
     * @param weight_gradient_magnitude Specify cost of gradient magnitude function (default: 0.14f)
     * @return automatically generated
     */
    public IntelligentScissorsMB setWeights(float weight_non_edge, float weight_gradient_direction, float weight_gradient_magnitude) {
        return new IntelligentScissorsMB(setWeights_0(nativeObj, weight_non_edge, weight_gradient_direction, weight_gradient_magnitude));
    }


    //
    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::setGradientMagnitudeMaxLimit(float gradient_magnitude_threshold_max = 0.0f)
    //

    /**
     * Specify gradient magnitude max value threshold
     *
     * Zero limit value is used to disable gradient magnitude thresholding (default behavior, as described in original article).
     * Otherwize pixels with {@code gradient magnitude &gt;= threshold} have zero cost.
     *
     * <b>Note:</b> Thresholding should be used for images with irregular regions (to avoid stuck on parameters from high-contract areas, like embedded logos).
     *
     * @param gradient_magnitude_threshold_max Specify gradient magnitude max value threshold (default: 0, disabled)
     * @return automatically generated
     */
    public IntelligentScissorsMB setGradientMagnitudeMaxLimit(float gradient_magnitude_threshold_max) {
        return new IntelligentScissorsMB(setGradientMagnitudeMaxLimit_0(nativeObj, gradient_magnitude_threshold_max));
    }

    /**
     * Specify gradient magnitude max value threshold
     *
     * Zero limit value is used to disable gradient magnitude thresholding (default behavior, as described in original article).
     * Otherwize pixels with {@code gradient magnitude &gt;= threshold} have zero cost.
     *
     * <b>Note:</b> Thresholding should be used for images with irregular regions (to avoid stuck on parameters from high-contract areas, like embedded logos).
     *
     * @return automatically generated
     */
    public IntelligentScissorsMB setGradientMagnitudeMaxLimit() {
        return new IntelligentScissorsMB(setGradientMagnitudeMaxLimit_1(nativeObj));
    }


    //
    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::setEdgeFeatureZeroCrossingParameters(float gradient_magnitude_min_value = 0.0f)
    //

    /**
     * Switch to "Laplacian Zero-Crossing" edge feature extractor and specify its parameters
     *
     * This feature extractor is used by default according to article.
     *
     * Implementation has additional filtering for regions with low-amplitude noise.
     * This filtering is enabled through parameter of minimal gradient amplitude (use some small value 4, 8, 16).
     *
     * <b>Note:</b> Current implementation of this feature extractor is based on processing of grayscale images (color image is converted to grayscale image first).
     *
     * <b>Note:</b> Canny edge detector is a bit slower, but provides better results (especially on color images): use setEdgeFeatureCannyParameters().
     *
     * @param gradient_magnitude_min_value Minimal gradient magnitude value for edge pixels (default: 0, check is disabled)
     * @return automatically generated
     */
    public IntelligentScissorsMB setEdgeFeatureZeroCrossingParameters(float gradient_magnitude_min_value) {
        return new IntelligentScissorsMB(setEdgeFeatureZeroCrossingParameters_0(nativeObj, gradient_magnitude_min_value));
    }

    /**
     * Switch to "Laplacian Zero-Crossing" edge feature extractor and specify its parameters
     *
     * This feature extractor is used by default according to article.
     *
     * Implementation has additional filtering for regions with low-amplitude noise.
     * This filtering is enabled through parameter of minimal gradient amplitude (use some small value 4, 8, 16).
     *
     * <b>Note:</b> Current implementation of this feature extractor is based on processing of grayscale images (color image is converted to grayscale image first).
     *
     * <b>Note:</b> Canny edge detector is a bit slower, but provides better results (especially on color images): use setEdgeFeatureCannyParameters().
     *
     * @return automatically generated
     */
    public IntelligentScissorsMB setEdgeFeatureZeroCrossingParameters() {
        return new IntelligentScissorsMB(setEdgeFeatureZeroCrossingParameters_1(nativeObj));
    }


    //
    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::setEdgeFeatureCannyParameters(double threshold1, double threshold2, int apertureSize = 3, bool L2gradient = false)
    //

    /**
     * Switch edge feature extractor to use Canny edge detector
     *
     * <b>Note:</b> "Laplacian Zero-Crossing" feature extractor is used by default (following to original article)
     *
     * SEE: Canny
     * @param threshold1 automatically generated
     * @param threshold2 automatically generated
     * @param apertureSize automatically generated
     * @param L2gradient automatically generated
     * @return automatically generated
     */
    public IntelligentScissorsMB setEdgeFeatureCannyParameters(double threshold1, double threshold2, int apertureSize, boolean L2gradient) {
        return new IntelligentScissorsMB(setEdgeFeatureCannyParameters_0(nativeObj, threshold1, threshold2, apertureSize, L2gradient));
    }

    /**
     * Switch edge feature extractor to use Canny edge detector
     *
     * <b>Note:</b> "Laplacian Zero-Crossing" feature extractor is used by default (following to original article)
     *
     * SEE: Canny
     * @param threshold1 automatically generated
     * @param threshold2 automatically generated
     * @param apertureSize automatically generated
     * @return automatically generated
     */
    public IntelligentScissorsMB setEdgeFeatureCannyParameters(double threshold1, double threshold2, int apertureSize) {
        return new IntelligentScissorsMB(setEdgeFeatureCannyParameters_1(nativeObj, threshold1, threshold2, apertureSize));
    }

    /**
     * Switch edge feature extractor to use Canny edge detector
     *
     * <b>Note:</b> "Laplacian Zero-Crossing" feature extractor is used by default (following to original article)
     *
     * SEE: Canny
     * @param threshold1 automatically generated
     * @param threshold2 automatically generated
     * @return automatically generated
     */
    public IntelligentScissorsMB setEdgeFeatureCannyParameters(double threshold1, double threshold2) {
        return new IntelligentScissorsMB(setEdgeFeatureCannyParameters_2(nativeObj, threshold1, threshold2));
    }


    //
    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::applyImage(Mat image)
    //

    /**
     * Specify input image and extract image features
     *
     * @param image input image. Type is #CV_8UC1 / #CV_8UC3
     * @return automatically generated
     */
    public IntelligentScissorsMB applyImage(Mat image) {
        return new IntelligentScissorsMB(applyImage_0(nativeObj, image.nativeObj));
    }


    //
    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::applyImageFeatures(Mat non_edge, Mat gradient_direction, Mat gradient_magnitude, Mat image = Mat())
    //

    /**
     * Specify custom features of input image
     *
     * Customized advanced variant of applyImage() call.
     *
     * @param non_edge Specify cost of non-edge pixels. Type is CV_8UC1. Expected values are {@code {0, 1}}.
     * @param gradient_direction Specify gradient direction feature. Type is CV_32FC2. Values are expected to be normalized: {@code x^2 + y^2 == 1}
     * @param gradient_magnitude Specify cost of gradient magnitude function: Type is CV_32FC1. Values should be in range {@code [0, 1]}.
     * @param image <b>Optional parameter</b>. Must be specified if subset of features is specified (non-specified features are calculated internally)
     * @return automatically generated
     */
    public IntelligentScissorsMB applyImageFeatures(Mat non_edge, Mat gradient_direction, Mat gradient_magnitude, Mat image) {
        return new IntelligentScissorsMB(applyImageFeatures_0(nativeObj, non_edge.nativeObj, gradient_direction.nativeObj, gradient_magnitude.nativeObj, image.nativeObj));
    }

    /**
     * Specify custom features of input image
     *
     * Customized advanced variant of applyImage() call.
     *
     * @param non_edge Specify cost of non-edge pixels. Type is CV_8UC1. Expected values are {@code {0, 1}}.
     * @param gradient_direction Specify gradient direction feature. Type is CV_32FC2. Values are expected to be normalized: {@code x^2 + y^2 == 1}
     * @param gradient_magnitude Specify cost of gradient magnitude function: Type is CV_32FC1. Values should be in range {@code [0, 1]}.
     * @return automatically generated
     */
    public IntelligentScissorsMB applyImageFeatures(Mat non_edge, Mat gradient_direction, Mat gradient_magnitude) {
        return new IntelligentScissorsMB(applyImageFeatures_1(nativeObj, non_edge.nativeObj, gradient_direction.nativeObj, gradient_magnitude.nativeObj));
    }


    //
    // C++:  void cv::segmentation::IntelligentScissorsMB::buildMap(Point sourcePt)
    //

    /**
     * Prepares a map of optimal paths for the given source point on the image
     *
     * <b>Note:</b> applyImage() / applyImageFeatures() must be called before this call
     *
     * @param sourcePt The source point used to find the paths
     */
    public void buildMap(Point sourcePt) {
        buildMap_0(nativeObj, sourcePt.x, sourcePt.y);
    }


    //
    // C++:  void cv::segmentation::IntelligentScissorsMB::getContour(Point targetPt, Mat& contour, bool backward = false)
    //

    /**
     * Extracts optimal contour for the given target point on the image
     *
     * <b>Note:</b> buildMap() must be called before this call
     *
     * @param targetPt The target point
     * @param contour The list of pixels which contains optimal path between the source and the target points of the image. Type is CV_32SC2 (compatible with {@code std::vector&lt;Point&gt;})
     * @param backward Flag to indicate reverse order of retrived pixels (use "true" value to fetch points from the target to the source point)
     */
    public void getContour(Point targetPt, Mat contour, boolean backward) {
        getContour_0(nativeObj, targetPt.x, targetPt.y, contour.nativeObj, backward);
    }

    /**
     * Extracts optimal contour for the given target point on the image
     *
     * <b>Note:</b> buildMap() must be called before this call
     *
     * @param targetPt The target point
     * @param contour The list of pixels which contains optimal path between the source and the target points of the image. Type is CV_32SC2 (compatible with {@code std::vector&lt;Point&gt;})
     */
    public void getContour(Point targetPt, Mat contour) {
        getContour_1(nativeObj, targetPt.x, targetPt.y, contour.nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::segmentation::IntelligentScissorsMB::IntelligentScissorsMB()
    private static native long IntelligentScissorsMB_0();

    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::setWeights(float weight_non_edge, float weight_gradient_direction, float weight_gradient_magnitude)
    private static native long setWeights_0(long nativeObj, float weight_non_edge, float weight_gradient_direction, float weight_gradient_magnitude);

    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::setGradientMagnitudeMaxLimit(float gradient_magnitude_threshold_max = 0.0f)
    private static native long setGradientMagnitudeMaxLimit_0(long nativeObj, float gradient_magnitude_threshold_max);
    private static native long setGradientMagnitudeMaxLimit_1(long nativeObj);

    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::setEdgeFeatureZeroCrossingParameters(float gradient_magnitude_min_value = 0.0f)
    private static native long setEdgeFeatureZeroCrossingParameters_0(long nativeObj, float gradient_magnitude_min_value);
    private static native long setEdgeFeatureZeroCrossingParameters_1(long nativeObj);

    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::setEdgeFeatureCannyParameters(double threshold1, double threshold2, int apertureSize = 3, bool L2gradient = false)
    private static native long setEdgeFeatureCannyParameters_0(long nativeObj, double threshold1, double threshold2, int apertureSize, boolean L2gradient);
    private static native long setEdgeFeatureCannyParameters_1(long nativeObj, double threshold1, double threshold2, int apertureSize);
    private static native long setEdgeFeatureCannyParameters_2(long nativeObj, double threshold1, double threshold2);

    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::applyImage(Mat image)
    private static native long applyImage_0(long nativeObj, long image_nativeObj);

    // C++:  IntelligentScissorsMB cv::segmentation::IntelligentScissorsMB::applyImageFeatures(Mat non_edge, Mat gradient_direction, Mat gradient_magnitude, Mat image = Mat())
    private static native long applyImageFeatures_0(long nativeObj, long non_edge_nativeObj, long gradient_direction_nativeObj, long gradient_magnitude_nativeObj, long image_nativeObj);
    private static native long applyImageFeatures_1(long nativeObj, long non_edge_nativeObj, long gradient_direction_nativeObj, long gradient_magnitude_nativeObj);

    // C++:  void cv::segmentation::IntelligentScissorsMB::buildMap(Point sourcePt)
    private static native void buildMap_0(long nativeObj, double sourcePt_x, double sourcePt_y);

    // C++:  void cv::segmentation::IntelligentScissorsMB::getContour(Point targetPt, Mat& contour, bool backward = false)
    private static native void getContour_0(long nativeObj, double targetPt_x, double targetPt_y, long contour_nativeObj, boolean backward);
    private static native void getContour_1(long nativeObj, double targetPt_x, double targetPt_y, long contour_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
