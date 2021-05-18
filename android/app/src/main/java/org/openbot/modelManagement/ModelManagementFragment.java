package org.openbot.modelManagement;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.nononsenseapps.filepicker.Utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.openbot.R;
import org.openbot.databinding.FragmentModelManagementBinding;
import org.openbot.main.OnItemClickListener;
import org.openbot.tflite.Model;
import org.openbot.utils.Constants;
import org.openbot.utils.FileUtils;
import org.openbot.utils.PermissionUtils;

public class ModelManagementFragment extends Fragment
    implements OnItemClickListener<Model>, ModelAdapter.OnItemClickListener<Model> {

  private FragmentModelManagementBinding binding;
  public static final String ALL = "ALL";
  private ModelAdapter adapter;
  private List<Model> masterList;
  private ActivityResultLauncher<Intent> mStartForResult;
  private OnBackPressedCallback onBackPressedCallback;
  private boolean isDownloading = false;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mStartForResult =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
              if (result.getResultCode() == Activity.RESULT_OK) {

                Intent intent = result.getData();
                // Handle the Intent
                List<Uri> files = Utils.getSelectedFilesFromResult(intent);

                String fileName = new File(files.get(0).getPath()).getName();
                if (FileUtils.checkFileExistence(requireActivity(), fileName)) {
                  AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                  builder.setTitle(R.string.file_available_title);
                  builder.setMessage(R.string.file_available_body);
                  builder.setPositiveButton(
                      "Yes", (dialog, id) -> processModelFromStorage(files, fileName));
                  builder.setNegativeButton(
                      "Cancel",
                      (dialog, id) -> {
                        // User cancelled the dialog
                      });
                  AlertDialog dialog = builder.create();
                  dialog.show();
                } else {
                  processModelFromStorage(files, fileName);
                }
              }
            });

    onBackPressedCallback =
        new OnBackPressedCallback(true) {
          @Override
          public void handleOnBackPressed() {
            if (isDownloading) {
              AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
              builder.setTitle(R.string.model_download_title);
              builder.setMessage(R.string.model_download_body);
              builder.setPositiveButton(
                  "Yes",
                  (dialog, id) -> {
                    onBackPressedCallback.setEnabled(false);
                    requireActivity().onBackPressed();
                  });
              builder.setNegativeButton("Cancel", (dialog, id) -> {});
              AlertDialog dialog = builder.create();
              dialog.show();
            } else {
              onBackPressedCallback.setEnabled(false);
              requireActivity().onBackPressed();
            }
          }
        };
    requireActivity().getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
  }

  private void processModelFromStorage(List<Uri> files, String fileName) {

    Model item =
        new Model(
            masterList.size() + 1,
            Model.CLASS.AUTOPILOT_F,
            Model.TYPE.AUTOPILOT,
            fileName,
            Model.PATH_TYPE.FILE,
            requireActivity().getFilesDir() + File.separator + fileName,
            "256x96");
    EditModelDialogFragment edMbS =
        new EditModelDialogFragment(
            item,
            item1 -> {
              try {
                InputStream inputStream =
                    requireActivity().getContentResolver().openInputStream(files.get(0));
                FileUtils.copyFile(
                    inputStream, fileName, requireActivity().getFilesDir().getAbsolutePath());
              } catch (IOException e) {
                e.printStackTrace();
              }
              masterList.add(item1);
              showModels(loadModelList(binding.modelSpinner.getSelectedItem().toString()));
              FileUtils.updateModelConfig(requireActivity(), masterList);
              Toast.makeText(
                      requireContext().getApplicationContext(),
                      "Model added: " + fileName,
                      Toast.LENGTH_SHORT)
                  .show();
            });
    edMbS.show(getChildFragmentManager(), edMbS.getTag());
  }

  private void openPicker() {

    Intent i = new Intent(requireActivity(), BackHandlingFilePickerActivity.class);
    // This works if you defined the intent filter
    // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

    // Set these depending on your use case. These are the defaults.
    i.putExtra(BackHandlingFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
    i.putExtra(BackHandlingFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
    i.putExtra(BackHandlingFilePickerActivity.EXTRA_MODE, BackHandlingFilePickerActivity.MODE_FILE);

    // Configure initial directory by specifying a String.
    // You could specify a String like "/storage/emulated/0/", but that can
    // dangerous. Always use Android's API calls to get paths to the SD-card or
    // internal memory.
    i.putExtra(
        BackHandlingFilePickerActivity.EXTRA_START_PATH,
        Environment.getExternalStorageDirectory().getPath());

    mStartForResult.launch(i);
  }

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
    masterList = FileUtils.loadConfigJSONFromAsset(requireActivity());

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
    binding.modelListContainer.addItemDecoration(
        new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    binding.modelSpinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            showModels(loadModelList(parent.getItemAtPosition(position).toString()));
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

    binding.addModel.setOnClickListener(
        v -> {
          if (!PermissionUtils.hasPermission(requireContext(), Constants.PERMISSION_STORAGE))
            PermissionUtils.requestStoragePermission(this);
          else openPicker();
        });
  }

  private void showModels(List<Model> modelList) {
    adapter.setItems(modelList);
  }

  private List<Model> loadModelList(String filter) {

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
    }

    return modelInfoList;
  }

  @Override
  public void onItemClick(Model item) {

    EditModelDialogFragment edMbS =
        new EditModelDialogFragment(
            item,
            item1 -> {
              adapter.notifyDataSetChanged();
              FileUtils.updateModelConfig(requireActivity(), masterList);
            });
    edMbS.show(getChildFragmentManager(), edMbS.getTag());
  }

  @Override
  public void onModelDownloadClicked() {
    isDownloading = true;
  }

  @Override
  public void onModelDownloaded(boolean status, Model mItem) {
    if (status && isAdded()) {
      isDownloading = false;
      for (Model model : masterList) {
        if (model.id.equals(mItem.id)) {
          model.setPath(requireActivity().getFilesDir() + File.separator + model.name);
          model.setPathType(Model.PATH_TYPE.FILE);
          adapter.notifyDataSetChanged();
          FileUtils.updateModelConfig(requireActivity(), masterList);
          break;
        }
      }
    }
  }

  @Override
  public void onModelDelete(Model mItem) {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
    builder.setTitle(R.string.model_delete_title);
    builder.setMessage(R.string.model_delete_body);
    builder.setPositiveButton(
        "Yes",
        (dialog, id) -> {
          new File(mItem.path).delete();
          masterList.remove(mItem);
          showModels(masterList);
          FileUtils.updateModelConfig(requireActivity(), masterList);
        });
    builder.setNegativeButton("Cancel", (dialog, id) -> {});
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case Constants.REQUEST_STORAGE_PERMISSION:
        // If the permission is granted, start logging,
        // otherwise, show a Toast
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          openPicker();
        } else {
          if (PermissionUtils.shouldShowRational(requireActivity(), Constants.PERMISSION_STORAGE)) {
            Toast.makeText(
                    requireContext().getApplicationContext(),
                    R.string.storage_permission_denied_logging,
                    Toast.LENGTH_LONG)
                .show();
          }
        }
        break;
    }
  }
}
