package com.goldthorp.ching.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.goldthorp.ching.R;
import com.goldthorp.ching.data.AppDatabase;
import com.goldthorp.ching.data.EntryDao;
import com.goldthorp.ching.model.Entry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntryListFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(
    @NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
    @Nullable final Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.fragment_entry_list, container, false);

    final ListView entryListView = root.findViewById(R.id.entry_list_view);

    // List of all entries
    final List<Entry> entryList = new ArrayList<>();

    // Format for displaying entry dates in list
    final SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy h:mm a", Locale.US);

    // Load all entries
    final EntryDao entryDao = AppDatabase.getInstance(requireContext()).getEntryDao();
    entryDao.getAll().observe(getViewLifecycleOwner(), entries -> {
      // Add entries to list to be referenced on item click
      entryList.clear();
      entryList.addAll(entries);
      // List of dates to display in UI
      final List<String> entryDates = new ArrayList<>();
      for (final Entry entry : entries) {
        entryDates.add(sdf.format(entry.getTimestamp()));
      }
      // Display list in UI
      final ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
        android.R.layout.simple_list_item_1, entryDates);
      entryListView.setAdapter(adapter);
    });

    // Click + button at the bottom to create a new entry
    final FloatingActionButton fab = root.findViewById(R.id.fab);
    fab.setOnClickListener(v -> {
      final Intent newEntryIntent = new Intent(requireContext(), NewEntryActivity.class);
      startActivity(newEntryIntent);
    });

    // Navigate to entry view when an entry in the list is clicked
    entryListView.setOnItemClickListener((parent, view, position, id) -> {
      final Entry entry = entryList.get(position);
      final EntryListFragmentDirections.ActionEntryListFragmentToViewEntryFragment action =
        EntryListFragmentDirections.actionEntryListFragmentToViewEntryFragment(entry.getId(),
          sdf.format(entry.getTimestamp()));
      Navigation.findNavController(root).navigate(action);
    });

    return root;
  }
}
