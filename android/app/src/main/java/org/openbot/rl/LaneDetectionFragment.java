package org.openbot.rl;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.common.CameraFragment;
import org.openbot.databinding.FragmentAutopilotBinding;
import org.openbot.databinding.FragmentLaneDetectionBinding;
import org.openbot.tracking.MultiBoxTracker;
import org.opencv.android.OpenCVLoader;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class LaneDetectionFragment extends CameraFragment {

    private FragmentLaneDetectionBinding binding;
    private MultiBoxTracker tracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!OpenCVLoader.initDebug()) {
            // OpenCV initialization failed, handle the error
            Log.e("OPENCV TEST", "OpenCV initialization failed");
        } else {
            // OpenCV initialized successfully, proceed with your code
            Log.d("OPENCV TEST", "OpenCV initialized successfully");
        }
    }

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLaneDetectionBinding.inflate(inflater, container, false);

        return inflateFragment(binding, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the ImageView by its ID
        ImageView imageView = view.findViewById(R.id.yourImageViewId);
    }

    // Create a custom image processing method
    private Bitmap performImageProcessing(Bitmap inputImage) {
        // Convert Bitmap to Mat
        Mat inputMat = new Mat(inputImage.getHeight(), inputImage.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(inputImage, inputMat);

        // Apply image processing operations (e.g., resize and convert to grayscale)
        Size newSize = new Size(300, 300);
        Imgproc.resize(inputMat, inputMat, newSize);
        Imgproc.cvtColor(inputMat, inputMat, Imgproc.COLOR_RGBA2GRAY);

        Mat rotatedMat = new Mat();

// Transpose the image (swap rows and columns)
        Core.transpose(inputMat, rotatedMat);

// Flip the transposed image horizontally (180 degrees rotation)
        Core.flip(rotatedMat, rotatedMat, 1);

        Scalar lowerWhite = new Scalar(200, 170, 170);
        Scalar higherWhite = new Scalar(254, 254, 254);

        double contrastFactor = 1.2; // Increase contrast by 50%
// Scale and convert the image data type
        Mat contrastedMat = new Mat();
        rotatedMat.convertTo(contrastedMat, -1, contrastFactor, -40);
// Ensure pixel values are within the valid range
        Core.normalize(contrastedMat, contrastedMat, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);

        Mat mask = new Mat();
        Core.inRange(contrastedMat, lowerWhite, higherWhite, mask);

        Mat blur = new Mat();
        Imgproc.medianBlur(mask, blur, 9);
        Mat edges = new Mat();
        Imgproc.Canny(blur, edges, 100, 150);

        Mat bottom = regionOfInterest(edges);
        Mat thresholdMat = new Mat();
        Imgproc.adaptiveThreshold(bottom, thresholdMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 2);


        // Convert Mat back to Bitmap
        Bitmap processedBitmap = Bitmap.createBitmap(bottom.cols(), bottom.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(bottom, processedBitmap);



        // Now, 'processedBitmap' contains the preprocessed image
        return processedBitmap;
    }

    public static Mat regionOfInterest(Mat inputMat){

        int width = inputMat.cols();
        int height = inputMat.rows();

        // Define the ROI as the bottom half of the image
        Point[] roiPoints = new Point[4];
        roiPoints[0] = new Point(width*0.5, 0);
        roiPoints[1] = new Point(width, 0);
        roiPoints[2] = new Point(width, height);
        roiPoints[3] = new Point(width*0.5, height);
        MatOfPoint roiContour = new MatOfPoint(roiPoints);
        Mat mask = Mat.zeros(inputMat.size(), CvType.CV_8U);
        List<MatOfPoint> roiContours = new ArrayList<>();
        roiContours.add(roiContour);
        Imgproc.fillPoly(mask, roiContours, new Scalar(255));
        Mat resultImage = new Mat();
        Core.bitwise_and(inputMat, inputMat, resultImage, mask);



        return resultImage;
    }

    @Override
    protected void processFrame(Bitmap image, ImageProxy imageProxy) {
        // Call your custom image processing method
        Bitmap processedBitmap = performImageProcessing(image);
        ImageView imageView = binding.yourImageViewId;
        // Continue with your custom processing or display the processed image
        // For example, if you want to display the processed image in an ImageView:
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(processedBitmap);
            }
        });
    }

    @Override
    protected void processControllerKeyData(String command) {

    }

    @Override
    protected void processUSBData(String data) {

    }
}
