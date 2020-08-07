package com.goldthorp.ching.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.goldthorp.ching.model.Entry;
import com.goldthorp.ching.model.EntryPart;

import java.util.List;

@Dao
public abstract class EntryDao {

  private final EntryPartDao entryPartDao;

  EntryDao(final AppDatabase appDatabase) {
    entryPartDao = appDatabase.getEntryPartDao();
  }

  @Query("SELECT * FROM entry ORDER BY timestamp DESC")
  public abstract LiveData<List<Entry>> getAll();

  @Insert
  public abstract long insert(Entry entry);

  public void insertWithParts(final Entry entry) {
    final long entryId = insert(entry);
    for (final EntryPart part : entry.getParts()) {
      part.setEntryId(entryId);
      entryPartDao.insert(part);
    }
  }
}
