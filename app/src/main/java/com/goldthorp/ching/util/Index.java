package com.goldthorp.ching.util;

import android.util.Pair;

import com.goldthorp.ching.model.Hexagram;

public class Index {
  public static Pair<Hexagram, Hexagram> getHexagrams(
    final Integer firstHexagramNumber, final Integer secondHexagramNumber) {
    final Hexagram firstHexagram =
      new Hexagram(HEXAGRAMS[firstHexagramNumber - 1][0], HEXAGRAMS[firstHexagramNumber - 1][1]);
    if (secondHexagramNumber == null) {
      return Pair.create(firstHexagram, null);
    }
    final Hexagram secondHexagram =
      new Hexagram(HEXAGRAMS[secondHexagramNumber - 1][0], HEXAGRAMS[secondHexagramNumber - 1][1]);
    for (int i = 0; i < firstHexagram.getLines().length; i++) {
      final Hexagram.Line hex1Line = firstHexagram.getLines()[i];
      final Hexagram.Line hex2Line = secondHexagram.getLines()[i];
      firstHexagram.getLines()[i].setChanging(hex1Line.isLight() != hex2Line.isLight());
    }
    return Pair.create(firstHexagram, secondHexagram);
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
