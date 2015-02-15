package com.project.furnishyourhome;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.adapters.NavDrawerAdapter;
import com.project.furnishyourhome.adapters.ViewPagerAdapter;
import com.project.furnishyourhome.materialdesign.SlidingTabLayout;
import com.project.furnishyourhome.models.CustomListItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private final int Numboftabs = 3;

    private DrawerLayout leftDrawerLayout;
    private ActionBarDrawerToggle leftDrawerListener;
    private RecyclerView mRecyclerView;                           // Declaring RecyclerView
    private RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    private RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager

    private ListView mDrawerLeftList;
    private ListView mDrawerRightList;
    private String[] mLeftDrawerMenu;
    private String[] mRightDrawerMenu;
    private ArrayList<CustomListItem> leftNavDrawerItems;
    private CustomListAdapter adapter;

    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapterViewPager;
    private SlidingTabLayout tabs;


    private boolean mTwoPane; // check is in Landscape mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionBarTabs();

        setLeftDrawer();
    }

    private void setActionBarTabs() {
        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.holo_green_light));
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterViewPager =  new ViewPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tabs), Numboftabs);

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

    private void setLeftDrawer() {

        this.mLeftDrawerMenu = new String[]{"Home", "TVs", "Laptops", "Sofas", "Chairs", "Chandeliers"};  // TODO: get menu from DB
        int icons[] = {android.R.drawable.star_big_on,android.R.drawable.star_big_on,android.R.drawable.star_big_on,android.R.drawable.star_big_on,android.R.drawable.star_big_on,android.R.drawable.star_big_on};

        String name = "Name Name";
        String email = "email@email.com";
        int profile = android.R.drawable.star_big_on;

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new NavDrawerAdapter(mLeftDrawerMenu,icons,name,email,profile);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        leftDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
        leftDrawerListener = new ActionBarDrawerToggle(this,leftDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        leftDrawerLayout.setDrawerListener(leftDrawerListener); // Drawer Listener set to the Drawer toggle
        leftDrawerListener.syncState();               // Finally we set the drawer toggle sync State

        //Initialize left menu
//        this.mDrawerLeftList = (ListView) findViewById(R.id.left_drawer);
//
//        this.leftNavDrawerItems = new ArrayList<CustomListItem>();
//        for (String title : mLeftDrawerMenu) {
//            leftNavDrawerItems.add(new CustomListItem(title, R.drawable.ic_home));
//        }
//
//        // Set the adapter
//        adapter = new CustomListAdapter(this, leftNavDrawerItems);
//        mDrawerLeftList.setAdapter(adapter);
//        mDrawerLeftList.setOnItemClickListener(MainActivity.this);
//
//        // set left drawer layout
//        leftDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        leftDrawerListener = new ActionBarDrawerToggle(this, leftDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
//
//        leftDrawerLayout.setDrawerListener(leftDrawerListener);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

//        if (leftDrawerListener.onOptionsItemSelected(item)){
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        leftDrawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        RecyclerView does not have onItemClickListener(). Because RecyclerView extends android.view.ViewGroup
//        and ListView extends android.widget.AbsListView
//        that's why we implement onClick in our RecyclerView Adapter:

//        if(parent.getId() == R.id.left_drawer) {
//            Toast.makeText(this, mLeftDrawerMenu[position], Toast.LENGTH_SHORT).show(); //TODO: Change Custom list items
//
//            selectItem(position);
//            leftDrawerLayout.closeDrawers();
//        }
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
