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
import java.util.concurrent.CountDownLatch;


public class ServiceHelper {
    //final CountDownLatch latch; //---->might be needed for retrofit async unit testing
    EventBus eventBus = EventBus.getDefault();
    public ServiceHelper() {
      //  latch = new CountDownLatch(1);
    }



    public void setEventBus(EventBus eventBus){
        this.eventBus = eventBus;
    }



    public void getIndividualEpisodeData(String show, String season) {
        Response<EpisodeList> episode = null;
        Call<EpisodeList> episodeDataList = RestClient.get().getEpisodeList(show, season);
//        try {
//            episode = episodeDataList.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if(episode.isSuccess()){
//            eventBus.post(new IndividualEpisodeResponseEvent(episode.body().Episodes));
//        }else{
//
//        }
        episodeDataList.enqueue(new Callback<EpisodeList>() {
            @Override
            public void onResponse(Response<EpisodeList> response, Retrofit retrofit) {
                /*data persistence should take place before sending out the eventbus message.
                 Passing the response object directly for sample purpose. */
                EventBus.getDefault().post(new IndividualEpisodeResponseEvent(response.body().Episodes));
               // latch.countDown(); //---->might be needed for retrofit async unit testing
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("%%%%%", "retrofit failure");
              //  latch.countDown(); //---->might be needed for retrofit async unit testing
            }
        });

//        try {
//            latch.await();
//        } catch (InterruptedException e) { //---->might be needed for retrofit async unit testing
//            e.printStackTrace();
//        }
    }
}
