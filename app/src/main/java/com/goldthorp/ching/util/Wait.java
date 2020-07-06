package com.goldthorp.ching.util;

import android.os.AsyncTask;

/**
 * Util class to delay execution without blocking the calling thread.
 */
public class Wait extends AsyncTask<Void, Void, Void> {

  private final long millis;
  private final Callback callback;

  /**
   * @param millis   how long to wait before calling callback
   * @param callback to call after wait
   */
  public Wait(final long millis, final Callback callback) {
    this.millis = millis;
    this.callback = callback;
  }

  @Override
  protected Void doInBackground(final Void... voids) {
    try {
      Thread.sleep(millis);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  protected void onPostExecute(final Void aVoid) {
    callback.resolve();
  }

  public interface Callback {
    void resolve();
  }
}
