package com.test.materialdesigndemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class DetailsFragment extends Fragment {

    TextView raceNumberTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        String episode = getArguments().getString("episode");
        String title = getArguments().getString("title");
        raceNumberTextView = (TextView) view.findViewById(R.id.raceNumberTextView);
        new GetIndividualEpisodeData().execute(episode,title);
        raceNumberTextView.setText(episode);
        return view;

    }


    public class GetIndividualEpisodeData extends AsyncTask<String, Void, IndividualEpisodeData> {

        @Override
        protected void onPostExecute(IndividualEpisodeData individualEpisodeData) {
            super.onPostExecute(individualEpisodeData);
            raceNumberTextView.setText(individualEpisodeData.director + individualEpisodeData.title);

        }

        @Override
        protected IndividualEpisodeData doInBackground(String... params) {
            IndividualEpisodeData individualEpisodeData = new IndividualEpisodeData();
            String episodeNumber = params[0];
            String title = params[1];
            String response = makeServiceCall(episodeNumber, title);
            try {
                JSONObject jsonObject = new JSONObject(response);
                individualEpisodeData.title = jsonObject.getString("Title");
                individualEpisodeData.released = jsonObject.getString("Released");
                individualEpisodeData.director = jsonObject.getString("Director");
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return individualEpisodeData;

        }

    }


    public String makeServiceCall(String episodeNumber, String title) {

        StringBuffer response = null;
        URL url = null;
        String urlValue;
        if (title.equalsIgnoreCase("friends")) {
            urlValue = "http://www.omdbapi.com/?t=Friends&Season=1&episode=" + episodeNumber;
        } else {
            urlValue = "http://www.omdbapi.com/?t=How&I&Met&Your&Mother&Season=1&episode=" + episodeNumber;
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
