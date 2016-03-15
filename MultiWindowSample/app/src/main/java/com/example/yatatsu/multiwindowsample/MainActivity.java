package com.example.yatatsu.multiwindowsample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.yatatsu.multiwindowsample.databinding.ActivityMainBinding;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
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
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.button_1:
                intent = new Intent(this, BasicActivity.class);
                break;
            case R.id.button_2:
                intent = new Intent(this, UnresizableActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case R.id.button_3:
                Uri uri = Uri.parse("http://android-developers.blogspot.jp/");
                intent = new Intent(Intent.ACTION_VIEW, uri)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
                break;
            case R.id.button_4:
                intent = new Intent(this, FixedActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case R.id.button_5:
                intent = new Intent(this, UnresizableActivity.class);
                break;
            case R.id.button_6:
                intent = new Intent(this, BasicActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
