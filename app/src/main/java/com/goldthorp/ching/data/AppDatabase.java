package com.goldthorp.ching.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.goldthorp.ching.model.Entry;
import com.goldthorp.ching.model.EntryPart;

import org.apache.commons.lang3.StringUtils;

@Database(entities = {Entry.class, EntryPart.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

  private static AppDatabase INSTANCE;

  public abstract EntryDao getEntryDao();

  public abstract EntryPartDao getEntryPartDao();

  public static synchronized AppDatabase getInstance(final Context context) {
    final String dbName = "ching.db";
    if (INSTANCE == null) {
      INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, dbName)
        .addMigrations(MIGRATION_1_2)
        .addMigrations(MIGRATION_2_3)
        .build();
    }
    return INSTANCE;
  }

  /**
   * Create the `entry-part` table and remove hexagram/text columns from entry. This is to break
   * entries up into parts so that multiple casts can be in a single entry.
   */
  private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(@NonNull final SupportSQLiteDatabase database) {
      // Create the `entry-part` table and populate it with data from the entries.
      database.execSQL("CREATE TABLE `entry-part` (`id` INTEGER PRIMARY KEY, `text` TEXT, " +
        "`hexagram` INTEGER, `second_hexagram` INTEGER, `entry_fk` INTEGER, `list_seq` INTEGER)");
      final Cursor query = database.query("SELECT * FROM `entry`");
      while (query.moveToNext()) {
        final ContentValues part1Values = new ContentValues();
        final int firstHexagram = query.getInt(2);
        final Integer secondHexagram = query.getInt(3);
        final int entryId = query.getInt(0);
        final String beforeText = query.getString(1);
        final String afterText = query.getString(4);
        part1Values.put("hexagram", firstHexagram);
        if (secondHexagram > 0) {
          part1Values.put("second_hexagram", secondHexagram);
        }
        part1Values.put("entry_fk", entryId);
        part1Values.put("text", beforeText);
        part1Values.put("list_seq", 0);
        database.insert("`entry-part`", OnConflictStrategy.ABORT, part1Values);
        if (StringUtils.isNotBlank(afterText)) {
          final ContentValues part2Values = new ContentValues();
          part2Values.put("entry_fk", entryId);
          part2Values.put("text", afterText);
          part2Values.put("list_seq", 1);
          database.insert("`entry-part`", OnConflictStrategy.ABORT, part2Values);
        }
      }
      // We need to create a new table since SQLite doesn't allow dropping columns.
      database.execSQL("CREATE TABLE `entry_1` (`id` INTEGER PRIMARY KEY, `timestamp` INTEGER)");
      database.execSQL("INSERT INTO `entry_1` SELECT `id`, `timestamp` FROM `entry`");
      database.execSQL("DROP TABLE `entry`");
      database.execSQL("ALTER TABLE `entry_1` RENAME TO `entry`");
    }
  };

  /**
   * Add the is_draft column (boolean) so entries can be saved as drafts and finished later.
   */
  private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
    @Override
    public void migrate(@NonNull final SupportSQLiteDatabase database) {
      database.execSQL("ALTER TABLE `entry` ADD COLUMN `is_draft` INTEGER NOT NULL DEFAULT 0");
    }
  };
}
