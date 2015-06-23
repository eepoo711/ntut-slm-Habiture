package com.habiture;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ntil.habiture.R;

import java.util.Calendar;

public class AlarmModel extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data =intent.getExtras();
        System.out.println("=======================================");
        System.out.println("\r\n\r\n\r\nalarm,extra="+data.toString());
        System.out.println("=======================================");
        if(data.get("key").equals("habiture_alarm_key")) {
            // TODO:...
        }
    }

    // sample code
    /*
        AlarmModel.setAlarm(this, 2015, 6, 22, 0, 50, 0,2323);
     */

    public static void setAlarm(Context context, int year, int month, int day, int hour, int minute, int second, int habitureID ) {
        Calendar mCal = Calendar.getInstance();
        mCal.set(year, month-1, day, hour, minute, second);
        Intent intent = new Intent(context, AlarmModel.class);
        intent.putExtra("key", "habiture_alarm_key");
        intent.putExtra("id", habitureID);

        // 以日期字串組出不同的 category
        intent.addCategory("D" + String.valueOf(mCal.get(Calendar.YEAR)) + String.valueOf((mCal.get(Calendar.MONTH) + 1)) + String.valueOf(mCal.get(Calendar.DATE)));
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, mCal.getTimeInMillis(), pi);
    }
}