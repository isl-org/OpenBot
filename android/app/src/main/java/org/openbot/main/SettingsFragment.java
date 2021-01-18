package org.openbot.main;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import org.openbot.R;

public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.root_preferences, rootKey);
  }
}
