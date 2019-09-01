package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Signup request HTTP POST response
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

        @SerializedName("profile")
        public Profile profile;
    }

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public Data data;
}