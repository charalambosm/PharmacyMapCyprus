package com.easysolutionscyprus.pharmacy.Pharmacy.view;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.easysolutionscyprus.pharmacy.Main.model.DatabaseSingleton;
import com.easysolutionscyprus.pharmacy.Main.view.TranslatableActivity;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyAdapter;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyCancellationToken;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyFilter;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyQueryTextListener;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyValueEventListener;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.Pharmacy;
import com.easysolutionscyprus.pharmacy.Preferences.model.DistrictPreference;
import com.easysolutionscyprus.pharmacy.Preferences.model.Favorites;
import com.easysolutionscyprus.pharmacy.Preferences.view.PharmacyFilterDialog;
import com.easysolutionscyprus.pharmacy.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public abstract class AbstractListActivity extends TranslatableActivity {
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

    DistrictPreference districtPreference;
    Favorites favorites;
    MyFilter myFilter;
    MyValueEventListener myValueEventListener;
    MyAdapter myAdapter;
    DatabaseReference pharmacyListDatabaseReference;

    ActivityResultLauncher<Intent> detailsActivityResultsLauncher;

    @Override
    protected int withLayout() {
        return R.layout.activity_pharmacy_list;
    }

    @Override
    protected void executeOnCreateActions() {
        // Define pharmacy list database reference
        configurePharmacyListDatabaseReference();

        // Get district preferences
        configureDistrictPreferences();

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

    private void configurePharmacyListDatabaseReference() {
        pharmacyListDatabaseReference = DatabaseSingleton.getInstance().getPharmacyList(localePreference.getPreference());
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

        // Configure ad view
        adView = findViewById(R.id.pharmacyListAdView);
    }

    @Override
    protected void configureToolbar() {
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

    private void configureDistrictPreferences() {
        districtPreference = new DistrictPreference(this);
    }

    private void configureFavorites() {
        favorites = new Favorites(this);
    }

    private void configureFilter() {
        myFilter = buildFilter(districtPreference, favorites);
    }

    protected abstract MyFilter buildFilter(DistrictPreference districtSettings, Favorites favorites);

    private void configureAdapter() {
        myAdapter = new MyAdapter() {
            @Override
            public void cardViewShowMoreCallback(Pharmacy pharmacy, int position) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("pharmacy", pharmacy);
                startActivity(intent);
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
                            position = intent.getIntExtra("cardPosition", 0x00);
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
        locationAccessNotGrantedCardView.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
        fusedLocationProvider.getCurrentLocation(PRIORITY_HIGH_ACCURACY, myCancellationToken)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        // Add value event listener to database reference
                        pharmacyListDatabaseReference.addValueEventListener(myValueEventListener);
                    }
                });
    }

    private void resetSearchView() {
        searchView.setQuery("",false);
        searchView.clearFocus();
    }

    private void swipeRefreshLayoutCallback() {
        getLocation();
        resetSearchView();
    }

    private void openFilterDialog() {
        PharmacyFilterDialog pharmacyFilterDialog = new PharmacyFilterDialog(this);
        pharmacyFilterDialog.setOnDismissListener(dialogInterface -> {
            configureFilter();
            swipeRefreshLayout.setRefreshing(true);
            getLocation();
        });
        pharmacyFilterDialog.show();
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
            } else if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED ){
                // PERMISSION DENIED
                // Add value event listener to database reference
                pharmacyListDatabaseReference.addValueEventListener(myValueEventListener);
                locationAccessNotGrantedCardView.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
            }
        }
    }
}