package com.easysolutionscyprus.pharmacy.Pharmacy;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.easysolutionscyprus.pharmacy.Favorites.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyAdapter;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyCancellationToken;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyFilter;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyFilterDialog;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyQueryTextListener;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyValueEventListener;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.Pharmacy;
import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Settings.Settings;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

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
    final MyCancellationToken myCancellationToken = new MyCancellationToken();

    Settings settings;
    Favorites favorites;
    MyFilter myFilter;
    MyValueEventListener myValueEventListener;
    public static MyAdapter myAdapter;
    DatabaseReference databaseReference;

    ActivityResultLauncher<Intent> detailsActivityResultsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Define views
        configureViews();

        // Configure toolbar
        configureToolbar();

        // Configure settings
        configureSettings();

        // Configure favorites
        configureFavorites();

        // Configure pharmacy filters
        configureFilter();

        // Define pharmacy adapter
        configureAdapter();

        // Define pharmacy list value event listener
        configureValueEventListener();

        // Configure fusedLocationProvider
        configureLocationProvider();

        // Configure details activity launcher
        configureDetailsActivityLauncher();

        // Get location
        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetSearchView();
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
        toolbar.setLogo(withLogo());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract String withTitle();

    protected abstract int withLogo();

    private void configureSettings() {
        settings = new Settings(this);
    }

    private void configureFavorites() {
        favorites = new Favorites(this);
    }

    private void configureFilter() {
        myFilter = buildFilter(settings, favorites);
    }

    protected abstract MyFilter buildFilter(Settings settings, Favorites favorites);

    private void configureAdapter() {
        myAdapter = new MyAdapter() {
            @Override
            public void cardViewShowMoreCallback(Pharmacy pharmacy, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("pharmacy", pharmacy);
                intent.putExtra("favorites", favorites.isFavorite(pharmacy.getId()));
                intent.putExtra("position", position);
                detailsActivityResultsLauncher.launch(intent);
            }

            @Override
            public void cardViewBookmarksCallback(Pharmacy pharmacy, int position) {
                if (favorites.isFavorite(pharmacy.getId())) {
                    favorites.deleteFavorite(pharmacy.getId());
                } else {
                    favorites.addFavorite(pharmacy.getId());
                }
                changeOrRemoveItem(position);
            }

            @Override
            public void cardViewDirectionCallback(Pharmacy pharmacy) {
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
        };
        myAdapter.setFavorites(favorites);
    }

    protected abstract void changeOrRemoveItem(int position);

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

    private void configureDetailsActivityLauncher() {
        detailsActivityResultsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        int position;
                        if (intent != null) {
                            position = intent.getIntExtra("position", 0x00);
                            changeOrRemoveItem(position);
                        }
                    }
                }
        );
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
                        databaseReference.child("pharmacy_list").addValueEventListener(myValueEventListener);
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

    private void openFilterDialog() {
        MyFilterDialog myFilterDialog = new MyFilterDialog(this);
        myFilterDialog.setOnDismissListener(dialogInterface -> {
            configureFilter();
            swipeRefreshLayout.setRefreshing(true);
            getLocation();
        });
        myFilterDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            openFilterDialog();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // PERMISSION GRANTED
                getLocation();
                locationAccessNotGrantedCardView.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            } else {
                // PERMISSION DENIED
                Log.e("LOCATION ACCESS", "PERMISSION DENIED");
                // Add value event listener to database reference
                databaseReference.child("pharmacy_list").addValueEventListener(myValueEventListener);
                locationAccessNotGrantedCardView.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
            }
        }
    }
}