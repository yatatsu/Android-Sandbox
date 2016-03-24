package com.example.yatatsu.realmplayground;

import android.app.Application;
import android.content.Context;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.List;
import timber.log.Timber;

public class App extends Application {

  public List<Todo> todoList;

  @Override public void onCreate() {
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
    RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
    Realm.setDefaultConfiguration(configuration);
  }

  public static App get(Context context) {
    return (App) context.getApplicationContext();
  }
}
