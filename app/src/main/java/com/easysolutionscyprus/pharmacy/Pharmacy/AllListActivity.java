package com.easysolutionscyprus.pharmacy.Pharmacy;

import com.easysolutionscyprus.pharmacy.Favorites.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyFilter;
import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Settings.Settings;

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
    protected MyFilter buildFilter(Settings settings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(settings.getSelectedDistrictsPreference())
                .build();
    }

    @Override
    protected void changeOrRemoveItem(int position) {
        myAdapter.changeItemAtPosition(position);
    }
}
