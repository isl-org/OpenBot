//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.videoio;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.utils.Converters;

// C++: class VideoWriter
/**
 * Video writer class.
 *
 * The class provides C++ API for writing video files or image sequences.
 */
public class VideoWriter {

    protected final long nativeObj;
    protected VideoWriter(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static VideoWriter __fromPtr__(long addr) { return new VideoWriter(addr); }

    //
    // C++:   cv::VideoWriter::VideoWriter()
    //

    /**
     * Default constructors
     *
     *     The constructors/functions initialize video writers.
     * <ul>
     *   <li>
     *        On Linux FFMPEG is used to write videos;
     *   </li>
     *   <li>
     *        On Windows FFMPEG or MSWF or DSHOW is used;
     *   </li>
     *   <li>
     *        On MacOSX AVFoundation is used.
     *   </li>
     * </ul>
     */
    public VideoWriter() {
        nativeObj = VideoWriter_0();
    }


    //
    // C++:   cv::VideoWriter::VideoWriter(String filename, int fourcc, double fps, Size frameSize, bool isColor = true)
    //

    /**
     *
     *     @param filename Name of the output video file.
     *     @param fourcc 4-character code of codec used to compress the frames. For example,
     *     VideoWriter::fourcc('P','I','M','1') is a MPEG-1 codec, VideoWriter::fourcc('M','J','P','G')
     *     is a motion-jpeg codec etc. List of codes can be obtained at
     *     [MSDN](https://docs.microsoft.com/en-us/windows/win32/medfound/video-fourccs) page
     *     or with this [archived page](https://web.archive.org/web/20220316062600/http://www.fourcc.org/codecs.php)
     *     of the fourcc site for a more complete list). FFMPEG backend with MP4 container natively uses
     *     other values as fourcc code: see [ObjectType](http://mp4ra.org/#/codecs),
     *     so you may receive a warning message from OpenCV about fourcc code conversion.
     *     @param fps Framerate of the created video stream.
     *     @param frameSize Size of the video frames.
     *     @param isColor If it is not zero, the encoder will expect and encode color frames, otherwise it
     *     will work with grayscale frames.
     *
     *     <b>Tips</b>:
     * <ul>
     *   <li>
     *      With some backends {@code fourcc=-1} pops up the codec selection dialog from the system.
     *   </li>
     *   <li>
     *      To save image sequence use a proper filename (eg. {@code img_%02d.jpg}) and {@code fourcc=0}
     *       OR {@code fps=0}. Use uncompressed image format (eg. {@code img_%02d.BMP}) to save raw frames.
     *   </li>
     *   <li>
     *      Most codecs are lossy. If you want lossless video file you need to use a lossless codecs
     *       (eg. FFMPEG FFV1, Huffman HFYU, Lagarith LAGS, etc...)
     *   </li>
     *   <li>
     *      If FFMPEG is enabled, using {@code codec=0; fps=0;} you can create an uncompressed (raw) video file.
     *   </li>
     * </ul>
     */
    public VideoWriter(String filename, int fourcc, double fps, Size frameSize, boolean isColor) {
        nativeObj = VideoWriter_1(filename, fourcc, fps, frameSize.width, frameSize.height, isColor);
    }

    /**
     *
     *     @param filename Name of the output video file.
     *     @param fourcc 4-character code of codec used to compress the frames. For example,
     *     VideoWriter::fourcc('P','I','M','1') is a MPEG-1 codec, VideoWriter::fourcc('M','J','P','G')
     *     is a motion-jpeg codec etc. List of codes can be obtained at
     *     [MSDN](https://docs.microsoft.com/en-us/windows/win32/medfound/video-fourccs) page
     *     or with this [archived page](https://web.archive.org/web/20220316062600/http://www.fourcc.org/codecs.php)
     *     of the fourcc site for a more complete list). FFMPEG backend with MP4 container natively uses
     *     other values as fourcc code: see [ObjectType](http://mp4ra.org/#/codecs),
     *     so you may receive a warning message from OpenCV about fourcc code conversion.
     *     @param fps Framerate of the created video stream.
     *     @param frameSize Size of the video frames.
     *     will work with grayscale frames.
     *
     *     <b>Tips</b>:
     * <ul>
     *   <li>
     *      With some backends {@code fourcc=-1} pops up the codec selection dialog from the system.
     *   </li>
     *   <li>
     *      To save image sequence use a proper filename (eg. {@code img_%02d.jpg}) and {@code fourcc=0}
     *       OR {@code fps=0}. Use uncompressed image format (eg. {@code img_%02d.BMP}) to save raw frames.
     *   </li>
     *   <li>
     *      Most codecs are lossy. If you want lossless video file you need to use a lossless codecs
     *       (eg. FFMPEG FFV1, Huffman HFYU, Lagarith LAGS, etc...)
     *   </li>
     *   <li>
     *      If FFMPEG is enabled, using {@code codec=0; fps=0;} you can create an uncompressed (raw) video file.
     *   </li>
     * </ul>
     */
    public VideoWriter(String filename, int fourcc, double fps, Size frameSize) {
        nativeObj = VideoWriter_2(filename, fourcc, fps, frameSize.width, frameSize.height);
    }


    //
    // C++:   cv::VideoWriter::VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, bool isColor = true)
    //

    /**
     *
     *     The {@code apiPreference} parameter allows to specify API backends to use. Can be used to enforce a specific reader implementation
     *     if multiple are available: e.g. cv::CAP_FFMPEG or cv::CAP_GSTREAMER.
     * @param filename automatically generated
     * @param apiPreference automatically generated
     * @param fourcc automatically generated
     * @param fps automatically generated
     * @param frameSize automatically generated
     * @param isColor automatically generated
     */
    public VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, boolean isColor) {
        nativeObj = VideoWriter_3(filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height, isColor);
    }

    /**
     *
     *     The {@code apiPreference} parameter allows to specify API backends to use. Can be used to enforce a specific reader implementation
     *     if multiple are available: e.g. cv::CAP_FFMPEG or cv::CAP_GSTREAMER.
     * @param filename automatically generated
     * @param apiPreference automatically generated
     * @param fourcc automatically generated
     * @param fps automatically generated
     * @param frameSize automatically generated
     */
    public VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize) {
        nativeObj = VideoWriter_4(filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height);
    }


    //
    // C++:   cv::VideoWriter::VideoWriter(String filename, int fourcc, double fps, Size frameSize, vector_int params)
    //

    /**
     *
     * The {@code params} parameter allows to specify extra encoder parameters encoded as pairs (paramId_1, paramValue_1, paramId_2, paramValue_2, ... .)
     * see cv::VideoWriterProperties
     * @param filename automatically generated
     * @param fourcc automatically generated
     * @param fps automatically generated
     * @param frameSize automatically generated
     * @param params automatically generated
     */
    public VideoWriter(String filename, int fourcc, double fps, Size frameSize, MatOfInt params) {
        Mat params_mat = params;
        nativeObj = VideoWriter_5(filename, fourcc, fps, frameSize.width, frameSize.height, params_mat.nativeObj);
    }


    //
    // C++:   cv::VideoWriter::VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, vector_int params)
    //

    public VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, MatOfInt params) {
        Mat params_mat = params;
        nativeObj = VideoWriter_6(filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height, params_mat.nativeObj);
    }


    //
    // C++:  bool cv::VideoWriter::open(String filename, int fourcc, double fps, Size frameSize, bool isColor = true)
    //

    /**
     * Initializes or reinitializes video writer.
     *
     *     The method opens video writer. Parameters are the same as in the constructor
     *     VideoWriter::VideoWriter.
     *     @return {@code true} if video writer has been successfully initialized
     *
     *     The method first calls VideoWriter::release to close the already opened file.
     * @param filename automatically generated
     * @param fourcc automatically generated
     * @param fps automatically generated
     * @param frameSize automatically generated
     * @param isColor automatically generated
     */
    public boolean open(String filename, int fourcc, double fps, Size frameSize, boolean isColor) {
        return open_0(nativeObj, filename, fourcc, fps, frameSize.width, frameSize.height, isColor);
    }

    /**
     * Initializes or reinitializes video writer.
     *
     *     The method opens video writer. Parameters are the same as in the constructor
     *     VideoWriter::VideoWriter.
     *     @return {@code true} if video writer has been successfully initialized
     *
     *     The method first calls VideoWriter::release to close the already opened file.
     * @param filename automatically generated
     * @param fourcc automatically generated
     * @param fps automatically generated
     * @param frameSize automatically generated
     */
    public boolean open(String filename, int fourcc, double fps, Size frameSize) {
        return open_1(nativeObj, filename, fourcc, fps, frameSize.width, frameSize.height);
    }


    //
    // C++:  bool cv::VideoWriter::open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, bool isColor = true)
    //

    public boolean open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, boolean isColor) {
        return open_2(nativeObj, filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height, isColor);
    }

    public boolean open(String filename, int apiPreference, int fourcc, double fps, Size frameSize) {
        return open_3(nativeObj, filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height);
    }


    //
    // C++:  bool cv::VideoWriter::open(String filename, int fourcc, double fps, Size frameSize, vector_int params)
    //

    public boolean open(String filename, int fourcc, double fps, Size frameSize, MatOfInt params) {
        Mat params_mat = params;
        return open_4(nativeObj, filename, fourcc, fps, frameSize.width, frameSize.height, params_mat.nativeObj);
    }


    //
    // C++:  bool cv::VideoWriter::open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, vector_int params)
    //

    public boolean open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, MatOfInt params) {
        Mat params_mat = params;
        return open_5(nativeObj, filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height, params_mat.nativeObj);
    }


    //
    // C++:  bool cv::VideoWriter::isOpened()
    //

    /**
     * Returns true if video writer has been successfully initialized.
     * @return automatically generated
     */
    public boolean isOpened() {
        return isOpened_0(nativeObj);
    }


    //
    // C++:  void cv::VideoWriter::release()
    //

    /**
     * Closes the video writer.
     *
     *     The method is automatically called by subsequent VideoWriter::open and by the VideoWriter
     *     destructor.
     */
    public void release() {
        release_0(nativeObj);
    }


    //
    // C++:  void cv::VideoWriter::write(Mat image)
    //

    /**
     * Writes the next video frame
     *
     *     @param image The written frame. In general, color images are expected in BGR format.
     *
     *     The function/method writes the specified image to video file. It must have the same size as has
     *     been specified when opening the video writer.
     */
    public void write(Mat image) {
        write_0(nativeObj, image.nativeObj);
    }


    //
    // C++:  bool cv::VideoWriter::set(int propId, double value)
    //

    /**
     * Sets a property in the VideoWriter.
     *
     *      @param propId Property identifier from cv::VideoWriterProperties (eg. cv::VIDEOWRITER_PROP_QUALITY)
     *      or one of REF: videoio_flags_others
     *
     *      @param value Value of the property.
     *      @return  {@code true} if the property is supported by the backend used by the VideoWriter instance.
     */
    public boolean set(int propId, double value) {
        return set_0(nativeObj, propId, value);
    }


    //
    // C++:  double cv::VideoWriter::get(int propId)
    //

    /**
     * Returns the specified VideoWriter property
     *
     *      @param propId Property identifier from cv::VideoWriterProperties (eg. cv::VIDEOWRITER_PROP_QUALITY)
     *      or one of REF: videoio_flags_others
     *
     *      @return Value for the specified property. Value 0 is returned when querying a property that is
     *      not supported by the backend used by the VideoWriter instance.
     */
    public double get(int propId) {
        return get_0(nativeObj, propId);
    }


    //
    // C++: static int cv::VideoWriter::fourcc(char c1, char c2, char c3, char c4)
    //

    /**
     * Concatenates 4 chars to a fourcc code
     *
     *     @return a fourcc code
     *
     *     This static method constructs the fourcc code of the codec to be used in the constructor
     *     VideoWriter::VideoWriter or VideoWriter::open.
     * @param c1 automatically generated
     * @param c2 automatically generated
     * @param c3 automatically generated
     * @param c4 automatically generated
     */
    public static int fourcc(char c1, char c2, char c3, char c4) {
        return fourcc_0(c1, c2, c3, c4);
    }


    //
    // C++:  String cv::VideoWriter::getBackendName()
    //

    /**
     * Returns used backend API name
     *
     *      <b>Note:</b> Stream should be opened.
     * @return automatically generated
     */
    public String getBackendName() {
        return getBackendName_0(nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::VideoWriter::VideoWriter()
    private static native long VideoWriter_0();

    // C++:   cv::VideoWriter::VideoWriter(String filename, int fourcc, double fps, Size frameSize, bool isColor = true)
    private static native long VideoWriter_1(String filename, int fourcc, double fps, double frameSize_width, double frameSize_height, boolean isColor);
    private static native long VideoWriter_2(String filename, int fourcc, double fps, double frameSize_width, double frameSize_height);

    // C++:   cv::VideoWriter::VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, bool isColor = true)
    private static native long VideoWriter_3(String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height, boolean isColor);
    private static native long VideoWriter_4(String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height);

    // C++:   cv::VideoWriter::VideoWriter(String filename, int fourcc, double fps, Size frameSize, vector_int params)
    private static native long VideoWriter_5(String filename, int fourcc, double fps, double frameSize_width, double frameSize_height, long params_mat_nativeObj);

    // C++:   cv::VideoWriter::VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, vector_int params)
    private static native long VideoWriter_6(String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height, long params_mat_nativeObj);

    // C++:  bool cv::VideoWriter::open(String filename, int fourcc, double fps, Size frameSize, bool isColor = true)
    private static native boolean open_0(long nativeObj, String filename, int fourcc, double fps, double frameSize_width, double frameSize_height, boolean isColor);
    private static native boolean open_1(long nativeObj, String filename, int fourcc, double fps, double frameSize_width, double frameSize_height);

    // C++:  bool cv::VideoWriter::open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, bool isColor = true)
    private static native boolean open_2(long nativeObj, String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height, boolean isColor);
    private static native boolean open_3(long nativeObj, String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height);

    // C++:  bool cv::VideoWriter::open(String filename, int fourcc, double fps, Size frameSize, vector_int params)
    private static native boolean open_4(long nativeObj, String filename, int fourcc, double fps, double frameSize_width, double frameSize_height, long params_mat_nativeObj);

    // C++:  bool cv::VideoWriter::open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, vector_int params)
    private static native boolean open_5(long nativeObj, String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height, long params_mat_nativeObj);

    // C++:  bool cv::VideoWriter::isOpened()
    private static native boolean isOpened_0(long nativeObj);

    // C++:  void cv::VideoWriter::release()
    private static native void release_0(long nativeObj);

    // C++:  void cv::VideoWriter::write(Mat image)
    private static native void write_0(long nativeObj, long image_nativeObj);

    // C++:  bool cv::VideoWriter::set(int propId, double value)
    private static native boolean set_0(long nativeObj, int propId, double value);

    // C++:  double cv::VideoWriter::get(int propId)
    private static native double get_0(long nativeObj, int propId);

    // C++: static int cv::VideoWriter::fourcc(char c1, char c2, char c3, char c4)
    private static native int fourcc_0(char c1, char c2, char c3, char c4);

    // C++:  String cv::VideoWriter::getBackendName()
    private static native String getBackendName_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
