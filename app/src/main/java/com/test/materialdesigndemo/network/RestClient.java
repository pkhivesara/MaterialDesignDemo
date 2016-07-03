package com.test.materialdesigndemo.network;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.test.materialdesigndemo.Constants;
import com.test.materialdesigndemo.activities.MainActivity;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import com.squareup.okhttp.logging.HttpLoggingInterceptor.Level;


public class RestClient {

    private static ApiCall REST_CLIENT;


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
        logging.setLevel(Level.BODY);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        REST_CLIENT = retrofit.create(ApiCall.class);
    }
}