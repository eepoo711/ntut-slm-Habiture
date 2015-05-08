package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeMiddleFragment extends Fragment {

    private static final boolean DEBUG = false;

    private TextView textView;

    private Listener mListener;

    private void trace(String message) {
        if(DEBUG)
            Log.d("HomeMiddleFragment", message);
    }

    public HomeMiddleFragment() {

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = (Listener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_home_middle, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        trace("onActivityCreated");

        getActivity().findViewById(R.id.btnGroup).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mListener.onShowGroupClicked();
            }
        });

    }

    public interface Listener {
        public void onShowGroupClicked();
    }
}
