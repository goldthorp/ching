package com.goldthorp.ching.util;

import android.app.Activity;

import lombok.Getter;

/**
 * An atomic integer whose value changes can be listened to. Listener callback will be called on
 * the UI thread.
 */
public class ObservableInteger {

  @Getter
  private int value;

  private final Activity activity;
  private final OnChangeListener listener;

  public ObservableInteger(final Activity activity, final OnChangeListener listener) {
    this.activity = activity;
    this.listener = listener;
  }

  public void set(final int value) {
    this.value = value;
    activity.runOnUiThread(() -> {
      listener.onChange(value);
    });
  }

  public interface OnChangeListener {
    void onChange(int value);
  }
}
