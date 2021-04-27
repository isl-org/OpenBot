package org.openbot.modelManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.openbot.R;
import org.openbot.databinding.FragmentModelManagementBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModelManagementFragment extends Fragment {

	private FragmentModelManagementBinding binding;
	public static final String ALL = "ALL";

	@Nullable
	@Override
	public View onCreateView(
			@NonNull LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		binding = FragmentModelManagementBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		List<String> modelTypes = Arrays.stream(ModelInfo.MODEL_TYPE.values()).map(Enum::toString).collect(Collectors.toList());
		modelTypes.add(0, ALL);

		ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, modelTypes);
		binding.modelSpinner.setAdapter(modelAdapter);

		showModels(loadModelList(ALL));

		binding.modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				showModels(loadModelList(parent.getItemAtPosition(position).toString()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void showModels(List<ModelInfo> modelList) {
		binding.modelContainer.removeAllViews();
		for (ModelInfo modelInfo : modelList) {
			RadioButton radioButton =
					(RadioButton)
							LayoutInflater.from(requireContext())
									.inflate(R.layout.item_model_radio, binding.modelContainer, false);
			radioButton.setText(modelInfo.getName().substring(0, modelInfo.getName().lastIndexOf('.')));
			binding.modelContainer.addView(radioButton);
		}
	}

	private List<ModelInfo> loadModelList(String filter) {

		List<ModelInfo> modelInfoList = new ArrayList<>();

		if (filter.equals(ModelInfo.MODEL_TYPE.AUTOPILOT.toString()) || filter.equals(ALL)) {
			List<ModelInfo> autoList = Arrays.stream(getModelFiles())
					.map(f -> new ModelInfo(f, ModelInfo.MODEL_TYPE.AUTOPILOT))
					.collect(Collectors.toList());
			modelInfoList.addAll(autoList);
		}
		if (filter.equals(ModelInfo.MODEL_TYPE.DETECTOR.toString()) || filter.equals(ALL)) {
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
		}
		return modelInfoList;
	}

	private void downloadFile(String url, String savepPath){
		Ion.with(this)
				.load(url)
				.progress((downloaded, total) -> System.out.println("" + downloaded + " / " + total))
//				.write(new File("/sdcard/really-big-file.zip"))
				.write(new File(savepPath))
				.setCallback(new FutureCallback<File>() {
					@Override
					public void onCompleted(Exception e, File file) {
						// download done...
						// do stuff with the File or error
					}
				});

	}
	private String[] getModelFiles() {
		return requireActivity().getFilesDir().list((dir1, name) -> name.endsWith(".tflite"));
	}
}
