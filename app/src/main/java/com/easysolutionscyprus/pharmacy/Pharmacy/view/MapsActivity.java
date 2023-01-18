package com.easysolutionscyprus.pharmacy.Pharmacy.view;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.easysolutionscyprus.pharmacy.Main.model.DatabaseSingleton;
import com.easysolutionscyprus.pharmacy.Main.view.TranslatableActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.InfoLayoutAdapter;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyCancellationToken;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyClusterRenderer;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyValueEventListener;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.Pharmacy;
import com.easysolutionscyprus.pharmacy.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.clustering.ClusterManager;

public class MapsActivity extends TranslatableActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        ClusterManager.OnClusterItemClickListener<Pharmacy>{
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProvider;
    GoogleMap googleMap;
    DatabaseReference pharmacyListDatabaseReference;
    MyValueEventListener myValueEventListener;
    final MyCancellationToken myCancellationToken = new MyCancellationToken();
    ClusterManager<Pharmacy> clusterManager;
    MyClusterRenderer myClusterRenderer;
    RelativeLayout infoLayout;
    CardView infoCardView;
    ImageButton expandInfoButton;
    InfoLayoutAdapter infoLayoutAdapter;
    Pharmacy currentPharmacy;
    SupportMapFragment supportMapFragment;

    @Override
    protected int withLayout() {
        return R.layout.activity_maps;
    }

    @Override
    protected void executeOnCreateActions() {
        // Define pharmacy list database reference
        configurePharmacyListDatabaseReference();

        // Get intent data
        getIntentData();

        // Define the map
        configureMapFragment();

        // Define pharmacy list value event listener
        configureValueEventListener();
    }

    @Override
    protected void configureViews() {
        adView = findViewById(R.id.adView);
        infoLayout = findViewById(R.id.mapsActivityInfoLayout);
        infoLayoutAdapter = new InfoLayoutAdapter(this);
        infoCardView = findViewById(R.id.mapsActivityInfoCardView);
        expandInfoButton = findViewById(R.id.mapsActivityExpandInfoButton);
    }

    @Override
    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.mapsActivityToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void configurePharmacyListDatabaseReference() {
        pharmacyListDatabaseReference = DatabaseSingleton.getInstance().getPharmacyList(localePreference.getPreference());
    }

    private void getIntentData() {
        Intent intent = getIntent();
        currentPharmacy = intent.getParcelableExtra("pharmacy");
    }

    private void configureValueEventListener() {
        myValueEventListener = new MyValueEventListener() {
            @Override
            public void update() {
                for (Pharmacy pharmacy: pharmacyList) {
                    if (currentLocation!=null) {
                        pharmacy.setDistance(calculatePharmacyDistance(currentLocation,pharmacy)/1000);
                    }
                    clusterManager.addItem(pharmacy);
                }
            }
        };
    }

    private void configureMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pharmaciesMap);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Initialize Cluster Manager
        initializeClusterManager();

        // Initialize Google Map
        initializeGoogleMap();

        // Add value event listener to database reference
        pharmacyListDatabaseReference.addValueEventListener(myValueEventListener);
    }

    @SuppressLint("MissingPermission")
    private void initializeGoogleMap() {
        initializeMapCamera();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getCurrentLocation();
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @SuppressLint("PotentialBehaviorOverride")
    private void initializeClusterManager() {
        clusterManager = new ClusterManager<>(this, googleMap);
        myClusterRenderer = new MyClusterRenderer(this,googleMap,clusterManager);
        clusterManager.setRenderer(myClusterRenderer);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setOnInfoWindowClickListener(clusterManager);
        clusterManager.setOnClusterItemClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // PERMISSION GRANTED - get current location
                    getCurrentLocation();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProvider.getCurrentLocation(PRIORITY_HIGH_ACCURACY, myCancellationToken)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        initializeMapCamera();
                 }
                });

    }

    private void initializeMapCamera() {
        LatLng initialLatLng;
        float zoom;
        if (currentPharmacy != null) {
            initialLatLng = new LatLng(currentPharmacy.getLatitude(), currentPharmacy.getLongitude());
            zoom = 17;
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, zoom));
            myClusterRenderer.setSelectedPharmacy(currentPharmacy);
            selectMarker(currentPharmacy);
        } else if (currentLocation != null) {
            initialLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            zoom = 15;
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, zoom));
        } else {
            initialLatLng = new LatLng(35.095192, 33.203430);
            zoom = 8;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, zoom));
        }
    }

    private void selectMarker(Pharmacy item) {
        myClusterRenderer.setSelectedMarker(item);
        infoCardView.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.VISIBLE);
        infoLayoutAdapter.setPharmacy(item);
    }

    @Override
    public boolean onClusterItemClick(Pharmacy item) {
        selectMarker(item);
        LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        return true;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        myClusterRenderer.setSelectedMarker(null);
        infoCardView.setVisibility(View.GONE);
    }

    public void expandInfoButtonCallback(View view) {
        if (infoLayout.getVisibility() == View.VISIBLE) {
            infoLayout.setVisibility(View.GONE);
            expandInfoButton.setImageResource(R.drawable.ic_chevron_up);
        } else {
            infoLayout.setVisibility(View.VISIBLE);
            expandInfoButton.setImageResource(R.drawable.ic_chevron_down);
        }
    }
}