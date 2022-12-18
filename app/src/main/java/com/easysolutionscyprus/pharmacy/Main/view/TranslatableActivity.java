package com.easysolutionscyprus.pharmacy.Main.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.easysolutionscyprus.pharmacy.Preferences.model.LocalePreference;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Collections;
import java.util.List;

public abstract class TranslatableActivity extends AppCompatActivity {
    protected LocalePreference localePreference;
    protected AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translateActivity();
        setContentView(withLayout());
        configureViews();
        executeOnCreateActions();
        configureToolbar();
        configureAds();
    }

    protected abstract int withLayout();

    protected abstract void executeOnCreateActions();

    protected abstract void configureViews();

    protected abstract void configureToolbar();

    protected final void translateActivity() {
        localePreference = new LocalePreference(this);
        localePreference.apply();
    }

    @SuppressLint("MissingPermission")
    private void configureAds() {
        MobileAds.initialize(this, initializationStatus -> {});
        List<String> testDeviceIds = Collections.singletonList("78CCC020CE6CDC9E1BF6F407A7848165");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

}
