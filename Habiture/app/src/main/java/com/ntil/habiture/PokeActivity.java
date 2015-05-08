package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class PokeActivity extends Activity {

    private static final boolean DEBUG = false;
    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeActivity", message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);
        String name = MainApplication.getInstance().getHabitureModel().getAccount();

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.profileContainer, HomeTopFragment.newInstance(name))
                    .add(R.id.pokeContainer, new PokeFragment())
                    .commit();
        }
    }
}
