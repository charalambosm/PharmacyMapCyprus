package com.charalambos.pharmaciescy.Pharmacy;

import com.charalambos.pharmaciescy.Favorites.Favorites;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyFilter;
import com.charalambos.pharmaciescy.Settings.Settings;

public class NightOnlyListActivity extends AbstractListActivity {
    @Override
    protected String withTitle() {
        return "Διημερεύον φαρμακεία";
    }

    @Override
    protected MyFilter buildFilter(Settings settings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(settings.getSelectedDistrictsPreference())
                .setNightOnlyFilter(true)
                .build();
    }
}
