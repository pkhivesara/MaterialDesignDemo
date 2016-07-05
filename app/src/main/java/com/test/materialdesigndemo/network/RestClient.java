package com.test.materialdesigndemo.network;


import android.support.annotation.NonNull;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.test.materialdesigndemo.activities.MainActivity;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import com.squareup.okhttp.logging.HttpLoggingInterceptor.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


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
        Dispatcher dispatcher = new Dispatcher(newSynchronousExecutorService());
        httpClient.setDispatcher(dispatcher);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        REST_CLIENT = retrofit.create(ApiCall.class);
    }
    public static ExecutorService newSynchronousExecutorService() {
        return new AbstractExecutorService() {
            private boolean shutingDown = false;
            private boolean terminated = false;

            @Override
            public void shutdown() {
                this.shutingDown = true;
                this.terminated = true;
            }

            @NonNull
            @Override
            public List<Runnable> shutdownNow() {
                return new ArrayList<>();
            }

            @Override
            public boolean isShutdown() {
                return this.shutingDown;
            }

            @Override
            public boolean isTerminated() {
                return this.terminated;
            }

            @Override
            public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };
    }
}
