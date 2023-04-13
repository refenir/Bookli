package com.example.bookli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.bookli.ui.login.LoginActivity;

public class SplashActivity extends Activity {
    public final String sharedPrefFile = "com.example.android.mainsharedpref";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
