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
    private Listener listener;
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
        listener = (Listener)context;
    }

    public class Item {
        public static final int FRIEND_ITEM_READY = 0;
        public static final int FRIEND_ITEM_DOWNING = 1;
        public static final int FRIEND_ITEM_SETTING = 2;
        Friend friend;
        Bitmap photo = null;
        int state = FRIEND_ITEM_READY;
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

        // download photo
        switch (item.state) {
            case Item.FRIEND_ITEM_READY:
                trace("FRIEND_ITEM_READY, position = " + position);
                listener.onDownloadFriendPhoto(this, item.getFriend().getUrl(), position);
                item.state = Item.FRIEND_ITEM_DOWNING;
                break;
            case Item.FRIEND_ITEM_DOWNING:
                // do Nothing
                break;
            case Item.FRIEND_ITEM_SETTING:
                trace("FRIEND_ITEM_SETTING, position = " + position);
                holder.ivFriendIcon.setImageBitmap(item.photo);
                break;
        }

        return convertView;
    }

    public void setFriendPhoto(byte[] photo, int position) {
        Item item = (Item) getItem(position);
        if (item.state == Item.FRIEND_ITEM_DOWNING) {
            trace("setFriendPhoto, position = " + position);
            item.photo = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            item.state = Item.FRIEND_ITEM_SETTING;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView ivFriendIcon;
        TextView tvName;
    }

    public interface Listener {
        void onDownloadFriendPhoto(FriendAdapter friendAdapter, String url, int position);
    }
}
