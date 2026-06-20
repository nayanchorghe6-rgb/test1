package com.tejyash.myadapto;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.button5), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CardView card1;
        card1 = findViewById(R.id.card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "Card click", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity3.this, MainActivity4.class);
                startActivity(i);

            }
        });
        CardView card2;
        card2 = findViewById(R.id.card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "Card Click", Toast.LENGTH_SHORT).show();
                Intent d = new Intent(MainActivity3.this, MainActivity4.class);
                startActivity(d);
            }
        });
        CardView card3;
        card3 = findViewById(R.id.card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "Card Click", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity3.this, MainActivity7.class);
                startActivity(i);
            }
        });
        CardView card4;
        card4= findViewById(R.id.card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "Card Click", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity3 .this, MainActivity4.class);
                startActivity(i);
            }
        });
    }
}
