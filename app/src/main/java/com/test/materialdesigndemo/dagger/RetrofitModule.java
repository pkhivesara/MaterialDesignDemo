package com.test.materialdesigndemo.dagger;

import android.support.annotation.NonNull;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.test.materialdesigndemo.activities.MainActivity;
import com.test.materialdesigndemo.network.ApiCall;
import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pratik on 7/16/16.
 */

@Module
public class RetrofitModule {
    private ApiCall retrofit;

    @Provides
    @Named("RealService")
    @Singleton
    ApiCall provideRetrofit(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);

       retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiCall.class);

        return retrofit;
    }

    @Provides
    @Singleton
    ApiCall provideRetrofitForTestingPurpose(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);
        Dispatcher dispatcher = new Dispatcher(newSynchronousExecutorService());
        httpClient.setDispatcher(dispatcher);

         retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiCall.class);

        return retrofit;
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
