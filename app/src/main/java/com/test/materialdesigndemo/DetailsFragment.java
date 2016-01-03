package com.test.materialdesigndemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class DetailsFragment extends Fragment {

    @Bind(R.id.detailsCardWritersTextView)
    TextView detailsCardWritersTextView;

    @Bind(R.id.detailsCardDirectorTextView)
    TextView detailsCardDirectorTextView;

    @Bind(R.id.detailsCardReleasedTextView)
    TextView detailsCardReleasedTextView;

    @Bind(R.id.detailsCardPlotTextView)
    TextView detailsCardPlotTextView;

    @Bind(R.id.detailsCardTitleNameTextView)
    TextView detailsCardTitleNameTextView;

    @Bind(R.id.imdbRatingTextView)
    TextView imdbRatingTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        String episode = getArguments().getString("episode");
        String title = getArguments().getString("title");
        String season = getArguments().getString("season");
        ButterKnife.bind(this, view);
        Call<IndividualEpisodeData> episodeDataList = RestClient.get().getEpisodeDetail(title, season, episode);
        episodeDataList.enqueue(new Callback<IndividualEpisodeData>() {
            @Override
            public void onResponse(Response<IndividualEpisodeData> response, Retrofit retrofit) {
                Log.d("%%%", "success");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("%%%", "fail");
            }
        });

        //new GetIndividualEpisodeData().execute(season, episode, title);
        return view;

    }


    public class GetIndividualEpisodeData extends AsyncTask<String, Void, IndividualEpisodeData> {
        @Override
        protected void onPostExecute(IndividualEpisodeData individualEpisodeData) {
            super.onPostExecute(individualEpisodeData);
            detailsCardDirectorTextView.setText(getString(R.string.director_name, individualEpisodeData.director));
            detailsCardWritersTextView.setText(individualEpisodeData.writer);
            detailsCardReleasedTextView.setText(getString(R.string.released_date, individualEpisodeData.released));
            detailsCardPlotTextView.setText(individualEpisodeData.plot);
            detailsCardTitleNameTextView.setText(individualEpisodeData.title);
            imdbRatingTextView.setText(individualEpisodeData.imdbRating);
            Intent intent = new Intent("LOAD_COMPLETE");
            intent.putExtra("url", individualEpisodeData.poster);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }

        @Override
        protected IndividualEpisodeData doInBackground(String... params) {
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

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

}
