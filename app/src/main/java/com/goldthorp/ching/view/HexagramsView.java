package com.goldthorp.ching.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goldthorp.ching.R;

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

    addFirstHexagram(hexagramNumber);

    if (secondHexagramNumber != null) {
      addSecondHexagram(secondHexagramNumber);
    }
  }

  /**
   * Add the first hexagram to the view.
   *
   * @param hexagramNumber number of hexagram to add
   */
  private void addFirstHexagram(final int hexagramNumber) {
    final HexagramView hexagram1 = new HexagramView(getContext(), hexagramNumber);
    final LayoutParams hex1Params =
      new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5);
    hexagram1.setLayoutParams(hex1Params);
    addView(hexagram1);
  }

  /**
   * Add an arrow next to the first hexagram then add the second hexagram.
   *
   * @param hexagramNumber number of second hexagram to add
   */
  private void addSecondHexagram(final int hexagramNumber) {
    final ImageView arrow = new ImageView(getContext());
    arrow.setImageResource(R.drawable.next);
    final LayoutParams arrowParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2);
    arrow.setLayoutParams(arrowParams);
    final int arrowPaddingPx = getResources().getDimensionPixelSize(R.dimen.arrow_padding);
    arrow.setPadding(arrowPaddingPx, 0, arrowPaddingPx, 0);
    addView(arrow);

    final HexagramView hexagram2 = new HexagramView(getContext(), hexagramNumber);
    final LayoutParams hex2Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5);
    hexagram2.setLayoutParams(hex2Params);
    addView(hexagram2);
  }
}
