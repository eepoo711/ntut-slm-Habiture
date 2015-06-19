package com.ntil.habiture;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.habiture.Friend;
import com.habiture.Group;

import java.util.List;

public class FriendFragment extends Fragment {

    private static final boolean DEBUG = false;

    private ListView item_list;



    private static List<Friend> friends;
    private static BaseAdapter adapter;

    private void trace(String message) {
        if(DEBUG)
            Log.d("FriendFragment", message);
    }

    public FriendFragment() {}

    public static FriendFragment newInstance(List<Friend> friends, BaseAdapter adapter){
        FriendFragment fragment = new FriendFragment();

        FriendFragment.friends = friends;
        FriendFragment.adapter = adapter;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        trace("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        item_list = (ListView) getActivity().findViewById(R.id.lvFriendList);
        item_list.setAdapter(adapter);

    }
}
