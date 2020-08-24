package com.goldthorp.ching.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.goldthorp.ching.R;
import com.goldthorp.ching.util.Index;

public class HexagramInfoActivity extends AppCompatActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hexagram_info);
    final Intent intent = getIntent();
    final int hexagram = intent.getIntExtra("hexagram", -1);
    if (hexagram == -1) {
      throw new IllegalArgumentException("Hexagram must be specified");
    }
    final HexagramView hexagramView = findViewById(R.id.info_hexagram_view);
    hexagramView.setHexagram(Index.getHexagrams(hexagram, null).first);

    final Button wilhelmButton = findViewById(R.id.wilhelm_button);
    final Intent browserIntent = new Intent(Intent.ACTION_VIEW,
      Uri.parse("http://www2.unipr.it/~deyoung/I_Ching_Wilhelm_Translation.html#" + hexagram));
    wilhelmButton.setOnClickListener(v -> startActivity(browserIntent));
  }
}
