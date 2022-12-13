package com.easysolutionscyprus.pharmacy.Preferences.model;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class PreferenceString {
    private final SharedPreferences settingsSharedPreferences;
    protected final String defaultPreference;
    protected final String key;
    protected final Context context;
    SharedPreferences.Editor editor;

    protected PreferenceString(Context context, String preferenceKey) {
        this.context = context;
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
