package com.project.furnishyourhome.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.furnishyourhome.R;
import com.project.furnishyourhome.models.CustomListItem;

import java.util.ArrayList;

/**
 * Created by Andrey on 10.2.2015 г..
 */
public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CustomListItem> listItems;
    private int layoutID;

    public CustomListAdapter(Context context, int layoutID, ArrayList<CustomListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
        this.layoutID = layoutID;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public CustomListItem getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(layoutID, null);
        }

        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon_menu);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title_menu);
        if(layoutID == R.layout.favourites_list_item) {
            TextView tvInfo = (TextView) convertView.findViewById(R.id.tv_extra_info_menu);
            tvInfo.setText("Store: "+listItems.get(position).getStore().getName()); // TODO: need to be the store
        }

        Bitmap bitmap = listItems.get(position).getBitmap();

        ivIcon.setImageBitmap(bitmap);
        tvTitle.setText(listItems.get(position).getTitle());

        return convertView;
    }
}
