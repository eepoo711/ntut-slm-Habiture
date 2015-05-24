package com.ntil.habiture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
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

import utils.BitmapHelper;
import utils.exception.ExceptionAlertDialog;
import utils.exception.UnhandledException;


public class DeclareActivity extends Activity implements DeclareFragment.Listener, InviteFriendFragment.Listener {

    private static final boolean DEBUG = false;

    private HabitureModule mHabitureModule;
    private int mPeriod = -1;
    private int mFrequency = -1;
    private String mDeclaration = null;
    private String mCost = null;

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

    public void onDeclareClicked(String declaration, String punishment, String frequency, String do_it_time, String goal) {
        trace("onDeclareClicked");
        new DeclareTask().execute(declaration, punishment, frequency, do_it_time, goal);
    }

    @Override
    public void onCancelClicked() {
        trace("onCancelClicked");
        finish();
    }

    @Override
    public void onInviteFriendsClicked(List<Friend> friends) {
        trace("onInviteFriendsClicked");
        //TODO
        new PostDeclarationTask().execute(friends);

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
            boolean declared = true;
            try {
                String declaration, punishment, frequency, do_it_time, goal;
                declaration =params[0];
                punishment =params[1];
                frequency = params[2];
                do_it_time =params[3];
                goal =      params[4];
                mHabitureModule.postDeclaration(frequency,declaration,punishment,goal,do_it_time);

            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
                trace(e.toString());
            }
            return declared;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("onDeclarePostExecute");
            try {
                progress.dismiss();

                if(success){
                    //new QueryFriendsTask().execute();
                    Toast.makeText(DeclareActivity.this,getApplicationContext().getString(R.string.declare_successfully),Toast.LENGTH_SHORT).show();
                    finish();
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
                        .commitAllowingStateLoss();

            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<Friend> doInBackground(Void... params) {
            return mHabitureModule.queryFriends();
        }
    }

    private class PostDeclarationTask extends AsyncTask<List<Friend>, Void, Boolean> {

        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(List<Friend>... params) {
            trace("doDeclareInBackground");
            boolean declared = false;
//            boolean declared = true;
            try {
                //TODO: global ?
                //declared = mHabitureModule.postDeclaration(mHabitureModule.getAccount(), mHabitureModule.getPassword(),
                        //mPeriod, mFrequency, mDeclaration, params[0]);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

            return declared;
        }

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

                finish();

            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }
    }
}
