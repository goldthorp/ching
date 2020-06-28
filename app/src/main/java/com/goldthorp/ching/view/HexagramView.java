package com.goldthorp.ching.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.goldthorp.ching.R;
import com.goldthorp.ching.util.Index;

public class HexagramView extends LinearLayout {

  public HexagramView(Context context) {
    super(context);
    setOrientation(VERTICAL);
  }

  public HexagramView(Context context, int hexagramNumber) {
    this(context);
    boolean[] hexagram = Index.getHexagram(hexagramNumber);
    for (int i = 0; i < hexagram.length; i++) {
      LayoutParams lineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
      if (i != 0) {
        lineParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.line_padding);
      }
      addView(new LineView(context, lineParams, hexagram[i]), 0);
    }
  }
}
