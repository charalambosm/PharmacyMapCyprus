package com.easysolutionscyprus.pharmacy.Pharmacy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pharmacy implements Parcelable {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String district;
    private boolean night;
    private int phone;
    private int homePhone;
    private double latitude;
    private double longitude;
    private double distance;

    @SuppressWarnings("unused")
    public Pharmacy() {}

    protected Pharmacy(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        address = in.readString();
        district = in.readString();
        night = in.readByte() != 0;
        phone = in.readInt();
        homePhone = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distance = in.readDouble();
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
        return night;
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
        parcel.writeByte((byte) (night ? 1 : 0));
        parcel.writeInt(phone);
        parcel.writeInt(homePhone);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(distance);
    }
}