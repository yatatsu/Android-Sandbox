package com.example.yatatsu.realmtodolist;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Favorite extends RealmObject {
  @PrimaryKey public int todoId;
}
