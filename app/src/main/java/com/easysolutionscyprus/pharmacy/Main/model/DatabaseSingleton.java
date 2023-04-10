package com.easysolutionscyprus.pharmacy.Main.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public final class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private final DatabaseReference databaseReference;
    private final HashMap<String,DatabaseReference> pharmacyListMap = new HashMap<>();
    private final DatabaseReference lastUpdated;
    private boolean connected;

    private DatabaseSingleton() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        checkIfConnected();
        lastUpdated = databaseReference.child("last_updated");
        setPharmacyListMap();
    }

    public static DatabaseSingleton getInstance() {
        if (instance==null) {
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    private void setPharmacyListMap() {
        pharmacyListMap.put("en",databaseReference.child("pharmacy_list_en"));
        pharmacyListMap.put("el",databaseReference.child("pharmacy_list_el"));
    }

    private void checkIfConnected() {
        Log.d("CONNECTED", "checkIfConnected called");
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setConnected(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setConnected(DataSnapshot dataSnapshot) {
        connected = Boolean.TRUE.equals(dataSnapshot.getValue(Boolean.class));
        Log.d("CONNECTED", String.valueOf(connected));
    }

    public DatabaseReference getPharmacyList(String locale) {
        return pharmacyListMap.get(locale);
    }

    public DatabaseReference getLastUpdated() {
        return lastUpdated;
    }

    public boolean isConnected() {return connected;}
}
