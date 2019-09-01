package com.lucianpiros.accountmanagement.restapi;

import com.lucianpiros.accountmanagement.restapi.pojo.LoginResponse;
import com.lucianpiros.accountmanagement.restapi.pojo.MeResponse;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableLogin;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableSignup;
import com.lucianpiros.accountmanagement.restapi.pojo.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * REST Restrofit API
 */
public interface UserAccountAPI {
    @POST("auth/signup")
    Call<SignupResponse> signUp(@Body SerializableSignup serializableSignup);

    @POST("auth/login")
    Call<LoginResponse> login(@Body SerializableLogin serializableLogin);

    @GET("user/me")
    Call<MeResponse> retrieveMeDetails(@Header("Authorization") String auth);

}
