package com.project.furnishyourhome.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.project.furnishyourhome.MainActivity;
import com.project.furnishyourhome.R;
import com.project.furnishyourhome.models.parse.FurnitureParse;

import java.util.Timer;
import java.util.TimerTask;


public class DataCountService extends Service {

    private final IBinder mBinder = new MyBinder();
    private Handler serviceHandler;
    private Task myTask = new Task();
    private TaskUpdate taskUpdate = new TaskUpdate();
    private int count = 0;
    private boolean isUpdated = false;
    private ResultReceiver resultReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public DataCountService getService() {
            return DataCountService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, getResources().getString(R.string.app_id), getResources().getString(R.string.app_key));
        Log.d("StartService", "counter");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        resultReceiver = intent.getParcelableExtra("receiver");
        serviceHandler = new Handler();
        serviceHandler.postDelayed(taskUpdate, 10000L);

        //Declare the timer
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                      Thread thread = new Thread(new Task());
                                      thread.start();
                                  }

                              },
         //Set how long before to start calling the TimerTask (in milliseconds)
                0,
        //Set the amount of time between each execution (in milliseconds)
                60000);

        return Service.START_NOT_STICKY;
    }

    class TaskUpdate implements Runnable {

        @Override
        public void run() {
            if (isUpdated) {
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                Log.d("count", count + "");
                isUpdated = false;
                Log.d("dad", "dadad");
                resultReceiver.send(100, bundle);
            }
            serviceHandler.postDelayed(taskUpdate, 10000L);
        }

    }

    class Task implements Runnable {

        @Override
        public void run() {
            final ParseQuery<FurnitureParse> query = ParseQuery.getQuery(FurnitureParse.class);

            try {
                count = query.count();
                isUpdated = true;

//                try {
//                    Thread.sleep(60000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}