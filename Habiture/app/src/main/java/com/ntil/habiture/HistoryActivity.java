package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.habiture.GroupHistory;
import com.habiture.HabitureModule;
import com.ntil.habiture.task.DownloadPhotoTask;

import java.util.List;

import utils.exception.ExceptionAlertDialog;

public class HistoryActivity extends Activity {

    private static final boolean DEBUG = false;
    private static final String TAG = "HistoryActivity";
    private static void trace(String message) {
        if(DEBUG) Log.d(TAG, message);
    }

    private List<GroupHistory> _groupHistories;

    private static int _pid = -1;

    private HabitureModule mHabitureModule;
    private HistoryAdapter historyAdapter;
    private DownloadPhotoTask historyTask;

    public static void startActivity(Context context, int pid) {
        trace("startActivity");


        _pid = pid;
        context.startActivity(new Intent(context, HistoryActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trace("onCreate");

        setContentView(R.layout.activity_history);

        mHabitureModule = MainApplication.getInstance().getHabitureModel();

        Fragment profileFragment = HomeTopFragment.newInstance(
                mHabitureModule.getName()
                , mHabitureModule.getHeader());

        getFragmentManager().beginTransaction()
                .add(R.id.profileContainer, profileFragment)
                .commit();

        new GroupHistoryTask().execute(_pid);
    }

    private void downloadPhoto() {
        String[] urls = new String[_groupHistories.size()];
        for(int i = 0; i < _groupHistories.size(); i++) {
            urls[i] = _groupHistories.get(i).getUrl();
        }
        DownloadPhotoTask.Listener listener = new DownloadPhotoTask.Listener() {
            @Override
            public void onDownloadFinished(int index, byte[] photo) {
                historyAdapter.setPhoto(photo, index);
            }
        };
        historyTask = new DownloadPhotoTask(urls, listener);
        historyTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        historyTask.cancel(true);
        historyAdapter.release();
    }

    private class GroupHistoryTask extends AsyncTask<Integer, Void, List<GroupHistory>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            trace("GroupHistoryTask onPreExecute");
            try {
                progress = ProgressDialog.show(HistoryActivity.this,
                        "習慣成真",
                        "載入中...");
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<GroupHistory> doInBackground(Integer... params) {
            trace("GroupHistoryTask doInBackground");
            List<GroupHistory> ret = null;
            try {
                int ownerId = params[0];
                ret = mHabitureModule.gueryGroupHistory(ownerId);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
            return ret;
        }

        @Override
        protected void onPostExecute(List<GroupHistory> groupHistories) {
            trace("GroupHistoryTask onPostExecute");
            progress.dismiss();
            if (groupHistories != null) {
                _groupHistories = groupHistories;
                historyAdapter = new HistoryAdapter(HistoryActivity.this, _groupHistories);

                getFragmentManager().beginTransaction()
                        .add(R.id.historyContainer, HistoryFragment.newInstance(historyAdapter))
                        .commitAllowingStateLoss();

                downloadPhoto();
            } else {
                Toast.makeText(HistoryActivity.this, "載入資料失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
