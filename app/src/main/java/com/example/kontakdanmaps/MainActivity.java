package com.example.kontakdanmaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kontakdanmaps.contact.KontakActivity;
import com.example.kontakdanmaps.gps.GpsActivity;


public class MainActivity extends AppCompatActivity {

    private Button btnKontak,btnKeluar,btnGps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Aplikasi Tugas PPB");

        setupItemView();
        setupView();

    }

    private void setupItemView(){
        //Button
        btnKontak = findViewById(R.id.btnKontak);
        btnKeluar = findViewById(R.id.btnKeluar);
        btnGps = findViewById(R.id.btnGps);

        btnKontak.setOnClickListener(RedirectToKontakPage);
        btnGps.setOnClickListener(RedirectToGpsPage);
        btnKeluar.setOnClickListener(keluar);

    }

    private void setupView(){
    }

    private final View.OnClickListener RedirectToKontakPage = v -> {
        startActivity(new Intent(MainActivity.this, KontakActivity.class));
    };

    private final View.OnClickListener RedirectToGpsPage = v -> {
        startActivity(new Intent(MainActivity.this, GpsActivity.class));
    };

    private final View.OnClickListener keluar = v -> {
        moveTaskToBack(true);
    };

}