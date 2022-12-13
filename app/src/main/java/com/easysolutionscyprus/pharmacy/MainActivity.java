package com.easysolutionscyprus.pharmacy;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.easysolutionscyprus.pharmacy.Database.DatabaseSingleton;
import com.easysolutionscyprus.pharmacy.Pharmacy.AllListActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.FavoritesListActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.NightOnlyListActivity;
import com.easysolutionscyprus.pharmacy.Settings.dialog.LanguageDialog;
import com.easysolutionscyprus.pharmacy.Support.ContactUsActivity;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends TranslatableActivity {
    public static DatabaseSingleton databaseReference;
    TextView lastUpdatedTextView;
    Toolbar toolbar;

    @Override
    protected int withLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void configureViews() {
        lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView);
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