package com.lucianpiros.accountmanagement.restapi.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Me class container - used as parameter for HTTP POST request
 */
public class SerializableMe {
    @SerializedName("name")
    public String name;

    @SerializedName("location")
    public String location;

    @SerializedName("birthdate")
    public String birthdate;

    public SerializableMe(String name, String location, String birthdate) {
        this.name = name;
        this.location = location;
        this.birthdate = birthdate;
    }
}