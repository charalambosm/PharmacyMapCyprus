package com.easysolutionscyprus.pharmacy.Preferences.view;

import static android.view.Window.FEATURE_NO_TITLE;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.easysolutionscyprus.pharmacy.R;
import com.easysolutionscyprus.pharmacy.Preferences.model.DistrictPreference;

import java.util.Set;

public class PharmacyFilterDialog extends Dialog implements View.OnClickListener, DialogPrototype {
    Button okButton;
    CheckBox nicosiaCheckbox, limassolCheckbox, larnacaCheckBox, paphosCheckbox, famagustaCheckbox;
    DistrictPreference districtSettings;

    public PharmacyFilterDialog(@NonNull Context context) {
        super(context);
        districtSettings = new DistrictPreference(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_filter);
        configureSaveButton();
        configureOptionWidgets();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.filterDialogOKButton) {
            onSave();
        } else {
            dismiss();
        }
    }

    @Override
    public void configureSaveButton() {
        okButton = findViewById(R.id.filterDialogOKButton);
        okButton.setOnClickListener(this);
    }

    @Override
    public void configureOptionWidgets() {
        Set<String> selectedDistricts = districtSettings.getPreference();
        nicosiaCheckbox = findViewById(R.id.nicosiaCheckbox);
        limassolCheckbox = findViewById(R.id.limassolCheckbox);
        larnacaCheckBox = findViewById(R.id.larnacaCheckbox);
        paphosCheckbox = findViewById(R.id.paphosCheckbox);
        famagustaCheckbox = findViewById(R.id.famagustaCheckbox);
        if (selectedDistricts.contains("Λευκωσία")) {
            nicosiaCheckbox.setChecked(true);
        }
        if (selectedDistricts.contains("Λεμεσός")) {
            limassolCheckbox.setChecked(true);
        }
        if (selectedDistricts.contains("Λάρνακα")) {
            larnacaCheckBox.setChecked(true);
        }
        if (selectedDistricts.contains("Πάφος")) {
            paphosCheckbox.setChecked(true);
        }
        if (selectedDistricts.contains("Αμμόχωστος")) {
            famagustaCheckbox.setChecked(true);
        }
    }

    @Override
    public void onSave() {
        districtSettings.setPreference(
                new DistrictPreference.DistrictSettingsBuilder()
                        .setNicosia(nicosiaCheckbox.isChecked())
                        .setLimassol(limassolCheckbox.isChecked())
                        .setLarnaca(larnacaCheckBox.isChecked())
                        .setPaphos(paphosCheckbox.isChecked())
                        .setFamagusta(famagustaCheckbox.isChecked())
                        .build());
        dismiss();
    }
}
