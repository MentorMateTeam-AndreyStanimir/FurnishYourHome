package com.project.furnishyourhome.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.furnishyourhome.fragments.MapFragment;
import com.project.furnishyourhome.fragments.MyFurnitureFragment;
import com.project.furnishyourhome.fragments.MyRoomFragment;

/**
 * Created by Andrey on 11.2.2015 г..
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int numbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);

        this.Titles = mTitles;
        this.numbOfTabs = mNumbOfTabs;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            MyRoomFragment myRoomFragment = MyRoomFragment.newInstance();
            return myRoomFragment;
        }
        else if(position == 1)
        {
           /* //debug
            ArrayList<CustomListItem> list = new ArrayList<>();
            list.add(new CustomListItem("table1", R.drawable.ic_launcher));
            list.add(new CustomListItem("table2", R.drawable.ic_launcher));
            list.add(new CustomListItem("table3", R.drawable.ic_launcher));
            Bundle b = new Bundle();
            b.putParcelableArrayList("chosenItems", list);
            MyFurnitureFragment myFurnitureFragment = MyFurnitureFragment.newInstance(b);
            //#############################################################*/

            Fragment fragment = null;

            if(this.numbOfTabs == 3) {
                fragment = MyFurnitureFragment.newInstance();
            } else {
                fragment = MapFragment.newInstance();
            }
            return fragment;
        }
        else            // As we are having 3 tabs if the position is not 0 or 1 it must be 2 so we are returning third tab
        {
            MapFragment mapFragment = MapFragment.newInstance();
            return mapFragment;
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