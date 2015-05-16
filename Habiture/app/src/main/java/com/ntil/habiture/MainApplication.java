package com.ntil.habiture;


import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.gcm.client.receiver.GcmModel;
import com.habiture.HabitureModule;
import com.habiture.NetworkChannel;
import com.habiture.ToolModel;

public class MainApplication extends Application{

    private static MainApplication context;
    private HabitureModule habitureModel;
    private ToolModel toolModel;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        toolModel = new ToolModel(context);

    }

    public static MainApplication getInstance() {
        return context;
    }

    public void createHabitureModel(Activity activity) {
        if(habitureModel == null) {
            GcmModel gcmModel = new GcmModel(activity);
            habitureModel = new HabitureModule(new NetworkChannel(), gcmModel);
        }
    }

    public HabitureModule getHabitureModel() {
        return habitureModel;
    }
}
