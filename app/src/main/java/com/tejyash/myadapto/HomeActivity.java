package com.tejyash.myadapto;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements AccessibilityPreferences.OnPrefsChangedListener,
        TextToSpeech.OnInitListener {

    private AppGridAdapter           adapter;
    private GridLayoutManager        layoutManager;
    private List<AppInfo>            allApps = new ArrayList<>();
    private AccessibilityPreferences prefs;
    private TextToSpeech             tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prefs = AccessibilityPreferences.get(this);
        tts = new TextToSpeech(this, this);

        // ── App grid ─────────────────────────────────────────────
        RecyclerView rvApps = findViewById(R.id.rv_apps);
        adapter       = new AppGridAdapter(this);
        layoutManager = new GridLayoutManager(this, prefs.getGridCols());

        rvApps.setLayoutManager(layoutManager);
        rvApps.setAdapter(adapter);
        rvApps.setHasFixedSize(false);

        adapter.setOnAppClickListener(this::launchApp);

        loadInstalledApps();

        // ── Search bar ───────────────────────────────────────────
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                filterApps(q);
                return true;
            }
        });

        // ── Settings FAB → MainActivity4 ─────────────────────────
        findViewById(R.id.fab_settings).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity4.class)));

        // ── TTS Button ───────────────────────────────────────────
        Button btnSpeak = findViewById(R.id.btn_speak);
        if (btnSpeak != null) {
            btnSpeak.setOnClickListener(v ->
                    speak("Welcome to Adapto. You have " + allApps.size() + " apps installed.")
            );
        }

        // ── Bottom dock ──────────────────────────────────────────
        setupDock();
    }

    // ── TTS init callback ────────────────────────────────────────
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
        }
    }

    // ── Speak helper ─────────────────────────────────────────────
    private void speak(String text) {
        if (tts != null && !text.isEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_uid");
        }
    }

    // ── Lifecycle ────────────────────────────────────────────────
    @Override
    protected void onResume() {
        super.onResume();
        prefs.setListener(this);
        onPrefsChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.clearListener();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    // ── AccessibilityPreferences listener ────────────────────────
    @Override
    public void onPrefsChanged() {
        int cols = prefs.getGridCols();
        if (layoutManager.getSpanCount() != cols) {
            layoutManager.setSpanCount(cols);
        }
        adapter.notifyResized();
    }

    // ── HOME button: never go back ───────────────────────────────
    @Override
    public void onBackPressed() {
        // Intentionally empty
    }

    // ── Load installed apps ──────────────────────────────────────
    private void loadInstalledApps() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolved = pm.queryIntentActivities(intent, 0);
        Collections.sort(resolved, new ResolveInfo.DisplayNameComparator(pm));

        allApps.clear();
        for (ResolveInfo ri : resolved) {
            String pkg = ri.activityInfo.packageName;
            if (pkg.equals(getPackageName())) continue;
            allApps.add(new AppInfo(
                    ri.loadLabel(pm).toString(),
                    pkg,
                    ri.activityInfo.name,
                    ri.loadIcon(pm)
            ));
        }
        adapter.setApps(allApps);
    }

    private void filterApps(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setApps(allApps);
            return;
        }
        String lower = query.toLowerCase().trim();
        List<AppInfo> filtered = new ArrayList<>();
        for (AppInfo a : allApps) {
            if (a.label.toLowerCase().contains(lower)) filtered.add(a);
        }
        adapter.setApps(filtered);
    }

    private void launchApp(AppInfo app) {
        speak("Opening " + app.label);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(app.packageName, app.activityName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (Exception e) {
            loadInstalledApps();
        }
    }

    // ── Bottom dock ──────────────────────────────────────────────
    private void setupDock() {
        ImageView imgVoice = findViewById(R.id.dock_voice);
        if (imgVoice != null) imgVoice.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity6.class)));

        ImageView imgPhone = findViewById(R.id.dock_phone);
        if (imgPhone != null) imgPhone.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_DIAL)));

        ImageView imgCamera = findViewById(R.id.dock_camera);
        if (imgCamera != null) imgCamera.setOnClickListener(v ->
                startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));

        ImageView imgGallery = findViewById(R.id.dock_gallery);
        if (imgGallery != null) imgGallery.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setType("image/*");
            startActivity(i);
        });

        ImageView imgContacts = findViewById(R.id.dock_contacts);
        if (imgContacts != null) imgContacts.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        android.provider.ContactsContract.Contacts.CONTENT_URI)));

        ImageView imgSOS = findViewById(R.id.dock_sos);
        if (imgSOS != null) imgSOS.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_DIAL,
                        android.net.Uri.parse("tel:112"))));
    }
}