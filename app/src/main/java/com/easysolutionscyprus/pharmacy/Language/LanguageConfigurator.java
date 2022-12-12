package com.easysolutionscyprus.pharmacy.Language;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageConfigurator {
    public static void setLanguage(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        context.getResources().updateConfiguration(configuration,
                context.getResources().getDisplayMetrics());
    }
}
