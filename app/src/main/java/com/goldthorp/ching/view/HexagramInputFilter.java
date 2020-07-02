package com.goldthorp.ching.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import com.goldthorp.ching.R;

/**
 * InputFilter to enforce the valid range for hexagram numbers ([1, 64]).
 */
public class HexagramInputFilter implements InputFilter {

  private final Context context;

  HexagramInputFilter(final Context context) {
    this.context = context;
  }

  @Override
  public CharSequence filter(
    final CharSequence source, final int start, final int end, final Spanned dest,
    final int dstart, final int dend) {
    try {
      final StringBuilder sb = new StringBuilder(dest.toString());
      sb.insert(dstart, source);
      final int input = Integer.parseInt(sb.toString());
      if (isInRange(input)) {
        return null;
      }
    } catch (final NumberFormatException e) {
      // Input is not a valid integer
    }
    Toast.makeText(context, context.getString(R.string.hexagram_input_range_error),
      Toast.LENGTH_SHORT).show();
    return "";
  }

  private boolean isInRange(final int input) {
    return input >= 1 && input <= 64;
  }
}