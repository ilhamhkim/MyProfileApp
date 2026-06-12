package com.example.myprofileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvNama, tvUsername, tvTempat, tvTanggal, tvHobi, tvBio;
    private CircleImageView imgProfile;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dashboard");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvWelcome = findViewById(R.id.tvWelcome);
        tvNama = findViewById(R.id.tvNama);
        tvUsername = findViewById(R.id.tvUsername);
        tvTempat = findViewById(R.id.tvTempat);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvHobi = findViewById(R.id.tvHobi);
        tvBio = findViewById(R.id.tvBio);
        imgProfile = findViewById(R.id.imgProfile);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        
        loadData();
        
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        String nama = sharedPreferences.getString("nama", "ILHAM AMIRUL HAKIM");
        String username = sharedPreferences.getString("username", "ilham");
        String tempat = sharedPreferences.getString("tempat", "Medan");
        String tanggal = sharedPreferences.getString("tanggal", "8/6/1996");
        String hobi = sharedPreferences.getString("hobi", "Programming, Desain Web");
        String bio = sharedPreferences.getString("bio", "Full Stack Developer");
        String imageUri = sharedPreferences.getString("imageUri", "");

        tvWelcome.setText("Welcome, " + nama + "!");
        tvNama.setText(nama);
        tvUsername.setText(username);
        tvTempat.setText(tempat);
        tvTanggal.setText(tanggal);
        tvHobi.setText(hobi);
        tvBio.setText(bio);

        if (!imageUri.isEmpty()) {
            try {
                imgProfile.setImageURI(Uri.parse(imageUri));
            } catch (Exception e) {
                imgProfile.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_update_profile) {
            startActivity(new Intent(this, EditProfileActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}