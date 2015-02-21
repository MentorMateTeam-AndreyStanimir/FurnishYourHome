package com.project.furnishyourhome.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Andrey on 18.2.2015 г..
 */
public class Store implements Parcelable{
    private String objectId;
    private String address;
    private String customersPhone;
    private String email;
    private Bitmap logo;
    private String name;
    private String webpage;
    private String workingHours;
    private Location location;
    private byte[] byteArray;

    public Store (){}

    public Store (Parcel in){
        ArrayList<String> strings = new ArrayList<String>();
        in.readStringList(strings);
        this.setAddress(strings.get(0));
        this.setCustomersPhone(strings.get(1));
        this.setEmail(strings.get(2));
        this.setName(strings.get(3));
        this.setWebpage(strings.get(4));
        this.setWorkingHours(strings.get(5));

        double[] doubleArray = new double[2];
        in.readDoubleArray(doubleArray);
        Location newLocation = new Location("");
        newLocation.setLatitude(doubleArray[0]);
        newLocation.setLongitude(doubleArray[1]);
        this.setLocation(newLocation);

        in.readByteArray(byteArray);
        this.setLogo(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringList(new ArrayList<String>(
                Arrays.asList(this.getAddress(),
                        this.getCustomersPhone(),
                        this.getEmail(),
                        this.getName(),
                        this.getWebpage(),
                        this.getWorkingHours())));

        Location loc = getLocation();
        out.writeDoubleArray(new double[]{
                loc.getLatitude(),
                loc.getLongitude()
        });

        Bitmap parcelLogo = this.getLogo();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        parcelLogo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        out.writeByteArray(byteArray);
    }

    public static final Parcelable.Creator<Store> CREATOR = new Creator<Store>(){

        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomersPhone() {
        return customersPhone;
    }

    public void setCustomersPhone(String customersPhone) {
        this.customersPhone = customersPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
