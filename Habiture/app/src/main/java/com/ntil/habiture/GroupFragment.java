package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.habiture.Group;

import java.util.List;

public class GroupFragment extends Fragment {

    private static final boolean DEBUG = true;

    private ListView item_list;

    private GroupAdapter groupAdapter;

    private Listener listener;

    private static List<Group> groups;

    public interface Listener {
        public void onClickGroupSingleItem(int pid);
    }

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Listener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        trace("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        item_list = (ListView) getActivity().findViewById(R.id.lvGroupList);

        groupAdapter = new GroupAdapter(getActivity(), groups);
        item_list.setAdapter(groupAdapter);
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pid = ((GroupAdapter.Item)groupAdapter.getItem(position)).getGroup().getId();
                trace("pid = "+pid);
                listener.onClickGroupSingleItem(pid);
            }
        });

    }
}
