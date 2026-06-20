package com.tejyash.myadapto;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MainActivity8 is kept for compatibility but is no longer the home screen.
 * MainActivity2 now redirects to HomeActivity after setup.
 * This activity can be reached via back-navigation if needed.
 */
public class MainActivity8 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main8);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Voice
        ImageView img = findViewById(R.id.imgv);
        img.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity6.class)));

        // Phone
        ImageView imgPhone = findViewById(R.id.imgPhone);
        imgPhone.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_DIAL)));

        // Camera
        ImageView imgGallery = findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(v ->
                startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));

        // Gallery
        ImageView galar = findViewById(R.id.galar);
        galar.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setType("image/*");
            startActivity(i);
        });

        // Contacts
        ImageView imgContact = findViewById(R.id.imgContact);
        imgContact.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        android.provider.ContactsContract.Contacts.CONTENT_URI)));

        // SOS
        ImageView imgSOS = findViewById(R.id.imgSOS);
        imgSOS.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_DIAL,
                        android.net.Uri.parse("tel:112"))));

        // NOTE: startListening() removed — auto-triggering voice on launch was a bug
    }
}
