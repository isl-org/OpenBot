/*
Author: Lilou Gras, 2023
This feature was added to try and communicate with the Lane Detection Fragment. By obtaining the centroid and distance, we could compute the reward.
However, this never succeeded as intented, the best result obtained was getting the last value of the centroid and distance, but it was no longer
computed when launching this Feature.
It was a good learning experience for Android Studio Programming.
 */

package org.openbot.rl;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.openbot.R;
import org.openbot.common.CameraFragment;
import org.openbot.databinding.FragmentLaneDetectionBinding;
import org.openbot.databinding.FragmentRewardBinding;
import org.opencv.core.Point;

public class RewardFragment extends CameraFragment {
    private TextView centroidTextView;
    private TextView distanceTextView;




    private static final int BASE_X_VALUE = 270;
    public RewardFragment() {
        // Required empty public constructor
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward, container, false);

        // Initialize the TextViews
        centroidTextView = view.findViewById(R.id.centroidTextView);
        distanceTextView = view.findViewById(R.id.distanceTextView);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Fragment is in the STARTED state, it's visible but not actively interacting with the user
        // Start any background tasks or processes here
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fragment is in the RESUMED state, it has the user's full attention
        // Perform any UI updates or actions here
    }

    @Override
    public void onPause() {
        super.onPause();
        // Fragment is in the PAUSED state, it's no longer in the foreground
        // Pause any operations that require the user's attention
    }

    @Override
    public void onStop() {
        super.onStop();
        // Fragment is in the STOPPED state, it's no longer visible
        // Stop any background tasks or processes here
    }

    public void updateCentroidAndCalculateDistance(Point centroid) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update the centroid TextView
                String centroidText = "Centroid: (" + (int) centroid.x + ", " + (int) centroid.y + ")";
                centroidTextView.setText(centroidText);

                // Calculate the distance based on the centroid and the base value
                double distance = calculateDistance(centroid);
                // Update the distance TextView
                String distanceText = "Distance: " + distance;
                distanceTextView.setText(distanceText);
            }
        });
    }

    private double calculateDistance(Point centroid) {
        double dx = centroid.x - 270;
        double dy = 0;
        return Math.sqrt(dx * dx + dy * dy);
    }


    @Override
    protected void processFrame(Bitmap image, ImageProxy imageProxy) {

    }

    @Override
    protected void processControllerKeyData(String command) {

    }

    @Override
    protected void processUSBData(String data) {

    }
}
