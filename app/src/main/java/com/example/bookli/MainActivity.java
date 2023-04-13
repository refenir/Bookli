package com.example.bookli;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.bookli.ui.home.HomeFragment;
import com.example.bookli.ui.home.HomeViewModel;
import com.example.bookli.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookli.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    EditText timePicker;
    public final String sharedPrefFile = "com.example.android.mainsharedpref";
    SharedPreferences mPreferences;
    BottomSheetDialog bottomSheetDialog;
    String name;
    int studentId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting name and student id from login page
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            studentId = extras.getInt("studentId");
        }

//        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
//        // app is already in use, or app is used for the first time
//        name = mPreferences.getString("name", name);
//        studentId = mPreferences.getInt("studentId", studentId);

        if (name == null || studentId == -1) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_content);

    }
    // store previous inputs and show after closing and reopening the app (shared preferences)
    @Override
    protected void onPause(){
        super.onPause();
//        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//        preferencesEditor.putString("name", name);
//        preferencesEditor.putInt("studentId", studentId);
//        preferencesEditor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.qr_toggle) {
            scanCode();
        }
        return super.onOptionsItemSelected(item);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up for flash on");
        options.setOrientationLocked(true);
        options.setBeepEnabled(false);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
}