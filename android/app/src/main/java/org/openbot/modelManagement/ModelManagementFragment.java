package org.openbot.modelManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.koushikdutta.ion.Ion;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.openbot.databinding.FragmentModelManagementBinding;
import org.openbot.main.OnItemClickListener;
import org.openbot.tflite.Model;
import org.openbot.utils.FileUtils;

public class ModelManagementFragment extends Fragment
    implements OnItemClickListener<Model>, ModelAdapter.OnItemClickListener<Model> {

  private FragmentModelManagementBinding binding;
  public static final String ALL = "ALL";
  private ModelAdapter adapter;

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
    List<String> modelTypes =
        Arrays.stream(Model.TYPE.values()).map(Enum::toString).collect(Collectors.toList());
    modelTypes.add(0, ALL);

    ArrayAdapter<String> modelAdapter =
        new ArrayAdapter<>(
            requireContext(), android.R.layout.simple_dropdown_item_1line, modelTypes);
    binding.modelSpinner.setAdapter(modelAdapter);

    adapter = new ModelAdapter(loadModelList(ALL), this);
    binding.modelListContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.modelListContainer.setAdapter(adapter);
    binding.modelListContainer.addItemDecoration(new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL));
    binding.modelSpinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            showModels(loadModelList(parent.getItemAtPosition(position).toString()));
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
  }

  private void showModels(List<Model> modelList) {
    adapter.setItems(modelList);
  }

  private List<Model> loadModelList(String filter) {

    List<Model> masterList = FileUtils.loadConfigJSONFromAsset(requireActivity());
    if (masterList == null) masterList = new ArrayList<>();

    List<Model> modelInfoList = new ArrayList<>();
    if (filter.equals(Model.TYPE.AUTOPILOT.toString()) || filter.equals(ALL)) {
      modelInfoList.addAll(
          masterList.stream()
              .filter(f -> f.type.equals(Model.TYPE.AUTOPILOT))
              .collect(Collectors.toList()));
    }

    if (filter.equals(Model.TYPE.DETECTOR.toString()) || filter.equals(ALL)) {
      modelInfoList.addAll(
          masterList.stream()
              .filter(f -> f.type.equals(Model.TYPE.DETECTOR))
              .collect(Collectors.toList()));
      /*
            try {
              List<Model> list =
                  Arrays.stream(requireContext().getAssets().list("networks"))
                      .filter(f -> f.endsWith(".tflite"))
                      .map(
                          f ->
                              new Model(
                                  Model.CLASS.MOBILENETV1_1_0_Q,
                                  Model.TYPE.DETECTOR,
                                  f,
                                  null,
                                  f,
                                  new Size(256, 96)))
                      .collect(Collectors.toList());

              modelInfoList.addAll(list);

            } catch (IOException e) {
              e.printStackTrace();
            }
      */
    }

    return modelInfoList;
  }

  private String[] getModelFiles() {
    return requireActivity().getFilesDir().list((dir1, name) -> name.endsWith(".tflite"));
  }

  @Override
  public void onItemClick(Model item) {}

  @Override
  public void onDownloadClick(Model item) {
    Ion.with(this)
        .load(item.path)
        .progress((downloaded, total) -> System.out.println("" + downloaded + " / " + total))
            .write(new File("/sdcard/openbot/tf.tflite"))
        .setCallback(
            (e, file) -> {
              // download done...
              // do stuff with the File or error
            });
  }
}
