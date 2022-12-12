package com.easysolutionscyprus.pharmacy.Pharmacy;

import com.easysolutionscyprus.pharmacy.Favorites.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyFilter;
import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Settings.DistrictPreference;

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
    protected MyFilter buildFilter(DistrictPreference districtSettings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(districtSettings.getPreference())
                .withBookmarksOnly(favorites)
                .build();
    }

    @Override
    protected void changeOrRemoveItem(int position) {
        myAdapter.removeItemFromPosition(position);
    }
}
