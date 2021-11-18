package org.openbot.voice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

public class TimeAlarmManager {

    public void reservationTimeByHour(Date date, Context context) {
        Intent intent = new Intent(context, org.openbot.voice.TimeAlarmReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Bundle bundle = new Bundle();
        bundle.putSerializable("bundleData", date);
        intent.putExtra("bundleDataByHour", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //알람 설정
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    public void reservationTimeByMin(Date date, Context context) {
        Intent intent = new Intent(context, org.openbot.voice.TimeAlarmReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Bundle bundle = new Bundle();
        bundle.putSerializable("bundleData", calendar);
        intent.putExtra("bundleDataByHour", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //알람 설정
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}
