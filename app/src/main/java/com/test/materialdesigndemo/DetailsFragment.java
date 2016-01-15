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
        String episode = getArguments().getString(getString(R.string.episode));
        String title = getArguments().getString(getString(R.string.title));
        String season = getArguments().getString(getString(R.string.season));
        ButterKnife.bind(this, view);
        Call<IndividualEpisodeData> episodeDataList = RestClient.get().getEpisodeDetail(title, season, episode);
        episodeDataList.enqueue(new Callback<IndividualEpisodeData>() {
            @Override
            public void onResponse(Response<IndividualEpisodeData> response, Retrofit retrofit) {
                detailsCardDirectorTextView.setText(getString(R.string.director_name, response.body().Director));
                detailsCardWritersTextView.setText(response.body().Writer);
                detailsCardReleasedTextView.setText(getString(R.string.released_date, response.body().Released));
                detailsCardPlotTextView.setText(response.body().Plot);
                detailsCardTitleNameTextView.setText(response.body().Title);
                imdbRatingTextView.setText(response.body().imdbRating);
                Intent intent = new Intent(getString(R.string.load_complete_broadcast));
                intent.putExtra("url", response.body().Poster);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        imdbRatingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_SEND);

                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "test");
                i.putExtra(Intent.EXTRA_TEXT, "test");

                startActivity(Intent.createChooser(i,("test chooser")));
            }
        });
        return view;

    }


    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

}
