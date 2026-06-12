package com.example.myprofileapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView imgProfile;
    private EditText etNama, etTempat, etTanggal;
    private RadioGroup rgGender;
    private Spinner spJurusan;
    private SwitchCompat swStatus;
    private Uri selectedImageUri;

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
        setContentView(R.layout.activity_main);
        
        // Handle window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi View
        imgProfile = findViewById(R.id.imgProfile);
        Button btnPilih = findViewById(R.id.btnPilih);
        Button btnSimpan = findViewById(R.id.btnSimpan);
        etNama = findViewById(R.id.etNama);
        etTempat = findViewById(R.id.etTempat);
        etTanggal = findViewById(R.id.etTanggal);
        rgGender = findViewById(R.id.rgGender);
        spJurusan = findViewById(R.id.spJurusan);
        swStatus = findViewById(R.id.swStatus);

        // Setup Spinner Jurusan
        String[] daftarJurusan = {"Sistem Informasi", "Teknik Informatika", "Manajemen Informatika", "Sistem Komputer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daftarJurusan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJurusan.setAdapter(adapter);

        // DatePicker untuk Tanggal Lahir
        etTanggal.setOnClickListener(v -> showDatePicker());
        etTanggal.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });

        // Pilih Gambar
        btnPilih.setOnClickListener(v -> getContent.launch("image/*"));

        // Simpan & Lihat Profile
        btnSimpan.setOnClickListener(v -> simpanProfile());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> 
                        etTanggal.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1)),
                year, month, day);
        datePickerDialog.show();
    }

    private void simpanProfile() {
        String nama = etNama.getText().toString().trim();
        String tempat = etTempat.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        
        String gender = "";
        if (selectedGenderId != -1) {
            RadioButton rb = findViewById(selectedGenderId);
            gender = rb.getText().toString();
        }
        
        String jurusan = spJurusan.getSelectedItem() != null ? spJurusan.getSelectedItem().toString() : "";
        boolean status = swStatus.isChecked();

        if (nama.isEmpty() || tempat.isEmpty() || tanggal.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pindah ke ProfileResultActivity dengan membawa data
        Intent intent = new Intent(MainActivity.this, ProfileResultActivity.class);
        intent.putExtra("nama", nama);
        intent.putExtra("tempat", tempat);
        intent.putExtra("tanggal", tanggal);
        intent.putExtra("gender", gender);
        intent.putExtra("jurusan", jurusan);
        intent.putExtra("status", status);
        if (selectedImageUri != null) {
            intent.putExtra("imageUri", selectedImageUri.toString());
        }
        startActivity(intent);

        Toast.makeText(this, "Profile Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
    }
}