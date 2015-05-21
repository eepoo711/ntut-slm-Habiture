package com.ntil.habiture;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.habiture.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends BaseAdapter{
    private List<Item> items;
    private LayoutInflater inflater;

    private static final boolean DEBUG = true;

    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeActivity", message);
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
            holder.tvName = (TextView) convertView.findViewById(R.id.tvGroupName);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = (Item) getItem(position);
        holder.ivIcon.setImageResource(R.drawable.default_icon);
        holder.tvName.setText(item.group.getSwear());
        return convertView;
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
    }


}
