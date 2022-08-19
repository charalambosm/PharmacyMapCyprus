package com.charalambos.pharmaciescy.Pharmacy;

import androidx.appcompat.app.ActionBar;

import com.charalambos.pharmaciescy.Bookmarks.Bookmarks;
import com.charalambos.pharmaciescy.Settings.Settings;

public class BookmarksListActivity extends PharmacyListActivity {
    @Override
    protected String withTitle() {
        return "Αγαπημένα φαρμακεία";
    }

    @Override
    protected PharmacyFilter buildFilter(Settings settings, Bookmarks bookmarks) {
        return new PharmacyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(settings.getSelectedDistrictsPreference())
                .withBookmarksOnly(bookmarks)
                .build();
    }
}
