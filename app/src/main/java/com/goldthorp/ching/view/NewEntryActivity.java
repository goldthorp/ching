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
    final Entry entry = new Entry();

    // Set up onClick for add hexagrams button
    final Button addHexagramsButton = findViewById(R.id.add_hexagrams_button);
    addHexagramsButton.setOnClickListener(v ->
      // Display dialog to add the hexagrams
      new AlertDialog.Builder(this)
        .setTitle(getString(R.string.enter_hexagrams))
        .setView(dialogLayout)
        .setPositiveButton(getString(R.string.submit), ((dialog, which) -> {
          // Submit pressed in dialog
          // Remove add hexagrams button
          addHexagramsButton.setVisibility(View.GONE);
          // Set first hexagram number on Entry
          entry.setHexagram(Integer.valueOf(hexagramEditText.getText().toString()));
          // If second hexagram specified, set it on entry
          String secondHexagramNumberString = secondHexagramEditText.getText().toString();
          if (StringUtils.isNotBlank(secondHexagramNumberString)) {
            entry.setSecondHexagram(Integer.valueOf(secondHexagramNumberString));
          }
          // Add hexagrams to layout
          HexagramsView hexagramsView =
            new HexagramsView(this, entry.getHexagram(), entry.getSecondHexagram());
          LinearLayout.LayoutParams hexParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getResources().getDimensionPixelSize(R.dimen.hexagrams_height));
          hexagramsView.setLayoutParams(hexParams);
          layout.addView(hexagramsView);

          // Add input for text after/save button to layout
          layout.addView(afterTextEditText);
          layout.addView(saveButton);
        })).show());

    // Save entry on save click then kill the activity
    final EntryDao entryDao = AppDatabase.getInstance(this).getEntryDao();
    saveButton.setOnClickListener(v -> {
      entry.setTimestamp(System.currentTimeMillis());
      entry.setBeforeText(beforeTextEditText.getText().toString());
      entry.setAfterText(afterTextEditText.getText().toString());
      final ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(() -> entryDao.insert(entry));
      finish();
    });
  }

}
