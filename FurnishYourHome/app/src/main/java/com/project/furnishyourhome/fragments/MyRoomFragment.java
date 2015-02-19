package com.project.furnishyourhome.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.project.furnishyourhome.R;
import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.models.CanvasView;
import com.project.furnishyourhome.models.CustomBitmap;
import com.project.furnishyourhome.models.CustomListItem;
import com.project.furnishyourhome.models.Furniture;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class MyRoomFragment extends Fragment {
    private static MyRoomFragment instance = null;

    ArrayList<CustomBitmap> arrayList;
    ArrayList<CustomListItem> listItems;
    private CanvasView customCanvas;

    int oldh;
    int oldw;

    public static MyRoomFragment newInstance() {
        if(instance == null) {
            instance = new MyRoomFragment();
        }
        return instance;
    }

    public static MyRoomFragment newInstance(Bundle args) {
        instance = MyRoomFragment.newInstance();
        instance.setArguments(args);
        return instance;
    }

    public static MyRoomFragment newInstance(ArrayList<Furniture> listItems) {
        MyRoomFragment f = MyRoomFragment.newInstance();
        setListItems(listItems, f);
        return f;
    }

    private static void setListItems(ArrayList<Furniture> listItems, MyRoomFragment f) {
        f.listItems = new ArrayList<CustomListItem>();

        for (Furniture item : listItems){
            CustomListItem listItem = new CustomListItem();
            listItem.setTitle(item.getName());
            listItem.setBitmap(item.getDrawable());
            f.listItems.add(listItem);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("savedBitmaps", customCanvas.getAddedBitmaps());
        outState.putInt("oldw", customCanvas.getWidth());
        outState.putInt("oldh", customCanvas.getHeight());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_room,container,false);

        customCanvas = (CanvasView) rootView.findViewById(R.id.cv_room_canvas);
        customCanvas.setBackgroundResource(R.drawable.bedroom);

        Bundle bundle = getArguments(); // TODO: get parameters for horizontal list view

        TwoWayView twoWayView = (TwoWayView) rootView.findViewById(R.id.twv_furniture);
        twoWayView.setAdapter(new CustomListAdapter(getActivity().getBaseContext(), listItems));
        twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomListItem item = (CustomListItem) parent.getItemAtPosition(position);
                customCanvas.addNewElement(item.getBitmap());

                Bundle args = new Bundle();
                args.putParcelableArrayList("chosenItems", listItems);// TODO: NEED FIX, put the correct data

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.container_my_furniture, MyFurnitureFragment.newInstance(args));
                tr.commit();
            }
        });

        if (savedInstanceState != null) {
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                arrayList = savedInstanceState.getParcelableArrayList("savedBitmaps");
                this.customCanvas.setAddedBitmaps(this.arrayList);
            } else {
                oldw = savedInstanceState.getInt("oldw");
                oldh = savedInstanceState.getInt("oldh");
                arrayList = savedInstanceState.getParcelableArrayList("savedBitmaps");

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
            }
        }
        return rootView;
    }
}