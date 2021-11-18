package org.openbot.voice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import org.openbot.R;
import org.openbot.main.MainActivity;

import java.util.Calendar;

public class TimeAlarmReceiver extends BroadcastReceiver {
    private static final String id = "TimeChannel";
    private static final String name = "TimeCheckChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle getBundle = intent.getParcelableExtra("bundleDataByHour");
        Calendar data = (Calendar) getBundle.getSerializable("bundleData");

        int month = data.get(Calendar.MONTH);
        int day = data.get(Calendar.DAY_OF_MONTH);
        int hour = data.get(Calendar.HOUR);
        int min = data.get(Calendar.MINUTE);

        //NotificationChannel, Builder 설정
        NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableVibration(true);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //content 까지 보여줌

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentTitle("스마또봇 시간 푸쉬 알림")
                .setContentText("예약된 시간 : " + month + "월 " + day + "일 " + hour + "시 " + min + "분")
                .setAutoCancel(true);


        //Notify 창(헤더 창) 클릭 이벤트
        Intent toMainIntent = new Intent(context, MainActivity.class);
        toMainIntent.putExtra("timeNotification", "스마또봇 시간 알림");
        toMainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent toMainPendingIntent = PendingIntent.getActivity(context, 1, toMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(toMainPendingIntent);
        builder.setColor(Color.BLUE);

        //notification 매니저 설정
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);

        Notification notification = builder.build();
        manager.notify(10, notification);
    }
}
