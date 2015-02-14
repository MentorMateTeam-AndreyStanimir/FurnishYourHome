package com.project.furnishyourhome.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.project.furnishyourhome.AddRoomActivity;
import com.project.furnishyourhome.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class MyRoomFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_room,container,false);

        FloatingActionButton mFab = (FloatingActionButton)rootView.findViewById(R.id.fab_add_room);
        mFab.setColorNormal(getResources().getColor(R.color.ColorPrimary));
        mFab.setColorPressed(getResources().getColor(R.color.ColorPrimaryDark));
        mFab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addRoomIntent = new Intent(getActivity().getApplicationContext(), AddRoomActivity.class);
                startActivity(addRoomIntent);
            }
        });

        return rootView;
    }
}