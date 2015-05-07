package com.ntil.habiture;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class HabitListFragment extends ListFragment {

    private static final boolean DEBUG = false;
    private void trace(String message) {
        if(DEBUG)
            Log.d("HabitListFragment", message);
    }


    public class HabitAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public HabitAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            trace("getCount");
            return 20;
        }

        @Override
        public Object getItem(int position) {
            trace("getItem");
            return null;
        }

        @Override
        public long getItemId(int position) {
            trace("getItemId");
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            trace("getView position = " + position);

            // get view
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_habit, null, false);
            } else {
                // TODO get view holder
            }
            // TODO put datas

            return convertView;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setListAdapter(new HabitAdapter(getActivity()));
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
