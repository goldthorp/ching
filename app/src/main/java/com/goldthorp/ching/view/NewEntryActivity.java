package com.goldthorp.ching.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.goldthorp.ching.R;
import com.goldthorp.ching.data.AppDatabase;
import com.goldthorp.ching.data.EntryDao;
import com.goldthorp.ching.model.Entry;
import com.goldthorp.ching.model.EntryPart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewEntryActivity extends AppCompatActivity {

  private Entry entry;

  private LinearLayout layout;
  private Button saveButton;

  private final List<NewEntryPartView> partViews = new ArrayList<>();

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_entry);

    layout = findViewById(R.id.layout);

    // Button to save entry (gets added after hexagrams are added)
    saveButton = findViewById(R.id.save_entry_button);

    // The entry to save
    entry = new Entry();
    entry.setTimestamp(System.currentTimeMillis());

    addPart();

    // Save entry on save click then kill the activity
    saveButton.setOnClickListener(v -> saveEntry());
  }

  // If the system is killing the app for memory and a hexagram has
  // already been added, just save the entry and kill the activity
  @Override
  protected void onSaveInstanceState(@NonNull final Bundle outState) {
    super.onSaveInstanceState(outState);
//    if (partViews.get(0).getHexagrams() != null) {
//      saveEntry();
//    }
  }

  private void addPart() {
    final NewEntryPartView partView = new NewEntryPartView(this);
    partViews.add(partView);
    layout.addView(partView);
    partView.setHexagramsSetListener(() -> {
      addPart();
      saveButton.setVisibility(View.VISIBLE);
    });
  }

  private void saveEntry() {
    final EntryDao entryDao = AppDatabase.getInstance(this).getEntryDao();
    for (int i = 0; i < partViews.size(); i++) {
      final NewEntryPartView partView = partViews.get(i);
      entry.getParts().add(new EntryPart(partView.getTextEditText().getText().toString(),
        partView.getHexagrams(), i));
    }
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> entryDao.insertWithParts(entry));
    finish();
  }

  // Display a confirm dialog before exiting on back-press
  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
      .setTitle(getString(R.string.confirm_discard_title))
      .setMessage(getString(R.string.confirm_discard_message))
      .setPositiveButton("Yes", (dialog, which) -> finish())
      .setNegativeButton("No", null)
      .show();
  }
}
