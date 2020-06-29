package com.goldthorp.ching.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.goldthorp.ching.model.Entry;

@Database(entities = {Entry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

  private static AppDatabase INSTANCE;

  public abstract EntryDao getEntryDao();

  public static synchronized AppDatabase getInstance(final Context context) {
    final String dbName = "ching.db";
    if (INSTANCE == null) {
      INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, dbName)
        .build();
    }
    return INSTANCE;
  }
}
