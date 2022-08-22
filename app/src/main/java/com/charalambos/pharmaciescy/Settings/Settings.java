package com.charalambos.pharmaciescy.Settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.charalambos.pharmaciescy.R;

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
}
