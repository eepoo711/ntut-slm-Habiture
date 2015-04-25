package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.habiture.Friend;

import java.util.ArrayList;
import java.util.List;


public class InviteFriendFragment extends Fragment {
    private static final boolean DEBUG = true;

    private ListView item_list;

    private InviteFriendAdapter invitefriendAdapter;

    private Button btnCancel, btnConfirm;

    private static List<Friend> friends;

    private Listener mListener;

    private void trace(String s)
    {
        if(DEBUG)
            Log.d("InviteFriendFragment", s);
    }

    public InviteFriendFragment()
    {

    }

    public static InviteFriendFragment newInstance(List<Friend> friends) {
        InviteFriendFragment fragment = new InviteFriendFragment();

        InviteFriendFragment.friends = friends;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_invite_friend, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        trace("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        initializeView();


        invitefriendAdapter = new InviteFriendAdapter(getActivity(), friends);
        item_list.setAdapter(invitefriendAdapter);

        // 註冊選單項目點擊監聽物件
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view,
                                     int position, long id) {
                 // 讀取選擇的物件
                 InviteFriendAdapter.Item item = (InviteFriendAdapter.Item) invitefriendAdapter.getItem(position);
                 // 處理是否顯示已選擇項目
                 // 重新設定記事項目
                 invitefriendAdapter.setSelected(position, !item.isSelected);

             }
        });

    }

    private void initializeView() {
        item_list = (ListView) getActivity().findViewById(R.id.lvFriendList);

        getActivity().findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("btnConfirm onClick");
                getFragmentManager().popBackStack();
            }
        });


        getActivity().findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("btnConfirm onClick");
                int index = invitefriendAdapter.getCount() - 1;
                List<Friend> selectedFriends = new ArrayList<>();

                while (index > -1) {
                    InviteFriendAdapter.Item item = (InviteFriendAdapter.Item) invitefriendAdapter.getItem(index);

                    if (item.isSelected()) {
                        selectedFriends.add(item.getFriend());
                        trace(item.getFriend().getName());
                    }
                    index--;
                }
                mListener.onInviteFriendsClicked(selectedFriends);
            }
        });

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (Listener) activity;
    }

    public interface Listener {
        public void onInviteFriendsClicked(List<Friend> friends);
    }
}
