package com.habiture;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.ntil.habiture.HabitListFragment;
import com.ntil.habiture.HomeMiddleFragment;
import com.ntil.habiture.MainApplication;
import com.ntil.habiture.R;

import java.util.List;

import utils.exception.ExceptionAlertDialog;

public class BootCompletedModel extends BroadcastReceiver {
    private HabitureModule mHabitureModule;
    @Override
    public void onReceive(Context context, Intent intent) {
        //mHabitureModule = MainApplication.getInstance().getHabitureModel();
        //new QueryHabituresTask().execute();
    }

    private class QueryHabituresTask extends AsyncTask<Void, Void, List<Habiture>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(List<Habiture> habitures) {
            RegisterAlarmByHabituresModel.RegisterAlarmByHabitures(habitures);
        }
        @Override
        protected List<Habiture> doInBackground(Void... params) {
            return mHabitureModule.queryHabitures();
        }
    }
}