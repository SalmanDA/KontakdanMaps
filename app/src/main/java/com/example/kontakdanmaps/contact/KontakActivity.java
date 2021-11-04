package com.example.kontakdanmaps.contact;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kontakdanmaps.R;
import com.example.kontakdanmaps.contact.adapter.KontakAdapter;
import com.example.kontakdanmaps.contact.data.KontakModels;

import java.util.ArrayList;

public class KontakActivity extends AppCompatActivity {

    private ArrayList<KontakModels> listKontak;
    private KontakAdapter kAdapter;
    private ListView lvKontak;
    private EditText etCariNama;
    private Button btnTambahKontak,btnEditKontak, btnHapusKontak, btnCariKontak, btnTeleponKontak, btnChangeFragment;
    private KontakModels pointerKontak = null;
    private SQLiteDatabase DB;
    private SQLiteOpenHelper Opendb;

    private boolean changeFragmentCheck = false;
    private boolean isDBStop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontak);
        getSupportActionBar().setTitle("Aplikasi Kontak");

        setupItemView();
        setupView();
        declareDB();
        loadKontak();

    }

    private void setupItemView(){
        //Edit Text
        etCariNama = findViewById(R.id.etCariNama);

        //Button
        btnTambahKontak = findViewById(R.id.btnTambahKontak);
        btnEditKontak = findViewById(R.id.btnEditKontak);
        btnHapusKontak = findViewById(R.id.btnHapusKontak);
        btnCariKontak = findViewById(R.id.btnCariKontak);
        btnTeleponKontak = findViewById(R.id.btnTeleponKontak);

        //ListView
        lvKontak = findViewById(R.id.lvKontak);

        btnTambahKontak.setOnClickListener(tambahKontak);
        btnEditKontak.setOnClickListener(editKontak);
        btnHapusKontak.setOnClickListener(hapusKontak);
        btnCariKontak.setOnClickListener(cariKontak);
        btnTeleponKontak.setOnClickListener(teleponKontak);

    }

    private void setupView(){

        listKontak = new ArrayList<>();
        kAdapter = new KontakAdapter(this, 0, listKontak);
        lvKontak.setAdapter(kAdapter);
        lvKontak.setOnItemClickListener((adapterView, view, i, l) -> {
            KontakModels kontak = (KontakModels) adapterView.getItemAtPosition(i);
            Toast.makeText(this, "Anda memilih " + kontak.getNama(), Toast.LENGTH_LONG).show();
            pointerKontak = kontak;
        });
    }

    private void declareDB(){
        if(!isDBStop){
            return;
        }
        Opendb = new SQLiteOpenHelper(this, "db.sql", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {}

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
        };
        DB = Opendb.getWritableDatabase();
        DB.execSQL("create table if not exists contact(nama TEXT, noHp TEXT)");
        isDBStop = false;
    }

    @Override
    protected void onStop(){
        DB.close();
        Opendb.close();
        super.onStop();
        isDBStop=true;
    }

    private final View.OnClickListener tambahKontak = v -> {
        if(isDBStop) declareDB();
        AlertDialog.Builder aD = new AlertDialog.Builder(this);
        aD.setTitle("Tambah Kontak");
        v = LayoutInflater.from(this).inflate(R.layout.dialog_tambah, null);
        final EditText etNamaInput = v.findViewById(R.id.etNama),
                etNoHpInput = v.findViewById(R.id.etNoHp);
        etNamaInput.setEnabled(true);
        aD.setView(v);
        aD.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {

            if(ambilKontakBerdasarkanNama(etNamaInput.getText().toString()) == true){
                Toast.makeText(this, "Nama kontak sudah di gunakan", Toast.LENGTH_LONG).show();
            }else{
                simpanKontak(etNamaInput.getText().toString(),etNoHpInput.getText().toString());
                Toast.makeText(this, "Kontak Ditambahkan", Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        });
        aD.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());
        aD.show();
//        Toast.makeText(this, "button tambah kontak", Toast.LENGTH_LONG).show();
    };

    private final View.OnClickListener editKontak = v -> {
        if(isDBStop) declareDB();
        if(pointerKontak == null){
            Toast.makeText(this, "Pilih kontak yang ingin di edit", Toast.LENGTH_LONG).show();
        }
        else{
            AlertDialog.Builder aD = new AlertDialog.Builder(this);
            aD.setTitle("Edit Kontak");
            v = LayoutInflater.from(this).inflate(R.layout.dialog_tambah, null);

            final EditText etNamaInput = v.findViewById(R.id.etNama),
                    etNoHpInput = v.findViewById(R.id.etNoHp);

            etNamaInput.setText(pointerKontak.getNama());
            etNoHpInput.setText(pointerKontak.getNoHp());
            etNamaInput.setEnabled(false);

            aD.setView(v);
            aD.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {

                updateKontak(etNamaInput.getText().toString(),etNoHpInput.getText().toString());

                dialogInterface.dismiss();
            });
            aD.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());
            aD.show();
//        Toast.makeText(this, "button hapus kontak", Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(this, "button edit kontak", Toast.LENGTH_LONG).show();
    };

    private final View.OnClickListener hapusKontak = v -> {
        if(isDBStop) declareDB();

        if(pointerKontak == null){
            Toast.makeText(this, "Pilih kontak yang ingin di hapus", Toast.LENGTH_LONG).show();
        }
        else{
            AlertDialog.Builder aD = new AlertDialog.Builder(this);
            aD.setTitle("Hapus Kontak");
            v = LayoutInflater.from(this).inflate(R.layout.dialog_konfirmasi, null);

            final TextView tvPrompt = (TextView)v.findViewById(R.id.tvPrompt);

            tvPrompt.setText("Apakah anda yakin menghapus data " + pointerKontak.getNama() + " ?" );

            aD.setView(v);
            aD.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {

                hapusKontak(pointerKontak.getNama());

                dialogInterface.dismiss();
            });
            aD.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());
            aD.show();
//        Toast.makeText(this, "button hapus kontak", Toast.LENGTH_LONG).show();
        }
    };

    private final View.OnClickListener cariKontak = v -> {
        if(isDBStop) declareDB();

        if(etCariNama.getText().toString().trim().length() == 0 ){
            loadKontak();
        }else{
            ambilKontakNama(etCariNama.getText().toString());
            etCariNama.setText("");
        }
    };

    private final View.OnClickListener teleponKontak = v -> {

        Uri number = Uri.parse("tel:"+pointerKontak.getNoHp());
        Intent callNumber = new Intent(Intent.ACTION_DIAL, number);

        startActivity(callNumber);
    };

    private void loadKontak() {
        kAdapter.clear();
        Cursor cur = DB.rawQuery("SELECT * FROM contact", null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                kAdapter.add(new KontakModels(cur.getString(0), cur.getString(1)));
            }
            while (cur.moveToNext());
        }
        cur.close();
    }

    public void simpanKontak(String nama, String noHp){
        ContentValues dataku = new ContentValues();

        dataku.put("nama", nama);
        dataku.put("noHp", noHp);
        DB.insert("contact", null, dataku);

        kAdapter.add(new KontakModels(nama, noHp));
    }

    public boolean ambilKontakBerdasarkanNama(String nama){
        @SuppressLint("Recycle") Cursor cur = DB.rawQuery("select * from contact where nama='" + nama + "'", null);
        if(cur.getCount() > 0){
            return true;
        } else{
            return false;
        }
    }

    public void ambilKontakNama(String nama){
        Cursor cur = DB.rawQuery("select * from contact where nama like '%" + nama + "%'", null);
        int i=0;
        if(cur.getCount() > 0){
            cur.moveToFirst();
            kAdapter.clear();
            while(i<cur.getCount()){

                @SuppressLint("Range") KontakModels newKontak = new KontakModels(cur.getString(cur.getColumnIndex("nama")),
                        cur.getString(cur.getColumnIndex("noHp")));

                kAdapter.add(newKontak);
                cur.moveToNext();
                i++;
            }
        }
        else{
            Toast.makeText(this, "nama yang anda masukan tidak ada", Toast.LENGTH_LONG).show();
        }
    }

    private void hapusKontak(String nama){
        DB.delete("contact", "nama='" + nama + "'", null);
        loadKontak();
    }

    private void updateKontak(String nama, String noHp){
        ContentValues dataku = new ContentValues();

        dataku.put("noHp", noHp);
        dataku.put("nama", nama);
        DB.update("contact", dataku, "nama='" + nama + "'", null);
        loadKontak();
    }

}
