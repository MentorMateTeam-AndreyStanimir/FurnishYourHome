package com.project.furnishyourhome.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.models.CustomListItem;

import java.util.ArrayList;

/**
 * Created by Andrey on 11.2.2015 г..
 */
public class MyFurnitureFragment extends ListFragment {

    public static MyFurnitureFragment newInstance(Bundle args) {
        MyFurnitureFragment f = new MyFurnitureFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList <CustomListItem> list = getArguments().getParcelableArrayList("chosenItems");

        setListAdapter(new CustomListAdapter(getActivity().getApplicationContext(), list));
    }
}