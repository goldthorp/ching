package com.goldthorp.ching.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goldthorp.ching.R;

public class HexagramsView extends LinearLayout {

  public HexagramsView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public HexagramsView(Context context, Integer hexagramNumber, Integer secondHexagramNumber) {
    super(context);
    setOrientation(HORIZONTAL);
    int layoutPaddingPx = getResources().getDimensionPixelSize(R.dimen.layout_padding);
    setPadding(layoutPaddingPx, layoutPaddingPx, layoutPaddingPx, layoutPaddingPx);

    addFirstHexagram(hexagramNumber);

    if (secondHexagramNumber != null) {
      addSecondHexagram(secondHexagramNumber);
    }
  }

  public void addFirstHexagram(int hexagramNumber) {
    HexagramView hexagram1 = new HexagramView(getContext(), hexagramNumber);
    LayoutParams hex1Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5);
    hexagram1.setLayoutParams(hex1Params);
    addView(hexagram1);
  }

  public void addSecondHexagram(int hexagramNumber) {
    ImageView arrow = new ImageView(getContext());
    arrow.setImageResource(R.drawable.next);
    LayoutParams arrowParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2);
    arrow.setLayoutParams(arrowParams);
    int arrowPaddingPx = getResources().getDimensionPixelSize(R.dimen.arrow_padding);
    arrow.setPadding(arrowPaddingPx, 0, arrowPaddingPx, 0);
    addView(arrow);

    HexagramView hexagram2 = new HexagramView(getContext(), hexagramNumber);
    LayoutParams hex2Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5);
    hexagram2.setLayoutParams(hex2Params);
    addView(hexagram2);
  }
}
