package com.goldthorp.ching.view;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.goldthorp.ching.R;
import com.goldthorp.ching.model.Hexagram;
import com.goldthorp.ching.util.Index;
import com.goldthorp.ching.util.ObservableInteger;
import com.goldthorp.ching.util.Wait;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

public class NewEntryPartView extends LinearLayout {

  @Getter
  private final EditText textEditText;
  private final HexagramsView hexagramsView;
  private final ImageView addHexagrams;
  private final LinearLayout layout;

  @Getter
  private Pair<Integer, Integer> hexagrams;

  private final AlertDialog addDialog;

  @Setter
  private HexagramsSetListener hexagramsSetListener;

  public NewEntryPartView(final Context context) {
    super(context);
    inflate(context, R.layout.view_new_entry_part, this);
    textEditText = findViewById(R.id.new_entry_part_edit_text);
    hexagramsView = findViewById(R.id.new_entry_part_hexagrams);
    addHexagrams = findViewById(R.id.new_entry_part_add_hexagrams);
    layout = findViewById(R.id.new_entry_part_layout);

    final LinearLayout dialogLayout = new LinearLayout(context);
    inflate(context, R.layout.dialog_add_hexagrams, dialogLayout);

    final Button addHexagramsEnterButton =
      dialogLayout.findViewById(R.id.add_hexagrams_dialog_enter);
    final Button addHexagramsThrowButton =
      dialogLayout.findViewById(R.id.add_hexagrams_dialog_throw);

    addDialog = new AlertDialog.Builder(context)
      .setTitle(getResources().getString(R.string.add_a_cast))
      .setView(dialogLayout).create();

    addHexagrams.setOnClickListener(v -> {
      addDialog.show();
    });

    final AlertDialog manualSetHexagramsDialog = getManualSetHexagramsDialog();
    addHexagramsEnterButton.setOnClickListener(v -> {
      addDialog.dismiss();
      manualSetHexagramsDialog.show();
    });

    final AlertDialog generateHexagramDialog = getGenerateHexagramDialog();
    addHexagramsThrowButton.setOnClickListener(v -> {
      addDialog.dismiss();
      hideKeyboard();
      generateHexagramDialog.show();
    });
  }

  /**
   * Sets up ability to manually set the hexagrams.
   */
  private AlertDialog getManualSetHexagramsDialog() {
    // Layout for dialog to set hexagrams
    final LinearLayout dialogLayout = new LinearLayout(getContext());
    inflate(getContext(), R.layout.dialog_enter_hexagrams, dialogLayout);

    // Input for the first hexagram
    final EditText hexagramEditText =
      dialogLayout.findViewById(R.id.first_hexagram_number_edit_text);
    // Enforce range validation
    hexagramEditText.setFilters(new InputFilter[]{new HexagramInputFilter(getContext())});

    // Input for the second hexagram
    final EditText secondHexagramEditText =
      dialogLayout.findViewById(R.id.second_hexagram_number_edit_text);
    // Enforce range validation
    secondHexagramEditText.setFilters(new InputFilter[]{new HexagramInputFilter(getContext())});

    // Build the dialog
    final AlertDialog setHexagramsDialog =
      new AlertDialog.Builder(getContext())
        .setTitle(getResources().getString(R.string.enter_hexagrams))
        .setView(dialogLayout)
        // Set listener to null; we will override on show so that we control whether the dialog hides
        .setPositiveButton(getResources().getString(R.string.submit), null)
        .create();

    // Override the submit button once the dialog is showing
    setHexagramsDialog.setOnShowListener(dialog -> {
      final Button submitButton =
        ((AlertDialog) dialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
      submitButton.setOnClickListener(v -> {
        // Validate the input
        final String firstHexagramInput = hexagramEditText.getText().toString();
        final String secondHexagramInput = secondHexagramEditText.getText().toString();
        if (StringUtils.isBlank(firstHexagramInput)) {
          // Invalid input - first hexagram must be specified
          Toast.makeText(getContext(),
            getResources().getString(R.string.hexagram_input_blank_error),
            Toast.LENGTH_SHORT).show();
          // Dialog stays open
          return;
        }
        if (StringUtils.equals(firstHexagramInput, secondHexagramInput)) {
          // Invalid input - first hexagram must be different from second
          Toast.makeText(getContext(),
            getResources().getString(R.string.hexagram_input_unique_error),
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
      textEditText.clearFocus();
      layout.requestFocus();
      hideKeyboard();
    });

    return setHexagramsDialog;
  }

  /**
   * Sets up the ability to generate a hexagram.
   */
  private AlertDialog getGenerateHexagramDialog() {
    // Layout for dialog to generate hexagram
    final LinearLayout dialogLayout = new LinearLayout(getContext());
    inflate(getContext(), R.layout.dialog_generate_hexagram, dialogLayout);

    // Build the dialog
    final AlertDialog generateHexagramDialog =
      new AlertDialog.Builder(getContext(), R.style.GenerateHexagramDialogTheme)
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
    final ObservableInteger lineValue = new ObservableInteger((Activity) getContext(), lineGeneratorView::setValue);
    lineGeneratorView.setOnClickListener(v -> {
      if (lineIndex.get() == 6) {
        return;
      }
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

    generateHexagramDialog.setOnShowListener(dialog -> {
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

    generateHexagramDialog.setOnDismissListener(v -> {
      // If the final line has been generated, set the hexagrams in the main view when the
      // dialog hides
      if (lineIndex.get() == 6) {
        final Pair<Integer, Integer> hexagramNumbers = Index.reverseGetHexagrams(hexagram);
        setHexagrams(hexagramNumbers.first, hexagramNumbers.second);
      }
    });

    return generateHexagramDialog;
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
  public void setHexagrams(
    final Integer hexagramNumber, final Integer secondHexagramNumber) {
    hexagrams = Pair.create(hexagramNumber, secondHexagramNumber);
    hexagramsView.setHexagrams(hexagramNumber, secondHexagramNumber);

    if (hexagramsView.getVisibility() == View.GONE) {
      // Remove enter/throw hexagram(s) buttons
      addHexagrams.setVisibility(View.GONE);
      // Add hexagrams to layout
      hexagramsView.setVisibility(View.VISIBLE);
      if (hexagramsSetListener != null) {
        hexagramsSetListener.onHexagramsSet();
      }
    }
  }

  /**
   * Hides the software keyboard.
   */
  private void hideKeyboard() {
    final InputMethodManager imm =
      (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
  }

  public interface HexagramsSetListener {
    void onHexagramsSet();
  }
}
