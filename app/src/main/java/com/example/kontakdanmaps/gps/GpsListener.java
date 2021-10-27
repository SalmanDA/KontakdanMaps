package com.example.kontakdanmaps.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kontakdanmaps.R;

public class GpsListener implements LocationListener {
    private TextView tvLat, tvLong;
    private Context context;

    public GpsListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        tvLat = ((Activity) context).findViewById(R.id.tvLat);
        tvLong = ((Activity) context).findViewById(R.id.tvLong);

        tvLat.setText(String.valueOf(location.getLatitude()));
        tvLong.setText(String.valueOf(location.getLongitude()));

        Toast.makeText(context, "GPS Capture", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

}
