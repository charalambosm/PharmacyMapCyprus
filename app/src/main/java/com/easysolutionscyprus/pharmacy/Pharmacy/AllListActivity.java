package com.easysolutionscyprus.pharmacy.Pharmacy;

import com.easysolutionscyprus.pharmacy.Favorites.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyFilter;
import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Settings.DistrictPreference;

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
    protected MyFilter buildFilter(DistrictPreference districtSettings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(districtSettings.getPreference())
                .build();
    }

    @Override
    protected void changeOrRemoveItem(int position) {
        myAdapter.changeItemAtPosition(position);
    }
}
