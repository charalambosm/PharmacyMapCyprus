package com.charalambos.pharmaciescy.Pharmacy.internal;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public abstract class MyQueryTextListener implements SearchView.OnQueryTextListener{
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }

    protected List<Pharmacy> containsAny(String newText, List<Pharmacy> fullPharmacyList) {
        List<Pharmacy> matchedPharmacyList = new ArrayList<>();
        for (Pharmacy pharmacy : fullPharmacyList) {
            if (pharmacy.getFirstName().toLowerCase().contains(newText.toLowerCase()) ||
                    pharmacy.getLastName().toLowerCase().contains(newText.toLowerCase())) {
                matchedPharmacyList.add(pharmacy);
            }
        }
        return matchedPharmacyList;
    }

    public abstract void search(String newText);
}

