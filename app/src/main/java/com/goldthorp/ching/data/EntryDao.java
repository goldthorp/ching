package com.goldthorp.ching.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.goldthorp.ching.model.Entry;

import java.util.List;

@Dao
public interface EntryDao {
  @Query("SELECT * FROM entry ORDER BY timestamp DESC")
  LiveData<List<Entry>> getAll();

  @Insert
  void insert(Entry entry);
}
