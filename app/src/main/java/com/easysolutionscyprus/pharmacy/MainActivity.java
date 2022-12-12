package com.easysolutionscyprus.pharmacy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.easysolutionscyprus.pharmacy.Database.DatabaseSingleton;
import com.easysolutionscyprus.pharmacy.Language.LanguageConfigurator;
import com.easysolutionscyprus.pharmacy.Pharmacy.AllListActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.FavoritesListActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.NightOnlyListActivity;
import com.easysolutionscyprus.pharmacy.Settings.LocalePreference;
import com.easysolutionscyprus.pharmacy.Settings.dialog.LanguageDialog;
import com.easysolutionscyprus.pharmacy.Support.ContactUsActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends AppCompatActivity {
    public static DatabaseSingleton databaseReference;
    TextView lastUpdatedTextView;
    LocalePreference localeSettings;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureSettings();
        setContentView(R.layout.activity_main);
        configureToolbar();
        getDatabaseReference();
        configureAds();
    }

    private void configureSettings() {
        localeSettings = new LocalePreference(this);
        LanguageConfigurator.setLanguage(getBaseContext(), localeSettings.getPreference());
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_language) {
            showLanguageDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("MissingPermission")
    private void configureAds() {
        MobileAds.initialize(this, initializationStatus -> {});
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void getDatabaseReference() {
        databaseReference = DatabaseSingleton.getInstance();
        databaseReference.getLastUpdated().get().addOnSuccessListener(this::configureLastUpdatedTextView);
    }

    private void configureLastUpdatedTextView(DataSnapshot dataSnapshot) {
        lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);
        lastUpdatedTextView.setText(String.format(getString(R.string.last_updated), dataSnapshot.getValue()));
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

    private void showLanguageDialog() {
        LanguageDialog languageDialog = new LanguageDialog(this);
        languageDialog.setOnDismissListener(dialog -> recreate());
        languageDialog.show();
    }

}