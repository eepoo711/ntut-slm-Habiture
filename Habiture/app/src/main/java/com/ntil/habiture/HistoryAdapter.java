package com.ntil.habiture;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.habiture.GroupHistory;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends BaseAdapter{
    private List<Item > items;
    protected LayoutInflater inflater;

    public HistoryAdapter(Context context, List<GroupHistory> groupHistories){
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>(groupHistories.size());
        for(GroupHistory history: groupHistories) {
            Item item = new Item();
            item.history = history;
            items.add(item);
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
            convertView = inflater.inflate(R.layout.singleitem_history, parent, false);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = (Item) getItem(position);
        GroupHistory history = item.getHistory();

        holder.ivIcon.setImageBitmap(history.getIcon());
        holder.tvName.setText(history.getName());
        holder.tvDate.setText(history.getDate());
        setPhoto(holder, item, history);

        return convertView;
    }

    private void setPhoto(ViewHolder holder, Item item, GroupHistory history) {
        if(item.photo != null)
            holder.ivPicture.setImageBitmap(item.photo);
        else
            holder.ivPicture.setImageResource(R.drawable.default_role);
    }

    public void setPhoto(byte[] photo, int position) {
        Item item = (Item) getItem(position);
        item.photo = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        notifyDataSetChanged();
    }

    public void release() {
        for(Item item: items)
            if(item.photo != null)
                item.photo.recycle();
    }

    public class Item {
        private Bitmap photo = null;
        private GroupHistory history;

        public GroupHistory getHistory() {
            return history;
        }
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvDate;
        ImageView ivPicture;
    }
}
