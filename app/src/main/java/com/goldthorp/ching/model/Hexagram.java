package com.goldthorp.ching.model;

import lombok.Getter;
import lombok.Setter;

public class Hexagram {

  @Getter
  private final Line[] lines;

  public Hexagram(final boolean[][] hexagram) {
    final boolean[] upperTrigram = hexagram[0];
    final boolean[] lowerTrigram = hexagram[1];
    lines = new Line[6];
    lines[0] = new Line(lowerTrigram[0]);
    lines[1] = new Line(lowerTrigram[1]);
    lines[2] = new Line(lowerTrigram[2]);
    lines[3] = new Line(upperTrigram[0]);
    lines[4] = new Line(upperTrigram[1]);
    lines[5] = new Line(upperTrigram[2]);
  }

  @Getter
  public class Line {
    private final boolean isLight;

    @Setter
    private boolean isChanging;

    Line(final boolean isLight) {
      this.isLight = isLight;
    }
  }
}
