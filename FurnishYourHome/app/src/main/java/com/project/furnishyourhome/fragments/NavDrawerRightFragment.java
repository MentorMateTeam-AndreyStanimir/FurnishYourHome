package com.project.furnishyourhome.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public static NavDrawerRightFragment newInstance() {
        NavDrawerRightFragment fragment = new NavDrawerRightFragment();
        return fragment;
    }

    public NavDrawerRightFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_drawer_right, container, false);
        rootView.setBackgroundColor(getActivity().getResources().getColor(R.color.list_background));

        ImageView iv = (ImageView) rootView.findViewById(R.id.iv_image);
        //price
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvDimensions = (TextView) rootView.findViewById(R.id.tv_dimensions);
        TextView tvDescription = (TextView) rootView.findViewById(R.id.tv_description);

        Bundle bundle = getArguments();
        if(bundle == null) {
            iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_launcher));
            tvTitle.setText("no item selected");
            tvDimensions.setText("");
            tvDescription.setText("");
        } else {
            tvTitle.setText(bundle.getString("title"));

            Bitmap bitmap =  BitmapFactory.decodeByteArray(bundle.getByteArray("bitmap"), 0, bundle.getByteArray("bitmap").length);
            iv.setImageBitmap(bitmap);

            //tvDimensions.setText(bundle.getString("dimensions"));
            //tvDescription.setText(bundle.getString("description"));
        }
        return rootView;
    }
}
