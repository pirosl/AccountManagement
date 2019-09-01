package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Login request HTTP POST response
 */
public class LoginResponse {

    public class Data {
        @SerializedName("user_token")
        public String user_token;

        @SerializedName("api_token")
        public String api_token;
    }

    @SerializedName("message")
    public String message;

    @SerializedName("error_short_code")
    public String error_short_code;

    @SerializedName("data")
    public Data data;

}
