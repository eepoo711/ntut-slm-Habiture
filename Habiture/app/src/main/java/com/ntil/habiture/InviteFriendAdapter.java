package com.ntil.habiture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class InviteFriendAdapter extends ArrayAdapter<InviteFriendSingleItem> {

    private int resource;

    private List<InviteFriendSingleItem> items;

    public InviteFriendAdapter (Context context, int resource, List<InviteFriendSingleItem> items)
    {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        // 讀取目前位置的物件
        final InviteFriendSingleItem item = getItem(position);

        if (convertView == null) {
            // 建立項目畫面元件
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(inflater);
            li.inflate(resource, itemView, true);
        }
        else {
            itemView = (LinearLayout) convertView;
        }

        // 讀取姓名、已選擇元件
        TextView tvFriendsName = (TextView) itemView.findViewById(R.id.tvFriendsName);
        ImageView ivSelected = (ImageView) itemView.findViewById(R.id.ivSelected);

        // 設定姓名
        tvFriendsName.setText(item.getFriendsName());

        // 設定是否已選擇
        ivSelected.setVisibility(item.isSelected() ? View.VISIBLE : View.INVISIBLE);

        return itemView;
    }

    // 設定指定編號的資料
    public void set(int index, InviteFriendSingleItem item) {
        if (index >= 0 && index < items.size()) {
            items.set(index, item);
            notifyDataSetChanged();
        }
    }

    // 讀取指定編號的資料
    public InviteFriendSingleItem get(int index) {
        return items.get(index);
    }
}
