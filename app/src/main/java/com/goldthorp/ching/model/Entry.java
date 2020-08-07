package com.goldthorp.ching.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "entry")
public class Entry implements Comparable<Entry>, Serializable {
  @PrimaryKey(autoGenerate = true)
  private Long id;

  private Long timestamp;

  @Ignore
  private List<EntryPart> parts = new ArrayList<>();

  // Sort by newest to oldest
  @Override
  public int compareTo(final Entry e) {
    return e.timestamp.compareTo(timestamp);
  }
}
