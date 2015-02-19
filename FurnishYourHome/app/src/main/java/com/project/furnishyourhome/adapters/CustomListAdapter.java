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

    public CustomListAdapter(Context context, ArrayList<CustomListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
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
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.iconMenu);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.titleMenu);

        Bitmap bitmap = getItem(position).getBitmap();
        if(bitmap != null){
            imgIcon.setImageBitmap(bitmap);
        } else {
            imgIcon.setImageResource(getItem(position).getIcon());
        }
        txtTitle.setText(listItems.get(position).getTitle());

        return convertView;
    }
}
