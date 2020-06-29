package com.goldthorp.ching.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.goldthorp.ching.R;
import com.goldthorp.ching.util.Index;

/**
 * Represents a view for a single hexagram.
 */
public class HexagramView extends LinearLayout {

  public HexagramView(final Context context) {
    super(context);
    setOrientation(VERTICAL);
  }

  public HexagramView(final Context context, final int hexagramNumber) {
    this(context);
    // Find the hexagram by its number in the index
    final boolean[] hexagram = Index.getHexagram(hexagramNumber);
    // Add the lines to the view for each lines in the hexagram
    // These are added from the bottom up
    for (int i = 0; i < hexagram.length; i++) {
      final LayoutParams lineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
      // Add bottom margin to every line except the bottom one
      if (i != 0) {
        lineParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.line_padding);
      }
      // Pass 0 for index since we're adding lines from the bottom up
      addView(new LineView(context, lineParams, hexagram[i]), 0);
    }
  }
}
