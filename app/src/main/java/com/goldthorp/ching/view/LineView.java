package com.goldthorp.ching.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import com.goldthorp.ching.R;

/**
 * Represents the view for a single line in a hexagram.
 */
public class LineView extends View {

  /**
   * If true, line is unbroken (yang). If false, line is broken (yin).
   */
  boolean isLight;

  /**
   * Paint used to add a rectangle to the middle of the line if it is broken (yin).
   */
  Paint white;

  private LineView(final Context context) {
    super(context);

    white = new Paint();
    white.setColor(getResources().getColor(R.color.background, null));
  }

  public LineView(
    final Context context, final ViewGroup.LayoutParams layoutParams, final boolean isLight) {
    this(context);
    this.isLight = isLight;
    setLayoutParams(layoutParams);
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    canvas.drawColor(Color.BLACK);
    if (!isLight) {
      // Add a white rectangle to the middle of the yin line to make it appear broken.
      // The break in the line makes up 1/10 the width of the line, so make the white rectangle
      // getWidth() / 20 from both sides of the middle.
      final int half = getWidth() / 2;
      final int left = half - getWidth() / 20;
      final int right = half + getWidth() / 20;
      canvas.drawRect(left, 0, right, getHeight(), white);
    }
  }
}
