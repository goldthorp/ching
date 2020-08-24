package com.goldthorp.ching.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Hexagram {

  private final Line[] lines;
  private int hexagramNumber;

  public Hexagram(final boolean[][] hexagram, final int hexagramNumber) {
    final boolean[] upperTrigram = hexagram[0];
    final boolean[] lowerTrigram = hexagram[1];
    lines = new Line[6];
    lines[0] = new Line(lowerTrigram[0]);
    lines[1] = new Line(lowerTrigram[1]);
    lines[2] = new Line(lowerTrigram[2]);
    lines[3] = new Line(upperTrigram[0]);
    lines[4] = new Line(upperTrigram[1]);
    lines[5] = new Line(upperTrigram[2]);
    this.hexagramNumber = hexagramNumber;
  }

  public Hexagram() {
    lines = new Line[6];
  }

  public boolean hasChangingLines() {
    for (final Line line : lines) {
      if (line.isChanging) {
        return true;
      }
    }
    return false;
  }

  @Getter
  public static class Line {
    private final boolean isLight;

    @Setter
    private boolean isChanging;

    private Line(final boolean isLight) {
      this.isLight = isLight;
    }

    public Line(final boolean isLight, final boolean isChanging) {
      this.isLight = isLight;
      this.isChanging = isChanging;
    }
  }
}
