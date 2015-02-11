package com.project.furnishyourhome;

import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.furnishyourhome.adapters.NavDrawerListAdapter;
import com.project.furnishyourhome.adapters.ViewPagerAdapter;
import com.project.furnishyourhome.materialdesign.SlidingTabLayout;
import com.project.furnishyourhome.models.NavDrawerItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private final CharSequence Titles[]={"Dimensions","My Room", "My furniture"};
    private final int Numboftabs =3;

    private DrawerLayout mDrawerLayoutLeft;
    private DrawerLayout mDrawerLayoutRight;
    private ActionBarDrawerToggle drawerListener;

    private ListView mDrawerLeftList;
    private ListView mDrawerRightList;
    private String[] mLeftDrawerMenu;
    private String[] mRightDrawerMenu;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapterViewPager;
    private SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDrawerMenu();

        mDrawerLayoutLeft = (DrawerLayout) findViewById(R.id.drawer_layout);

        //TODO: android.support.v7.app.ActionBarDrawerToggle; because now is deprecated
        drawerListener = new ActionBarDrawerToggle(this, mDrawerLayoutLeft, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//            }
        };

        mDrawerLayoutLeft.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setActionBarTabs();

        // Right drawer
//        mDrawerLayoutRight = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerRightList = (ListView) findViewById(R.id.right_drawer);
//
//        // Set the adapter for the list view
//        mDrawerLeftList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.support_simple_spinner_dropdown_item, mRightDrawerMenu));
    }

    private void setActionBarTabs() {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterViewPager =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapterViewPager);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    private void setDrawerMenu() {

        //Initialize left menu
        this.mDrawerLeftList = (ListView) findViewById(R.id.left_drawer);

        this.mLeftDrawerMenu = new String[]{"Home", "TVs", "Laptops", "Sofas", "Chairs", "Chandeliers"};  // TODO: get menu from DB
//        mRightDrawerMenu = new String[]{"4", "5", "6"};

        this.navDrawerItems = new ArrayList<NavDrawerItem>();

        for (String title : mLeftDrawerMenu) {
            navDrawerItems.add(new NavDrawerItem(title, R.drawable.ic_home));
        }

        // Set the adapter
        adapter = new NavDrawerListAdapter(this, navDrawerItems);
        mDrawerLeftList.setAdapter(adapter);
        mDrawerLeftList.setOnItemClickListener(MainActivity.this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Shows Action bar icon
        drawerListener.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (drawerListener.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.left_drawer) {
            Toast.makeText(this, mLeftDrawerMenu[position], Toast.LENGTH_SHORT).show(); //TODO: Change Custom list items

            selectItem(position);
            mDrawerLayoutLeft.closeDrawers();
        }
    }

    private void selectItem(int position) {
        mDrawerLeftList.setItemChecked(position, true);

        if(position == 0){
            setTitle(getResources().getString(R.string.app_name));
        } else {
            setTitle(mLeftDrawerMenu[position]);
//        getSupportActionBar().setTitle(mLeftDrawerMenu[position]);
        }
    }
}
