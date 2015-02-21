package com.project.furnishyourhome.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.project.furnishyourhome.fragments.MapFragment;
import com.project.furnishyourhome.fragments.MyFurnitureFragment;
import com.project.furnishyourhome.fragments.MyRoomFragment;
import com.project.furnishyourhome.models.CustomBitmap;
import com.project.furnishyourhome.models.CustomListItem;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = ViewPagerAdapter.class.getSimpleName();

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int numbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    FragmentManager fragmentManager;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);
        fragmentManager = fm;
        this.Titles = mTitles;
        this.numbOfTabs = mNumbOfTabs;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
               /* if(fragmentManager.findFragmentByTag("MyRoomFragment") != null) {
                    MyRoomFragment fragment = (MyRoomFragment) fragmentManager.findFragmentByTag("MyRoomFragment");
                    ArrayList<CustomListItem> horizontalListItems = fragment.getHorizontalListItems();
                    ArrayList<CustomListItem> chosenItems = fragment.getChosenItems();
                    MyRoomFragment newFragment = MyRoomFragment.newInstance();
                    newFragment.setChosenItems(chosenItems);
                    newFragment.setHorizontalListItems(horizontalListItems);
                    return newFragment;
                }*/

                Log.d(TAG, "loading MyRoomFragment.newInstance()");
                return MyRoomFragment.newInstance(); // TODO: PROBLEM
            case 1:
                if(this.numbOfTabs == 3) {
                    Log.d(TAG, "loading MyFurnitureFragment.newInstance()");
                    return MyFurnitureFragment.newInstance();
                } else {
                    Log.d(TAG, "loading MapFragment.newInstance()");
                    return MapFragment.newInstance();
                }
            default:
                Log.d(TAG, "loading MapFragment.newInstance()");
                return MapFragment.newInstance();
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return numbOfTabs;
    }
}