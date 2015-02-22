package com.project.furnishyourhome.fragments;

import android.content.res.Configuration;
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

import com.project.furnishyourhome.MainActivity;
import com.project.furnishyourhome.R;
import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.models.CanvasView;
import com.project.furnishyourhome.models.CustomBitmap;
import com.project.furnishyourhome.models.CustomListItem;
import com.project.furnishyourhome.models.Furniture;
import com.project.furnishyourhome.models.Store;

import org.lucasr.twowayview.TwoWayView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class MyRoomFragment extends Fragment {
    private static final String TAG = MyRoomFragment.class.getSimpleName();
    //private static MyRoomFragment instance = null;

    private ArrayList<CustomBitmap> arrayList;
    private ArrayList<Store> stores;        // не се стресирвай само за проба e :)
    private ArrayList<CustomListItem> horizontalListItems;
    private ArrayList<CustomListItem> chosenItems;
    private CanvasView customCanvas;

    //int oldh;
    //int oldw;


    public static MyRoomFragment newInstance() {
        /*if(instance == null) {
            instance = new MyRoomFragment();
        }
        return instance;*/
        return new MyRoomFragment();

    }

    public static MyRoomFragment newInstance(Bundle args) {
        /*instance = MyRoomFragment.newInstance();
        instance.setArguments(args);
        return instance;*/
        MyRoomFragment f = new MyRoomFragment();
        f.setArguments(args);
        return f;
    }

    /*public static MyRoomFragment newInstance(ArrayList<Furniture> horizontalListItems) {
        MyRoomFragment f = MyRoomFragment.newInstance();
        setListItems(horizontalListItems, f);
        return f;
    }

    private static void setListItems(ArrayList<Furniture> horizontalListItems, MyRoomFragment f) {
        f.horizontalListItems = new ArrayList<CustomListItem>();

        for (Furniture item : horizontalListItems){
            CustomListItem listItem = new CustomListItem();
            listItem.setTitle(item.getName());
            listItem.setBitmap(item.getDrawable());
            listItem.setStore(item.getStore());
            f.listItems.add(listItem);
            f.horizontalListItems.add(listItem);
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("oldw", customCanvas.getWidth());
        outState.putInt("oldh", customCanvas.getHeight());
        outState.putParcelableArrayList("savedBitmaps", customCanvas.getAddedBitmaps()); //items inside the canvas
        outState.putParcelableArrayList("chosenItems", chosenItems); //items for second fragment
        Log.d(TAG, "horizontalListItems.size() in outState is: "+horizontalListItems.size());
        outState.putParcelableArrayList("horizontalListItems", horizontalListItems); //items for horizontal listView
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_room, container, false);

        customCanvas = (CanvasView) rootView.findViewById(R.id.cv_room_canvas);
        customCanvas.setBackgroundResource(R.drawable.bedroom);

        chosenItems = new ArrayList<>();
        stores = new ArrayList<Store>();
        //chosenItems = MainActivity.chosenItemsInMyRoomFragment;
        horizontalListItems = new ArrayList<>();
        //horizontalListItems = MainActivity.horizontalListViewInMyRoomFragment;
        Bundle bundle = getArguments();
        if(savedInstanceState != null){
            arrayList = savedInstanceState.getParcelableArrayList("savedBitmaps");
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                /*recalculateCoordinates(
                        savedInstanceState.getInt("oldw"),
                        savedInstanceState.getInt("oldh"),
                        arrayList);*/
            }
            customCanvas.setAddedBitmaps(arrayList);
            chosenItems = savedInstanceState.getParcelableArrayList("chosenItems");

            horizontalListItems = savedInstanceState.getParcelableArrayList("horizontalListItems");

            Log.d(TAG, "got items from savedInstanceState");
            Log.d(TAG, "horizontalListItems.size() "+horizontalListItems.size());

        } else {
            if(bundle != null) {
                horizontalListItems = bundle.getParcelableArrayList("horizontalListItems");
                //MainActivity.horizontalListViewInMyRoomFragment = horizontalListItems;
                Log.d(TAG, "got items from bundle");
                Log.d(TAG, "horizontalListItems.size() from bundle: "+horizontalListItems.size());
            } else {
                //horizontalListItems.add(new CustomListItem()); // TODO: WORKAROUND
                //Log.d(TAG, "add empty Item");
            }
        }

        TwoWayView twoWayView = (TwoWayView) rootView.findViewById(R.id.twv_furniture);
        twoWayView.setAdapter(new CustomListAdapter(getActivity().getBaseContext(), R.layout.horizontal_list_item, horizontalListItems));
        twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomListItem item = horizontalListItems.get(position);

                Bundle args = new Bundle();
                //args.putParcelable("item", item);
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
                customCanvas.addNewElement(item.getBitmap());

                chosenItems.add(horizontalListItems.get(position));
                stores.add(horizontalListItems.get(position).getStore());
                MapFragment.stores = stores;
                Bundle args = new Bundle();
                args.putParcelableArrayList("chosenItems", chosenItems);

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.container_my_furniture, MyFurnitureFragment.newInstance(args));
                tr.commit();
                return false;
            }
        });

//        if (savedInstanceState != null) {
//            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                arrayList = savedInstanceState.getParcelableArrayList("savedBitmaps");
//                this.customCanvas.setAddedBitmaps(this.arrayList);
//                this.chosenItems = savedInstanceState.getParcelableArrayList("chosenItems");
//                for(CustomListItem item : this.chosenItems){
//                    this.stores.add(item.getStore());
//                }
//            } else {
//                oldw = savedInstanceState.getInt("oldw");
//                oldh = savedInstanceState.getInt("oldh");
//                arrayList = savedInstanceState.getParcelableArrayList("savedBitmaps");
        return rootView;
    }

    private void recalculateCoordinates(int oldw, int oldh, ArrayList<CustomBitmap> arrayList) {
        if ((oldw != 0) && (oldh != 0)) {
//            float f1 = this.customCanvas.getWidth() / this.oldw;  // incorrect

            float currentWidth = this.customCanvas.getWidth(); // get current canvas width
            float currentHeight = this.customCanvas.getHeight();  // get current canvas height

//            Log.d("DIMENTIONS", "w: " + this.customCanvas.getWidth() + " oldw: " + this.oldw + " coef: " + f1);
//            float f2 = this.customCanvas.getHeight() / this.oldh; // incorrect
//
//            Log.d("DIMENTIONS", "h: " + this.customCanvas.getHeight() + " oldh: " + this.oldh + " coef: " + f2);

            for (int i = 0; i < arrayList.size(); i++) {
                CustomBitmap item = arrayList.get(i); // and the problem with X and Y equals 0 disappears :)

                float coefficientX = oldw / (item.getX() + item.getHalfWidth()); // add getHalfWidth to find the center X of the image
                float coefficientY = oldh / (item.getY() + item.getHalfHeight()); // add getHalfHeight to find the center Y of the image

                Log.d("DIMENTIONS", "w: " + currentWidth + " oldw: " + oldw + " coef: " + coefficientX);
                Log.d("DIMENTIONS", "h: " + currentHeight + " oldh: " + oldh + " coef: " + coefficientY);

                float nextX = currentWidth / coefficientX;
                float nextY = currentHeight / coefficientY;

                item.setX(nextX);
                item.setY(nextY);
                arrayList.set(i, item);
            }
        }
    }

    public ArrayList<CustomListItem> getHorizontalListItems() {
        return horizontalListItems;
    }

    public void setHorizontalListItems(ArrayList<CustomListItem> horizontalListItems) {
        this.horizontalListItems = horizontalListItems;
    }

    public ArrayList<CustomListItem> getChosenItems() {
        return chosenItems;
    }

    public void setChosenItems(ArrayList<CustomListItem> chosenItems) {
        this.chosenItems = chosenItems;
    }
}
