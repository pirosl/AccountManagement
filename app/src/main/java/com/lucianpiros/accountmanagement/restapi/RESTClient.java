package com.lucianpiros.accountmanagement.restapi;

import com.google.gson.Gson;
import com.lucianpiros.accountmanagement.restapi.pojo.LoginResponse;
import com.lucianpiros.accountmanagement.restapi.pojo.MeResponse;
import com.lucianpiros.accountmanagement.restapi.pojo.MeUpdateResponse;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableLogin;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableMe;
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
                    apiToken = signupResponse.data.api_token;
                    callback.onResponse(signupResponse.message);
                }
                else {
                    // use Gson to deserialize the response
                    apiToken = null;
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

    /**
     * Login Rest API call
     * @param serializableLogin - login parameters
     * @param callback - callback instance
     */
    public void login(SerializableLogin serializableLogin, final Callback callback) {
        UserAccountAPI userAccountAPI = getClient().create(UserAccountAPI.class);

        Call<LoginResponse> call = userAccountAPI.login(serializableLogin);

        call.enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    apiToken = loginResponse.data.api_token;
                    callback.onResponse(loginResponse.message);
                }
                else {
                    // use Gson to deserialize the response
                    apiToken = null;
                    LoginResponse loginResponse = new Gson().fromJson(response.errorBody().charStream(), LoginResponse.class);
                    callback.onFailure(response.code(), loginResponse.message);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(-1, t.getMessage());
            }
        });
    }

    /**
     * Me Rest API call
     * @param callback - callback instance
     */
    public void retrieveMeDetails(final Callback callback) {
        UserAccountAPI userAccountAPI = getClient().create(UserAccountAPI.class);

        Call<MeResponse> call = userAccountAPI.retrieveMeDetails("Bearer " + apiToken);

        call.enqueue(new retrofit2.Callback<MeResponse>() {
            @Override
            public void onResponse(Call<MeResponse> call, Response<MeResponse> response) {
                if(response.isSuccessful()) {
                    MeResponse meResponse = response.body();
                    // TODO: we'll have to save details on our persistence

                    callback.onResponse(meResponse.message);
                }
                else {
                    // use Gson to deserialize the response
                    MeResponse meResponse = new Gson().fromJson(response.errorBody().charStream(), MeResponse.class);
                    callback.onFailure(response.code(), meResponse.message);
                }
            }

            @Override
            public void onFailure(Call<MeResponse> call, Throwable t) {
                callback.onFailure(-1, t.getMessage());
            }
        });
    }

    /**
     * Me Rest PATCH API call
     * @param callback - callback instance
     */
    public void updateMeDetails(SerializableMe serializableMe, final Callback callback) {
        UserAccountAPI userAccountAPI = getClient().create(UserAccountAPI.class);

        Call<MeUpdateResponse> call = userAccountAPI.updateMeDetails(serializableMe, "Bearer " + apiToken);

        call.enqueue(new retrofit2.Callback<MeUpdateResponse>() {
            @Override
            public void onResponse(Call<MeUpdateResponse> call, Response<MeUpdateResponse> response) {
                if(response.isSuccessful()) {
                    MeUpdateResponse meUpdateResponse = response.body();
                    // TODO: we'll have to save details on our persistence

                    callback.onResponse(meUpdateResponse.message);
                }
                else {
                    // use Gson to deserialize the response
                    MeUpdateResponse meUpdateResponse = new Gson().fromJson(response.errorBody().charStream(), MeUpdateResponse.class);
                    callback.onFailure(response.code(), meUpdateResponse.message);
                }
            }

            @Override
            public void onFailure(Call<MeUpdateResponse> call, Throwable t) {
                callback.onFailure(-1, t.getMessage());
            }
        });
    }
}
