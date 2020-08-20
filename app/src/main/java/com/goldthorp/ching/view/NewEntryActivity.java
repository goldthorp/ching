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

  @Override
  protected void onSaveInstanceState(@NonNull final Bundle outState) {
    super.onSaveInstanceState(outState);
    setPartsOnEntry();
    outState.putSerializable("entry", entry);
  }

  @Override
  protected void onRestoreInstanceState(final Bundle savedInstanceState) {
    final Object entryObj = savedInstanceState.get("entry");
    if (entryObj != null) {
      entry = (Entry) entryObj;
      partViews.clear();
      layout.removeAllViews();
      // Populate the view with the parts from the entry in the saved instance
      final List<EntryPart> parts = entry.getParts();
      for (int i = 0; i < parts.size(); i++) {
        final EntryPart part = parts.get(i);
        final NewEntryPartView partView = new NewEntryPartView(this);
        partViews.add(partView);
        layout.addView(partView);
        partView.getTextEditText().setText(part.getText());
        // Only set the HexagramsSetListener on the last part, otherwise too many parts get added
        if (i == parts.size() - 1) {
          partView.setHexagramsSetListener(() -> {
            addPart();
            saveButton.setVisibility(View.VISIBLE);
          });
        }
        if (part.getHexagram() != null) {
          partView.setHexagrams(part.getHexagram(), part.getSecondHexagram());
        }
      }
    }
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
    setPartsOnEntry();
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> entryDao.insertWithParts(entry));
    finish();
  }

  private void setPartsOnEntry() {
    entry.getParts().clear();
    for (int i = 0; i < partViews.size(); i++) {
      final NewEntryPartView partView = partViews.get(i);
      entry.getParts().add(new EntryPart(partView.getTextEditText().getText().toString(),
        partView.getHexagrams(), i));
    }
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
