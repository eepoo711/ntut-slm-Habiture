package com.ntil.habiture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.habiture.Group;
import com.habiture.HabitureModule;

import java.util.List;

import utils.exception.ExceptionAlertDialog;

/**
 * Created by GawinHsu on 5/6/15.
 */
public class HomeActivity extends Activity implements HomeMiddleFragment.Listener, HomeBottomFragment.Listener{
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
                    .add(R.id.topContainer, HomeTopFragment.newInstance(name))
                    .add(R.id.middleContainer, new HomeMiddleFragment())
                    .add(R.id.bottomContainer, new HomeBottomFragment())
                    .commit();
        }

    }

    @Override
    public void onShowGroupClicked() {
        trace("onShowGroupClicked");
        new QueryGroupsTask().execute();
    }

    @Override
    public void onShowPaintClicked() {
        trace("onShowPaintClicked");
        PokeView pv = new PokeView(this, null);
        setContentView(pv);

    }

    @Override
    public void onTabHabit() {
        trace("onTabHabit");
        getFragmentManager().beginTransaction()
                .replace(R.id.middleContainer, new HabitListFragment())
                .commit();
    }

    @Override
    public void onTabPoke() {
        trace("onTabPoke");
        // TODO
    }

    @Override
    public void onTabDeclare() {
        trace("onTabDeclare");
        startActivity(new Intent(this, DeclareActivity.class));
    }

    @Override
    public void onTabFriend() {
        trace("onTabFriend");
        // TODO
    }

    @Override
    public void onTabMore() {
        trace("onTabMore");
        // TODO
    }

    private class QueryGroupsTask extends AsyncTask<Void, Void, List<Group>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            trace("onDeclarePreExecute");

            try {
                progress = ProgressDialog.show(HomeActivity.this,
                        HomeActivity.this.getString(R.string.progress_title),
                        HomeActivity.this.getString(R.string.searching_friends));
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
}
