package com.test.materialdesigndemo.network;


import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeData;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ApiCall {



    @GET("?")
    Call<IndividualEpisodeData> getEpisodeDetail(@Query("t") String title, @Query("Season") String season, @Query("episode") String episodeNumber);

    @GET("?")
    Call<EpisodeList> getEpisodeList(@Query("t") String title, @Query("Season") String season);
}
