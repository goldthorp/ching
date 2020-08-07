package com.goldthorp.ching.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldthorp.ching.R;
import com.goldthorp.ching.model.EntryPart;

public class EntryPartView extends LinearLayout {

  TextView textView;
  HexagramsView hexagramsView;

  public EntryPartView(final Context context) {
    super(context);
    inflate(context, R.layout.view_entry_part, this);
    textView = findViewById(R.id.part_text_text_view);
    hexagramsView = findViewById(R.id.hexagrams_view);
  }

  public EntryPartView(final Context context, final EntryPart entryPart) {
    this(context);
    textView.setText(entryPart.getText());
    if (entryPart.getHexagram() != null) {
      hexagramsView.setHexagrams(entryPart.getHexagram(), entryPart.getSecondHexagram());
    }
  }
}
