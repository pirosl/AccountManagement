package com.lucianpiros.accountmanagement.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Signup class container. Implements Parcelable - holds all information required to signup through REST service
 */
public final class Signup implements Parcelable {
    private String name;
    private String email;
    private String password;
    private String password2;

    public Signup(String name, String email, String password, String password2) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.password2 = password2;
    }

    protected Signup(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Signup> CREATOR = new Creator<Signup>() {
        @Override
        public Signup createFromParcel(Parcel in) {
            return new Signup(in);
        }

        @Override
        public Signup[] newArray(int size) {
            return new Signup[size];
        }
    };

    @Override
    public int describeContents() {
        // default implementation
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(password2);
    }

    private void readFromParcel(Parcel parcel) {
        name = parcel.readString();
        email = parcel.readString();
        password = parcel.readString();
        password2 = parcel.readString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }
}
