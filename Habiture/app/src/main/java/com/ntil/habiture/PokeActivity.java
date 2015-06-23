package com.ntil.habiture;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.habiture.GroupHistory;
import com.habiture.HabitureModule;
import com.habiture.PokeData;
import com.ntil.habiture.task.DownloadPhotoTask;

import java.util.List;
import java.util.Random;

import utils.exception.ExceptionAlertDialog;


/**
 * Created by GawinHsu on 5/7/15.
 */
public class PokeActivity extends Activity implements PokeFragment.Listener, FounderAdapter.Listener{

    private HabitureModule mHabitureModule;
    private Bitmap mBitmapPoke;
    private static final boolean DEBUG = true;
    private PokeFragment mPoketFragment;
    private static Random random_tool=null;
    private static PokeData pokeData;
    private static boolean isFounder = false;
    private static int pid = -1;
    private FounderAdapter founderAdapter = null;



    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeActivity", message);
    }

    public static void startActivity(Context context, PokeData pokeData, boolean isFounder, int pid) {
        Intent intent = new Intent(context, PokeActivity.class);
        PokeActivity.pokeData = pokeData;
        PokeActivity.isFounder = isFounder;
        PokeActivity.pid = pid;
        random_tool = new Random();
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);
        String name = MainApplication.getInstance().getHabitureModel().getAccount();
        mHabitureModule = MainApplication.getInstance().getHabitureModel();

        founderAdapter = new FounderAdapter(this, pokeData.getFounderList());

        if (savedInstanceState == null) {
            mPoketFragment = PokeFragment.newInstance(pokeData, isFounder, founderAdapter);
            getFragmentManager().beginTransaction()
                    .add(R.id.profileContainer, HomeTopFragment.newInstance(
                            mHabitureModule.getName()
                            , mHabitureModule.getHeader()))
                    .add(R.id.pokeContainer, mPoketFragment)
                    .commit();
        }
        registerToolBroadReceiver();
        downloadPhoto();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHabitureModule.stopSendSoundTimer();
        unregisterReceiver(toolBroadReceiver);
        photoTask.cancel(true);
        founderAdapter.release();
    }

    private BroadcastReceiver toolBroadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int to_id=intent.getIntExtra("to_id",1);
            int tool_id =intent.getIntExtra("tool_id",1);
            trace("registerToolBroadReceiver(), to_id="+to_id+" pid="+pid+" tool_id="+tool_id);
            new SendToolTask().execute(to_id,pid,tool_id);
        }
    };

    private void registerToolBroadReceiver() {
        registerReceiver(toolBroadReceiver, new IntentFilter(this.getString(R.string.tool_clicck_intent_name)));
<<<<<<< HEAD
=======
<<<<<<< HEAD
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // call this function after capture
        trace("onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            mBitmapCaputred = (Bitmap) data.getExtras().get("data");
            new UploadProofTask().execute(pid);
        }
=======
>>>>>>> origin/master
>>>>>>> origin/Add-ViewPage
    }

    @Override
    public void onClickCamera() {
        trace("onClickCamera");
        RecordActivity.startActivity(this, pid);
    }

    @Override
    public void onClickRecords() {
        trace("onClickRecords");

        HistoryActivity.startActivity(this, pid);
    }

    @Override
    public void onClickGroupFriend() {
        trace("onClickGroupFriend");
        DialogFragment newFragment = new NoImplementDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClickTool() {
        trace("onClickTool");
        DialogFragment newFragment = new NoImplementDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClickFollow() {
        trace("onClickFollow");
        new FollowTask().execute();
    }

    @Override
    public void onClickAlarm() {
        trace("onClickAlarm");
        DialogFragment newFragment = new NoImplementDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onPoke(int position) {
        int random_tool_id =random_tool.nextInt(6)+1;
        System.out.println("onPoke="+random_tool_id);
        // TODO: to guest now
        Intent broadcastIntent = new Intent(this.getString(R.string.tool_clicck_intent_name));
        broadcastIntent.putExtra("to_id",pokeData.getFounderList().get(position).getUid());
        broadcastIntent.putExtra("pid",pid);
        broadcastIntent.putExtra("tool_id", random_tool_id);
        this.sendBroadcast(broadcastIntent);

        // Client's business
        Intent broadcastIntent_client_playsound = new Intent(getApplicationContext().getString(R.string.play_tool_sound));
        broadcastIntent_client_playsound.putExtra("tool_id",random_tool_id);
        sendBroadcast(broadcastIntent_client_playsound);
    }


    private class FollowTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("PassTask onPreExecute");

            try {
                progress = ProgressDialog.show(PokeActivity.this,
                        PokeActivity.this.getString(R.string.progress_title),
                        "載入中...");
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            trace("PassTask doInBackground");

            return mHabitureModule.followHabit(pid);
        }

        @Override
        protected void onPostExecute(Boolean isFollow) {
            trace("PassTask onPostExecute");
            progress.dismiss();
            if (isFollow)
                Toast.makeText(PokeActivity.this, "follow success", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(PokeActivity.this, "follow failed", Toast.LENGTH_SHORT).show();
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
                int from_id = params[1];
                int tool_id = params[2];
                is_tool_sent =mHabitureModule.sendSoundToPartner(to_id,from_id,tool_id);
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

//    private class QueryOwnerPhoto extends AsyncTask<String, Void, Bitmap> {
//        private String url;
//
//        @Override
//        protected void onPreExecute() {
//            trace("QueryOwnerPhoto onPreExecute");
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            trace("QueryOwnerPhoto doInBackground");
//            url = params[0];
//            Bitmap bitmap = null;
//
//            try {
//                bitmap = mHabitureModule.queryBitmapUrl(url);
//            } catch (Throwable e) {
//                ExceptionAlertDialog.showException(getFragmentManager(), e);
//            }
//            return bitmap;
//
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            trace("QueryOwnerPhoto onPostExecute");
//            try {
//                if (bitmap != null && !PokeActivity.this.isFinishing()) {
//                    mBitmapPoke = bitmap;
//                    //mPoketFragment.setImage(mBitmapPoke);
//                } else {
//                    Toast.makeText(PokeActivity.this, "載入資料失敗", Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (Throwable e) {
//                ExceptionAlertDialog.showException(getFragmentManager(), e);
//            }
//        }
//    }


    private DownloadPhotoTask photoTask;
    private void downloadPhoto() {
        String[] urls = new String[pokeData.getFounderList().size()];
        for(int i = 0; i < pokeData.getFounderList().size(); i++) {
            urls[i] = pokeData.getFounderList().get(i).getUrl();
        }
        DownloadPhotoTask.Listener listener = new DownloadPhotoTask.Listener() {
            @Override
            public void onDownloadFinished(int index, byte[] photo) {
                founderAdapter.setPhoto(photo, index);
            }
        };
        photoTask = new DownloadPhotoTask(urls, listener);
        photoTask.execute();
    }


}
