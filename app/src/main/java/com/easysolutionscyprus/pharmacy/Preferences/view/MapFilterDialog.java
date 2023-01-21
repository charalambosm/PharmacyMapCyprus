package com.easysolutionscyprus.pharmacy.Preferences.view;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.easysolutionscyprus.pharmacy.R;

public class MapFilterDialog extends AbstractDialog implements View.OnClickListener, DialogPrototype {
    RadioGroup nightOnlyRadioGroup;
    boolean showNightOnly;

    public MapFilterDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int withContentView() {
        return R.layout.dialog_filter_map;
    }

    @Override
    public int withSaveButton() {
        return R.id.mapFilterDialogSaveButton;
    }

    @Override
    public int withCancelButton() {
        return R.id.mapFilterDialogCancelButton;
    }

    @Override
    public void configureOptionWidgets() {
        nightOnlyRadioGroup = findViewById(R.id.mapFilterDialogRadioGroup);
        if (showNightOnly) {
            nightOnlyRadioGroup.check(R.id.mapFilterDialogNightPharmaciesRadioButton);
        } else {
            nightOnlyRadioGroup.check(R.id.mapFilterDialogAllPharmaciesRadioButton);
        }
    }

    @Override
    public void onSave() {
        showNightOnly = nightOnlyRadioGroup.getCheckedRadioButtonId() == R.id.mapFilterDialogNightPharmaciesRadioButton;
    }

    public boolean isShowNightOnly() {
        return showNightOnly;
    }
}
