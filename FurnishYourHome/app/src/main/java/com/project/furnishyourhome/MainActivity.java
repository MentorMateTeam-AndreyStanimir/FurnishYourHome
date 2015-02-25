package com.project.furnishyourhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import com.project.furnishyourhome.database.FYHApp;
import com.project.furnishyourhome.fragments.MyRoomFragment;
import com.project.furnishyourhome.fragments.NavDrawerRightFragment;
import com.project.furnishyourhome.interfaces.DbTableNames;
import com.project.furnishyourhome.interfaces.IGestureListener;
import com.project.furnishyourhome.interfaces.ISwipeable;
import com.project.furnishyourhome.materialdesign.SlidingTabLayout;
import com.project.furnishyourhome.models.CustomListItem;
import com.project.furnishyourhome.models.CustomViewPager;
import com.project.furnishyourhome.models.Furniture;
import com.project.furnishyourhome.models.HolderCount;
import com.project.furnishyourhome.models.SimpleGestureFilter;
import com.project.furnishyourhome.models.Sofa;
import com.project.furnishyourhome.models.Table;
import com.project.furnishyourhome.models.Type;
import com.project.furnishyourhome.models.parse.FurnitureParse;
import com.project.furnishyourhome.models.parse.StoreParse;
import com.project.furnishyourhome.services.DataCountService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, IGestureListener, ISwipeable, DbTableNames {
	private static final String TAG = MainActivity.class.getSimpleName();

    private static boolean isFirstTime = true;
    private static ArrayList<Furniture> furnitures;
    private static HashMap<String, ArrayList<Furniture>> furnitureLists;
    private static ArrayList<CustomListItem> leftNavDrawerItems;

    private Context context;
    private int selectedPosition;
    private DrawerLayout leftDrawerLayout;
    private ActionBarDrawerToggle leftDrawerListener;
    private ListView mDrawerLeftList;
    private SimpleGestureFilter detector;
    private TaskUpdateList taskUpdateList;
    private Handler updateListHandler;
    private HolderCount holderCount = new HolderCount();
    private int countDataOnServer = 0;
    public ArrayList<Type> types;

    private Toolbar toolbar;
    ViewPagerAdapter adapterViewPager;
    CustomViewPager pager;

    private boolean swipeable;

    private ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(wifi.isWifiEnabled()) {
            Log.d(TAG, "wifi is ON");
            if(isFirstTime){
                loadData(false);
                isFirstTime = false;
            }
        } else {
            Log.d(TAG, "wifi is OFF");
            showWiFiDisabledAlertToUser();
        }
    }

    private void showWiFiDisabledAlertToUser(){
        Log.d(TAG, "showGPSDisabledAlertToUser");
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Base_Theme_AppCompat_Dialog));
        builder.setTitle("Network connectivity");
        builder.setMessage("Your WiFi is OFF, do you want to turn it ON ?");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 1);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            onResume();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Display density: "+getResources().getDisplayMetrics().density);

        this.context = this;

        // Enable Local Datastore.
        if (isFirstTime) {
            Parse.enableLocalDatastore(this);
            leftNavDrawerItems = new ArrayList<>();
            furnitures = new ArrayList<>();
            furnitureLists = new HashMap<>();
            //stores          = new ArrayList<>();
        }
        ParseObject.registerSubclass(StoreParse.class);
        ParseObject.registerSubclass(FurnitureParse.class);
        Parse.initialize(this, getResources().getString(R.string.app_id), getResources().getString(R.string.app_key));

        this.detector = new SimpleGestureFilter(this, this);
        this.swipeable = true;
        setCustomToolbar();

        this.setActionBarTabs();
        this.setLeftDrawer();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Start service data counter
        taskUpdateList = new TaskUpdateList();
        updateListHandler = new Handler();
        updateListHandler.postDelayed(taskUpdateList, 30000);

        resultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(this, DataCountService.class);
        intent.putExtra("receiver", resultReceiver);
        startService(intent);
    }

    private class TaskUpdateList implements Runnable {

        @Override
        public void run() {
            if (holderCount.count != countDataOnServer) {
                Log.d(TAG, "checkServerCount: "+countDataOnServer);
                Log.d(TAG, "holderCount.count: "+holderCount.count);
                Log.d(TAG, "downloading data");
                loadData(true);
                Toast.makeText(context, "The data was updated", Toast.LENGTH_LONG).show();
            }
            updateListHandler.postDelayed(taskUpdateList, 5000);
        }
    }

    MyResultReceiver resultReceiver;
    private DataCountService dataService;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            DataCountService.MyBinder dataBinder = (DataCountService.MyBinder) binder;
            dataService = dataBinder.getService();
            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            dataService = null;
        }
    };

    class UpdateUI implements Runnable {
        public UpdateUI(int count) {
            countDataOnServer = count;
        }

        public void run() {
            Log.d(TAG, "activityCount: "+countDataOnServer);
        }
    }

    private class MyResultReceiver extends ResultReceiver {
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 100) {
                try {
                    ((Activity) context).runOnUiThread(new UpdateUI(resultData.getInt("count")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setCustomToolbar() {
        // Creating The Toolbar and setting it as the Toolbar for the activity
        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(getResources().getColor(R.color.TextColor));
        this.toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
        setSupportActionBar(toolbar);

        //for initializing right fragment
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.add(R.id.right_drawer, NavDrawerRightFragment.newInstance());
        tr.commit();
    }

    private void loadData(boolean isFromInternet) {
        (new GetAsyncResult(isFromInternet)).execute();
    }

    private void setActionBarTabs() {

        int orientation = getResources().getConfiguration().orientation;
        int tabsNumber = 3;
        CharSequence[] titles = getResources().getStringArray(R.array.tabs_three);

        if (this.checkIsTablet() && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tabsNumber = 2;
            getResources().getStringArray(R.array.tabs_two);
        }

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterViewPager = new ViewPagerAdapter(this, getSupportFragmentManager(), titles, tabsNumber);

        // Assigning ViewPager View and setting the adapter
        pager = (CustomViewPager) findViewById(R.id.pager);
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
        ImageView ivProfile = (ImageView) header.findViewById(R.id.profile_image);

        if(this.mDrawerLeftList.getHeaderViewsCount() == 0) {
            this.mDrawerLeftList.addHeaderView(header);
        }

        // Set the adapter
        //TODO: bug initial size 4
        Log.d(TAG, "leftNavDrawerItems.size(): "+leftNavDrawerItems.size());
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

        ArrayList<Furniture> tempList;
        ArrayList<CustomListItem> tempListItems;

        if(selectedPosition == 0){
            tempList = searchInFor(furnitures, query);
        } else {
            String type = leftNavDrawerItems.get(selectedPosition - 1).getTitle();
            tempList = searchInFor(furnitureLists.get(type), query);
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
        for (int i = 0; i < furniture.size(); i++) {
            Furniture item = furniture.get(i);
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
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
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_search:
                onSearchRequested();
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
        if (parent.getId() == R.id.left_drawer) {
            selectItem(position);
            leftDrawerLayout.closeDrawers();
        }
    }

    private void selectItem(int position) {
        selectedPosition = position;
        MyRoomFragment fragment = (MyRoomFragment) getSupportFragmentManager().findFragmentByTag("MyRoomFragment");
        Fragment.SavedState myFragmentState = getSupportFragmentManager().saveFragmentInstanceState(fragment);//TODO: bug when resuming
        Bundle args = new Bundle();

        if (position == 0) {
            Toast.makeText(this, getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            setTitle(getResources().getString(R.string.app_name));
            ArrayList<CustomListItem> allListItems = convertFurnitureToListItem(furnitures);
            args.putParcelableArrayList("horizontalListItems", allListItems);
        } else {
            Toast.makeText(this, leftNavDrawerItems.get(position - 1).getTitle(), Toast.LENGTH_SHORT).show();
            setTitle(leftNavDrawerItems.get(position - 1).getTitle());
            mDrawerLeftList.setItemChecked(position, true);

            ArrayList<CustomListItem> itemsToShow;

            // get furniture type
            String type = leftNavDrawerItems.get(position - 1).getTitle();
            // get elements for this type
            itemsToShow = convertFurnitureToListItem(furnitureLists.get(type));

            args.putParcelableArrayList("horizontalListItems", itemsToShow);
        }

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        MyRoomFragment newFragment = MyRoomFragment.newInstance(args);
        newFragment.setInitialSavedState(myFragmentState);
        tr.replace(R.id.container_my_room, newFragment, "MyRoomFragment");
        tr.commit();
    }

    private ArrayList<CustomListItem> convertFurnitureToListItem(ArrayList<? extends Furniture> furniture) {
        ArrayList<CustomListItem> listItems = new ArrayList<>();
        for (int i = 0; i < furniture.size(); i++) {
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
    public boolean dispatchTouchEvent(@NonNull MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {

        if (swipeable) {
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

    @Override
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }

    private void downloadData() {
        try {
            types = new ArrayList<>();
            leftNavDrawerItems = new ArrayList<>();
            //menu items for left drawer
            final ParseQuery<ParseObject> typesQuery = ParseQuery.getQuery("Furniture");
            List<ParseObject> parseObjects = typesQuery.find();

            for (ParseObject obj : parseObjects) {
                String type = obj.getString("type");
                String id = obj.getObjectId();
                ParseFile imgParse = obj.getParseFile("icon");
                byte[] imageByte = new byte[0];
                try {
                    imageByte = imgParse.getData();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                Bitmap icon = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                CustomListItem item = new CustomListItem(type, icon);

                //need for DB update
                Type typeItem = new Type();
                typeItem.setId(id);
                typeItem.setType(type);
                typeItem.setBitmap(icon);
                this.types.add(typeItem);

                leftNavDrawerItems.add(item);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final ParseQuery<FurnitureParse> furnitureItems = ParseQuery.getQuery(FurnitureParse.class);
        List<FurnitureParse> fItems = null;

        try {
            fItems = furnitureItems.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        furnitures = new ArrayList<>();
        furnitureLists = new HashMap<>();
        if (fItems != null) {
            for (FurnitureParse fItem : fItems) {
                String type = fItem.getType();

                // Now SofaParse and TableParse became useless
                if(type.equals("Table")){

                    Table table = fItem.getTable();
                    furnitures.add(table);

                    initializeHashMapKey("Table");
                    furnitureLists.get("Table").add(table);
                }else if (type.equals("Sofa")) {

                    Sofa sofa = fItem.getSofa();
                    furnitures.add(sofa);

                    initializeHashMapKey("Sofa");
                    furnitureLists.get("Sofa").add(sofa);
                }
            }
        }
    }

    private void saveDataAfterDownloading() {
        Bundle args = new Bundle();
        ArrayList<CustomListItem> listItems = convertFurnitureToListItem(furnitures);
        args.putParcelableArrayList("horizontalListItems", listItems);

        Log.d(TAG, "loading MyRoomFragment.newInstance(args)");
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.container_my_room, MyRoomFragment.newInstance(args), "MyRoomFragment");
        tr.commit();   //TODO: Е те тука гърми (понякога).
    }

    private void showProgressDialog() {
       	progressDialog = new ProgressDialog(context, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        progressDialog.setTitle("Loading data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        Log.d(TAG, "loading data");
        progressDialog.show();
    }

    private class GetAsyncResult extends AsyncTask<Void, Void, Void> {

        boolean isFromInternet = false;

        private GetAsyncResult(boolean isFromInternet) {
            this.isFromInternet = isFromInternet;
        }

        @Override
        protected Void doInBackground(Void... params) {

            if(this.isFromInternet) {
                downloadData();
            } else {
                if (((FYHApp) getApplication()).getUtilitiesDb().isDbEmpty()) {
                    this.isFromInternet = true;
                    downloadData();
                } else {
                    holderCount.count = ((FYHApp) getApplication()).getUtilitiesDb().getTableCount(TABLE_FURNITURES);
                    countDataOnServer = holderCount.count;
                    loadDataFromDb();
                }
            }
            Log.d(TAG, "Success");
            return null;
        }

        @Override
        protected void onPreExecute() {
            if(!this.isFromInternet) {
                showProgressDialog();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            saveDataAfterDownloading();
            setLeftDrawer();

            if(this.isFromInternet) {
                saveDataInDb();
            } else {
                progressDialog.dismiss();
            }
        }
    }

    private void saveDataInDb() {

        int itemsCountInDb = ((FYHApp) getApplication()).getUtilitiesDb().getTableCount(TABLE_FURNITURES);

        //if items in Database are more than the items from internet then clean all data
        if(itemsCountInDb > furnitures.size()) {
            ((FYHApp) getApplication()).getUtilitiesDb().deleteTable(TABLE_FURNITURES);
            ((FYHApp) getApplication()).getUtilitiesDb().deleteTable(TABLE_TYPES);
        }

        if (itemsCountInDb < furnitures.size()) {
                boolean isSavedTypesIntoDB = ((FYHApp) getApplication()).getUtilitiesDb().addTypes(types);
                if (isSavedTypesIntoDB) {
                    Log.d("Database", "Types saved into DB");
                } else {
                    Log.d("Database", "Something went wrong (types)");
                }
                boolean isSavedItemsIntoDB = ((FYHApp) getApplication()).getUtilitiesDb().addItems(furnitures);
                if (isSavedItemsIntoDB) {
                    Log.d("Database", "Items saved into DB");
                } else {
                    Log.d("Database", "Something went wrong (items)");
                }
            }

        holderCount.count = furnitures.size();
    }

    private void loadDataFromDb() {
        ArrayList<Type> typesFromDB = ((FYHApp) getApplication()).getUtilitiesDb().getTypes();
        ArrayList<Furniture> itemsFromDB = ((FYHApp) getApplication()).getUtilitiesDb().getAllItems();

        leftNavDrawerItems = new ArrayList<>();
        furnitureLists = new HashMap<>();

        for (Type type : typesFromDB) {

            CustomListItem item = new CustomListItem(type.getType(), type.getBitmap());

            leftNavDrawerItems.add(item);
        }

        furnitures = itemsFromDB;
        for (Furniture fItem : itemsFromDB) {

            if(fItem instanceof Table){
                initializeHashMapKey("Table");

                furnitureLists.get("Table").add(fItem);
            } else if (fItem instanceof Sofa) {
                initializeHashMapKey("Sofa");

                furnitureLists.get("Sofa").add(fItem);
            }
        }
    }

    private void initializeHashMapKey(String key) {
        if(!furnitureLists.containsKey(key)){
            furnitureLists.put(key, new ArrayList<Furniture>());
        }
    }
}
