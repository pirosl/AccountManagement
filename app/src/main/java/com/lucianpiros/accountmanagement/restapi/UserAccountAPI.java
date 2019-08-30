package com.lucianpiros.accountmanagement.restapi;

import com.lucianpiros.accountmanagement.restapi.pojo.SerializableSignup;
import com.lucianpiros.accountmanagement.restapi.pojo.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAccountAPI {
    @POST("/auth/signup ")
    Call<SignupResponse> createUser(@Body SerializableSignup serializableSignup);
}