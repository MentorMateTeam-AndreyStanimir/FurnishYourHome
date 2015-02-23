package com.project.furnishyourhome.models.parse;

import com.parse.ParseClassName;
import com.project.furnishyourhome.models.Sofa;

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
