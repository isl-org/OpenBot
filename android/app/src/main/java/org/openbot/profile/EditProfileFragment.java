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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.openbot.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends Fragment {
  private FragmentEditProfileBinding binding;
  private FirebaseUser user;
  private Uri selectedImageUri;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the FragmentEditProfile layout.
    binding = FragmentEditProfileBinding.inflate(inflater, container, false);
    // Get the current user.
    user = FirebaseAuth.getInstance().getCurrentUser();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Set click listener on the "Edit Profile Picture" button and call function to select image
    // from local storage.
    binding.editProfilePicBtn.setOnClickListener(v -> imageChooser());
    // If the current user is not null, set the user profile details.
    if (user != null) {
      setProfileDetails();
    }
    binding.saveChanges.setOnClickListener(
        v -> {
          // Get the user's first name and last name
          String userName =
              binding.firstName.getText().toString() + " " + binding.lastName.getText().toString();
          // Call the updateProfile method with the selected image URI and the user's name
          updateProfile(selectedImageUri, userName);
        });
    // Set click listener on the "Cancel" button to go back to the previous fragment
    binding.cancelChanges.setOnClickListener(
        v -> Navigation.findNavController(requireView()).popBackStack());
  }

  /** Method to open the image gallery to choose an image for the profile picture. */
  private void imageChooser() {
    Intent i = new Intent();
    i.setType("image/*");
    i.setAction(Intent.ACTION_GET_CONTENT);
    launchImagePickerActivity.launch(i);
  }

  /** Register for activity result using the ActivityResultLauncher. */
  ActivityResultLauncher<Intent> launchImagePickerActivity =
      registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
              Intent data = result.getData();
              if (data != null && data.getData() != null) {
                // Set the selectedImageUri to the chosen image URI.
                selectedImageUri = data.getData();
                try {
                  // Load the selected image into a bitmap.
                  Bitmap selectedImageBitmap =
                      MediaStore.Images.Media.getBitmap(
                          requireActivity().getContentResolver(), selectedImageUri);
                  // Set the selected image bitmap to the profile picture ImageView.
                  binding.profilePic.setImageBitmap(selectedImageBitmap);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }
          });

  /** Method to update the user's profile with the selected image URI and user name. */
  private void updateProfile(Uri imageUri, String userName) {
    startLoader(true);
    if (imageUri != null && !userName.isBlank()) {
      try {
        // Get a reference to the Firebase Storage location where the image will be stored
        StorageReference storageRef =
            FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pictures/" + user.getUid() + ".jpg");
        // Upload the compressed image to Firebase Storage
        Bitmap bitmap =
            MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Compress the selected image to JPEG format with 50% quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask
            .addOnSuccessListener(
                taskSnapshot -> {
                  // Get the download URL of the uploaded image
                  storageRef
                      .getDownloadUrl()
                      .addOnSuccessListener(
                          uri ->
                              user.updateProfile(
                                  new UserProfileChangeRequest.Builder()
                                      .setDisplayName(userName)
                                      .setPhotoUri(uri)
                                      .build()));
                  startLoader(false);
                })
            .addOnFailureListener(
                e -> {
                  // Failed to upload image
                  startLoader(false);
                });
      } catch (IOException e) {
        startLoader(false);
        e.printStackTrace();
      }
    } else if (!userName.isBlank()) {
      user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(userName).build());
      startLoader(false);
    }
  }

  /** Set the profile details from the FirebaseUser object to the UI elements in the layout. */
  private void setProfileDetails() {

    // Load the profile picture using Glide library
    Glide.with(this).load(user.getPhotoUrl()).into(binding.profilePic);

    // Split the full name into first and last names, and set them to their respective text fields
    String fullName = user.getDisplayName();
    String firstName = "";
    String lastName = "";
    if (fullName != null && !fullName.isEmpty()) {
      String[] parts = fullName.split("\\s+");
      if (parts.length > 0) {
        firstName = parts[0];
      }
      if (parts.length > 1) {
        lastName = parts[parts.length - 1];
      }
    }
    binding.profilePic.setImageURI(user.getPhotoUrl());
    binding.firstName.setText(firstName);
    binding.lastName.setText(lastName);
    binding.emailAddress.setText(user.getEmail());
  }

  /**
   * Show or hide the loader views based on the given boolean value.
   *
   * @param isStart true if the loader views should be shown, false otherwise
   */
  private void startLoader(Boolean isStart) {
    if (isStart) {
      binding.loaderView.setVisibility(View.VISIBLE);
      binding.profileUpdateLoader.setVisibility(View.VISIBLE);
    } else {
      binding.loaderView.setVisibility(View.GONE);
      binding.profileUpdateLoader.setVisibility(View.GONE);
    }
  }
}
