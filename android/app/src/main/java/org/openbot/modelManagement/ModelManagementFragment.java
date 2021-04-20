package org.openbot.modelManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.openbot.R;
import org.openbot.databinding.FragmentModelManagementBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModelManagementFragment extends Fragment {

	private FragmentModelManagementBinding binding;
	private List<ModelInfo> modelList;

	@Nullable
	@Override
	public View onCreateView(
			@NonNull LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		modelList = new ArrayList<>();
		binding = FragmentModelManagementBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
      List<String> modelTypes = Arrays.stream(ModelInfo.MODEL_TYPE.values()).map(Enum::toString).collect(Collectors.toList());
      modelTypes.add(0,"ALL");

      ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, modelTypes);
		binding.modelSpinner.setAdapter(modelAdapter);
		modelList.addAll(loadAllModels());
		showModels(modelList);
	}

	private void showModels(List<ModelInfo> modelList) {
		for (ModelInfo modelInfo : modelList) {
			RadioButton radioButton =
					(RadioButton)
							LayoutInflater.from(requireContext())
									.inflate(R.layout.item_model_radio, binding.modelContainer, false);
			radioButton.setText(modelInfo.getName().substring(0, modelInfo.getName().lastIndexOf('.')));
			binding.modelContainer.addView(radioButton);
		}
	}

	private List<ModelInfo> loadAllModels() {

		List<ModelInfo> modelInfoList =
				Arrays.stream(getModelFiles())
						.map(f -> new ModelInfo(f, ModelInfo.MODEL_TYPE.AUTOPILOT))
						.collect(Collectors.toList());
		try {
			List<ModelInfo> list =
					Arrays.stream(requireContext().getAssets().list("networks"))
							.filter(f -> f.endsWith(".tflite"))
							.map(f -> new ModelInfo(f, ModelInfo.MODEL_TYPE.DETECTOR))
							.collect(Collectors.toList());

			modelInfoList.addAll(list);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return modelInfoList;
	}

	private String[] getModelFiles() {
		return requireActivity().getFilesDir().list((dir1, name) -> name.endsWith(".tflite"));
	}
}
