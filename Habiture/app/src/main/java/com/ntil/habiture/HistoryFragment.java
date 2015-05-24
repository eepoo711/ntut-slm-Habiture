package com.ntil.habiture;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.habiture.GroupHistory;

import java.util.List;

/**
 * Created by GawinHsu on 5/12/15.
 */
public class HistoryFragment extends Fragment {
    private static final boolean DEBUG = false;
    private ListView lvHistory;
    private static List<GroupHistory> groupHistories;
    private HistoryAdapter historyAdapter;

    private void trace(String message) {
        if(DEBUG)
            Log.d("HistoryFragment", message);
    }

    public static HistoryFragment newInstance(List<GroupHistory> groupHistories){
        HistoryFragment fragment = new HistoryFragment();
        fragment.groupHistories = groupHistories;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trace("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        lvHistory = (ListView) getActivity().findViewById(R.id.lvHistory);

        historyAdapter = new HistoryAdapter(getActivity(), groupHistories);
        lvHistory.setAdapter(historyAdapter);
    }
}

