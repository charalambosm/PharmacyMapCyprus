package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pharmacy implements Parcelable, ClusterItem {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String district;
    private int phone;
    private int homePhone;
    private double latitude;
    private double longitude;
    private double distance;
    private String firstNameNormalized;
    private String lastNameNormalized;
    private String addressNormalized;
    private List<Boolean> nightList;
    private List<HashMap<String, String>> openingTimesList;

    @SuppressWarnings("unused")
    public Pharmacy() {}

    protected Pharmacy(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        address = in.readString();
        district = in.readString();
        nightList = new ArrayList<>();
        in.readList(nightList, Boolean.class.getClassLoader());
        phone = in.readInt();
        homePhone = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distance = in.readDouble();
        openingTimesList = new ArrayList<>();
        in.readList(openingTimesList, HashMap.class.getClassLoader());
    }

    public static final Creator<Pharmacy> CREATOR = new Creator<Pharmacy>() {
        @Override
        public Pharmacy createFromParcel(Parcel in) {
            return new Pharmacy(in);
        }

        @Override
        public Pharmacy[] newArray(int size) {
            return new Pharmacy[size];
        }
    };

    public void postProcess() {
        addressNormalized = normalizeText(address);
        firstNameNormalized = normalizeText(firstName);
        lastNameNormalized = normalizeText(lastName);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getDistrict() {
        return district;
    }

    public boolean isNight() {
        return nightList.get(0);
    }

    public int getPhone() {
        return phone;
    }

    public int getHomePhone() {
        return homePhone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance() {return distance;}

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getFirstNameNormalized() {
        return firstNameNormalized;
    }

    public String getLastNameNormalized() {
        return lastNameNormalized;
    }

    public String getAddressNormalized() {
        return addressNormalized;
    }

    public HashMap<String, String> getOpeningTimes() {
        return openingTimesList.get(0);
    }

    private String normalizeText(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalizedText.replaceAll("\\p{M}", "");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(address);
        parcel.writeString(district);
        parcel.writeList(nightList);
        parcel.writeInt(phone);
        parcel.writeInt(homePhone);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(distance);
        parcel.writeList(openingTimesList);
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Nullable
    @Override
    public String getTitle() {
        return firstName + " " + lastName;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }

    public List<Boolean> getNightList() {
        return nightList;
    }

    public List<HashMap<String, String>> getOpeningTimesList() {
        return openingTimesList;
    }
}