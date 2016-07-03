package com.test.materialdesigndemo;

import android.util.Log;
import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeResponseEvent;
import com.test.materialdesigndemo.network.RestClient;
import org.greenrobot.eventbus.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.IOException;
import java.util.List;


public class ServiceHelper {

    public ServiceHelper() {
    }


    public void getIndividualEpisodeData(String show, String season) {

        Call<EpisodeList> episodeDataList = RestClient.get().getEpisodeList(show, season);
        Response<EpisodeList>  episodeListResponse = null;
        try {
            episodeListResponse = episodeDataList.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(episodeListResponse.isSuccess()){
            EventBus.getDefault().post(new IndividualEpisodeResponseEvent(episodeListResponse.body().Episodes));
        }
//        episodeDataList.enqueue(new Callback<EpisodeList>() {
//            @Override
//            public void onResponse(Response<EpisodeList> response, Retrofit retrofit) {
//                /*data persistence should take place before sending out the eventbus message.
//                 Passing the response object directly for sample purpose. */
//                EventBus.getDefault().post(new IndividualEpisodeResponseEvent(response.body().Episodes));
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d("%%%%%", "retrofit failure");
//            }
//        });
    }
}
