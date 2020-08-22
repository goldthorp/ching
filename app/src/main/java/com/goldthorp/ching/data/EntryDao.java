package com.goldthorp.ching.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.goldthorp.ching.model.Entry;
import com.goldthorp.ching.model.EntryPart;

import java.util.List;

@Dao
public abstract class EntryDao {

  private final EntryPartDao entryPartDao;

  EntryDao(final AppDatabase appDatabase) {
    entryPartDao = appDatabase.getEntryPartDao();
  }

  @Query("SELECT * FROM entry WHERE is_draft = 0 ORDER BY timestamp DESC")
  public abstract LiveData<List<Entry>> getAll();

  @Query("SELECT * FROM entry WHERE is_draft = 1")
  public abstract Entry getDraft();

  @Query("SELECT COUNT(*) FROM entry WHERE is_draft = 1")
  public abstract long getDraftCount();

  @Insert
  public abstract long insert(Entry entry);

  @Update
  abstract void update(Entry entry);

  @Delete
  public abstract void delete(Entry entry);

  /**
   * Delete an entry and all its parts.
   *
   * @param entry to delete
   */
  public void deleteWithParts(final Entry entry) {
    entryPartDao.deleteByEntryId(entry.getId());
    delete(entry);
  }

  /**
   * Insert a new entry and its parts.
   *
   * @param entry to insert
   */
  private void insertWithParts(final Entry entry) {
    final long entryId = insert(entry);
    entry.setId(entryId);
    for (final EntryPart part : entry.getParts()) {
      part.setEntryId(entryId);
      final long partId = entryPartDao.insert(part);
      part.setId(partId);
    }
  }

  /**
   * Update an existing entry and its parts.
   *
   * @param entry to update
   */
  private void updateWithParts(final Entry entry) {
    update(entry);
    entryPartDao.deleteByEntryId(entry.getId());
    for (final EntryPart part : entry.getParts()) {
      entryPartDao.insert(part);
    }
  }

  /**
   * Save an entry and all its parts. Creates if new entry (ID not set); updates if existing.
   *
   * @param entry to save
   */
  public void saveWithParts(final Entry entry) {
    if (entry.getId() == null) {
      insertWithParts(entry);
    } else {
      updateWithParts(entry);
    }
  }
}
