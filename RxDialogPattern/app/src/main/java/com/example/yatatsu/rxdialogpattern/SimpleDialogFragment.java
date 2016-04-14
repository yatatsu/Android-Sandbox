package com.example.yatatsu.rxdialogpattern;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import com.yatatsu.autobundle.AutoBundleField;

public class SimpleDialogFragment extends AppCompatDialogFragment {

  @AutoBundleField String tag;
  @AutoBundleField @StringRes int title;
  @AutoBundleField @StringRes int message;
  @AutoBundleField(required = false) int positive;
  @AutoBundleField(required = false) int negative;

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      SimpleDialogFragmentAutoBundle.bind(this, savedInstanceState);
    } else {
      SimpleDialogFragmentAutoBundle.bind(this);
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
        .setTitle(title)
        .setMessage(message);
    if (positive != 0) {
      builder.setPositiveButton(positive, onClickPositive);
    }
    if (negative != 0) {
      builder.setNegativeButton(negative, onClickNegative);
    }
    return builder.create();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    SimpleDialogFragmentAutoBundle.pack(this, outState);
  }

  @Override public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    DialogEventBus.DISMISS.post(tag);
  }

  @Override public void onCancel(DialogInterface dialog) {
    super.onCancel(dialog);
    DialogEventBus.CANCEL.post(tag);
  }

  private DialogInterface.OnClickListener onClickNegative = new DialogInterface.OnClickListener() {
    @Override public void onClick(DialogInterface dialog, int which) {
      DialogEventBus.NEGATIVE.post(tag);
    }
  };

  private DialogInterface.OnClickListener onClickPositive = new DialogInterface.OnClickListener() {
    @Override public void onClick(DialogInterface dialog, int which) {
      DialogEventBus.POSITIVE.post(tag);
    }
  };
}
