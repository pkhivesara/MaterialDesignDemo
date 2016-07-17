package com.test.materialdesigndemo.model;

import java.util.List;

/**
 * Created by Pratik on 7/1/16.
 */
public class IndividualEpisodeResponseEvent {

    private List<EpisodeList.Episodes> episodesList;
    private IndividualEpisodeData individualEpisodeData;

    public IndividualEpisodeResponseEvent(List<EpisodeList.Episodes> episodesList){
        this.episodesList = episodesList;

    }

    public IndividualEpisodeResponseEvent(IndividualEpisodeData individualEpisodeData){
        this.individualEpisodeData = individualEpisodeData;

    }

    public IndividualEpisodeData getIndividualEpisodeData(){
        return individualEpisodeData;
    }

    public List<EpisodeList.Episodes> getEpisodesList(){
        return episodesList;
    }

}
