package com.tejyash.myadapto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("AdaptoPrefs", MODE_PRIVATE);
        boolean setupComplete   = prefs.getBoolean("setupComplete", false);

        if (setupComplete) {
            // User already did setup — go straight to the launcher home
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v ->
                startActivity(new Intent(MainActivity2.this, MainActivity3.class)));
    }
}
