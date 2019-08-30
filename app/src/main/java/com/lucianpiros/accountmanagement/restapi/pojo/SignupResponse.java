package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Signup request HTTP POST response
 */
public class SignupResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("error_short_code")
    public String error_short_code;

    @SerializedName("user_uuid")
    public String user_uuid;

    @SerializedName("user_token")
    public String user_token;

    @SerializedName("api_token")
    public String api_token;
}
