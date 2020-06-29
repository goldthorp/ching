package com.goldthorp.ching.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.goldthorp.ching.R;
import com.goldthorp.ching.data.AppDatabase;
import com.goldthorp.ching.data.EntryDao;
import com.goldthorp.ching.model.Entry;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NewEntryActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_entry);

    LinearLayout layout = findViewById(R.id.layout);

    EditText beforeTextEditText = findViewById(R.id.before_text_edit_text);

    LayoutInflater inflater = getLayoutInflater();
    LinearLayout dialogLayout = new LinearLayout(this);
    inflater.inflate(R.layout.dialog_add_hexagrams, dialogLayout);
    EditText hexagramEditText = dialogLayout.findViewById(R.id.first_hexagram_number_edit_text);
    EditText secondHexagramEditText = dialogLayout.findViewById(R.id.second_hexagram_number_edit_text);

    EditText afterTextEditText = new EditText(this);
    ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    afterTextEditText.setLayoutParams(params);
    afterTextEditText.setRawInputType(
      InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

    Button saveButton = new Button(this);
    saveButton.setText("save");
    saveButton.setLayoutParams(params);

    Entry entry = new Entry();

    Button addHexagramsButton = findViewById(R.id.add_hexagrams_button);
    addHexagramsButton.setOnClickListener(v ->
      new AlertDialog.Builder(this)
        .setTitle("Enter Hexagram(s)")
        .setView(dialogLayout)
        .setPositiveButton("Submit", ((dialog, which) -> {
          addHexagramsButton.setVisibility(View.GONE);
          entry.setHexagram(Integer.valueOf(hexagramEditText.getText().toString()));
          String secondHexagramNumberString = secondHexagramEditText.getText().toString();
          if (StringUtils.isNotBlank(secondHexagramNumberString)) {
            entry.setSecondHexagram(Integer.valueOf(secondHexagramNumberString));
          }
          HexagramsView hexagramsView = new HexagramsView(this, entry.getHexagram(), entry.getSecondHexagram());
          LinearLayout.LayoutParams hexParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getResources().getDimensionPixelSize(R.dimen.hexagrams_height));
          hexagramsView.setLayoutParams(hexParams);
          layout.addView(hexagramsView);

          layout.addView(afterTextEditText);
          layout.addView(saveButton);
        })).show());

    EntryDao entryDao = AppDatabase.getInstance(this).getEntryDao();

    saveButton.setOnClickListener(v -> {
      entry.setTimestamp(System.currentTimeMillis());
      entry.setBeforeText(beforeTextEditText.getText().toString());
      entry.setAfterText(afterTextEditText.getText().toString());
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(() -> entryDao.insert(entry));
      finish();
    });
  }

}
