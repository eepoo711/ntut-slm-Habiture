package com.ntil.habiture;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GawinHsu on 5/6/15.
 */
public class HomeBottomFragment extends Fragment {

    private static final boolean DEBUG = false;
    private void trace(String message) {
        if(DEBUG)
            Log.d("HomeBottomFragment", message);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_home_bottom, container, false);
    }
}
