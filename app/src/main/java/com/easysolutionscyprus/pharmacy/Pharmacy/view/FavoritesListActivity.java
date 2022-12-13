package com.easysolutionscyprus.pharmacy.Pharmacy.view;

import com.easysolutionscyprus.pharmacy.Preferences.model.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyFilter;
import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Preferences.model.DistrictPreference;

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
