package com.habiture;

import android.provider.ContactsContract;
import android.util.Base64;

import com.ntil.habiture.MainApplication;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegisterAlarmByHabituresModel  {

    public static void RegisterAlarmByHabitures(List<Habiture> habitures ) {
        int do_it_time =0;
        int id =0;
        for(Habiture habiture : habitures){
            do_it_time =habiture.getDoItTime();
            id=habiture.getId();
            String swear = habiture.getSwear();


            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hours = c.get(Calendar.HOUR_OF_DAY);

            // if hbiture had must do... next day!
            if( hours>do_it_time ) {
                day +=1;
            }

            AlarmModel.setAlarm(MainApplication.getInstance(), year, month, day, do_it_time, 0, 0,id,swear);
        }
    }
}