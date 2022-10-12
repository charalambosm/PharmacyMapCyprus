package com.easysolutionscyprus.pharmacy.Settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.easysolutionscyprus.pharmacy.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Settings {
    protected final String settingsSharedPreferencesFile = "settings";
    final SharedPreferences settingsSharedPreferences;
    SharedPreferences.Editor editor;
    final String selectedDistrictsKey = "district_preference";
    final Set<String> defaultDistrictsValue;

    public Settings(Context context) {
        settingsSharedPreferences = context.getSharedPreferences(settingsSharedPreferencesFile, Context.MODE_PRIVATE);
        defaultDistrictsValue = new HashSet<> (Arrays.asList(context.getResources().getStringArray(R.array.district_default)));
    }

    public Set<String> getSelectedDistrictsPreference() {
        return settingsSharedPreferences.getStringSet(selectedDistrictsKey, defaultDistrictsValue);
    }

    public void setSelectedDistrictsPreferences(Set<String> selectedDistricts) {
        editor = settingsSharedPreferences.edit();
        editor.putStringSet(selectedDistrictsKey, selectedDistricts);
        editor.apply();
    }

    public static class SelectedDistrictsBuilder {
        boolean nicosia, limassol, larnaca, paphos, famagusta;
        Set<String> selectedDistricts = new HashSet<>();

        public SelectedDistrictsBuilder() {
        }

        public SelectedDistrictsBuilder setNicosia(boolean nicosia) {
            this.nicosia = nicosia;
            return this;
        }

        public SelectedDistrictsBuilder setLimassol(boolean limassol) {
            this.limassol = limassol;
            return this;
        }

        public SelectedDistrictsBuilder setLarnaca(boolean larnaca) {
            this.larnaca = larnaca;
            return this;
        }

        public SelectedDistrictsBuilder setPaphos(boolean paphos) {
            this.paphos = paphos;
            return this;
        }

        public SelectedDistrictsBuilder setFamagusta(boolean famagusta) {
            this.famagusta = famagusta;
            return this;
        }

        public Set<String> build() {
            if (nicosia) {
                selectedDistricts.add("Λευκωσία");
            }
            if (limassol) {
                selectedDistricts.add("Λεμεσός");
            }
            if (larnaca) {
                selectedDistricts.add("Λάρνακα");
            }
            if (paphos) {
                selectedDistricts.add("Πάφος");
            }
            if (famagusta) {
                selectedDistricts.add("Αμμόχωστος");
            }
            return selectedDistricts;
        }
    }
}
