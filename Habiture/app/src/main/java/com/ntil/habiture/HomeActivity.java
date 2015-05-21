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

import com.habiture.Friend;
import com.habiture.Group;
import com.habiture.Habiture;
import com.habiture.HabitureModule;
import com.habiture.PokeData;

import java.util.List;

import utils.exception.ExceptionAlertDialog;

/**
 * Created by GawinHsu on 5/6/15.
 */
public class HomeActivity extends Activity implements HomeBottomFragment.Listener,
        ExitAlertDialog.Listener, GroupFragment.Listener, HabitListFragment.Listener,
        HabitListAdapter.Listener {
    private HabitureModule mHabitureModule;
    private Bitmap mBitmapCaputred;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        trace("onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            mBitmapCaputred = (Bitmap) data.getExtras().get("data");
            new UploadProofTask().execute(getIntent().getIntExtra("pid", 0));
        }
    }

    private final static int CAMERA_REQUEST = 66 ;

    private static final boolean DEBUG = true;
    private void trace(String message) {
        if(DEBUG)
            Log.d("HomeActivity", message);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        trace("onCreate");

        mHabitureModule = MainApplication.getInstance().getHabitureModel();

        if(savedInstanceState == null) {

            String name = mHabitureModule.getAccount();
            getFragmentManager().beginTransaction()
                    .add(R.id.topContainer, HomeTopFragment.newInstance(name,mHabitureModule.getHeader()))
                    .add(R.id.middleContainer, new HomeMiddleFragment())
                    .add(R.id.bottomContainer, new HomeBottomFragment())
                    .commit();
        }

        registerRegisterIDBroadReceiver();
        mHabitureModule.registerGCM();


    }
    private void registerRegisterIDBroadReceiver() {
        BroadcastReceiver toolBroadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String reg_id =intent.getStringExtra("reg_id");
                trace("registerRegisterIDBroadReceiver(), reg_id="+reg_id);
                if(reg_id!="") {
                    new send_register_id_to_server().execute(reg_id);
                }
            }
        };
        registerReceiver(toolBroadReceiver, new IntentFilter(this.getString(R.string.return_register_id)));
    }

    @Override
    public void onBackPressed() {
        trace("onBackPressed");
        DialogFragment newFragment = new ExitAlertDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onTabHabit() {
        trace("onTabHabit");
        new QueryHabituresTask().execute();
    }

    @Override
    public void onTabPoke() {
        trace("onTabPoke");
        //startActivity(new Intent(this, PokeActivity.class));
        new QueryGroupsTask().execute();
    }

    @Override
    public void onTabDeclare() {
        trace("onTabDeclare");
        startActivity(new Intent(this, DeclareActivity.class));
    }

    @Override
    public void onTabFriend() {
        trace("onTabFriend");
        new QueryFriendsTask().execute();
    }

    @Override
    public void onTabMore() {
        trace("onTabMore");
        // TODO
    }

    @Override
    public void onExit() {
        finish();
    }

    @Override
    public void onClickGroupSingleItem(int pid) {
        trace("onClickGroupSingleItem pid = " + pid);
        // TODO: QueryPokePageTask
        new QueryPokePageTask().execute(pid, QueryPokePageTask.POKE_NOT_FOUNDER);
        //PokeActivity.startActivity(this, url, "123", "456", pid, 1, 1, 1);
    }

    @Override
    public void onClickHabitSingleItem(int pid) {
        trace("onClickHabitSingleItem pid = " + pid);
        new QueryPokePageTask().execute(pid, QueryPokePageTask.POKE_IS_FOUNDER);
        //PokeActivity.startActivity(this, url, "123", "456", pid, 1, 1, 1);
    }

    @Override
    public void onClickHabitCamera(int pid) {
        trace("onClickHabitCamera");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private class QueryPokePageTask extends AsyncTask<Integer, Void, PokeData> {
        private ProgressDialog progress;
        public static final int POKE_IS_FOUNDER = 1;
        public static final int POKE_NOT_FOUNDER = 0;

        private String url = null;
        private String swear = null;
        private String punishment = null;
        private int pid = -1;
        private int frequency = -1;
        private int doItTime = -1;
        private int goal = -1;
        private int to_id = -1;
        private int remain = -1;
        private boolean isFounder = false;

        @Override
        protected void onPreExecute() {
            trace("QueryPokePageTask onPreExecute");

            try {
                progress = ProgressDialog.show(HomeActivity.this,
                        HomeActivity.this.getString(R.string.progress_title),
                        "載入中...");
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected PokeData doInBackground(Integer... params) {
            trace("QueryPokePageTask doInBackground");
            this.pid = Integer.valueOf(params[0]);
            if (params[1] == POKE_IS_FOUNDER)
                this.isFounder = true;
            else
                this.isFounder = false;
            return mHabitureModule.queryPokeData(pid);
        }


        @Override
        protected void onPostExecute(PokeData pokeData) {
            trace("QueryPokePageTask onPostExecute");

            try {
                progress.dismiss();
                String test_url = "http://140.124.144.121/Habiture/profile/1896858_878006565605886_1570385858202168833_n.jpg";
                PokeActivity.startActivity(HomeActivity.this, isFounder, test_url, "123", "123", 154, 1, 1, 1, 1, 1);

//                if (pokeData != null) {
//                    // TODO: startPokeActivity
//                    url = pokeData.getFounderList().get(0).getUrl();
//                    swear = pokeData.getSwear();
//                    punishment = pokeData.getPunishment();
//                    doItTime = pokeData.getDoItime();
//                    frequency = pokeData.getFrequency();
//                    goal = pokeData.getGoal();
//                    remain = pokeData.getFounderList().get(0).getRemainFrequency();
//
//                    if (url == null || swear == null || punishment == null || remain == -1 ||
//                            doItTime == -1 || frequency == -1 || goal == -1 || to_id == -1) {
//                        Toast.makeText(HomeActivity.this, "載入失敗", Toast.LENGTH_SHORT).show();
//                        return ;
//                    }
//                    String test_url = "http://140.124.144.121/Habiture/profile/1896858_878006565605886_1570385858202168833_n.jpg";
//                    PokeActivity.startActivity(HomeActivity.this, isFounder, test_url, "123", "123", 154, 1, 1, 1, 1);
////                    PokeActivity.startActivity(HomeActivity.this, isFounder, url, swear, punishment, pid,
////                            to_id, frequency, doItTime, goal);
//                } else {
//                    Toast.makeText(HomeActivity.this, "載入失敗", Toast.LENGTH_SHORT).show();
//                }


            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }


    }

    private class QueryGroupsTask extends AsyncTask<Void, Void, List<Group>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            trace("onDeclarePreExecute");

            try {
                progress = ProgressDialog.show(HomeActivity.this,
                        HomeActivity.this.getString(R.string.progress_title),
                        HomeActivity.this.getString(R.string.searching_groups));
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected void onPostExecute(List<Group> groups) {
            trace("onDeclarePostExecute");
            try {
                progress.dismiss();

                if(groups == null || groups.size() == 0) {
                    Toast.makeText(
                            HomeActivity.this,
                            R.string.no_group,
                            Toast.LENGTH_SHORT).show();
                    return ;
                }

                getFragmentManager().beginTransaction()
                        .replace(R.id.middleContainer, GroupFragment.newInstance(groups))
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<Group> doInBackground(Void... params) {
            return mHabitureModule.queryGroups();
        }
    }

    private class QueryFriendsTask extends AsyncTask<Void, Void, List<Friend>> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("onDeclarePreExecute");

            try {
                progress = ProgressDialog.show(HomeActivity.this,
                        HomeActivity.this.getString(R.string.progress_title),
                        HomeActivity.this.getString(R.string.searching_friends));
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            trace("onDeclarePostExecute");
            try {
                progress.dismiss();

                if (friends == null || friends.size() == 0) {
                    Toast.makeText(
                            HomeActivity.this,
                            R.string.no_friend,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                getFragmentManager().beginTransaction()
                        .replace(R.id.middleContainer, FriendFragment.newInstance(friends))
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<Friend> doInBackground(Void... params) {
            return mHabitureModule.queryFriends();
        }
    }

    private class QueryHabituresTask extends AsyncTask<Void, Void, List<Habiture>> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("onDeclarePreExecute");

            try {
                progress = ProgressDialog.show(HomeActivity.this,
                        HomeActivity.this.getString(R.string.progress_title),
                        HomeActivity.this.getString(R.string.searching_habiture));
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected void onPostExecute(List<Habiture> habitures) {
            trace("onDeclarePostExecute");
            try {
                progress.dismiss();

                if (habitures == null || habitures.size() == 0) {
                    Toast.makeText(
                            HomeActivity.this,
                            R.string.no_habiture,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                getFragmentManager().beginTransaction()
                        .replace(R.id.middleContainer, HabitListFragment.newInstance(habitures))
                        .commitAllowingStateLoss();

            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<Habiture> doInBackground(Void... params) {
            return mHabitureModule.queryHabitures();
        }
    }

    private class send_register_id_to_server extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("onPreExecute");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            trace("doInBackground");

            boolean is_registered_sent = false;
            try {
                String reg_id = params[0];
                is_registered_sent =mHabitureModule.sendRegisterIdToServer(reg_id);
            } catch (Throwable e) {
                //ExceptionAlertDialog.showException(getFragmentManager(), e);
                Toast.makeText(
                        HomeActivity.this,
                        "GCM register failed",
                        Toast.LENGTH_SHORT).show();
            }
            return is_registered_sent;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("onPostExecute");

            try {
                Toast.makeText(
                        HomeActivity.this,
                        success ? "GCM register done" : "GCM register failed",
                        Toast.LENGTH_SHORT).show();

            } catch(Throwable e) {
                //ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }
    }

    private class UploadProofTask extends AsyncTask<Integer, Void, Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            trace("UploadProofTask onPreExecute");
            try {
                progress = ProgressDialog.show(HomeActivity.this,
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
                    HomeActivity.this,
                    success ? "上傳成功" : "上傳失敗",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
