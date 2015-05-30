package com.ntil.habiture;

/**
 * Created by GawinHsu on 5/18/15.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.habiture.Habiture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GawinHsu on 5/7/15.
 */

public class HabitListAdapter extends BaseAdapter {
    private List<Item> items;
    private LayoutInflater inflater;
    private Listener listener;

    private static final boolean DEBUG = false;
    private void trace(String message) {
        if(DEBUG)
            Log.d("HabitListAdapter", message);
    }

    public HabitListAdapter(Context context, List<Habiture> habitures){
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>(habitures.size());
        for(Habiture habiture : habitures){
            Item item = new Item();
            item.habiture = habiture;
            items.add(item);
        }
        listener = (Listener) context;
    }

    public void setPassDisable(int position) {
        Item item = (Item) getItem(position);
        trace("setPassDisable");
        item.isPassDisable = true;
        notifyDataSetChanged();

    }

    public class Item {
        Habiture habiture;
        boolean isPassDisable = false;

        public Habiture getHabiture() {
            return habiture;
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
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.singleitem_habit, parent, false);
            holder = new ViewHolder();
            holder.tvSwear = (TextView) convertView.findViewById(R.id.tvSwear);
            holder.tvPunishment = (TextView) convertView.findViewById(R.id.tvPunishment);
            holder.tvRemain = (TextView) convertView.findViewById(R.id.tvRemain);
            holder.ibMore = (ImageButton) convertView.findViewById(R.id.btnMore);
            holder.ibCamera = (ImageButton) convertView.findViewById(R.id.btnCamera);
            holder.ibPass = (ImageButton) convertView.findViewById(R.id.btnPass);


            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Item item = (Item) getItem(position);

        holder.tvSwear.setText(item.habiture.getSwear());
        holder.tvPunishment.setText(item.habiture.getPunishment());
        holder.tvRemain.setText("本週剩 " + item.habiture.getRemainFrequency() + " 次");
        trace("notice = " + item.getHabiture().getNoticeEnable());
        trace("isPassDisable = " + item.isPassDisable);
        if (item.isPassDisable) {
            holder.ibPass.setEnabled(false);
            holder.ibPass.getDrawable().setAlpha(128);
        } else {
            if (!item.getHabiture().getNoticeEnable()) {
                holder.ibPass.setEnabled(false);
                holder.ibPass.getDrawable().setAlpha(128);
            } else {
                holder.ibPass.setEnabled(true);
                holder.ibPass.getDrawable().setAlpha(255);
            }
        }

        final Habiture habiture = item.getHabiture();
        holder.tvPunishment.setSingleLine(true);
        holder.ibMore.setVisibility(View.VISIBLE);
        holder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick, pid = " + habiture.getId());
                holder.tvPunishment.setSingleLine(false);
                holder.ibMore.setVisibility(View.INVISIBLE);
            }
        });
        holder.ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick, pid = " + habiture.getId());
                listener.onClickHabitCamera(habiture.getId());
            }
        });

        holder.ibPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick, pid = " + habiture.getId() + ", remain passHabitToday = " +
                        habiture.getRemainPass());
                listener.onClickHabitPass(
                        habiture,
                        position,
                        habiture.getRemainPass());
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tvSwear;
        TextView tvPunishment;
        TextView tvRemain;
        ImageButton ibMore;
        ImageButton ibCamera;
        ImageButton ibPass;
    }

    public interface Listener {
        public void onClickHabitSingleItem(int pid);
        public void onClickHabitCamera(int pid);
        public void onClickHabitPass(Habiture habiture, int position, int passRemain);
    }
}
