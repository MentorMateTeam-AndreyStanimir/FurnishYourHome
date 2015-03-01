package com.project.furnishyourhome.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.furnishyourhome.R;
import com.project.furnishyourhome.models.CustomListItem;

import java.util.ArrayList;


public class MapFragment extends Fragment {
    private static final String TAG = MapFragment.class.getSimpleName();

    private final float TOP_VIEW = 12.0f;

    private GoogleMap map;

    LocationManager locationManager;
    ArrayList<CustomListItem> storesLocations;

    public static MapFragment newInstance() {
        Log.d(TAG, "newInstance()");
        return new MapFragment();
    }

    public static MapFragment newInstance (Bundle args){
        Log.d(TAG, "newInstance (Bundle args)");
        MapFragment f = new MapFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(TAG, "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "isVisibleToUser: "+isVisibleToUser);
        if (isVisibleToUser) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Log.d(TAG, "GPS is ON");
            } else {
                Log.d(TAG, "GPS is OFF");
                showGPSDisabledAlertToUser();
            }
            onResume();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        trackMyPosition();
        showStoresOnMap();
        super.onResume();
    }

    private void trackMyPosition() {
        Log.d(TAG, "trackMyPosition");
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                showMeOnTheMap(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, locationListener);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        MapsInitializer.initialize(getActivity().getApplicationContext());
        //creating map
        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        storesLocations = new ArrayList<>();
        Bundle args = getArguments();
        if(savedInstanceState != null) {
            storesLocations = savedInstanceState.getParcelableArrayList("storesLocations");
        } else {
            if(args != null) {
                storesLocations = args.getParcelableArrayList("chosenItems");
                Log.d(TAG, "storesLocations.size(): "+storesLocations.size());
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelableArrayList("storesLocations", storesLocations);
        super.onSaveInstanceState(outState);
    }

    private void showStoresOnMap() {
        Log.d(TAG, "showStoresOnMap");
        for(int i=0; i<storesLocations.size(); i++) {
            double lat = storesLocations.get(i).getStore().getLocation().getLatitude();
            double lng = storesLocations.get(i).getStore().getLocation().getLongitude();


            MarkerOptions storeLocation = new MarkerOptions();
            storeLocation.position(new LatLng(lat, lng));
            storeLocation.title(storesLocations.get(i).getStore().getName());
            storeLocation.icon(BitmapDescriptorFactory.fromBitmap(storesLocations.get(i).getStore().getLogo()));
            map.addMarker(storeLocation);
            Log.d(TAG, storesLocations.get(i).getStore().getName()+": "+lat+" "+lng);
        }
    }

    private void showGPSDisabledAlertToUser(){
        Log.d(TAG, "showGPSDisabledAlertToUser");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.Base_Theme_AppCompat_Dialog));
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showMeOnTheMap(Location location) {
        Log.d(TAG, "showMeOnTheMap");
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        map.clear();
        showStoresOnMap();

        MarkerOptions myLocation = new MarkerOptions();
        myLocation.position(new LatLng(lat, lng));
        myLocation.title("me");
        myLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_me));
        map.addMarker(myLocation);
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng) , TOP_VIEW) );
        Log.d(TAG, "me: "+lat+" "+lng);
    }
}