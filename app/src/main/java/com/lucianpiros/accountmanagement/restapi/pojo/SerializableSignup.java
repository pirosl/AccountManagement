package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Signup class container - used as parameter for HTTP POST request
 */
public class SerializableSignup {
    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("password2")
    public String password2;

    public SerializableSignup(String name, String email, String password, String password2) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.password2 = password2;
    }
}
