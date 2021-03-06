package com.example.geofencing;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class Check_Recent_Apps extends Service {
    private static final String TAG = "Check_Recent_Apps";
    TimerTask timerTask;
    Timer timer;
    Intent displayBlockScreen;
    List<ApplicationInfo> apps;
    PackageManager pm;

    @Override
    public void onCreate() {
        super.onCreate();
        displayBlockScreen = new Intent(getApplicationContext(), SplashScreen.class);
        displayBlockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        pm = getPackageManager();
        apps = pm.getInstalledApplications(0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: " + getAppPackageName("Phone"));
        Log.d(TAG, "onStartCommand: " + getAppPackageName("Camera"));
        List<ApplicationInfo> installedApps = getAppsOnPhone();
        for (ApplicationInfo app : installedApps) {
            //Log.d(TAG, "onStartCommand: " + app.packageName);
        }

        clearTimerSchedule();
        timerTask = new Lock_App();
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 1000, 10);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        clearTimerSchedule();
        super.onDestroy();
    }

    private void clearTimerSchedule() {
        if (timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String getCurrentRunningApp(Context context) {
        String currentApp = "";
        UsageStatsManager usageStatsManager;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            usageStatsManager =
                    (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            if (usageStatsManager != null) {
                long now = System.currentTimeMillis();
                List<UsageStats> appList =
                        usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                now - 1000 * 1000, now);
                // Log.d(TAG, "Running app number in last 10 minutes : " + appList.size());
                // Get a app, that is currently running app recently running
                if ((appList != null) && (!appList.isEmpty())) {
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                    for (UsageStats usageStats : appList) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (!mySortedMap.isEmpty()) {
                        currentApp =
                                Objects.requireNonNull(mySortedMap.get(mySortedMap.lastKey())).getPackageName();
                    }
                }
            }
        }
        return currentApp;
    }

    private List<ApplicationInfo> getAppsOnPhone() {
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo app : apps) {
            //checks for flags; if flagged, check if updated system app
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app);
                //it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                installedApps.add(app);
                //in this case, it should be a user-installed app
            } else {
                installedApps.add(app);
            }
        }
        return installedApps;
    }

    public String getAppPackageName(String app) {
        String packageName = "";

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities(mainIntent
                , 0);
        for (ResolveInfo resolveInfo : pkgAppsList) {
            try {
                ApplicationInfo applicationInfo =
                        packageManager
                                .getApplicationInfo(resolveInfo.activityInfo.packageName, 0);
                if (app.contentEquals(packageManager.getApplicationLabel(applicationInfo))) {
                    packageName = resolveInfo.activityInfo.packageName;
                }
                 /*Log.d(TAG, "App Name: "+this.getPackageManager().getApplicationLabel
                  (applicationInfo));*/
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "getAppPackageName: " + resolveInfo.activityInfo.packageName);
        }
        return packageName;
    }

    public class Lock_App extends TimerTask {

        @Override
        public void run() {
            String currentRunningApp = getCurrentRunningApp(getApplicationContext());

            if (!currentRunningApp.isEmpty()) {

                if (getAppPackageName("Camera").equals(currentRunningApp)) {
                    //startActivity(displayBlockScreen);
                    Log.d(TAG, "run: camera is open");
                    sendBroadcast(new Intent("com.example.geofencing.ActiveApps"));
                }
                if ("com.oneplus.camera".equals(currentRunningApp)) {
                    //startActivity(displayBlockScreen);
                    Log.d(TAG, "run: camera is open");
                    sendBroadcast(new Intent("com.example.geofencing.ActiveApps"));
                }
                if (getAppPackageName("Dialer").equals(currentRunningApp)) {
                    //startActivity(displayBlockScreen);
                    Log.d(TAG, "run: camera is open");
                    sendBroadcast(new Intent("com.example.geofencing.ActiveApps"));
                }
            }
        }
    }
}