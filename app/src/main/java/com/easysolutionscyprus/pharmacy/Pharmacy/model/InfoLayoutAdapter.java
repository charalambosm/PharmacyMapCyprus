package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easysolutionscyprus.pharmacy.R;

import java.util.Locale;

public class InfoLayoutAdapter {
    Pharmacy pharmacy;
    private final Context context;
    protected final TextView textViewName;
    protected final TextView textViewAddress;
    protected final TextView textViewNight;
    protected final TextView textViewPhonePharmacy;
    protected final TextView textViewPhoneHome;
    protected final TextView textViewDistance;
    protected final ImageButton buttonDirection;

    public InfoLayoutAdapter(Activity activity) {
        context = activity.getApplicationContext();
        textViewName = activity.findViewById(R.id.infoLayoutContactTextView);
        textViewAddress = activity.findViewById(R.id.infoLayoutAddressTextView);
        textViewPhonePharmacy = activity.findViewById(R.id.infoLayoutPhonePharmacyTextView);
        textViewPhoneHome = activity.findViewById(R.id.infoLayoutPhoneHomeTextView);
        textViewNight = activity.findViewById(R.id.infoLayoutNightTextView);
        textViewDistance = activity.findViewById(R.id.infoLayoutDistanceTextView);
        buttonDirection = activity.findViewById(R.id.infoLayoutDirectionButton);
    }

    public InfoLayoutAdapter(View itemView) {
        context = itemView.getContext();
        textViewName = itemView.findViewById(R.id.infoLayoutContactTextView);
        textViewAddress = itemView.findViewById(R.id.infoLayoutAddressTextView);
        textViewPhonePharmacy = itemView.findViewById(R.id.infoLayoutPhonePharmacyTextView);
        textViewPhoneHome = itemView.findViewById(R.id.infoLayoutPhoneHomeTextView);
        textViewNight = itemView.findViewById(R.id.infoLayoutNightTextView);
        textViewDistance = itemView.findViewById(R.id.infoLayoutDistanceTextView);
        buttonDirection = itemView.findViewById(R.id.infoLayoutDirectionButton);
    }


    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        updateViews();
    }

    private void updateViews() {
        textViewName.setText(String.format("%s %s", pharmacy.getFirstName(), pharmacy.getLastName()));
        textViewAddress.setText(pharmacy.getAddress());
        if (!pharmacy.isNight()) {
            textViewNight.setVisibility(View.GONE);
        } else {
            textViewNight.setVisibility(View.VISIBLE);
        }
        if (pharmacy.getHomePhone() == 0) {
            textViewPhonePharmacy.setText(PhoneNumberUtils.formatNumber("+357"+pharmacy.getPhone(), "CY"));
            textViewPhoneHome.setVisibility(View.GONE);
        } else {
            textViewPhoneHome.setVisibility(View.VISIBLE);
            textViewPhonePharmacy.setText(PhoneNumberUtils.formatNumber("+357"+pharmacy.getPhone(), "CY"));
            textViewPhoneHome.setText(PhoneNumberUtils.formatNumber("+357"+pharmacy.getHomePhone(), "CY"));
        }
        if (pharmacy.getDistance() == 0) {
            textViewDistance.setVisibility(View.GONE);
        } else {
            textViewDistance.setVisibility(View.VISIBLE);
            textViewDistance.setText(String.format(Locale.getDefault(), "%3.1f km",pharmacy.getDistance()));
        }
        buttonDirection.setOnClickListener(view -> infoLayoutDirectionButtonCallback());
    }

    private void infoLayoutDirectionButtonCallback() {
        Uri gmmIntentUri
                = Uri.parse(String.format(Locale.ENGLISH
                ,"geo:%f,%f?q=%f,%f(%s %s)"
                ,pharmacy.getLatitude(),pharmacy.getLongitude()
                ,pharmacy.getLatitude(),pharmacy.getLongitude()
                ,pharmacy.getFirstName(),pharmacy.getLastName()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }}
