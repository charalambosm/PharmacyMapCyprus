package com.charalambos.pharmaciescy.Pharmacy.internal;

import com.charalambos.pharmaciescy.Bookmarks.Bookmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyFilter {
    private final List<Predicate<Pharmacy>> allFilters;

    public MyFilter(PharmacyFilterBuilder pharmacyFilterBuilder) {
        this.allFilters = pharmacyFilterBuilder.allFilters;
    }

    public List<Pharmacy> apply(List<Pharmacy> allPharmacies) {
        return allPharmacies.stream()
                .filter(allFilters.stream().reduce(x->true,Predicate::and))
                .collect(Collectors.toList());
    }

    public static class PharmacyFilterBuilder {
        private final List<Predicate<Pharmacy>> allFilters;

        public PharmacyFilterBuilder() {
            allFilters = new ArrayList<>();
        }

        public PharmacyFilterBuilder setNightOnlyFilter(boolean showNightOnly) {
            if (showNightOnly) {
                allFilters.add(Pharmacy::isNight);
            }
            return this;
        }

        public PharmacyFilterBuilder withBookmarksOnly(Bookmarks bookmarks) {
            allFilters.add(Pharmacy -> (bookmarks.isBookmark(Pharmacy.getId())));
            return this;
        }

        public PharmacyFilterBuilder setDistrictsFilter(Set<String> selectedDistricts) {
            List<Predicate<Pharmacy>> districtFilter = new ArrayList<>();
            for (String district: selectedDistricts) {
                districtFilter.add(Pharmacy -> (Pharmacy.getDistrict().equals(district)));
            }
            allFilters.add(districtFilter.stream().reduce(x->false, Predicate::or));
            return this;
        }

        public MyFilter build() {
            return new MyFilter(this);
        }
    }


}
