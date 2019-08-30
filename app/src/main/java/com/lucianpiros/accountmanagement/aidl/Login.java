package com.lucianpiros.accountmanagement.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Login class container. Implements Parcelable - holds all information required to login to REST service
 */
public final class Login implements Parcelable {
    private String email;
    private String password;

    protected Login(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Login> CREATOR = new Creator<Login>() {
        @Override
        public Login createFromParcel(Parcel in) {
            return new Login(in);
        }

        @Override
        public Login[] newArray(int size) {
            return new Login[size];
        }
    };

    @Override
    public int describeContents() {
        // default implementation
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(password);
    }

    private void readFromParcel(Parcel parcel) {
        email = parcel.readString();
        password = parcel.readString();
    }
}