package com.goldthorp.ching.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldthorp.ching.R;
import com.goldthorp.ching.model.Entry;

public class ViewEntryFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(
    @NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
    @Nullable final Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.fragment_view_entry, container, false);

    final ViewEntryFragmentArgs args = ViewEntryFragmentArgs.fromBundle(requireArguments());
    final Entry entry = args.getEntry();

    final TextView beforeTextTextView = root.findViewById(R.id.before_text_text_view);
    beforeTextTextView.setText(entry.getBeforeText());

    final HexagramsView hexagramsView = root.findViewById(R.id.hexagrams_view);
    hexagramsView.setHexagrams(entry.getHexagram(), entry.getSecondHexagram());

    final TextView afterTextTextView = root.findViewById(R.id.after_text_text_view);
    afterTextTextView.setText(entry.getAfterText());

    return root;
  }
}
