package com.ntil.habiture;


import android.content.Context;
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
    }

    public class Item {
        Group group;

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
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = (Item) getItem(position);
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

        return convertView;
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvSwear;
        TextView tvTime;
        TextView tvFrequency;
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
}
