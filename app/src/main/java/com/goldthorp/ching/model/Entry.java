package com.goldthorp.ching.model;

import android.util.Pair;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity(tableName = "entry")
@ToString
public class Entry implements Comparable<Entry>, Serializable {
  @PrimaryKey(autoGenerate = true)
  private Long id;

  private Long timestamp;

  @Ignore
  private List<EntryPart> parts = new ArrayList<>();

  public void addPart(final String text, final Pair<Integer, Integer> hexagrams) {
    final EntryPart entryPart = new EntryPart(text, hexagrams, parts.size());
    entryPart.setEntryId(getId());
    parts.add(entryPart);
  }

  @ColumnInfo(name = "is_draft")
  private boolean isDraft;

  // Sort by newest to oldest
  @Override
  public int compareTo(final Entry e) {
    return e.timestamp.compareTo(timestamp);
  }
}
