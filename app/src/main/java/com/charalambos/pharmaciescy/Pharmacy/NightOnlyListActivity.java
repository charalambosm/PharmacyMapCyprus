package com.charalambos.pharmaciescy.Pharmacy;

import com.charalambos.pharmaciescy.Bookmarks.Bookmarks;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyFilter;
import com.charalambos.pharmaciescy.Settings.Settings;

public class NightOnlyListActivity extends AbstractListActivity {
    @Override
    protected String withTitle() {
        return "Διημερεύον φαρμακεία";
    }

    @Override
    protected MyFilter buildFilter(Settings settings, Bookmarks bookmarks) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(settings.getSelectedDistrictsPreference())
                .setNightOnlyFilter(true)
                .build();
    }
}
