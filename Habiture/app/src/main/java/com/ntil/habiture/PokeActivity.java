package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.habiture.GroupHistory;
import com.habiture.HabitureModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import utils.exception.ExceptionAlertDialog;
import utils.exception.UnhandledException;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class PokeActivity extends Activity implements PokeFragment.Listener{

    private final static int CAMERA_REQUEST = 66 ;
    private HabitureModule mHabitureModule;
    private int mPid;
    private String mSwear;
    private static final boolean DEBUG = true;

    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeActivity", message);
    }

    public static void startActivity(Context context, String url, String swear, String punishment,
            int pid, int frequency, int doItTime, int goal) {
        Intent intent = new Intent(context, PokeActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("swear", swear);
        intent.putExtra("punishment", punishment);
        intent.putExtra("pid", pid);
        intent.putExtra("frequency", frequency);
        intent.putExtra("doItTime", doItTime);
        intent.putExtra("goal", goal);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);
        String name = MainApplication.getInstance().getHabitureModel().getAccount();
        mHabitureModule = MainApplication.getInstance().getHabitureModel();
        mPid = getIntent().getIntExtra("pid", -1);
        new QueryOwnerPhoto().execute(getIntent().getStringExtra("url"));
        trace("pid = " + mPid);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // call this function after capture
        trace("onActivityResult");

        if (requestCode == CAMERA_REQUEST && data != null) {
            String path = Environment.getExternalStorageDirectory()+"/image.jpg";
            String pid = "" + mPid;
            new UploadProofTask().execute(pid, path);
        }
    }

    @Override
    public void onClickCamera() {
        trace("onClickCamera");
        File tmpFile = new File( Environment.getExternalStorageDirectory(), "/image.jpg");
        Uri outputFileUri = Uri.fromFile(tmpFile);

        // open camera
        Intent intent =  new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, CAMERA_REQUEST);
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onClickRecords() {
        trace("onClickRecords");
        new GroupHistoryTask().execute(154);
    }

    @Override
    public void onClickGroupFriend() {
        trace("onClickGroupFriend");
    }

    @Override
    public void onClickTool() {
        trace("onClickTool");
    }

    @Override
    public void onPoke() {
        Random random_tool = new Random();
        int random_tool_id =random_tool.nextInt(6)+1;
        // TODO: to guest now
        int to_id =1;
        Intent broadcastIntent = new Intent(this.getString(R.string.tool_clicck_intent_name));
        broadcastIntent.putExtra("to_id",1);
        broadcastIntent.putExtra("pid",mPid);
        broadcastIntent.putExtra("tool_id",random_tool_id);
        this.sendBroadcast(broadcastIntent);
    }

    private class GroupHistoryTask extends AsyncTask<Integer, Void, List<GroupHistory>> {

        @Override
        protected void onPreExecute() {
            trace("GroupHistoryTask onPreExecute");
        }

        @Override
        protected List<GroupHistory> doInBackground(Integer... params) {
            trace("GroupHistoryTask doInBackground");
            List<GroupHistory> ret = null;
            try {
                int pid = params[0];
                ret = mHabitureModule.gueryGroupHistory(pid);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
            return ret;
        }

        @Override
        protected void onPostExecute(List<GroupHistory> groupHistories) {
            trace("GroupHistoryTask onPostExecute");
            getFragmentManager().beginTransaction()
                    .replace(R.id.pokeContainer, HistoryFragment.newInstance(groupHistories))
                    .addToBackStack(null)
                    .commit();
        }
    }


    private class UploadProofTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            trace("UploadProofTask onPreExecute");
            try {
                progress = ProgressDialog.show(PokeActivity.this,
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
                int pid = Integer.valueOf(params[0]);
                String path = params[1];

                is_upload = mHabitureModule.uploadProofImage(pid, path);
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
                    PokeActivity.this,
                    success ? "上傳成功" : "上傳失敗",
                    Toast.LENGTH_SHORT).show();
        }
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

    private class QueryOwnerPhoto extends AsyncTask<String, Void, Bitmap> {
        private ProgressDialog progress;
        private int pid;
        private String url;

        @Override
        protected void onPreExecute() {
            trace("QueryOwnerPhoto onPreExecute");
            try {
                progress = ProgressDialog.show(PokeActivity.this,
                        "習慣成真",
                        "載入中...");
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            trace("QueryOwnerPhoto doInBackground");
            url = params[0];
            Bitmap bitmap = null;

            try {
                bitmap = mHabitureModule.queryBitmapUrl(url);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            trace("QueryOwnerPhoto onPostExecute");
            try {
                progress.dismiss();
                if (bitmap != null) {
                    getFragmentManager().beginTransaction()
                            .add(R.id.profileContainer, HomeTopFragment.newInstance(
                                    mHabitureModule.getAccount()
                                    , mHabitureModule.getHeader()))
                            .add(R.id.pokeContainer, PokeFragment.newInstance(bitmap, "123", "456", 1, 1, 1))
                            .commit();
                } else {
                    Toast.makeText(PokeActivity.this, "載入資料失敗", Toast.LENGTH_SHORT).show();
                }

            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }



    }
}
