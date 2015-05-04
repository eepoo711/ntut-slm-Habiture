package com.ntil.habiture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.habiture.Friend;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class InviteFriendAdapter extends BaseAdapter {
    private List<Item> items;
    private LayoutInflater inflater;

    public InviteFriendAdapter(Context context, List<Friend> friends) {
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>(friends.size());
        for (Friend friend : friends) {
            Item item = new Item();
            item.friend = friend;
            items.add(item);
        }
    }

    public class Item {
        Friend friend;
        boolean isSelected = false;

        public Friend getFriend() {
            return friend;
        }

        public boolean isSelected() {
            return isSelected;
        }

    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.singleitem_invite_friend, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvFriendsName);
            holder.ivSelected = (ImageView) convertView.findViewById(R.id.ivSelected);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = (Item) getItem(position);
        // 設定姓名
        holder.tvName.setText(item.friend.getName());

        // 設定是否已選擇
        holder.ivSelected.setVisibility(item.isSelected() ? View.VISIBLE : View.INVISIBLE);

        return convertView;
    }

    public void setSelected(int position, boolean selected) {
        Item item = (Item) getItem(position);
        item.isSelected = selected;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvName;
        ImageView ivSelected;
    }
}
