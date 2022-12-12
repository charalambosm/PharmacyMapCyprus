package com.easysolutionscyprus.pharmacy.Pharmacy;

import com.easysolutionscyprus.pharmacy.Favorites.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyFilter;
import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Settings.DistrictPreference;

public class NightOnlyListActivity extends AbstractListActivity {
    @Override
    protected String withTitle() {
        return getString(R.string.night_title);
    }

    @Override
    protected int withLogo() {
        return R.drawable.ic_night;
    }

    @Override
    protected MyFilter buildFilter(DistrictPreference districtSettings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(districtSettings.getPreference())
                .setNightOnlyFilter(true)
                .build();
    }

    @Override
    protected void changeOrRemoveItem(int position) {
        myAdapter.changeItemAtPosition(position);
    }
}
