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

//TODO: delete items
public class MyFurnitureFragment extends Fragment {
    private static final String TAG = MyFurnitureFragment.class.getSimpleName();

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
        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteAlertToUser(position);
                return false;
            }
        });*/

        TextView tvTotalPrice = (TextView) rootView.findViewById(R.id.tv_total_price);
        double totalPrice = 0;
        for (int i=0; i<chosenItems.size(); i++) {
            totalPrice += chosenItems.get(i).getPrice();
        }
        tvTotalPrice.setText("Total price: "+totalPrice);
        return rootView;
    }

    /*private void showDeleteAlertToUser(int position){
        Log.d(TAG, "showDeleteAlertToUser");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.Base_Theme_AppCompat_Dialog));
        alertDialogBuilder.setMessage("Do you really want to delete this item?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                MyRoomFragment fragment = (MyRoomFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MyRoomFragment");
                Fragment.SavedState myFragmentState = getActivity().getSupportFragmentManager().saveFragmentInstanceState(fragment);
                Bundle args = new Bundle();

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                MyRoomFragment newFragment = MyRoomFragment.newInstance(args);
                newFragment.setInitialSavedState(myFragmentState);
                tr.replace(R.id.container_my_room, newFragment, "MyRoomFragment");
                tr.commit();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }*/
}