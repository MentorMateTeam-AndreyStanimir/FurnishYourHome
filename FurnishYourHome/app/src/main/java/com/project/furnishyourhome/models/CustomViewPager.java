package com.project.furnishyourhome.models;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.project.furnishyourhome.R;
import com.project.furnishyourhome.interfaces.ISwipeable;

/**
 * Created by Andrey on 18.2.2015 г..
 */
public class CustomViewPager extends ViewPager implements ISwipeable {
    private boolean swipeable;
    private static CustomViewPager instance = null;
    private Context context;

    private Toolbar toolbar;

    public CustomViewPager(Context context) {
        super(context);

        initializeElements(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);


        initializeElements(context);
    }

    private void initializeElements(Context context) {
        this.context = context;
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.swipeable = true;
        instance = this;
    }

    public static CustomViewPager getInstanceIfExist() throws Exception {
        if(instance == null){
            throw new Exception("Not available view pager instance");
        }

        return instance;
    }

    // Call this method in your motion events when you want to disable or enable
    // It should work as desired.
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return (this.swipeable) ? super.onInterceptTouchEvent(arg0) : false;
    }
}
