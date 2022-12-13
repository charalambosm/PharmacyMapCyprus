package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

public class MyCancellationToken extends CancellationToken {
    @NonNull
    @Override
    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
        return this;
    }

    @Override
    public boolean isCancellationRequested() {
        return false;
    }
}
