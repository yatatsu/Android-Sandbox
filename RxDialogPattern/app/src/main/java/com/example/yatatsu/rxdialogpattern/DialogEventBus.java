package com.example.yatatsu.rxdialogpattern;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

public enum DialogEventBus {
  POSITIVE,
  NEGATIVE,
  CANCEL,
  DISMISS,
  ;

  private final SerializedSubject<String, String> bus;

  DialogEventBus() {
    bus = new SerializedSubject<>(PublishSubject.<String>create());
  }

  public void post(String tag) {
    bus.onNext(tag);
  }

  public Observable<String> observeAll() {
    return bus;
  }

  public Observable<String> observe(final String tag) {
    return bus.filter(new Func1<String, Boolean>() {
      @Override public Boolean call(String s) {
        return tag != null && tag.equals(s);
      }
    });
  }

  public boolean hasObservers() {
    return bus.hasObservers();
  }
}
