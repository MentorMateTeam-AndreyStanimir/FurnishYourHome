package com.project.furnishyourhome.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.furnishyourhome.R;
import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.models.ItemsHolder;


public class MyFurnitureFragment extends Fragment {
    private static final String TAG = MyFurnitureFragment.class.getSimpleName();

    private ListView listView;
    private CustomListAdapter adapter;
    private TextView tvTotalPrice;
    TextView tvEmptyList;
    double totalPrice;

    public static MyFurnitureFragment newInstance() {
        return new MyFurnitureFragment();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(TAG, "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "isVisibleToUser: "+isVisibleToUser);
        if (isVisibleToUser) {
           //todo: logic
            onResume();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        checkIfListIsEmpty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_furniture, container, false);

        tvEmptyList= (TextView) rootView.findViewById(R.id.tv_empty_list_info);

        listView = (ListView) rootView.findViewById(R.id.lv_my_furniture);

        adapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.favourites_list_item, ItemsHolder.chosenItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Toast.makeText(
                        getActivity(),
                        "store location: lat "
                                + chosenItems.get(position).getStore().getLocation().getLatitude()
                                + " lon " + chosenItems.get(position).getStore().getLocation().getLongitude(),
                        Toast.LENGTH_LONG).show();*/
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteAlertToUser(position);
                return false;
            }
        });

        tvTotalPrice = (TextView) rootView.findViewById(R.id.tv_total_price);
        totalPrice = 0;
        for (int i=0; i<ItemsHolder.chosenItems.size(); i++) {
            totalPrice += ItemsHolder.chosenItems.get(i).getPrice();
        }
        tvTotalPrice.setText(getResources().getString(R.string.total_price)+totalPrice+getResources().getString(R.string.currency));
        return rootView;
    }

    private void showDeleteAlertToUser(final int position){
        Log.d(TAG, "showDeleteAlertToUser");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.Base_Theme_AppCompat_Dialog));
        alertDialogBuilder.setMessage("Do you really want to delete this item?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                totalPrice -= ItemsHolder.chosenItems.get(position).getPrice();
                ItemsHolder.chosenItems.remove(position);
                ItemsHolder.canvasItems.remove(position);

                checkIfListIsEmpty();

                adapter.notifyDataSetChanged();
                tvTotalPrice.setText(getResources().getString(R.string.total_price)+totalPrice+getResources().getString(R.string.currency));

                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.container_my_room, MyRoomFragment.newInstance());//todo: refactor
                tr.replace(R.id.container_map, MapFragment.newInstance());
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
    }

    private void checkIfListIsEmpty(){
        if(ItemsHolder.chosenItems.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            tvEmptyList.setText("List is empty.");
        } else {
            tvEmptyList.setText("");
            tvEmptyList.setVisibility(View.GONE);
        }
    }
}