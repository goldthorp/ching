package com.goldthorp.ching.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.goldthorp.ching.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Set up action bar to automatically add back button after navigating from start fragment
    final NavController navController =
      Navigation.findNavController(this, R.id.nav_host_fragment_container);
    final AppBarConfiguration appBarConfiguration =
      new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
  }

  // Make the action bar back button navigate back
  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
