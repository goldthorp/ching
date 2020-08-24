package com.goldthorp.ching.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldthorp.ching.R;
import com.goldthorp.ching.data.AppDatabase;
import com.goldthorp.ching.data.EntryPartDao;
import com.goldthorp.ching.model.EntryPart;
import com.goldthorp.ching.util.BackgroundUtil;

public class ViewEntryFragment extends Fragment implements HexagramView.HexagramClickListener {
  @Nullable
  @Override
  public View onCreateView(
    @NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
    @Nullable final Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.fragment_view_entry, container, false);

    final ViewEntryFragmentArgs args = ViewEntryFragmentArgs.fromBundle(requireArguments());

    final LinearLayout layout = root.findViewById(R.id.view_entry_layout);
    final EntryPartDao entryPartDao = AppDatabase.getInstance(requireContext()).getEntryPartDao();
    BackgroundUtil.doInBackground(() -> entryPartDao.getByEntryId(args.getEntryId()))
      .then(parts -> {
        for (final EntryPart part : parts) {
          layout.addView(new EntryPartView(requireContext(), part, this));
        }
      });

    return root;
  }

  @Override
  public void onClick(final int hexagram) {
    final Intent intent = new Intent(requireContext(), HexagramInfoActivity.class);
    intent.putExtra("hexagram", hexagram);
    startActivity(intent);
  }
}
