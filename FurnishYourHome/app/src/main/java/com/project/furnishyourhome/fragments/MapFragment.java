package com.project.furnishyourhome.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.furnishyourhome.R;
import com.project.furnishyourhome.models.CanvasView;
import com.project.furnishyourhome.models.CustomBitmap;

import java.util.ArrayList;

/**
 * Created by Andrey on 11.2.2015 г..
 */
public class MapFragment extends Fragment {

    private static MapFragment instance = null;

    public static MapFragment newInstance() {
        if(instance == null) {
            instance = new MapFragment();
        }
        return instance;
    }

    public static MapFragment instance (Bundle args){
        instance = MapFragment.newInstance();
        instance.setArguments(args);

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map,container,false);

        return rootView;
    }
}