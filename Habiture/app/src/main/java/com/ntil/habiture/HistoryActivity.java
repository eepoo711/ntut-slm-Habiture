package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.habiture.GroupHistory;
import com.habiture.HabitureModule;
import com.ntil.habiture.task.DownloadPhotoTask;

import java.util.List;

public class HistoryActivity extends Activity {

    private static final boolean DEBUG = false;
    private static final String TAG = "HistoryActivity";
    private static void trace(String message) {
        if(DEBUG) Log.d(TAG, message);
    }

    private static List<GroupHistory> _groupHistories;

    private HabitureModule mHabitureModule;
    private HistoryAdapter historyAdapter;
    private DownloadPhotoTask historyTask;

    public static void startActivity(Context context, List<GroupHistory> groupHistories) {
        trace("startActivity");

        _groupHistories = groupHistories;
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

        historyAdapter = new HistoryAdapter(this, _groupHistories);

        getFragmentManager().beginTransaction()
                .add(R.id.profileContainer, profileFragment)
                .add(R.id.historyContainer, HistoryFragment.newInstance(historyAdapter))
                .commit();

        downloadPhoto();

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
}
