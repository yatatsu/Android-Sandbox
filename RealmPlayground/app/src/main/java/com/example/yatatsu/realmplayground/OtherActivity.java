package com.example.yatatsu.realmplayground;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.yatatsu.realmplayground.databinding.ActivityOtherBinding;
import com.example.yatatsu.realmplayground.databinding.ListItemTodoBinding;
import timber.log.Timber;

public class OtherActivity extends AppCompatActivity implements View.OnClickListener {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Timber.i("onCreate");
    ActivityOtherBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_other);
    binding.list.setLayoutManager(new LinearLayoutManager(this));
    binding.list.setAdapter(new TodoAdapter(this));
  }

  App getApp() {
    return App.get(this);
  }

  @Override public void onClick(View v) {
    startActivity(new Intent(this, Other2Activity.class));
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
