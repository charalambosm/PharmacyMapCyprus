package com.easysolutionscyprus.pharmacy.Settings;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public abstract class PreferenceStringSet {
    private final SharedPreferences settingsSharedPreferences;
    protected final Set<String> defaultPreference;
    protected final String key;
    SharedPreferences.Editor editor;

    protected PreferenceStringSet(Context context, String preferenceKey) {
        settingsSharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        key = preferenceKey;
        defaultPreference = getDefaultPreference(context);
    }

    public Set<String> getPreference() {
        return settingsSharedPreferences.getStringSet(key, defaultPreference);
    }

    public void setPreference(Set<String> preference) {
        editor = settingsSharedPreferences.edit();
        editor.putStringSet(key, preference);
        editor.apply();
    }

    protected abstract Set<String> getDefaultPreference(Context context);
}
