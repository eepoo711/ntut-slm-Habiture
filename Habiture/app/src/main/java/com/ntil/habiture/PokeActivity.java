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
    private Bitmap mBitmapCaputred;
    private Bitmap mBitmapPoke;
    private static final boolean DEBUG = false;
    private PokeFragment mPoketFragment;
    private static Random random_tool=null;

    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeActivity", message);
    }

    public static void startActivity(Context context, boolean isFounder, String url, String swear,
            String punishment,int pid, int to_id, int frequency, int doItTime, int goal, int remain) {
        Intent intent = new Intent(context, PokeActivity.class);
        intent.putExtra("isFounder", isFounder);
        intent.putExtra("url", url);
        intent.putExtra("swear", swear);
        intent.putExtra("punishment", punishment);
        intent.putExtra("pid", pid);
        intent.putExtra("frequency", frequency);
        intent.putExtra("doItTime", doItTime);
        intent.putExtra("goal", goal);
        intent.putExtra("to_id", to_id);
        intent.putExtra("remain", remain);
        context.startActivity(intent);

        random_tool = new Random();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);
        String name = MainApplication.getInstance().getHabitureModel().getAccount();
        mHabitureModule = MainApplication.getInstance().getHabitureModel();

        if (savedInstanceState == null) {
            mPoketFragment = PokeFragment.newInstance(getIntent().getBooleanExtra("isFounder", false),
                    getIntent().getStringExtra("swear"), getIntent().getStringExtra("punishment"),
                    getIntent().getIntExtra("frequency", 0), getIntent().getIntExtra("doItTime", 0),
                    getIntent().getIntExtra("goal", 0), getIntent().getIntExtra("remain", 0));
            getFragmentManager().beginTransaction()
                    .add(R.id.profileContainer, HomeTopFragment.newInstance(
                            mHabitureModule.getName()
                            , mHabitureModule.getHeader()))
                    .add(R.id.pokeContainer, mPoketFragment)
                    .commit();
            new QueryOwnerPhoto().execute(getIntent().getStringExtra("url"));
        }
        registerToolBroadReceiver();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(toolBroadReceiver);
    }

    private BroadcastReceiver toolBroadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int to_id =intent.getIntExtra("to_id",1);
            int pid =intent.getIntExtra("pid",154);
            int tool_id =intent.getIntExtra("tool_id",1);
            trace("registerToolBroadReceiver(), to_id="+to_id+" pid="+pid+" tool_id="+tool_id);
            new SendToolTask().execute(to_id,pid,tool_id);
        }
    };

    private void registerToolBroadReceiver() {
        registerReceiver(toolBroadReceiver,new IntentFilter(this.getString(R.string.tool_clicck_intent_name)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // call this function after capture
        trace("onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            mBitmapCaputred = (Bitmap) data.getExtras().get("data");
            new UploadProofTask().execute(getIntent().getIntExtra("pid", 0));
        }
    }

    @Override
    public void onClickCamera() {
        trace("onClickCamera");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onClickRecords() {
        trace("onClickRecords");
        new GroupHistoryTask().execute(getIntent().getIntExtra("pid", 0));
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
        int random_tool_id =random_tool.nextInt(6)+1;
        System.out.println("onPoke="+random_tool_id);
        // TODO: to guest now
        Intent broadcastIntent = new Intent(this.getString(R.string.tool_clicck_intent_name));
        broadcastIntent.putExtra("to_id",getIntent().getIntExtra("to_id", 1));
        broadcastIntent.putExtra("pid",getIntent().getIntExtra("pid", 154));
        broadcastIntent.putExtra("tool_id", random_tool_id);
        this.sendBroadcast(broadcastIntent);

        // Client's business
        Intent broadcastIntent_client_playsound = new Intent(getApplicationContext().getString(R.string.play_tool_sound));
        broadcastIntent_client_playsound.putExtra("tool_id",random_tool_id);
        sendBroadcast(broadcastIntent_client_playsound);
    }

    private class GroupHistoryTask extends AsyncTask<Integer, Void, List<GroupHistory>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            trace("GroupHistoryTask onPreExecute");
            try {
                progress = ProgressDialog.show(PokeActivity.this,
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
            progress.dismiss();
            if (groupHistories != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.pokeContainer, HistoryFragment.newInstance(groupHistories))
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(PokeActivity.this, "載入資料失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class UploadProofTask extends AsyncTask<Integer, Void, Boolean> {
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
        protected Boolean doInBackground(Integer... params) {
            trace("UploadProofTask doInBackground");
            boolean is_upload = false;
            try {
                int pid = params[0];

                is_upload = mHabitureModule.uploadProofImage(pid, mBitmapCaputred);
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
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }
    }

    private class QueryOwnerPhoto extends AsyncTask<String, Void, Bitmap> {
        private int pid;
        private String url;

        @Override
        protected void onPreExecute() {
            trace("QueryOwnerPhoto onPreExecute");
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
                if (bitmap != null && !PokeActivity.this.isFinishing()) {
                    mBitmapPoke = bitmap;
                    mPoketFragment.setImage(mBitmapPoke);
                } else {
                    Toast.makeText(PokeActivity.this, "載入資料失敗", Toast.LENGTH_SHORT).show();
                }

            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }



    }
}
