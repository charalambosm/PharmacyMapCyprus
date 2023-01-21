package com.easysolutionscyprus.pharmacy.Preferences.view;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Preferences.model.LocalePreference;

public class LanguageDialog extends AbstractDialog implements View.OnClickListener, DialogPrototype {
    LocalePreference localeSettings;
    RadioGroup languageRadioGroup;

    public LanguageDialog(@NonNull Context context) {
        super(context);
        localeSettings = new LocalePreference(context);
        localeSettings.apply();
    }

    @Override
    public int withContentView() {
        return R.layout.dialog_language;
    }

    @Override
    public int withSaveButton() {
        return R.id.languageDialogSaveButton;
    }

    @Override
    public int withCancelButton() {
        return R.id.languageDialogCancelButton;
    }

    @Override
    public void configureOptionWidgets() {
        languageRadioGroup = findViewById(R.id.languageRadioGroup);
        if(localeSettings.getPreference().equals("en")) {
            languageRadioGroup.check(R.id.englishRadioButton);
        } else {
            languageRadioGroup.check(R.id.greekRadioButton);
        }
    }

    @Override
    public void onSave() {
        int checkedLocale = languageRadioGroup.getCheckedRadioButtonId();
        if (checkedLocale == R.id.englishRadioButton) {
            localeSettings.setPreference("en");
        } else {
            localeSettings.setPreference("el");
        }
    }
}
