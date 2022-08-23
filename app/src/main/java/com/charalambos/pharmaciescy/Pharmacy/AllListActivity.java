package com.charalambos.pharmaciescy.Pharmacy;

import com.charalambos.pharmaciescy.Favorites.Favorites;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyFilter;
import com.charalambos.pharmaciescy.R;
import com.charalambos.pharmaciescy.Settings.Settings;

public class AllListActivity extends AbstractListActivity {
    @Override
    protected String withTitle() {
        return getString(R.string.all_pharmacies_title);
    }

    @Override
    protected int withLogo() {
        return R.drawable.ic_pharmacy;
    }

    @Override
    protected MyFilter buildFilter(Settings settings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(settings.getSelectedDistrictsPreference())
                .build();
    }
}
