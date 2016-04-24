package com.example.yatatsu.realmtodolist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.yatatsu.realmtodolist.databinding.ActivityTodoBinding;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

public class TodoActivity extends AppCompatActivity {

  int id;
  private ActivityTodoBinding binding;
  private CompositeSubscription compositeSubscription;
  private Realm realm;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_todo);
    realm = Realm.getDefaultInstance();
    compositeSubscription = new CompositeSubscription();
    int todoId = getIntent().getIntExtra("id", 0);
    Todo todo = realm.where(Todo.class).equalTo("id", todoId).findFirst();
    binding.setTodo(todo);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    compositeSubscription.unsubscribe();
    realm.close();
    realm = null;
  }
}
