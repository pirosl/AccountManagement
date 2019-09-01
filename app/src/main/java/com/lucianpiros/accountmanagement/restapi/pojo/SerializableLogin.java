package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Login class container - used as parameter for HTTP POST request
 */
public class SerializableLogin {
    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    public SerializableLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}