package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import static com.easysolutionscyprus.pharmacy.Main.view.MainActivity.databaseReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.easysolutionscyprus.pharmacy.Pharmacy.view.MapsActivity;
import com.easysolutionscyprus.pharmacy.Preferences.model.Favorites;
import com.easysolutionscyprus.pharmacy.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class InfoLayoutAdapter {
    Pharmacy pharmacy;
    public boolean isExpanded = false;
    private final Context context;
    private final Favorites favorites;
    private final MaterialButton buttonName, buttonAddress, buttonPhonePharmacy,
            buttonPhoneHome, buttonGetDirections, buttonCallPharmacy, buttonCallHome,
            buttonIsOpen, buttonIsOpenDetails, buttonDisconnected;
    private final TableLayout scheduleTable;
    private final TextView buttonDistance;
    private final LinearLayout expandableLayout, nightLayout, buttonLayout, isOpenLayout;
    private final ImageButton buttonBookmark, buttonExpand;
    private final List<MaterialButton> scheduleTimeList = new ArrayList<>();
    private final List<MaterialButton> scheduleDayList = new ArrayList<>();
    private final View marginView;
    private final boolean connected;

    enum openingTimesSlot {
        OPEN_IN_FIRST_PERIOD,
        CLOSED_FOR_LUNCH_BREAK,
        OPEN_IN_SECOND_PERIOD,
        CLOSED_OPENS_TOMORROW,
        CLOSED_FOR_MORE_THAN_ONE_DAY
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
        buttonLayout = infoLayoutAdapterBuilder.buttonLayout;
        buttonIsOpenDetails = infoLayoutAdapterBuilder.buttonIsOpenDetails;
        buttonGetDirections = infoLayoutAdapterBuilder.buttonGetDirections;
        buttonCallPharmacy = infoLayoutAdapterBuilder.buttonCallPharmacy;
        buttonCallHome = infoLayoutAdapterBuilder.buttonCallHome;
        expandableLayout = infoLayoutAdapterBuilder.expandableLayout;
        nightLayout = infoLayoutAdapterBuilder.nightLayout;
        scheduleDayList.add(infoLayoutAdapterBuilder.scheduleDay1);
        scheduleDayList.add(infoLayoutAdapterBuilder.scheduleDay2);
        scheduleDayList.add(infoLayoutAdapterBuilder.scheduleDay3);
        scheduleDayList.add(infoLayoutAdapterBuilder.scheduleDay4);
        scheduleDayList.add(infoLayoutAdapterBuilder.scheduleDay5);
        scheduleDayList.add(infoLayoutAdapterBuilder.scheduleDay6);
        scheduleDayList.add(infoLayoutAdapterBuilder.scheduleDay7);
        scheduleTimeList.add(infoLayoutAdapterBuilder.scheduleTime1);
        scheduleTimeList.add(infoLayoutAdapterBuilder.scheduleTime2);
        scheduleTimeList.add(infoLayoutAdapterBuilder.scheduleTime3);
        scheduleTimeList.add(infoLayoutAdapterBuilder.scheduleTime4);
        scheduleTimeList.add(infoLayoutAdapterBuilder.scheduleTime5);
        scheduleTimeList.add(infoLayoutAdapterBuilder.scheduleTime6);
        scheduleTimeList.add(infoLayoutAdapterBuilder.scheduleTime7);
        marginView = infoLayoutAdapterBuilder.marginView;
        connected = databaseReference.isConnected();
        buttonDisconnected = infoLayoutAdapterBuilder.buttonDisconnected;
        scheduleTable = infoLayoutAdapterBuilder.scheduleTable;
        isOpenLayout = infoLayoutAdapterBuilder.isOpenLayout;
    }

    public static class InfoLayoutAdapterBuilder {
        private final Context context;
        private final MaterialButton buttonName, buttonAddress, buttonPhonePharmacy,
                buttonPhoneHome, buttonGetDirections, buttonCallPharmacy, buttonCallHome,
                buttonIsOpen, buttonIsOpenDetails, buttonDisconnected;
        private final TextView buttonDistance;
        private final LinearLayout buttonLayout, expandableLayout, nightLayout, expandLayout, isOpenLayout;
        private final ImageButton buttonBookmark, buttonExpand;
        private final TableLayout scheduleTable;
        private final MaterialButton scheduleDay1, scheduleDay2, scheduleDay3, scheduleDay4,
                scheduleDay5, scheduleDay6, scheduleDay7;
        private final MaterialButton scheduleTime1, scheduleTime2, scheduleTime3, scheduleTime4,
                scheduleTime5, scheduleTime6, scheduleTime7;
        private final View marginView;

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
            scheduleDay1 = itemView.findViewById(R.id.scheduleDay1);
            scheduleDay2 = itemView.findViewById(R.id.scheduleDay2);
            scheduleDay3 = itemView.findViewById(R.id.scheduleDay3);
            scheduleDay4 = itemView.findViewById(R.id.scheduleDay4);
            scheduleDay5 = itemView.findViewById(R.id.scheduleDay5);
            scheduleDay6 = itemView.findViewById(R.id.scheduleDay6);
            scheduleDay7 = itemView.findViewById(R.id.scheduleDay7);
            scheduleTime1 = itemView.findViewById(R.id.scheduleTime1);
            scheduleTime2 = itemView.findViewById(R.id.scheduleTime2);
            scheduleTime3 = itemView.findViewById(R.id.scheduleTime3);
            scheduleTime4 = itemView.findViewById(R.id.scheduleTime4);
            scheduleTime5 = itemView.findViewById(R.id.scheduleTime5);
            scheduleTime6 = itemView.findViewById(R.id.scheduleTime6);
            scheduleTime7 = itemView.findViewById(R.id.scheduleTime7);
            marginView = itemView.findViewById(R.id.marginView);
            buttonDisconnected = itemView.findViewById(R.id.infoLayoutDisconnectedButton);
            scheduleTable = itemView.findViewById(R.id.infoLayoutScheduleTable);
            isOpenLayout = itemView.findViewById(R.id.infoLayoutIsOpenLayout);
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
            scheduleDay1 = activity.findViewById(R.id.scheduleDay1);
            scheduleDay2 = activity.findViewById(R.id.scheduleDay2);
            scheduleDay3 = activity.findViewById(R.id.scheduleDay3);
            scheduleDay4 = activity.findViewById(R.id.scheduleDay4);
            scheduleDay5 = activity.findViewById(R.id.scheduleDay5);
            scheduleDay6 = activity.findViewById(R.id.scheduleDay6);
            scheduleDay7 = activity.findViewById(R.id.scheduleDay7);
            scheduleTime1 = activity.findViewById(R.id.scheduleTime1);
            scheduleTime2 = activity.findViewById(R.id.scheduleTime2);
            scheduleTime3 = activity.findViewById(R.id.scheduleTime3);
            scheduleTime4 = activity.findViewById(R.id.scheduleTime4);
            scheduleTime5 = activity.findViewById(R.id.scheduleTime5);
            scheduleTime6 = activity.findViewById(R.id.scheduleTime6);
            scheduleTime7 = activity.findViewById(R.id.scheduleTime7);
            marginView = activity.findViewById(R.id.marginView);
            buttonDisconnected = activity.findViewById(R.id.infoLayoutDisconnectedButton);
            scheduleTable = activity.findViewById(R.id.infoLayoutScheduleTable);
            isOpenLayout = activity.findViewById(R.id.infoLayoutIsOpenLayout);
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
        if (connected) {
            setIsOpenButtonText();
            buttonIsOpenDetails.setText(getIsOpenDetailsString());
            updateScheduleTable();
            isOpenLayout.setVisibility(View.VISIBLE);
            scheduleTable.setVisibility(View.VISIBLE);
            buttonDisconnected.setVisibility(View.GONE);
        } else {
            isOpenLayout.setVisibility(View.GONE);
            scheduleTable.setVisibility(View.GONE);
            buttonDisconnected.setVisibility(View.VISIBLE);
        }

        updateBookmarkButtonDrawable();
        buttonBookmark.setOnClickListener(view -> infoLayoutBookmarkButtonCallback());
        buttonGetDirections.setOnClickListener(view -> infoLayoutDirectionButtonCallback());
        buttonExpand.setOnClickListener(view -> infoExpandButtonCallback());
    }

    private void setIsOpenButtonText() {
        if (findOpeningTimeSlot() == openingTimesSlot.OPEN_IN_FIRST_PERIOD ||
                findOpeningTimeSlot() == openingTimesSlot.OPEN_IN_SECOND_PERIOD) {
            buttonIsOpen.setTextColor(context.getColor(R.color.green));
            buttonIsOpen.setText(R.string.open);
        } else {
            buttonIsOpen.setTextColor(context.getColor(R.color.red));
            buttonIsOpen.setText(R.string.closed);
        }
    }

    private String getIsOpenDetailsString() {
        switch(findOpeningTimeSlot()) {
            case OPEN_IN_FIRST_PERIOD:
                return String.format(context.getString(R.string.closes_at), pharmacy.getOpeningTimesList().get(0).get("end1"));
            case CLOSED_FOR_LUNCH_BREAK:
                return String.format(context.getString(R.string.opens_at), pharmacy.getOpeningTimesList().get(0).get("start2"));
            case OPEN_IN_SECOND_PERIOD:
                return String.format(context.getString(R.string.closes_at), pharmacy.getOpeningTimesList().get(0).get("end2"));
            case CLOSED_OPENS_TOMORROW:
                return String.format(context.getString(R.string.opens_at), pharmacy.getOpeningTimesList().get(1).get("start1"));
            case CLOSED_FOR_MORE_THAN_ONE_DAY:
                return String.format(context.getString(R.string.opens_on_day), findNextAvailableOpeningDay());
            default:
                return "";
        }
    }

    private String findNextAvailableOpeningDay() {
        TimeZone nicosiaTimezone = TimeZone.getTimeZone("Europe/Athens");
        Calendar calendar = Calendar.getInstance(nicosiaTimezone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        for (int i=1; i < pharmacy.getOpeningTimesList().size(); i++) {
            if (pharmacy.getOpeningTimesList().get(i) != null) {
                calendar.add(Calendar.DAY_OF_YEAR, i);
                break;
            }
        }
        return dateFormat.format(calendar.getTime());
    }

    private openingTimesSlot findOpeningTimeSlot() {
        // First get local time in Cyprus
        LocalTime currentTimeInCyprus = getCurrentTimeInCyprus();

        // Read all pharmacy opening times
        HashMap<String, String> openingTimes = pharmacy.getOpeningTimesList().get(0);
        if (openingTimes == null) {
            if (pharmacy.getOpeningTimesList().get(1) == null) {
                return openingTimesSlot.CLOSED_FOR_MORE_THAN_ONE_DAY;
            }
            return openingTimesSlot.CLOSED_OPENS_TOMORROW;
        }
        String start1 = openingTimes.get("start1");
        String end1 = openingTimes.get("end1");
        String start2 = openingTimes.get("start2");
        String end2 = openingTimes.get("end2");

        // Check first time period
        LocalTime startTime1 = LocalTime.parse(start1);
        LocalTime endTime1 = LocalTime.parse(end1);
        if (currentTimeInCyprus.isAfter(startTime1) && currentTimeInCyprus.isBefore(endTime1)) {
            return openingTimesSlot.OPEN_IN_FIRST_PERIOD;
        }

        if (start2 == null && end2 == null) {
            // Pharmacy will not open next morning -> find which day it ope s
            if (pharmacy.getOpeningTimesList().get(1) == null) {
                return openingTimesSlot.CLOSED_FOR_MORE_THAN_ONE_DAY;
            }
            // Pharmacy will open in the morning again
            return openingTimesSlot.CLOSED_OPENS_TOMORROW;
        }

        // Check second time period
        LocalTime startTime2 = LocalTime.parse(start2);
        LocalTime endTime2 = LocalTime.parse(end2);
        if (currentTimeInCyprus.isAfter(startTime2) && currentTimeInCyprus.isBefore(endTime2)) {
            return openingTimesSlot.OPEN_IN_SECOND_PERIOD;
        }

        // Check if it is during lunch break
        if (currentTimeInCyprus.isAfter(endTime1) && currentTimeInCyprus.isBefore(startTime2)) {
            return openingTimesSlot.CLOSED_FOR_LUNCH_BREAK;
        }

        // It will be closed for more than one day
        if (pharmacy.getOpeningTimesList().get(1) == null) {
            return openingTimesSlot.CLOSED_FOR_MORE_THAN_ONE_DAY;
        }

        // Otherwise it will be open the next day (or morning)
        return openingTimesSlot.CLOSED_OPENS_TOMORROW;
    }

    private LocalTime getCurrentTimeInCyprus() {
        // Get current time in Nicosia
        ZoneId nicosiaZoneId = ZoneId.of("Europe/Athens");
        return LocalTime.now(nicosiaZoneId);
    }

    private void updateScheduleTable() {
        // Set the timezone to Nicosia
        TimeZone nicosiaTimezone = TimeZone.getTimeZone("Europe/Athens");
        Calendar calendar = Calendar.getInstance(nicosiaTimezone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        for (int i = 0; i < scheduleDayList.size(); i++) {
            try {
                scheduleDayList.get(i).setText(dateFormat.format(calendar.getTime()));
                scheduleTimeList.get(i).setText(getOpeningTimesString(
                        pharmacy.getOpeningTimesList().get(i)));
                scheduleTimeList.get(i).setTextColor(context.getColor(R.color.black));
            } catch (Exception e) {
                scheduleTimeList.get(i).setTextColor(context.getColor(R.color.red));
                scheduleTimeList.get(i).setText(context.getString(R.string.closed));
            } finally {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }

    private String getOpeningTimesString(HashMap<String, String> openingTimes) {
        // Case where the pharmacy is open for two time periods in one day
        String str1 = String.join("-",openingTimes.get("start1"), openingTimes.get("end1"));
        if (openingTimes.get("start2") != null) {
            String str2 = String.join("-",openingTimes.get("start2"), openingTimes.get("end2"));
            return String.join("\n", str1, str2);
        }

        // Case where the pharmacy is open for one time period in one day
        return str1;
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
        buttonLayout.setVisibility(View.VISIBLE);
        expandableLayout.setVisibility(View.VISIBLE);
        buttonExpand.setImageResource(R.drawable.ic_expand_less);
        if (connected) {
            marginView.setVisibility(View.VISIBLE);
        }
    }

    private void collapseLayout() {
        isExpanded = false;
        buttonLayout.setVisibility(View.GONE);
        expandableLayout.setVisibility(View.GONE);
        buttonExpand.setImageResource(R.drawable.ic_expand_more);
        marginView.setVisibility(View.GONE);
    }

    private void infoExpandButtonCallback() {
        if (isExpanded) {
            collapseLayout();
        } else {
            expandLayout();
        }
    }
}