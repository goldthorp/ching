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
    @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_view_entry, container, false);

    ViewEntryFragmentArgs args = ViewEntryFragmentArgs.fromBundle(requireArguments());
    Entry entry = args.getEntry();

    TextView beforeTextTextView = root.findViewById(R.id.before_text_text_view);
    beforeTextTextView.setText(entry.getBeforeText());

    HexagramsView hexagramsView = root.findViewById(R.id.hexagrams_view);
    hexagramsView.addFirstHexagram(entry.getHexagram());
    if (entry.getSecondHexagram() != null) {
      hexagramsView.addSecondHexagram(entry.getSecondHexagram());
    }

    TextView afterTextTextView = root.findViewById(R.id.after_text_text_view);
    afterTextTextView.setText(entry.getAfterText());

    return root;
  }
}
