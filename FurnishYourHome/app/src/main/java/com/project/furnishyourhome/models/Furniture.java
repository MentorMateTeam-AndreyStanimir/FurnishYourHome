package com.project.furnishyourhome.models;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by Andrey on 18.2.2015 г..
 */
public abstract class Furniture {
    private String dimensions;
    private Bitmap drawable;
    private String info;
    private String material;
    private String name;
    private double price;
    private Store store;
    private String storeId;
    private String objectId;

    public String getDimensions() {
        return dimensions;
    }

    public Furniture(){
    }

    public Furniture(String storeId) {
        super();
        this.storeId = storeId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Bitmap getDrawable() {
        return drawable;
    }

    public void setDrawable(Bitmap drawable) {
        this.drawable = drawable;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }


    public byte[] getImageAsByteArray (){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getDrawable().compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }
}
