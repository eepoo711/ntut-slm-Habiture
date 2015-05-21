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
import android.widget.Button;
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

    private static final boolean DEBUG = true;
    private void trace(String message) {
        if(DEBUG)
            Log.d("HabitListFragment", message);
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

    public class Item {
        Habiture habiture;

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
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_habit, parent, false);
            holder = new ViewHolder();
            holder.tvSwear = (TextView) convertView.findViewById(R.id.tvSwear);
            holder.tvPunishment = (TextView) convertView.findViewById(R.id.tvPunishment);
            holder.tvRemain = (TextView) convertView.findViewById(R.id.tvRemain);
            holder.btnMore = (Button) convertView.findViewById(R.id.btnMore);
            holder.ibCamera = (ImageButton) convertView.findViewById(R.id.btnCamera);
            holder.btnPass = (Button) convertView.findViewById(R.id.btnPass);


            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = (Item) getItem(position);
        holder.tvSwear.setText(item.habiture.getSwear());
        holder.tvPunishment.setText("做不到的話就 " + item.habiture.getPunishment());
        holder.tvRemain.setText("本週剩餘 " + item.habiture.getRemainFrequency() + " 次數");

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick, pid = " + ((Item) getItem(position)).getHabiture().getId());
                int pid = ((Item) getItem(position)).getHabiture().getId();
                listener.onClickHabitSingleItem(pid);
            }
        });
        holder.ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick, pid = " + ((Item) getItem(position)).getHabiture().getId());
                listener.onClickHabitCamera(((Item) getItem(position)).getHabiture().getId());
            }
        });

        holder.btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick, pid = " + ((Item) getItem(position)).getHabiture().getId() + ", remain pass = " +
                        ((Item) getItem(position)).getHabiture().getRemainPass());
                listener.onClickHabitPass(((Item) getItem(position)).getHabiture().getId(),
                        ((Item) getItem(position)).getHabiture().getRemainPass());
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tvSwear;
        TextView tvPunishment;
        TextView tvRemain;
        Button btnMore;
        ImageButton ibCamera;
        Button btnPass;
    }

    public interface Listener {
        public void onClickHabitSingleItem(int pid);
        public void onClickHabitCamera(int pid);
        public void onClickHabitPass(int pid, int passRemain);
    }
}
