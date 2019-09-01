package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Signup request HTTP POST response
 */
public class MeResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("name")
    public String name;

    @SerializedName("location")
    public String location;

    @SerializedName("birthday")
    public String birthday;
}