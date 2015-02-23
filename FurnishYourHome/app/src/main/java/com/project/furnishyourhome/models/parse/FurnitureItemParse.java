package com.project.furnishyourhome.models.parse;

import com.parse.ParseClassName;
import com.project.furnishyourhome.models.Sofa;
import com.project.furnishyourhome.models.Table;

/**
 * Created by Andrey on 22.2.2015 г..
 */

@ParseClassName("FurnitureItems")
public class FurnitureItemParse extends FurnitureParse {

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

        return table;
    }
}
