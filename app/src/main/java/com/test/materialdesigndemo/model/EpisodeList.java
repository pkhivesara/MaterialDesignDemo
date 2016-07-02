package com.test.materialdesigndemo.model;


import java.util.List;

public class EpisodeList {
    public String Title;
    public String Season;
    public List<Episodes> Episodes;

    public class Episodes{
        public String Title;
        public String Released;
        public String Episode;
        public String imdbRating;
        public String imdbID;

    }

}
