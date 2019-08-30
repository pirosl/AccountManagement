package com.lucianpiros.accountmanagement.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Me class container. Implements Parcelable - holds all user account information
 */
public final class Me implements Parcelable {
    private String name;
    private String location;
    private String birthdate;

    public Me(String name, String location, String birthdate) {
        this.name = name;
        this.location = location;
        this.birthdate = birthdate;
    }

    protected Me(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Me> CREATOR = new Creator<Me>() {
        @Override
        public Me createFromParcel(Parcel in) {
            return new Me(in);
        }

        @Override
        public Me[] newArray(int size) {
            return new Me[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(birthdate);
    }

    private void readFromParcel(Parcel parcel) {
        name = parcel.readString();
        location = parcel.readString();
        birthdate = parcel.readString();
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
