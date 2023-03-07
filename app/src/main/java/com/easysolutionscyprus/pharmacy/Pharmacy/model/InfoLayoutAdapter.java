package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easysolutionscyprus.pharmacy.Pharmacy.view.MapsActivity;
import com.easysolutionscyprus.pharmacy.Preferences.model.Favorites;
import com.easysolutionscyprus.pharmacy.R;
import com.google.android.material.button.MaterialButton;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

public class InfoLayoutAdapter {
    Pharmacy pharmacy;
    public boolean isExpanded = false;
    private final Context context;
    private final Favorites favorites;
    private final MaterialButton buttonName, buttonAddress, buttonPhonePharmacy,
            buttonPhoneHome, buttonGetDirections, buttonCallPharmacy, buttonCallHome,
            buttonIsOpen, buttonIsOpenDetails;
    private final TextView buttonDistance;
    private final LinearLayout expandableLayout, nightLayout;
    private final ImageButton buttonBookmark, buttonExpand;

    enum OpeningTimesSlot {
        OPEN_FIRST,
        CLOSED_LUNCH_BREAK,
        OPEN_SECOND,
        CLOSED_NEXT_DAY
    }

    private InfoLayoutAdapter(InfoLayoutAdapterBuilder infoLayoutAdapterBuilder) {
        context = infoLayoutAdapterBuilder.context;
        favorites = new Favorites(context);
        buttonName = infoLayoutAdapterBuilder.buttonName;
        buttonAddress = infoLayoutAdapterBuilder.buttonAddress;
        buttonDistance = infoLayoutAdapterBuilder.buttonDistance;
        buttonPhonePharmacy = infoLayoutAdapterBuilder.buttonPhonePharmacy;
        buttonPhoneHome = infoLayoutAdapterBuilder.buttonPhoneHome;
        buttonBookmark = infoLayoutAdapterBuilder.buttonBookmark;
        buttonExpand = infoLayoutAdapterBuilder.buttonExpand;
        buttonIsOpen = infoLayoutAdapterBuilder.buttonIsOpen;
        buttonIsOpenDetails = infoLayoutAdapterBuilder.buttonIsOpenDetails;
        buttonGetDirections = infoLayoutAdapterBuilder.buttonGetDirections;
        buttonCallPharmacy = infoLayoutAdapterBuilder.buttonCallPharmacy;
        buttonCallHome = infoLayoutAdapterBuilder.buttonCallHome;
        expandableLayout = infoLayoutAdapterBuilder.expandableLayout;
        nightLayout = infoLayoutAdapterBuilder.nightLayout;
    }

    public static class InfoLayoutAdapterBuilder {
        private final Context context;
        private final MaterialButton buttonName, buttonAddress, buttonPhonePharmacy,
                buttonPhoneHome, buttonGetDirections, buttonCallPharmacy, buttonCallHome,
                buttonIsOpen, buttonIsOpenDetails;
        private final TextView buttonDistance;
        private final LinearLayout buttonLayout, expandableLayout, nightLayout, expandLayout;
        private final ImageButton buttonBookmark, buttonExpand;

        public InfoLayoutAdapterBuilder(View itemView) {
            context = itemView.getContext();
            buttonName = itemView.findViewById(R.id.infoLayoutNameButton);
            buttonBookmark = itemView.findViewById(R.id.infoLayoutBookmarksButton);
            buttonAddress = itemView.findViewById(R.id.infoLayoutAddressButton);
            buttonPhonePharmacy = itemView.findViewById(R.id.infoLayoutPhonePharmacyButton);
            buttonPhoneHome = itemView.findViewById(R.id.infoLayoutPhoneHomeButton);
            buttonLayout = itemView.findViewById(R.id.infoLayoutButtonsLayout);
            buttonDistance = itemView.findViewById(R.id.infoLayoutDistanceButton);
            buttonIsOpen = itemView.findViewById(R.id.infoLayoutIsOpenButton);
            buttonIsOpenDetails = itemView.findViewById(R.id.infoLayoutIsOpenDetailsButton);
            buttonGetDirections = itemView.findViewById(R.id.infoLayoutGetDirectionsButton);
            buttonCallPharmacy = itemView.findViewById(R.id.infoLayoutCallPharmacyButton);
            buttonCallHome = itemView.findViewById(R.id.infoLayoutCallHomeButton);
            expandableLayout = itemView.findViewById(R.id.infoLayoutExpandableLayout);
            nightLayout = itemView.findViewById(R.id.infoLayoutNightLayout);
            buttonExpand = itemView.findViewById(R.id.infoLayoutExpandButton);
            expandLayout = itemView.findViewById(R.id.infoLayoutExpandLayout);
        }

        public InfoLayoutAdapterBuilder(Activity activity) {
            context = activity.getApplicationContext();
            buttonName = activity.findViewById(R.id.infoLayoutNameButton);
            buttonBookmark = activity.findViewById(R.id.infoLayoutBookmarksButton);
            buttonAddress = activity.findViewById(R.id.infoLayoutAddressButton);
            buttonPhonePharmacy = activity.findViewById(R.id.infoLayoutPhonePharmacyButton);
            buttonPhoneHome = activity.findViewById(R.id.infoLayoutPhoneHomeButton);
            buttonLayout = activity.findViewById(R.id.infoLayoutButtonsLayout);
            buttonDistance = activity.findViewById(R.id.infoLayoutDistanceButton);
            buttonIsOpen = activity.findViewById(R.id.infoLayoutIsOpenButton);
            buttonIsOpenDetails = activity.findViewById(R.id.infoLayoutIsOpenDetailsButton);
            buttonGetDirections = activity.findViewById(R.id.infoLayoutGetDirectionsButton);
            buttonCallPharmacy = activity.findViewById(R.id.infoLayoutCallPharmacyButton);
            buttonCallHome = activity.findViewById(R.id.infoLayoutCallHomeButton);
            expandableLayout = activity.findViewById(R.id.infoLayoutExpandableLayout);
            nightLayout = activity.findViewById(R.id.infoLayoutNightLayout);
            buttonExpand = activity.findViewById(R.id.infoLayoutExpandButton);
            expandLayout = activity.findViewById(R.id.infoLayoutExpandLayout);
        }

        public InfoLayoutAdapterBuilder withAddressButtonDisabled() {
            buttonAddress.setEnabled(false);
            buttonAddress.setCompoundDrawables(buttonAddress.getCompoundDrawables()[0],null,null,null);
            return this;
        }

        public InfoLayoutAdapterBuilder withButtonLayout() {
            buttonLayout.setVisibility(View.VISIBLE);
            return this;
        }

        public InfoLayoutAdapterBuilder withExpandLayout() {
            expandLayout.setVisibility(View.VISIBLE);
            return this;
        }

        public InfoLayoutAdapter build() {
            return new InfoLayoutAdapter(this);
        }
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        updateViews();
    }

    private void updateViews() {
        collapseLayout();
        buttonName.setText(String.format("%s %s", pharmacy.getFirstName(), pharmacy.getLastName()));
        buttonAddress.setText(pharmacy.getAddress());
        buttonAddress.setOnClickListener(view -> infoLayoutAddressButtonCallback());
        if (!pharmacy.isNight()) {
            nightLayout.setVisibility(View.GONE);
        } else {
            nightLayout.setVisibility(View.VISIBLE);
        }
        if (pharmacy.getHomePhone() == 0) {
            buttonPhonePharmacy.setText(PhoneNumberUtils.formatNumber("+357"+pharmacy.getPhone(), "CY"));
            buttonPhonePharmacy.setOnClickListener(view -> infoLayoutPhonePharmacyButtonCallback());
            buttonCallPharmacy.setOnClickListener(view -> infoLayoutPhonePharmacyButtonCallback());
            buttonPhoneHome.setVisibility(View.GONE);
            buttonCallHome.setVisibility(View.GONE);
        } else {
            buttonPhoneHome.setVisibility(View.VISIBLE);
            buttonCallHome.setVisibility(View.VISIBLE);
            buttonPhonePharmacy.setText(PhoneNumberUtils.formatNumber("+357"+pharmacy.getPhone(), "CY"));
            buttonPhonePharmacy.setOnClickListener(view -> infoLayoutPhonePharmacyButtonCallback());
            buttonCallPharmacy.setOnClickListener(view -> infoLayoutPhonePharmacyButtonCallback());
            buttonPhoneHome.setText(PhoneNumberUtils.formatNumber("+357"+pharmacy.getHomePhone(), "CY"));
            buttonPhoneHome.setOnClickListener(view -> infoLayoutPhoneHomeButtonCallback());
            buttonCallHome.setOnClickListener(view -> infoLayoutPhoneHomeButtonCallback());
        }
        if (pharmacy.getDistance() == 0) {
            buttonDistance.setVisibility(View.GONE);
        } else {
            buttonDistance.setVisibility(View.VISIBLE);
            buttonDistance.setText(String.format(Locale.getDefault(), "~ %3.1f km",pharmacy.getDistance()));
        }
        setIsOpenButtonText();
        buttonIsOpenDetails.setText(getIsOpenDetailsString());
        updateBookmarkButtonDrawable();
        buttonBookmark.setOnClickListener(view -> infoLayoutBookmarkButtonCallback());
        buttonGetDirections.setOnClickListener(view -> infoLayoutDirectionButtonCallback());
        buttonExpand.setOnClickListener(view -> infoExpandButtonCallback());
    }

    private void setIsOpenButtonText() {
        if (findOpeningTimesSlot() == OpeningTimesSlot.OPEN_FIRST ||
                findOpeningTimesSlot() == OpeningTimesSlot.OPEN_SECOND) {
            buttonIsOpen.setTextColor(context.getColor(R.color.green));
            buttonIsOpen.setText(R.string.open);
        } else {
            buttonIsOpen.setTextColor(context.getColor(R.color.red));
            buttonIsOpen.setText(R.string.closed);
        }
    }

    private String getIsOpenDetailsString() {
        switch(findOpeningTimesSlot()) {
            case OPEN_FIRST:
                return String.format(context.getString(R.string.closes_at), pharmacy.getOpeningTimes().get("end1"));
            case CLOSED_LUNCH_BREAK:
                return String.format(context.getString(R.string.opens_at), pharmacy.getOpeningTimes().get("start2"));
            case OPEN_SECOND:
                return String.format(context.getString(R.string.closes_at), pharmacy.getOpeningTimes().get("end2"));
            case CLOSED_NEXT_DAY:
                return String.format(context.getString(R.string.opens_at), pharmacy.getOpeningTimes().get("start1"));
            default:
                return "";
        }
    }

    private OpeningTimesSlot findOpeningTimesSlot() {
        // First get local time in Cyprus
        LocalTime currentTimeInCyprus = getCurrentTimeInCyprus();

        // Read all pharmacy opening times
        String start1 = pharmacy.getOpeningTimes().get("start1");
        String end1 = pharmacy.getOpeningTimes().get("end1");
        String start2 = pharmacy.getOpeningTimes().get("start2");
        String end2 = pharmacy.getOpeningTimes().get("end2");

        // Check first time period
        LocalTime startTime1 = LocalTime.parse(start1);
        LocalTime endTime1 = LocalTime.parse(end1);
        if (currentTimeInCyprus.isAfter(startTime1) && currentTimeInCyprus.isBefore(endTime1)) {
            return OpeningTimesSlot.OPEN_FIRST;
        }

        // If there is not second time slot, then the pharmacy will open in the morning again
        if (start2 == null && end2 == null) {
            return OpeningTimesSlot.CLOSED_NEXT_DAY;
        }

        // Check second time period
        LocalTime startTime2 = LocalTime.parse(start2);
        LocalTime endTime2 = LocalTime.parse(end2);
        if (currentTimeInCyprus.isAfter(startTime2) && currentTimeInCyprus.isBefore(endTime2)) {
            return OpeningTimesSlot.OPEN_SECOND;
        }

        // Check if it is during lunch break
        if (currentTimeInCyprus.isAfter(endTime1) && currentTimeInCyprus.isBefore(startTime2)) {
            return OpeningTimesSlot.CLOSED_LUNCH_BREAK;
        }

        // Otherwise it will be open the next day (or morning)
        return OpeningTimesSlot.CLOSED_NEXT_DAY;
    }

    private LocalTime getCurrentTimeInCyprus() {
        // Get current time in Nicosia
        ZoneId nicosiaZoneId = ZoneId.of("Asia/Nicosia");
        return LocalTime.now(nicosiaZoneId);
    }

    private void infoLayoutAddressButtonCallback() {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("pharmacy", pharmacy);
        context.startActivity(intent);
    }

    private void infoLayoutPhonePharmacyButtonCallback() {
        makeCall(pharmacy.getPhone());
    }

    private void infoLayoutPhoneHomeButtonCallback() {
        makeCall(pharmacy.getHomePhone());
    }

    private void makeCall(int phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String phoneNumberFormatted = PhoneNumberUtils.formatNumber("+357"+phoneNumber, "CY");
        intent.setData(Uri.parse("tel:" + phoneNumberFormatted));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void infoLayoutBookmarkButtonCallback() {
        if (favorites.isFavorite(pharmacy.getId())) {
            favorites.deleteFavorite(pharmacy.getId());
        } else {
            favorites.addFavorite(pharmacy.getId());
        }
        updateBookmarkButtonDrawable();
    }
    private void updateBookmarkButtonDrawable() {
        if (favorites.isFavorite(pharmacy.getId())) {
            buttonBookmark.setImageResource(R.drawable.ic_favorite);
        } else {
            buttonBookmark.setImageResource(R.drawable.ic_favorite_add);
        }
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
    }

    private void expandLayout() {
        isExpanded = true;
        expandableLayout.setVisibility(View.VISIBLE);
        buttonExpand.setImageResource(R.drawable.ic_expand_less);
    }

    private void collapseLayout() {
        isExpanded = false;
        expandableLayout.setVisibility(View.GONE);
        buttonExpand.setImageResource(R.drawable.ic_expand_more);
    }

    private void infoExpandButtonCallback() {
        if (isExpanded) {
            collapseLayout();
        } else {
            expandLayout();
        }
    }
}
