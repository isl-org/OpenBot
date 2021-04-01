package org.openbot.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.openbot.databinding.FragmentControllerMappingBinding;


public class ControllerMappingFragment extends Fragment {


	private FragmentControllerMappingBinding binding;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentControllerMappingBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}
}