package com.ntil.habiture;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.habiture.Group;

import java.util.List;

public class GroupFragment extends Fragment {

    private static final boolean DEBUG = false;

    private ListView item_list;

    private GroupAdapter groupAdapter;

    private static List<Group> groups;

    private void trace(String message) {
        if(DEBUG)
            Log.d("GroupFragment", message);
    }

    public GroupFragment() {}

    public static GroupFragment newInstance(List<Group> groups){
        GroupFragment fragment = new GroupFragment();

        GroupFragment.groups = groups;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        trace("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        item_list = (ListView) getActivity().findViewById(R.id.lvGroupList);

        groupAdapter = new GroupAdapter(getActivity(), groups);
        item_list.setAdapter(groupAdapter);

    }
}
