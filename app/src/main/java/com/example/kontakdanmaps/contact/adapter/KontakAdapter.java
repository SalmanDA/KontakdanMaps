package com.example.kontakdanmaps.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kontakdanmaps.R;
import com.example.kontakdanmaps.contact.data.KontakModels;

import java.util.List;

public class KontakAdapter extends ArrayAdapter<KontakModels> {
    private static class ViewHolder {
        TextView tvNama;
        TextView tvNoPhone;
    }

    public KontakAdapter(Context context, int resource, List<KontakModels> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View ConvertView, ViewGroup parent) {
        KontakModels kontak = getItem(position);
        ViewHolder viewKontak;
        if (ConvertView == null) {
            viewKontak = new ViewHolder();
            ConvertView = LayoutInflater.from(getContext()).inflate(R.layout.contact, parent, false);
            viewKontak.tvNama = (TextView) ConvertView.findViewById(R.id.tvNama);
            viewKontak.tvNoPhone = (TextView) ConvertView.findViewById(R.id.tvNoPhone);
            ConvertView.setTag(viewKontak);
        }
        else {
            viewKontak = (ViewHolder) ConvertView.getTag();
        }

        viewKontak.tvNama.setText(kontak.getNama());
        viewKontak.tvNoPhone.setText(kontak.getNoHp());

        return ConvertView;
    }
}
