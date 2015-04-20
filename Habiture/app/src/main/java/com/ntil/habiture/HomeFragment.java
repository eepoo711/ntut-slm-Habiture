package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private static final boolean DEBUG = false;

    private static final String ARGS_NAME = "name";

    private TextView textView;

    private Listener mListener;

    private void trace(String message) {
        if(DEBUG)
            Log.d("HomeFragment", message);
    }

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String name) {
        HomeFragment fragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_NAME, name);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = (Listener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        trace("onActivityCreated");

        Bundle args = this.getArguments();
        String name = args.getString(ARGS_NAME);

        textView = (TextView) getActivity().findViewById(R.id.name);
        textView.setText(name);

        getActivity().findViewById(R.id.btnDeclare).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mListener.onShowDeclarationClicked();
            }
        });
    }

    public interface Listener {
        public void onShowDeclarationClicked();
    }
}
