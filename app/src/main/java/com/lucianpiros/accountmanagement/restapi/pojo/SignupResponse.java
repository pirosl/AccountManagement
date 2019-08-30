package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("error_short_code")
    public String error_short_code;
}
