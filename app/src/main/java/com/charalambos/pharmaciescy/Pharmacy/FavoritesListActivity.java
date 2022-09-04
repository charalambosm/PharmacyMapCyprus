package com.charalambos.pharmaciescy.Pharmacy;

import com.charalambos.pharmaciescy.Favorites.Favorites;
import com.charalambos.pharmaciescy.Pharmacy.internal.MyFilter;
import com.charalambos.pharmaciescy.R;
import com.charalambos.pharmaciescy.Settings.Settings;

public class FavoritesListActivity extends AbstractListActivity {
    @Override
    protected String withTitle() {
        return getString(R.string.favorites_title);
    }

    @Override
    protected int withLogo() {
        return R.drawable.ic_favorite;
    }

    @Override
    protected MyFilter buildFilter(Settings settings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(settings.getSelectedDistrictsPreference())
                .withBookmarksOnly(favorites)
                .build();
    }

    @Override
    protected void changeOrRemoveItem(int position) {
        myAdapter.removeItemFromPosition(position);
    }
}
