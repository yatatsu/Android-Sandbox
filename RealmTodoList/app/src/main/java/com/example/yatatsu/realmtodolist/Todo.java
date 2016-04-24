package com.example.yatatsu.realmtodolist;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Todo extends RealmObject {
  @PrimaryKey public int id;
  public String title;
  public String description;
}
