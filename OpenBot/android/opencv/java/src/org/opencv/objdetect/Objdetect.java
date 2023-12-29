//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.Dictionary;
import org.opencv.utils.Converters;

// C++: class Objdetect

public class Objdetect {

    // C++: enum <unnamed>
    public static final int
            CASCADE_DO_CANNY_PRUNING = 1,
            CASCADE_SCALE_IMAGE = 2,
            CASCADE_FIND_BIGGEST_OBJECT = 4,
            CASCADE_DO_ROUGH_SEARCH = 8;


    // C++: enum ObjectStatus (cv.DetectionBasedTracker.ObjectStatus)
    public static final int
            DetectionBasedTracker_DETECTED_NOT_SHOWN_YET = 0,
            DetectionBasedTracker_DETECTED = 1,
            DetectionBasedTracker_DETECTED_TEMPORARY_LOST = 2,
            DetectionBasedTracker_WRONG_OBJECT = 3;


    // C++: enum CornerRefineMethod (cv.aruco.CornerRefineMethod)
    public static final int
            CORNER_REFINE_NONE = 0,
            CORNER_REFINE_SUBPIX = 1,
            CORNER_REFINE_CONTOUR = 2,
            CORNER_REFINE_APRILTAG = 3;


    // C++: enum PredefinedDictionaryType (cv.aruco.PredefinedDictionaryType)
    public static final int
            DICT_4X4_50 = 0,
            DICT_4X4_100 = 0+1,
            DICT_4X4_250 = 0+2,
            DICT_4X4_1000 = 0+3,
            DICT_5X5_50 = 0+4,
            DICT_5X5_100 = 0+5,
            DICT_5X5_250 = 0+6,
            DICT_5X5_1000 = 0+7,
            DICT_6X6_50 = 0+8,
            DICT_6X6_100 = 0+9,
            DICT_6X6_250 = 0+10,
            DICT_6X6_1000 = 0+11,
            DICT_7X7_50 = 0+12,
            DICT_7X7_100 = 0+13,
            DICT_7X7_250 = 0+14,
            DICT_7X7_1000 = 0+15,
            DICT_ARUCO_ORIGINAL = 0+16,
            DICT_APRILTAG_16h5 = 0+17,
            DICT_APRILTAG_25h9 = 0+18,
            DICT_APRILTAG_36h10 = 0+19,
            DICT_APRILTAG_36h11 = 0+20,
            DICT_ARUCO_MIP_36h12 = 0+21;


    //
    // C++:  void cv::groupRectangles(vector_Rect& rectList, vector_int& weights, int groupThreshold, double eps = 0.2)
    //

    public static void groupRectangles(MatOfRect rectList, MatOfInt weights, int groupThreshold, double eps) {
        Mat rectList_mat = rectList;
        Mat weights_mat = weights;
        groupRectangles_0(rectList_mat.nativeObj, weights_mat.nativeObj, groupThreshold, eps);
    }

    public static void groupRectangles(MatOfRect rectList, MatOfInt weights, int groupThreshold) {
        Mat rectList_mat = rectList;
        Mat weights_mat = weights;
        groupRectangles_1(rectList_mat.nativeObj, weights_mat.nativeObj, groupThreshold);
    }


    //
    // C++:  void cv::aruco::drawDetectedMarkers(Mat& image, vector_Mat corners, Mat ids = Mat(), Scalar borderColor = Scalar(0, 255, 0))
    //

    /**
     * Draw detected markers in image
     *
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not altered.
     * @param corners positions of marker corners on input image.
     * (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers, the dimensions of
     * this array should be Nx4. The order of the corners should be clockwise.
     * @param ids vector of identifiers for markers in markersCorners .
     * Optional, if not provided, ids are not painted.
     * @param borderColor color of marker borders. Rest of colors (text color and first corner color)
     * are calculated based on this one to improve visualization.
     *
     * Given an array of detected marker corners and its corresponding ids, this functions draws
     * the markers in the image. The marker borders are painted and the markers identifiers if provided.
     * Useful for debugging purposes.
     */
    public static void drawDetectedMarkers(Mat image, List<Mat> corners, Mat ids, Scalar borderColor) {
        Mat corners_mat = Converters.vector_Mat_to_Mat(corners);
        drawDetectedMarkers_0(image.nativeObj, corners_mat.nativeObj, ids.nativeObj, borderColor.val[0], borderColor.val[1], borderColor.val[2], borderColor.val[3]);
    }

    /**
     * Draw detected markers in image
     *
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not altered.
     * @param corners positions of marker corners on input image.
     * (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers, the dimensions of
     * this array should be Nx4. The order of the corners should be clockwise.
     * @param ids vector of identifiers for markers in markersCorners .
     * Optional, if not provided, ids are not painted.
     * are calculated based on this one to improve visualization.
     *
     * Given an array of detected marker corners and its corresponding ids, this functions draws
     * the markers in the image. The marker borders are painted and the markers identifiers if provided.
     * Useful for debugging purposes.
     */
    public static void drawDetectedMarkers(Mat image, List<Mat> corners, Mat ids) {
        Mat corners_mat = Converters.vector_Mat_to_Mat(corners);
        drawDetectedMarkers_1(image.nativeObj, corners_mat.nativeObj, ids.nativeObj);
    }

    /**
     * Draw detected markers in image
     *
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not altered.
     * @param corners positions of marker corners on input image.
     * (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers, the dimensions of
     * this array should be Nx4. The order of the corners should be clockwise.
     * Optional, if not provided, ids are not painted.
     * are calculated based on this one to improve visualization.
     *
     * Given an array of detected marker corners and its corresponding ids, this functions draws
     * the markers in the image. The marker borders are painted and the markers identifiers if provided.
     * Useful for debugging purposes.
     */
    public static void drawDetectedMarkers(Mat image, List<Mat> corners) {
        Mat corners_mat = Converters.vector_Mat_to_Mat(corners);
        drawDetectedMarkers_2(image.nativeObj, corners_mat.nativeObj);
    }


    //
    // C++:  void cv::aruco::generateImageMarker(Dictionary dictionary, int id, int sidePixels, Mat& img, int borderBits = 1)
    //

    /**
     * Generate a canonical marker image
     *
     * @param dictionary dictionary of markers indicating the type of markers
     * @param id identifier of the marker that will be returned. It has to be a valid id in the specified dictionary.
     * @param sidePixels size of the image in pixels
     * @param img output image with the marker
     * @param borderBits width of the marker border.
     *
     * This function returns a marker image in its canonical form (i.e. ready to be printed)
     */
    public static void generateImageMarker(Dictionary dictionary, int id, int sidePixels, Mat img, int borderBits) {
        generateImageMarker_0(dictionary.nativeObj, id, sidePixels, img.nativeObj, borderBits);
    }

    /**
     * Generate a canonical marker image
     *
     * @param dictionary dictionary of markers indicating the type of markers
     * @param id identifier of the marker that will be returned. It has to be a valid id in the specified dictionary.
     * @param sidePixels size of the image in pixels
     * @param img output image with the marker
     *
     * This function returns a marker image in its canonical form (i.e. ready to be printed)
     */
    public static void generateImageMarker(Dictionary dictionary, int id, int sidePixels, Mat img) {
        generateImageMarker_1(dictionary.nativeObj, id, sidePixels, img.nativeObj);
    }


    //
    // C++:  void cv::aruco::drawDetectedCornersCharuco(Mat& image, Mat charucoCorners, Mat charucoIds = Mat(), Scalar cornerColor = Scalar(255, 0, 0))
    //

    /**
     * Draws a set of Charuco corners
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not
     * altered.
     * @param charucoCorners vector of detected charuco corners
     * @param charucoIds list of identifiers for each corner in charucoCorners
     * @param cornerColor color of the square surrounding each corner
     *
     * This function draws a set of detected Charuco corners. If identifiers vector is provided, it also
     * draws the id of each corner.
     */
    public static void drawDetectedCornersCharuco(Mat image, Mat charucoCorners, Mat charucoIds, Scalar cornerColor) {
        drawDetectedCornersCharuco_0(image.nativeObj, charucoCorners.nativeObj, charucoIds.nativeObj, cornerColor.val[0], cornerColor.val[1], cornerColor.val[2], cornerColor.val[3]);
    }

    /**
     * Draws a set of Charuco corners
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not
     * altered.
     * @param charucoCorners vector of detected charuco corners
     * @param charucoIds list of identifiers for each corner in charucoCorners
     *
     * This function draws a set of detected Charuco corners. If identifiers vector is provided, it also
     * draws the id of each corner.
     */
    public static void drawDetectedCornersCharuco(Mat image, Mat charucoCorners, Mat charucoIds) {
        drawDetectedCornersCharuco_1(image.nativeObj, charucoCorners.nativeObj, charucoIds.nativeObj);
    }

    /**
     * Draws a set of Charuco corners
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not
     * altered.
     * @param charucoCorners vector of detected charuco corners
     *
     * This function draws a set of detected Charuco corners. If identifiers vector is provided, it also
     * draws the id of each corner.
     */
    public static void drawDetectedCornersCharuco(Mat image, Mat charucoCorners) {
        drawDetectedCornersCharuco_2(image.nativeObj, charucoCorners.nativeObj);
    }


    //
    // C++:  void cv::aruco::drawDetectedDiamonds(Mat& image, vector_Mat diamondCorners, Mat diamondIds = Mat(), Scalar borderColor = Scalar(0, 0, 255))
    //

    /**
     * Draw a set of detected ChArUco Diamond markers
     *
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not
     * altered.
     * @param diamondCorners positions of diamond corners in the same format returned by
     * detectCharucoDiamond(). (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers,
     * the dimensions of this array should be Nx4. The order of the corners should be clockwise.
     * @param diamondIds vector of identifiers for diamonds in diamondCorners, in the same format
     * returned by detectCharucoDiamond() (e.g. std::vector&lt;Vec4i&gt;).
     * Optional, if not provided, ids are not painted.
     * @param borderColor color of marker borders. Rest of colors (text color and first corner color)
     * are calculated based on this one.
     *
     * Given an array of detected diamonds, this functions draws them in the image. The marker borders
     * are painted and the markers identifiers if provided.
     * Useful for debugging purposes.
     */
    public static void drawDetectedDiamonds(Mat image, List<Mat> diamondCorners, Mat diamondIds, Scalar borderColor) {
        Mat diamondCorners_mat = Converters.vector_Mat_to_Mat(diamondCorners);
        drawDetectedDiamonds_0(image.nativeObj, diamondCorners_mat.nativeObj, diamondIds.nativeObj, borderColor.val[0], borderColor.val[1], borderColor.val[2], borderColor.val[3]);
    }

    /**
     * Draw a set of detected ChArUco Diamond markers
     *
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not
     * altered.
     * @param diamondCorners positions of diamond corners in the same format returned by
     * detectCharucoDiamond(). (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers,
     * the dimensions of this array should be Nx4. The order of the corners should be clockwise.
     * @param diamondIds vector of identifiers for diamonds in diamondCorners, in the same format
     * returned by detectCharucoDiamond() (e.g. std::vector&lt;Vec4i&gt;).
     * Optional, if not provided, ids are not painted.
     * are calculated based on this one.
     *
     * Given an array of detected diamonds, this functions draws them in the image. The marker borders
     * are painted and the markers identifiers if provided.
     * Useful for debugging purposes.
     */
    public static void drawDetectedDiamonds(Mat image, List<Mat> diamondCorners, Mat diamondIds) {
        Mat diamondCorners_mat = Converters.vector_Mat_to_Mat(diamondCorners);
        drawDetectedDiamonds_1(image.nativeObj, diamondCorners_mat.nativeObj, diamondIds.nativeObj);
    }

    /**
     * Draw a set of detected ChArUco Diamond markers
     *
     * @param image input/output image. It must have 1 or 3 channels. The number of channels is not
     * altered.
     * @param diamondCorners positions of diamond corners in the same format returned by
     * detectCharucoDiamond(). (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers,
     * the dimensions of this array should be Nx4. The order of the corners should be clockwise.
     * returned by detectCharucoDiamond() (e.g. std::vector&lt;Vec4i&gt;).
     * Optional, if not provided, ids are not painted.
     * are calculated based on this one.
     *
     * Given an array of detected diamonds, this functions draws them in the image. The marker borders
     * are painted and the markers identifiers if provided.
     * Useful for debugging purposes.
     */
    public static void drawDetectedDiamonds(Mat image, List<Mat> diamondCorners) {
        Mat diamondCorners_mat = Converters.vector_Mat_to_Mat(diamondCorners);
        drawDetectedDiamonds_2(image.nativeObj, diamondCorners_mat.nativeObj);
    }


    //
    // C++:  Dictionary cv::aruco::getPredefinedDictionary(int dict)
    //

    /**
     * Returns one of the predefined dictionaries referenced by DICT_*.
     * @param dict automatically generated
     * @return automatically generated
     */
    public static Dictionary getPredefinedDictionary(int dict) {
        return new Dictionary(getPredefinedDictionary_0(dict));
    }


    //
    // C++:  Dictionary cv::aruco::extendDictionary(int nMarkers, int markerSize, Dictionary baseDictionary = Dictionary(), int randomSeed = 0)
    //

    /**
     * Extend base dictionary by new nMarkers
     *
     * @param nMarkers number of markers in the dictionary
     * @param markerSize number of bits per dimension of each markers
     * @param baseDictionary Include the markers in this dictionary at the beginning (optional)
     * @param randomSeed a user supplied seed for theRNG()
     *
     * This function creates a new dictionary composed by nMarkers markers and each markers composed
     * by markerSize x markerSize bits. If baseDictionary is provided, its markers are directly
     * included and the rest are generated based on them. If the size of baseDictionary is higher
     * than nMarkers, only the first nMarkers in baseDictionary are taken and no new marker is added.
     * @return automatically generated
     */
    public static Dictionary extendDictionary(int nMarkers, int markerSize, Dictionary baseDictionary, int randomSeed) {
        return new Dictionary(extendDictionary_0(nMarkers, markerSize, baseDictionary.nativeObj, randomSeed));
    }

    /**
     * Extend base dictionary by new nMarkers
     *
     * @param nMarkers number of markers in the dictionary
     * @param markerSize number of bits per dimension of each markers
     * @param baseDictionary Include the markers in this dictionary at the beginning (optional)
     *
     * This function creates a new dictionary composed by nMarkers markers and each markers composed
     * by markerSize x markerSize bits. If baseDictionary is provided, its markers are directly
     * included and the rest are generated based on them. If the size of baseDictionary is higher
     * than nMarkers, only the first nMarkers in baseDictionary are taken and no new marker is added.
     * @return automatically generated
     */
    public static Dictionary extendDictionary(int nMarkers, int markerSize, Dictionary baseDictionary) {
        return new Dictionary(extendDictionary_1(nMarkers, markerSize, baseDictionary.nativeObj));
    }

    /**
     * Extend base dictionary by new nMarkers
     *
     * @param nMarkers number of markers in the dictionary
     * @param markerSize number of bits per dimension of each markers
     *
     * This function creates a new dictionary composed by nMarkers markers and each markers composed
     * by markerSize x markerSize bits. If baseDictionary is provided, its markers are directly
     * included and the rest are generated based on them. If the size of baseDictionary is higher
     * than nMarkers, only the first nMarkers in baseDictionary are taken and no new marker is added.
     * @return automatically generated
     */
    public static Dictionary extendDictionary(int nMarkers, int markerSize) {
        return new Dictionary(extendDictionary_2(nMarkers, markerSize));
    }




    // C++:  void cv::groupRectangles(vector_Rect& rectList, vector_int& weights, int groupThreshold, double eps = 0.2)
    private static native void groupRectangles_0(long rectList_mat_nativeObj, long weights_mat_nativeObj, int groupThreshold, double eps);
    private static native void groupRectangles_1(long rectList_mat_nativeObj, long weights_mat_nativeObj, int groupThreshold);

    // C++:  void cv::aruco::drawDetectedMarkers(Mat& image, vector_Mat corners, Mat ids = Mat(), Scalar borderColor = Scalar(0, 255, 0))
    private static native void drawDetectedMarkers_0(long image_nativeObj, long corners_mat_nativeObj, long ids_nativeObj, double borderColor_val0, double borderColor_val1, double borderColor_val2, double borderColor_val3);
    private static native void drawDetectedMarkers_1(long image_nativeObj, long corners_mat_nativeObj, long ids_nativeObj);
    private static native void drawDetectedMarkers_2(long image_nativeObj, long corners_mat_nativeObj);

    // C++:  void cv::aruco::generateImageMarker(Dictionary dictionary, int id, int sidePixels, Mat& img, int borderBits = 1)
    private static native void generateImageMarker_0(long dictionary_nativeObj, int id, int sidePixels, long img_nativeObj, int borderBits);
    private static native void generateImageMarker_1(long dictionary_nativeObj, int id, int sidePixels, long img_nativeObj);

    // C++:  void cv::aruco::drawDetectedCornersCharuco(Mat& image, Mat charucoCorners, Mat charucoIds = Mat(), Scalar cornerColor = Scalar(255, 0, 0))
    private static native void drawDetectedCornersCharuco_0(long image_nativeObj, long charucoCorners_nativeObj, long charucoIds_nativeObj, double cornerColor_val0, double cornerColor_val1, double cornerColor_val2, double cornerColor_val3);
    private static native void drawDetectedCornersCharuco_1(long image_nativeObj, long charucoCorners_nativeObj, long charucoIds_nativeObj);
    private static native void drawDetectedCornersCharuco_2(long image_nativeObj, long charucoCorners_nativeObj);

    // C++:  void cv::aruco::drawDetectedDiamonds(Mat& image, vector_Mat diamondCorners, Mat diamondIds = Mat(), Scalar borderColor = Scalar(0, 0, 255))
    private static native void drawDetectedDiamonds_0(long image_nativeObj, long diamondCorners_mat_nativeObj, long diamondIds_nativeObj, double borderColor_val0, double borderColor_val1, double borderColor_val2, double borderColor_val3);
    private static native void drawDetectedDiamonds_1(long image_nativeObj, long diamondCorners_mat_nativeObj, long diamondIds_nativeObj);
    private static native void drawDetectedDiamonds_2(long image_nativeObj, long diamondCorners_mat_nativeObj);

    // C++:  Dictionary cv::aruco::getPredefinedDictionary(int dict)
    private static native long getPredefinedDictionary_0(int dict);

    // C++:  Dictionary cv::aruco::extendDictionary(int nMarkers, int markerSize, Dictionary baseDictionary = Dictionary(), int randomSeed = 0)
    private static native long extendDictionary_0(int nMarkers, int markerSize, long baseDictionary_nativeObj, int randomSeed);
    private static native long extendDictionary_1(int nMarkers, int markerSize, long baseDictionary_nativeObj);
    private static native long extendDictionary_2(int nMarkers, int markerSize);

}
