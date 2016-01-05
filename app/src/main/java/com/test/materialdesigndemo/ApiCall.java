package com.test.materialdesigndemo;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ApiCall {



    @GET("?")
    Call<IndividualEpisodeData> getEpisodeDetail(@Query("t") String title, @Query("Season") String season, @Query("episode") String episodeNumber);

    @GET("?")
    Call<EpisodeList> getEpisodeList(@Query("t") String title, @Query("Season") String season);
}
