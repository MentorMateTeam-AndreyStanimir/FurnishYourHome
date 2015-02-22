package com.project.furnishyourhome.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrey on 18.2.2015 г..
 */
public class Table extends Furniture{
    private String type = "Table";
    public Table() {
        super();
    }

    public Table(String objectId){
        super(objectId);
    }

    public int describeContents() {
        return this.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        super.writeToParcel(dest, flags);
    }

    public Table(Parcel source) {
        super(source);
    }

    public static final Parcelable.Creator<Table> CREATOR = new Parcelable.Creator<Table>() {
        public Table createFromParcel(Parcel in) {
            return new Table(in);
        }

        public Table[] newArray(int size) {
            return new Table[size];
        }
    };
}
