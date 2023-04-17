package org.openbot.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import org.openbot.R;
import org.openbot.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends Fragment {
  private FragmentEditProfileBinding binding;
  private ImageButton editProfilePicBtn;
  private ImageView profilePicture;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentEditProfileBinding.inflate(inflater, container, false);
    editProfilePicBtn = binding.getRoot().findViewById(R.id.edit_profile_pic_btn);
    profilePicture = binding.getRoot().findViewById(R.id.profile_pic);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    editProfilePicBtn.setOnClickListener(v -> imageChooser());
  }

  /** open image gallery to choose for update profile pic. */
  private void imageChooser() {
    Intent i = new Intent();
    i.setType("image/*");
    i.setAction(Intent.ACTION_GET_CONTENT);
    launchImagePickerActivity.launch(i);
  }

  ActivityResultLauncher<Intent> launchImagePickerActivity =
      registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
              Intent data = result.getData();
              if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                Bitmap selectedImageBitmap = null;
                try {
                  selectedImageBitmap =
                      MediaStore.Images.Media.getBitmap(
                          requireActivity().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                  e.printStackTrace();
                }
                profilePicture.setImageBitmap(selectedImageBitmap);
              }
            }
          });
}
