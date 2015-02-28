package com.project.furnishyourhome.models;

import com.project.furnishyourhome.adapters.CustomListAdapter;

import java.util.ArrayList;

/**
 * Created by toni on 15-2-27.
 */
public class ItemsHolder {

    //items for MainActivity
    public static ArrayList<Furniture> allFurniture;
    public static ArrayList<CustomListItem> leftNavDrawerItems;

    //items for MyRoomFragment & MyFurnitureFragment
    public static ArrayList<CustomListItem> horizontalListItems;
    public static ArrayList<CustomListItem> chosenItems;
    public static ArrayList<CustomBitmap> canvasItems;
}
