package com.project.furnishyourhome.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Sofa extends Furniture {
    private String type = "Sofa";
    public Sofa() {
        super();
    }

    public Sofa(String objectId){
        super(objectId);
    }

    public int describeContents() {
        return this.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        super.writeToParcel(dest, flags);
    }

    public Sofa(Parcel source) {
        super(source);
    }

    public static final Parcelable.Creator<Sofa> CREATOR = new Parcelable.Creator<Sofa>() {
        public Sofa createFromParcel(Parcel in) {
            return new Sofa(in);
        }

        public Sofa[] newArray(int size) {
            return new Sofa[size];
        }
    };
}
