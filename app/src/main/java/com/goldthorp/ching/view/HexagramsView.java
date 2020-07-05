package com.goldthorp.ching.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goldthorp.ching.R;
import com.goldthorp.ching.model.Hexagram;
import com.goldthorp.ching.util.Index;

/**
 * Represents a view with one or two hexagrams. If two, show an arrow between them pointing from
 * the first to the second.
 */
public class HexagramsView extends LinearLayout {

  public HexagramsView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public HexagramsView(final Context context) {
    super(context);
    setOrientation(HORIZONTAL);
    final int layoutPaddingPx = getResources().getDimensionPixelSize(R.dimen.layout_padding);
    setPadding(layoutPaddingPx, layoutPaddingPx, layoutPaddingPx, layoutPaddingPx);
    setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      getResources().getDimensionPixelSize(R.dimen.hexagrams_height)));
  }

  /**
   * Set the hexagrams in the view.
   *
   * @param hexagramNumber       number for first hexagram
   * @param secondHexagramNumber (optional) number for second hexagram
   */
  public void setHexagrams(final Integer hexagramNumber, final Integer secondHexagramNumber) {
    // Remove all views in case we are changing the hexagrams that have already been added
    removeAllViews();

    final Pair<Hexagram, Hexagram> hexagrams =
      Index.getHexagrams(hexagramNumber, secondHexagramNumber);
    addFirstHexagram(hexagrams.first, secondHexagramNumber == null);

    if (secondHexagramNumber != null) {
      addSecondHexagram(hexagrams.second);
    }
  }

  /**
   * Add the first hexagram to the view.
   *
   * @param hexagram   hexagram to add
   * @param addSpacers pass true if this is a static hexagram so that spacer views are added on
   *                   either side to center the hexagram
   */
  private void addFirstHexagram(final Hexagram hexagram, final boolean addSpacers) {
    // View index to add hexagram at
    int hexagramIndex = 0;
    if (addSpacers) {
      // Add spacer views on either side so that the hexagram is centered and not full-screen
      final LayoutParams spacerParams =
        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3);
      final View leftSpacer = new View(getContext());
      leftSpacer.setLayoutParams(spacerParams);
      final View rightSpacer = new View(getContext());
      rightSpacer.setLayoutParams(spacerParams);
      addView(leftSpacer);
      addView(rightSpacer);
      // Set index so hexagram is added between the two spacers
      hexagramIndex = 1;
    }

    final LayoutParams hex1Params =
      new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5);
    final HexagramView hexagram1 = new HexagramView(getContext(), hexagram);
    hexagram1.setLayoutParams(hex1Params);
    addView(hexagram1, hexagramIndex);
  }

  /**
   * Add an arrow next to the first hexagram then add the second hexagram.
   *
   * @param hexagram second hexagram to add
   */
  private void addSecondHexagram(final Hexagram hexagram) {
    final ImageView arrow = new ImageView(getContext());
    arrow.setImageResource(R.drawable.next);
    final LayoutParams arrowParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2);
    arrow.setLayoutParams(arrowParams);
    final int arrowPaddingPx = getResources().getDimensionPixelSize(R.dimen.arrow_padding);
    arrow.setPadding(arrowPaddingPx, 0, arrowPaddingPx, 0);
    addView(arrow);

    final HexagramView hexagram2 = new HexagramView(getContext(), hexagram);
    final LayoutParams hex2Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5);
    hexagram2.setLayoutParams(hex2Params);
    addView(hexagram2);
  }
}
