package com.project.furnishyourhome.fragments;

import android.graphics.Bitmap;
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

import org.lucasr.twowayview.TwoWayView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MyRoomFragment extends Fragment {
    private static final String TAG = MyRoomFragment.class.getSimpleName();

    private ArrayList<CustomBitmap> arrayList;
    private ArrayList<CustomListItem> horizontalListItems;
    private ArrayList<CustomListItem> chosenItems;
    private CanvasView customCanvas;

    private TwoWayView twoWayView;

    //int oldh;
    //int oldw;

    public static MyRoomFragment newInstance() {
        Log.d(TAG, "newInstance()");
        return new MyRoomFragment();

    }

    public static MyRoomFragment newInstance(Bundle args) {
        Log.d(TAG, "newInstance(Bundle args)");
        MyRoomFragment f = new MyRoomFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState()");
        //outState.putInt("oldw", customCanvas.getWidth());
        //outState.putInt("oldh", customCanvas.getHeight());
        outState.putParcelableArrayList("savedBitmaps", customCanvas.getAddedBitmaps()); //items inside the canvas
        outState.putParcelableArrayList("chosenItems", chosenItems); //items for second fragment
        Log.d(TAG, "horizontalListItems.size() in outState is: "+horizontalListItems.size());
        outState.putParcelableArrayList("horizontalListItems", horizontalListItems); //items for horizontal listView
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
/*
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // TODO: this fucking recalculation
            recalculateCoordinates(oldw, oldh, arrayList);
        }*/

        CustomListAdapter adapter = new CustomListAdapter(getActivity(), R.layout.horizontal_list_item, horizontalListItems);
        adapter.notifyDataSetChanged();
        twoWayView.setAdapter(adapter);
        twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomListItem item = horizontalListItems.get(position);

                //its not the best but work stable
                Bundle args = new Bundle();
                args.putString("title", item.getTitle());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = item.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                args.putByteArray("bitmap", byteArray);
                args.putDouble("price", item.getPrice());
                args.putString("dimensions", item.getDimensions());
                args.putString("material", item.getMaterial());
                args.putString("info", item.getInfo());

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.right_drawer, NavDrawerRightFragment.newInstance(args));
                tr.commit();
            }
        });

        twoWayView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CustomListItem item = (CustomListItem) parent.getItemAtPosition(position);

                //resizing bitmap depending on screen before inserting in canvas
                int width = (int) (item.getBitmap().getWidth()*getActivity().getResources().getDisplayMetrics().density);
                int height = (int) (item.getBitmap().getHeight() * getActivity().getResources().getDisplayMetrics().density);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(item.getBitmap(), width, height, true);

                customCanvas.addNewElement(resizedBitmap);

                chosenItems.add(horizontalListItems.get(position));
                Bundle args = new Bundle();
                args.putParcelableArrayList("chosenItems", chosenItems);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.container_my_furniture, MyFurnitureFragment.newInstance(args));
                tr.replace(R.id.container_map, MapFragment.newInstance(args));
                tr.commit();
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_my_room, container, false);

        customCanvas = (CanvasView) rootView.findViewById(R.id.cv_room_canvas);
        customCanvas.setBackgroundResource(R.drawable.room);

        chosenItems = new ArrayList<>();
        horizontalListItems = new ArrayList<>();

        twoWayView = (TwoWayView) rootView.findViewById(R.id.twv_furniture);

        Bundle bundle = getArguments();
        if(savedInstanceState != null){
            arrayList = savedInstanceState.getParcelableArrayList("savedBitmaps");
            customCanvas.setAddedBitmaps(arrayList);

            //oldw = savedInstanceState.getInt("oldw");
            //oldh = savedInstanceState.getInt("oldh");


            chosenItems = savedInstanceState.getParcelableArrayList("chosenItems");
            horizontalListItems = savedInstanceState.getParcelableArrayList("horizontalListItems");
            Log.d(TAG, "got items from savedInstanceState");
            Log.d(TAG, "horizontalListItems.size() "+horizontalListItems.size());

            if(bundle != null) {
                horizontalListItems = bundle.getParcelableArrayList("horizontalListItems");
                Log.d(TAG, "GOT items from bundle, overriding savedState");
                Log.d(TAG, "horizontalListItems.size() from bundle: "+horizontalListItems.size());
            }

        } else {
            if(bundle != null) {
                horizontalListItems = bundle.getParcelableArrayList("horizontalListItems");
                Log.d(TAG, "GOT items from bundle");
                Log.d(TAG, "horizontalListItems.size() from bundle: "+horizontalListItems.size());
            }
        }

        // TODO: THIS LIST VIEW NOT LOADING ON API 16
        return rootView;
    }

   /* // TODO: this function
    private void recalculateCoordinates(int oldw, int oldh, ArrayList<CustomBitmap> arrayList) {
        Log.d(TAG, "recalculateCoordinates()");
        if ((oldw != 0) && (oldh != 0)) {
//            float f1 = this.customCanvas.getWidth() / this.oldw;  // incorrect

            float currentWidth = customCanvas.getCanvasWidth();// get current canvas width
            float currentHeight = customCanvas.getCanvasHeight();  // get current canvas height

//            Log.d("DIMENTIONS", "w: " + this.customCanvas.getWidth() + " oldw: " + this.oldw + " coef: " + f1);
//            float f2 = this.customCanvas.getHeight() / this.oldh; // incorrect
//
//            Log.d("DIMENTIONS", "h: " + this.customCanvas.getHeight() + " oldh: " + this.oldh + " coef: " + f2);

            for (int i = 0; i < arrayList.size(); i++) {
                CustomBitmap item = arrayList.get(i); // and the problem with ic_no_preview and Y equals 0 disappears :)

                float coefficientX = oldw / (item.getX() + item.getHalfWidth()); // add getHalfWidth to find the center ic_no_preview of the image
                float coefficientY = oldh / (item.getY() + item.getHalfHeight()); // add getHalfHeight to find the center Y of the image

                //float coefficientX = oldw / customCanvas.getCanvasWidth(); // add getHalfWidth to find the center ic_no_preview of the image
                //float coefficientY = oldh / customCanvas.getCanvasHeight(); // add getHalfHeight to find the center Y of the image

                Log.d("DIMENTIONS", "w: " + currentWidth + " oldw: " + oldw + " coef: " + coefficientX);
                Log.d("DIMENTIONS", "h: " + currentHeight + " oldh: " + oldh + " coef: " + coefficientY);

                float nextX = currentWidth / coefficientX;
                float nextY = currentHeight / coefficientY;

                item.setX(nextX);
                item.setY(nextY);
                arrayList.set(i, item);
                customCanvas.setAddedBitmaps(arrayList);
            }
        }
    }*/
}
