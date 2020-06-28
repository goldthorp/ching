package com.goldthorp.ching.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.goldthorp.ching.R;

public class LineView extends View {

  boolean isLight;

  Paint white;

  Rect rectangle;

  private LineView(Context context) {
    super(context);
    rectangle = new Rect(getLeft(), getTop(), getRight(), getBottom());

    white = new Paint();
    white.setColor(getResources().getColor(R.color.background, null));
  }

  public LineView(Context context, ViewGroup.LayoutParams layoutParams, boolean isLight) {
    this(context);
    this.isLight = isLight;
    setLayoutParams(layoutParams);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawColor(Color.BLACK);
    if (!isLight) {
      int half = getWidth() / 2;
      int left = half - getWidth() / 20;
      int right = half + getWidth() / 20;
      canvas.drawRect(left, 0, right, getHeight(), white);
      canvas.drawRect(rectangle, white);
    }
  }
}
