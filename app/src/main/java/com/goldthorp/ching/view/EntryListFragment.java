package com.goldthorp.ching.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_entry_list, container, false);

    ListView entryListView = root.findViewById(R.id.entry_list_view);

    List<Entry> entryList = new ArrayList<>();

    EntryDao entryDao = AppDatabase.getInstance(requireContext()).getEntryDao();
    final SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy h:mm a", Locale.US);
    entryDao.getAll().observe(getViewLifecycleOwner(), entries -> {
      entryList.addAll(entries);
      List<String> entryDates = new ArrayList<>();
      for (Entry entry : entries) {
        entryDates.add(sdf.format(entry.getTimestamp()));
      }
      final ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
        android.R.layout.simple_list_item_1, entryDates);
      entryListView.setAdapter(adapter);
    });

    FloatingActionButton fab = root.findViewById(R.id.fab);
    fab.setOnClickListener(v -> {
      Intent newEntryIntent = new Intent(requireContext(), NewEntryActivity.class);
      startActivity(newEntryIntent);
    });


    entryListView.setOnItemClickListener((parent, view, position, id) -> {
      Entry entry = entryList.get(position);
      EntryListFragmentDirections.ActionEntryListFragmentToViewEntryFragment action =
        EntryListFragmentDirections.actionEntryListFragmentToViewEntryFragment(entry);
      Navigation.findNavController(root).navigate(action);
    });

    return root;
  }
}
