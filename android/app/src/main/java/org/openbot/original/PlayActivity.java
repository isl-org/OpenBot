package org.openbot.original;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openbot.R;
import org.openbot.env.SharedPreferencesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PlayActivity extends AppCompatActivity {

    final Bundle bundle = new Bundle();
    ArrayList x_list = new ArrayList();
    ArrayList y_list = new ArrayList();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        init();


    }

    private void init() {
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


                        Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();

                    }
                };


                new Thread() {
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
                        }

                        super.run();
                    }
                }.start();


            }
        });


    }
}

