package com.charalambos.pharmaciescy.Pharmacy.internal;

import static android.view.Window.FEATURE_NO_TITLE;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.charalambos.pharmaciescy.R;
import com.charalambos.pharmaciescy.Settings.Settings;

import java.util.Set;

public class MyFilterDialog extends Dialog implements View.OnClickListener {
    Button okButton;
    CheckBox nicosiaCheckbox, limassolCheckbox, larnacaCheckBox, paphosCheckbox, famagustaCheckbox;
    Settings settings;

    public MyFilterDialog(@NonNull Context context) {
        super(context);
        settings = new Settings(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_filter);
        configureOkButton();
        configureDistrictCheckboxes();
    }

    private void configureOkButton() {
        okButton = findViewById(R.id.filterDialogOKButton);
        okButton.setOnClickListener(this);
    }

    public void configureDistrictCheckboxes() {
        Set<String> selectedDistricts = settings.getSelectedDistrictsPreference();
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

    public void saveFilters() {
        settings.setSelectedDistrictsPreferences(
                new Settings.SelectedDistrictsBuilder()
                        .setNicosia(nicosiaCheckbox.isChecked())
                        .setLimassol(limassolCheckbox.isChecked())
                        .setLarnaca(larnacaCheckBox.isChecked())
                        .setPaphos(paphosCheckbox.isChecked())
                        .setFamagusta(famagustaCheckbox.isChecked())
                        .build());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.filterDialogOKButton) {
            saveFilters();
            dismiss();
        }
    }
}
