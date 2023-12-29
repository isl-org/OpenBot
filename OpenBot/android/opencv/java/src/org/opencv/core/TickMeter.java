//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.core;



// C++: class TickMeter
/**
 * a Class to measure passing time.
 *
 * The class computes passing time by counting the number of ticks per second. That is, the following code computes the
 * execution time in seconds:
 * SNIPPET: snippets/core_various.cpp TickMeter_total
 *
 * It is also possible to compute the average time over multiple runs:
 * SNIPPET: snippets/core_various.cpp TickMeter_average
 *
 * SEE: getTickCount, getTickFrequency
 */
public class TickMeter {

    protected final long nativeObj;
    protected TickMeter(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static TickMeter __fromPtr__(long addr) { return new TickMeter(addr); }

    //
    // C++:   cv::TickMeter::TickMeter()
    //

    public TickMeter() {
        nativeObj = TickMeter_0();
    }


    //
    // C++:  void cv::TickMeter::start()
    //

    public void start() {
        start_0(nativeObj);
    }


    //
    // C++:  void cv::TickMeter::stop()
    //

    public void stop() {
        stop_0(nativeObj);
    }


    //
    // C++:  int64 cv::TickMeter::getTimeTicks()
    //

    public long getTimeTicks() {
        return getTimeTicks_0(nativeObj);
    }


    //
    // C++:  double cv::TickMeter::getTimeMicro()
    //

    public double getTimeMicro() {
        return getTimeMicro_0(nativeObj);
    }


    //
    // C++:  double cv::TickMeter::getTimeMilli()
    //

    public double getTimeMilli() {
        return getTimeMilli_0(nativeObj);
    }


    //
    // C++:  double cv::TickMeter::getTimeSec()
    //

    public double getTimeSec() {
        return getTimeSec_0(nativeObj);
    }


    //
    // C++:  int64 cv::TickMeter::getCounter()
    //

    public long getCounter() {
        return getCounter_0(nativeObj);
    }


    //
    // C++:  double cv::TickMeter::getFPS()
    //

    public double getFPS() {
        return getFPS_0(nativeObj);
    }


    //
    // C++:  double cv::TickMeter::getAvgTimeSec()
    //

    public double getAvgTimeSec() {
        return getAvgTimeSec_0(nativeObj);
    }


    //
    // C++:  double cv::TickMeter::getAvgTimeMilli()
    //

    public double getAvgTimeMilli() {
        return getAvgTimeMilli_0(nativeObj);
    }


    //
    // C++:  void cv::TickMeter::reset()
    //

    public void reset() {
        reset_0(nativeObj);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::TickMeter::TickMeter()
    private static native long TickMeter_0();

    // C++:  void cv::TickMeter::start()
    private static native void start_0(long nativeObj);

    // C++:  void cv::TickMeter::stop()
    private static native void stop_0(long nativeObj);

    // C++:  int64 cv::TickMeter::getTimeTicks()
    private static native long getTimeTicks_0(long nativeObj);

    // C++:  double cv::TickMeter::getTimeMicro()
    private static native double getTimeMicro_0(long nativeObj);

    // C++:  double cv::TickMeter::getTimeMilli()
    private static native double getTimeMilli_0(long nativeObj);

    // C++:  double cv::TickMeter::getTimeSec()
    private static native double getTimeSec_0(long nativeObj);

    // C++:  int64 cv::TickMeter::getCounter()
    private static native long getCounter_0(long nativeObj);

    // C++:  double cv::TickMeter::getFPS()
    private static native double getFPS_0(long nativeObj);

    // C++:  double cv::TickMeter::getAvgTimeSec()
    private static native double getAvgTimeSec_0(long nativeObj);

    // C++:  double cv::TickMeter::getAvgTimeMilli()
    private static native double getAvgTimeMilli_0(long nativeObj);

    // C++:  void cv::TickMeter::reset()
    private static native void reset_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
