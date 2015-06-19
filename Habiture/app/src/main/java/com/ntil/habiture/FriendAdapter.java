package com.ntil.habiture;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.habiture.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends BaseAdapter{
    private List<Item> items;
    private LayoutInflater inflater;
    private static final boolean DEBUG = true;

    private void trace(String message) {
        if(DEBUG)
            Log.d("GroupAdapter", message);
    }


    public FriendAdapter(Context context, List<Friend> friends){
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>(friends.size());
        for(Friend friend : friends){
            Item item = new Item();
            item.friend = friend;
            items.add(item);
        }
    }

    public void release() {
        for(Item item: items) {
            if(item.photo != null) item.photo.recycle();
        }
    }

    public class Item {
        Friend friend;
        Bitmap photo = null;
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
            holder.ivFriendIcon = (ImageView) convertView.findViewById(R.id.ivFriendIcon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvFriendName);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = (Item) getItem(position);
        // TODO 設定群組圖案
        holder.tvName.setText(item.friend.getName());

        setPhoto(holder, item);

        return convertView;
    }

    private void setPhoto(ViewHolder holder, Item item) {
        if(item.photo != null) {
            holder.ivFriendIcon.setImageBitmap(item.photo);
        } else {
            holder.ivFriendIcon.setImageResource(R.mipmap.default_icon);
        }
    }

    public void setFriendPhoto(byte[] photo, int position) {
        Item item = (Item) getItem(position);
        trace("setFriendPhoto, position = " + position);
        item.photo = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView ivFriendIcon;
        TextView tvName;
    }
}
