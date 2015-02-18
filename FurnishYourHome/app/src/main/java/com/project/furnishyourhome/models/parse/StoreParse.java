package com.project.furnishyourhome.models.parse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.project.furnishyourhome.models.Store;
import com.project.furnishyourhome.models.Table;

import java.io.ByteArrayOutputStream;

/**
 * Created by Andrey on 18.2.2015 г..
 */

@ParseClassName("Store")
public class StoreParse extends ParseObject {

    private String objectId;
    private String address;
    private String customersPhone;
    private String email;
    private ParseFile logo;
    private String name;
    private String webpage;
    private String workingHours;

    public StoreParse(){
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }

    public String getCustomersPhone() {
        return getString("customersPhone");
    }

    public void setCustomersPhone(String customersPhone) {
        put("customersPhone", customersPhone);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public Bitmap getLogo() {
        ParseFile file = getParseFile("logo");
        byte[] imageByte = new byte[0];
        try {
            imageByte = file.getData();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        Bitmap image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

        return image;
    }

    public void setLogo(Bitmap logo, String fileName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        logo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile parseImage = new ParseFile(fileName, byteArray);
        put("drawable", parseImage);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getWebpage() {
        return getString("webpage");
    }

    public void setWebpage(String webpage) {
        put("webpage", webpage);
    }

    public String getWorkingHours() {
        return getString("workingHours");
    }

    public void setWorkingHours(String workingHours) {
        put("workingHours", workingHours);
    }

    public Store getStore(){
        Store store = new Store();

        store.setName(this.getName());
        store.setAddress(this.getAddress());
        store.setCustomersPhone(this.getCustomersPhone());
        store.setEmail(this.getEmail());
        store.setWebpage(this.getWebpage());
        store.setWorkingHours(this.getWorkingHours());
        store.setLogo(this.getLogo());
        store.setObjectId(this.getObjectId());

        return store;
    }
}
