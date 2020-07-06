package com.goldthorp.ching.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.goldthorp.ching.model.Hexagram;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Represents a view for a single hexagram.
 */
@Getter
public class HexagramView extends LinearLayout {

  private final List<LineView> lineViews;

  public HexagramView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    setOrientation(VERTICAL);
    lineViews = new ArrayList<>();

    // Fill the hexagram with blank lines (these are spacers while generating lines for a hexagram)
    for (int i = 0; i < 6; i++) {
      final LineView lineView = new LineView(context);
      addLine(lineView);
    }
  }

  public HexagramView(final Context context, final Hexagram hexagram) {
    super(context);
    setOrientation(VERTICAL);
    lineViews = new ArrayList<>();
    // Add the lineViews to the view for each lineViews in the hexagram
    // These are added from the bottom up
    for (int i = 0; i < hexagram.getLines().length; i++) {
      final Hexagram.Line line = hexagram.getLines()[i];
      final LineView lineView = new LineView(context, line);
      // Remove bottom margin from bottom line
      if (i == 0) {
        ((MarginLayoutParams) lineView.getLayoutParams()).bottomMargin = 0;
      }
      addLine(lineView);
    }
  }

  private void addLine(final LineView line) {
    lineViews.add(line);
    // Pass 0 for index since we're adding lineViews from the bottom up
    super.addView(line, 0);
  }
}
