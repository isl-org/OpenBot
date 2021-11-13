package org.openbot.original;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openbot.R;
import org.openbot.env.Vehicle;
import org.openbot.utils.Enums;
import org.openbot.OpenBotApplication;

//import org.openbot.env.SharedPreferencesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class PlayActivity extends AppCompatActivity {

    final Bundle bundle = new Bundle();
    ArrayList x_list = new ArrayList();
    ArrayList y_list = new ArrayList();
    ArrayList angle = new ArrayList();
    ArrayList distance = new ArrayList();
    private Vehicle vehicle;


    ArrayList<Double> movingLength = new ArrayList<Double>();
    ArrayList<Double> movingDegree = new ArrayList<Double>();


    double degree;
    double gyro;

    //센서 받아오는 변수들


    //Using the Accelometer & Gyroscoper
    private SensorManager mSensorManager = null;

    //Using the Gyroscope
    private SensorEventListener mGyroLis;
    private Sensor mGgyroSensor = null;

    //Roll and Pitch
    private double pitch;
    private double roll;
    private double yaw;

    //timestamp and dt
    private double timestamp;
    private double dt;

    // for radian -> dgree
    private double RAD2DGR = 180 / Math.PI;
    private static final float NS2S = 1.0f / 1000000000.0f;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        request();
        vehicle = OpenBotApplication.vehicle;

    }

    private void request() {
        EditText gx_num = findViewById(R.id.gx);
        EditText gy_num = findViewById(R.id.gy);
        Button sendBtn = findViewById(R.id.SendBtn);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                x_list.clear();
                y_list.clear();


                String str = gx_num.getText().toString() + "/" + gy_num.getText().toString();
                String url = "https://mysterious-sea-88696.herokuapp.com/" + str;

                //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();


                //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();


                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        Bundle bundle = msg.getData();
                        String temp = bundle.getString("hi");
                        temp.trim();
                        temp = temp.replace(" ", "");
                        temp = temp.replace("[", "");
                        temp = temp.replace("]", "");

                        String[] arr = temp.split(",");


                        for (int i = 0; i < arr.length; i++) {
                            if (i % 2 == 0) {
                                x_list.add(arr[i]);
                            } else {
                                y_list.add(arr[i]);
                            }
                        }


                        Collections.reverse(x_list);
                        Collections.reverse(y_list);


                        System.out.println("사이즈는 " + x_list.size());
                        System.out.println("사이즈는 " + y_list.size());


                        System.out.println("x임다");

                        for (int i = 0; i < x_list.size(); i++) {
                            System.out.println(x_list.get(i));
                        }

                        System.out.println("y임다");

                        for (int i = 0; i < y_list.size(); i++) {
                            System.out.println(y_list.get(i));
                        }


                        tracking();


                        Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();

                    }
                };


                Thread t1 = new Thread() {

                    @Override
                    public void run() {
                        String title = "";


                        Document doc = null;
                        try {
                            doc = Jsoup.connect(url).get();
                            Elements mElementDatas = doc.select("body");
                            title = mElementDatas.text();
//                            for(Element elem : mElementDatas){
//                                title = elem.select("body").text();
//                            }
                            //System.out.print(title);
                            bundle.putString("hi", title);
                            Message msg = handler.obtainMessage();
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            System.out.println("쓰레드종료됨");
                        }

//                        super.run();
                    }
                };
                t1.start();

            }
        });

        tracking();



    }

    private void tracking() {
        angle();
        distance();
        getGyro();




        for (int i = 0; i<angle.size();i++){
            double range = (Double.parseDouble(angle.get(i).toString()));
            //아두이노에 회전 명령(왼쪽이면 양수, 오른쪽 회전이면 음수)
            while(degree < range *0.97) {
                if (range>0) {
                    Toast.makeText(this, "돌아감", Toast.LENGTH_LONG).show();
                    vehicle.sendControl(-130, 0);
                }
                else
                    vehicle.sendControl(0,-130);
                degree +=gyro;
            }
            //직진 명령
//            long t= System.currentTimeMillis();
//            long end = t+15000;
//            while(System.currentTimeMillis() < end) {
//                 do something
//                 pause to avoid churning
//                Thread.sleep( xxx );
//            }
            //거리 계산한것 만큼 아두이노로 start 신호 보냄.
        }
    }

    private void angle() {
        //ArrayList<Double> initialDegree = new ArrayList<Double>();
        for (int i = 0; i < x_list.size() - 1; i++) {
            double current = 0;

            double gap_y = Double.parseDouble(y_list.get(i + 1).toString()) - Double.parseDouble(y_list.get(i).toString());
            double gap_x = Double.parseDouble(x_list.get(i + 1).toString()) - Double.parseDouble(x_list.get(i).toString());


            current = (Math.atan2(gap_y, gap_x) * 180) / Math.PI;
            //initialDegree.add(current);
            movingDegree.add(current);
        }

//        for (int i = 0; i < initialDegree.size(); i++) {
//            double movingAngle = initialDegree.get(i);
//            if (i == 0) {
//                movingDegree.add(movingAngle);
//            } else {
//                movingAngle -= initialDegree.get(i - 1);
//                movingDegree.add(movingAngle);
//            }
//        }


        System.out.println("앵글값");

        for (int i = 0; i < movingDegree.size(); i++) {
            System.out.println(movingDegree.get(i));
        }


    }

    private void distance() {
        for (int i = 0; i < x_list.size() - 1; i++) {

            double gap_y = Double.parseDouble(y_list.get(i + 1).toString()) - Double.parseDouble(y_list.get(i).toString());
            double gap_x = Double.parseDouble(x_list.get(i + 1).toString()) - Double.parseDouble(x_list.get(i).toString());

            double current = Math.sqrt(((gap_x) * (gap_x)) + ((gap_y) * (gap_y)));
            movingLength.add(current);
        }


        System.out.println("거리값");

        for (int i = 0; i < movingLength.size(); i++) {
            System.out.println(movingLength.get(i));
        }
    }

    private int getGyro() {


        //Using the Gyroscope & Accelometer
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Using the Accelometer
        mGgyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGyroLis = new GyroscopeListener();
        Button getBtn = findViewById(R.id.getBtn);
        mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);


        //Touch Listener for Accelometer
        findViewById(R.id.getBtn).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        roll = 0;
                        mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);
                        vehicle.sendControl(-130, 0);
                        getBtn.setText("GETTING...");
                        break;

                    case MotionEvent.ACTION_UP:
                        //mSensorManager.unregisterListener(mGyroLis);
                        getBtn.setText("GET_SENSOR");
                        break;

                }
                return false;
            }
        });


        return 1;

    }


    private class GyroscopeListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {

            /* 각 축의 각속도 성분을 받는다. */
            double gyroX = event.values[0];
            double gyroY = event.values[1];
            double gyroZ = event.values[2];

            /* 각속도를 적분하여 회전각을 추출하기 위해 적분 간격(dt)을 구한다.
             * dt : 센서가 현재 상태를 감지하는 시간 간격
             * NS2S : nano second -> second */
            dt = (event.timestamp - timestamp) * NS2S;
            timestamp = event.timestamp;

            /* 맨 센서 인식을 활성화 하여 처음 timestamp가 0일때는 dt값이 올바르지 않으므로 넘어간다. */
            if (dt - timestamp * NS2S != 0) {

                /* 각속도 성분을 적분 -> 회전각(pitch, roll)으로 변환.
                 * 여기까지의 pitch, roll의 단위는 '라디안'이다.
                 * SO 아래 로그 출력부분에서 멤버변수 'RAD2DGR'를 곱해주어 degree로 변환해줌.  */
                pitch = pitch + gyroY * dt;
                roll = roll + gyroX * dt;
                yaw = yaw + gyroZ * dt;


                degree = roll * RAD2DGR;

                TextView sensor_text = findViewById(R.id.sensorText);
                String dtos = String.valueOf(degree);
                sensor_text.setText(dtos);


                Log.e("LOG", "GYROSCOPE           [X]:" + String.format("%.4f", event.values[0])
                        + "           [Y]:" + String.format("%.4f", event.values[1])
                        + "           [Z]:" + String.format("%.4f", event.values[2])
                        + "           [Pitch]: " + String.format("%.1f", pitch * RAD2DGR)
                        + "           [Roll]: " + String.format("%.1f", roll * RAD2DGR)
                        + "           [Yaw]: " + String.format("%.1f", yaw * RAD2DGR)
                        + "           [dt]: " + String.format("%.4f", dt));

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}

