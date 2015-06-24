package com.habiture;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.ntil.habiture.R;

import java.util.Calendar;
import java.util.Random;

public class AlarmModel extends BroadcastReceiver {
    private static Random random_tool=null;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data =intent.getExtras();
        if(data.get("key").equals("habiture_alarm_key")) {
            String swear =data.get("swear").toString();
            int id=Integer.valueOf(data.get("id").toString());

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentTitle(swear)
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .setContentText("該執行習慣囉~");

            NotificationManager notificationManager;
            notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationID=id*10000;
            notificationManager.notify(notificationID, mBuilder.build());

            if(random_tool==null) {
                random_tool = new Random();
            }
            int random_tool_id =random_tool.nextInt(6)+1;
            Intent broadcastIntent = new Intent(context.getString(R.string.play_tool_sound));
            broadcastIntent.putExtra("tool_id",random_tool_id);
            context.sendBroadcast(broadcastIntent);
        }
    }

    // sample code
    /*
        AlarmModel.setAlarm(this, 2015, 6, 22, 0, 50, 0,2323,"i swear");
     */

    public static void setAlarm(Context context, int year, int month, int day, int hour, int minute, int second, int habitureID, String swear ) {
        int index =0;
        int setDays =7;
        for( index=0; index<setDays; index++ ) {
            Calendar mCal = Calendar.getInstance();
            mCal.set(year, month-1, day+index, hour, minute, second);
            Intent intent = new Intent(context, AlarmModel.class);
            intent.putExtra("key", "habiture_alarm_key");
            intent.putExtra("id", habitureID);
            intent.putExtra("swear", swear);

            // 以日期字串組出不同的 category
            intent.addCategory("D" + String.valueOf(mCal.get(Calendar.YEAR)) + String.valueOf((mCal.get(Calendar.MONTH) + 1)) + String.valueOf(mCal.get(Calendar.DATE)));
            PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, mCal.getTimeInMillis(), pi);
        }
    }
}