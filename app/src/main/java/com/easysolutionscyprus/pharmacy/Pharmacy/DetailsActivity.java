package com.easysolutionscyprus.pharmacy.Pharmacy;


import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.easysolutionscyprus.pharmacy.Favorites.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyCancellationToken;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.Pharmacy;
import com.easysolutionscyprus.pharmacy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment supportMapFragment;
    GoogleMap mMap;
    Pharmacy pharmacy;
    LatLng pharmacyLatLng;
    TextView addressTextView, phoneTextView, nightTextView, distanceTextView;
    Toolbar toolbar;
    Favorites favorites;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProvider;
    final MyCancellationToken myCancellationToken = new MyCancellationToken();
    boolean isFavorite;
    int cardPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        // Get data from incoming intent
        getIntentData();

        // Set up toolbar
        configureToolbar();

        // Set up views
        configureViews();

        // Set up map
        configureMapFragment();

        // Configure ads
        configureAds();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        pharmacy = intent.getParcelableExtra("pharmacy");
        isFavorite = intent.getBooleanExtra("favorites",false);
        cardPosition = intent.getIntExtra("position", 1);
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.pharmacyActivityToolbar);
        toolbar.setTitle(String.format("%s %s", pharmacy.getFirstName(), pharmacy.getLastName()));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configureViews() {
        addressTextView = findViewById(R.id.pharmacyActivityAddressTextView);
        phoneTextView = findViewById(R.id.pharmacyActivityPhoneTextView);
        nightTextView = findViewById(R.id.pharmacyActivityNightTextView);
        distanceTextView = findViewById(R.id.pharmacyActivityDistanceTextView);
        addressTextView.setText(pharmacy.getAddress());
        if (pharmacy.getDistance() == 0) {
            distanceTextView.setVisibility(View.GONE);
        } else {
            distanceTextView.setText(String.format(Locale.getDefault(), "%3.1f km", pharmacy.getDistance()));
        }
        if (pharmacy.getHomePhone() == 0) {
            phoneTextView.setText(PhoneNumberUtils.formatNumber("+357"+pharmacy.getPhone(), "CY"));
        } else {
            phoneTextView.setText(String.format(Locale.getDefault(), "%s\n%s",
                    PhoneNumberUtils.formatNumber("+357"+pharmacy.getPhone(), "CY"),
                    PhoneNumberUtils.formatNumber("+357"+pharmacy.getHomePhone(), "CY")));
        }
        setNightTextViewVisibility();
    }

    private void configureMapFragment() {
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("mapFragment");
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        initializeGoogleMap(googleMap);
        setupPharmacyMarker();
        configureMapCamera();
    }

    private void configureMapCamera() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pharmacyLatLng, 15));
    }

    private void setupPharmacyMarker() {
        pharmacyLatLng = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(pharmacyLatLng).
                title(String.format("%s %s", pharmacy.getFirstName(), pharmacy.getLastName()));
        Marker marker = mMap.addMarker(markerOptions);
        if (marker != null) {
            marker.showInfoWindow();
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeGoogleMap(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
    }

    public void directionButtonCallback(View view) {
        Uri gmmIntentUri
                = Uri.parse(String.format(Locale.ENGLISH
                ,"geo:%f,%f?q=%f,%f(%s %s)"
                ,pharmacy.getLatitude(),pharmacy.getLongitude()
                ,pharmacy.getLatitude(),pharmacy.getLongitude()
                ,pharmacy.getFirstName(),pharmacy.getLastName()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void setNightTextViewVisibility() {
        if (pharmacy.isNight()) {
            nightTextView.setVisibility(View.VISIBLE);
        } else {
            nightTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // PERMISSION GRANTED
                    configureMapFragment();
                    // Get current location
                    getLocation();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProvider.getCurrentLocation(PRIORITY_HIGH_ACCURACY, myCancellationToken)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        // Show distance from user
                        calculateAndShowDistance();                    }
                });
    }

    void calculateAndShowDistance() {
        float[] result = new float[3];
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                pharmacy.getLatitude(), pharmacy.getLongitude(), result);
        double distance = result[0] / 1000;
        distanceTextView.setVisibility(View.VISIBLE);
        distanceTextView.setText(String.format(Locale.getDefault(), "%3.1f km", distance));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pharmacy_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favoritesAdd = menu.findItem(R.id.action_favorites_add);
        MenuItem favoritesDelete = menu.findItem(R.id.action_favorites_delete);
        if (isFavorite) {
            favoritesAdd.setVisible(false);
            favoritesDelete.setVisible(true);
        } else {
            favoritesAdd.setVisible(true);
            favoritesDelete.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorites_add) {
            addToFavorites();
            return true;
        } else if (item.getItemId() == R.id.action_favorites_delete) {
            removeFromFavorites();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void removeFromFavorites() {
        favorites = new Favorites(this);
        favorites.deleteFavorite(pharmacy.getId());
        isFavorite = false;
        invalidateOptionsMenu();
    }

    private void addToFavorites() {
        favorites = new Favorites(this);
        favorites.addFavorite(pharmacy.getId());
        isFavorite = true;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("cardPosition", cardPosition);
        setResult(RESULT_OK, intent);
        finish();
    }

    @SuppressLint("MissingPermission")
    private void configureAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = findViewById(R.id.pharmacyDetailsAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}