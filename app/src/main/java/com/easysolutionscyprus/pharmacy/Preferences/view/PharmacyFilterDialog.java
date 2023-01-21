package com.easysolutionscyprus.pharmacy.Preferences.view;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.easysolutionscyprus.pharmacy.Preferences.model.DistrictPreference;
import com.easysolutionscyprus.pharmacy.R;

import java.util.Set;

public class PharmacyFilterDialog extends AbstractDialog implements View.OnClickListener, DialogPrototype {
    CheckBox nicosiaCheckbox, limassolCheckbox, larnacaCheckBox, paphosCheckbox, famagustaCheckbox;
    DistrictPreference districtSettings;

    public PharmacyFilterDialog(@NonNull Context context) {
        super(context);
        districtSettings = new DistrictPreference(context);
    }

    @Override
    public int withContentView() {
        return R.layout.dialog_filter;
    }

    @Override
    public int withSaveButton() {
        return R.id.filterDialogSaveButton;
    }

    @Override
    public int withCancelButton() {
        return R.id.filterDialogCancelButton;
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
    }
}
