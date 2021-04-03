package org.openbot.controller;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.openbot.R;
import org.openbot.databinding.FragmentControllerMappingBinding;
import org.openbot.utils.Constants;


public class ControllerMappingFragment extends Fragment {


	private FragmentControllerMappingBinding binding;

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
		requireActivity()
				.getSupportFragmentManager()
				.setFragmentResultListener(
						Constants.GENERIC_MOTION_EVENT_CONTINUOUS,
						this,
						(requestKey, result) -> {
							MotionEvent motionEvent = result.getParcelable(Constants.DATA_CONTINUOUS);

						});
		requireActivity()
				.getSupportFragmentManager()
				.setFragmentResultListener(
						Constants.KEY_EVENT_CONTINUOUS,
						this,
						(requestKey, result) -> processKeyEvent(result.getParcelable(Constants.DATA_CONTINUOUS)));

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
					break;
				case KeyEvent.KEYCODE_BUTTON_THUMBR:
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