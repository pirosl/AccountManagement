package com.lucianpiros.accountmanagement.aidl;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class SignupUnitTest {

    private static String NAME = "name";
    private static String EMAIL = "email";
    private static String PASSWORD = "password";

    @Test
    public void testSignupIsParcelable() {
        Signup signup = new Signup(NAME, EMAIL, PASSWORD, PASSWORD);

        Parcel parcel = Parcel.obtain();
        signup.writeToParcel(parcel, signup.describeContents());
        parcel.setDataPosition(0);

        Signup createdFromParcel = Signup.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getName(), is(NAME));
        assertThat(createdFromParcel.getEmail(), is(EMAIL));
        assertThat(createdFromParcel.getPassword(), is(PASSWORD));
        assertThat(createdFromParcel.getPassword2(), is(PASSWORD));
    }
}