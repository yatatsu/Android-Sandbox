package com.example.yatatsu.realmtodolist;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;


public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
    RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
    Realm.setDefaultConfiguration(configuration);
  }
}
