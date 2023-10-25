//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;



// C++: class Params
/**
 * QR code encoder parameters.
 *      version The optional version of QR code (by default - maximum possible depending on
 *                     the length of the string).
 *      correction_level The optional level of error correction (by default - the lowest).
 *      mode The optional encoding mode - Numeric, Alphanumeric, Byte, Kanji, ECI or Structured Append.
 *      structure_number The optional number of QR codes to generate in Structured Append mode.
 */
public class QRCodeEncoder_Params {

    protected final long nativeObj;
    protected QRCodeEncoder_Params(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static QRCodeEncoder_Params __fromPtr__(long addr) { return new QRCodeEncoder_Params(addr); }

    //
    // C++:   cv::QRCodeEncoder::Params::Params()
    //

    public QRCodeEncoder_Params() {
        nativeObj = QRCodeEncoder_Params_0();
    }


    //
    // C++: int QRCodeEncoder_Params::version
    //

    public int get_version() {
        return get_version_0(nativeObj);
    }


    //
    // C++: void QRCodeEncoder_Params::version
    //

    public void set_version(int version) {
        set_version_0(nativeObj, version);
    }


    //
    // C++: CorrectionLevel QRCodeEncoder_Params::correction_level
    //

    // Return type 'CorrectionLevel' is not supported, skipping the function


    //
    // C++: void QRCodeEncoder_Params::correction_level
    //

    // Unknown type 'CorrectionLevel' (I), skipping the function


    //
    // C++: EncodeMode QRCodeEncoder_Params::mode
    //

    // Return type 'EncodeMode' is not supported, skipping the function


    //
    // C++: void QRCodeEncoder_Params::mode
    //

    // Unknown type 'EncodeMode' (I), skipping the function


    //
    // C++: int QRCodeEncoder_Params::structure_number
    //

    public int get_structure_number() {
        return get_structure_number_0(nativeObj);
    }


    //
    // C++: void QRCodeEncoder_Params::structure_number
    //

    public void set_structure_number(int structure_number) {
        set_structure_number_0(nativeObj, structure_number);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::QRCodeEncoder::Params::Params()
    private static native long QRCodeEncoder_Params_0();

    // C++: int QRCodeEncoder_Params::version
    private static native int get_version_0(long nativeObj);

    // C++: void QRCodeEncoder_Params::version
    private static native void set_version_0(long nativeObj, int version);

    // C++: int QRCodeEncoder_Params::structure_number
    private static native int get_structure_number_0(long nativeObj);

    // C++: void QRCodeEncoder_Params::structure_number
    private static native void set_structure_number_0(long nativeObj, int structure_number);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
