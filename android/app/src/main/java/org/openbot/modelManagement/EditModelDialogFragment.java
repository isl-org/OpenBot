package org.openbot.modelManagement;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.openbot.R;
import org.openbot.databinding.DialogEditModelBinding;
import org.openbot.main.OnItemClickListener;
import org.openbot.tflite.Model;

public class EditModelDialogFragment extends DialogFragment {

  private DialogEditModelBinding binding;
  private final Model model;
  private final OnItemClickListener<Model> itemClickListener;

  public EditModelDialogFragment(Model model, OnItemClickListener<Model> itemClickListener) {
    this.model = model;
    this.itemClickListener = itemClickListener;
  }

  @Override
  public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = DialogEditModelBinding.inflate(inflater, container, false);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    List<String> modelTypes =
        Arrays.stream(Model.TYPE.values()).map(Enum::toString).collect(Collectors.toList());
    ArrayAdapter<String> modelAdapter =
        new ArrayAdapter<>(
            requireContext(), android.R.layout.simple_dropdown_item_1line, modelTypes);
    binding.typeSpinner.setAdapter(modelAdapter);
    binding.typeSpinner.setSelection(model.type.ordinal());

    List<String> classes =
        Arrays.stream(Model.CLASS.values()).map(Enum::toString).collect(Collectors.toList());
    ArrayAdapter<String> classAdapter =
        new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, classes);
    binding.classSpinner.setAdapter(classAdapter);
    binding.classSpinner.setSelection(model.classType.ordinal());

    binding.name.setText(model.getName());
    binding.inputWidth.setText(String.valueOf(model.getInputSize().getWidth()));
    binding.inputHeight.setText(String.valueOf(model.getInputSize().getHeight()));

    binding.btnSubmit.setOnClickListener(
        v -> {
          model.setName(binding.name.getText().toString());
          model.setType(Model.TYPE.valueOf(binding.typeSpinner.getSelectedItem().toString()));
          model.setClassType(Model.CLASS.valueOf(binding.classSpinner.getSelectedItem().toString()));
          model.setInputSize(new Size(Integer.parseInt(binding.inputWidth.getText().toString()),
                  Integer.parseInt(binding.inputHeight.getText().toString())));
          itemClickListener.onItemClick(model);
          dismiss();
        });
    binding.dismiss.setOnClickListener(v -> dismiss());
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return super.onCreateDialog(savedInstanceState);
  }
}
