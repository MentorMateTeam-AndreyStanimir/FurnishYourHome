package com.project.furnishyourhome;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.furnishyourhome.adapters.CustomListAdapter;
import com.project.furnishyourhome.adapters.ViewPagerAdapter;
import com.project.furnishyourhome.dialogs.NetworkDialog;
import com.project.furnishyourhome.fragments.MyRoomFragment;
import com.project.furnishyourhome.fragments.NavDrawerRightFragment;
import com.project.furnishyourhome.interfaces.IGestureListener;
import com.project.furnishyourhome.interfaces.ISwipeable;
import com.project.furnishyourhome.materialdesign.SlidingTabLayout;
import com.project.furnishyourhome.models.CustomListItem;
import com.project.furnishyourhome.models.CustomViewPager;
import com.project.furnishyourhome.models.Furniture;
import com.project.furnishyourhome.models.SimpleGestureFilter;
import com.project.furnishyourhome.models.Sofa;
import com.project.furnishyourhome.models.Store;
import com.project.furnishyourhome.models.Table;
import com.project.furnishyourhome.models.parse.SofaParse;
import com.project.furnishyourhome.models.parse.StoreParse;
import com.project.furnishyourhome.models.parse.TableParse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, IGestureListener, ISwipeable{
    private static final String TAG = MainActivity.class.getSimpleName();

    //private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private Context context;
    private static boolean isFirstTime = true;
    private static ArrayList<Furniture> furnituresList;
    private static ArrayList<Sofa> sofasList;
    private static ArrayList<Table> tablesList;
    //private static ArrayList<Store> stores;
    private static ArrayList<CustomListItem> leftNavDrawerItems;
    private int selectedPosition;

    private DrawerLayout leftDrawerLayout;
    private ActionBarDrawerToggle leftDrawerListener;
    private ListView mDrawerLeftList;
    private SimpleGestureFilter detector;

    private Toolbar toolbar;

    private boolean swipeable;

    private ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            Log.d(TAG, "wifi is ON");
        } else {
            Log.d(TAG, "wifi is OFF");
            DialogFragment dialog = new NetworkDialog();
            dialog.show(getSupportFragmentManager(), "NetworkDialog");
        }

        if(isFirstTime && mWifi.isConnected()){
            loadData();
            isFirstTime = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        // Enable Local Datastore.
        if(isFirstTime) {
            Parse.enableLocalDatastore(this);
            leftNavDrawerItems = new ArrayList<>();
            furnituresList  = new ArrayList<>();
            sofasList       = new ArrayList<>();
            tablesList      = new ArrayList<>();
            //stores          = new ArrayList<>();
        }
        ParseObject.registerSubclass(SofaParse.class);
        ParseObject.registerSubclass(StoreParse.class);
        ParseObject.registerSubclass(TableParse.class);
        Parse.initialize(this, "ueFuNcN0Cx1xgBzycLJOgwqGqLwDzlt9zJEHulqJ", "s1vnSldgEhOfOMyBfIXSnKsl8F7YHuGNXisSr2jM");

        this.detector = new SimpleGestureFilter(this,this);
        this.swipeable = true;
        setCustomToolbar();

        this.setActionBarTabs();
        this.setLeftDrawer();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setCustomToolbar() {
        // Creating The Toolbar and setting it as the Toolbar for the activity
        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(getResources().getColor(android.R.color.holo_green_light));
        this.toolbar.setBackgroundColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        //for initializing right fragment
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.add(R.id.right_drawer, NavDrawerRightFragment.newInstance());
        tr.commit();
    }

    private void loadData() {
        (new GetAsyncResult()).execute();
    }

    private void setActionBarTabs() {

        int orientation = getResources().getConfiguration().orientation;
        int tabsNumber = 3;
        CharSequence[] titles = getResources().getStringArray(R.array.tabs_three);

        if(this.checkIsTablet() && orientation == Configuration.ORIENTATION_LANDSCAPE){
            tabsNumber = 2;
            getResources().getStringArray(R.array.tabs_two);
        }

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        ViewPagerAdapter adapterViewPager = new ViewPagerAdapter(this, getSupportFragmentManager(), titles, tabsNumber);

        // Assigning ViewPager View and setting the adapter
        CustomViewPager pager = (CustomViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapterViewPager);

        // Assiging the Sliding Tab Layout View
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
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

    private boolean checkIsTablet() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;
        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        double diagonalInches = Math.sqrt(
                (widthInches * widthInches) +
                        (heightInches * heightInches)
        );
        return (diagonalInches >= 6);
    }

    private void setLeftDrawer() {

        //Initialize left menu
        this.mDrawerLeftList = (ListView) findViewById(R.id.left_drawer);

        // adding header to listView
        View header = getLayoutInflater().inflate(R.layout.header, null);
        ImageView ivProfile =(ImageView)header.findViewById(R.id.profile_image);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        this.mDrawerLeftList.addHeaderView(header);

        // Set the adapter
        Log.d(TAG, "leftNavDrawerItems.size():"+leftNavDrawerItems.size());
        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.drawer_list_item, leftNavDrawerItems);
        mDrawerLeftList.setAdapter(adapter);
        mDrawerLeftList.setOnItemClickListener(this);

        // set left drawer layout
        leftDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        leftDrawerListener = new ActionBarDrawerToggle(this, leftDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        leftDrawerLayout.setDrawerListener(leftDrawerListener);
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
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearch(newText);
                return false;
            }
        });
        return true;
    }

    private void handleSearch(String query) {
        MyRoomFragment fragment = (MyRoomFragment) getSupportFragmentManager().findFragmentByTag("MyRoomFragment");
        Fragment.SavedState myFragmentState = getSupportFragmentManager().saveFragmentInstanceState(fragment);
        Bundle args = new Bundle();

        ArrayList<Furniture> tempList = new ArrayList<>();
        ArrayList<CustomListItem> tempListItems;

        switch (selectedPosition) {
            case 0:
                tempList = searchInFor(furnituresList, query);
                break;
            case 1:
                tempList = searchInFor(sofasList, query);
                break;
            case 2:
                tempList = searchInFor(tablesList, query);
                break;
        }
        tempListItems = convertFurnitureToListItem(tempList);
        args.putParcelableArrayList("horizontalListItems", tempListItems);

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        MyRoomFragment newFragment = MyRoomFragment.newInstance(args);
        newFragment.setInitialSavedState(myFragmentState);
        tr.replace(R.id.container_my_room, newFragment, "MyRoomFragment");
        tr.commit();
    }

    private ArrayList<Furniture> searchInFor(ArrayList<? extends Furniture> furniture, String query) {
        ArrayList<Furniture> list = new ArrayList<>();
        for (int i=0; i<furniture.size(); i++) {
            Furniture item = furniture.get(i);
            if(item.getName().toLowerCase().contains(query.toLowerCase())) {
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                onSearchRequested();
        }

        if (leftDrawerListener.onOptionsItemSelected(item)){ // TODO: WTF is this
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
            selectItem(position);
            leftDrawerLayout.closeDrawers();
        }
    }

    private void selectItem(int position) {
        selectedPosition = position;
        MyRoomFragment fragment = (MyRoomFragment) getSupportFragmentManager().findFragmentByTag("MyRoomFragment");
        Fragment.SavedState myFragmentState = getSupportFragmentManager().saveFragmentInstanceState(fragment);
        Bundle args = new Bundle();

        if(position == 0) {
            Toast.makeText(this, getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            setTitle(getResources().getString(R.string.app_name));
            ArrayList<CustomListItem> allListItems = convertFurnitureToListItem(furnituresList);
            args.putParcelableArrayList("horizontalListItems", allListItems);
        } else {
            Toast.makeText(this, leftNavDrawerItems.get(position - 1).getTitle(), Toast.LENGTH_SHORT).show();
            setTitle(leftNavDrawerItems.get(position - 1).getTitle());
            mDrawerLeftList.setItemChecked(position, true);
            switch (position) {
                case 1:
                    ArrayList<CustomListItem> sofasListItems = convertFurnitureToListItem(sofasList);
                    args.putParcelableArrayList("horizontalListItems", sofasListItems);
                    break;
                case 2:
                    ArrayList<CustomListItem> tablesListItems = convertFurnitureToListItem(tablesList);
                    args.putParcelableArrayList("horizontalListItems", tablesListItems);
                    break;
            }
        }

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        MyRoomFragment newFragment = MyRoomFragment.newInstance(args);
        newFragment.setInitialSavedState(myFragmentState);
        tr.replace(R.id.container_my_room, newFragment, "MyRoomFragment");
        tr.commit();
    }

    private ArrayList<CustomListItem> convertFurnitureToListItem(ArrayList<? extends Furniture> furniture) {
        ArrayList<CustomListItem> listItems = new ArrayList<>();
        for (int i = 0; i<furniture.size(); i++) {
            CustomListItem item = new CustomListItem();
            item.setTitle(furniture.get(i).getName());
            item.setBitmap(furniture.get(i).getDrawable());
            item.setPrice(furniture.get(i).getPrice());
            item.setDimensions(furniture.get(i).getDimensions());
            item.setMaterial(furniture.get(i).getMaterial());
            item.setInfo(furniture.get(i).getInfo());
            item.setStore(furniture.get(i).getStore());
            listItems.add(item);
        }
        return listItems;
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {

        if(swipeable) {
            getSupportActionBar().setShowHideAnimationEnabled(true);
            switch (direction) {
                case SimpleGestureFilter.SWIPE_DOWN:
                    getSupportActionBar().show();
                    break;
                case SimpleGestureFilter.SWIPE_UP:
                    //toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                    getSupportActionBar().hide();
                    break;
            }
        }
    }

   /* @Override
    public void onDoubleTap() {
        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }

    private class GetAsyncResult extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //menu items for left drawer
                final ParseQuery<ParseObject> typesQuery = ParseQuery.getQuery("Furniture");
                List<ParseObject> parseObjects = typesQuery.find();

                for (ParseObject obj : parseObjects) {
                    String type = obj.getString("type");
                    ParseFile imgParse = obj.getParseFile("icon");
                    byte[] imageByte = new byte[0];
                    try {
                        imageByte = imgParse.getData();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    Bitmap icon = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                    CustomListItem item = new CustomListItem(type, icon);
                    leftNavDrawerItems.add(item);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (CustomListItem item : leftNavDrawerItems) {
                String type = item.getTitle();

                //need to be added more tabels
                if (type.equals("Table")) {
                    final ParseQuery<TableParse> query = ParseQuery.getQuery(TableParse.class);
                    List<TableParse> tables = null;

                    try {
                        tables = query.find();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (tables != null) {
                        for (TableParse table : tables) {
                            furnituresList.add(table.getTable());
                            tablesList.add(table.getTable());
                        }
                    }
                } else if (type.equals("Sofa")) {
                    final ParseQuery<SofaParse> query = ParseQuery.getQuery(SofaParse.class);
                    List<SofaParse> sofas = null;

                    try {
                        sofas = query.find();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (sofas != null) {
                        for (SofaParse sofa : sofas) {
                            furnituresList.add(sofa.getSofa());
                            sofasList.add(sofa.getSofa());
                        }
                    }
                }
            }

            //items in STORE table
            /*final ParseQuery<StoreParse> storeQuery = ParseQuery.getQuery(StoreParse.class);
            List<StoreParse> storesList = null;
            try {
                storesList = storeQuery.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(storesList != null) {
                for (StoreParse store : storesList) {
                    stores.add(store.getStore());
                }
            }*/

            Log.d(TAG, "Success");
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Loading data");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            Log.d(TAG, "loading data");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Bundle args = new Bundle();
            ArrayList<CustomListItem> listItems = convertFurnitureToListItem(furnituresList);
            args.putParcelableArrayList("horizontalListItems", listItems);

            Log.d(TAG, "loading MyRoomFragment.newInstance(args)");
            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.container_my_room, MyRoomFragment.newInstance(args), "MyRoomFragment");
            tr.commit();

            progressDialog.dismiss();

        }
    }
}
