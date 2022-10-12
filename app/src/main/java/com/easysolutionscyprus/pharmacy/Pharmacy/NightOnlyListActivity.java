package com.easysolutionscyprus.pharmacy.Pharmacy;

import com.easysolutionscyprus.pharmacy.Favorites.Favorites;
import com.easysolutionscyprus.pharmacy.Pharmacy.internal.MyFilter;
import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Settings.Settings;

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
    protected MyFilter buildFilter(Settings settings, Favorites favorites) {
        return new MyFilter.PharmacyFilterBuilder()
                .setDistrictsFilter(settings.getSelectedDistrictsPreference())
                .setNightOnlyFilter(true)
                .build();
    }

    @Override
    protected void changeOrRemoveItem(int position) {
        myAdapter.changeItemAtPosition(position);
    }
}
