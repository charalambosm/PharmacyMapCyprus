package com.easysolutionscyprus.pharmacy.Pharmacy.view;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.easysolutionscyprus.pharmacy.Preferences.view.MapFilterDialog;
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
    MapFilterDialog mapFilterDialog;
    boolean showNightOnly = false;
    boolean stayOnLocation = false;

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

        // Define the map filter dialog
        configureMapFilterDialog();

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
        expandInfoButton = findViewById(R.id.mapsActivityCloseInfoButton);
    }

    private void configureMapFilterDialog() {
        mapFilterDialog = new MapFilterDialog(this);
        mapFilterDialog.setOnDismissListener(dialog -> {
            showNightOnly = mapFilterDialog.isShowNightOnly();
            currentPharmacy = null;
            myClusterRenderer.unselectPharmacy();
            stayOnLocation = true;
            getCurrentLocation();
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            mapFilterDialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
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
                Log.d("GMAP", "Updating pharmacy markers");
                clusterManager.clearItems();
                for (Pharmacy pharmacy: pharmacyList) {
                    if (currentLocation!=null) {
                        pharmacy.setDistance(calculatePharmacyDistance(currentLocation,pharmacy)/1000);
                    }
                    if (showNightOnly && pharmacy.isNight()) {
                        clusterManager.addItem(pharmacy);
                    } else if (!showNightOnly) {
                        clusterManager.addItem(pharmacy);
                    }
                }
                clusterManager.cluster();
                if (currentPharmacy != null) {
                    myClusterRenderer.selectPharmacy(currentPharmacy);
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

        // Initialize Cluster Renderer
        initializeClusterRenderer();

        // Initialize Google Map
        initializeGoogleMap();
    }

    @SuppressLint("MissingPermission")
    private void initializeGoogleMap() {
        initializeMapCamera();
        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getCurrentLocation();
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private void initializeClusterManager() {
        clusterManager = new ClusterManager<>(this, googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setOnInfoWindowClickListener(clusterManager);
        clusterManager.setOnClusterItemClickListener(this);
    }

    private void initializeClusterRenderer() {
        myClusterRenderer = new MyClusterRenderer(this, googleMap, clusterManager) {
            @Override
            public void selectPharmacy(Pharmacy pharmacy) {
                unselectPreviouslySelectedMarker();
                selectNewMarker(pharmacy);
                infoCardView.setVisibility(View.VISIBLE);
                infoLayoutAdapter.setPharmacy(pharmacy);
            }

            @Override
            public void unselectPharmacy() {
                unselectPreviouslySelectedMarker();
                infoCardView.setVisibility(View.GONE);
            }
        };
        clusterManager.setRenderer(myClusterRenderer);
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
            } else {
                // Add value event listener to database reference
                pharmacyListDatabaseReference.addValueEventListener(myValueEventListener);
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
                        if (!stayOnLocation) {
                            initializeMapCamera();
                        }
                 }
                    // Add value event listener to database reference
                    pharmacyListDatabaseReference.addValueEventListener(myValueEventListener);
                });
    }

    private void initializeMapCamera() {
        if (currentPharmacy != null) {
            moveCameraWithZoom(currentPharmacy.getLatitude(), currentPharmacy.getLongitude(), 15);
        } else if (currentLocation != null) {
            moveCameraWithZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 15);
        } else {
            moveCameraWithZoom(35.095192, 33.203430,9);
        }
    }

    private void moveCameraWithZoom(double latitude, double longitude, float zoom) {
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public boolean onClusterItemClick(Pharmacy item) {
        myClusterRenderer.selectPharmacy(item);
        moveCameraWithZoom(item.getLatitude(), item.getLongitude(),googleMap.getCameraPosition().zoom);
        return true;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        myClusterRenderer.unselectPharmacy();
    }

    public void closeInfoButtonCallback(View view) {
        myClusterRenderer.unselectPharmacy();
    }
}