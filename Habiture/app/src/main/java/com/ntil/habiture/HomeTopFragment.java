package com.ntil.habiture;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by GawinHsu on 5/6/15.
 */
public class HomeTopFragment extends Fragment {
    private static final String ARGS_NAME = "name";

    private static final boolean DEBUG = false;
    private static void trace(String message) {
        if(DEBUG)
            Log.d("HomeTopFragment", message);
    }

    public static HomeTopFragment newInstance(String name) {
        trace("newInstance");
        HomeTopFragment fragment = new HomeTopFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_home_top, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trace("onActivityCreated");
        Bundle args = this.getArguments();
        String name = args.getString(ARGS_NAME);

        ((TextView)getActivity().findViewById(R.id.tvName)).setText(name);

    }
}
