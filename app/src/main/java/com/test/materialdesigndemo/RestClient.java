package com.test.materialdesigndemo;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import com.squareup.okhttp.logging.HttpLoggingInterceptor.Level;


public class RestClient {

    private static ApiCall REST_CLIENT;
    private static String URL =
            "http://www.omdbapi.com/";

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static ApiCall get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BASIC);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        REST_CLIENT = retrofit.create(ApiCall.class);
    }
}