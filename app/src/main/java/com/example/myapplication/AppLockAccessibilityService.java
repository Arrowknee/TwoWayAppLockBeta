package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import java.util.HashSet;
import java.util.Set;

public class AppLockAccessibilityService extends AccessibilityService {

    private Set<String> lockedApps;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null) {
                String packageName = event.getPackageName().toString();
                System.out.println("Package Name(OnAccessibilityEvent) : " + packageName);
                updateLockedApps();
                
                if (lockedApps != null && lockedApps.contains(packageName)) {
                     // Prevent loop: don't lock if we are already showing the lock screen or our own app
                    if (!packageName.equals(getPackageName())) {
                        System.out.println("Attempt to lock");
                        Intent intent = new Intent(this, LockScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    private void updateLockedApps() {
        SharedPreferences prefs = getSharedPreferences("AppLockPrefs", MODE_PRIVATE);
        lockedApps = prefs.getStringSet("lockedApps", new HashSet<>());
    }

    @Override
    public void onInterrupt() {
        // Required method
    }
}