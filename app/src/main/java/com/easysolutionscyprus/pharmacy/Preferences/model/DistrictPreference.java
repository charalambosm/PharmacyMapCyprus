package com.easysolutionscyprus.pharmacy.Preferences.model;

import android.content.Context;

import com.easysolutionscyprus.pharmacy.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DistrictPreference extends PreferenceStringSet {

    public DistrictPreference(Context context) {
        super(context, "district_preference");
    }

    @Override
    protected Set<String> getDefaultPreference(Context context) {
        return new HashSet<>(Arrays.asList(context.getResources().getStringArray(R.array.district_default)));
    }

    public static class DistrictSettingsBuilder {
        boolean nicosia, limassol, larnaca, paphos, famagusta;
        Set<String> selectedDistricts = new HashSet<>();

        public DistrictSettingsBuilder() {
        }

        public DistrictSettingsBuilder setNicosia(boolean nicosia) {
            this.nicosia = nicosia;
            return this;
        }

        public DistrictSettingsBuilder setLimassol(boolean limassol) {
            this.limassol = limassol;
            return this;
        }

        public DistrictSettingsBuilder setLarnaca(boolean larnaca) {
            this.larnaca = larnaca;
            return this;
        }

        public DistrictSettingsBuilder setPaphos(boolean paphos) {
            this.paphos = paphos;
            return this;
        }

        public DistrictSettingsBuilder setFamagusta(boolean famagusta) {
            this.famagusta = famagusta;
            return this;
        }

        public Set<String> build() {
            if (nicosia) {
                selectedDistricts.add("Λευκωσία");
                selectedDistricts.add("Nicosia");
            }
            if (limassol) {
                selectedDistricts.add("Λεμεσός");
                selectedDistricts.add("Limassol");
            }
            if (larnaca) {
                selectedDistricts.add("Λάρνακα");
                selectedDistricts.add("Larnaca");
            }
            if (paphos) {
                selectedDistricts.add("Πάφος");
                selectedDistricts.add("Paphos");
            }
            if (famagusta) {
                selectedDistricts.add("Αμμόχωστος");
                selectedDistricts.add("Famagusta");
            }
            return selectedDistricts;
        }
    }
}
