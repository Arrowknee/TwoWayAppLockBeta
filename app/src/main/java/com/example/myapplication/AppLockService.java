package com.example.myapplication;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;

import java.util.List;
//OUTDATED: ActivityManager and UsageMonitor DOES NOT allow the app to see other app processes clearly,
// especially since Android 27.
public class AppLockService extends Service {

    private static final long CHECK_INTERVAL = 1000;
    private Handler mHandler;
    private List<String> mLockedApps;

    private final Runnable mCheckRunnable = new Runnable() {
        @Override
        public void run() {
            String foregroundApp = getForegroundApp();
            // Prevent service from locking itself and creating a loop
            if (foregroundApp != null && !foregroundApp.equals(getPackageName()) && mLockedApps != null && mLockedApps.contains(foregroundApp)) {
                System.out.println("Attempt to lock " + foregroundApp);
                Intent intent = new Intent(AppLockService.this, LockScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            mHandler.postDelayed(this, CHECK_INTERVAL);
            System.out.println("Current app: " + (foregroundApp != null ? foregroundApp : "No App"));
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mLockedApps = intent.getStringArrayListExtra("lockedApps");
        }
        mHandler = new Handler();
        mHandler.post(mCheckRunnable);
        return START_STICKY;
    }

    private String getForegroundApp() {
        String foregroundApp = null;
        long latestTimestamp = 0;
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();

        // Query for events in the last 5 minutes to catch recent transitions
        UsageEvents usageEvents = usageStatsManager.queryEvents(currentTime - 1000 * 60 * 5, currentTime);
        UsageEvents.Event event = new UsageEvents.Event();
        if(usageEvents == null){
            System.out.println("Null usage events");
            return null;
        }
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            System.out.println("Event packageName: " + event.getPackageName());
//            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
//                if (event.getTimeStamp() > latestTimestamp) {
//                    latestTimestamp = event.getTimeStamp();
//                    foregroundApp = event.getPackageName();
//                }
//            }
        }

//        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


        return foregroundApp;
    }
}