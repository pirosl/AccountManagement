package com.lucianpiros.accountmanagement.restapi;

import com.google.gson.Gson;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableSignup;
import com.lucianpiros.accountmanagement.restapi.pojo.SignupResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * REST client implemented as Singleton
 */
public class RESTClient {

    /**
     * Callback interface for REST API calls
     */
    public interface Callback {
        void onResponse(String message);
        void onFailure(int code, String error);
    }

    private final static String BASE_URL = "https://dev.refinemirror.com/api/v1/";
    private static RESTClient restClientInstance = null;
    private Retrofit retrofit;
    private String apiToken;

    /**
     * Private constructor
     */
    private RESTClient() {
        retrofit = null;
    }

    public static synchronized RESTClient getInstance() {
        if(restClientInstance == null) {
            restClientInstance = new RESTClient();
        }

        return restClientInstance;
    }

    /**
     * Returns a Retrofit client
     * @return - Retrofit client
     */
    private Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    /**
     * Signup Rest API call
     * @param serializableSignup - sign up parameters
     * @param callback - callback instance
     */
    public void signUp(SerializableSignup serializableSignup, final Callback callback) {
        UserAccountAPI userAccountAPI = getClient().create(UserAccountAPI.class);

        Call<SignupResponse> call = userAccountAPI.signUp(serializableSignup);

        call.enqueue(new retrofit2.Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if(response.isSuccessful()) {
                    SignupResponse signupResponse = response.body();
                    apiToken = signupResponse.api_token;
                    callback.onResponse(signupResponse.message);
                }
                else {
                    // use Gson to deserialize the response
                    SignupResponse signupResponse = new Gson().fromJson(response.errorBody().charStream(), SignupResponse.class);
                    callback.onFailure(response.code(), signupResponse.message);
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                callback.onFailure(-1, t.getMessage());
            }
        });
    }

}
