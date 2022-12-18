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
import com.easysolutionscyprus.pharmacy.Pharmacy.view.NightOnlyListActivity;
import com.easysolutionscyprus.pharmacy.Preferences.view.LanguageDialog;
import com.easysolutionscyprus.pharmacy.R;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends TranslatableActivity {
    public static DatabaseSingleton databaseReference;
    TextView lastUpdatedTextView, lastUpdatedLabel;
    Button allPharmaciesButton, nightPharmaciesButton, favoritePharmaciesButton, contactUsButton;
    Toolbar toolbar;

    @Override
    protected int withLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void configureViews() {
        lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);
        lastUpdatedLabel = findViewById(R.id.lastUpdatedLabel);
        allPharmaciesButton = findViewById(R.id.allPharmaciesButton);
        nightPharmaciesButton = findViewById(R.id.nightPharmaciesButton);
        favoritePharmaciesButton = findViewById(R.id.favoritePharmaciesButton);
        contactUsButton = findViewById(R.id.contactUsButton);
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

    private void getDatabaseReference() {
        databaseReference = DatabaseSingleton.getInstance();
        databaseReference.getLastUpdated().get().addOnSuccessListener(this::configureLastUpdatedTextView);
    }

    private void configureLastUpdatedTextView(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            lastUpdatedTextView.setText(dataSnapshot.getValue().toString());
        }
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
        languageDialog.setOnDismissListener(dialog -> updateViewsText());
        languageDialog.show();
    }

    private void updateViewsText() {
        translateActivity();
        lastUpdatedLabel.setText(getString(R.string.last_updated));
        allPharmaciesButton.setText(getString(R.string.all_pharmacies_title));
        nightPharmaciesButton.setText(getString(R.string.night_title));
        favoritePharmaciesButton.setText(getString(R.string.favorites_title));
        contactUsButton.setText(getString(R.string.contact_us_title));
        toolbar.setTitle(getString(R.string.main_menu));
    }

}