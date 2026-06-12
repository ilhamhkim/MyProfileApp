package com.example.myprofileapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CircleImageView resImgProfile = findViewById(R.id.resImgProfile);
        TextView resNama = findViewById(R.id.resNama);
        TextView resLahir = findViewById(R.id.resLahir);
        TextView resGender = findViewById(R.id.resGender);
        TextView resJurusan = findViewById(R.id.resJurusan);
        TextView resStatus = findViewById(R.id.resStatus);
        Button btnKembali = findViewById(R.id.btnKembali);

        // Ambil data dari Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            resNama.setText("Nama: " + extras.getString("nama"));
            resLahir.setText("Lahir: " + extras.getString("tempat") + ", " + extras.getString("tanggal"));
            resGender.setText("Gender: " + extras.getString("gender"));
            resJurusan.setText("Jurusan: " + extras.getString("jurusan"));
            resStatus.setText("Status: " + (extras.getBoolean("status") ? "Aktif" : "Tidak Aktif"));

            String imageUriString = extras.getString("imageUri");
            if (imageUriString != null && !imageUriString.isEmpty()) {
                resImgProfile.setImageURI(Uri.parse(imageUriString));
            }
        }

        btnKembali.setOnClickListener(v -> finish());
    }
}