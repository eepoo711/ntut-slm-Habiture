package com.ntil.habiture.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.ntil.habiture.MainApplication;

/**
 * Created by Yeh on 2015/6/19.
 */
public class DownloadPhotoTask extends AsyncTask<Void, DownloadPhotoTask.DataInfo, Void> {

    public interface Listener {
        void onDownloadFinished(int index, byte[] photo);
    }

    private String[] urls;
    private Listener listener;

    class DataInfo {
        int index;
        byte[] photo;
    }

    public DownloadPhotoTask(String[] urls, Listener listener) {
        this.urls = urls;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {

        int index = 0;
        for(String url: urls) {
            if(isCancelled()) break;

            if(!TextUtils.isEmpty(url)) {
                byte[] photo = MainApplication.getInstance().getHabitureModel()
                        .downloadPhoto(url);
                publishProgress(index, photo);
            }


            index ++;
        }

        return null;
    }

    private void publishProgress(int index, byte[] photo) {
        if(photo != null) {
            DataInfo dataInfo = new DataInfo();
            dataInfo.index = index;
            dataInfo.photo = photo;
            publishProgress(dataInfo);
        }
    }

    @Override
    protected void onProgressUpdate(DataInfo... values) {
        listener.onDownloadFinished(
                values[0].index,
                values[0].photo);
    }
}
