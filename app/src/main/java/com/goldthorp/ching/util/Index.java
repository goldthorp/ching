package com.goldthorp.ching.util;

import android.util.Pair;

import com.goldthorp.ching.model.Hexagram;

public class Index {
  /**
   * Find the pair of Hexagrams corresponding to the given numbers.
   *
   * @param firstHexagramNumber  number of first hexagram
   * @param secondHexagramNumber (optional) number of second hexagram
   * @return pair of Hexagrams (second will be null if null was passed for secondHexagramNumber)
   */
  public static Pair<Hexagram, Hexagram> getHexagrams(
    final Integer firstHexagramNumber, final Integer secondHexagramNumber) {
    final Hexagram firstHexagram =
      new Hexagram(HEXAGRAMS[firstHexagramNumber - 1], firstHexagramNumber);
    if (secondHexagramNumber == null) {
      return Pair.create(firstHexagram, null);
    }
    final Hexagram secondHexagram =
      new Hexagram(HEXAGRAMS[secondHexagramNumber - 1], secondHexagramNumber);
    for (int i = 0; i < firstHexagram.getLines().length; i++) {
      final Hexagram.Line hex1Line = firstHexagram.getLines()[i];
      final Hexagram.Line hex2Line = secondHexagram.getLines()[i];
      firstHexagram.getLines()[i].setChanging(hex1Line.isLight() != hex2Line.isLight());
    }
    return Pair.create(firstHexagram, secondHexagram);
  }

  /**
   * Find the numbers for the specified Hexagram and the one it changes to (if it has changing
   * lines).
   *
   * @param hexagram to find number(s) of
   * @return pair of integers for the hexagram numbers. Second integer will be null if the
   * specified hexagram has no changing lines
   */
  public static Pair<Integer, Integer> reverseGetHexagrams(final Hexagram hexagram) {
    Integer firstHexagramNumber = null;
    Integer secondHexagramNumber = null;

    for (int i = 0; i < HEXAGRAMS.length; i++) {
      final boolean[] upper = HEXAGRAMS[i][0];
      final boolean[] lower = HEXAGRAMS[i][1];
      final Hexagram.Line[] lines = hexagram.getLines();
      if (lines[0].isLight() == lower[0] &&
        lines[1].isLight() == lower[1] &&
        lines[2].isLight() == lower[2] &&
        lines[3].isLight() == upper[0] &&
        lines[4].isLight() == upper[1] &&
        lines[5].isLight() == upper[2]) {
        firstHexagramNumber = i + 1;
        break;
      }
    }

    // Find the second hexagram if the first one has changing lines
    if (hexagram.hasChangingLines()) {
      for (int i = 0; i < HEXAGRAMS.length; i++) {
        final boolean[] upper = HEXAGRAMS[i][0];
        final boolean[] lower = HEXAGRAMS[i][1];
        final Hexagram.Line[] lines = hexagram.getLines();
        // Use XOR to 'flip' the value of the line in the original hexagram if it is changing
        if ((lines[0].isLight() ^ lines[0].isChanging()) == lower[0] &&
          (lines[1].isLight() ^ lines[1].isChanging()) == lower[1] &&
          (lines[2].isLight() ^ lines[2].isChanging()) == lower[2] &&
          (lines[3].isLight() ^ lines[3].isChanging()) == upper[0] &&
          (lines[4].isLight() ^ lines[4].isChanging()) == upper[1] &&
          (lines[5].isLight() ^ lines[5].isChanging()) == upper[2]) {
          secondHexagramNumber = i + 1;
          break;
        }
      }
    }

    return Pair.create(firstHexagramNumber, secondHexagramNumber);
  }

  final private static boolean[][][] HEXAGRAMS = {
    {
      Trigrams.HEAVEN,
      Trigrams.HEAVEN
    },
    {
      Trigrams.EARTH,
      Trigrams.EARTH
    },
    {
      Trigrams.WATER,
      Trigrams.THUNDER
    },
    {
      Trigrams.MOUNTAIN,
      Trigrams.WATER
    },
    {
      Trigrams.WATER,
      Trigrams.HEAVEN
    },
    {
      Trigrams.HEAVEN,
      Trigrams.WATER
    },
    {
      Trigrams.EARTH,
      Trigrams.WATER
    },
    {
      Trigrams.WATER,
      Trigrams.EARTH
    },
    {
      Trigrams.WIND,
      Trigrams.HEAVEN
    },
    {
      Trigrams.HEAVEN,
      Trigrams.LAKE
    },
    {
      Trigrams.EARTH,
      Trigrams.HEAVEN
    },
    {
      Trigrams.HEAVEN,
      Trigrams.EARTH
    },
    {
      Trigrams.HEAVEN,
      Trigrams.FIRE
    },
    {
      Trigrams.FIRE,
      Trigrams.HEAVEN
    },
    {
      Trigrams.EARTH,
      Trigrams.MOUNTAIN
    },
    {
      Trigrams.THUNDER,
      Trigrams.EARTH
    },
    {
      Trigrams.LAKE,
      Trigrams.THUNDER
    },
    {
      Trigrams.MOUNTAIN,
      Trigrams.WIND
    },
    {
      Trigrams.EARTH,
      Trigrams.LAKE
    },
    {
      Trigrams.WIND,
      Trigrams.EARTH
    },
    {
      Trigrams.FIRE,
      Trigrams.THUNDER
    },
    {
      Trigrams.MOUNTAIN,
      Trigrams.FIRE
    },
    {
      Trigrams.MOUNTAIN,
      Trigrams.EARTH
    },
    {
      Trigrams.EARTH,
      Trigrams.THUNDER
    },
    {
      Trigrams.HEAVEN,
      Trigrams.THUNDER
    },
    {
      Trigrams.MOUNTAIN,
      Trigrams.HEAVEN
    },
    {
      Trigrams.MOUNTAIN,
      Trigrams.THUNDER
    },
    {
      Trigrams.LAKE,
      Trigrams.WIND
    },
    {
      Trigrams.WATER,
      Trigrams.WATER
    },
    {
      Trigrams.FIRE,
      Trigrams.FIRE
    },
    {
      Trigrams.LAKE,
      Trigrams.MOUNTAIN
    },
    {
      Trigrams.THUNDER,
      Trigrams.WIND
    },
    {
      Trigrams.HEAVEN,
      Trigrams.MOUNTAIN
    },
    {
      Trigrams.THUNDER,
      Trigrams.HEAVEN
    },
    {
      Trigrams.FIRE,
      Trigrams.EARTH
    },
    {
      Trigrams.EARTH,
      Trigrams.FIRE
    },
    {
      Trigrams.WIND,
      Trigrams.FIRE
    },
    {
      Trigrams.FIRE,
      Trigrams.LAKE
    },
    {
      Trigrams.WATER,
      Trigrams.MOUNTAIN
    },
    {
      Trigrams.THUNDER,
      Trigrams.WATER
    },
    {
      Trigrams.MOUNTAIN,
      Trigrams.LAKE
    },
    {
      Trigrams.WIND,
      Trigrams.THUNDER
    },
    {
      Trigrams.LAKE,
      Trigrams.HEAVEN
    },
    {
      Trigrams.HEAVEN,
      Trigrams.WIND
    },
    {
      Trigrams.LAKE,
      Trigrams.EARTH
    },
    {
      Trigrams.EARTH,
      Trigrams.WIND
    },
    {
      Trigrams.LAKE,
      Trigrams.WATER
    },
    {
      Trigrams.WATER,
      Trigrams.WIND
    },
    {
      Trigrams.LAKE,
      Trigrams.FIRE
    },
    {
      Trigrams.FIRE,
      Trigrams.WIND
    },
    {
      Trigrams.THUNDER,
      Trigrams.THUNDER
    },
    {
      Trigrams.MOUNTAIN,
      Trigrams.MOUNTAIN
    },
    {
      Trigrams.WIND,
      Trigrams.MOUNTAIN
    },
    {
      Trigrams.THUNDER,
      Trigrams.LAKE
    },
    {
      Trigrams.THUNDER,
      Trigrams.FIRE
    },
    {
      Trigrams.FIRE,
      Trigrams.MOUNTAIN
    },
    {
      Trigrams.WIND,
      Trigrams.WIND
    },
    {
      Trigrams.LAKE,
      Trigrams.LAKE
    },
    {
      Trigrams.WIND,
      Trigrams.WATER
    },
    {
      Trigrams.WATER,
      Trigrams.LAKE
    },
    {
      Trigrams.WIND,
      Trigrams.LAKE
    },
    {
      Trigrams.THUNDER,
      Trigrams.MOUNTAIN
    },
    {
      Trigrams.WATER,
      Trigrams.FIRE
    },
    {
      Trigrams.FIRE,
      Trigrams.WATER
    },
  };

  private static class Trigrams {
    final static boolean[] HEAVEN = {true, true, true};
    final static boolean[] THUNDER = {true, false, false};
    final static boolean[] WATER = {false, true, false};
    final static boolean[] MOUNTAIN = {false, false, true};
    final static boolean[] EARTH = {false, false, false};
    final static boolean[] WIND = {false, true, true};
    final static boolean[] FIRE = {true, false, true};
    final static boolean[] LAKE = {true, true, false};
  }
}
