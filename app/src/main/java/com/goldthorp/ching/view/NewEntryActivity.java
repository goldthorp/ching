package com.goldthorp.ching.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.goldthorp.ching.R;
import com.goldthorp.ching.data.AppDatabase;
import com.goldthorp.ching.data.EntryDao;
import com.goldthorp.ching.data.EntryPartDao;
import com.goldthorp.ching.model.Entry;
import com.goldthorp.ching.model.EntryPart;
import com.goldthorp.ching.util.BackgroundUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class NewEntryActivity extends AppCompatActivity
  implements HexagramView.HexagramClickListener {

  private EntryDao entryDao;

  private Entry entry;

  private LinearLayout layout;
  private MenuItem saveMenuItem;

  private final List<NewEntryPartView> partViews = new ArrayList<>();

  private final String TAG = "NewEntryActivity";

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_entry);

    entryDao = AppDatabase.getInstance(this).getEntryDao();

    layout = findViewById(R.id.layout);

    // Check if there's a draft saved and if so restore it instead of creating a new entry
    BackgroundUtil.doInBackground(entryDao::getDraft).then(draftEntry -> {
      if (draftEntry == null) {
        entry = new Entry();
        entry.setTimestamp(System.currentTimeMillis());
        addPart();
        Log.d(TAG, "no draft");
      } else {
        entry = draftEntry;
        // Load the parts for the draft
        final EntryPartDao entryPartDao = AppDatabase.getInstance(this).getEntryPartDao();
        BackgroundUtil.doInBackground(() -> entryPartDao.getByEntryId(entry.getId()))
          .then(parts -> {
            entry.getParts().addAll(parts);
            showParts();
            Log.d(TAG, "reopening draft " + draftEntry);
          });
      }
    });
  }

  @Override
  protected void onStop() {
    super.onStop();
    // If the activity is stopping and it is NOT because finish() was called, save the entry as
    // a draft so it can be restored later
    if (!isFinishing()) {
      performSave(true);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.new_entry_menu, menu);
    saveMenuItem = menu.getItem(0);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    if (item.getItemId() == saveMenuItem.getItemId()) {
      // Save entry on save click then kill the activity
      saveEntry();
    }
    return true;
  }

  /**
   * Render the entry's parts.
   */
  private void showParts() {
    final List<EntryPart> parts = entry.getParts();
    for (int i = 0; i < parts.size(); i++) {
      final EntryPart part = parts.get(i);
      final NewEntryPartView partView = new NewEntryPartView(this, this);
      partViews.add(partView);
      layout.addView(partView);
      partView.getTextEditText().setText(part.getText());
      // Only set the HexagramsSetListener on the last part, otherwise too many parts get added
      if (i == parts.size() - 1) {
        partView.setHexagramsSetListener(this::addPart);
      }
      if (part.getHexagram() != null) {
        partView.setHexagrams(part.getHexagram(), part.getSecondHexagram());
        saveMenuItem.setVisible(true);
      }
    }
  }

  /**
   * Add a new part to the view.
   */
  private void addPart() {
    final NewEntryPartView partView = new NewEntryPartView(this, this);
    partViews.add(partView);
    layout.addView(partView);
    partView.setHexagramsSetListener(() -> {
      addPart();
      saveMenuItem.setVisible(true);
    });
  }

  private void saveEntry() {
    performSave(false);
    finish();
  }

  private void performSave(final boolean isDraft) {
    entry.setDraft(isDraft);
    setPartsOnEntry();
    Log.d(TAG, "saving entry " + entry);
    BackgroundUtil.doInBackgroundNow(() -> entryDao.saveWithParts(entry));
  }

  /**
   * Set the parts in the view on the entry to be saved.
   */
  private void setPartsOnEntry() {
    entry.getParts().clear();
    for (final NewEntryPartView partView : partViews) {
      final String text = partView.getTextEditText().getText().toString();
      if (StringUtils.isNotBlank(text) || partView.getHexagrams() != null) {
        entry.addPart(partView.getTextEditText().getText().toString(), partView.getHexagrams());
      }
    }
  }

  // Display a confirm dialog before exiting on back-press
  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
      .setTitle(getString(R.string.confirm_discard_title))
      .setMessage(getString(R.string.confirm_discard_message))
      .setPositiveButton("Yes", (dialog, which) -> {
        // If the entry has already been saved as a draft, delete the draft.
        if (entry.getId() != null) {
          BackgroundUtil.doInBackgroundNow(() -> entryDao.deleteWithParts(entry));
          Log.d(TAG, "deleting draft");
        }
        finish();
      })
      .setNegativeButton("No", null)
      .show();
  }

  @Override
  public void onClick(final int hexagram) {
    final Intent intent = new Intent(this, HexagramInfoActivity.class);
    intent.putExtra("hexagram", hexagram);
    startActivity(intent);
  }
}
