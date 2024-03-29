package com.easysolutionscyprus.pharmacy.Main.view;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.easysolutionscyprus.pharmacy.ContactUs.view.ContactUsActivity;
import com.easysolutionscyprus.pharmacy.Main.model.DatabaseSingleton;
import com.easysolutionscyprus.pharmacy.Pharmacy.view.AllListActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.view.FavoritesListActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.view.MapsActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.view.NightOnlyListActivity;
import com.easysolutionscyprus.pharmacy.Preferences.view.LanguageDialog;
import com.easysolutionscyprus.pharmacy.R;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends TranslatableActivity {
    public static DatabaseSingleton databaseReference;
    TextView lastUpdatedTextView, lastUpdatedLabel;
    Button pharmacyMapButton, allPharmaciesButton, nightPharmaciesButton, favoritePharmaciesButton, contactUsButton, settingsButton;
    Toolbar toolbar;

    @Override
    protected int withLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void configureViews() {
        lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);
        lastUpdatedLabel = findViewById(R.id.lastUpdatedLabel);
        pharmacyMapButton = findViewById(R.id.mapButton);
        allPharmaciesButton = findViewById(R.id.allPharmaciesButton);
        nightPharmaciesButton = findViewById(R.id.nightPharmaciesButton);
        favoritePharmaciesButton = findViewById(R.id.favoritePharmaciesButton);
        contactUsButton = findViewById(R.id.contactUsButton);
        settingsButton = findViewById(R.id.settingsButton);
        adView = findViewById(R.id.adView);
    }

    @Override
    protected void executeOnCreateActions() {
        configureToolbar();
        getDatabaseReference();
    }

    @Override
    protected void configureToolbar() {
        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
    }

    private void getDatabaseReference() {
        databaseReference = DatabaseSingleton.getInstance();
        databaseReference.getLastUpdated().get().addOnSuccessListener(this::configureLastUpdatedTextView);
    }

    private void configureLastUpdatedTextView(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            lastUpdatedTextView.setText(dataSnapshot.getValue().toString());
        }
    }

    public void pharmacyMapCallback(View view) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
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

    public void settingsCallback(View view) {
        showLanguageDialog();
    }

    private void showLanguageDialog() {
        LanguageDialog languageDialog = new LanguageDialog(this);
        languageDialog.setOnDismissListener(dialog -> updateViewsText());
        languageDialog.show();
    }

    private void updateViewsText() {
        translateActivity();
        lastUpdatedLabel.setText(getString(R.string.last_updated));
        pharmacyMapButton.setText(R.string.map);
        allPharmaciesButton.setText(getString(R.string.all_pharmacies_title));
        nightPharmaciesButton.setText(getString(R.string.night_title_btn));
        favoritePharmaciesButton.setText(getString(R.string.favorites_title));
        contactUsButton.setText(getString(R.string.contact_us_title));
        settingsButton.setText(R.string.settings);
        toolbar.setTitle(getString(R.string.main_menu));
    }

}