package com.ntil.habiture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.habiture.HabitureModule;

import utils.exception.ExceptionAlertDialog;
/**
 * Created by Dewei on 2015/6/23.
 */
public class RecordActivity extends Activity implements RecordFragment.Listener {
    private HabitureModule mHabitureModule;
    private Bitmap mBitmapCaputred = null;
    private RecordFragment recordFragment;
    private static final boolean DEBUG = true;
    private final static int CAMERA_REQUEST = 100 ;
    private static int g_pid;

    private static void trace(String message) {
        if(DEBUG)
            Log.d("RecordActivity", message);
    }

    public static void startActivity(Context context, int pid) {
        trace("startActivity pid = "+pid);
        Intent intent = new Intent(context, RecordActivity.class);
        RecordActivity.g_pid = pid;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mHabitureModule = MainApplication.getInstance().getHabitureModel();

        recordFragment = new RecordFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.container, recordFragment)
                .commit();
    }

    @Override
    public void onUploadClicked(String text) {
        trace("onUploadClicked text = " + text);
        if(mBitmapCaputred!=null)
            new UploadProofTask().execute(text);
        else
            Toast.makeText(this,"沒有照片無法上傳", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelClicked() {
        trace("onCancelClicked");
        finish();
    }

    @Override
    public void onCamera() {
        trace("onCamera");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        trace("onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);

        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    mBitmapCaputred = (Bitmap) data.getExtras().get("data");
                    recordFragment.setPhoto(mBitmapCaputred);
                }
                break;
        }
    }

    private class UploadProofTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("UploadProofTask onPreExecute");
            try {
                progress = ProgressDialog.show(RecordActivity.this,
                        "習慣成真",
                        "上傳中...");
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            trace("UploadProofTask doInBackground");
            boolean is_upload = false;
            try {
                String text = params[0];
                is_upload = mHabitureModule.uploadProofImage(g_pid,text, mBitmapCaputred);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
            return is_upload;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("UploadProofTask onPostExecute");
            progress.dismiss();

            Toast.makeText(
                    RecordActivity.this,
                    success ? "上傳成功" : "上傳失敗",
                    Toast.LENGTH_SHORT).show();
            finish();

            if(success) {
                HistoryActivity.startActivity(RecordActivity.this, g_pid);
            }
        }

    }
}
