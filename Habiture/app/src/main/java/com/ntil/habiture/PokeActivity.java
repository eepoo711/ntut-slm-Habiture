package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.habiture.HabitureModule;

import utils.exception.ExceptionAlertDialog;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class PokeActivity extends Activity {

    private HabitureModule mHabitureModule;
    private static final boolean DEBUG = true;
    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeActivity", message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);
        String name = MainApplication.getInstance().getHabitureModel().getAccount();
        mHabitureModule = MainApplication.getInstance().getHabitureModel();

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.profileContainer, HomeTopFragment.newInstance(name,mHabitureModule.getHeader()))
                    .add(R.id.pokeContainer, new PokeFragment())
                    .commit();
        }

        registerToolBroadReceiver();
    }
    private void registerToolBroadReceiver() {
        BroadcastReceiver toolBroadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int to_id =intent.getIntExtra("to_id",1);
                int pid =intent.getIntExtra("pid",154);
                int tool_id =intent.getIntExtra("tool_id",1);
                trace("registerToolBroadReceiver(), to_id="+to_id+" pid="+pid+" tool_id="+tool_id);
                new SendToolTask().execute(to_id,pid,tool_id);
            }
        };
        registerReceiver(toolBroadReceiver,new IntentFilter(this.getString(R.string.tool_clicck_intent_name)));
    }

    private class SendToolTask extends AsyncTask<Integer, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            trace("doInBackground");

            boolean is_tool_sent = false;
            try {
                int to_id = params[0];
                int pid = params[1];
                int tool_id = params[2];
                is_tool_sent =mHabitureModule.sendSoundToPartner(to_id,pid,tool_id);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

            return is_tool_sent;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("onPostExecute");

            try {

                String toolSentSuccessful = getString(R.string.tool_sent_successful);
                String toolSentFailed = getString(R.string.tool_sent_failed);
                Toast.makeText(
                        PokeActivity.this,
                        success ? toolSentSuccessful : toolSentFailed,
                        Toast.LENGTH_SHORT).show();

            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }
    }
}
