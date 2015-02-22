package com.project.furnishyourhome.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.furnishyourhome.R;
import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.models.CustomListItem;

import java.util.ArrayList;

/**
 * Created by Andrey on 11.2.2015 г..
 */
public class MyFurnitureFragment extends Fragment {

    private ArrayList <CustomListItem> chosenItems;

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

        TextView textView = (TextView) rootView.findViewById(R.id.tv_empty_list_info);

        chosenItems = new ArrayList<>();
        if(savedInstanceState != null) {
            textView.setText("");
            textView.setVisibility(View.GONE);
            chosenItems = savedInstanceState.getParcelableArrayList("chosenItems");
        }

        ListView listView = (ListView) rootView.findViewById(R.id.lv_my_furniture);

        if(chosenItems.isEmpty() && getArguments()!=null) {
            textView.setText("");
            textView.setVisibility(View.GONE);
            chosenItems = getArguments().getParcelableArrayList("chosenItems");
        } else {
            textView.setText("List is empty.");
        }

        listView.setAdapter(new CustomListAdapter(getActivity().getApplicationContext(), R.layout.favourites_list_item, chosenItems));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        getActivity(),
                        "store location: lat "
                                + chosenItems.get(position).getStore().getLocation().getLatitude()
                                + " lon " + chosenItems.get(position).getStore().getLocation().getLongitude(),
                        Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }
}