package com.easysolutionscyprus.pharmacy.Preferences.view;

public interface DialogPrototype {
    int withContentView();
    int withSaveButton();
    int withCancelButton();
    void configureOptionWidgets();
    void onSave();
}
