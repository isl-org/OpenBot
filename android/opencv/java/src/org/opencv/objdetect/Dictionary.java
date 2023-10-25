//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import org.opencv.core.Mat;

// C++: class Dictionary
/**
 * Dictionary/Set of markers, it contains the inner codification
 *
 * BytesList contains the marker codewords where:
 * - bytesList.rows is the dictionary size
 * - each marker is encoded using {@code nbytes = ceil(markerSize*markerSize/8.)}
 * - each row contains all 4 rotations of the marker, so its length is {@code 4*nbytes}
 *
 * {@code bytesList.ptr(i)[k*nbytes + j]} is then the j-th byte of i-th marker, in its k-th rotation.
 */
public class Dictionary {

    protected final long nativeObj;
    protected Dictionary(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static Dictionary __fromPtr__(long addr) { return new Dictionary(addr); }

    //
    // C++:   cv::aruco::Dictionary::Dictionary()
    //

    public Dictionary() {
        nativeObj = Dictionary_0();
    }


    //
    // C++:   cv::aruco::Dictionary::Dictionary(Mat bytesList, int _markerSize, int maxcorr = 0)
    //

    public Dictionary(Mat bytesList, int _markerSize, int maxcorr) {
        nativeObj = Dictionary_1(bytesList.nativeObj, _markerSize, maxcorr);
    }

    public Dictionary(Mat bytesList, int _markerSize) {
        nativeObj = Dictionary_2(bytesList.nativeObj, _markerSize);
    }


    //
    // C++:  bool cv::aruco::Dictionary::readDictionary(FileNode fn)
    //

    // Unknown type 'FileNode' (I), skipping the function


    //
    // C++:  void cv::aruco::Dictionary::writeDictionary(FileStorage fs, String name = String())
    //

    // Unknown type 'FileStorage' (I), skipping the function


    //
    // C++:  bool cv::aruco::Dictionary::identify(Mat onlyBits, int& idx, int& rotation, double maxCorrectionRate)
    //

    /**
     * Given a matrix of bits. Returns whether if marker is identified or not.
     *
     * It returns by reference the correct id (if any) and the correct rotation
     * @param onlyBits automatically generated
     * @param idx automatically generated
     * @param rotation automatically generated
     * @param maxCorrectionRate automatically generated
     * @return automatically generated
     */
    public boolean identify(Mat onlyBits, int[] idx, int[] rotation, double maxCorrectionRate) {
        double[] idx_out = new double[1];
        double[] rotation_out = new double[1];
        boolean retVal = identify_0(nativeObj, onlyBits.nativeObj, idx_out, rotation_out, maxCorrectionRate);
        if(idx!=null) idx[0] = (int)idx_out[0];
        if(rotation!=null) rotation[0] = (int)rotation_out[0];
        return retVal;
    }


    //
    // C++:  int cv::aruco::Dictionary::getDistanceToId(Mat bits, int id, bool allRotations = true)
    //

    /**
     * Returns the distance of the input bits to the specific id.
     *
     * If allRotations is true, the four posible bits rotation are considered
     * @param bits automatically generated
     * @param id automatically generated
     * @param allRotations automatically generated
     * @return automatically generated
     */
    public int getDistanceToId(Mat bits, int id, boolean allRotations) {
        return getDistanceToId_0(nativeObj, bits.nativeObj, id, allRotations);
    }

    /**
     * Returns the distance of the input bits to the specific id.
     *
     * If allRotations is true, the four posible bits rotation are considered
     * @param bits automatically generated
     * @param id automatically generated
     * @return automatically generated
     */
    public int getDistanceToId(Mat bits, int id) {
        return getDistanceToId_1(nativeObj, bits.nativeObj, id);
    }


    //
    // C++:  void cv::aruco::Dictionary::generateImageMarker(int id, int sidePixels, Mat& _img, int borderBits = 1)
    //

    /**
     * Generate a canonical marker image
     * @param id automatically generated
     * @param sidePixels automatically generated
     * @param _img automatically generated
     * @param borderBits automatically generated
     */
    public void generateImageMarker(int id, int sidePixels, Mat _img, int borderBits) {
        generateImageMarker_0(nativeObj, id, sidePixels, _img.nativeObj, borderBits);
    }

    /**
     * Generate a canonical marker image
     * @param id automatically generated
     * @param sidePixels automatically generated
     * @param _img automatically generated
     */
    public void generateImageMarker(int id, int sidePixels, Mat _img) {
        generateImageMarker_1(nativeObj, id, sidePixels, _img.nativeObj);
    }


    //
    // C++: static Mat cv::aruco::Dictionary::getByteListFromBits(Mat bits)
    //

    /**
     * Transform matrix of bits to list of bytes in the 4 rotations
     * @param bits automatically generated
     * @return automatically generated
     */
    public static Mat getByteListFromBits(Mat bits) {
        return new Mat(getByteListFromBits_0(bits.nativeObj));
    }


    //
    // C++: static Mat cv::aruco::Dictionary::getBitsFromByteList(Mat byteList, int markerSize)
    //

    /**
     * Transform list of bytes to matrix of bits
     * @param byteList automatically generated
     * @param markerSize automatically generated
     * @return automatically generated
     */
    public static Mat getBitsFromByteList(Mat byteList, int markerSize) {
        return new Mat(getBitsFromByteList_0(byteList.nativeObj, markerSize));
    }


    //
    // C++: Mat Dictionary::bytesList
    //

    public Mat get_bytesList() {
        return new Mat(get_bytesList_0(nativeObj));
    }


    //
    // C++: void Dictionary::bytesList
    //

    public void set_bytesList(Mat bytesList) {
        set_bytesList_0(nativeObj, bytesList.nativeObj);
    }


    //
    // C++: int Dictionary::markerSize
    //

    public int get_markerSize() {
        return get_markerSize_0(nativeObj);
    }


    //
    // C++: void Dictionary::markerSize
    //

    public void set_markerSize(int markerSize) {
        set_markerSize_0(nativeObj, markerSize);
    }


    //
    // C++: int Dictionary::maxCorrectionBits
    //

    public int get_maxCorrectionBits() {
        return get_maxCorrectionBits_0(nativeObj);
    }


    //
    // C++: void Dictionary::maxCorrectionBits
    //

    public void set_maxCorrectionBits(int maxCorrectionBits) {
        set_maxCorrectionBits_0(nativeObj, maxCorrectionBits);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::aruco::Dictionary::Dictionary()
    private static native long Dictionary_0();

    // C++:   cv::aruco::Dictionary::Dictionary(Mat bytesList, int _markerSize, int maxcorr = 0)
    private static native long Dictionary_1(long bytesList_nativeObj, int _markerSize, int maxcorr);
    private static native long Dictionary_2(long bytesList_nativeObj, int _markerSize);

    // C++:  bool cv::aruco::Dictionary::identify(Mat onlyBits, int& idx, int& rotation, double maxCorrectionRate)
    private static native boolean identify_0(long nativeObj, long onlyBits_nativeObj, double[] idx_out, double[] rotation_out, double maxCorrectionRate);

    // C++:  int cv::aruco::Dictionary::getDistanceToId(Mat bits, int id, bool allRotations = true)
    private static native int getDistanceToId_0(long nativeObj, long bits_nativeObj, int id, boolean allRotations);
    private static native int getDistanceToId_1(long nativeObj, long bits_nativeObj, int id);

    // C++:  void cv::aruco::Dictionary::generateImageMarker(int id, int sidePixels, Mat& _img, int borderBits = 1)
    private static native void generateImageMarker_0(long nativeObj, int id, int sidePixels, long _img_nativeObj, int borderBits);
    private static native void generateImageMarker_1(long nativeObj, int id, int sidePixels, long _img_nativeObj);

    // C++: static Mat cv::aruco::Dictionary::getByteListFromBits(Mat bits)
    private static native long getByteListFromBits_0(long bits_nativeObj);

    // C++: static Mat cv::aruco::Dictionary::getBitsFromByteList(Mat byteList, int markerSize)
    private static native long getBitsFromByteList_0(long byteList_nativeObj, int markerSize);

    // C++: Mat Dictionary::bytesList
    private static native long get_bytesList_0(long nativeObj);

    // C++: void Dictionary::bytesList
    private static native void set_bytesList_0(long nativeObj, long bytesList_nativeObj);

    // C++: int Dictionary::markerSize
    private static native int get_markerSize_0(long nativeObj);

    // C++: void Dictionary::markerSize
    private static native void set_markerSize_0(long nativeObj, int markerSize);

    // C++: int Dictionary::maxCorrectionBits
    private static native int get_maxCorrectionBits_0(long nativeObj);

    // C++: void Dictionary::maxCorrectionBits
    private static native void set_maxCorrectionBits_0(long nativeObj, int maxCorrectionBits);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
