package com.example.myprofileapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNama, etTempat, etTanggal, etHobi, etBio;
    private CircleImageView imgProfile;
    private Button btnChangeImage, btnSave;
    private Uri selectedImageUri;
    private SharedPreferences sharedPreferences;

    private final ActivityResultLauncher<String> getContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imgProfile.setImageURI(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNama = findViewById(R.id.etNama);
        etTempat = findViewById(R.id.etTempat);
        etTanggal = findViewById(R.id.etTanggal);
        etHobi = findViewById(R.id.etHobi);
        etBio = findViewById(R.id.etBio);
        imgProfile = findViewById(R.id.imgProfile);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        btnSave = findViewById(R.id.btnSave);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        loadCurrentData();

        etTanggal.setOnClickListener(v -> showDatePicker());
        btnChangeImage.setOnClickListener(v -> getContent.launch("image/*"));
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadCurrentData() {
        etNama.setText(sharedPreferences.getString("nama", "ILHAM AMIRUL HAKIM"));
        etTempat.setText(sharedPreferences.getString("tempat", "Medan"));
        etTanggal.setText(sharedPreferences.getString("tanggal", "8/6/1996"));
        etHobi.setText(sharedPreferences.getString("hobi", "Programming, Desain Web"));
        etBio.setText(sharedPreferences.getString("bio", "Full Stack Developer"));
        
        String imageUriStr = sharedPreferences.getString("imageUri", "");
        if (!imageUriStr.isEmpty()) {
            try {
                selectedImageUri = Uri.parse(imageUriStr);
                imgProfile.setImageURI(selectedImageUri);
            } catch (Exception e) {
                selectedImageUri = null;
                imgProfile.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> 
                        etTanggal.setText(String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, monthOfYear + 1, year1)),
                year, month, day);
        datePickerDialog.show();
    }

    private void saveProfile() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nama", etNama.getText().toString());
        editor.putString("tempat", etTempat.getText().toString());
        editor.putString("tanggal", etTanggal.getText().toString());
        editor.putString("hobi", etHobi.getText().toString());
        editor.putString("bio", etBio.getText().toString());
        if (selectedImageUri != null) {
            editor.putString("imageUri", selectedImageUri.toString());
        }
        editor.apply();

        Toast.makeText(this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}