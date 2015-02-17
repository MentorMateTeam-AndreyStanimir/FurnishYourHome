package com.project.furnishyourhome.fragments;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.project.furnishyourhome.AddRoomActivity;
import com.project.furnishyourhome.R;
import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.models.CanvasView;
import com.project.furnishyourhome.models.CustomBitmap;
import com.project.furnishyourhome.models.CustomListItem;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class MyRoomFragment extends Fragment {

    ArrayList<CustomBitmap> arrayList;
    private CanvasView customCanvas;
    int oldh;
    int oldw;

    public static MyFurnitureFragment newInstance() {
        MyFurnitureFragment f = new MyFurnitureFragment();
        return f;
    }

    public static MyFurnitureFragment newInstance(Bundle args) {
        MyFurnitureFragment f = new MyFurnitureFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("savedBitmaps", customCanvas.getAddedBitmaps());
        outState.putInt("oldw", customCanvas.getWidth());
        outState.putInt("oldh", customCanvas.getHeight());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            oldw = savedInstanceState.getInt("oldw");
            oldh = savedInstanceState.getInt("oldh");
            arrayList = savedInstanceState.getParcelableArrayList("savedBitmaps");
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if ((this.oldw != 0) && (this.oldh != 0)) {
//            float f1 = this.customCanvas.getWidth() / this.oldw;  // incorrect

            float currentWidth = this.customCanvas.getWidth(); // get current canvas width
            float currentHeight = this.customCanvas.getHeight();  // get current canvas height

//            Log.d("DIMENTIONS", "w: " + this.customCanvas.getWidth() + " oldw: " + this.oldw + " coef: " + f1);
//            float f2 = this.customCanvas.getHeight() / this.oldh; // incorrect
//
//            Log.d("DIMENTIONS", "h: " + this.customCanvas.getHeight() + " oldh: " + this.oldh + " coef: " + f2);

            for (int i = 0; i < this.arrayList.size(); i++) {
                CustomBitmap item = this.arrayList.get(i); // and the problem with X and Y equals 0 disappears :)

                float coefficientX = this.oldw / (item.getX() + item.getHalfWidth()); // add getHalfWidth to find the center X of the image
                float coefficientY = this.oldh / (item.getY() + item.getHalfHeight()); // add getHalfHeight to find the center Y of the image

                Log.d("DIMENTIONS", "w: " + currentWidth + " oldw: " + this.oldw + " coef: " + coefficientX);
                Log.d("DIMENTIONS", "h: " + currentHeight + " oldh: " + this.oldh + " coef: " + coefficientY);

                float nextX = currentWidth / coefficientX;
                float nextY = currentHeight / coefficientY;

                item.setX(nextX);
                item.setY(nextY);
                this.arrayList.set(i, item);
            }
            this.customCanvas.setAddedBitmaps(this.arrayList);
        }

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_room,container,false);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.sp_created_rooms);
        //spinner logic

        customCanvas = (CanvasView)rootView.findViewById(R.id.cv_room_canvas);


        if(getArguments() != null) {
            TextView tv = (TextView) rootView.findViewById(R.id.tv_empty_furniture_list);
            tv.setVisibility(View.VISIBLE);
        } else {
            Bundle bundle = getArguments();
            ArrayList<CustomListItem> list = new ArrayList<>();

            //debug
            list.add(new CustomListItem("item 1", R.drawable.ic_launcher));
            list.add(new CustomListItem("item 2", R.drawable.ic_launcher));
            list.add(new CustomListItem("item 3", R.drawable.ic_launcher));
            list.add(new CustomListItem("item 4", R.drawable.ic_launcher));
            list.add(new CustomListItem("item 5", R.drawable.ic_launcher));
            list.add(new CustomListItem("item 6", R.drawable.ic_launcher));
            list.add(new CustomListItem("item 7", R.drawable.ic_launcher));
            //======

            TwoWayView twoWayView = (TwoWayView) rootView.findViewById(R.id.twv_furniture);
            twoWayView.setAdapter(new CustomListAdapter(getActivity().getBaseContext(), list));
            twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CustomListItem item = (CustomListItem) parent.getItemAtPosition(position);
                    customCanvas.addNewElement(item.getIcon());
                }
            });
        }








        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab_add_room);
        fab.setColorNormal(getActivity().getResources().getColor(R.color.ColorPrimary));
        fab.setColorPressed(getActivity().getResources().getColor(R.color.ColorPrimaryDark));
        fab.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        fab.setIconDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_menu_add));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addRoomIntent = new Intent(getActivity().getApplicationContext(), AddRoomActivity.class);
                startActivity(addRoomIntent);
            }
        });

        return rootView;
    }
}