package com.test.materialdesigndemo;


import retrofit.Call;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ApiCall {



    @GET("{title}&Season ={season}&episode={episodeNumber}")
    public void getEpisodeDetail(@Path("title") String title, @Path("season") String season, @Path("episodeNumber") String episodeNumber,
                                 Callback<IndividualEpisodeData> callback);

    @GET("{title}={season}")
    Call<EpisodeList> getEpisodeList(@Path("title") String title, @Path("season")String season);
}
