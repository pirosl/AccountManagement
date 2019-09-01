package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Me request HTTP GET response
 */
public class MeResponse {

    public class Data {

        public class Profile {
            @SerializedName("location")
            public String location;

            @SerializedName("birthday")
            public String birthday;
        }

        @SerializedName("name")
        public String name;

        @SerializedName("email")
        public String email;

        @SerializedName("profile")
        public Profile profile;
    }

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public Data data;
}