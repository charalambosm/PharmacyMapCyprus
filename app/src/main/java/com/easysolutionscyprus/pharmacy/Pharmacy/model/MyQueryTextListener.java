package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import androidx.appcompat.widget.SearchView;

import java.text.Normalizer;
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
        String normalizedText = normalizeText(newText);
        for (Pharmacy pharmacy : fullPharmacyList) {
            if (contains(normalizedText, pharmacy)) {
                matchedPharmacyList.add(pharmacy);
            }
        }
        return matchedPharmacyList;
    }

    private String normalizeText(String newText) {
        String normalizedText = Normalizer.normalize(newText, Normalizer.Form.NFD);
        return normalizedText.replaceAll("\\p{M}", "");
    }

    private boolean contains(String normalizedText, Pharmacy pharmacy) {
        return  pharmacy.getFirstNameNormalized().toLowerCase().contains(normalizedText.toLowerCase()) ||
                pharmacy.getLastNameNormalized().toLowerCase().contains(normalizedText.toLowerCase()) ||
                pharmacy.getAddressNormalized().toLowerCase().contains(normalizedText.toLowerCase());
    }

    public abstract void search(String newText);
}

