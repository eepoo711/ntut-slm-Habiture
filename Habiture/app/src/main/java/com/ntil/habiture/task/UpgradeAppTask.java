package com.ntil.habiture.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.habiture.AppInfo;
import com.habiture.FileStream;
import com.habiture.HabitureModule;
import com.ntil.habiture.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import utils.Utils;
import utils.exception.UnhandledException;

/**
 * Created by Yeh on 2015/5/30.
 */
public class UpgradeAppTask extends AsyncTask<Void, Void, File> {

    private static final boolean DEBUG = true;
    private static final String APP_NAME = "tempApp.apk";

    private static void trace(String message) {
        if(DEBUG)
            Log.d("UpgradeAppTask", message);
    }

    private Activity activity;
    private HabitureModule habitureModule;
    private ProgressDialog progress;
    private int nowVersionCode;
    private String nowVersionName;

    public UpgradeAppTask(Activity activity, HabitureModule habitureModule) {
        trace("UpgradeAppTask <init>");
        this.activity = activity;
        this.habitureModule = habitureModule;
    }

    @Override
    protected void onPreExecute() {
        trace("onPreExecute");
        super.onPreExecute();

        progress = ProgressDialog.show(activity, activity.getString(R.string.progress_title), activity.getString(R.string.progress_upgrade_app));

        nowVersionName = getNowVersionName();
        nowVersionCode = getNowVersionCode();
        trace("nowVersionName = " + nowVersionName + " nowVersionCode = " + nowVersionCode);
    }

    @Override
    protected File doInBackground(Void... params) {
        trace("doInBackground");

        AppInfo appInfo = habitureModule.getOnlineAppInfo();


        if(appInfo == null
                || isLatestVersionNow(appInfo))
            return null;

        // download file
        FileStream fileStream = habitureModule.downloadFile(appInfo.getUrl());
        if(fileStream == null) {
            trace("doInBackground >> download file failed.");
            return null;
        }

        // copy app file to internal storage
        InputStream in = fileStream.getInputStream();
        int length = fileStream.getContentLength();
        OutputStream out = null;
        try {
            out = activity.openFileOutput(APP_NAME, Context.MODE_WORLD_READABLE);
            byte[] buffer = new byte[1024];
            int totalReadSize = 0;
            for(int readLen = in.read(buffer); readLen > 0; readLen = in.read(buffer)) {
                out.write(buffer, 0, readLen);
                totalReadSize += readLen;
            }
            if(totalReadSize != length) {
                trace("doInBackground >> download failed network unstable.");
                return null;
            }
            return activity.getFileStreamPath(APP_NAME);
        } catch (FileNotFoundException e) {
            throw new UnhandledException(e);
        } catch (IOException e) {
            throw new UnhandledException(e);
        } finally {
            Utils.closeIO(out);
            if(fileStream != null) fileStream.close();
        }
    }

    private boolean isLatestVersionNow(AppInfo appInfo) {
        return nowVersionCode > appInfo.getVersionCode();
    }

    @Override
    protected void onPostExecute(File app) {
        trace("onPostExecute");
        super.onPostExecute(app);


        progress.dismiss();

        if(app != null && !activity.isFinishing()) {
            installApp(app);
        }
    }

    private int getNowVersionCode() {

        try {
            trace("getNowVersionCode " + activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode);
            return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new UnhandledException(e);
        }
    }

    private String getNowVersionName() {
        try {
            trace("getNowVersionName " + activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName);
            return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new UnhandledException(e);
        }
    }

    private void installApp(File app) {
        trace("installApp");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(app), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
