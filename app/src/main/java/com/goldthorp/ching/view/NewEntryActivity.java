package com.goldthorp.ching.view;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.goldthorp.ching.R;
import com.goldthorp.ching.data.AppDatabase;
import com.goldthorp.ching.data.EntryDao;
import com.goldthorp.ching.model.Entry;
import com.goldthorp.ching.model.Hexagram;
import com.goldthorp.ching.util.Index;
import com.goldthorp.ching.util.ObservableInteger;
import com.goldthorp.ching.util.Wait;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NewEntryActivity extends AppCompatActivity {

  private Entry entry;

  private LinearLayout layout;
  private RelativeLayout buttonsLayout;
  private EditText beforeTextEditText;
  private EditText afterTextEditText;
  private HexagramsView hexagramsView;
  private Button saveButton;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_entry);

    layout = findViewById(R.id.layout);
    buttonsLayout = findViewById(R.id.buttons_layout);

    // Input for text before adding hexagrams
    beforeTextEditText = findViewById(R.id.before_text_edit_text);

    // Input for text after adding hexagrams (gets added after hexagrams are added)
    afterTextEditText = new EditText(this);
    final ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    afterTextEditText.setLayoutParams(params);
    afterTextEditText.setRawInputType(
      InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

    // Button to save entry (gets added after hexagrams are added)
    saveButton = new Button(this);
    saveButton.setText(getString(R.string.save));
    saveButton.setLayoutParams(params);

    // The entry to save
    entry = new Entry();
    entry.setTimestamp(System.currentTimeMillis());

    hexagramsView = new HexagramsView(this);

    setUpManualSetHexagrams();
    setUpGenerateHexagram();

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
   * Sets up ability to manually set the hexagrams.
   */
  private void setUpManualSetHexagrams() {
    // Layout for dialog to set hexagrams
    final LinearLayout dialogLayout = new LinearLayout(this);
    final LayoutInflater inflater = getLayoutInflater();
    inflater.inflate(R.layout.dialog_add_hexagrams, dialogLayout);

    // Input for the first hexagram
    final EditText hexagramEditText =
      dialogLayout.findViewById(R.id.first_hexagram_number_edit_text);
    // Enforce range validation
    hexagramEditText.setFilters(new InputFilter[]{new HexagramInputFilter(this)});

    // Input for the second hexagram
    final EditText secondHexagramEditText =
      dialogLayout.findViewById(R.id.second_hexagram_number_edit_text);
    // Enforce range validation
    secondHexagramEditText.setFilters(new InputFilter[]{new HexagramInputFilter(this)});

    // Build the dialog
    final AlertDialog setHexagramsDialog = new AlertDialog.Builder(this)
      .setTitle(getString(R.string.enter_hexagrams))
      .setView(dialogLayout)
      // Set listener to null; we will override on show so that we control whether the dialog hides
      .setPositiveButton(getString(R.string.submit), null)
      .create();

    // Override the submit button once the dialog is showing
    setHexagramsDialog.setOnShowListener(dialog -> {
      final Button submitButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
      submitButton.setOnClickListener(v -> {
        // Validate the input
        final String firstHexagramInput = hexagramEditText.getText().toString();
        final String secondHexagramInput = secondHexagramEditText.getText().toString();
        if (StringUtils.isBlank(firstHexagramInput)) {
          // Invalid input - first hexagram must be specified
          Toast.makeText(this, this.getString(R.string.hexagram_input_blank_error),
            Toast.LENGTH_SHORT).show();
          // Dialog stays open
          return;
        }
        if (StringUtils.equals(firstHexagramInput, secondHexagramInput)) {
          // Invalid input - first hexagram must be different from second
          Toast.makeText(this, this.getString(R.string.hexagram_input_unique_error),
            Toast.LENGTH_SHORT).show();
          // Dialog stays open
          return;
        }

        // Set hexagrams on entry and in hexagramView
        setHexagrams(hexagramEditText.getText().toString(),
          secondHexagramEditText.getText().toString());

        // Long press the hexagrams to edit
        hexagramsView.setOnLongClickListener(hv -> {
          setHexagramsDialog.show();
          return true;
        });

        // Close dialog
        dialog.dismiss();
      });
    });

    setHexagramsDialog.setOnDismissListener(v -> {
      beforeTextEditText.clearFocus();
      layout.requestFocus();
      hideKeyboard();
    });

    // Button to show the dialog
    final Button addHexagramsButton = findViewById(R.id.add_hexagrams_button);
    addHexagramsButton.setOnClickListener(v -> setHexagramsDialog.show());
  }

  /**
   * Sets up the ability to generate a hexagram.
   */
  private void setUpGenerateHexagram() {
    // Layout for dialog to generate hexagram
    final LinearLayout dialogLayout = new LinearLayout(this);
    final LayoutInflater inflater = getLayoutInflater();
    inflater.inflate(R.layout.dialog_generate_hexagram, dialogLayout);

    // Build the dialog
    final AlertDialog generateHexagramDialog =
      new AlertDialog.Builder(this, R.style.GenerateHexagramDialogTheme)
        .setTitle(R.string.throw_hexagram)
        .setView(dialogLayout)
        .create();

    // The view for the hexagram we are generating
    final HexagramView hexagramView = dialogLayout.findViewById(R.id.hexagram_view);

    // The flashing square at the bottom that you tap to generate a line
    // (take the value of lineValue at the time it was tapped and use it to generate a line)
    final LineGeneratorView lineGeneratorView = dialogLayout.findViewById(R.id.line_generator_view);

    // The hexagram we are generating
    final Hexagram hexagram = new Hexagram();

    // Which line we're on (starts at bottom [0] and moves up to top [5])
    final AtomicInteger lineIndex = new AtomicInteger();
    // This is a number between 6 and 9 (inclusive). It gets updated every 20 milliseconds while
    // the dialog is showing. lineGeneratorView::setValue is passed in to make the view flash
    // with the changing values.
    final ObservableInteger lineValue = new ObservableInteger(this, lineGeneratorView::setValue);
    lineGeneratorView.setOnClickListener(v -> {
      final int value = lineValue.getValue();
      final boolean isLight = value % 2 == 1;
      final boolean isChanging = value == 6 || value == 9;
      final Hexagram.Line line = new Hexagram.Line(isLight, isChanging);
      hexagram.getLines()[lineIndex.get()] = line;
      hexagramView.getLineViews().get(lineIndex.getAndIncrement()).setLine(line);
      // If the final line has been generated
      if (lineIndex.get() == 6) {
        // Wait 500ms before hiding the dialog
        new Wait(500, generateHexagramDialog::dismiss).execute();
      }
    });

    generateHexagramDialog.setOnDismissListener(v -> {
      // If the final line has been generated, set the hexagrams in the main view when the
      // dialog hides
      if (lineIndex.get() == 6) {
        final Pair<Integer, Integer> hexagramNumbers = Index.reverseGetHexagrams(hexagram);
        setHexagrams(hexagramNumbers.first, hexagramNumbers.second);
      }
    });

    final Button generateHexagramButton = findViewById(R.id.generate_hexagram_button);
    generateHexagramButton.setOnClickListener(v -> {
      hideKeyboard();
      generateHexagramDialog.show();
      // While the dialog is showing, update lineValue to a new random value every 20ms
      final ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(() -> {
        while (generateHexagramDialog.isShowing()) {
          // Sum 3 numbers in the range [2, 3] to simulate throwing 3 dice, coins, etc.
          lineValue.set(RandomUtils.nextInt(2, 4) + RandomUtils.nextInt(2, 4) +
            RandomUtils.nextInt(2, 4));
          try {
            Thread.sleep(20);
          } catch (final InterruptedException e) {
            e.printStackTrace();
          }
        }
      });
    });
  }

  /**
   * Set the hexagrams for the entry and the view.
   *
   * @param hexagramNumberString       number for first hexagram
   * @param secondHexagramNumberString (optional) number for second hexagram
   */
  private void setHexagrams(
    final String hexagramNumberString, final String secondHexagramNumberString) {
    setHexagrams(Integer.valueOf(hexagramNumberString),
      StringUtils.isNotBlank(secondHexagramNumberString) ?
        Integer.valueOf(secondHexagramNumberString) : null);
  }

  /**
   * Set the hexagrams for the entry and the view.
   *
   * @param hexagramNumber       number for the first hexagram
   * @param secondHexagramNumber (optional) number for the second hexagram
   */
  private void setHexagrams(
    final Integer hexagramNumber, final Integer secondHexagramNumber) {
    entry.setHexagram(hexagramNumber);
    entry.setSecondHexagram(secondHexagramNumber);
    hexagramsView.setHexagrams(entry.getHexagram(), entry.getSecondHexagram());

    // If parent is null on the hexagrams view, we are adding hexagrams for the first time
    if (hexagramsView.getParent() == null) {
      // Remove enter/throw hexagram(s) buttons
      buttonsLayout.setVisibility(View.GONE);
      // Add hexagrams to layout
      layout.addView(hexagramsView);
      // Add input for text after hexagrams to layout
      layout.addView(afterTextEditText);
      // Add save button to layout
      layout.addView(saveButton);
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

  /**
   * Hides the software keyboard.
   */
  private void hideKeyboard() {
    final InputMethodManager imm =
      (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
  }
}
