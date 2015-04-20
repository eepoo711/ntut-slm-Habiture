package com.ntil.habiture;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.habiture.Friend;
import com.habiture.HabitureModule;

import java.util.List;

import utils.exception.ExceptionAlertDialog;


public class DeclareActivity extends ActionBarActivity implements DeclareFragment.Listener, InviteFriendFragment.Listener {

    private static final boolean DEBUG = true;

    private HabitureModule mHabitureModule;



    private void trace(String message) {
        if(DEBUG)
            Log.d("DeclareActivity", message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trace("onDeclareCreate");

        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);
            mHabitureModule = MainApplication.getInstance().getHabitureModel();

            if(savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new DeclareFragment())
                        .commit();
            }
        } catch(Throwable e) {
            ExceptionAlertDialog.showException(getFragmentManager(), e);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        trace("onDeclareCreateOptionsMenu");

        try {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
        } catch(Throwable e) {
            ExceptionAlertDialog.showException(getFragmentManager(), e);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        trace("onDeclareOptionsItemSelected");

        try {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
        } catch(Throwable e) {
            ExceptionAlertDialog.showException(getFragmentManager(), e);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeclareClicked(String peroid, String frequency, String account, String password) {
        trace("onDeclareClicked");
        new DeclareTask().execute(peroid, frequency, account, password);
    }

    @Override
    public void onCancelClicked() {
        trace("onCancelClicked");
        finish();
    }

    @Override
    public void onInviteFriendsClicked(List<Long> friendsId) {
        trace("onInviteFriendsClicked friendsId = " + friendsId);
        //TODO
        finish();
    }

    private class DeclareTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("onDeclarePreExecute");

            try {
                progress = ProgressDialog.show(DeclareActivity.this,
                        DeclareActivity.this.getString(R.string.progress_title),
                        DeclareActivity.this.getString(R.string.declaring));
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }

        @Override
        protected Boolean doInBackground(String... params) {
            trace("doDeclareInBackground");
            boolean declared = false;
            try {
                String peroid = params[0];
                String frequency = params[1];
                String account = params[2];
                String password = params[3];
                declared = mHabitureModule.declare(peroid, frequency, account, password);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

            return declared;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("onDeclarePostExecute");
            try {
                progress.dismiss();


                String textDeclareSuccessful = DeclareActivity.this.getString(R.string.declare_successfully);
                String textDeclareFailed = DeclareActivity.this.getString(R.string.declare_failed);
                Toast.makeText(
                        DeclareActivity.this,
                        success ? textDeclareSuccessful : textDeclareFailed,
                        Toast.LENGTH_SHORT).show();

                if(success) {

                    new QueryFriendsTask().execute();
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
                progress = ProgressDialog.show(DeclareActivity.this,
                        DeclareActivity.this.getString(R.string.progress_title),
                        DeclareActivity.this.getString(R.string.searching_friends));
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            trace("onDeclarePostExecute");
            try {
                progress.dismiss();

                if(friends == null || friends.size() == 0) {
                    Toast.makeText(
                            DeclareActivity.this,
                            R.string.no_friend,
                            Toast.LENGTH_SHORT).show();
                    return ;
                }

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, InviteFriendFragment.newInstance(friends))
                        .addToBackStack(null)
                        .commit();

            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<Friend> doInBackground(Void... params) {
            return mHabitureModule.queryFriends();
        }
    }
}
