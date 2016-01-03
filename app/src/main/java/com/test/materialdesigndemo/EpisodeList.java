package com.test.materialdesigndemo;


import java.util.List;

public class EpisodeList {
    public String Title;
    public String Season;
    public List<Episodes> Episodes;

    public class Episodes{
        String Title;
        String Released;
        String Episode;
        String imdbRating;
        String imdbID;

    }

}
