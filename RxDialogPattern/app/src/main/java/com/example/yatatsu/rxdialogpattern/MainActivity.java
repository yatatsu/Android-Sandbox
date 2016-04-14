package com.example.yatatsu.rxdialogpattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG_A = "TAG_A";
  private static final String TAG_B = "TAG_B";
  private CompositeSubscription subscriptions = new CompositeSubscription();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override protected void onResume() {
    super.onResume();
    subscriptions.add(DialogEventBus.POSITIVE.observe(TAG_A).subscribe(new Action1<String>() {
      @Override public void call(String s) {
        Log.i(TAG_A, "onclick positive");
      }
    }));
  }

  @Override protected void onPause() {
    super.onPause();
    subscriptions.unsubscribe();
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_a:
        startDialog(TAG_A);
        break;
      case R.id.button_b:
        startDialog(TAG_B);
        break;
    }
  }

  private void startDialog(String tag) {
    SimpleDialogFragmentAutoBundle.createFragmentBuilder(tag, R.string.dialog_a, R.string.message)
        .positive(R.string.positive)
        .negative(R.string.negative)
        .build().show(getSupportFragmentManager(), tag);
  }
}
