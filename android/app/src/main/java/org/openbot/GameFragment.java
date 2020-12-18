package org.openbot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.camera.core.ImageProxy;
import org.jetbrains.annotations.NotNull;
import org.openbot.databinding.FragmentGameBinding;

public class GameFragment extends CameraFragment {

  private FragmentGameBinding binding;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentGameBinding.inflate(inflater, container, false);

    return inflateFragment(binding, inflater, container);
  }

  @Override
  protected void processFrame(ImageProxy image) {}
}
