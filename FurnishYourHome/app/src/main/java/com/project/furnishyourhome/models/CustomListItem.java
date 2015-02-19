package com.project.furnishyourhome.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrey on 10.2.2015 г..
 */
public class CustomListItem implements Parcelable {
    private String title;
    private int icon;
    private Bitmap bitmap;

    public CustomListItem(){}

    public CustomListItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    private CustomListItem(Parcel in) {
        title = in.readString();
        icon = in.readInt();
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
        out.writeInt(icon);
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

    public int getIcon(){
        return this.icon;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
