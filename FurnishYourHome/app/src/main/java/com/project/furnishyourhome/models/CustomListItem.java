package com.project.furnishyourhome.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

/**
 * Created by Andrey on 10.2.2015 г..
 */
public class CustomListItem implements Parcelable {
    private String title;
    private Bitmap bitmap;
    byte[] byteArray;

    public CustomListItem(){}

    public CustomListItem(String title, Bitmap bitmap){
        this.title = title;
        this.bitmap = bitmap;
    }

    private CustomListItem(Parcel in) {
        this.title = in.readString();
        in.readByteArray(byteArray);
        this.bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        out.writeByteArray(byteArray);
    }

    public static final Parcelable.Creator<CustomListItem> CREATOR = new Parcelable.Creator<CustomListItem>() {
        public CustomListItem createFromParcel(Parcel in) {
            return new CustomListItem(in);
        }

        public CustomListItem[] newArray(int size) {
            return new CustomListItem[size];
        }
    };

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
