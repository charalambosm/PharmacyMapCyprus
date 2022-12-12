package com.easysolutionscyprus.pharmacy.Settings.dialog;

import static android.view.Window.FEATURE_NO_TITLE;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Settings.LocalePreference;

public class LanguageDialog extends Dialog implements View.OnClickListener, DialogPrototype {
    LocalePreference localeSettings;
    Button okButton;
    RadioGroup languageRadioGroup;

    public LanguageDialog(@NonNull Context context) {
        super(context);
        localeSettings = new LocalePreference(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_language);
        configureSaveButton();
        configureOptionWidgets();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.languageDialogOKButton) {
            save();
            dismiss();
        }
    }

    @Override
    public void configureSaveButton() {
        okButton = findViewById(R.id.languageDialogOKButton);
        okButton.setOnClickListener(this);
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
    public void save() {
        int checkedLocale = languageRadioGroup.getCheckedRadioButtonId();
        if (checkedLocale == R.id.englishRadioButton) {
            localeSettings.setPreference("en");
        } else {
            localeSettings.setPreference("el");
        }
    }
}
