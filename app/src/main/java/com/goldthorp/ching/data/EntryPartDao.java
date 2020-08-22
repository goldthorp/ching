package com.goldthorp.ching.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.goldthorp.ching.model.EntryPart;

import java.util.List;

@Dao
public interface EntryPartDao {
  @Insert
  long insert(EntryPart part);

  @Query("SELECT * FROM `entry-part` WHERE entry_fk = :entryId ORDER BY list_seq ASC")
  List<EntryPart> getByEntryId(long entryId);

  @Query("DELETE FROM `entry-part` WHERE entry_fk = :entryId")
  void deleteByEntryId(long entryId);
}
