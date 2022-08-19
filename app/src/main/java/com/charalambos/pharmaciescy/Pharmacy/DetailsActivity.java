package com.charalambos.pharmaciescy.Pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.charalambos.pharmaciescy.Bookmarks.Bookmarks;
import com.charalambos.pharmaciescy.Pharmacy.internal.Pharmacy;
import com.charalambos.pharmaciescy.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    FragmentContainerView fragmentContainerView;
    SupportMapFragment supportMapFragment;
    GoogleMap mMap;
    Pharmacy pharmacy;
    LatLng pharmacyLatLng;
    TextView toolbarTitle, addressTextView, phoneTextView, nightTextView, distanceTextView;
    Toolbar toolbar;
    Bookmarks bookmarks;
    boolean isBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        // Get data from incoming intent
        AsyncTask.execute(this::getIntentData);

        // Set up toolbar
        AsyncTask.execute(this::configureToolbar);

        // Set up views
        AsyncTask.execute(this::configureViews);

        // Set up map
        configureMapFragment();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        pharmacy = intent.getParcelableExtra("pharmacy");
        isBookmark = intent.getBooleanExtra("bookmark",false);
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.pharmacyActivityToolbar);
        toolbarTitle = findViewById(R.id.pharmacyActivityToolbarTitle);
        toolbarTitle.setText(String.format("%s %s", pharmacy.getFirstName(), pharmacy.getLastName()));
        setSupportActionBar(toolbar);
    }

    private void configureViews() {
        addressTextView = findViewById(R.id.pharmacyActivityAddressTextView);
        phoneTextView = findViewById(R.id.pharmacyActivityPhoneTextView);
        nightTextView = findViewById(R.id.pharmacyActivityNightTextView);
        distanceTextView = findViewById(R.id.pharmacyActivityDistanceTextView);
        addressTextView.setText(pharmacy.getAddress());
        phoneTextView.setText(String.format(Locale.getDefault(), "%d", pharmacy.getPhone()));
        distanceTextView.setText(String.format(Locale.getDefault(), "%3.2f km", pharmacy.getDistance()));
        setNightTextViewVisibility();
    }

    private void configureMapFragment() {
        fragmentContainerView = findViewById(R.id.pharmacyActivityFragmentContainerView);
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

    public void phoneButtonCallback(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+357"+pharmacy.getPhone()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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
                } else {
                    // PERMISSION DENIED
                    Log.e("LOCATION ACCESS", "PERMISSION DENIED");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pharmacy_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem bookmarksAdd = menu.findItem(R.id.action_bookmarks_add);
        MenuItem bookmarksRemove = menu.findItem(R.id.action_bookmarks_remove);
        if (isBookmark) {
            Log.e("BOOKMARK","YES");
            bookmarksAdd.setVisible(false);
            bookmarksRemove.setVisible(true);
        } else {
            Log.e("BOOKMARK","NO");
            bookmarksAdd.setVisible(true);
            bookmarksRemove.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_bookmarks_add) {
            addToBookmarks();
            return true;
        } else if (item.getItemId() == R.id.action_bookmarks_remove) {
            removeFromBookmarks();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void removeFromBookmarks() {
        bookmarks = new Bookmarks(this);
        bookmarks.deleteBookmark(pharmacy.getId());
        isBookmark = false;
        invalidateOptionsMenu();
    }

    private void addToBookmarks() {
        bookmarks = new Bookmarks(this);
        bookmarks.addBookmark(pharmacy.getId());
        isBookmark = true;
        invalidateOptionsMenu();
    }
}