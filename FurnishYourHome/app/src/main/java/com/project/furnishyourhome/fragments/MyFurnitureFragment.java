package com.project.furnishyourhome.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.project.furnishyourhome.R;
import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.models.CustomListItem;

import java.util.ArrayList;

/**
 * Created by Andrey on 11.2.2015 г..
 */
public class MyFurnitureFragment extends Fragment {

    public static MyFurnitureFragment newInstance(Bundle args) {
        MyFurnitureFragment f = new MyFurnitureFragment();
        f.setArguments(args);
        return f;
    }

    public static MyFurnitureFragment newInstance() {
        MyFurnitureFragment f = new MyFurnitureFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_furniture, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.tv_empty_list_info);

        if(getArguments() != null) {
            textView.setVisibility(View.INVISIBLE);

            ArrayList <CustomListItem> list = getArguments().getParcelableArrayList("chosenItems");

            ListView listView = (ListView) rootView.findViewById(R.id.lv_my_furniture);
            listView.setAdapter(new CustomListAdapter(getActivity().getApplicationContext(), list));
        } /*else {
            textView.setVisibility(View.VISIBLE);
        }*/

        return rootView;
    }
}