package com.test.materialdesigndemo;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RestClient {

    private static ApiCall REST_CLIENT;
    private static String URL =
            "http://www.omdbapi.com/?t=";

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static ApiCall get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
//        RestAdapter.Builder builder = new RestAdapter.Builder()
//                .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL)
//                .setClient(new OkClient(new OkHttpClient()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        REST_CLIENT = retrofit.create(ApiCall.class);
    }
}