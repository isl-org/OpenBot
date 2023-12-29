//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.MSER;
import org.opencv.utils.Converters;

// C++: class MSER
/**
 * Maximally stable extremal region extractor
 *
 * The class encapsulates all the parameters of the %MSER extraction algorithm (see [wiki
 * article](http://en.wikipedia.org/wiki/Maximally_stable_extremal_regions)).
 *
 * <ul>
 *   <li>
 *  there are two different implementation of %MSER: one for grey image, one for color image
 *   </li>
 * </ul>
 *
 * <ul>
 *   <li>
 *  the grey image algorithm is taken from: CITE: nister2008linear ;  the paper claims to be faster
 * than union-find method; it actually get 1.5~2m/s on my centrino L7200 1.2GHz laptop.
 *   </li>
 * </ul>
 *
 * <ul>
 *   <li>
 *  the color image algorithm is taken from: CITE: forssen2007maximally ; it should be much slower
 * than grey image method ( 3~4 times )
 *   </li>
 * </ul>
 *
 * <ul>
 *   <li>
 *  (Python) A complete example showing the use of the %MSER detector can be found at samples/python/mser.py
 *   </li>
 * </ul>
 */
public class MSER extends Feature2D {

    protected MSER(long addr) { super(addr); }

    // internal usage only
    public static MSER __fromPtr__(long addr) { return new MSER(addr); }

    //
    // C++: static Ptr_MSER cv::MSER::create(int delta = 5, int min_area = 60, int max_area = 14400, double max_variation = 0.25, double min_diversity = .2, int max_evolution = 200, double area_threshold = 1.01, double min_margin = 0.003, int edge_blur_size = 5)
    //

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     *     @param min_area prune the area which smaller than minArea
     *     @param max_area prune the area which bigger than maxArea
     *     @param max_variation prune the area have similar size to its children
     *     @param min_diversity for color image, trace back to cut off mser with diversity less than min_diversity
     *     @param max_evolution  for color image, the evolution steps
     *     @param area_threshold for color image, the area threshold to cause re-initialize
     *     @param min_margin for color image, ignore too small margin
     *     @param edge_blur_size for color image, the aperture size for edge blur
     * @return automatically generated
     */
    public static MSER create(int delta, int min_area, int max_area, double max_variation, double min_diversity, int max_evolution, double area_threshold, double min_margin, int edge_blur_size) {
        return MSER.__fromPtr__(create_0(delta, min_area, max_area, max_variation, min_diversity, max_evolution, area_threshold, min_margin, edge_blur_size));
    }

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     *     @param min_area prune the area which smaller than minArea
     *     @param max_area prune the area which bigger than maxArea
     *     @param max_variation prune the area have similar size to its children
     *     @param min_diversity for color image, trace back to cut off mser with diversity less than min_diversity
     *     @param max_evolution  for color image, the evolution steps
     *     @param area_threshold for color image, the area threshold to cause re-initialize
     *     @param min_margin for color image, ignore too small margin
     * @return automatically generated
     */
    public static MSER create(int delta, int min_area, int max_area, double max_variation, double min_diversity, int max_evolution, double area_threshold, double min_margin) {
        return MSER.__fromPtr__(create_1(delta, min_area, max_area, max_variation, min_diversity, max_evolution, area_threshold, min_margin));
    }

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     *     @param min_area prune the area which smaller than minArea
     *     @param max_area prune the area which bigger than maxArea
     *     @param max_variation prune the area have similar size to its children
     *     @param min_diversity for color image, trace back to cut off mser with diversity less than min_diversity
     *     @param max_evolution  for color image, the evolution steps
     *     @param area_threshold for color image, the area threshold to cause re-initialize
     * @return automatically generated
     */
    public static MSER create(int delta, int min_area, int max_area, double max_variation, double min_diversity, int max_evolution, double area_threshold) {
        return MSER.__fromPtr__(create_2(delta, min_area, max_area, max_variation, min_diversity, max_evolution, area_threshold));
    }

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     *     @param min_area prune the area which smaller than minArea
     *     @param max_area prune the area which bigger than maxArea
     *     @param max_variation prune the area have similar size to its children
     *     @param min_diversity for color image, trace back to cut off mser with diversity less than min_diversity
     *     @param max_evolution  for color image, the evolution steps
     * @return automatically generated
     */
    public static MSER create(int delta, int min_area, int max_area, double max_variation, double min_diversity, int max_evolution) {
        return MSER.__fromPtr__(create_3(delta, min_area, max_area, max_variation, min_diversity, max_evolution));
    }

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     *     @param min_area prune the area which smaller than minArea
     *     @param max_area prune the area which bigger than maxArea
     *     @param max_variation prune the area have similar size to its children
     *     @param min_diversity for color image, trace back to cut off mser with diversity less than min_diversity
     * @return automatically generated
     */
    public static MSER create(int delta, int min_area, int max_area, double max_variation, double min_diversity) {
        return MSER.__fromPtr__(create_4(delta, min_area, max_area, max_variation, min_diversity));
    }

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     *     @param min_area prune the area which smaller than minArea
     *     @param max_area prune the area which bigger than maxArea
     *     @param max_variation prune the area have similar size to its children
     * @return automatically generated
     */
    public static MSER create(int delta, int min_area, int max_area, double max_variation) {
        return MSER.__fromPtr__(create_5(delta, min_area, max_area, max_variation));
    }

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     *     @param min_area prune the area which smaller than minArea
     *     @param max_area prune the area which bigger than maxArea
     * @return automatically generated
     */
    public static MSER create(int delta, int min_area, int max_area) {
        return MSER.__fromPtr__(create_6(delta, min_area, max_area));
    }

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     *     @param min_area prune the area which smaller than minArea
     * @return automatically generated
     */
    public static MSER create(int delta, int min_area) {
        return MSER.__fromPtr__(create_7(delta, min_area));
    }

    /**
     * Full constructor for %MSER detector
     *
     *     @param delta it compares \((size_{i}-size_{i-delta})/size_{i-delta}\)
     * @return automatically generated
     */
    public static MSER create(int delta) {
        return MSER.__fromPtr__(create_8(delta));
    }

    /**
     * Full constructor for %MSER detector
     *
     * @return automatically generated
     */
    public static MSER create() {
        return MSER.__fromPtr__(create_9());
    }


    //
    // C++:  void cv::MSER::detectRegions(Mat image, vector_vector_Point& msers, vector_Rect& bboxes)
    //

    /**
     * Detect %MSER regions
     *
     *     @param image input image (8UC1, 8UC3 or 8UC4, must be greater or equal than 3x3)
     *     @param msers resulting list of point sets
     *     @param bboxes resulting bounding boxes
     */
    public void detectRegions(Mat image, List<MatOfPoint> msers, MatOfRect bboxes) {
        Mat msers_mat = new Mat();
        Mat bboxes_mat = bboxes;
        detectRegions_0(nativeObj, image.nativeObj, msers_mat.nativeObj, bboxes_mat.nativeObj);
        Converters.Mat_to_vector_vector_Point(msers_mat, msers);
        msers_mat.release();
    }


    //
    // C++:  void cv::MSER::setDelta(int delta)
    //

    public void setDelta(int delta) {
        setDelta_0(nativeObj, delta);
    }


    //
    // C++:  int cv::MSER::getDelta()
    //

    public int getDelta() {
        return getDelta_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setMinArea(int minArea)
    //

    public void setMinArea(int minArea) {
        setMinArea_0(nativeObj, minArea);
    }


    //
    // C++:  int cv::MSER::getMinArea()
    //

    public int getMinArea() {
        return getMinArea_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setMaxArea(int maxArea)
    //

    public void setMaxArea(int maxArea) {
        setMaxArea_0(nativeObj, maxArea);
    }


    //
    // C++:  int cv::MSER::getMaxArea()
    //

    public int getMaxArea() {
        return getMaxArea_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setMaxVariation(double maxVariation)
    //

    public void setMaxVariation(double maxVariation) {
        setMaxVariation_0(nativeObj, maxVariation);
    }


    //
    // C++:  double cv::MSER::getMaxVariation()
    //

    public double getMaxVariation() {
        return getMaxVariation_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setMinDiversity(double minDiversity)
    //

    public void setMinDiversity(double minDiversity) {
        setMinDiversity_0(nativeObj, minDiversity);
    }


    //
    // C++:  double cv::MSER::getMinDiversity()
    //

    public double getMinDiversity() {
        return getMinDiversity_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setMaxEvolution(int maxEvolution)
    //

    public void setMaxEvolution(int maxEvolution) {
        setMaxEvolution_0(nativeObj, maxEvolution);
    }


    //
    // C++:  int cv::MSER::getMaxEvolution()
    //

    public int getMaxEvolution() {
        return getMaxEvolution_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setAreaThreshold(double areaThreshold)
    //

    public void setAreaThreshold(double areaThreshold) {
        setAreaThreshold_0(nativeObj, areaThreshold);
    }


    //
    // C++:  double cv::MSER::getAreaThreshold()
    //

    public double getAreaThreshold() {
        return getAreaThreshold_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setMinMargin(double min_margin)
    //

    public void setMinMargin(double min_margin) {
        setMinMargin_0(nativeObj, min_margin);
    }


    //
    // C++:  double cv::MSER::getMinMargin()
    //

    public double getMinMargin() {
        return getMinMargin_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setEdgeBlurSize(int edge_blur_size)
    //

    public void setEdgeBlurSize(int edge_blur_size) {
        setEdgeBlurSize_0(nativeObj, edge_blur_size);
    }


    //
    // C++:  int cv::MSER::getEdgeBlurSize()
    //

    public int getEdgeBlurSize() {
        return getEdgeBlurSize_0(nativeObj);
    }


    //
    // C++:  void cv::MSER::setPass2Only(bool f)
    //

    public void setPass2Only(boolean f) {
        setPass2Only_0(nativeObj, f);
    }


    //
    // C++:  bool cv::MSER::getPass2Only()
    //

    public boolean getPass2Only() {
        return getPass2Only_0(nativeObj);
    }


    //
    // C++:  String cv::MSER::getDefaultName()
    //

    public String getDefaultName() {
        return getDefaultName_0(nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_MSER cv::MSER::create(int delta = 5, int min_area = 60, int max_area = 14400, double max_variation = 0.25, double min_diversity = .2, int max_evolution = 200, double area_threshold = 1.01, double min_margin = 0.003, int edge_blur_size = 5)
    private static native long create_0(int delta, int min_area, int max_area, double max_variation, double min_diversity, int max_evolution, double area_threshold, double min_margin, int edge_blur_size);
    private static native long create_1(int delta, int min_area, int max_area, double max_variation, double min_diversity, int max_evolution, double area_threshold, double min_margin);
    private static native long create_2(int delta, int min_area, int max_area, double max_variation, double min_diversity, int max_evolution, double area_threshold);
    private static native long create_3(int delta, int min_area, int max_area, double max_variation, double min_diversity, int max_evolution);
    private static native long create_4(int delta, int min_area, int max_area, double max_variation, double min_diversity);
    private static native long create_5(int delta, int min_area, int max_area, double max_variation);
    private static native long create_6(int delta, int min_area, int max_area);
    private static native long create_7(int delta, int min_area);
    private static native long create_8(int delta);
    private static native long create_9();

    // C++:  void cv::MSER::detectRegions(Mat image, vector_vector_Point& msers, vector_Rect& bboxes)
    private static native void detectRegions_0(long nativeObj, long image_nativeObj, long msers_mat_nativeObj, long bboxes_mat_nativeObj);

    // C++:  void cv::MSER::setDelta(int delta)
    private static native void setDelta_0(long nativeObj, int delta);

    // C++:  int cv::MSER::getDelta()
    private static native int getDelta_0(long nativeObj);

    // C++:  void cv::MSER::setMinArea(int minArea)
    private static native void setMinArea_0(long nativeObj, int minArea);

    // C++:  int cv::MSER::getMinArea()
    private static native int getMinArea_0(long nativeObj);

    // C++:  void cv::MSER::setMaxArea(int maxArea)
    private static native void setMaxArea_0(long nativeObj, int maxArea);

    // C++:  int cv::MSER::getMaxArea()
    private static native int getMaxArea_0(long nativeObj);

    // C++:  void cv::MSER::setMaxVariation(double maxVariation)
    private static native void setMaxVariation_0(long nativeObj, double maxVariation);

    // C++:  double cv::MSER::getMaxVariation()
    private static native double getMaxVariation_0(long nativeObj);

    // C++:  void cv::MSER::setMinDiversity(double minDiversity)
    private static native void setMinDiversity_0(long nativeObj, double minDiversity);

    // C++:  double cv::MSER::getMinDiversity()
    private static native double getMinDiversity_0(long nativeObj);

    // C++:  void cv::MSER::setMaxEvolution(int maxEvolution)
    private static native void setMaxEvolution_0(long nativeObj, int maxEvolution);

    // C++:  int cv::MSER::getMaxEvolution()
    private static native int getMaxEvolution_0(long nativeObj);

    // C++:  void cv::MSER::setAreaThreshold(double areaThreshold)
    private static native void setAreaThreshold_0(long nativeObj, double areaThreshold);

    // C++:  double cv::MSER::getAreaThreshold()
    private static native double getAreaThreshold_0(long nativeObj);

    // C++:  void cv::MSER::setMinMargin(double min_margin)
    private static native void setMinMargin_0(long nativeObj, double min_margin);

    // C++:  double cv::MSER::getMinMargin()
    private static native double getMinMargin_0(long nativeObj);

    // C++:  void cv::MSER::setEdgeBlurSize(int edge_blur_size)
    private static native void setEdgeBlurSize_0(long nativeObj, int edge_blur_size);

    // C++:  int cv::MSER::getEdgeBlurSize()
    private static native int getEdgeBlurSize_0(long nativeObj);

    // C++:  void cv::MSER::setPass2Only(bool f)
    private static native void setPass2Only_0(long nativeObj, boolean f);

    // C++:  bool cv::MSER::getPass2Only()
    private static native boolean getPass2Only_0(long nativeObj);

    // C++:  String cv::MSER::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
