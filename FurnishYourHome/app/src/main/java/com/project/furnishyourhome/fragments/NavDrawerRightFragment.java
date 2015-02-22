package com.project.furnishyourhome.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.furnishyourhome.R;
import com.project.furnishyourhome.models.CustomListItem;

import org.w3c.dom.Text;

//ToDo: Still in progress.
public class NavDrawerRightFragment extends Fragment {
    private static final String TAG = NavDrawerRightFragment.class.getSimpleName();

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
        View rootView = inflater.inflate(R.layout.fragment_nav_drawer_right, container, false);
        rootView.setBackgroundColor(getActivity().getResources().getColor(R.color.list_background));

        ImageView iv            = (ImageView) rootView.findViewById(R.id.iv_image);
        TextView tvTitle        = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvPrice        = (TextView) rootView.findViewById(R.id.tv_price);
        TextView tvDimensions   = (TextView) rootView.findViewById(R.id.tv_dimensions);
        TextView tvMaterial     = (TextView) rootView.findViewById(R.id.tv_material);
        TextView tvInfo         = (TextView) rootView.findViewById(R.id.tv_info);

        Bundle bundle = getArguments();
        if(bundle == null) {
            iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_no_preview));
            tvTitle.setText("no item selected");
            tvPrice.setText("");
            tvDimensions.setText("");
            tvMaterial.setText("");
            tvInfo.setText("");
        } else {
            Bitmap bitmap =  BitmapFactory.decodeByteArray(bundle.getByteArray("bitmap"), 0, bundle.getByteArray("bitmap").length);
            iv.setImageBitmap(bitmap);// TODO: NEED cosmetic fixes
            tvTitle.setText(bundle.getString("title"));
            tvPrice.setText("Цена: " + bundle.getDouble("price"));
            tvDimensions.setText("Размери:\n"+bundle.getString("dimensions"));
            tvMaterial.setText("Материали:\n"+bundle.getString("material"));
            tvInfo.setText("Допълнителана информация:\n"+bundle.getString("info"));
        }
        return rootView;
    }
}
