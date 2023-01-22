package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.Normalizer;
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
            if (pharmacy != null) {
                pharmacy.setAddressNormalized(normalizeText(pharmacy.getAddress()));
                pharmacy.setFirstNameNormalized(normalizeText(pharmacy.getFirstName()));
                pharmacy.setLastNameNormalized(normalizeText(pharmacy.getLastName()));
                Log.d("NORMALIZED_ADDRESS", pharmacy.getAddressNormalized());
            }
            this.pharmacyList.add(pharmacy);
        }
        update();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }

    protected void sortPharmacyListByDistance(Location location) {
        Comparator<Pharmacy> comparator = (p1, p2) -> {
            Float distance1 = calculatePharmacyDistance(location, p1);
            p1.setDistance(distance1/1000);

            Float distance2 = calculatePharmacyDistance(location, p2);
            return distance1.compareTo(distance2);
        };
        pharmacyList.sort(comparator);
    }

    protected Float calculatePharmacyDistance(Location currentLocation, Pharmacy pharmacy) {
        float[] result = new float[3];
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), pharmacy.getLatitude(), pharmacy.getLongitude(), result);
        return result[0];
    }

    private String normalizeText(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalizedText.replaceAll("\\p{M}", "");
    }

    public abstract void update();
}
