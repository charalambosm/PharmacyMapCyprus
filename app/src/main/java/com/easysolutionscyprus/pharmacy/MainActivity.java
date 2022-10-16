package com.easysolutionscyprus.pharmacy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.easysolutionscyprus.pharmacy.Pharmacy.AllListActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.FavoritesListActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.NightOnlyListActivity;
import com.easysolutionscyprus.pharmacy.Support.ContactUsActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextView lastUpdatedTextView;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLastUpdatedReference();
        configureAds();
    }

    @SuppressLint("MissingPermission")
    private void configureAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void getLastUpdatedReference() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("last_updated").get().addOnSuccessListener(this::configureLastUpdatedTextView);
    }

    private void configureLastUpdatedTextView(DataSnapshot dataSnapshot) {
        lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);
        lastUpdatedTextView.setText(String.format("Ανανεώθηκε: %s", dataSnapshot.getValue()));
    }

    public void allPharmaciesCallback(View view) {
        Intent intent = new Intent(getApplicationContext(), AllListActivity.class);
        startActivity(intent);
    }

    public void nightOnlyPharmaciesCallback(View view) {
        Intent intent = new Intent(getApplicationContext(), NightOnlyListActivity.class);
        startActivity(intent);
    }

    public void bookmarksPharmaciesCallback(View view) {
        Intent intent = new Intent(getApplicationContext(), FavoritesListActivity.class);
        startActivity(intent);
    }

    public void contactUsCallback(View view) {
        Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
        startActivity(intent);
    }
}