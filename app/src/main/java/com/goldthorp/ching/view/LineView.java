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
  private boolean isLight;

  /**
   * If true, line is changing. If false line is static.
   *
   * Only applies to the first hexagram of a pair (should always be false for second).
   */
  private boolean isChanging;

  /**
   * Paint used to add a rectangle to the middle of the line if it is broken (yin).
   *
   * Also used for the dot in the middle of a changing yang line.
   */
  private final Paint white;

  /**
   * Paint used for the dot in the middle of a changing yin line.
   */
  private final Paint black;

  private LineView(final Context context) {
    super(context);

    white = new Paint(Paint.ANTI_ALIAS_FLAG);
    white.setColor(getResources().getColor(R.color.background, null));

    black = new Paint(Paint.ANTI_ALIAS_FLAG);
    black.setColor(Color.BLACK);
  }

  public LineView(
    final Context context, final ViewGroup.LayoutParams layoutParams, final boolean isLight,
    final boolean isChanging) {
    this(context);
    this.isLight = isLight;
    this.isChanging = isChanging;
    setLayoutParams(layoutParams);
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    canvas.drawColor(Color.BLACK);
    final int half = getWidth() / 2;
    // Color of dot for changing lines
    final Paint dotColor;
    if (!isLight) {
      // Add a white rectangle to the middle of the yin line to make it appear broken.
      // The break in the line makes up 1/10 the width of the line, so make the white rectangle
      // getWidth() / 20 from both sides of the middle.
      final int left = half - getWidth() / 20;
      final int right = half + getWidth() / 20;
      canvas.drawRect(left, 0, right, getHeight(), white);

      dotColor = black;
    } else {
      dotColor = white;
    }

    // Draw dot in the middle of changing lines
    if (isChanging) {
      final float radius = (getHeight() * .5f) - 7;
      canvas.drawCircle(half, getHeight() / 2, radius, dotColor);
    }
  }
}
