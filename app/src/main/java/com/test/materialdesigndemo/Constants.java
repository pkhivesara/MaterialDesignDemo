package com.test.materialdesigndemo;


public interface Constants {

    String NAV_DRAWER_BROADCAST_RECEIVER = "NAV_DRAWER_ITEM_CLICKED";
    int READ_PHONE_STATE_PERMISSION_ID = 100;
    int TYPE_HEADER = 0;
    int TYPE_LIST = 1;
    int TAG_INBOX_STYLE_NOTIFICATION = 10;
    int TAG_BIG_TEXT_STYLE_NOTIFICATION = 11;
    int TAG_BIG_PICTURE_STYLE_NOTIFICATION = 12;
    int TAG_STACKED_STYLE_NOTIFICATION = 13;
    int ALARM_ID = 11;

}



/**
    private String makeServiceCall(String year) {
        StringBuffer response = null;
        URL url = null;
        try {
            url = new URL("http://www.omdbapi.com/?t=Friends&Season=" + year);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpsURLConnection.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }


    String year = params[0];
if (year.equalsIgnoreCase(getString(R.string.season_one))) {
        year = String.valueOf(year.charAt(year.length() - 1));
        }
        String response = makeServiceCall(year);
        List<String> responseList = new ArrayList<String>();
        try {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray searchArray = jsonObject.getJSONArray("Episodes");

        for (int i = 0; i < searchArray.length(); i++) {
        JSONObject mobileObject = searchArray.getJSONObject(i);
        String movieTitle = mobileObject.getString("Title");
        responseList.add(movieTitle);
        }


        } catch (JSONException e) {
        e.printStackTrace();
        }
        return responseList;



 IndividualEpisodeData individualEpisodeData = new IndividualEpisodeData();
 String episodeNumber = params[1];
 String title = params[2];
 String season = params[0];
 String response = makeServiceCall(season, episodeNumber, title);
 try {
 JSONObject jsonObject = new JSONObject(response);
 individualEpisodeData.title = jsonObject.getString("Title");
 individualEpisodeData.year = jsonObject.getString("Year");
 individualEpisodeData.rated = jsonObject.getString("Rated");
 individualEpisodeData.released = jsonObject.getString("Released");
 individualEpisodeData.season = jsonObject.getString("Season");
 individualEpisodeData.episode = jsonObject.getString("Episode");
 individualEpisodeData.runtime = jsonObject.getString("Runtime");
 individualEpisodeData.genre = jsonObject.getString("Genre");
 individualEpisodeData.director = jsonObject.getString("Director");
 individualEpisodeData.writer = jsonObject.getString("Writer");
 individualEpisodeData.actors = jsonObject.getString("Actors");
 individualEpisodeData.plot = jsonObject.getString("Plot");
 individualEpisodeData.imdbRating = jsonObject.getString("imdbRating");
 individualEpisodeData.poster = jsonObject.getString("Poster");
 } catch (JSONException e) {
 e.printStackTrace();
 Log.d("###", e.getMessage());

 }
 return individualEpisodeData;

 }

 }


 public String makeServiceCall(String season, String episodeNumber, String title) {
 if (season == null) {
 season = "1";
 }
 StringBuffer response = null;
 URL url = null;
 String urlValue;
 if (title.equalsIgnoreCase("friends")) {
 urlValue = "http://www.omdbapi.com/?t=Friends&Season=" + season + "&episode=" + episodeNumber;
 } else {
 urlValue = "http://www.omdbapi.com/?t=How&I&Met&Your&Mother&Season=" + season + "&episode=" + episodeNumber;
 }
 try {
 url = new URL(urlValue);
 } catch (MalformedURLException e) {
 e.printStackTrace();
 }
 try {
 HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
 httpsURLConnection.setRequestMethod("GET");
 BufferedReader in = new BufferedReader(
 new InputStreamReader(httpsURLConnection.getInputStream()));
 String inputLine;
 response = new StringBuffer();

 while ((inputLine = in.readLine()) != null) {
 response.append(inputLine);
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 return response.toString();
 }


*/