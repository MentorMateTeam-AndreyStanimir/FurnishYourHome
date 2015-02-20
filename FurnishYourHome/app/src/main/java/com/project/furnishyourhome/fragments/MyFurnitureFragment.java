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

    private ArrayList <CustomListItem> chosenItems;
    TextView textView;
    ListView listView;

    public static MyFurnitureFragment newInstance(Bundle args) {
        MyFurnitureFragment f = new MyFurnitureFragment();
        f.setArguments(args);
        return f;
    }

    public static MyFurnitureFragment newInstance() {
        return new MyFurnitureFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("chosenItems", chosenItems);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_furniture, container, false);

        textView = (TextView) rootView.findViewById(R.id.tv_empty_list_info);

        chosenItems = new ArrayList<>();
        if(savedInstanceState != null) {
            textView.setText("");
            textView.setVisibility(View.GONE);
            chosenItems = savedInstanceState.getParcelableArrayList("chosenItems");
        }

        listView = (ListView) rootView.findViewById(R.id.lv_my_furniture);

        if(chosenItems.isEmpty() && getArguments()!=null) {
            textView.setText("");
            textView.setVisibility(View.GONE);
            chosenItems = getArguments().getParcelableArrayList("chosenItems");
        } else {

            textView.setText("List is empty.");
        }

        listView.setAdapter(new CustomListAdapter(getActivity().getApplicationContext(), R.layout.drawer_list_item, chosenItems));
        return rootView;
    }
}