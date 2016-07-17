package com.test.materialdesigndemo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeData;
import com.test.materialdesigndemo.R;
import com.test.materialdesigndemo.network.RestClient;
import com.test.materialdesigndemo.presenters.CommonFragmentPresenter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.List;


public class DetailsFragment extends Fragment implements CommonFragmentPresenter.MainFragmentPresenterInterface {

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

    @OnClick(R.id.imdbRatingTextView)
    public void showDirectShareChooser() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, imdbRatingTextView.getText());
        startActivity(Intent.createChooser(i, ("test chooser")));
    }

    CommonFragmentPresenter commonFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        commonFragmentPresenter = new CommonFragmentPresenter(this);
        String episode = getArguments().getString(getString(R.string.episode));
        String title = getArguments().getString(getString(R.string.title));
        String season = getArguments().getString(getString(R.string.season));
        ButterKnife.bind(this, view);
        commonFragmentPresenter.getEpisodeDetails(title, season, episode);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        commonFragmentPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        commonFragmentPresenter.onStop();
    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public void setDataForRecyclerViewAdapter(List<EpisodeList.Episodes> episodes) {

    }

    @Override
    public void setIndividualEpisodeDetails(IndividualEpisodeData individualEpisodeData) {
                detailsCardDirectorTextView.setText(getString(R.string.director_name, individualEpisodeData.Director));
                detailsCardWritersTextView.setText(individualEpisodeData.Writer);
                detailsCardReleasedTextView.setText(getString(R.string.released_date, individualEpisodeData.Released));
                detailsCardPlotTextView.setText(individualEpisodeData.Plot);
                detailsCardTitleNameTextView.setText(individualEpisodeData.Title);
                imdbRatingTextView.setText(individualEpisodeData.imdbRating);
                Intent intent = new Intent(getString(R.string.load_complete_broadcast));
                intent.putExtra("url", individualEpisodeData.Poster);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}
