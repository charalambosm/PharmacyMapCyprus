package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import androidx.appcompat.widget.SearchView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public abstract class MyQueryTextListener implements SearchView.OnQueryTextListener{
    protected List<Pharmacy> searchResults = new ArrayList<>();
    List<Pharmacy> pharmacyList;

    public MyQueryTextListener(List<Pharmacy> pharmacyList) {
        this.pharmacyList = pharmacyList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        performSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        performSearch(newText);
        return false;
    }

    private void performSearch(String query) {
        // Preprocess query
        String normalizedQuery = normalizeText(query);
        String[] keywords = normalizedQuery.split("\\s");

        // Clear the current search results
        searchResults.clear();

        // Iterate over pharmacy list to find any that match
        for (Pharmacy pharmacy : pharmacyList) {
            boolean match = true;
            for (String keyword : keywords) {
                if (!pharmacy.getFirstNameNormalized().toLowerCase().contains(keyword.toLowerCase()) &&
                !pharmacy.getLastNameNormalized().toLowerCase().contains(keyword.toLowerCase()) &&
                !pharmacy.getAddressNormalized().toLowerCase().contains(keyword.toLowerCase())) {
                    match = false;
                    break;
                }
            }

            if (match) {
                searchResults.add(pharmacy);
            }
        }

        // Update the UI with the new search results
        updateSearchResultsUI();
    }

    protected abstract void updateSearchResultsUI();

    private String normalizeText(String newText) {
        String normalizedText = Normalizer.normalize(newText, Normalizer.Form.NFD);
        return normalizedText.replaceAll("\\p{M}", "");
    }
}

