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
 * Created by GawinHsu on 5/6/15.
 */
public class HomeBottomFragment extends Fragment {

    public interface Listener {
        public void onTabHabit();
        public void onTabPoke();
        public void onTabDeclare();
        public void onTabFriend();
        public void onTabMore();
    }

    
    private static final boolean DEBUG = false;
    private void trace(String message) {
        if(DEBUG)
            Log.d("HomeBottomFragment", message);
    }

    private Listener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_home_bottom, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Listener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.btnHabitList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnHabitList");
                listener.onTabHabit();
            }
        });
        getActivity().findViewById(R.id.btnPokeList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnPokeList");
                listener.onTabPoke();
            }
        });
        getActivity().findViewById(R.id.btnDeclare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnDeclare");
                listener.onTabDeclare();
            }
        });
        getActivity().findViewById(R.id.btnFriendList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnFriendList");
                listener.onTabFriend();
            }
        });
        getActivity().findViewById(R.id.btnMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnMore");
                listener.onTabMore();
            }
        });
    }
}
