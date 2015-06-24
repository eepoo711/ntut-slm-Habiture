package com.ntil.habiture;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
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
import com.habiture.RegisterAlarmByHabituresModel;
import com.ntil.habiture.task.DownloadPhotoTask;

import java.util.List;

import utils.exception.ExceptionAlertDialog;
import utils.exception.UnhandledException;

public class HomeActivity extends Activity implements HomeBottomFragment.Listener,
        ExitAlertDialog.Listener, GroupFragment.Listener, HabitListFragment.Listener,
        HabitListAdapter.Listener, PassAlertDialog.Listener, GroupAdapter.Listener {
    private HabitureModule mHabitureModule;
    private Bitmap mBitmapCaputred;
    private HabitListFragment habitListFragment = null;
    private final static int DECLARE_ACTIVITY_RESULT = 101;
    private final static int POKE_ACTIVITY_RESULT = 102;

    private int g_pid = 0;
    private int g_position = 0;

    private static final boolean DEBUG = true;
    private void trace(String message) {
        if(DEBUG)
            Log.d("HomeActivity", message);
    }

    private DownloadPhotoTask friendPhotoTask;
    private FriendAdapter friendAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        trace("onCreate");

        mHabitureModule = MainApplication.getInstance().getHabitureModel();

        if(savedInstanceState == null) {

            String name = mHabitureModule.getName();
            getFragmentManager().beginTransaction()
                    .add(R.id.topContainer, HomeTopFragment.newInstance(name, mHabitureModule.getHeader()))
                    .add(R.id.middleContainer, new HomeMiddleFragment())
                    .add(R.id.bottomContainer, new HomeBottomFragment())
                    .commitAllowingStateLoss();
            new QueryHabituresTask().execute();
        }

        registerRegisterIDBroadReceiver();
        mHabitureModule.registerGCM();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        trace("onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);

        switch (requestCode) {
            case DECLARE_ACTIVITY_RESULT:
                if (resultCode == RESULT_OK) {
                    new QueryHabituresTask().execute();
                }
                break;
            case POKE_ACTIVITY_RESULT:
                break;
        }
    }


    private BroadcastReceiver toolBroadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String reg_id =intent.getStringExtra("reg_id");
            trace("registerRegisterIDBroadReceiver(), reg_id="+reg_id);
            if(reg_id!="") {
                new send_register_id_to_server().execute(reg_id);
            }
        }
    };

    private void registerRegisterIDBroadReceiver() {
        registerReceiver(toolBroadReceiver, new IntentFilter(this.getString(R.string.return_register_id)));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHabitureModule.stopRegisterGCM();
        this.unregisterReceiver(toolBroadReceiver);

        if(friendPhotoTask != null) friendPhotoTask.cancel(true);
        if(friendAdapter != null) friendAdapter.release();
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
        startActivityForResult(new Intent(this, DeclareActivity.class), DECLARE_ACTIVITY_RESULT);
    }

    @Override
    public void onTabFriend() {
        trace("onTabFriend");
        new QueryFriendsTask().execute();
    }

    @Override
    public void onTabMore() {
        trace("onTabMore");
        // TODO more function
        DialogFragment newFragment = new NoImplementDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onExit() {
        trace("onExit");
        finish();
    }

    @Override
    public void onPass(Habiture habiture, int position) {
        trace("onPass " + ", position = " + position);
        habiture.setNoticeEnable(false);
        new PassTask(habiture, position).execute();
    }

    @Override
    public void onClickGroupSingleItem(int pid) {
        trace("onClickGroupSingleItem pid = " + pid);
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
    public void onClickHabitAddGourpFriend(int pid) {
        trace("onClickHabitAddGourpFriend");
        // TODO
        DialogFragment newFragment = new NoImplementDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClickHabitCamera(int pid, int position) {
        trace("onClickHabitCamera");
        RecordActivity.startActivity(this,pid);
    }



    @Override
    public void onClickHabitPass(Habiture habiture, int position, int passRemain) {
        trace("onClickHabitPass");
        DialogFragment newFragment = PassAlertDialog.newInstance(habiture, position, passRemain);
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onDownloadGroupPhoto(GroupAdapter groupAdapter, String url, int position) {
        new DownloadGroupPhotoTask(groupAdapter, position).execute(url);
    }

    private class DownloadGroupPhotoTask extends AsyncTask<String, Void, byte[]> {
        GroupAdapter groupAdapter;
        int position;

        public DownloadGroupPhotoTask(GroupAdapter groupAdapter, int position) {
            this.groupAdapter = groupAdapter;
            this.position = position;
        }

        @Override
        protected byte[] doInBackground(String... params) {
            trace("DownloadGroupPhotoTask doInBackground");
            String url = params[0];
            byte[] image = null;
            try {
                image = mHabitureModule.downloadPhoto(url);
            } catch (Exception e) {
                new UnhandledException("doInBackground" + e);
            }

            return image;
        }

        @Override
        protected void onPostExecute(byte[] image) {
            if (image != null && !groupAdapter.isEmpty() && !HomeActivity.this.isFinishing()) {
                groupAdapter.setGroupPhoto(image, position);
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

    private class PassTask extends AsyncTask<Integer, Void, Boolean> {
        private final int position;
        private ProgressDialog progress;

        private final Habiture habiture;

        public PassTask(Habiture habiture, int position) {
            this.habiture = habiture;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            trace("PassTask onPreExecute");

            try {
                progress = ProgressDialog.show(HomeActivity.this,
                        HomeActivity.this.getString(R.string.progress_title),
                        "載入中...");
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }


        @Override
        protected Boolean doInBackground(Integer... params) {
            trace("PassTask doInBackground");

            return mHabitureModule.passHabitToday(habiture);
        }
        @Override
        protected void onPostExecute(Boolean isPass) {
            trace("PassTask onPostExecute");
            progress.dismiss();
            if (isPass)
                habitListFragment.setPassDisable(position);
            //else
                //throw new UnhandledException("pass failed" );
        }



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

        private int notice_enable = -1;

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
                if (pokeData != null) {
                    PokeActivity.startActivity(HomeActivity.this, pokeData, isFounder, pid);
                } else {
                    Toast.makeText(HomeActivity.this, "載入失敗", Toast.LENGTH_SHORT).show();
                }


            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
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

                if(friendAdapter != null) friendAdapter.release();
                friendAdapter = new FriendAdapter(HomeActivity.this, friends);


                Fragment newFragment = FriendFragment.newInstance(friends, friendAdapter);
                getFragmentManager().beginTransaction()
                        .replace(R.id.middleContainer, newFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

                downloadFriendPhoto(friends);

            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<Friend> doInBackground(Void... params) {
            return mHabitureModule.queryFriends();
        }
    }

    private void downloadFriendPhoto(List<Friend> friends) {
        String[] photoUrls = new String[friends.size()];
        for(int i = 0; i < photoUrls.length; i++) {
            photoUrls[i] = friends.get(i).getUrl();
        }

        DownloadPhotoTask.Listener listener = new DownloadPhotoTask.Listener() {
            @Override
            public void onDownloadFinished(int index, byte[] photo) {
                friendAdapter.setFriendPhoto(photo, index);
            }
        };

        friendPhotoTask = new DownloadPhotoTask(photoUrls, listener);
        friendPhotoTask.execute();
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
                    getFragmentManager().beginTransaction()
                            .replace(R.id.middleContainer, new HomeMiddleFragment())
                            .commitAllowingStateLoss();
                    return;
                }

                habitListFragment = HabitListFragment.newInstance(habitures);
                RegisterAlarmByHabituresModel.RegisterAlarmByHabitures(habitures);

                getFragmentManager().beginTransaction()
                        .replace(R.id.middleContainer, habitListFragment)
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
        private String register_id =null;

        @Override
        protected void onPreExecute() {
            trace("onPreExecute");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            trace("doInBackground");
            register_id =params[0];

            boolean is_registered_sent = false;
            try {
                is_registered_sent =mHabitureModule.sendRegisterIdToServer(register_id);
            } catch (Throwable e) {
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
                if(success) {
                    Toast.makeText(
                            HomeActivity.this,
                            "GCM register done ",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("gcm register done");
                } else {
                    // wait 2 seconds then try again
                    new AsyncTask<String, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(String... params) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                            }
                            return true;
                        }
                        @Override
                        protected void onPostExecute(Boolean success) {
                            retry_register_gcm(register_id);
                        }
                    }.execute(register_id, null, null);
                }
            } catch(Throwable e) {
                //ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }
    }

    private void retry_register_gcm(String reg_id) {
        trace("retry_register_gcm()"+reg_id);

        Intent broadcastIntent = new Intent(this.getString(R.string.return_register_id));
        broadcastIntent.putExtra("reg_id",reg_id);
        sendBroadcast(broadcastIntent);
    }
}
