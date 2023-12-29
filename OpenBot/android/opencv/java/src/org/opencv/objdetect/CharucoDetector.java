//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.objdetect.CharucoBoard;
import org.opencv.objdetect.CharucoParameters;
import org.opencv.objdetect.DetectorParameters;
import org.opencv.objdetect.RefineParameters;
import org.opencv.utils.Converters;

// C++: class CharucoDetector

public class CharucoDetector extends Algorithm {

    protected CharucoDetector(long addr) { super(addr); }

    // internal usage only
    public static CharucoDetector __fromPtr__(long addr) { return new CharucoDetector(addr); }

    //
    // C++:   cv::aruco::CharucoDetector::CharucoDetector(CharucoBoard board, CharucoParameters charucoParams = CharucoParameters(), DetectorParameters detectorParams = DetectorParameters(), RefineParameters refineParams = RefineParameters())
    //

    /**
     * Basic CharucoDetector constructor
     *
     * @param board ChAruco board
     * @param charucoParams charuco detection parameters
     * @param detectorParams marker detection parameters
     * @param refineParams marker refine detection parameters
     */
    public CharucoDetector(CharucoBoard board, CharucoParameters charucoParams, DetectorParameters detectorParams, RefineParameters refineParams) {
        super(CharucoDetector_0(board.nativeObj, charucoParams.nativeObj, detectorParams.nativeObj, refineParams.nativeObj));
    }

    /**
     * Basic CharucoDetector constructor
     *
     * @param board ChAruco board
     * @param charucoParams charuco detection parameters
     * @param detectorParams marker detection parameters
     */
    public CharucoDetector(CharucoBoard board, CharucoParameters charucoParams, DetectorParameters detectorParams) {
        super(CharucoDetector_1(board.nativeObj, charucoParams.nativeObj, detectorParams.nativeObj));
    }

    /**
     * Basic CharucoDetector constructor
     *
     * @param board ChAruco board
     * @param charucoParams charuco detection parameters
     */
    public CharucoDetector(CharucoBoard board, CharucoParameters charucoParams) {
        super(CharucoDetector_2(board.nativeObj, charucoParams.nativeObj));
    }

    /**
     * Basic CharucoDetector constructor
     *
     * @param board ChAruco board
     */
    public CharucoDetector(CharucoBoard board) {
        super(CharucoDetector_3(board.nativeObj));
    }


    //
    // C++:  CharucoBoard cv::aruco::CharucoDetector::getBoard()
    //

    public CharucoBoard getBoard() {
        return new CharucoBoard(getBoard_0(nativeObj));
    }


    //
    // C++:  void cv::aruco::CharucoDetector::setBoard(CharucoBoard board)
    //

    public void setBoard(CharucoBoard board) {
        setBoard_0(nativeObj, board.nativeObj);
    }


    //
    // C++:  CharucoParameters cv::aruco::CharucoDetector::getCharucoParameters()
    //

    public CharucoParameters getCharucoParameters() {
        return new CharucoParameters(getCharucoParameters_0(nativeObj));
    }


    //
    // C++:  void cv::aruco::CharucoDetector::setCharucoParameters(CharucoParameters charucoParameters)
    //

    public void setCharucoParameters(CharucoParameters charucoParameters) {
        setCharucoParameters_0(nativeObj, charucoParameters.nativeObj);
    }


    //
    // C++:  DetectorParameters cv::aruco::CharucoDetector::getDetectorParameters()
    //

    public DetectorParameters getDetectorParameters() {
        return new DetectorParameters(getDetectorParameters_0(nativeObj));
    }


    //
    // C++:  void cv::aruco::CharucoDetector::setDetectorParameters(DetectorParameters detectorParameters)
    //

    public void setDetectorParameters(DetectorParameters detectorParameters) {
        setDetectorParameters_0(nativeObj, detectorParameters.nativeObj);
    }


    //
    // C++:  RefineParameters cv::aruco::CharucoDetector::getRefineParameters()
    //

    public RefineParameters getRefineParameters() {
        return new RefineParameters(getRefineParameters_0(nativeObj));
    }


    //
    // C++:  void cv::aruco::CharucoDetector::setRefineParameters(RefineParameters refineParameters)
    //

    public void setRefineParameters(RefineParameters refineParameters) {
        setRefineParameters_0(nativeObj, refineParameters.nativeObj);
    }


    //
    // C++:  void cv::aruco::CharucoDetector::detectBoard(Mat image, Mat& charucoCorners, Mat& charucoIds, vector_Mat& markerCorners = vector_Mat(), Mat& markerIds = Mat())
    //

    /**
     * detect aruco markers and interpolate position of ChArUco board corners
     * @param image input image necesary for corner refinement. Note that markers are not detected and
     * should be sent in corners and ids parameters.
     * @param charucoCorners interpolated chessboard corners.
     * @param charucoIds interpolated chessboard corners identifiers.
     * @param markerCorners vector of already detected markers corners. For each marker, its four
     * corners are provided, (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers, the
     * dimensions of this array should be Nx4. The order of the corners should be clockwise.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     * @param markerIds list of identifiers for each marker in corners.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     *
     * This function receives the detected markers and returns the 2D position of the chessboard corners
     * from a ChArUco board using the detected Aruco markers.
     *
     * If markerCorners and markerCorners are empty, the detectMarkers() will run and detect aruco markers and ids.
     *
     * If camera parameters are provided, the process is based in an approximated pose estimation, else it is based on local homography.
     * Only visible corners are returned. For each corner, its corresponding identifier is also returned in charucoIds.
     * SEE: findChessboardCorners
     */
    public void detectBoard(Mat image, Mat charucoCorners, Mat charucoIds, List<Mat> markerCorners, Mat markerIds) {
        Mat markerCorners_mat = Converters.vector_Mat_to_Mat(markerCorners);
        detectBoard_0(nativeObj, image.nativeObj, charucoCorners.nativeObj, charucoIds.nativeObj, markerCorners_mat.nativeObj, markerIds.nativeObj);
        Converters.Mat_to_vector_Mat(markerCorners_mat, markerCorners);
        markerCorners_mat.release();
    }

    /**
     * detect aruco markers and interpolate position of ChArUco board corners
     * @param image input image necesary for corner refinement. Note that markers are not detected and
     * should be sent in corners and ids parameters.
     * @param charucoCorners interpolated chessboard corners.
     * @param charucoIds interpolated chessboard corners identifiers.
     * @param markerCorners vector of already detected markers corners. For each marker, its four
     * corners are provided, (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers, the
     * dimensions of this array should be Nx4. The order of the corners should be clockwise.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     *
     * This function receives the detected markers and returns the 2D position of the chessboard corners
     * from a ChArUco board using the detected Aruco markers.
     *
     * If markerCorners and markerCorners are empty, the detectMarkers() will run and detect aruco markers and ids.
     *
     * If camera parameters are provided, the process is based in an approximated pose estimation, else it is based on local homography.
     * Only visible corners are returned. For each corner, its corresponding identifier is also returned in charucoIds.
     * SEE: findChessboardCorners
     */
    public void detectBoard(Mat image, Mat charucoCorners, Mat charucoIds, List<Mat> markerCorners) {
        Mat markerCorners_mat = Converters.vector_Mat_to_Mat(markerCorners);
        detectBoard_1(nativeObj, image.nativeObj, charucoCorners.nativeObj, charucoIds.nativeObj, markerCorners_mat.nativeObj);
        Converters.Mat_to_vector_Mat(markerCorners_mat, markerCorners);
        markerCorners_mat.release();
    }

    /**
     * detect aruco markers and interpolate position of ChArUco board corners
     * @param image input image necesary for corner refinement. Note that markers are not detected and
     * should be sent in corners and ids parameters.
     * @param charucoCorners interpolated chessboard corners.
     * @param charucoIds interpolated chessboard corners identifiers.
     * corners are provided, (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ). For N detected markers, the
     * dimensions of this array should be Nx4. The order of the corners should be clockwise.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     *
     * This function receives the detected markers and returns the 2D position of the chessboard corners
     * from a ChArUco board using the detected Aruco markers.
     *
     * If markerCorners and markerCorners are empty, the detectMarkers() will run and detect aruco markers and ids.
     *
     * If camera parameters are provided, the process is based in an approximated pose estimation, else it is based on local homography.
     * Only visible corners are returned. For each corner, its corresponding identifier is also returned in charucoIds.
     * SEE: findChessboardCorners
     */
    public void detectBoard(Mat image, Mat charucoCorners, Mat charucoIds) {
        detectBoard_2(nativeObj, image.nativeObj, charucoCorners.nativeObj, charucoIds.nativeObj);
    }


    //
    // C++:  void cv::aruco::CharucoDetector::detectDiamonds(Mat image, vector_Mat& diamondCorners, Mat& diamondIds, vector_Mat& markerCorners = vector_Mat(), Mat& markerIds = Mat())
    //

    /**
     * Detect ChArUco Diamond markers
     *
     * @param image input image necessary for corner subpixel.
     * @param diamondCorners output list of detected diamond corners (4 corners per diamond). The order
     * is the same than in marker corners: top left, top right, bottom right and bottom left. Similar
     * format than the corners returned by detectMarkers (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ).
     * @param diamondIds ids of the diamonds in diamondCorners. The id of each diamond is in fact of
     * type Vec4i, so each diamond has 4 ids, which are the ids of the aruco markers composing the
     * diamond.
     * @param markerCorners list of detected marker corners from detectMarkers function.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     * @param markerIds list of marker ids in markerCorners.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     *
     * This function detects Diamond markers from the previous detected ArUco markers. The diamonds
     * are returned in the diamondCorners and diamondIds parameters. If camera calibration parameters
     * are provided, the diamond search is based on reprojection. If not, diamond search is based on
     * homography. Homography is faster than reprojection, but less accurate.
     */
    public void detectDiamonds(Mat image, List<Mat> diamondCorners, Mat diamondIds, List<Mat> markerCorners, Mat markerIds) {
        Mat diamondCorners_mat = new Mat();
        Mat markerCorners_mat = Converters.vector_Mat_to_Mat(markerCorners);
        detectDiamonds_0(nativeObj, image.nativeObj, diamondCorners_mat.nativeObj, diamondIds.nativeObj, markerCorners_mat.nativeObj, markerIds.nativeObj);
        Converters.Mat_to_vector_Mat(diamondCorners_mat, diamondCorners);
        diamondCorners_mat.release();
        Converters.Mat_to_vector_Mat(markerCorners_mat, markerCorners);
        markerCorners_mat.release();
    }

    /**
     * Detect ChArUco Diamond markers
     *
     * @param image input image necessary for corner subpixel.
     * @param diamondCorners output list of detected diamond corners (4 corners per diamond). The order
     * is the same than in marker corners: top left, top right, bottom right and bottom left. Similar
     * format than the corners returned by detectMarkers (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ).
     * @param diamondIds ids of the diamonds in diamondCorners. The id of each diamond is in fact of
     * type Vec4i, so each diamond has 4 ids, which are the ids of the aruco markers composing the
     * diamond.
     * @param markerCorners list of detected marker corners from detectMarkers function.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     *
     * This function detects Diamond markers from the previous detected ArUco markers. The diamonds
     * are returned in the diamondCorners and diamondIds parameters. If camera calibration parameters
     * are provided, the diamond search is based on reprojection. If not, diamond search is based on
     * homography. Homography is faster than reprojection, but less accurate.
     */
    public void detectDiamonds(Mat image, List<Mat> diamondCorners, Mat diamondIds, List<Mat> markerCorners) {
        Mat diamondCorners_mat = new Mat();
        Mat markerCorners_mat = Converters.vector_Mat_to_Mat(markerCorners);
        detectDiamonds_1(nativeObj, image.nativeObj, diamondCorners_mat.nativeObj, diamondIds.nativeObj, markerCorners_mat.nativeObj);
        Converters.Mat_to_vector_Mat(diamondCorners_mat, diamondCorners);
        diamondCorners_mat.release();
        Converters.Mat_to_vector_Mat(markerCorners_mat, markerCorners);
        markerCorners_mat.release();
    }

    /**
     * Detect ChArUco Diamond markers
     *
     * @param image input image necessary for corner subpixel.
     * @param diamondCorners output list of detected diamond corners (4 corners per diamond). The order
     * is the same than in marker corners: top left, top right, bottom right and bottom left. Similar
     * format than the corners returned by detectMarkers (e.g std::vector&lt;std::vector&lt;cv::Point2f&gt; &gt; ).
     * @param diamondIds ids of the diamonds in diamondCorners. The id of each diamond is in fact of
     * type Vec4i, so each diamond has 4 ids, which are the ids of the aruco markers composing the
     * diamond.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     * If markerCorners and markerCorners are empty, the function detect aruco markers and ids.
     *
     * This function detects Diamond markers from the previous detected ArUco markers. The diamonds
     * are returned in the diamondCorners and diamondIds parameters. If camera calibration parameters
     * are provided, the diamond search is based on reprojection. If not, diamond search is based on
     * homography. Homography is faster than reprojection, but less accurate.
     */
    public void detectDiamonds(Mat image, List<Mat> diamondCorners, Mat diamondIds) {
        Mat diamondCorners_mat = new Mat();
        detectDiamonds_2(nativeObj, image.nativeObj, diamondCorners_mat.nativeObj, diamondIds.nativeObj);
        Converters.Mat_to_vector_Mat(diamondCorners_mat, diamondCorners);
        diamondCorners_mat.release();
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::aruco::CharucoDetector::CharucoDetector(CharucoBoard board, CharucoParameters charucoParams = CharucoParameters(), DetectorParameters detectorParams = DetectorParameters(), RefineParameters refineParams = RefineParameters())
    private static native long CharucoDetector_0(long board_nativeObj, long charucoParams_nativeObj, long detectorParams_nativeObj, long refineParams_nativeObj);
    private static native long CharucoDetector_1(long board_nativeObj, long charucoParams_nativeObj, long detectorParams_nativeObj);
    private static native long CharucoDetector_2(long board_nativeObj, long charucoParams_nativeObj);
    private static native long CharucoDetector_3(long board_nativeObj);

    // C++:  CharucoBoard cv::aruco::CharucoDetector::getBoard()
    private static native long getBoard_0(long nativeObj);

    // C++:  void cv::aruco::CharucoDetector::setBoard(CharucoBoard board)
    private static native void setBoard_0(long nativeObj, long board_nativeObj);

    // C++:  CharucoParameters cv::aruco::CharucoDetector::getCharucoParameters()
    private static native long getCharucoParameters_0(long nativeObj);

    // C++:  void cv::aruco::CharucoDetector::setCharucoParameters(CharucoParameters charucoParameters)
    private static native void setCharucoParameters_0(long nativeObj, long charucoParameters_nativeObj);

    // C++:  DetectorParameters cv::aruco::CharucoDetector::getDetectorParameters()
    private static native long getDetectorParameters_0(long nativeObj);

    // C++:  void cv::aruco::CharucoDetector::setDetectorParameters(DetectorParameters detectorParameters)
    private static native void setDetectorParameters_0(long nativeObj, long detectorParameters_nativeObj);

    // C++:  RefineParameters cv::aruco::CharucoDetector::getRefineParameters()
    private static native long getRefineParameters_0(long nativeObj);

    // C++:  void cv::aruco::CharucoDetector::setRefineParameters(RefineParameters refineParameters)
    private static native void setRefineParameters_0(long nativeObj, long refineParameters_nativeObj);

    // C++:  void cv::aruco::CharucoDetector::detectBoard(Mat image, Mat& charucoCorners, Mat& charucoIds, vector_Mat& markerCorners = vector_Mat(), Mat& markerIds = Mat())
    private static native void detectBoard_0(long nativeObj, long image_nativeObj, long charucoCorners_nativeObj, long charucoIds_nativeObj, long markerCorners_mat_nativeObj, long markerIds_nativeObj);
    private static native void detectBoard_1(long nativeObj, long image_nativeObj, long charucoCorners_nativeObj, long charucoIds_nativeObj, long markerCorners_mat_nativeObj);
    private static native void detectBoard_2(long nativeObj, long image_nativeObj, long charucoCorners_nativeObj, long charucoIds_nativeObj);

    // C++:  void cv::aruco::CharucoDetector::detectDiamonds(Mat image, vector_Mat& diamondCorners, Mat& diamondIds, vector_Mat& markerCorners = vector_Mat(), Mat& markerIds = Mat())
    private static native void detectDiamonds_0(long nativeObj, long image_nativeObj, long diamondCorners_mat_nativeObj, long diamondIds_nativeObj, long markerCorners_mat_nativeObj, long markerIds_nativeObj);
    private static native void detectDiamonds_1(long nativeObj, long image_nativeObj, long diamondCorners_mat_nativeObj, long diamondIds_nativeObj, long markerCorners_mat_nativeObj);
    private static native void detectDiamonds_2(long nativeObj, long image_nativeObj, long diamondCorners_mat_nativeObj, long diamondIds_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
