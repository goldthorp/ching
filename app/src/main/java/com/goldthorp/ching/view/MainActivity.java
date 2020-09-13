package com.goldthorp.ching.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.goldthorp.ching.R;
import com.goldthorp.ching.data.BackupUtil;
import com.goldthorp.ching.view.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

  private static final int LOGIN_REQUEST_CODE = 1;

  private BackupUtil backupUtil;

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

    // Check if user is already signed in to firebase. If so, start backup immediately. If not,
    // start login activity and start backup once authentication is complete
    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser == null) {
      final Intent intent = new Intent(this, LoginActivity.class);
      startActivityForResult(intent, LOGIN_REQUEST_CODE);
    } else {
      backupUtil = new BackupUtil(this);
      backupUtil.start();
    }
  }

  // Start the backup once user is logged in
  @Override
  protected void onActivityResult(
    final int requestCode, final int resultCode, @Nullable final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
      backupUtil = new BackupUtil(this);
      backupUtil.start();
    }
  }

  // Make the action bar back button navigate back
  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    if (item.getItemId() == R.id.action_restore_backup) {
      backupUtil.restore();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
