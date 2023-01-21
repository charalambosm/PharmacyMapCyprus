package com.easysolutionscyprus.pharmacy.Preferences.view;

import static android.view.Window.FEATURE_NO_TITLE;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.easysolutionscyprus.pharmacy.R;

public abstract class AbstractDialog extends Dialog implements DialogPrototype, View.OnClickListener {
    protected Button saveButton;
    protected ImageButton cancelButton;

    public AbstractDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(withContentView());
        configureButtons();
        configureOptionWidgets();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == saveButton.getId()) {
            onSave();
            dismiss();
        } else if (v.getId() == cancelButton.getId()) {
            dismiss();
        }
    }

    private void configureButtons() {
        saveButton = findViewById(withSaveButton());
        cancelButton = findViewById(withCancelButton());
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }
}
