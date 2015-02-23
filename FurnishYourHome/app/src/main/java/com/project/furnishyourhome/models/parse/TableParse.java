package com.project.furnishyourhome.models.parse;


import com.parse.ParseClassName;
import com.project.furnishyourhome.models.Table;

/**
 * Created by Andrey on 18.2.2015 г..
 */

@ParseClassName("Table")
public class TableParse extends FurnitureParse {

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
