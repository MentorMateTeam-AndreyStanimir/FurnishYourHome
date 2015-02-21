package com.project.furnishyourhome.location;

/**
 * Created by Andrey on 21.2.2015 г..
 */
public class MyLocation {
    private double latitude;
    private double longitude;

    public MyLocation (double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
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
}
