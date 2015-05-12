package com.ntil.habiture;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.habiture.Habiture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class HabitListFragment extends Fragment {


    private ListView item_list;
    private static List<Habiture> habitures;
    private HabitureAdapter habitureAdapter;

    private static final boolean DEBUG = true;
    private void trace(String message) {
        if(DEBUG)
            Log.d("HabitListFragment", message);
    }


    public HabitListFragment() {}

    public static HabitListFragment newInstance(List<Habiture> habitures){
        HabitListFragment fragment = new HabitListFragment();

        HabitListFragment.habitures = habitures;

        return fragment;
    }

    public class HabitureAdapter extends BaseAdapter {
        private List<Item> items;
        private LayoutInflater inflater;

        public HabitureAdapter(Context context, List<Habiture> habitures){
            inflater = LayoutInflater.from(context);
            items = new ArrayList<>(habitures.size());
            for(Habiture habiture : habitures){
                Item item = new Item();
                item.habiture = habiture;
                items.add(item);
            }
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_habit, parent, false);
                holder = new ViewHolder();
                holder.tv5 = (TextView) convertView.findViewById(R.id.textView5);
                holder.tv4 = (TextView) convertView.findViewById(R.id.textView4);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            Item item = (Item) getItem(position);
            holder.tv5.setText(item.habiture.getSwear() + "," + item.habiture.getPunishment());
            holder.tv4.setText(item.habiture.getRemain()+"/"+item.habiture.getId());

            return convertView;
        }

        private class ViewHolder {
            TextView tv5;
            TextView tv4;
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        trace("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        item_list = (ListView) getActivity().findViewById(R.id.lvHabitureList);

        habitureAdapter = new HabitureAdapter(getActivity(), habitures);
        item_list.setAdapter(habitureAdapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_habiture, container, false);
    }
}
