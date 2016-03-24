package com.example.yatatsu.realmplayground;

import io.realm.RealmObject;

public class Todo extends RealmObject {
  public String title;
  public String body;

  public static Todo create(int number) {
    Todo todo = new Todo();
    todo.title = String.valueOf(number);
    todo.body = "body" + todo.title;
    return todo;
  }
}
