package com.example.kontakdanmaps.contact.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class KontakRepository {

    private SQLiteDatabase dbku;

    public void simpanKontak(KontakModels data){
        ContentValues dataku = new ContentValues();

        dataku.put("nama", data.nama);
        dataku.put("noHp", data.noHp);
        dbku.insert("contact", null, dataku);

    }

    public void ambilKontakBerdasarkanNama(String nama){
        @SuppressLint("Recycle") Cursor cur = dbku.rawQuery("select * from contact where nama='" + nama + "'", null);
        if(cur.getCount() > 0){
            cur.moveToFirst();
        }
    }

    private void hapus(String nama){
        dbku.delete("contact", "nama='" + nama + "'", null);
    }

    private void update(String nama, String noHp){
        ContentValues dataku = new ContentValues();

        dataku.put("noHp", noHp);
        dataku.put("nama", nama);
        dbku.update("contact", dataku, "nama='" + nama + "'", null);
    }
}
