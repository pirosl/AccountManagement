package com.lucianpiros.accountmanagement.aidl;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LoginUnitTest {

    private static String EMAIL = "email";
    private static String PASSWORD = "password";

    @Test
    public void testLoginIsParcelable() {
        Login login = new Login(EMAIL, PASSWORD);

        Parcel parcel = Parcel.obtain();
        login.writeToParcel(parcel, login.describeContents());
        parcel.setDataPosition(0);

        Login createdFromParcel = Login.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getEmail(), is(EMAIL));
        assertThat(createdFromParcel.getPassword(), is(PASSWORD));
    }
}