package org.openbot.main;

import static android.widget.Toast.makeText;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.openbot.R;
import org.openbot.common.FeatureList;
import org.openbot.databinding.FragmentMainBinding;
import org.openbot.model.SubCategory;
import org.openbot.original.DefaultActivity;
import org.openbot.original.PlayActivity;
import org.openbot.voice.TimeAlarmManager;
import org.openbot.voice.WeatherGetter;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import timber.log.Timber;

public class MainFragment extends Fragment implements OnItemClickListener<SubCategory>, RecognitionListener {

  private MainViewModel mViewModel;
  private FragmentMainBinding binding;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentMainBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    binding.list.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.list.setAdapter(new CategoryAdapter(FeatureList.getCategories(), this));
  }

  @Override
  public void onItemClick(SubCategory subCategory) {

    Timber.d("onItemClick: %s", subCategory.getTitle());

    switch (subCategory.getTitle()) {
      case FeatureList.DEFAULT:
        Intent intent = new Intent(requireActivity(), DefaultActivity.class);
        startActivity(intent);
        break;

      case FeatureList.FREE_ROAM:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_robotCommunicationFragment);
        break;

      case FeatureList.DATA_COLLECTION:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_loggerFragment);
        break;

      case FeatureList.CONTROLLER:
        // For a library module, uncomment the following line
        // intent = new Intent(this, ControllerActivity.class);
        // startActivity(intent);
        break;
      case FeatureList.AUTOPILOT:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_autopilotFragment);
        break;

      case FeatureList.OBJECT_NAV:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_objectNavFragment);
        break;

      case FeatureList.CONTROLLER_MAPPING:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_controllerMappingFragment);
        break;

      case FeatureList.MODEL_MANAGEMENT:
        Navigation.findNavController(requireView())
            .navigate(R.id.action_mainFragment_to_modelManagementFragment);
        break;

      case FeatureList.PLAY:
        Intent intent2 = new Intent(requireActivity(), PlayActivity.class);
        startActivity(intent2);
        break;

      case FeatureList.VOICE:
        initPermission();
        if(!canVoiceRec) {
          Toast.makeText(getContext(), "활성화", Toast.LENGTH_SHORT).show();
          new SetupTask(this).execute();
          canVoiceRec = true;
        }
        else {
          Toast.makeText(getContext(), "비활성화", Toast.LENGTH_SHORT).show();
          new SetupTask(this).cancel(true);
          canVoiceRec = false;
        }
        break;
    }
  }

  public static Boolean canVoiceRec = false;

  private SpeechRecognizer recognizer;
  private TimeAlarmManager timeAlarmManager = new TimeAlarmManager();
  private WeatherGetter weatherGetter = new WeatherGetter();

  private static final String FAIL = "fail";
  private static final String SUCCESS = "success";
  private static final String KEYPHRASE = "ya max"; // 야 맥스

  /* Used to handle permission request */
  private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

  public void initPermission() {
    // Check if user has given permission to record audio
    int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
    }
  }
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupTask(this).execute();
      } /*else {
        finish();
      }*/
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();

    if (recognizer != null) {
      recognizer.cancel();
      recognizer.shutdown();
    }
  }

  @Override
  public void onBeginningOfSpeech() {

  }

  @Override
  public void onEndOfSpeech() {
    if (!recognizer.getSearchName().equals(FAIL))
      switchSearch(FAIL);
  }

  @Override
  public void onPartialResult(Hypothesis hypothesis) {
    if (hypothesis == null)
      return;

    String text = hypothesis.getHypstr();
    if (text.equals(KEYPHRASE))
      switchSearch(SUCCESS);
//        else
//            ((TextView) findViewById(R.id.ListeningTextView)).setText(text);
  }

  private void switchSearch(String searchName) {
    recognizer.stop();

    // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
    if (searchName.equals(FAIL)) {
      recognizer.startListening(searchName);
    } else {
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        public void run() {
//        알림음 나오게 해야될 듯
          Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
          intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
          intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

          android.speech.SpeechRecognizer speechRecognizer;
          speechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(getActivity());
          speechRecognizer.setRecognitionListener(listener);
          speechRecognizer.startListening(intent);
        }
      }, 1000);
    }
  }

  @Override
  public void onResult(Hypothesis hypothesis) {
//    ((TextView) findViewById(R.id.ListeningTextView)).setText("");
//    if (hypothesis != null) {
//      String text = hypothesis.getHypstr();
//      makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//    }
  }

  @Override
  public void onError(Exception e) {
    Toast.makeText(getContext(), "오류 발생", Toast.LENGTH_SHORT).show();
//    ((TextView) findViewById(R.id.flagTextView)).setText("오류 발생");
  }

  @Override
  public void onTimeout() {
    switchSearch(FAIL);
  }

//  public void init() {
//    new SetupTask((MainActivity) getActivity()).execute();
//
//  }

  private static class SetupTask extends AsyncTask<Void, Void, Exception> {
    WeakReference<MainFragment> activityReference;

    SetupTask(MainFragment fragment) {
      this.activityReference = new WeakReference<>(fragment);
    }

    @Override
    protected Exception doInBackground(Void... params) {
      try {
        Assets assets = new Assets(activityReference.get().getActivity());
        File assetDir = assets.syncAssets();
        activityReference.get().setupRecognizer(assetDir);
      } catch (IOException e) {
        return e;
      }
      return null;
    }

    @Override
    protected void onPostExecute(Exception result) {
      if (result == null) {
        activityReference.get().switchSearch(FAIL);
      }
    }
  }

  private void setupRecognizer(File assetsDir) throws IOException {
    // The recognizer can be configured to perform multiple searches
    // of different kind and switch between them

    recognizer = SpeechRecognizerSetup.defaultSetup()
            .setAcousticModel(new File(assetsDir, "en-us-ptm"))
            .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

            .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

            .getRecognizer();
    recognizer.addListener(this);

        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

    // Create keyword-activation search.
    recognizer.addKeyphraseSearch(FAIL, KEYPHRASE);
    recognizer.addKeyphraseSearch(SUCCESS, KEYPHRASE);
  }


  private android.speech.RecognitionListener listener = new android.speech.RecognitionListener() {
    @Override
    public void onReadyForSpeech(Bundle params) {
//            ((TextView) findViewById(R.id.ListeningTextView)).setText("Listening...");
      Toast.makeText(getContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
      Toast.makeText(getContext(), "음성인식을 종료합니다.", Toast.LENGTH_SHORT).show();
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          recognizer.startListening(FAIL);
        }
      }, 1000);
    }

    @Override
    public void onError(int error) {
      String message;
      switch (error) {
        case android.speech.SpeechRecognizer.ERROR_AUDIO:
          message = "오디오 에러";
          break;
        case android.speech.SpeechRecognizer.ERROR_CLIENT:
          message = "클라이언트 에러";
          break;
        case android.speech.SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
          message = "퍼미션 없음";
          break;
        case android.speech.SpeechRecognizer.ERROR_NETWORK:
          message = "네트워크 에러";
          break;
        case android.speech.SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
          message = "네트웍 타임아웃";
          break;
        case android.speech.SpeechRecognizer.ERROR_NO_MATCH:
          message = "찾을 수 없음";
          break;
        case android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
          message = "RECOGNIZER가 바쁨";
          break;
        case android.speech.SpeechRecognizer.ERROR_SERVER:
          message = "서버가 이상함";
          break;
        case android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
          message = "말하는 시간초과";
          break;
        default:
          message = "알 수 없는 오류임";
          break;
      }
      Toast.makeText(getContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResults(Bundle results) {
//      ArrayList<String> matches = results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
//
//      for (int i = 0; i < matches.size(); i++) {
//        ((TextView) findViewById(R.id.ListeningTextView)).setText(matches.get(i));
//      }
//
//      TextView listeningText = (TextView)findViewById(R.id.ListeningTextView);
//      String str = listeningText.getText().toString();
//      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
//      Calendar calendar = Calendar.getInstance();
//
//      int alarmByHour = 0;
//      int alarmByMin = 0;
//
//      if (str.contains("시간 뒤 알림") || str.contains("시간 뒤에 알림")) {  //시간 알림
//        alarmByHour = str.charAt(0) - '0';
//        //int hour = calendar.get(Calendar.HOUR);
//
//        calendar.add(Calendar.HOUR, alarmByHour);
//        calendar.set(Calendar.SECOND, 3);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        String reservationTime = dateFormat.format(calendar.getTime());
//        Toast.makeText(getApplicationContext(), reservationTime + "에 알림 예약되었습니다", Toast.LENGTH_SHORT).show();
//
//        try {
//          timeAlarmManager.reservationTimeByHour(calendar.getTime(), getApplicationContext());
//        } catch (NullPointerException ex) {
//          Toast.makeText(getApplicationContext(), "몇 시간 후인지 정확히 인식되지 않았습니다.", Toast.LENGTH_SHORT).show();
//        }
//
//      } else if (str.contains("분 뒤 알림") || str.contains("분 뒤에 알림")) { // 분 알림
//        alarmByMin = str.charAt(0) - '0';
//
//        calendar.add(Calendar.MINUTE, alarmByMin);
//        calendar.set(Calendar.SECOND, 3);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        String reservationTime = dateFormat.format(calendar.getTime());
//        Toast.makeText(getApplicationContext(), reservationTime + "에 알림 예약되었습니다", Toast.LENGTH_SHORT).show();
//
//        try {
//          timeAlarmManager.reservationTimeByMin(calendar.getTime(), getApplicationContext());
//          System.out.println(reservationTime + "에 알림 설정되었다");
//        } catch (NullPointerException ex) {
//          if (getApplicationContext() == null) {
//            Toast.makeText(getApplicationContext(), "context null error", Toast.LENGTH_SHORT).show();
//          } else {
//            Toast.makeText(getApplicationContext(), "몇 분 후인지 정확히 인식되지 않았습니다.", Toast.LENGTH_SHORT).show();
//          }
//        }
//      }
//            else if (str.contains("날씨 어때")) {
//                String weatherStr = weatherGetter.getWeatherInfo();
//
//                if (weatherStr == null)
//                    Toast.makeText(getApplicationContext(), "날씨를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
//            }
//


    }

    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
  };
}
