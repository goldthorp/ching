package com.goldthorp.ching.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.goldthorp.ching.R;

/**
 * Represents the view for the flashing square at the bottom of the dialog for generating a
 * hexagram. How lines for a hexagram are generated: There is an integer whose value is changing
 * rapidly in the range [6, 9]. The value of this integer at the time the LineGeneratorView is
 * clicked becomes the value for the next line. 6 = yin/changing, 7 = yang/unchanging,
 * 8 = yin/unchanging, 9 = yang/changing.
 */
public class LineGeneratorView extends View {

  private final Paint black;

  private final Paint white;

  /**
   * The current value will determine the color of the square and whether or not a circle is
   * drawn in the middle.
   */
  private int value;

  public LineGeneratorView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    black = new Paint(Paint.ANTI_ALIAS_FLAG);
    black.setColor(Color.BLACK);
    black.setStrokeWidth(5);
    white = new Paint(Paint.ANTI_ALIAS_FLAG);
    white.setColor(getResources().getColor(R.color.background, null));
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    if (value % 2 == 1) {
      // Odd number = yang line - fill the square with black
      black.setStyle(Paint.Style.FILL);
    } else {
      // Even number = yin line - outline the square with black
      black.setStyle(Paint.Style.STROKE);
    }
    canvas.drawRect(0, 0, getWidth(), getHeight(), black);

    // If it is a changing line, draw the circle in the middle in black/white depending on yin/yang
    final float halfWidth = getWidth() / 2;
    final float halfHeight = getHeight() / 2;
    final float sixthHeight = getHeight() / 6;
    if (value == 9) {
      canvas.drawCircle(halfWidth, halfHeight, sixthHeight, white);
    }
    if (value == 6) {
      black.setStyle(Paint.Style.FILL);
      canvas.drawCircle(halfWidth, halfHeight, sixthHeight, black);
    }
  }

  /**
   * Update the view to represent the given value.
   */
  public void setValue(final int value) {
    this.value = value;
    // Redraw view
    invalidate();
  }
}
