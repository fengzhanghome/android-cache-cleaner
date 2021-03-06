package com.frozendevs.cache.cleaner.model;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.frozendevs.cache.cleaner.R;
import com.frozendevs.cache.cleaner.helper.CacheManager;

import java.util.List;

public class CleanerService extends Service implements CacheManager.OnActionListener {

    private static final String TAG = "CleanerService";

    private CacheManager cacheManager = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        cacheManager = new CacheManager(getPackageManager());
        cacheManager.setOnActionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        cacheManager.scanCache();
        
        return START_STICKY;
    }

    @Override
    public void onScanStarted(int appsCount) {

    }

    @Override
    public void onScanProgressUpdated(int current, int max) {

    }

    @Override
    public void onScanCompleted(List<AppsListItem> apps) {
        long size = 0;

        for(AppsListItem app : apps)
            size += app.getCacheSize();

        cacheManager.cleanCache(size);
    }

    @Override
    public void onCleanStarted() {

    }

    @Override
    public void onCleanCompleted(long cacheSize) {
        String msg = getString(R.string.cleaned) + " (" +
                Formatter.formatShortFileSize(this, cacheSize) + ")";

        Log.d(TAG, msg);

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 5000);
    }
}
