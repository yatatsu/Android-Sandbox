package com.example.yatatsu.realmplayground;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.yatatsu.realmplayground.databinding.ActivityMainBinding;
import com.example.yatatsu.realmplayground.databinding.ListItemTodoBinding;
import io.realm.Realm;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Realm realm;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Timber.i("onCreate");
    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    realm = Realm.getDefaultInstance();
    createTodos(20);
    binding.list.setLayoutManager(new LinearLayoutManager(this));
    binding.list.setAdapter(new TodoAdapter(this));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    Timber.i("onDestroy");
    realm.close();
  }

  public void createTodos(int size) {
    realm.beginTransaction();
    realm.deleteAll();
    for (int i = 0; i < size; i++) {
      Todo todo = Todo.create(i);
      realm.copyToRealm(todo);
    }
    realm.commitTransaction();
    App.get(this).todoList = realm.where(Todo.class).findAll();
  }

  App getApp() {
    return App.get(this);
  }

  @Override public void onClick(View v) {
    startActivity(new Intent(this, OtherActivity.class));
    finish();
  }

  private class TodoAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final Context context;


    public TodoAdapter(Context context) {
      this.context = context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(context, parent);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
      holder.binding.setTodo(getApp().todoList.get(position));
    }

    @Override public int getItemCount() {
      return getApp().todoList.size();
    }
  }

  private static class ViewHolder extends RecyclerView.ViewHolder {

    ListItemTodoBinding binding;

    public ViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_todo, parent, false));
      binding = DataBindingUtil.bind(itemView);
    }
  }
}
