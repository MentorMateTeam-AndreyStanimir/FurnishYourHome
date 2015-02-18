package com.project.furnishyourhome;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.adapters.ViewPagerAdapter;
import com.project.furnishyourhome.interfaces.IGestureListener;
import com.project.furnishyourhome.interfaces.ISwipeable;
import com.project.furnishyourhome.materialdesign.SlidingTabLayout;
import com.project.furnishyourhome.models.CustomListItem;
import com.project.furnishyourhome.models.CustomViewPager;
import com.project.furnishyourhome.models.SimpleGestureFilter;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, IGestureListener, ISwipeable {

    private final int Numboftabs = 3;

    private DrawerLayout leftDrawerLayout;
    private ActionBarDrawerToggle leftDrawerListener;
    private ListView mDrawerLeftList;
    private ListView mDrawerRightList;
    private String[] mLeftDrawerMenu;
    private ArrayList<CustomListItem> leftNavDrawerItems;
    private CustomListAdapter adapter;
    private SimpleGestureFilter detector;

    private Toolbar toolbar;
    private CustomViewPager pager;
    private ViewPagerAdapter adapterViewPager;
    private SlidingTabLayout tabs;

    private boolean mTwoPane; // check is in Landscape mode
    private boolean swipeable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.detector = new SimpleGestureFilter(this,this);
        this.swipeable = true;

        // Creating The Toolbar and setting it as the Toolbar for the activity
        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(getResources().getColor(android.R.color.holo_green_light));
        this.toolbar.setBackgroundColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        this.setActionBarTabs();
        this.setLeftDrawer();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setActionBarTabs() {

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterViewPager =  new ViewPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tabs), Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (CustomViewPager) findViewById(R.id.pager);
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

    private void setLeftDrawer() {

        this.mLeftDrawerMenu = new String[]{"All", "TVs", "Laptops", "Sofas", "Chairs", "Chandeliers"};  // TODO: get menu from DB

        //Initialize left menu
        this.mDrawerLeftList = (ListView) findViewById(R.id.left_drawer);

        // adding header to listView
        View header=getLayoutInflater().inflate(R.layout.header, null);
        ImageView pro=(ImageView)header.findViewById(R.id.profile_image);
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Clicked",                                                                                    Toast.LENGTH_SHORT).show();
            }
        });
        this.mDrawerLeftList.addHeaderView(header);

        // fill ArrayList with data
        this.leftNavDrawerItems = new ArrayList<CustomListItem>();
        for (String title : mLeftDrawerMenu) {
            leftNavDrawerItems.add(new CustomListItem(title, R.drawable.ic_home));
        }

        // Set the adapter
        adapter = new CustomListAdapter(this, leftNavDrawerItems);
        mDrawerLeftList.setAdapter(adapter);
        mDrawerLeftList.setOnItemClickListener(MainActivity.this);

        // set left drawer layout
        leftDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        leftDrawerListener = new ActionBarDrawerToggle(this, leftDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        leftDrawerLayout.setDrawerListener(leftDrawerListener);
    }

    private void handleSearch(String query) {
        //doMySearch(query); //ToDo:
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Shows Action bar icon
        leftDrawerListener.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //handleSearch(newText);
                return false;
            }
        });

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
        else if(id == R.id.action_search){
            onSearchRequested();
        }

        if (leftDrawerListener.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        leftDrawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.left_drawer) {

            //TODO: Change onClick event
            if(position == 0){
                Toast.makeText(this, getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, mLeftDrawerMenu[position -1], Toast.LENGTH_SHORT).show();
            }

            selectItem(position);
            leftDrawerLayout.closeDrawers();
        }
    }

    private void selectItem(int position) {
        mDrawerLeftList.setItemChecked(position, true);

        if(position == 0){
            setTitle(getResources().getString(R.string.app_name));
        } else {
            setTitle(mLeftDrawerMenu[position - 1]);
//        getSupportActionBar().setTitle(mLeftDrawerMenu[position]);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {

        if(swipeable) {
            switch (direction) {
                case SimpleGestureFilter.SWIPE_DOWN:
                    getSupportActionBar().show();
                    break;
                case SimpleGestureFilter.SWIPE_UP:
                    getSupportActionBar().hide();
                    break;
            }
        }
    }

    @Override
    public void onDoubleTap() {
        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }
}
