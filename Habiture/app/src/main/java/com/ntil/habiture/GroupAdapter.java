package com.ntil.habiture;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.habiture.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends BaseAdapter{
    private List<Item> items;
    private LayoutInflater inflater;
    Listener listener;

    private static final boolean DEBUG = false;

    private void trace(String message) {
        if(DEBUG)
            Log.d("GroupAdapter", message);
    }

    public GroupAdapter(Context context, List<Group> groups){
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>(groups.size());
        for(Group group : groups){
            Item item = new Item();
            item.group = group;
            items.add(item);
        }
        listener = (Listener) context;
    }

    public class Item {
        public static final int GROUP_ITEM_READY = 0;
        public static final int GROUP_ITEM_DOWNING = 1;
        public static final int GROUP_ITEM_SETTING = 2;
        Group group;
        Bitmap photo = null;
        int state = GROUP_ITEM_READY;
        public Group getGroup() {
            return group;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.singleitem_group, parent, false);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivGroupIcon);
            holder.tvSwear = (TextView) convertView.findViewById(R.id.tvGroupName);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvGroupTime);
            holder.tvFrequency = (TextView) convertView.findViewById(R.id.tvGroupFrequency);
            holder.ivAlert = (ImageView) convertView.findViewById(R.id.ivAlert);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = (Item) getItem(position);
        trace("position = " + position);
        holder.ivIcon.setImageResource( getGroupIconId(item.group.getIcon()) );
        holder.tvSwear.setText(item.group.getSwear());
        holder.tvFrequency.setText("每週 " + item.getGroup().getFrequency() + " 次");

        // fix 24 clock to 12
        String ampm = item.getGroup().getDoItTime() >= 12 ? "PM " : "AM ";
        int ampmDoItTime = item.getGroup().getDoItTime() > 12 ? item.getGroup().getDoItTime() - 12
                :item.getGroup().getDoItTime();
        if (ampmDoItTime == 0)
            ampmDoItTime = 12;
        holder.tvTime.setText(ampm + ampmDoItTime + ":00");

        //  set Alert icon
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone資料。
        t.setToNow(); // 取得系統時間。
        if(t.hour >= item.getGroup().getDoItTime() && item.getGroup().getNoticeStatus()==1)
            holder.ivAlert.setVisibility(View.VISIBLE);
        else
            holder.ivAlert.setVisibility(View.INVISIBLE);

        //download photo
        switch (item.state) {
            case Item.GROUP_ITEM_READY:
                trace("GROUP_ITEM_READY, position = " + position);
                if(item.getGroup().getUrl().length()>0) {
                    listener.onDownloadGroupPhoto(this, item.getGroup().getUrl(), position);
                    item.state = Item.GROUP_ITEM_DOWNING;
                }
                else {
                    Bitmap srcBmp = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.button_friends_list);
                    Bitmap temBmp = srcBmp.copy(srcBmp.getConfig(), true);

                    item.photo = temBmp;
                    item.state = Item.GROUP_ITEM_SETTING;
                    notifyDataSetChanged();
                }
                break;
            case Item.GROUP_ITEM_DOWNING:
                // do Nothing
                break;
            case Item.GROUP_ITEM_SETTING:
                trace("GROUP_ITEM_WAIT_SET, position = " + position);
                holder.ivPhoto.setImageBitmap(item.photo);
                break;
        }

        return convertView;
    }

    public void setGroupPhoto(byte[] photo, int position) {
        Item item = (Item) getItem(position);
        if (item.state == Item.GROUP_ITEM_DOWNING) {
            trace("GROUP_ITEM_DOWNING, position = " + position);
            item.photo = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            item.state = Item.GROUP_ITEM_SETTING;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvSwear;
        TextView tvTime;
        TextView tvFrequency;
        ImageView ivAlert;
        ImageView ivPhoto;

    }

    private int getGroupIconId(int icon) {
        int resId;
        switch(icon) {
            case 1:
                resId=R.drawable.mark_running;
                break;
            case 2:
                resId=R.drawable.mark_running;
                break;
            case 3:
                resId=R.drawable.mark_swimming;
                break;
            case 4:
                resId=R.drawable.mark_riding;
                break;
            default:
                resId=R.drawable.mark_reading;
        }
        return resId;
    }

    public interface Listener {
        void onDownloadGroupPhoto(GroupAdapter groupAdapter, String url, int position);
    }
}
