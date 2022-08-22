package com.charalambos.pharmaciescy.Pharmacy;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.charalambos.pharmaciescy.Bookmarks.Bookmarks;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyAdapter;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyCancellationToken;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyFilter;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyQueryTextListener;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyValueEventListener;
import com.charalambos.pharmaciescy.Pharmacy.internal.Pharmacy;
import com.charalambos.pharmaciescy.R;
import com.charalambos.pharmaciescy.Settings.Settings;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public abstract class AbstractListActivity extends AppCompatActivity {
    // Views
    RecyclerView recyclerView;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    CardView locationAccessNotGrantedCardView;
    MaterialDivider divider;

    // Location related
    FusedLocationProviderClient fusedLocationProvider;
    Location currentLocation;
    MyCancellationToken myCancellationToken = new MyCancellationToken();

    // Database related
    Settings settings;
    Bookmarks bookmarks;
    MyFilter myFilter;
    MyValueEventListener myValueEventListener;
    MyAdapter myAdapter;
    DatabaseReference pharmacyDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);

        pharmacyDatabaseReference = FirebaseDatabase.getInstance().getReference().child("pharmacy_list");

        // Define views
        configureViews();

        // Configure toolbar
        configureToolbar();

        // Configure settings
        configureSettings();

        // Configure bookmarks
        configureBookmarks();

        // Configure pharmacy filters
        configureFilter();

        // Define pharmacy adapter
        configureAdapter();

        // Define pharmacy value event listener
        configureValueEventListener();

        // Configure fusedLocationProvider
        configureLocationProvider();

        // Get location
        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetSearchView();
        getLocation();
    }

    protected void configureViews() {
        // Configure swipe refresh layout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::swipeRefreshLayoutCallback);
        swipeRefreshLayout.setRefreshing(true);

        // Configure location access card view and divider
        locationAccessNotGrantedCardView = findViewById(R.id.locationAccessNotGrantedCardView);
        divider = findViewById(R.id.divider);

        // Configure recycler view
        recyclerView = findViewById(R.id.pharmacyRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider, 0);

        // Configure search view
        searchView = findViewById(R.id.pharmacySearchView);
        searchView.setOnQueryTextListener(new MyQueryTextListener() {
            @Override
            public void search(String newText) {
                if (myAdapter.getFullPharmacyList() != null) {
                    List<Pharmacy> matchedPharmacyList = containsAny(newText, myAdapter.getFullPharmacyList());
                    myAdapter.searchPharmacyList(matchedPharmacyList);
                    recyclerView.setAdapter(myAdapter);
                }
            }
        });
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.listActivityToolbar);
        toolbar.setTitle(withTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract String withTitle();

    private void configureSettings() {
        settings = new Settings(this);
    }

    private void configureBookmarks() {
        bookmarks = new Bookmarks(this);
    }

    private void configureFilter() {
        myFilter = buildFilter(settings, bookmarks);
    }

    protected abstract MyFilter buildFilter(Settings settings, Bookmarks bookmarks);

    private void configureAdapter() {
        myAdapter = new MyAdapter() {
            @Override
            public void cardViewShowMoreCallback(Pharmacy pharmacy) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("pharmacy", pharmacy);
                intent.putExtra("bookmark", bookmarks.isBookmark(pharmacy.getId()));
                startActivity(intent);
            }
        };
        myAdapter.setBookmarks(bookmarks);
    }

    private void configureValueEventListener() {
        myValueEventListener = new MyValueEventListener() {
            @Override
            public void update() {
                if (pharmacyList.size() > 0) {
                    // Sort Pharmacy List based on distance from current location
                    if (currentLocation != null) {
                        sortPharmacyListByDistance(currentLocation);
                    }
                    // Filter Pharmacy List
                    List<Pharmacy> filteredPharmacyList = myFilter.apply(pharmacyList);

                    // Set pharmacy list to adapter and set adapter to recycler view
                    myAdapter.setFullPharmacyList(filteredPharmacyList);
                    recyclerView.setAdapter(myAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Log.e("PHARMACY_LIST", "No pharmacies found");
                }
            }
        };
    }

    private void configureLocationProvider() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationProvider.getCurrentLocation(PRIORITY_HIGH_ACCURACY, myCancellationToken)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        // Add value event listener to database reference
                        pharmacyDatabaseReference.addValueEventListener(myValueEventListener);
                    }
                });
    }

    private void resetSearchView() {
        searchView.setQuery("",false);
        searchView.clearFocus();
    }

    private void swipeRefreshLayoutCallback() {
        getLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_toolbar, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // PERMISSION GRANTED
                    getLocation();
                } else {
                    // PERMISSION DENIED
                    Log.e("LOCATION ACCESS", "PERMISSION DENIED");
                }
            }
        }
    }
}