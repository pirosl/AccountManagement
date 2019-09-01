package com.lucianpiros.accountmanagement.restapi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * REST client implemented as Singleton
 */
public class RESTClient {

    private final static String BASE_URL = "https://dev.refinemirror.comapi/v1/";
    private static RESTClient restClientInstance = null;
    private Retrofit retrofit;

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
}
