//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.objdetect.Board;
import org.opencv.objdetect.Dictionary;

// C++: class GridBoard
/**
 * Planar board with grid arrangement of markers
 *
 * More common type of board. All markers are placed in the same plane in a grid arrangement.
 * The board image can be drawn using generateImage() method.
 */
public class GridBoard extends Board {

    protected GridBoard(long addr) { super(addr); }

    // internal usage only
    public static GridBoard __fromPtr__(long addr) { return new GridBoard(addr); }

    //
    // C++:   cv::aruco::GridBoard::GridBoard(Size size, float markerLength, float markerSeparation, Dictionary dictionary, Mat ids = Mat())
    //

    /**
     * GridBoard constructor
     *
     * @param size number of markers in x and y directions
     * @param markerLength marker side length (normally in meters)
     * @param markerSeparation separation between two markers (same unit as markerLength)
     * @param dictionary dictionary of markers indicating the type of markers
     * @param ids set of marker ids in dictionary to use on board.
     */
    public GridBoard(Size size, float markerLength, float markerSeparation, Dictionary dictionary, Mat ids) {
        super(GridBoard_0(size.width, size.height, markerLength, markerSeparation, dictionary.nativeObj, ids.nativeObj));
    }

    /**
     * GridBoard constructor
     *
     * @param size number of markers in x and y directions
     * @param markerLength marker side length (normally in meters)
     * @param markerSeparation separation between two markers (same unit as markerLength)
     * @param dictionary dictionary of markers indicating the type of markers
     */
    public GridBoard(Size size, float markerLength, float markerSeparation, Dictionary dictionary) {
        super(GridBoard_1(size.width, size.height, markerLength, markerSeparation, dictionary.nativeObj));
    }


    //
    // C++:  Size cv::aruco::GridBoard::getGridSize()
    //

    public Size getGridSize() {
        return new Size(getGridSize_0(nativeObj));
    }


    //
    // C++:  float cv::aruco::GridBoard::getMarkerLength()
    //

    public float getMarkerLength() {
        return getMarkerLength_0(nativeObj);
    }


    //
    // C++:  float cv::aruco::GridBoard::getMarkerSeparation()
    //

    public float getMarkerSeparation() {
        return getMarkerSeparation_0(nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::aruco::GridBoard::GridBoard(Size size, float markerLength, float markerSeparation, Dictionary dictionary, Mat ids = Mat())
    private static native long GridBoard_0(double size_width, double size_height, float markerLength, float markerSeparation, long dictionary_nativeObj, long ids_nativeObj);
    private static native long GridBoard_1(double size_width, double size_height, float markerLength, float markerSeparation, long dictionary_nativeObj);

    // C++:  Size cv::aruco::GridBoard::getGridSize()
    private static native double[] getGridSize_0(long nativeObj);

    // C++:  float cv::aruco::GridBoard::getMarkerLength()
    private static native float getMarkerLength_0(long nativeObj);

    // C++:  float cv::aruco::GridBoard::getMarkerSeparation()
    private static native float getMarkerSeparation_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
