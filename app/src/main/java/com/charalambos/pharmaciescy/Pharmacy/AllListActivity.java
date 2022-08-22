package com.charalambos.pharmaciescy.Pharmacy;

import com.charalambos.pharmaciescy.Favorites.Favorites;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyFilter;
import com.charalambos.pharmaciescy.Settings.Settings;

public class AllListActivity extends AbstractListActivity {
    @Override
    protected String withTitle() {
        return "Όλα τα φαρμακεία";
    }

    @Override
    protected MyFilter buildFilter(Settings settings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(settings.getSelectedDistrictsPreference())
                .build();
    }
}
