package com.project.furnishyourhome.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.project.furnishyourhome.fragments.MapFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationListener implements LocationListener {

    private Context context;
    private Location myLocation;
    private double latitude;
    private double longitude;
    private String currentAddress;

    public static MyLocationListener myLocationListener = null;

    private MyLocationListener(Context context) {
        this.context = context;
        this.setCurrentAddress("");
    }

    public static MyLocationListener instance (Context context){
        if(myLocationListener == null){
            myLocationListener = new MyLocationListener(context);
        }

        return myLocationListener;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    private void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    public Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public double getLatitude() {
        return latitude;
    }

    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Location changed");
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
        this.setMyLocation(location);
        (new GetAddressTask()).execute(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Location", "Status changed");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Location", "Provider enabled: " + provider);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Location", "Provider disabled: " + provider);

    }
    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Location loc;

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            String addressText = "";

            // Get the current location from the input parameter list
            loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                /*
				 * Return 1 address.
				 */
                List<Address> address = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                addresses = address;
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments "
                        + Double.toString(loc.getLatitude()) + " , "
                        + Double.toString(loc.getLongitude())
                        + " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */

                if (address.getMaxAddressLineIndex() > 0) {
                    addressText = address.getAddressLine(0);
                    Log.e("Success", "Success");
                } else {
                    addressText = String.format("%s, %s, %s", "",
                            address.getLocality(), address.getCountryName());
                }

                // Return the text
                return addressText;
            }
            Log.e("Success", "No Success");
            return "No address found";
        }

        @Override
        protected void onPostExecute(String result) {
            setCurrentAddress(result);
            Log.d("RESULT", String.valueOf(getLatitude()));
            Log.d("RESULT", String.valueOf(getLongitude()));
            Log.d("RESULT", result);

            super.onPostExecute(result);
        }
    }
}
