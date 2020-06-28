package com.goldthorp.ching.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "entry")
public class Entry implements Comparable<Entry>, Serializable {
  @PrimaryKey(autoGenerate = true)
  private Long id;

  @ColumnInfo(name = "before-text")
  private String beforeText;

  private Integer hexagram;

  @ColumnInfo(name = "second-hexagram")
  private Integer secondHexagram;

  @ColumnInfo(name = "after-text")
  private String afterText;

  private Long timestamp;

  // Sort by newest to oldest
  @Override
  public int compareTo(final Entry e) {
    return e.timestamp.compareTo(timestamp);
  }
}
