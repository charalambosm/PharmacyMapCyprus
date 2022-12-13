package com.easysolutionscyprus.pharmacy.Pharmacy.view;

import com.easysolutionscyprus.pharmacy.Preferences.model.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.model.MyFilter;
import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Preferences.model.DistrictPreference;

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
