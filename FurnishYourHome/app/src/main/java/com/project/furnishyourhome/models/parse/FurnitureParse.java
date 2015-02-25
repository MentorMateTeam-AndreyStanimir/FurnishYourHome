package com.project.furnishyourhome.models.parse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.furnishyourhome.models.Sofa;
import com.project.furnishyourhome.models.Store;
import com.project.furnishyourhome.models.Table;

import java.io.ByteArrayOutputStream;
import java.util.List;


@ParseClassName("FurnitureItems")
public class FurnitureParse extends ParseObject {
    protected String dimensions;
    protected Bitmap drawable;
    protected String info;
    protected String material;
    protected String name;
    protected double price;
    protected String storeId;
    protected String type;
    protected String furnitureId;

    public FurnitureParse() {
    }

    public String getDimension() {
        this.dimensions = getString("dimensions");

        return this.dimensions;
    }

    public void setDimension(String dimensions) {
        put("dimensions", dimensions);
    }

    public Bitmap getDrawable() {

        ParseFile file = getParseFile("drawable");
        byte[] imageByte = new byte[0];
        try {
            imageByte = file.getData();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        this.drawable = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

        return this.drawable;
    }

    public void setDrawable(Bitmap drawable, String fileName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        drawable.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile parseImage = new ParseFile(fileName, byteArray);
        put("drawable", parseImage);
    }

    public String getInfo() {
        this.info = getString("info");
        return this.info;
    }

    public void setInfo(String info) {
        put("info", info);
    }

    public String getMaterial() {
        this.material = getString("material");

        return this.material;
    }

    public void setMaterial(String material) {
        put("material", material);
    }

    public String getName() {
        this.name = getString("name");

        return this.name;
    }

    public void setName(String name) {
        put("name", name);
    }

    public double getPrice() {
        this.price = Double.parseDouble(String.valueOf(getNumber("price")));

        return this.price;
    }

    public void setPrice(String price) {
        put("price", price);
    }

    public String getType() {
        ParseObject typeObj = getParseObject("furnitureId");
        this.setFurnitureId(typeObj.getObjectId());
        //String txt = typeObj.getString("type");
        String fType = "";

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Furniture");
        try {
            ParseObject fObj = query.get(this.getFurnitureId());
            fType = fObj.getString("type");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fType;
    }

    public void setType(String furnitureId) {
        put("furnitureId", furnitureId);
    }

    public String getFurnitureId() {
        return furnitureId;
    }

    public void setFurnitureId(String furnitureId) {
        this.furnitureId = furnitureId;
    }

    public Store getStore() {
        ParseObject store = getParseObject("store");
        this.storeId = store.getObjectId();
        StoreParse obj = new StoreParse();

        ParseQuery<StoreParse> query = ParseQuery.getQuery(StoreParse.class);
        try {
            obj = query.get(this.storeId);
            obj.setObjectId(this.storeId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj.getStore();
    }

    public void setStore(String storeId) {
        put("store", storeId);
    }

    public Sofa getSofa(){
        Sofa sofa = new Sofa(this.storeId);

        sofa.setDimensions(this.getDimension());
        sofa.setInfo(this.getInfo());
        sofa.setMaterial(this.getMaterial());
        sofa.setName(this.getName());
        sofa.setPrice(this.getPrice());
        sofa.setStore(this.getStore());
        sofa.setDrawable(this.getDrawable());
        sofa.setStoreId(this.storeId);
        sofa.setObjectId(this.getObjectId());
        sofa.setFurnitureId(this.getFurnitureId());

        return sofa;
    }

    public Table getTable(){
        Table table = new Table(this.storeId);

        table.setDimensions(this.getDimension());
        table.setInfo(this.getInfo());
        table.setMaterial(this.getMaterial());
        table.setName(this.getName());
        table.setPrice(this.getPrice());
        table.setStore(this.getStore());
        table.setDrawable(this.getDrawable());
        table.setStoreId(this.storeId);
        table.setObjectId(this.getObjectId());
        table.setFurnitureId(this.getFurnitureId());

        return table;
    }
}
