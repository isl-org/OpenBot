package org.openbot.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.internal.ViewUtils;

import org.openbot.R;
import org.openbot.databinding.FragmentControllerMappingBinding;
import org.openbot.utils.Constants;

import timber.log.Timber;


public class ControllerMappingFragment extends Fragment {


	private FragmentControllerMappingBinding binding;
	private Pair<Float,Float> originalLocationLeftJoyTip;
	private Pair<Float,Float> originalLocationRightJoyTip;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentControllerMappingBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		binding.getRoot().post(() -> {
			originalLocationLeftJoyTip = new Pair<>(binding.joyLeftTip.getX(), binding.joyLeftTip.getY());
			originalLocationRightJoyTip = new Pair<>(binding.joyRightTip.getX(), binding.joyRightTip.getY());
		});

		requireActivity()
				.getSupportFragmentManager()
				.setFragmentResultListener(
						Constants.GENERIC_MOTION_EVENT,
						this,
						(requestKey, result) -> {
							MotionEvent motionEvent = result.getParcelable(Constants.DATA);
							processJoyStickInput(motionEvent);
						});
		requireActivity()
				.getSupportFragmentManager()
				.setFragmentResultListener(
						Constants.KEY_EVENT_CONTINUOUS,
						this,
						(requestKey, result) -> processKeyEvent(result.getParcelable(Constants.DATA_CONTINUOUS)));

	}

	@SuppressLint("RestrictedApi")
	private void processJoyStickInput(MotionEvent motionEvent) {
		Pair<Float, Float> pair = JoyStickProcessor.processJoystickInputLeft(motionEvent, -1);

		binding.joyLeftTip.animate().translationX(ViewUtils.dpToPx(requireContext(), 15) * pair.first);
		binding.joyLeftTip.animate().translationY(ViewUtils.dpToPx(requireContext(), 15) *pair.second);

		Pair<Float, Float> pair2 = JoyStickProcessor.processJoystickInputRight(motionEvent, -1);
		binding.joyRightTip.animate().translationX(ViewUtils.dpToPx(requireContext(), 15) * pair2.first);
		binding.joyRightTip.animate().translationY(ViewUtils.dpToPx(requireContext(), 15) *pair2.second);
	}

	private void processKeyEvent(KeyEvent keyCode) {
			switch (keyCode.getKeyCode()) {

				case KeyEvent.KEYCODE_BUTTON_X: // square
					toggleView(binding.btnX,keyCode);
						break;
				case KeyEvent.KEYCODE_BUTTON_Y: // triangle
					toggleView(binding.btnY,keyCode);
					break;
				case KeyEvent.KEYCODE_BUTTON_B: // circle
					toggleView(binding.btnB,keyCode);
					break;

				case KeyEvent.KEYCODE_BUTTON_A: // x
					toggleView(binding.btnA,keyCode);
					break;
				case KeyEvent.KEYCODE_BUTTON_START: // options
					break;
				case KeyEvent.KEYCODE_BUTTON_L1:
					break;
				case KeyEvent.KEYCODE_BUTTON_R1:
					break;
				case KeyEvent.KEYCODE_BUTTON_THUMBL:
					toggleView(binding.joyLeft,keyCode);
					break;
				case KeyEvent.KEYCODE_BUTTON_THUMBR:
					toggleView(binding.joyRight,keyCode);
					break;

				default:
					break;
			}
	}
	private void toggleView(ImageView imageView, KeyEvent keyCode)
	{
		if (keyCode.getAction() == KeyEvent.ACTION_DOWN)
			imageView.setImageResource(R.drawable.ic_circle_selected);
		if (keyCode.getAction() == KeyEvent.ACTION_UP)
			imageView.setImageResource(R.drawable.ic_outline_circle);

	}

}