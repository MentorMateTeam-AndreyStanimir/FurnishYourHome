package com.project.furnishyourhome.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.furnishyourhome.R;

//ToDo: Still in progress.
public class NavDrawerRightFragment extends Fragment {

    public static NavDrawerRightFragment newInstance(Bundle args) {
        NavDrawerRightFragment fragment = new NavDrawerRightFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NavDrawerRightFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_drawer_right, container, false);

        ImageView iv = (ImageView) rootView.findViewById(R.id.iv_image);
        //price
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvDimensions = (TextView) rootView.findViewById(R.id.tv_dimensions);
        TextView tvDescription = (TextView) rootView.findViewById(R.id.tv_description);


        return rootView;
    }
}
