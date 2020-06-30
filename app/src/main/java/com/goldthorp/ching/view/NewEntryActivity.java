package com.goldthorp.ching.view;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.goldthorp.ching.R;
import com.goldthorp.ching.data.AppDatabase;
import com.goldthorp.ching.data.EntryDao;
import com.goldthorp.ching.model.Entry;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewEntryActivity extends AppCompatActivity {

  private Entry entry;

  private HexagramsView hexagramsView;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_entry);

    final LinearLayout layout = findViewById(R.id.layout);

    // Input for text before adding hexagrams
    final EditText beforeTextEditText = findViewById(R.id.before_text_edit_text);

    // Dialog for adding hexagrams
    final LayoutInflater inflater = getLayoutInflater();
    final LinearLayout dialogLayout = new LinearLayout(this);
    inflater.inflate(R.layout.dialog_add_hexagrams, dialogLayout);
    final EditText hexagramEditText = dialogLayout.findViewById(R.id.first_hexagram_number_edit_text);
    final EditText secondHexagramEditText = dialogLayout.findViewById(R.id.second_hexagram_number_edit_text);

    // Input for text after adding hexagrams (gets added after hexagrams are added)
    final EditText afterTextEditText = new EditText(this);
    final ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    afterTextEditText.setLayoutParams(params);
    afterTextEditText.setRawInputType(
      InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

    // Button to save entry (gets added after hexagrams are added)
    final Button saveButton = new Button(this);
    saveButton.setText(getString(R.string.save));
    saveButton.setLayoutParams(params);

    // The entry to save
    entry = new Entry();
    entry.setTimestamp(System.currentTimeMillis());

    hexagramsView = new HexagramsView(this);

    // Button to add hexagrams
    final Button addHexagramsButton = findViewById(R.id.add_hexagrams_button);

    // Dialog to set the hexagrams
    final AlertDialog setHexagramsDialog = new AlertDialog.Builder(this)
      .setTitle(getString(R.string.enter_hexagrams))
      .setView(dialogLayout)
      .setPositiveButton(getString(R.string.submit), ((dialog, which) -> {
        // Set hexagrams on entry and in hexagramView
        setHexagrams(hexagramEditText.getText().toString(),
          secondHexagramEditText.getText().toString());

        // If parent is null on the hexagrams view, we are adding hexagrams for the first time
        if (hexagramsView.getParent() == null) {
          // Remove add hexagrams button
          addHexagramsButton.setVisibility(View.GONE);
          // Add hexagrams to layout
          layout.addView(hexagramsView);
          // Add input for text after/save button to layout
          layout.addView(afterTextEditText);
          layout.addView(saveButton);
        }
      })).create();

    // Add hexagrams click listener
    addHexagramsButton.setOnClickListener(v -> setHexagramsDialog.show());

    // Edit hexagrams click listener
    hexagramsView.setOnLongClickListener(v -> {
      setHexagramsDialog.show();
      return true;
    });

    // Save entry on save click then kill the activity
    final EntryDao entryDao = AppDatabase.getInstance(this).getEntryDao();
    saveButton.setOnClickListener(v -> {
      entry.setBeforeText(beforeTextEditText.getText().toString());
      entry.setAfterText(afterTextEditText.getText().toString());
      final ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(() -> entryDao.insert(entry));
      finish();
    });
  }

  /**
   * Set the hexagrams for the entry and the view.
   *
   * @param hexagramNumberString       number for first hexagram
   * @param secondHexagramNumberString (optional) number for second hexagram
   */
  public void setHexagrams(
    final String hexagramNumberString, final String secondHexagramNumberString) {
    entry.setHexagram(Integer.valueOf(hexagramNumberString));
    // If second hexagram specified, set it on entry
    if (StringUtils.isNotBlank(secondHexagramNumberString)) {
      entry.setSecondHexagram(Integer.valueOf(secondHexagramNumberString));
    } else {
      entry.setSecondHexagram(null);
    }
    hexagramsView.setHexagrams(entry.getHexagram(), entry.getSecondHexagram());
  }
}
