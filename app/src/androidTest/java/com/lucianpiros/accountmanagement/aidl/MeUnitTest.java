package com.lucianpiros.accountmanagement.aidl;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MeUnitTest {

    private static String NAME = "name";
    private static String LOCATION = "location";
    private static String BIRTHDAY = "yyyy-mm-dd";

    @Test
    public void testMeIsParcelable() {
        Me me = new Me(NAME, LOCATION, BIRTHDAY);

        Parcel parcel = Parcel.obtain();
        me.writeToParcel(parcel, me.describeContents());
        parcel.setDataPosition(0);

        Me createdFromParcel = Me.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getName(), is(NAME));
        assertThat(createdFromParcel.getLocation(), is(LOCATION));
        assertThat(createdFromParcel.getBirthdate(), is(BIRTHDAY));
    }
}