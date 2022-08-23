package com.charalambos.pharmaciescy.Support;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class TextValidator implements TextWatcher {
    private final TextView textView;
    public final String fullNamePattern = "([A-Za-z\\u0370-\\u03ff ]*)";
    public final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = textView.getText().toString();
        validate(textView, text);
    }
}
