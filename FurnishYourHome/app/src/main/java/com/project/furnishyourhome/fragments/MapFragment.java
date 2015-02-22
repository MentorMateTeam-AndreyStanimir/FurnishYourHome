package com.project.furnishyourhome.fragments;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.furnishyourhome.MainActivity;
import com.project.furnishyourhome.R;
import com.project.furnishyourhome.location.MyLocationListener;
import com.project.furnishyourhome.models.Store;

import java.util.ArrayList;

/**
 * Created by Andrey on 11.2.2015 г..
 */
public class MapFragment extends Fragment implements GoogleMap.OnMyLocationChangeListener {

    private final int TOP_VIEW = 12;

    private static MapFragment instance = null;
    public static ArrayList<Store> stores = new ArrayList<Store>();

    private MyLocationListener mLocationClient;
    private View view;
    private GoogleMap map;
    private boolean isLocationFound;
    private FragmentActivity activity;
    private static Fragment fragment;
    private Marker currentPositionMarker;
    private Location myLocation;
    private LatLng latLngLocation;

    public static MapFragment newInstance() {

        if(instance == null) {
            instance = new MapFragment();
            instance.view = null;
            instance.map = null;
        }

        return instance;
    }

    public static MapFragment newInstance (MyLocationListener listener){
        instance = MapFragment.newInstance();
        instance.mLocationClient = listener;

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        view = inflater.inflate(R.layout.fragment_map,container,false);

//            mMapView = (MapView) view.findViewById(R.id.mapView);
//            mMapView.onCreate(savedInstanceState);
//
//            mMapView.onResume();// needed to get the map to display immediately
//
//            try {
//                MapsInitializer.initialize(getActivity().getApplicationContext());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            map = mMapView.getMap();
        this.activity = (FragmentActivity) getActivity();

        this.isLocationFound = false;

        // Така всеки път презарежда отново, вместо цялото да е в един if
      //  if (map == null) {
            map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        //}

        if (map != null) {
            this.setUpMap();
        }

        onResume();

        return view;
    }

    private void setUpMap() {
        // Enable finding location
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(this);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get location manager object from System service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) activity
                .getSystemService(activity.LOCATION_SERVICE);

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // provider = locationManager.GPS_PROVIDER;
        // MyLocationListener loc = new MyLocationListener();
        locationManager.requestLocationUpdates(provider, 5000, 5, mLocationClient);

        // Get current location
        myLocation = mLocationClient.getMyLocation();
        if(myLocation == null) {
            myLocation = locationManager.getLastKnownLocation(provider);
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();

        latLngLocation = new LatLng(latitude, longitude);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                latLngLocation, TOP_VIEW);
        map.animateCamera(update);

        map.addMarker(new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(mLocationClient.getCurrentAddress()));
    }


    @Override
    public void onStart() {
        super.onStart();
       // mLocationClient.connect();
    }

//    /**** The mapfragment's id must be removed from the FragmentManager
//     **** or else if the same it is passed on the next time then
//     **** app will crash ****/
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (map != null) {
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.map)).commit();
//            map = null;
//        }
//    }

    @Override
    public void onResume() {
         super.onResume();
        this.map.clear();

        this.setStoreMarkers();

        if (this.currentPositionMarker != null) {
            this.currentPositionMarker.setVisible(false);
            this.currentPositionMarker.remove();
        }
        if(this.latLngLocation != null) {
            this.currentPositionMarker = map.addMarker(new MarkerOptions().position(this.latLngLocation).title(mLocationClient.getCurrentAddress()));
        }

        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latLngLocation, TOP_VIEW));
    }

    private void setStoreMarkers() {
        for (Store store : stores) {

            LatLng latLng = new LatLng(store.getLocation().getLatitude(), store.getLocation().getLongitude());
            this.map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(store.getAddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    @Override
    public void onStop() {
        //mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMyLocationChange(Location location) {
        this.mLocationClient.onLocationChanged(location);
        this.changeCurrentLocation(location);
        this.isLocationFound(location);
    }

    public void changeCurrentLocation(Location location) {
        this.latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void isLocationFound(Location location) {
        if (!this.isLocationFound) {
            this.getAddress(location);
            this.onResume();
        }
    }

    public void getAddress(Location location) {
        this.map.clear();

        this.setStoreMarkers();
        this.myLocation = this.mLocationClient.getMyLocation();
        this.latLngLocation = new LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude());

        this.addMarkerPosition(this.latLngLocation);
    }

    public void addMarkerPosition(LatLng latLng) {
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, TOP_VIEW));
        this.map.addMarker(new MarkerOptions().position(latLng).title(this.mLocationClient.getCurrentAddress()));
        this.isLocationFound = true;
    }
}