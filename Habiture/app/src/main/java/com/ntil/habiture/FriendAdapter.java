package com.ntil.habiture;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.habiture.Friend;
import com.habiture.Group;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends BaseAdapter{
    private List<Item> items;
    private LayoutInflater inflater;

    public FriendAdapter(Context context, List<Friend> friends){
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>(friends.size());
        for(Friend friend : friends){
            Item item = new Item();
            item.friend = friend;
            items.add(item);
        }
    }

    public class Item {
        Friend friend;

        public Friend getFriend() {
            return friend;
        }
    }
    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int position)  {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)  {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.singleitem_friend, parent, false);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivFriendIcon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvFriendName);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = (Item) getItem(position);
        // TODO 設定群組圖案
//        holder.ivIcon.setImageBitmap(item.friend.getImage());
        // 設定群組名稱
        holder.tvName.setText(item.friend.getName());

        return convertView;
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
    }
}
