package com.ntil.habiture;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gcm.client.receiver.DemoActivity;
import com.habiture.Friend;
import com.habiture.Group;
import com.habiture.Habiture;
import com.habiture.HabitureModule;

import java.util.List;

import utils.exception.ExceptionAlertDialog;

/**
 * Created by GawinHsu on 5/6/15.
 */
public class HomeActivity extends Activity implements HomeMiddleFragment.Listener, HomeBottomFragment.Listener,
        ExitAlertDialog.Listener{
    private HabitureModule mHabitureModule;

    private static final boolean DEBUG = false;
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

        mHabitureModule.registerGCM();


    }

    @Override
    public void onBackPressed() {
        trace("onBackPressed");
        DialogFragment newFragment = new ExitAlertDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onShowGroupClicked() {
        trace("onShowGroupClicked");
        new QueryGroupsTask().execute();
    }

    @Override
    public void onTabHabit() {
        trace("onTabHabit");
        new QueryHabituresTask().execute();
    }

    @Override
    public void onTabPoke() {
        trace("onTabPoke");
        startActivity(new Intent(this, PokeActivity.class));
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
                        .commit();

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
                        .commit();

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
                        .commit();

            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<Habiture> doInBackground(Void... params) {
            return mHabitureModule.queryHabitures();
        }
    }

}
