package com.ntil.habiture;


import android.app.Application;
import android.content.Context;

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
        habitureModel = new HabitureModule(new NetworkChannel());
        toolModel = new ToolModel(context);

    }

    public static MainApplication getInstance() {
        return context;
    }

    public HabitureModule getHabitureModel() {
        return habitureModel;
    }
}
