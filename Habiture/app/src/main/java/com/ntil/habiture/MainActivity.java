package com.ntil.habiture;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.habiture.HabitureModule;
import com.habiture.NetworkChannel;


public class MainActivity extends ActionBarActivity implements LoginFragment.Listener {

    private static final boolean DEBUG = true;

    private HabitureModule mHabitureModule;



    private void trace(String message) {
        if(DEBUG)
            Log.d("MainActivity", message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trace("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHabitureModule = new HabitureModule(new NetworkChannel());

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        trace("onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        trace("onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoginClicked(String account, String password) {
        trace("onLoginClicked");
        new LoginTask().execute(account, password);
    }


    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("onPreExecute");
            progress = ProgressDialog.show(MainActivity.this,
                    MainActivity.this.getString(R.string.progress_title),
                    MainActivity.this.getString(R.string.logining));
        }

        @Override
        protected Boolean doInBackground(String... params) {
            trace("doInBackground");
            String account = params[0];
            String password = params[1];

            return mHabitureModule.login(account, password);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("onPostExecute");
            progress.dismiss();


            String textLoginSuccessful = MainActivity.this.getString(R.string.login_successfully);
            String textLoginFailed = MainActivity.this.getString(R.string.login_failed);
            Toast.makeText(
                    MainActivity.this,
                    success ? textLoginSuccessful : textLoginFailed,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
