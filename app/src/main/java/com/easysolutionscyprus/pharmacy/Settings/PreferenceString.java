package com.easysolutionscyprus.pharmacy.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class PreferenceString {
    private final SharedPreferences settingsSharedPreferences;
    protected final String defaultPreference;
    protected final String key;
    SharedPreferences.Editor editor;

    protected PreferenceString(Context context, String preferenceKey) {
        settingsSharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        key = preferenceKey;
        defaultPreference = getDefaultPreference(context);
    }

    public String getPreference() {
        return settingsSharedPreferences.getString(key, defaultPreference);
    }

    public void setPreference(String preference) {
        editor = settingsSharedPreferences.edit();
        editor.putString(key, preference);
        editor.apply();
    }

    protected abstract String getDefaultPreference(Context context);
}
