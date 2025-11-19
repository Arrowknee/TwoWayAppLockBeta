package com.example.myapplication;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    RecyclerView app_list;
    FloatingActionButton ok_btn;
    AppListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test21);
        app_list = findViewById(R.id.app_list);
        ok_btn = findViewById(R.id.ok_btn);
        app_list.setLayoutManager(new LinearLayoutManager(this));
        requestPermissions();
        loadAppList();

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLockedApps();
//                setContentView(R.layout.main_app_layout);
                if (!isAccessibilityServiceEnabled()) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }

            }
        });

//        Button unlockAll = findViewById(R.id.unlock_button);
//        unlockAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                for(AppInfo appInfo : adapter.getAppList()){
//                    appInfo.appStatus = false;
//                }
//                adapter.notifyDataSetChanged();
//                }
//            });
    }

    public void loadAppList(){
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installedApps = packageManager.queryIntentActivities(intent, 0);
        List<AppInfo> apps = new ArrayList<>();
        for(ResolveInfo info : installedApps){
            AppInfo appInfo = new AppInfo();
            appInfo.setAppLogo(info.loadIcon(packageManager));
            appInfo.setPackageName(info.activityInfo.packageName);
            appInfo.setAppName((String)info.loadLabel(packageManager));
            apps.add(appInfo);
        }
        adapter = new AppListAdapter(apps);
        app_list.setAdapter(adapter);
    }

    private void requestPermissions() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void saveLockedApps() {
        if (adapter != null) {
            Set<String> lockedApps = new HashSet<>();
            List<AppInfo> appInfoList = adapter.getAppList();
            for (AppInfo appInfo : appInfoList) {
                if (appInfo.appStatus) {
                    lockedApps.add(appInfo.getPackageName());
                }
            }
            SharedPreferences prefs = getSharedPreferences("AppLockPrefs", MODE_PRIVATE);
            prefs.edit().putStringSet("lockedApps", lockedApps).apply();
        }
    }

    private boolean isAccessibilityServiceEnabled() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo service : enabledServices) {
            if (service.getId().contains(getPackageName())) {
                return true;
            }
        }
        return false;
    }
}