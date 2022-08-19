package com.charalambos.pharmaciescy.Pharmacy.internal;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class MyValueEventListener implements com.google.firebase.database.ValueEventListener {
    protected final List<Pharmacy> pharmacyList;

    public MyValueEventListener() {
        super();
        this.pharmacyList = new ArrayList<>();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        this.pharmacyList.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Pharmacy pharmacy = dataSnapshot.getValue(Pharmacy.class);
            this.pharmacyList.add(pharmacy);
        }
        Log.e("PHARMACY LIST", "Finished retrieving pharmacy list");
        update();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }

    protected void sortPharmacyListByDistance(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Comparator<Pharmacy> comparator = (p1, p2) -> {
            float[] result1 = new float[3];
            Location.distanceBetween(latitude, longitude, p1.getLatitude(), p1.getLongitude(), result1);
            Float distance1 = result1[0];
            p1.setDistance(distance1/1000);

            float[] result2 = new float[3];
            Location.distanceBetween(latitude, longitude, p2.getLatitude(), p2.getLongitude(), result2);
            Float distance2 = result2[0];

            return distance1.compareTo(distance2);
        };
        pharmacyList.sort(comparator);
    }

    public abstract void update();
}
