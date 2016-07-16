package com.test.materialdesigndemo;

import android.util.Log;
import com.test.materialdesigndemo.activities.MainActivity;
import com.test.materialdesigndemo.dagger.RetrofitComponent;
import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeResponseEvent;
import com.test.materialdesigndemo.network.ApiCall;
import com.test.materialdesigndemo.network.RestClient;
import org.greenrobot.eventbus.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class ServiceHelper {
    EventBus eventBus = EventBus.getDefault();

    @Inject
    @Named("RealService")
    ApiCall retrofit;

    public ServiceHelper() {
        MainActivity.retrofitComponent.inject(this);
    }


    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }


    public void getIndividualEpisodeData(String show, String season) {
        Call<EpisodeList> episodeDataList = retrofit.getEpisodeList(show, season);
        episodeDataList.enqueue(new Callback<EpisodeList>() {
            @Override
            public void onResponse(Response<EpisodeList> response, Retrofit retrofit) {
                /*data persistence should take place before sending out the eventbus message.
                 Passing the response object directly for sample purpose. */
                if (response.errorBody() != null) {
                    Log.d("%%%%%", response.errorBody().toString());
                } else {
                    eventBus.post(new IndividualEpisodeResponseEvent(response.body().Episodes));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("%%%%%", "retrofit failure");
            }
        });

    }
}
