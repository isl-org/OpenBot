package org.openbot.logging;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.databinding.DialogSensorsBinding;
import org.openbot.env.SharedPreferencesManager;

public class SensorsDialog extends DialogFragment {

  public SensorsDialog() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
  }

  @Override
  public View onCreateView(
          @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    SharedPreferencesManager preferencesManager = new SharedPreferencesManager(requireContext());
    DialogSensorsBinding binding = DialogSensorsBinding.inflate(LayoutInflater.from(getContext()));
    SensorManager sensorManager =
        (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
    List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

    HashMap<String, Boolean> list = new HashMap<>();

    for (Sensor sensor : sensorList) {
      if (sensor
          .getName()
          .toLowerCase()
          .contains(SharedPreferencesManager.ACCELEROMETER.toLowerCase())) {
        list.put(
            SharedPreferencesManager.ACCELEROMETER,
            preferencesManager.getSensorStatus(SharedPreferencesManager.ACCELEROMETER));
      }
      if (sensor
          .getName()
          .toLowerCase()
          .contains(SharedPreferencesManager.GYROSCOPE.toLowerCase())) {
        list.put(
            SharedPreferencesManager.GYROSCOPE,
            preferencesManager.getSensorStatus(SharedPreferencesManager.GYROSCOPE));
      }
      if (sensor
          .getName()
          .toLowerCase()
          .contains(SharedPreferencesManager.MAGNETIC.toLowerCase())) {
        list.put(
            SharedPreferencesManager.MAGNETIC,
            preferencesManager.getSensorStatus(SharedPreferencesManager.MAGNETIC));
      }
      if (sensor.getName().toLowerCase().contains(SharedPreferencesManager.LIGHT.toLowerCase())) {
        list.put(
            SharedPreferencesManager.LIGHT,
            preferencesManager.getSensorStatus(SharedPreferencesManager.LIGHT));
      }
      if (sensor.getName().toLowerCase().contains(SharedPreferencesManager.GRAVITY.toLowerCase())) {
        list.put(
            SharedPreferencesManager.GRAVITY,
            preferencesManager.getSensorStatus(SharedPreferencesManager.GRAVITY));
      }
      if (sensor
          .getName()
          .toLowerCase()
          .contains(SharedPreferencesManager.PROXIMITY.toLowerCase())) {
        list.put(
            SharedPreferencesManager.PROXIMITY,
            preferencesManager.getSensorStatus(SharedPreferencesManager.PROXIMITY));
      }
    }
    SensorListAdapter adapter = new SensorListAdapter(list, preferencesManager);

    binding.listView.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.listView.setAdapter(adapter);
    binding.dismiss.setOnClickListener(v -> dismiss());
    return binding.getRoot();
  }
}
