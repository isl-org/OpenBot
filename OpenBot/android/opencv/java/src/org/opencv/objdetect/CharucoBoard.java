//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Size;
import org.opencv.objdetect.Board;
import org.opencv.objdetect.Dictionary;
import org.opencv.utils.Converters;

// C++: class CharucoBoard
/**
 * ChArUco board is a planar chessboard where the markers are placed inside the white squares of a chessboard.
 *
 * The benefits of ChArUco boards is that they provide both, ArUco markers versatility and chessboard corner precision,
 * which is important for calibration and pose estimation. The board image can be drawn using generateImage() method.
 */
public class CharucoBoard extends Board {

    protected CharucoBoard(long addr) { super(addr); }

    // internal usage only
    public static CharucoBoard __fromPtr__(long addr) { return new CharucoBoard(addr); }

    //
    // C++:   cv::aruco::CharucoBoard::CharucoBoard(Size size, float squareLength, float markerLength, Dictionary dictionary, Mat ids = Mat())
    //

    /**
     * CharucoBoard constructor
     *
     * @param size number of chessboard squares in x and y directions
     * @param squareLength squareLength chessboard square side length (normally in meters)
     * @param markerLength marker side length (same unit than squareLength)
     * @param dictionary dictionary of markers indicating the type of markers
     * @param ids array of id used markers
     * The first markers in the dictionary are used to fill the white chessboard squares.
     */
    public CharucoBoard(Size size, float squareLength, float markerLength, Dictionary dictionary, Mat ids) {
        super(CharucoBoard_0(size.width, size.height, squareLength, markerLength, dictionary.nativeObj, ids.nativeObj));
    }

    /**
     * CharucoBoard constructor
     *
     * @param size number of chessboard squares in x and y directions
     * @param squareLength squareLength chessboard square side length (normally in meters)
     * @param markerLength marker side length (same unit than squareLength)
     * @param dictionary dictionary of markers indicating the type of markers
     * The first markers in the dictionary are used to fill the white chessboard squares.
     */
    public CharucoBoard(Size size, float squareLength, float markerLength, Dictionary dictionary) {
        super(CharucoBoard_1(size.width, size.height, squareLength, markerLength, dictionary.nativeObj));
    }


    //
    // C++:  void cv::aruco::CharucoBoard::setLegacyPattern(bool legacyPattern)
    //

    /**
     * set legacy chessboard pattern.
     *
     * Legacy setting creates chessboard patterns starting with a white box in the upper left corner
     * if there is an even row count of chessboard boxes, otherwise it starts with a black box.
     * This setting ensures compatibility to patterns created with OpenCV versions prior OpenCV 4.6.0.
     * See https://github.com/opencv/opencv/issues/23152.
     *
     * Default value: false.
     * @param legacyPattern automatically generated
     */
    public void setLegacyPattern(boolean legacyPattern) {
        setLegacyPattern_0(nativeObj, legacyPattern);
    }


    //
    // C++:  bool cv::aruco::CharucoBoard::getLegacyPattern()
    //

    public boolean getLegacyPattern() {
        return getLegacyPattern_0(nativeObj);
    }


    //
    // C++:  Size cv::aruco::CharucoBoard::getChessboardSize()
    //

    public Size getChessboardSize() {
        return new Size(getChessboardSize_0(nativeObj));
    }


    //
    // C++:  float cv::aruco::CharucoBoard::getSquareLength()
    //

    public float getSquareLength() {
        return getSquareLength_0(nativeObj);
    }


    //
    // C++:  float cv::aruco::CharucoBoard::getMarkerLength()
    //

    public float getMarkerLength() {
        return getMarkerLength_0(nativeObj);
    }


    //
    // C++:  vector_Point3f cv::aruco::CharucoBoard::getChessboardCorners()
    //

    /**
     * get CharucoBoard::chessboardCorners
     * @return automatically generated
     */
    public MatOfPoint3f getChessboardCorners() {
        return MatOfPoint3f.fromNativeAddr(getChessboardCorners_0(nativeObj));
    }


    //
    // C++:  bool cv::aruco::CharucoBoard::checkCharucoCornersCollinear(Mat charucoIds)
    //

    /**
     * check whether the ChArUco markers are collinear
     *
     * @param charucoIds list of identifiers for each corner in charucoCorners per frame.
     * @return bool value, 1 (true) if detected corners form a line, 0 (false) if they do not.
     * solvePnP, calibration functions will fail if the corners are collinear (true).
     *
     * The number of ids in charucoIDs should be &lt;= the number of chessboard corners in the board.
     * This functions checks whether the charuco corners are on a straight line (returns true, if so), or not (false).
     * Axis parallel, as well as diagonal and other straight lines detected.  Degenerate cases:
     * for number of charucoIDs &lt;= 2,the function returns true.
     */
    public boolean checkCharucoCornersCollinear(Mat charucoIds) {
        return checkCharucoCornersCollinear_0(nativeObj, charucoIds.nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::aruco::CharucoBoard::CharucoBoard(Size size, float squareLength, float markerLength, Dictionary dictionary, Mat ids = Mat())
    private static native long CharucoBoard_0(double size_width, double size_height, float squareLength, float markerLength, long dictionary_nativeObj, long ids_nativeObj);
    private static native long CharucoBoard_1(double size_width, double size_height, float squareLength, float markerLength, long dictionary_nativeObj);

    // C++:  void cv::aruco::CharucoBoard::setLegacyPattern(bool legacyPattern)
    private static native void setLegacyPattern_0(long nativeObj, boolean legacyPattern);

    // C++:  bool cv::aruco::CharucoBoard::getLegacyPattern()
    private static native boolean getLegacyPattern_0(long nativeObj);

    // C++:  Size cv::aruco::CharucoBoard::getChessboardSize()
    private static native double[] getChessboardSize_0(long nativeObj);

    // C++:  float cv::aruco::CharucoBoard::getSquareLength()
    private static native float getSquareLength_0(long nativeObj);

    // C++:  float cv::aruco::CharucoBoard::getMarkerLength()
    private static native float getMarkerLength_0(long nativeObj);

    // C++:  vector_Point3f cv::aruco::CharucoBoard::getChessboardCorners()
    private static native long getChessboardCorners_0(long nativeObj);

    // C++:  bool cv::aruco::CharucoBoard::checkCharucoCornersCollinear(Mat charucoIds)
    private static native boolean checkCharucoCornersCollinear_0(long nativeObj, long charucoIds_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
