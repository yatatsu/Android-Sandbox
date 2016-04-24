package com.example.yatatsu.realmtodolist;

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
import com.example.yatatsu.realmtodolist.databinding.ActivityMainBinding;
import com.example.yatatsu.realmtodolist.databinding.ListItemTodoBinding;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private CompositeSubscription compositeSubscription;
  private Realm realm;
  private TodoAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    compositeSubscription = new CompositeSubscription();
    realm = Realm.getDefaultInstance();
    inflateViews();
    compositeSubscription.add(realm.where(Todo.class).findAllAsync().<Todo>asObservable().subscribe(
        new Action1<RealmResults<Todo>>() {
          @Override public void call(RealmResults<Todo> todoList) {
            adapter.setTodoList(todoList);
            adapter.notifyDataSetChanged();
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Timber.e(throwable, "error in todoList");
          }
        }));
    compositeSubscription.add(
        realm.where(Favorite.class).findAllAsync().<Favorite>asObservable().subscribe(
            new Action1<RealmResults<Favorite>>() {
              @Override public void call(RealmResults<Favorite> favorites) {
                List<Integer> favoriteIds =
                    Observable.from(favorites).map(new Func1<Favorite, Integer>() {
                      @Override public Integer call(Favorite favorite) {
                        return favorite.todoId;
                      }
                    }).toList().toBlocking().single();
                adapter.setFavoriteIds(favoriteIds);
                adapter.notifyDataSetChanged();
              }
            }));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    compositeSubscription.unsubscribe();
    realm.close();
    realm = null;
  }

  private void inflateViews() {
    binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new TodoAdapter(this);
    adapter.setOnFavoriteListener(new OnFavoriteListener() {
      @Override public void onFavorite(final Todo item) {
        realm.executeTransaction(new Realm.Transaction() {
          @Override public void execute(Realm realm) {
            Favorite fav = realm.where(Favorite.class).equalTo("todoId", item.id).findFirst();
            if (fav == null) {
              Favorite favorite = new Favorite();
              favorite.todoId = item.id;
              realm.copyToRealm(favorite);
            } else {
              fav.deleteFromRealm();
            }
          }
        });
      }
    });
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override public void onItemClick(Todo item) {
        Intent intent = new Intent(MainActivity.this, TodoActivity.class);
        intent.putExtra("id", item.id);
        startActivity(intent);
      }
    });
    binding.recyclerView.setAdapter(this.adapter);

    binding.createButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        realm.executeTransaction(new Realm.Transaction() {
          @Override public void execute(Realm realm) {
            long count = realm.where(Todo.class).count();
            Todo todo = new Todo();
            todo.id = (int) (count + 1);
            todo.title = "task " + todo.id;
            todo.description = "This is task " + todo.id;
            realm.copyToRealm(todo);
          }
        });
      }
    });
  }

  interface OnFavoriteListener {
    void onFavorite(Todo item);
  }

  interface OnItemClickListener {
    void onItemClick(Todo item);
  }

  private class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private final LayoutInflater inflater;
    private List<Todo> todoList;
    private List<Integer> favoriteIds;
    private OnFavoriteListener onFavoriteListener;
    private OnItemClickListener onItemClickListener;

    TodoAdapter(Context context) {
      inflater = LayoutInflater.from(context);
      todoList = new ArrayList<>();
      favoriteIds = new ArrayList<>();
    }

    public void setOnFavoriteListener(OnFavoriteListener onFavoriteListener) {
      this.onFavoriteListener = onFavoriteListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
      this.onItemClickListener = onItemClickListener;
    }

    public void setTodoList(List<Todo> todoList) {
      this.todoList = todoList;
    }

    public void setFavoriteIds(List<Integer> favoriteIds) {
      this.favoriteIds = favoriteIds;
    }

    @Override public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final TodoViewHolder holder = new TodoViewHolder(inflater, parent);
      holder.binding.favoriteIcon.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (onFavoriteListener != null) {
            int position = holder.getAdapterPosition();
            onFavoriteListener.onFavorite(todoList.get(position));
          }
        }
      });
      holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (onItemClickListener != null) {
            int position = holder.getAdapterPosition();
            onItemClickListener.onItemClick(todoList.get(position));
          }
        }
      });
      return holder;
    }

    @Override public void onBindViewHolder(TodoViewHolder holder, int position) {
      Todo todo = todoList.get(position);
      holder.binding.setTodo(todo);
      boolean isFavorite = favoriteIds.contains(todo.id);
      holder.binding.favoriteIcon.setImageResource(
          isFavorite ? R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp);
    }

    @Override public int getItemCount() {
      return todoList.size();
    }
  }

  private static class TodoViewHolder extends RecyclerView.ViewHolder {
    ListItemTodoBinding binding;

    public TodoViewHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.list_item_todo, parent, false));
      binding = DataBindingUtil.bind(itemView);
    }
  }
}
