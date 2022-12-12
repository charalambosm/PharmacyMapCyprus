package com.easysolutionscyprus.pharmacy.Settings;

import android.content.Context;

import com.easysolutionscyprus.pharmacy.R;

public class LocalePreference extends PreferenceString {

    public LocalePreference(Context context) {
        super(context, "locale_preference");
    }

    @Override
    public String getDefaultPreference(Context context) {
        return "en";
    }
}
