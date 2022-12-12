package com.easysolutionscyprus.pharmacy.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public final class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private final DatabaseReference databaseReference;
    private final HashMap<String,DatabaseReference> pharmacyListMap = new HashMap<>();
    private final DatabaseReference lastUpdated;

    private DatabaseSingleton() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
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

    public DatabaseReference getPharmacyList(String locale) {
        return pharmacyListMap.get(locale);
    }

    public DatabaseReference getLastUpdated() {
        return lastUpdated;
    }
}
