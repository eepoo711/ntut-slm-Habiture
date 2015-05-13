package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import utils.exception.UnhandledException;


public class DeclareFragment extends Fragment {

    private static final boolean DEBUG = false;

    private Listener mListener;

    public DeclareFragment() {
        // Required empty public constructor
        trace("DeclareFragment");
    }

    private void trace(String message) {
        if(DEBUG)
            Log.d("DeclareFragment", message);
    }

    @Override
    public void onAttach(Activity activity) {
        trace("onDeclaretionAttach");
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " Declaretion must implement Listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        trace("onCreate");
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        trace("onCreateDeclaretionView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_declaration, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        trace("onDeclaretionActivityCreated");
        super.onActivityCreated(savedInstanceState);

//        final TextView tvAppVersion = (TextView) getActivity().findViewById(R.id.tvAppVersion);
//        tvAppVersion.setText(getAppVersion());

        final EditText etDeclaration = (EditText) getActivity().findViewById(R.id.etDeclaration);
        final EditText etCost = (EditText) getActivity().findViewById(R.id.etCost);
        final EditText etGoal = (EditText) getActivity().findViewById(R.id.etGoal);
        final Spinner spDoItTime = (Spinner) getActivity().findViewById(R.id.spDoItTime);
        final Spinner spFrequency = (Spinner) getActivity().findViewById(R.id.spFrequency);

        // initial spinner items of frequency
        String[] frequency = getActivity().getResources().getStringArray(R.array.declare_weekly_frequency);
        ArrayAdapter frequencyList = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, frequency);
        spFrequency.setAdapter(frequencyList);

        // initial spinner items of do it time
        String[] do_it_time = getActivity().getResources().getStringArray(R.array.declare_do_it_time);
        ArrayAdapter do_it_time_list = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, do_it_time);
        spDoItTime.setAdapter(do_it_time_list);

//public void onDeclareClicked(String declaration, String punishment, String frequency, String do_it_time, String goal) {
        getActivity().findViewById(R.id.btnDeclare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnDeclare");
                mListener.onDeclareClicked(
                        etDeclaration.getText().toString(),
                        etCost.getText().toString(),
                        spFrequency.getSelectedItem().toString(),
                        spDoItTime.getSelectedItem().toString(),
                        etGoal.getText().toString());
            }
        });

        getActivity().findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClicked();
            }
        });
    }



    @Override
    public void onDetach() {
        trace("onDeclaretionDetach");
        super.onDetach();
        mListener = null;
    }

    public interface Listener {
        public void onDeclareClicked(String declaration, String punishment, String frequency, String do_it_time, String goal);
        public void onCancelClicked();
    }

    private String getAppVersion() {
        trace("getAppVersion");
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new UnhandledException(e);
        }
    }

}
