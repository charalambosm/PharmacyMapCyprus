package com.easysolutionscyprus.pharmacy.Preferences.model;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocalePreference extends PreferenceString {

    public LocalePreference(Context context) {
        super(context, "locale_preference");
    }

    @Override
    public String getDefaultPreference(Context context) {
        return "en";
    }

    public void apply() {
        Locale locale = new Locale(getPreference());
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        context.getResources().updateConfiguration(configuration,
                context.getResources().getDisplayMetrics());
    }
}
