package com.goldthorp.ching.model;

import android.util.Pair;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.goldthorp.annotation.BackupEntity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity(tableName = "entry-part")
@BackupEntity(name = "entry-part")
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"entryId", "listSeq"})
public class EntryPart implements Serializable {
  @PrimaryKey(autoGenerate = true)
  private Long id;

  private String text;

  private Integer hexagram;

  @ColumnInfo(name = "second_hexagram")
  private Integer secondHexagram;

  @ColumnInfo(name = "entry_fk")
  private Long entryId;

  @ColumnInfo(name = "list_seq")
  private Integer listSeq;

  public EntryPart(
    final String text, final Integer hexagram, final Integer secondHexagram, final Integer listSeq) {
    this.text = text;
    this.hexagram = hexagram;
    this.secondHexagram = secondHexagram;
    this.listSeq = listSeq;
  }

  public EntryPart(
    final String text, final Pair<Integer, Integer> hexagrams, final Integer listSeq) {
    this.text = text;
    this.listSeq = listSeq;
    if (hexagrams != null) {
      hexagram = hexagrams.first;
      secondHexagram = hexagrams.second;
    }
  }
}
