package com.example.yatatsu.multiwindowsample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

public class LoggerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.i("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.i("onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.i("onPause");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.i("onConfigurationChanged: %s", newConfig);
    }

    @Override
    public void onMultiWindowChanged(boolean inMultiWindow) {
        super.onMultiWindowChanged(inMultiWindow);
        Timber.i("onMultiWindowChanged %s", inMultiWindow);
    }
}
