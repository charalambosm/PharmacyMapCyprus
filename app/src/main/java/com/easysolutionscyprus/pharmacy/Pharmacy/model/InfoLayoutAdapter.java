package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.TextView;

import com.easysolutionscyprus.pharmacy.R;

public class InfoLayoutAdapter {
    Pharmacy pharmacy;
    protected final TextView textViewName;
    protected final TextView textViewAddress;
    protected final TextView textViewNight;
    protected final TextView textViewPhonePharmacy;
    protected final TextView textViewPhoneHome;

    public InfoLayoutAdapter(Activity activity) {
        textViewName = activity.findViewById(R.id.infoLayoutContactTextView);
        textViewAddress = activity.findViewById(R.id.infoLayoutAddressTextView);
        textViewPhonePharmacy = activity.findViewById(R.id.infoLayoutPhonePharmacyTextView);
        textViewPhoneHome = activity.findViewById(R.id.infoLayoutPhoneHomeTextView);
        textViewNight = activity.findViewById(R.id.infoLayoutNightTextView);
    }

    public InfoLayoutAdapter(View itemView) {
        textViewName = itemView.findViewById(R.id.infoLayoutContactTextView);
        textViewAddress = itemView.findViewById(R.id.infoLayoutAddressTextView);
        textViewPhonePharmacy = itemView.findViewById(R.id.infoLayoutPhonePharmacyTextView);
        textViewPhoneHome = itemView.findViewById(R.id.infoLayoutPhoneHomeTextView);
        textViewNight = itemView.findViewById(R.id.infoLayoutNightTextView);
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
    }
}
