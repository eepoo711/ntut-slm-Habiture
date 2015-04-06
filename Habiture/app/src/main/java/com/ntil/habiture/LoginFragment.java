package com.ntil.habiture;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import utils.exception.UnhandledException;


public class LoginFragment extends Fragment {

    private static final boolean DEBUG = true;

    private Listener mListener;

    public LoginFragment() {
        // Required empty public constructor
        trace("LoginFragment");
    }

    private void trace(String message) {
        if(DEBUG)
            Log.d("LoginFragment", message);
    }

    @Override
    public void onAttach(Activity activity) {
        trace("onAttach");
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        trace("onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        trace("onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        trace("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        final TextView tvAppVersion = (TextView) getActivity().findViewById(R.id.tvAppVersion);
        tvAppVersion.setText(getAppVersion());

        final EditText etAccount = (EditText) getActivity().findViewById(R.id.etAccount);
        final EditText etPassword = (EditText) getActivity().findViewById(R.id.etPassword);

        getActivity().findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick");
                mListener.onLoginClicked(
                        etAccount.getText().toString(),
                        etPassword.getText().toString());
            }
        });
    }



    @Override
    public void onDetach() {
        trace("onDetach");
        super.onDetach();
        mListener = null;
    }

    public interface Listener {
        public void onLoginClicked(String account, String password);
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
