package com.tejyash.myadapto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity4 extends AppCompatActivity {

    private AccessibilityPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);

        prefs = AccessibilityPreferences.get(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.button5), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SeekBar   seekTextSize   = findViewById(R.id.seekBar2);
        SeekBar   seekIconSize   = findViewById(R.id.seekBar3);
        Switch    switchContrast = findViewById(R.id.switch1);
        TextView  tvLargeA      = findViewById(R.id.textView20);
        ImageView imgIconBig    = findViewById(R.id.imageView8);
        CardView  card4         = findViewById(R.id.card4);
        TextView  tvContrastLabel = findViewById(R.id.textView16);
        TextView  tvContrastSub   = findViewById(R.id.textView22);

        // ── Restore saved values so seekbars reflect current prefs ──
        seekTextSize.setMax(3);
        seekTextSize.setProgress(prefs.getFontStep());
        tvLargeA.setTextSize(prefs.getFontSizeSp() + 10);

        seekIconSize.setMax(3);
        seekIconSize.setProgress(prefs.getIconStep());
        applyIconPreview(imgIconBig, prefs.getIconSizeDp());

        // ── Text size — save on every drag, update preview ──────────
        seekTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                float sp = AccessibilityPreferences.TEXT_SIZES[progress];
                tvLargeA.setTextSize(sp + 10);
                if (fromUser) prefs.setFontStep(progress); // persists + notifies HomeActivity
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s)  {}
        });

        // ── Icon size — save on every drag, update preview ───────────
        seekIconSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                int dp = AccessibilityPreferences.ICON_SIZES_DP[progress];
                applyIconPreview(imgIconBig, dp);
                if (fromUser) prefs.setIconStep(progress); // persists + notifies HomeActivity
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s)  {}
        });

        // ── High contrast toggle (visual only — no persistence needed) ──
        switchContrast.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                card4.setCardBackgroundColor(0xFF000000);
                tvContrastLabel.setTextColor(0xFFFFFFFF);
                tvContrastSub.setTextColor(0xFFFFFFFF);
                tvContrastLabel.setTypeface(null, Typeface.BOLD);
            } else {
                card4.setCardBackgroundColor(0xFFFFFFFF);
                tvContrastLabel.setTextColor(0xFF0F120B);
                tvContrastSub.setTextColor(0xFF000000);
                tvContrastLabel.setTypeface(null, Typeface.NORMAL);
            }
        });

        // ── Continue button ──────────────────────────────────────────
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            // Mark setup complete so next cold start goes straight to HomeActivity
            getSharedPreferences("AdaptoPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("setupComplete", true)
                    .apply();

            Intent intent = new Intent(MainActivity4.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void applyIconPreview(ImageView img, int dp) {
        int px = Math.round(dp * getResources().getDisplayMetrics().density);
        android.view.ViewGroup.LayoutParams lp = img.getLayoutParams();
        lp.width  = px;
        lp.height = px;
        img.setLayoutParams(lp);
    }
}
