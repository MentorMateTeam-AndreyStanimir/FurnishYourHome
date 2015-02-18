package com.project.furnishyourhome.models.parse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.project.furnishyourhome.models.Sofa;
import com.project.furnishyourhome.models.Store;
import com.project.furnishyourhome.models.Table;

import java.io.ByteArrayOutputStream;

/**
 * Created by Andrey on 18.2.2015 г..
 */

@ParseClassName("Sofa")
public class SofaParse extends FurnitureParse {

    public Sofa getSofa(){
        Sofa sofa = new Sofa(this.storeId);

        sofa.setDimensions(this.getDimension());
        sofa.setInfo(this.getInfo());
        sofa.setMaterial(this.getMaterial());
        sofa.setName(this.getName());
        sofa.setPrice(this.getPrice());
        sofa.setStore(this.getStore());
        sofa.setDrawable(this.getDrawable());
        sofa.setObjectId(this.getObjectId());

        return sofa;
    }
}
