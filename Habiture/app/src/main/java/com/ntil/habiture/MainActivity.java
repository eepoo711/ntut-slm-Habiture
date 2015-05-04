package com.ntil.habiture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.habiture.Group;
import com.habiture.HabitureModule;
import com.habiture.NetworkChannel;

import java.util.List;

import utils.exception.ExceptionAlertDialog;


public class MainActivity extends ActionBarActivity implements LoginFragment.Listener,
        HomeFragment.Listener {

    private static final boolean DEBUG = false;

    private HabitureModule mHabitureModule;



    private void trace(String message) {
        if(DEBUG)
            Log.d("MainActivity", message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trace("onCreate");

        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);
            mHabitureModule = MainApplication.getInstance().getHabitureModel();

            if(savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new LoginFragment())
                        .commit();
            }
        } catch(Throwable e) {
            ExceptionAlertDialog.showException(getFragmentManager(), e);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        trace("onCreateOptionsMenu");

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
        trace("onOptionsItemSelected");

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
    public void onLoginClicked(String account, String password) {
        trace("onLoginClicked");
        new LoginTask().execute(account, password);
    }

    @Override
    public void onShowDeclarationClicked() {
        trace("onShowDeclarationClicked");

        startActivity(new Intent(MainActivity.this, DeclareActivity.class));
    }

    @Override
    public void onShowGroupClicked() {
        trace("onShowGroupClicked");
        new QueryGroupsTask().execute();
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("onPreExecute");

            try {
                progress = ProgressDialog.show(MainActivity.this,
                        MainActivity.this.getString(R.string.progress_title),
                        MainActivity.this.getString(R.string.logining));
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }

        @Override
        protected Boolean doInBackground(String... params) {
            trace("doInBackground");

            boolean logined = false;
            try {
                String account = params[0];
                String password = params[1];
                logined = mHabitureModule.login(account, password);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

            return logined;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("onPostExecute");

            try {
                progress.dismiss();


                String textLoginSuccessful = MainActivity.this.getString(R.string.login_successfully);
                String textLoginFailed = MainActivity.this.getString(R.string.login_failed);
                Toast.makeText(
                        MainActivity.this,
                        success ? textLoginSuccessful : textLoginFailed,
                        Toast.LENGTH_SHORT).show();

                if(success){
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, HomeFragment.newInstance(mHabitureModule.getAccount()))
                            .commit();
                }


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
                progress = ProgressDialog.show(MainActivity.this,
                        MainActivity.this.getString(R.string.progress_title),
                        MainActivity.this.getString(R.string.searching_friends));
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
                            MainActivity.this,
                            R.string.no_group,
                            Toast.LENGTH_SHORT).show();
                    return ;
                }

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, GroupFragment.newInstance(groups))
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
}
